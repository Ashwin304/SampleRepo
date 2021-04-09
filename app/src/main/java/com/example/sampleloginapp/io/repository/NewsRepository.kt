package com.example.sampleloginapp.io.repository

import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.network.NewsApi
import io.reactivex.Flowable

class NewsRepository(val newsApi: NewsApi,val db: NewsDatabase) {

    fun getNews(sources: String,  apiKey: String): Flowable<NewsResponse> {
       return  newsApi.getNews(sources, apiKey)

    }

    fun saveAll(news: List<Article>): List<Long> {
       return db.getNewsDao().saveAll(news)
    }

    fun getAllNews(): List<Article> {
        return db.getNewsDao().getAllNews()
    }

    fun updateFavourite(article: Article){
        db.getNewsDao().updateFavourite(article)
    }

    fun deleteFavourite(article: Article){
        db.getNewsDao().deleteFavourite(article)

    }

    fun deleteAllFavourite(){
        db.getNewsDao().deleteAllFavourite()

    }
}