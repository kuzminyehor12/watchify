package com.android.watchify.ui.fragments.favourites

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
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.watchify.R
import com.android.watchify.databinding.FragmentDetailsBinding
import com.android.watchify.databinding.FragmentFavouritesBinding
import com.android.watchify.ui.adapters.FavouritesAdapter
import com.android.watchify.ui.adapters.NewsAdapter
import com.android.watchify.ui.fragments.details.DetailsViewModel
import com.android.watchify.utils.Constants
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel by viewModels<FavouritesViewModel>()
    private lateinit var adapter: FavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavouritesAdapter()
        binding.favouritesList.adapter = adapter
        binding.favouritesList.layoutManager = LinearLayoutManager(activity)

        adapter.setOnItemClickListener {
            val bundle = bundleOf(Constants.ARTICLE_KEY to it)
            view.findNavController().navigate(R.id.action_favouritesFragment_to_detailsFragment, bundle)
        }

        adapter.setOnLikeListener {
            try {
                viewModel.removeArticle(it)
                Toast.makeText(
                    context,
                    "Article has been removed from favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception){
                Toast.makeText(
                    context,
                    "Article cannot be removed from favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.favourites.observe(viewLifecycleOwner) {
            adapter.differ.submitList(it)
        }
    }
}