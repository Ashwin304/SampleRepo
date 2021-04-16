package com.example.sampleloginapp.io.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sampleloginapp.utils.Constants




@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)

abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao

    companion object {

        @Volatile
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it

            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder<NewsDatabase>(
                context.applicationContext,
                NewsDatabase::class.java,
                    Constants().DATABASE_NAME
            ).build()
    }


}