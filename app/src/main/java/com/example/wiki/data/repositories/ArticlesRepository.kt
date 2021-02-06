package com.example.wiki.data.repositories

import com.example.wiki.data.db.AppDatabase
import com.example.wiki.data.db.entities.Article
import com.example.wiki.data.network.WikiApi
import com.example.wiki.data.network.responses.ArticlesResponse
import retrofit2.Response

class ArticlesRepository(private val api: WikiApi, private val db: AppDatabase) {

    val savedArticles = db.getArticlesDao().getAllSavedArticles()

    suspend fun getSearchedArticles(searchQuery:String): Response<ArticlesResponse> {
        return api.getSearchedArticles(
            "query",
            "json",
            "pageimages|pageterms|info", //pageimages%7Cpageterms%7Cinfo
            "prefixsearch",
            1,
            2,
            "thumbnail",
            50,
            20,
            "description",
            searchQuery,
            20,
            "url"
        )
    }

    suspend fun insertArticle(article: Article): Long {
        return db.getArticlesDao().insertArticle(article)
    }


    suspend fun delete(article: Article): Int {
        return db.getArticlesDao().deleteArticle(article)
    }
}