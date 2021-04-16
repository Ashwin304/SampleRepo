package com.example.sampleloginapp.ui.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sampleloginapp.R
import com.example.sampleloginapp.ui.fragment.LoginFragment
import com.example.sampleloginapp.ui.fragment.NewsDetailFragment
import com.example.sampleloginapp.ui.fragment.NewsFragment
import com.example.sampleloginapp.ui.fragment.WebviewFragment
import com.example.sampleloginapp.utils.CallBackInterface
import kotlinx.android.synthetic.main.fragment_webview.*


class HomeActivity : AppCompatActivity(), CallBackInterface {
    lateinit var fragmentTransaction: FragmentTransaction
    private val fragmentManager: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        addFragment()

    }

    fun addFragment() {
        fragmentTransaction = fragmentManager.beginTransaction()
        val fragment: LoginFragment = LoginFragment()
        fragment.setCallBackInterface(this)
        fragmentTransaction.add(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    fun addNewFragment(fragment: Fragment) {

        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
       // fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    override fun callBackMethod(fragment: Fragment) {
        addNewFragment(fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
       onSupportNavigateUp()
        return super.onSupportNavigateUp()
    }


}