package com.example.sampleloginapp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.ActivityLoginBinding
import com.example.sampleloginapp.repository.LoginRepository
import com.example.sampleloginapp.viewmodel.LoginViewModel
import com.example.sampleloginapp.viewmodel.ViewModelFactory
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    val RC_SIGN_IN = 100
    lateinit var loginDatabinding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    val facebook_permissions = mutableListOf("email", "public_profile")
    val callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(getApplicationContext())

        loginDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViewModel()


        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLUE))

        loginDatabinding.signinButton.setOnClickListener {
            loginViewModel.googleLogin().also{
                startActivityForResult(it, RC_SIGN_IN)
            }
        }

     /*   loginDatabinding.loginButton.setOnClickListener {
            loginViewModel.loginManager.logInWithReadPermissions(this, facebook_permissions)
        }*/

        val EMAIL = "email"
        loginDatabinding.loginButton.setReadPermissions(Arrays.asList(EMAIL))

        loginDatabinding.loginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d(TAG, loginResult?.accessToken?.userId.toString())

            }

            override fun onCancel() {
               Log.d(TAG, "Cancled")
            }

            override fun onError(exception: FacebookException) {
                Log.d(TAG, exception.toString())
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
           val user = loginViewModel.handleGoogleSignIn(data)
            loginDatabinding.id.text = user.id
            loginDatabinding.name.text = user.name
            loginDatabinding.email.text = user.email
        }
    }


    private fun initViewModel() {
        val repository = LoginRepository()
        val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        loginDatabinding.loginViewModel = loginViewModel
    }


}