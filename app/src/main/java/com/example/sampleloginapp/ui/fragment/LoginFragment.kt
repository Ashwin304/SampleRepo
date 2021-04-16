package com.example.sampleloginapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.FragmentLoginBinding
import com.example.sampleloginapp.io.repository.LoginRepository
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.utils.SharedPreference
import com.example.sampleloginapp.viewmodel.LoginViewModel
import com.example.sampleloginapp.viewmodel.ViewModelFactory
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext


class LoginFragment : Fragment() {


    lateinit var loginDatabinding: FragmentLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var sharedPreferences: SharedPreference
    val constants = Constants()
    val facebook_permissions = mutableListOf(constants.EMAIL, constants.PROFILE)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        FacebookSdk.sdkInitialize(getApplicationContext())

        loginDatabinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        initViewModel()
        initActionBar()
        loginCheck()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
         requireActivity().finish()
        }



        loginDatabinding.signinButton.setOnClickListener {
            loginViewModel.googleLogin().also{
                startActivityForResult(it, constants.RC_SIGN_IN)

            }
        }

        loginDatabinding.button.setOnClickListener {
            loginViewModel.facebookLoginManger().logInWithReadPermissions(this, facebook_permissions)
        }
        return loginDatabinding.root
    }




    private fun initViewModel() {
        val repository = LoginRepository()
        val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        loginDatabinding.loginViewModel = loginViewModel
    }

    private fun loginCheck() {

        sharedPreferences = SharedPreference(getApplicationContext())
        val userId = sharedPreferences.getUserId()
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        //navigate to newsFragment
       if (!userId.isNullOrEmpty() || !accessToken?.token.isNullOrEmpty()) {

           findNavController().navigate(R.id.action_loginFragment_to_newsFragment)
        }
    }
    private fun initActionBar() {

        val toolbar: Toolbar = loginDatabinding.toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == constants.RC_SIGN_IN) {

            val user = loginViewModel.handleGoogleSignIn(data)
            when(user){
                null -> {
                    Toast.makeText(context, constants.FAILED, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    sharedPreferences.saveUserId(user.id.toString())
                    findNavController().navigate(R.id.action_loginFragment_to_newsFragment)
                }
            }

        }else {
            loginViewModel.getCallbackManager()?.onActivityResult(requestCode, resultCode, data).also {
                val userId: String? = AccessToken.getCurrentAccessToken()?.userId
                if(userId != null) {

                    findNavController().navigate(R.id.action_loginFragment_to_newsFragment)
                }
            }

        }
    }



}