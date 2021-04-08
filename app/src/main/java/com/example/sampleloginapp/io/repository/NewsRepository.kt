package com.example.sampleloginapp.io.repository

import com.example.sampleloginapp.io.db.NewsData
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.model.Article
import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.network.NewsApi
import io.reactivex.Flowable

class NewsRepository(val newsApi: NewsApi,val db: NewsDatabase) {

    fun getNews(sources: String,  apiKey: String): Flowable<NewsResponse> {

       return  newsApi.getNews(sources, apiKey)

    }

    fun saveFavourite(article: Article){
        db.getNewsDao().saveFavourite(NewsData(article.url!!))
    }

    fun getFavouriteNews(): List<NewsData>?{
        return db.getNewsDao().getFavouriteNews()
    }

    fun deleteAllFavourite(){
        db.getNewsDao().deleteAllFavourite()

    }
}