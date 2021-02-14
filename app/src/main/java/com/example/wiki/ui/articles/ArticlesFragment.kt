package com.example.wiki.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.wiki.R
import com.example.wiki.data.network.responses.Page
import com.example.wiki.databinding.FragmentArticlesBinding
import com.example.wiki.ui.ArticlesViewModel
import com.example.wiki.ui.MainActivity
import com.example.wiki.ui.adapters.ArticleListAdapter


class ArticlesFragment : Fragment()/*,OnArticleItemClickListner*/ {
    private lateinit var adapter: ArticleListAdapter

    //private lateinit var adapter: ArticleAdapter

    //declare navController
    private lateinit var navController: NavController
    private var searchQuery:String?=null

    private  lateinit var binding: FragmentArticlesBinding
    private lateinit var viewModel: ArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchQuery= requireArguments().getString("search_query")!!
        Log.d("onCreate", "searchQuery: $searchQuery")
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreate", "onCreateView:  ArticlesFragment executing")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)
        setDataBindingRelatedComponents()
        initRecyclerView()
        createObservers()
        callSearchApi()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onCreate", "onDestroyView: ArticlesFragment executing")
    }


    private fun callSearchApi() {
        //call search api
        if(searchQuery!=null){
            viewModel.getSearchedArticles(searchQuery!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialize navController
        navController= Navigation.findNavController(view)
        setButtonClicks()
    }




    private fun setDataBindingRelatedComponents() {
        viewModel = (activity as MainActivity).viewModel
        binding.articlesViewModel=viewModel
        binding.lifecycleOwner=this
    }

    private fun initRecyclerView() {
        binding.articlesRv.layoutManager = LinearLayoutManager(activity)
        adapter = ArticleListAdapter { selectedItem: Page, position: Int ->
            listItemClicked(
                selectedItem,
                position
            )
        }
        binding.articlesRv.adapter = adapter

       /* binding.articlesRv.layoutManager = LinearLayoutManager(activity)
        binding.articlesRv.adapter = ArticleAdapter(this)
        adapter = binding.articlesRv.adapter as ArticleAdapter*/

        //to be added only when there is no pagination starts-----
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.d("onCreate", "Scroll Top: ")
                binding.articlesRv.smoothScrollToPosition(0)
            }
        })
        //to be added only when there is no pagination ends-----

    }

    private fun createObservers() {
        //Required for Observing and displaying Events
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
               Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.progData.observe(viewLifecycleOwner) {
            if(it){
                showProgressBar()
            }else{
                dismissProgressBar()
            }
        }

        viewModel.articlesData.observe(viewLifecycleOwner) {
            if(it!=null){
                Log.d("onCreate", "Search: Call 2")
                adapter.differ.submitList(it)
                //adapter.submitList(it)
            }
        }
    }

    private fun dismissProgressBar() {
        binding.progBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progBar.visibility = View.VISIBLE
    }

    private fun setButtonClicks() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    /*override fun onItemClick(item: Page, position: Int) {
        Log.d("onCreate", "listItem position clicked : $position")
        Log.d("onCreate", "listItemClicked: $item")
        if (::navController.isInitialized) {
            // navController is initialized
            val bundle= bundleOf(
                    "article" to item
            )
            navController.navigate(R.id.action_articlesFragment_to_articleDetailFragment, bundle)
        }
    }*/


    //ITEM Click on each item of the Recycler View
    private fun listItemClicked(article : Page, position: Int) {
        Log.d("onCreate", "listItem position clicked : $position")
        Log.d("onCreate", "listItemClicked: $article")
        if (::navController.isInitialized) {
            // navController is initialized
            val bundle= bundleOf(
                    "article" to article
            )
            navController.navigate(R.id.action_articlesFragment_to_articleDetailFragment,bundle)
        }
    }

}