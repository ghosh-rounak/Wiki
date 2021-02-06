package com.example.wiki.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wiki.data.repositories.ArticlesRepository
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class ArticlesViewModelFactory(private val repository: ArticlesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArticlesViewModel::class.java)){
            return ArticlesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}