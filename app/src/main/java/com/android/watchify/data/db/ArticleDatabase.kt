package com.android.watchify.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.watchify.data.dao.ArticleDAO
import java.util.concurrent.locks.Lock

abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDAO

    companion object {
        private const val DATABASE_NAME = "watchify_db"
        private var instance: ArticleDatabase? = null

        private fun createDatabase(context: Context): ArticleDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}