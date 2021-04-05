package com.example.sampleloginapp.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleloginapp.model.User
import com.example.sampleloginapp.repository.LoginRepository
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

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

}