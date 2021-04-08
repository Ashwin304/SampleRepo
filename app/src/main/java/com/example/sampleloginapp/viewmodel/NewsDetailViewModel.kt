package com.example.sampleloginapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleloginapp.io.model.Article

class NewsDetailViewModel(): ViewModel() {

    val newsDetail = MutableLiveData<Article>()
    val url = MutableLiveData<String>()


    fun getNewsDetail(article: Article?){

        newsDetail.value = article
    }

    fun onClickReadMore(){
        url.value = newsDetail.value?.url.toString()
    }

}