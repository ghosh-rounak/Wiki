package com.example.wiki.ui.article_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wiki.R
import com.example.wiki.data.db.entities.Article
import com.example.wiki.databinding.FragmentArticleSearchBinding
import com.example.wiki.ui.ArticlesViewModel
import com.example.wiki.ui.MainActivity
import com.example.wiki.ui.adapters.SavedArticleListAdapter
import com.example.wiki.utils.hideKeyboard


class ArticleSearchFragment : Fragment() {
    private lateinit var adapter: SavedArticleListAdapter

    //declare navController
    private lateinit var navController: NavController

    private  lateinit var binding:FragmentArticleSearchBinding
    private lateinit var viewModel: ArticlesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article_search,container,false)
        setDataBindingRelatedComponents()
        initRecyclerView()
        createObservers()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialize navController
        navController= Navigation.findNavController(view)
        setButtonClicks()
    }




    private fun setDataBindingRelatedComponents() {
        viewModel = (activity as MainActivity).viewModel
        binding.searchViewModel=viewModel
        binding.lifecycleOwner=this
    }

    private fun initRecyclerView() {
        binding.recentRv.layoutManager = LinearLayoutManager(activity)

        adapter = SavedArticleListAdapter( { selectedItem: Article, position: Int ->
            listItemClicked(
                selectedItem,
                position
            )
        },{ selectedItem: Article, position: Int ->
            listItemLongClicked(
                selectedItem,
                position
            )
        })

        binding.recentRv.adapter = adapter

        //Added ----
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.d("onCreate", "Scroll Top: ")
                binding.recentRv.smoothScrollToPosition(0)
            }
        })
        //-------
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
                showProgressBar()
            }else{
                dismissProgressBar()
            }
        }


        viewModel.savedArticles.observe(viewLifecycleOwner) {
            if(it!=null){
                adapter.differ.submitList(it)
            }
        }
    }

    private fun setButtonClicks() {
        binding.searchIcon.setOnClickListener {
            if(binding.inputSearch.text.toString().isNotEmpty()){
                hideKeyboard(activity as MainActivity)
                val bundle= bundleOf("search_query" to binding.inputSearch.text.toString())
                navController.navigate(R.id.action_articleSearchFragment_to_articlesFragment,bundle)
            }
        }
    }


    private fun dismissProgressBar() {
        binding.progBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progBar.visibility = View.VISIBLE
    }


    //ITEM Click on each item of the Recycler View
    private fun listItemClicked(article : Article, position: Int) {
        Log.d("onCreate", "listItem position clicked : $position")
        Log.d("onCreate", "listItemClicked: $article")
        val bundle= bundleOf(
            "saved_article" to article
        )
        navController.navigate(R.id.action_articleSearchFragment_to_articleDetailFragment,bundle)
    }

    //ITEM Long Click on each item of the Recycler View
    private fun listItemLongClicked(article: Article, position: Int) : Boolean{
        //do something
        showAlertDelete(article,position)
        return true
    }

    private fun showAlertDelete(article: Article, position: Int) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do wanna delete this article")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            binding.progBar.visibility= View.VISIBLE
            viewModel.deleteArticleFromDB(article,position)
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

}