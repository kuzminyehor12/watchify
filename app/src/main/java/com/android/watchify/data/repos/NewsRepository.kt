package com.android.watchify.data.repos

import com.android.watchify.data.api.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi) {
    suspend fun getNews(country: String, page: Int) = newsApi.getHeadlines(country, page)
    suspend fun searchNews(searchText: String, page: Int) = newsApi.getEverything(searchText, page)
}