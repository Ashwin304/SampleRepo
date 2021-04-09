package com.example.sampleloginapp.ui.view

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.db.NewsDatabase

import com.example.sampleloginapp.io.network.NetworkConnectionInterceptor
import com.example.sampleloginapp.io.network.NewsApi
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.ui.adapter.NewsRecyclerViewAdapter
import com.example.sampleloginapp.utils.ACTION_BAR
import com.example.sampleloginapp.utils.ARTICLE
import com.example.sampleloginapp.utils.SharedPreference
import com.example.sampleloginapp.viewmodel.NewsViewModel
import com.example.sampleloginapp.viewmodel.NewsViewModelFactory
import com.facebook.login.LoginManager

class NewsActivity : AppCompatActivity(), NewsRecyclerViewAdapter.NewsItemClickeListener {


    lateinit var newsViewModel: NewsViewModel
    var boolean = true
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: NewsRecyclerViewAdapter
    lateinit var sharedPreference: SharedPreference



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initViewModel()
        sharedPreference = SharedPreference(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(ACTION_BAR)))
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


        newsViewModel.getNews()




        newsViewModel._news.observe(this, Observer {
            val news = ArrayList<Article>()

            for(i in 0..it.articles!!.size -1){
                val _news = it.articles[i]
                news.add(i, Article(_news.title!!, _news.description, _news.url, _news.urlToImage, _news.publishedAt, false))
            }
            newsViewModel.saveAll(news)

        })

        newsViewModel.returnValue.observe(this, Observer {
            newsViewModel.getAllNews()
        })

        newsViewModel.favouriteNews.observe(this, Observer {

            adapter = NewsRecyclerViewAdapter(it, this)
            recyclerView.adapter = adapter

        })

    }

    private fun initViewModel() {
        val interceptor = NetworkConnectionInterceptor(this)
        val newsApi = NewsApi(interceptor)
        val db = NewsDatabase(this)
        val repository = NewsRepository(newsApi,db)
        val factory = NewsViewModelFactory(repository)
        newsViewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.news_menu, menu)
        val searchViewItem: MenuItem? = menu?.findItem(R.id.seachBar)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.moreButton -> {
                LoginManager.getInstance().logOut()
                sharedPreference.saveUserId("")
                newsViewModel.deleteAllFavourite()
                onBackPressed()

            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNewsItemClicked(article: Article) {
        val intent = Intent(this, NewsDetailActivity::class.java).also {
            it.putExtra(ARTICLE, article)
        }
        startActivity(intent)
    }

    override fun onFavouriteClicked(article: Article, favourite: Boolean) {
        if(favourite == false) {
            val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, true)
            newsViewModel.updateFavourite(news!!)
            boolean = false
        } else {
            val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, false)
            newsViewModel.deleteFavourite(news)
        }

    }



    fun filter(text: String?) {
        val filterdArticle: ArrayList<Article> = ArrayList()
        val filterPattern = text.toString().toLowerCase().trim()
        for (s in newsViewModel.favouriteNews.value!!) {

            if (s.title!!.toLowerCase().contains(filterPattern)) {
                filterdArticle.add(s)
            }
        }
        adapter.filterList(filterdArticle)
    }

}