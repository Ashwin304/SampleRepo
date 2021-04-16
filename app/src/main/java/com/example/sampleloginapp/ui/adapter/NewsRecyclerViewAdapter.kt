package com.example.sampleloginapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.CardviewNewsLayoutBinding
import com.example.sampleloginapp.io.db.Article

import kotlinx.android.synthetic.main.cardview_news_layout.view.*

class NewsRecyclerViewAdapter( val listener: NewsItemClickeListener) : RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsHolder>(){

    class NewsHolder(val newsLayoutBinding: CardviewNewsLayoutBinding): RecyclerView.ViewHolder(newsLayoutBinding.root) {

        fun bind(article: Article, listener: NewsItemClickeListener){
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
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.bind(differ.currentList.get(position), listener)

        val button = holder.itemView.btn_favourite
        if(button.isChecked){
            button.isChecked = true
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)



    interface NewsItemClickeListener{
        fun onNewsItemClicked(article: Article)
        fun onFavouriteClicked(article: Article, favourite: Boolean)
    }


    fun filterList(filterdArticle: ArrayList<Article>){
      //  this.differ.currentList = filterdArticle
        notifyDataSetChanged()
    }


}


