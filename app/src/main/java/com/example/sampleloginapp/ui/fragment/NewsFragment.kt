package com.example.sampleloginapp.ui.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.NewsApplication
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.FragmentNewsBinding
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.network.NetworkConnectionInterceptor
import com.example.sampleloginapp.io.network.NewsApi
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.ui.adapter.NewsRecyclerViewAdapter
import com.example.sampleloginapp.ui.view.HomeActivity
import com.example.sampleloginapp.utils.CallBackInterface
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.utils.SharedPreference
import com.example.sampleloginapp.viewmodel.NewsViewModel
import com.example.sampleloginapp.viewmodel.NewsViewModelFactory
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class NewsFragment : Fragment(), NewsRecyclerViewAdapter.NewsItemClickeListener {


    lateinit var newsViewModel: NewsViewModel
    var boolean = true
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: NewsRecyclerViewAdapter
    lateinit var sharedPreference: SharedPreference
    lateinit var newsDatabinding: FragmentNewsBinding



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        newsDatabinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        initViewModel()
        setHasOptionsMenu(true)
        initActionBar()

        sharedPreference = SharedPreference(context!!)

        initRecyclerView()

        newsViewModel.getNews()

        newsViewModel._news.observe(viewLifecycleOwner, Observer {
            val news = ArrayList<Article>()
            for (i in 0..it.articles!!.size - 1) {
                val _news = it.articles[i]
                news.add(i, Article(_news.title!!, _news.description, _news.url, _news.urlToImage, _news.publishedAt, false))
            }
            newsViewModel.saveAll(news)

        })

        newsViewModel.returnValue.observe(viewLifecycleOwner, Observer {
            newsViewModel.getAllNews()

        })

        newsViewModel.favouriteNews.observe(viewLifecycleOwner, Observer {
            newsDatabinding.progressBar.visibility = View.GONE
            adapter.differ.submitList(it)
        })
        return newsDatabinding.root
    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.moreButton -> {
                LoginManager.getInstance().logOut()
                sharedPreference.saveUserId("")
                logoutGoogle()
                newsViewModel.deleteAllFavourite()
                startActivity(Intent(context, HomeActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onNewsItemClicked(article: Article) {

        val newsDetailFragment = NewsDetailFragment()
        val args = Bundle()
        args.putParcelable(Constants().ARTICLE,  article)
        newsDetailFragment.setArguments(args)
        initFragmentTransaction(newsDetailFragment)

    }

    private fun initRecyclerView(): RecyclerView {
        val recyclerView = newsDatabinding.recyclerView
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        adapter = NewsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        return recyclerView
    }

    private fun initFragmentTransaction(fragment: Fragment) {
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onFavouriteClicked(article: Article, favourite: Boolean) {
        if (favourite == false) {
            val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, true)
            newsViewModel.updateFavourite(news!!)
            boolean = false
        } else {

        val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, false)
        newsViewModel.deleteFavourite(news)
    }
}


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun initViewModel() {

        val interceptor = NetworkConnectionInterceptor(context!!)
        val newsApi = NewsApi(interceptor)
        val db = NewsDatabase(context!!)
        val repository = NewsRepository(newsApi,db)
        val factory = NewsViewModelFactory(activity!!.application, repository)
        newsViewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        newsDatabinding.newsViewModel = newsViewModel


    }

    @SuppressLint("ResourceType")
    private fun initActionBar() {
       val toolbar: Toolbar = newsDatabinding.toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val acttionBar = (activity as AppCompatActivity?)!!.supportActionBar

    }


    private fun filter(text: String?) {
        val filterdArticle: ArrayList<Article> = ArrayList()
        val filterPattern = text.toString().toLowerCase().trim()
        for (s in newsViewModel.favouriteNews.value!!) {

            if (s.title!!.toLowerCase().contains(filterPattern)) {
                filterdArticle.add(s)
            }
        }
        adapter.differ.submitList(filterdArticle)
    }



    private fun logoutGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()

        val mGoogleApiClient = GoogleSignIn.getClient(context!!, gso)
        mGoogleApiClient.signOut()
    }

    fun isInternetAvailable(): Boolean {

        val connectivityManager: ConnectivityManager = context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }


}