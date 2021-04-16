package com.example.sampleloginapp.io.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context): Interceptor {


    val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {

        Log.d("interceptor", chain.toString())
            if(!hasInternetConnection())
                throw NoInternetException(Constants().INTERNT_CONNECTION)
            return chain.proceed(chain.request())


    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         connectivityManager.activeNetworkInfo.also{
             return it != null && it.isConnected
         }

    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}