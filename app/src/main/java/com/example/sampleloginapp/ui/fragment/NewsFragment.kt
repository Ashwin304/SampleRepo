package com.example.sampleloginapp.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.FragmentNewsBinding
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.io.db.NewsDatabase
import com.example.sampleloginapp.io.network.NewsApi
import com.example.sampleloginapp.io.repository.NewsRepository
import com.example.sampleloginapp.ui.adapter.NewsRecyclerViewAdapter
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.utils.NewsItemClickeListener
import com.example.sampleloginapp.utils.SharedPreference
import com.example.sampleloginapp.viewmodel.NewsViewModel
import com.example.sampleloginapp.viewmodel.NewsViewModelFactory
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class NewsFragment : Fragment(), NewsItemClickeListener {


    lateinit var newsViewModel: NewsViewModel
    var boolean = true
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

        sharedPreference = SharedPreference(requireContext())

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()

        }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.moreButton -> {
                LoginManager.getInstance().logOut()
                sharedPreference.saveUserId("")
                logoutGoogle()
                newsViewModel.deleteAllFavourite()

                findNavController().navigate(R.id.action_newsFragment_to_loginFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onNewsItemClicked(article: Article) {

        val newsDetailFragment = NewsDetailFragment()
        val args = Bundle()
        args.putParcelable(Constants().ARTICLE,  article)
        newsDetailFragment.setArguments(args)
        findNavController().navigate(R.id.action_newsFragment_to_newsDetailFragment, args)

    }

    private fun initRecyclerView(): RecyclerView {
        val recyclerView = newsDatabinding.recyclerView
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        adapter = NewsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        return recyclerView
    }


    override fun onFavouriteClicked(article: Article, favourite: Boolean) {

        when (favourite) {
            false -> {
               val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, true)

                newsViewModel.updateFavourite(news!!)
                newsViewModel.getAllNews()
        }
           true ->{
            val news = Article(article.title, article.description, article.url, article.urlToImage, article.publishedAt, false)
               newsViewModel.deleteFavourite(news)
               newsViewModel.getAllNews()

        }
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

        val newsApi = NewsApi()
        val db = NewsDatabase(requireContext())
        val repository = NewsRepository(newsApi,db)
        val factory = NewsViewModelFactory(requireActivity().application, repository)
        newsViewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        newsDatabinding.newsViewModel = newsViewModel


    }

    @SuppressLint("ResourceType")
    private fun initActionBar() {
       val toolbar: Toolbar = newsDatabinding.toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)


    }


    private fun filter(text: String?) {
        val filterdArticle: ArrayList<Article> = ArrayList()
        val filterPattern = text.toString().toLowerCase().trim()
        for (s in newsViewModel.favouriteNews.value!!) {

            if (s.title!!.toLowerCase().startsWith(filterPattern)) {
                filterdArticle.add(s)
            }
        }
        adapter.differ.submitList(filterdArticle)
    }



    private fun logoutGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()

        val mGoogleApiClient = GoogleSignIn.getClient(requireContext(), gso)
        mGoogleApiClient.signOut()
    }



}