package com.example.wiki.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.wiki.R
import com.example.wiki.databinding.ActivityMainBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity() , KodeinAware {
    override val kodein by kodein()
    private val factory: ArticlesViewModelFactory by instance()

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, factory).get(ArticlesViewModel::class.java)
        binding.articlesViewModel = viewModel
        binding.lifecycleOwner = this

    }

}