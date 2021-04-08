package com.example.sampleloginapp.io.network

import com.example.sampleloginapp.io.model.NewsResponse
import com.example.sampleloginapp.io.model.User
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    fun getNews(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String
    ): Flowable<NewsResponse>




    companion object{
        operator fun invoke(interceptor: NetworkConnectionInterceptor): NewsApi {
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(" https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NewsApi::class.java)

        }
    }
}