package com.example.sampleloginapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private const val GOOGLE_USER_ID = "userId"


class SharedPreference(context: Context)  {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveUserId(token: String?){
        val editor:SharedPreferences.Editor =  preference.edit()
        editor.putString(GOOGLE_USER_ID, token)
        editor.apply()
        editor.commit()
    }

    fun getUserId(): String? {
        return preference.getString(GOOGLE_USER_ID, null)

    }

}