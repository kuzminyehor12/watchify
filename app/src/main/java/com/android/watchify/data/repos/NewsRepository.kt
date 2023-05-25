package com.android.watchify.data.repos

import androidx.lifecycle.MutableLiveData
import com.android.watchify.data.api.NewsApi
import com.android.watchify.data.dao.ArticleDAO
import com.android.watchify.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDAO: ArticleDAO) {
    val favourites: MutableLiveData<List<Article>> = MutableLiveData()
    suspend fun getNews(country: String, page: Int) = newsApi.getHeadlines(country, page)
    suspend fun searchNews(searchText: String, page: Int) = newsApi.getEverything(searchText, page)
    suspend fun getFavourites(uid: String) = articleDAO.getAllByUid(uid)
    suspend fun addToFavourites(article: Article, uid: String){
        articleDAO.insert(article)
        val favouritesList = getFavourites(uid)
        favourites.postValue(favouritesList)
    }
    suspend fun removeFromFavourites(article: Article, uid: String){
        articleDAO.delete(article)
        val favouritesList = getFavourites(uid)
        favourites.postValue(favouritesList)
    }
}