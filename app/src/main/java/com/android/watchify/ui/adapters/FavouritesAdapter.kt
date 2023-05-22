package com.android.watchify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.watchify.R
import com.android.watchify.data.repos.NewsRepository
import com.android.watchify.databinding.ItemArticleBinding
import com.android.watchify.models.Article
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {
    class FavouritesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val image = view.findViewById<ImageView>(R.id.favouriteArticleImage)
        private val date = view.findViewById<TextView>(R.id.favouriteArticleDate)
        private val title = view.findViewById<TextView>(R.id.favouriteArticleTitle)
        private val removeFavouriteBtn = view.findViewById<ImageView>(R.id.favouriteArticleRemove)

        fun bind(article: Article) {
            Glide.with(itemView).load(article.urlToImage).into(image)
            image.clipToOutline = true
            title.text = article.title
            date.text = article.publishedAt
        }

        fun setOnLikeListener(listener: OnClickListener){
            removeFavouriteBtn.setOnClickListener(listener)
        }
    }

    private lateinit var onItemClickListener: (Article) -> Unit
    private lateinit var onFavouriteRemoveClickListener: (Article) -> Unit

    private val callback = object : DiffUtil.ItemCallback<Article>(){
        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.url == newItem.url
    }

    val differ: AsyncListDiffer<Article> = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }

    fun setOnLikeListener(listener: (Article) -> Unit){
        onFavouriteRemoveClickListener = listener
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        FirebaseAuth.getInstance().currentUser?.let {
            val article = differ.currentList[position]
            article.uid = it.uid

            holder.bind(article)

            holder.itemView.setOnClickListener {
                onItemClickListener(article)
            }

            holder.setOnLikeListener {
                onFavouriteRemoveClickListener(article)
            }
        }
    }
}