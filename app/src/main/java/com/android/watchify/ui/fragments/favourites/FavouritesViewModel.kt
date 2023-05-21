package com.android.watchify.ui.fragments.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.watchify.data.repos.NewsRepository
import com.android.watchify.models.Article
import com.android.watchify.models.NewsResponse
import com.android.watchify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {
    val favourites: MutableLiveData<List<Article>> = MutableLiveData()

    init {
        getFavourites()
    }

    private fun getFavourites() = viewModelScope.launch(Dispatchers.IO) {
        val favouritesList = newsRepository.getFavourites()
        favourites.postValue(favouritesList)
    }

    fun removeArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.removeFromFavourites(article)
    }
}