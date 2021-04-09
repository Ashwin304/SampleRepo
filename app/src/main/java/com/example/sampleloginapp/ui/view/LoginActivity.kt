package com.example.sampleloginapp.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.ActivityLoginBinding
import com.example.sampleloginapp.io.repository.LoginRepository
import com.example.sampleloginapp.utils.*
import com.example.sampleloginapp.viewmodel.LoginViewModel
import com.example.sampleloginapp.viewmodel.ViewModelFactory
import com.facebook.*


class LoginActivity : AppCompatActivity() {

    lateinit var loginDatabinding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var sharedPreferences: SharedPreference
    val facebook_permissions = mutableListOf(EMAIL, PROFILE)



    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(getApplicationContext())

        loginDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViewModel()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(ACTION_BAR)))
        loginCheck()


        loginDatabinding.signinButton.setOnClickListener {
            loginViewModel.googleLogin().also{
                startActivityForResult(it, RC_SIGN_IN)
            }
        }

       loginDatabinding.button.setOnClickListener {
            loginViewModel.facebookLoginManger().logInWithReadPermissions(this, facebook_permissions)
        }


    }

    private fun loginCheck() {
        sharedPreferences = SharedPreference(this)
        val userId = sharedPreferences.getUserId()
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (!userId.isNullOrEmpty() || !accessToken?.token.isNullOrEmpty()) {
            startActivity(Intent(this, NewsActivity::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        loginViewModel.getCallbackManager()?.onActivityResult(requestCode, resultCode, data).also {
            val userId: String? = AccessToken.getCurrentAccessToken()?.userId
            if(userId != null) {
                startActivity(Intent(this, NewsActivity::class.java))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
           val user = loginViewModel.handleGoogleSignIn(data)
            sharedPreferences.saveUserId(user.id.toString())
            if(user != null) {
                startActivity(Intent(this, NewsActivity::class.java))
            }
        }
    }


    private fun initViewModel() {
        val repository = LoginRepository()
        val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        loginDatabinding.loginViewModel = loginViewModel
    }


}