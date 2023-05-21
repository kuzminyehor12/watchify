package com.android.watchify.ui.fragments.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.watchify.R
import com.android.watchify.databinding.FragmentDetailsBinding
import com.android.watchify.utils.Constants
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val bundleArgs: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleArg = bundleArgs.article

        articleArg.urlToImage.let {
            Glide.with(this).load(it).into(binding.articleDetailedImage)
        }

        binding.articleDetailedTitle.text = articleArg.title
        binding.articleDescription.text = articleArg.description

        binding.goBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.articleLinkButton.setOnClickListener {
            try {
                Intent()
                    .setAction(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_BROWSABLE)
                    .setData(Uri.parse(takeIf { URLUtil.isValidUrl(articleArg.url) }
                        ?.let {
                        articleArg.url
                        } ?: Constants.DEFAULT_LINK))
                        .let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }
            } catch(e: Exception) {
                Toast.makeText(
                    context,
                    "The device doesn`t have any instruments to visit this link!",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}