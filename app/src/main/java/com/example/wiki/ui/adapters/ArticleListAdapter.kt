package com.example.wiki.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wiki.R
import com.example.wiki.data.network.responses.Page
import com.example.wiki.databinding.ListArticleRowBinding

class ArticleListAdapter(private val clickListener:(Page, Int)->Unit)
    : RecyclerView.Adapter<ArticleViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Page>() {
        override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean {
            return oldItem.pageid == newItem.pageid
        }

        override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListArticleRowBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_article_row,parent,false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

   /* //Added starts
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    //Added ends*/

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(differ.currentList[position],clickListener)
    }

}

class ArticleViewHolder(private val binding: ListArticleRowBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(article : Page,clickListener:(Page,Int)->Unit){
        binding.articleImg.setImageBitmap(null)
        binding.article=article //alternately row can be set manually
        binding.executePendingBindings()

        binding.rowLayout.setOnClickListener{
            clickListener(article,adapterPosition)
        }
    }
}