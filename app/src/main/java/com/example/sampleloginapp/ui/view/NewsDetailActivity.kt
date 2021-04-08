package com.example.sampleloginapp.ui.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.ActivityNewsDetailBinding
import com.example.sampleloginapp.io.model.Article
import com.example.sampleloginapp.viewmodel.NewsDetailViewModel

class NewsDetailActivity : AppCompatActivity() {
    private val TAG = "NewsDetailActivity"

    lateinit var newsDetailViewModel: NewsDetailViewModel
    lateinit var newsDetailBinding: ActivityNewsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initActionBar()

        val item: Article? = intent.getParcelableExtra("article")
        newsDetailViewModel.getNewsDetail(item)

        newsDetailViewModel.url.observe(this, Observer {
            url ->
            val intent = Intent(this, WebViewActivity::class.java).also {
                it.putExtra("url", url)
            }
            startActivity(intent)
        })


    }

    private fun initViewModel() {
        newsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)
        newsDetailViewModel = ViewModelProvider(this).get(NewsDetailViewModel::class.java)
        newsDetailBinding.newsDetailViewModel = newsDetailViewModel
    }

    private fun initActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}