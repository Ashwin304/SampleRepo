package com.example.sampleloginapp.io.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sampleloginapp.utils.Constants




@Database(
    entities = [Article::class],
    version = 1
)

abstract class NewsDatabase(): RoomDatabase() {
    abstract fun getNewsDao(): NewsDao

    companion object {

        @Volatile
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context, userId: String) =
            buildDatabase(context, userId)

        private fun buildDatabase(context: Context, userId: String) =
            Room.databaseBuilder<NewsDatabase>(
                context.applicationContext,
                NewsDatabase::class.java,
                    "${Constants().DATABASE_NAME}_${userId}.db"
            ).build()
    }


}