package com.android.watchify.ui.fragments.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.watchify.R
import com.android.watchify.databinding.FragmentSearchBinding
import com.android.watchify.ui.activities.MainActivity
import com.android.watchify.ui.adapters.NewsAdapter
import com.android.watchify.ui.fragments.main.MainFragment
import com.android.watchify.ui.fragments.main.MainViewModel
import com.android.watchify.utils.Constants
import com.android.watchify.utils.Resource
import com.android.watchify.utils.relaunch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter()
        binding.searchList.adapter = adapter
        binding.searchList.layoutManager = LinearLayoutManager(activity)

        adapter.setOnItemClickListener {
            val bundle = bundleOf(Constants.ARTICLE_KEY to it)
            view.findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }

        adapter.setOnLikeListener {
            try {
                viewModel.saveArticle(it)
                Toast.makeText(
                    context,
                    "Article has been added to favourites",
                    Toast.LENGTH_SHORT)
                    .show()
                relaunch()
            } catch (e: Exception){
                Toast.makeText(
                    context,
                    "Article cannot be added into favourites",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.searchText.addTextChangedListener { text: Editable? ->
            MainScope().launch {
                delay(Constants.SEARCH_DELAY)
                text?.let {
                    if (it.toString().isNotEmpty() && it.length > 2) {
                        viewModel.getNewsBySearchText(it.toString())
                    }
                }
            }
        }

        viewModel.news.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Failure -> {
                    binding.searchProgressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        Log.e("failure", "MainFragment error: ${it}")
                    }
                }
                is Resource.InProgress -> {
                    binding.searchProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}