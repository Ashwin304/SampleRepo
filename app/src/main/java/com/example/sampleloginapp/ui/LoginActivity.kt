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
import com.facebook.*
import com.facebook.login.LoginResult
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    val RC_SIGN_IN = 100
    lateinit var loginDatabinding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    val facebook_permissions = mutableListOf("email", "public_profile")



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

       loginDatabinding.button.setOnClickListener {
            loginViewModel.facebookLoginManger().logInWithReadPermissions(this, facebook_permissions)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        loginViewModel.getCallbackManager()?.onActivityResult(requestCode, resultCode, data).also {
            val userId: String? = AccessToken.getCurrentAccessToken()?.userId
            if(userId != null) {
                loginDatabinding.tvFacebookId.text = userId
            }
        }

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