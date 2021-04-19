package com.example.sampleloginapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.FragmentNewsDetailBinding
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.utils.Constants
import com.example.sampleloginapp.viewmodel.NewsDetailViewModel
import kotlinx.android.synthetic.main.fragment_news_detail.*


class NewsDetailFragment : Fragment() {

    lateinit var newsDetailViewModel: NewsDetailViewModel
    lateinit var newsDetailBinding: FragmentNewsDetailBinding
    private var boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false)

        initViewModel()

        initActionBar()
        val value = requireArguments().getParcelable<Article>(Constants().ARTICLE)
        newsDetailViewModel.getNewsDetail(value)

        newsDetailBinding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        newsDetailViewModel.url.observe(viewLifecycleOwner, Observer {
            url ->

            if(!boolean) {
                val args = Bundle()
                args.putString(Constants().URL, url)
                findNavController().navigate(R.id.action_newsDetailFragment_to_webviewFragment, args)
                boolean = true
            }

        })
        return newsDetailBinding.root
    }


    override fun onResume() {
        super.onResume()
       boolean = false

    }


    private fun initViewModel() {
         newsDetailViewModel = ViewModelProvider(this).get(NewsDetailViewModel::class.java)
        newsDetailBinding.newsDetailViewModel = newsDetailViewModel
    }

    private fun initActionBar() {
        val toolbar: Toolbar = newsDetailBinding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

    }





}