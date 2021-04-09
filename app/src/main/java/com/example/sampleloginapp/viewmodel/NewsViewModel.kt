package com.example.sampleloginapp.viewmodel

import androidx.lifecycle.*
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.utils.API_KEY
import com.example.sampleloginapp.utils.Coroutines
import com.example.sampleloginapp.utils.SOURCE
import io.reactivex.schedulers.Schedulers

class NewsViewModel(val repository: NewsRepository): ViewModel() {


    val newsResponse = MediatorLiveData<NewsResponse>()
    val _news: LiveData<NewsResponse>
        get() =  newsResponse
    val favouriteNews = MutableLiveData<List<Article>>()

    val returnValue = MutableLiveData<List<Long>>()



    fun getNews(){
       val news = LiveDataReactiveStreams.fromPublisher(repository.getNews(SOURCE, API_KEY).subscribeOn(Schedulers.io()))

        newsResponse.addSource(news, object: Observer<NewsResponse> {
            override fun onChanged(t: NewsResponse?) {
                newsResponse.value = t

            }
        })
    }

    fun saveAll(news: List<Article>){
        Coroutines.io {
            val long = repository.saveAll(news)
            returnValue.postValue(long)

        }

    }
    fun getAllNews(){
        Coroutines.io {
            favouriteNews.postValue(repository.getAllNews())
        }

    }


    fun updateFavourite(article: Article){
        Coroutines.io {
            repository.updateFavourite(article)

        }

    }

    fun deleteFavourite(article: Article){
        Coroutines.io {
            repository.deleteFavourite(article)
        }
    }

    fun deleteAllFavourite(){
        Coroutines.io {
            repository.deleteAllFavourite()
        }
    }


}