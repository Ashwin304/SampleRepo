package com.example.sampleloginapp.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleloginapp.model.User
import com.example.sampleloginapp.repository.LoginRepository
import com.facebook.*
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

    fun facebookLoginManger(): LoginManager{
        return repository.loginManager
    }

    fun getCallbackManager(): CallbackManager? {
        return repository.mCallbackManager
    }


    fun Logout(){
        LoginManager.getInstance().logOut()
        repository.mGoogleSignClient.signOut()
    }


}