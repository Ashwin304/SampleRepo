package com.example.sampleloginapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.sampleloginapp.io.db.NewsData
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.model.Article
import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.network.NewsApi
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.utils.Coroutines
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class NewsViewModel(val repository: NewsRepository): ViewModel() {
    private val TAG = "NewsViewModel"

    val newsResponse = MediatorLiveData<NewsResponse>()
    val _news: LiveData<NewsResponse>
        get() =  newsResponse
    val favouriteNews = MutableLiveData<List<NewsData>>()

    val sources= "techcrunch"
    val apiKey="9356e9605ff54a65894d7370e5605161"

    fun getNews(){
       val news = LiveDataReactiveStreams.fromPublisher(repository.getNews(sources, apiKey).subscribeOn(Schedulers.io()))

        newsResponse.addSource(news, object: Observer<NewsResponse> {
            override fun onChanged(t: NewsResponse?) {
                newsResponse.value = t

            }
        })
    }

    fun saveFavourite(article: Article){
        Coroutines.io {
            repository.saveFavourite(article)

        }

    }

    fun deleteAllFavourite(){
        Coroutines.io {
            repository.deleteAllFavourite()
        }
    }

    fun getFavouriteNews(){
        Coroutines.io {
            favouriteNews.postValue(repository.getFavouriteNews())
        }

    }



}