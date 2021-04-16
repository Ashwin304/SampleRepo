package com.example.sampleloginapp.io.repository

import android.content.Intent
import android.util.Log
import com.example.sampleloginapp.io.model.User
import com.example.sampleloginapp.utils.Constants
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginRepository() {

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    val mGoogleSignClient by lazy {
        GoogleSignIn.getClient(getApplicationContext(), gso)
    }

    

    fun googleLogin() = mGoogleSignClient.signInIntent

    fun handleGoogleSignInResult(data: Intent?): User? {

        val account = GoogleSignIn.getSignedInAccountFromIntent(data)

     if(account.exception == null) {
         val user = User(account.result?.id, account.result?.displayName, account.result?.email)
         return user
     }else{
         return null
     }

    }

    val loginManager: LoginManager = LoginManager.getInstance()
    val mCallbackManager = CallbackManager.Factory.create()

    private val mFacebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {

            Log.d(Constants().TAG, AccessToken.getCurrentAccessToken().toString())

        }

        override fun onCancel() {
            Log.d(Constants().TAG, Constants().CANCEL)
        }

        override fun onError(error: FacebookException?) {
             Log.d(Constants().TAG, error.toString())
        }
    }

    init {
        loginManager.registerCallback(mCallbackManager, mFacebookCallback)
    }



}