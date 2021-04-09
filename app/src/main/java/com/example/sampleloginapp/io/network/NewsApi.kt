package com.example.sampleloginapp.io.network


import com.example.sampleloginapp.io.model.NewsResponse

import com.example.sampleloginapp.utils.API
import com.example.sampleloginapp.utils.BASE_URL
import com.example.sampleloginapp.utils.SOURCES
import com.example.sampleloginapp.utils.SUB_URL
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET(SUB_URL)
    fun getNews(
            @Query(SOURCES) sources: String,
            @Query(API) apiKey: String
    ): Flowable<NewsResponse>




    companion object{
        operator fun invoke(interceptor: NetworkConnectionInterceptor): NewsApi {
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NewsApi::class.java)

        }
    }
}