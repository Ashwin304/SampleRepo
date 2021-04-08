package com.example.sampleloginapp.ui.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.ActivityNewsBinding
import com.example.sampleloginapp.io.db.NewsData
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.model.Article
import com.example.sampleloginapp.io.network.NetworkConnectionInterceptor
import com.example.sampleloginapp.io.network.NewsApi
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.ui.adapter.NewsRecyclerViewAdapter
import com.example.sampleloginapp.viewmodel.NewsViewModel
import com.example.sampleloginapp.viewmodel.NewsViewModelFactory
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.cardview_news_layout.*

class NewsActivity : AppCompatActivity(), NewsRecyclerViewAdapter.NewsItemClickeListener {

    lateinit var newsViewModel: NewsViewModel
    private val TAG = "NewsActivity"
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: NewsRecyclerViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initViewModel()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#379CF4")))
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        newsViewModel.getNews()
        newsViewModel.getFavouriteNews()


        newsViewModel._news.observe(this, Observer {
            adapter = NewsRecyclerViewAdapter(it.articles, this)
            Log.d(TAG, it.totalResults.toString())
            recyclerView.adapter = adapter
        })

        newsViewModel.favouriteNews.observe(this, Observer {
            Log.d(TAG, it.toString())

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
                newsViewModel.deleteAllFavourite()
                onBackPressed()


            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNewsItemClicked(article: Article?) {
        val intent = Intent(this, NewsDetailActivity::class.java).also {
            it.putExtra("article", article)
        }
        startActivity(intent)
    }

    override fun onFavouriteClicked(article: Article?) {
        newsViewModel.saveFavourite(article!!)
    }


    fun filter(text: String?) {
        val filterdArticle: ArrayList<Article> = ArrayList()
        val filterPattern = text.toString().toLowerCase().trim()
        for (s in newsViewModel._news.value?.articles!!) {

            if (s.title!!.toLowerCase().contains(filterPattern)) {
                filterdArticle.add(s)
            }
        }
        adapter.filterList(filterdArticle)
    }

}