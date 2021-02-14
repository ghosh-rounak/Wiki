package com.example.wiki.ui.adapters.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wiki.R
import com.example.wiki.data.network.responses.Page
import com.example.wiki.databinding.ListArticleRowBinding

/*This is just an Extra backup file not used anywhere in this project. */
class ArticleAdapter(private var clickListner: OnArticleItemClickListner)
    : ListAdapter<Page, ArticleViewHolder>(ArticleDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListArticleRowBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.list_article_row,parent,false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position),clickListner)
    }
}

class ArticleViewHolder(private val binding: ListArticleRowBinding) : RecyclerView.ViewHolder(binding.root){


    fun bind(article : Page,action:OnArticleItemClickListner){
        binding.articleImg.setImageBitmap(null)
        binding.article=article //alternately row can be set manually
        binding.executePendingBindings()

        binding.rowLayout.setOnClickListener{
            action.onItemClick(article,adapterPosition)
        }
    }

}

interface OnArticleItemClickListner{
    fun onItemClick(item: Page, position: Int)
}

class ArticleDiffUtil : DiffUtil.ItemCallback<Page>(){
    override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean {
        return oldItem.pageid == newItem.pageid
    }

    override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean {
        return oldItem == newItem
    }
}