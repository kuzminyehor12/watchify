package com.android.watchify.di

import android.content.Context
import androidx.room.Room
import com.android.watchify.data.api.NewsApi
import com.android.watchify.data.dao.ArticleDAO
import com.android.watchify.data.db.ArticleDatabase
import com.android.watchify.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val API_KEY = "apiKey"
    private const val DATABASE_NAME = "watchify_db"

    @Provides
    fun baseUrl() = Constants.BASE_URL

    @Provides
    fun logging() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun okHttpClient() = OkHttpClient.Builder()
        .addInterceptor(logging())
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url.newBuilder()
                .addQueryParameter(API_KEY, Constants.API_KEY)
                .build()
            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()
            chain.proceed(modifiedRequest)
        }
        .build()

    @Provides
    @Singleton
    fun retrofit(): NewsApi =
        Retrofit.Builder()
            .baseUrl(baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
            .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun db(@ApplicationContext context: Context): ArticleDatabase =
            Room
            .databaseBuilder(context,ArticleDatabase::class.java, DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun dao(database: ArticleDatabase) = database.getArticleDao()
}