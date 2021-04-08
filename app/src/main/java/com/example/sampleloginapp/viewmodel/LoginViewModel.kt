package com.example.sampleloginapp.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleloginapp.io.model.User
import com.example.sampleloginapp.io.repository.LoginRepository
import com.facebook.*
import com.facebook.login.LoginManager

class LoginViewModel(val repository: LoginRepository): ViewModel() {


    val id = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    private val TAG = "LoginViewModel"


    fun googleLogin(): Intent{
        return repository.googleLogin()
    }

    fun handleGoogleSignIn(data: Intent?): User{
     return repository.handleGoogleSignInResult(data)
    }

    fun facebookLoginManger(): LoginManager{
        return repository.loginManager
    }

    fun getCallbackManager(): CallbackManager? {
        return repository.mCallbackManager
    }


    fun Logout(){

        repository.mGoogleSignClient.signOut()
    }


}