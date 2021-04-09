package com.example.sampleloginapp.io.db

import androidx.room.*


@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(article: List<Article>): List<Long>

    @Query("SELECT * FROM News_Table")
    fun getAllNews(): List<Article>

    @Update
    fun updateFavourite(article: Article)

    @Update
    fun deleteFavourite(article: Article)


    @Query("DELETE FROM News_Table")
    fun deleteAllFavourite()

}

