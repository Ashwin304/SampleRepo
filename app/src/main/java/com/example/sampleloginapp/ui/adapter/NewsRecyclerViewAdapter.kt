package com.example.sampleloginapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.CardviewNewsLayoutBinding
import com.example.sampleloginapp.io.model.Article
import kotlinx.android.synthetic.main.cardview_news_layout.view.*

class NewsRecyclerViewAdapter(var articles: List<Article>?, val listener: NewsItemClickeListener) : RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsHolder>(){

    class NewsHolder(val newsLayoutBinding: CardviewNewsLayoutBinding): RecyclerView.ViewHolder(newsLayoutBinding.root) {



        fun bind(article: Article?, listener: NewsItemClickeListener){
            newsLayoutBinding.newsModel = article
            newsLayoutBinding.newsItemClicked = listener
            newsLayoutBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val binding: CardviewNewsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.cardview_news_layout, parent, false)
        return NewsHolder(binding)
    }

    override fun getItemCount(): Int {
        return articles?.size!!
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.bind(articles?.get(position), listener)
       // holder.itemView.btn_favourite.

    }

    interface NewsItemClickeListener{
        fun onNewsItemClicked(article: Article?)
        fun onFavouriteClicked(article: Article?)
    }


    fun filterList(filterdArticle: List<Article>){
        this.articles = filterdArticle
        notifyDataSetChanged()
    }


}


