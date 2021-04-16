package com.example.sampleloginapp.ui.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.sampleloginapp.R
import com.example.sampleloginapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_webview.*


class WebviewFragment : Fragment(){

    lateinit var value: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         value = arguments!!.getString(Constants().URL)!!

        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(value)

    }



}