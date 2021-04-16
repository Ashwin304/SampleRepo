package com.example.sampleloginapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleloginapp.R
import com.example.sampleloginapp.databinding.CardviewNewsLayoutBinding
import com.example.sampleloginapp.io.db.Article
import com.example.sampleloginapp.utils.NewsItemClickeListener
import kotlinx.android.synthetic.main.cardview_news_layout.view.*


class NewsRecyclerViewAdapter(val listener: NewsItemClickeListener) : RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsHolder>(), Filterable{

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



    override fun getFilter(): Filter {
        return  object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results: MutableList<Article> = ArrayList<Article>()
                val item = differ.currentList
                if (item == null) differ.submitList(item)
                if (constraint != null) {
                    if ( item.size > 0) {
                        for (g in item) {
                            if (g.title.toLowerCase().contains(constraint.toString())) results.add(g)
                        }
                    }
                    oReturn.values = results
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as MutableList<Article>?)
            }

        }
    }




}


