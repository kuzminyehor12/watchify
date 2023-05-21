package com.android.watchify.data.repos

import com.android.watchify.data.api.NewsApi
import com.android.watchify.data.dao.ArticleDAO
import com.android.watchify.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDAO: ArticleDAO) {
    suspend fun getNews(country: String, page: Int) = newsApi.getHeadlines(country, page)
    suspend fun searchNews(searchText: String, page: Int) = newsApi.getEverything(searchText, page)
    suspend fun getFavourites() = articleDAO.getAll()
    suspend fun addToFavourites(article: Article) = articleDAO.insert(article)
    suspend fun removeFromFavourites(article: Article) = articleDAO.delete(article)
}