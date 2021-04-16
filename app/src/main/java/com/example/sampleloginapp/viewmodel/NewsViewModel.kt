package com.example.sampleloginapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.sampleloginapp.NewsApplication
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.utils.ApiException
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.utils.Coroutines
import com.example.sampleloginapp.utils.NoInternetException
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Response

class NewsViewModel(val app: Application, val repository: NewsRepository): AndroidViewModel(app) {


    val newsResponse = MediatorLiveData<NewsResponse>()
    val _news: LiveData<NewsResponse>
        get() =  newsResponse
    val favouriteNews = MutableLiveData<List<Article>>()

    val returnValue = MutableLiveData<List<Long>>()

    fun getNews(){
       when(hasInternetConnection()){
           true -> {
               val news = LiveDataReactiveStreams.fromPublisher(repository.getNews(Constants().SOURCE, Constants().API_KEY).subscribeOn(Schedulers.io()))

               newsResponse.addSource(news, object: Observer<NewsResponse> {
                   override fun onChanged(t: NewsResponse?) {
                       newsResponse.value = t

                   }
               })
           }
           else -> Toast.makeText(app.applicationContext, Constants().INTERNT_CONNECTION, Toast.LENGTH_SHORT).show()

       }

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


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }



}