package com.android.watchify.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.watchify.data.dao.ArticleDAO
import com.android.watchify.models.Article
import java.util.concurrent.locks.Lock

@Database(entities = [Article::class], version = 1, exportSchema = true)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDAO
}