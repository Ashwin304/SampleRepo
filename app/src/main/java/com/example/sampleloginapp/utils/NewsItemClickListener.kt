package com.example.sampleloginapp.utils

import com.example.sampleloginapp.io.db.Article

interface NewsItemClickeListener{
    fun onNewsItemClicked(article: Article)
    fun onFavouriteClicked(article: Article, favourite: Boolean)
}