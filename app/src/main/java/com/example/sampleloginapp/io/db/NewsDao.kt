package com.example.sampleloginapp.io.db

import androidx.room.*
import com.example.sampleloginapp.io.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFavourite(article: NewsData)

    @Query("SELECT * FROM News_Table")
    fun getFavouriteNews(): List<NewsData>

    @Delete
    fun deleteFavourite(url: NewsData)

    @Query("DELETE FROM News_Table")
    fun deleteAllFavourite()

}

