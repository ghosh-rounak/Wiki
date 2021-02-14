package com.example.wiki.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wiki.R
import com.example.wiki.data.db.entities.Article
import com.example.wiki.databinding.ListSavedArticleRowBinding

class SavedArticleListAdapter(private val clickListener:(Article, Int)->Unit,private val longClickListener:(Article, Int)->Boolean)
    : RecyclerView.Adapter<SavedArticleViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.pageid == newItem.pageid
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListSavedArticleRowBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_saved_article_row,parent,false)
        return SavedArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

  /*  //Added starts
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    //Added ends*/

    override fun onBindViewHolder(holder: SavedArticleViewHolder, position: Int) {
        holder.bind(differ.currentList[position],clickListener,longClickListener)
    }

}

class SavedArticleViewHolder(private val binding: ListSavedArticleRowBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(article : Article, clickListener:(Article, Int)->Unit,longClickListener:(Article,Int)->Boolean){
        binding.articleImg.setImageBitmap(null)
        binding.article=article //alternately row can be set manually
        binding.executePendingBindings()

        binding.rowLayout.setOnClickListener{
            clickListener(article,adapterPosition)
        }
        binding.rowLayout.setOnLongClickListener {
            longClickListener(article,adapterPosition)
        }
    }
}