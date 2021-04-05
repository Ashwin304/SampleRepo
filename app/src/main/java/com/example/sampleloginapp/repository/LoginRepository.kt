package com.example.sampleloginapp.repository

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewModelScope
import com.example.sampleloginapp.model.User
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
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



}