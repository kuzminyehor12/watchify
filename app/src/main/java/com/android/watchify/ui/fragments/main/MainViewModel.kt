package com.android.watchify.ui.fragments.main

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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {
    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var page = 1

    init {
        getNews()
    }

    private fun getNews(country: String = "ua") = viewModelScope.launch {
        news.postValue(Resource.InProgress())
        val response = newsRepository.getNews(country, page)
        if (response.isSuccessful){
            response.body().let {
                news.postValue(Resource.Success(it))
            }
        } else {
            news.postValue(Resource.Failure(response.message()))
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.addToFavourites(article)
    }
}