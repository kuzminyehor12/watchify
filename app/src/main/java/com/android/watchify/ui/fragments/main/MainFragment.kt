package com.android.watchify.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.watchify.R
import com.android.watchify.databinding.FragmentMainBinding
import com.android.watchify.ui.adapters.NewsAdapter
import com.android.watchify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    companion object {
        private const val ARTICLE: String = "article"
    }

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

        adapter.setOnItemClickListener {
            val bundle = bundleOf(ARTICLE to it)
            view.findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
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