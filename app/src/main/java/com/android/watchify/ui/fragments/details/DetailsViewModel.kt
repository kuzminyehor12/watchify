package com.android.watchify.ui.fragments.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.watchify.data.repos.NewsRepository
import com.android.watchify.models.Article
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    fun saveArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseAuth.getInstance().currentUser?.let {
            newsRepository.addToFavourites(article, it.uid)
        }
    }
}