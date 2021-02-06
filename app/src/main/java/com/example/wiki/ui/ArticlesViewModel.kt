package com.example.wiki.ui

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wiki.data.db.entities.Article
import com.example.wiki.data.network.responses.ArticlesResponse
import com.example.wiki.data.network.responses.Page
import com.example.wiki.data.repositories.ArticlesRepository
import com.example.wiki.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ArticlesViewModel(private val repository: ArticlesRepository) : ViewModel(), Observable {
    private var listOfArticles: MutableList<Page>? = null

    //Required for Events
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> //to be Observed from UI for displaying Events
        get() = statusMessage

    private var articles = MutableLiveData<MutableList<Page>>()
    val articlesData: LiveData<MutableList<Page>>
        get() = articles

    private var prog = MutableLiveData<Boolean>()
    val progData: LiveData<Boolean>
        get() = prog




    val savedArticles: LiveData<List<Article>> =
            repository.savedArticles // Live Data to be observed from Main Activity


    fun getSearchedArticles(searchQuery: String) = viewModelScope.launch {
        safeSearchedArticlesApiCall(searchQuery)
    }

    private suspend fun safeSearchedArticlesApiCall(searchQuery: String) {
        prog.postValue(true)
        try {
            val response = repository.getSearchedArticles(searchQuery)
            handleSearchedArticlesResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    prog.postValue(false)
                    statusMessage.value = Event("Network Failure")
                }
                else -> {
                    prog.postValue(false)
                    statusMessage.value = Event(t.message ?: "Unknown Error")
                    t.printStackTrace()
                }
            }
        }
    }

    private fun handleSearchedArticlesResponse(response: Response<ArticlesResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(resultResponse.query?.pages != null){
                    listOfArticles = resultResponse.query.pages as MutableList<Page>
                    articles.postValue(listOfArticles) //to be observed from ui
                }
                prog.postValue(false)
            }
        } else {
            val errorResponse = response.errorBody()?.string()
            Log.d("onCreate", "handleRounakQuotesResponse Error: $errorResponse")
            prog.postValue(false)
            statusMessage.value = Event(response.message())
        }
    }


    fun saveInDB(page: Page) {
        Log.d("onCreate", "saveInDB: called $page")
        var picUrl:String?=null
        Log.d("onCreate", "page.thumbnail: ${page.thumbnail}")
        if(page.thumbnail!=null){
             picUrl = page.thumbnail.source
        }
        var desc:String? =null
        if(page.terms!=null){
            if(page.terms.description!=null && page.terms.description.size>=0){
                desc = page.terms.description[0]
            }
        }

        val article = Article(
                page.pageid,
                page.title,
                picUrl,
                desc,
                page.fullurl
        )

        insert(article)
    }

    //INSERT OPERATION
    private fun insert(article: Article) = viewModelScope.launch {
        try {
            val newRowId = repository.insertArticle(article)
            Log.d("onCreate", "newRowId Inserted: $newRowId")
            prog.postValue(false)
            statusMessage.value = Event("Save Success")
        } catch (e: Exception) {
            prog.postValue(false)
            if (e is SQLiteConstraintException) {
                e.printStackTrace()
                Log.d("onCreate", "insert: ${e.message}")
                //statusMessage.value = Event("SQLiteConstraintException")
            } else {
                e.printStackTrace()
                statusMessage.value = Event("Error Occurred during Insertion")
            }

        }
    }


    //DELETE
    fun deleteArticleFromDB(article: Article, position: Int) {
        Log.d("onCreate", "deleteMovieFromDB position passed : $position ")
        deleteArticle(article,position)
    }


    //DELETE OPERATION
    private fun deleteArticle(article: Article,position: Int) = viewModelScope.launch {
        Log.d("onCreate", "deleteMovieFromDB position passed : $position ")
        val noOfRowsDeleted = repository.delete(article)

        if (noOfRowsDeleted > 0) {
            prog.postValue(false)
            statusMessage.value = Event("$noOfRowsDeleted Item Deleted Successfully")
        } else {
            prog.postValue(false)
            statusMessage.value = Event("Error Occurred During Deletion")
        }

    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}