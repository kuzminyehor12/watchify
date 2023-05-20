package com.android.watchify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.watchify.R
import com.android.watchify.databinding.ItemArticleBinding
import com.android.watchify.models.Article
import com.bumptech.glide.Glide

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val image = view.findViewById<ImageView>(R.id.articleImage)
        private val date = view.findViewById<TextView>(R.id.articleDate)
        private val title = view.findViewById<TextView>(R.id.articleTitle)

        fun bind(article: Article) {
            Glide.with(itemView).load(article.urlToImage).into(image)
            image.clipToOutline = true
            title.text = article.title
            date.text = article.publishedAt
        }
    }

    private lateinit var onItemClickListener: ((Article) -> Unit)

    private val callback = object : DiffUtil.ItemCallback<Article>(){
        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.url == newItem.url
    }

    val differ: AsyncListDiffer<Article> = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }
}