package com.android.watchify.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.watchify.R
import com.android.watchify.data.repos.NewsRepository
import com.android.watchify.databinding.FragmentMainBinding
import com.android.watchify.ui.adapters.NewsAdapter
import com.android.watchify.utils.Constants
import com.android.watchify.utils.Resource
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter()
        binding.newsList.adapter = adapter
        binding.newsList.layoutManager = LinearLayoutManager(activity)
        // AuthUI.getInstance().signOut(requireContext())

        adapter.setOnItemClickListener {
            val bundle = bundleOf(Constants.ARTICLE_KEY to it)
            view.findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }

        adapter.setOnLikeListener {
            try {
                viewModel.saveArticle(it)
                Toast.makeText(
                    context,
                    "Article has been added to favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception){
                Toast.makeText(
                    context,
                    "Article cannot be added into favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.news.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    binding.mainProgressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Failure -> {
                    binding.mainProgressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        Log.e("failure", "MainFragment error: ${it}")
                    }
                }
                is Resource.InProgress -> {
                    binding.mainProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}