package com.android.watchify.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.watchify.models.Article

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM articles")
    suspend fun getAll(): List<Article>

    @Query("SELECT * FROM articles WHERE uid=:uid")
    suspend fun getAllByUid(uid: String): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)
}