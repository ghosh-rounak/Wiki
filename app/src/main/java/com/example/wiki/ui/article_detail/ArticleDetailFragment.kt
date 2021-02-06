package com.example.wiki.ui.article_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.wiki.R
import com.example.wiki.data.db.entities.Article
import com.example.wiki.data.network.responses.Page
import com.example.wiki.databinding.FragmentArticleDetailBinding
import com.example.wiki.ui.ArticlesViewModel
import com.example.wiki.ui.MainActivity

class ArticleDetailFragment : Fragment() {
    private  lateinit var binding: FragmentArticleDetailBinding
    private lateinit var viewModel: ArticlesViewModel
    private var article:Page?=null
    private var savedArticle:Article?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(requireArguments().getSerializable("article")!=null){
            article=requireArguments().getSerializable("article") as Page
        }
        if(requireArguments().getSerializable("saved_article")!=null){
            savedArticle=requireArguments().getSerializable("saved_article") as Article
        }
        Log.d("onCreate", "article: $article")
        Log.d("onCreate", "savedArticle: $savedArticle")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article_detail,container,false)
        setDataBindingRelatedComponents()
        createObservers()
        saveInLocalStoarage()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWebsite()
        setButtonClicks()
    }

    private fun setButtonClicks() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun loadWebsite() {
        var url:String ?= null
        if(article!=null){
            url = article!!.fullurl
        }else{
            if(savedArticle!=null){
                url = savedArticle!!.fullurl
            }
        }
        Log.d("onCreate", "loadWebsite: $url")
        if(url!=null){
            showProgressBar()
            binding.webview.apply {
                webViewClient = WebViewClient()
                loadUrl(url)
            }
            dismissProgressBar()
        }
    }

    private fun saveInLocalStoarage() {
        if(article!=null){
            showProgressBar()
            viewModel.saveInDB(article!!)
        }
    }


    private fun setDataBindingRelatedComponents() {
        viewModel = (activity as MainActivity).viewModel
        binding.detailViewModel=viewModel
        binding.lifecycleOwner=this
    }

    private fun createObservers() {
        //Required for Observing and displaying Events
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.progData.observe(viewLifecycleOwner) {
            if(it){
                Log.d("onCreate", "createObservers: Progress bar showing")
                showProgressBar()
            }else{
                dismissProgressBar()
            }
        }
    }



    private fun dismissProgressBar() {
        binding.progBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progBar.visibility = View.VISIBLE
    }


}