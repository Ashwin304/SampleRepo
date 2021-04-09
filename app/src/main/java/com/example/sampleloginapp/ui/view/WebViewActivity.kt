package com.example.sampleloginapp.ui.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.sampleloginapp.R
import com.example.sampleloginapp.io.model.Article
import com.example.sampleloginapp.utils.URL
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val item = intent.getStringExtra(URL)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(item!!)
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }
}