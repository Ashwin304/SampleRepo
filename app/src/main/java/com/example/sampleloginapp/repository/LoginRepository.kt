package com.example.sampleloginapp.repository

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewModelScope
import com.example.sampleloginapp.model.User
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

class LoginRepository {
    private val TAG = "LoginRepository"

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    val mGoogleSignClient by lazy {
        GoogleSignIn.getClient(FacebookSdk.getApplicationContext(), gso)
    }

    fun googleLogin() = mGoogleSignClient.signInIntent

    fun handleGoogleSignInResult(data: Intent?): User {

        val account = GoogleSignIn.getSignedInAccountFromIntent(data)
        val user = User(account.result?.id, account.result?.displayName, account.result?.email)
        Log.d(TAG, account.result?.email.toString())
        return user
    }

    val loginManager: LoginManager = LoginManager.getInstance()
    val mCallbackManager = CallbackManager.Factory.create()

    private val mFacebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {

            Log.d(TAG, AccessToken.getCurrentAccessToken().toString())

        }

        override fun onCancel() {
            Log.d(TAG, "canceled")
        }

        override fun onError(error: FacebookException?) {
             Log.d(TAG, error.toString())
        }
    }

    init {
        loginManager.registerCallback(mCallbackManager, mFacebookCallback)
    }

}