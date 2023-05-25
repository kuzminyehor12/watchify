package com.android.watchify.ui.fragments.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.watchify.data.repos.NewsRepository
import com.android.watchify.models.Article
import com.android.watchify.models.NewsResponse
import com.android.watchify.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {
    val favourites: MutableLiveData<List<Article>> = newsRepository.favourites

    init {
        getFavourites()
    }

    private fun getFavourites() = viewModelScope.launch(Dispatchers.IO) {
        FirebaseAuth.getInstance().currentUser?.let {
            val favouritesList = newsRepository.getFavourites(it.uid)
            withContext(Dispatchers.Main){
                favourites.postValue(favouritesList)
            }
        }
    }

    fun removeArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseAuth.getInstance().currentUser?.let {
            newsRepository.removeFromFavourites(article, it.uid)
        }
    }
}
