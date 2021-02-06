package com.example.wiki.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.wiki.data.db.entities.Article

@Dao
interface ArticlesDao {
    @Insert
    suspend fun insertArticle(article: Article): Long  //returns id of new row

    @Delete
    suspend fun deleteArticle(article: Article) : Int  //returns no of rows deleted

    @Query("SELECT * FROM  articles_table")
    fun getAllSavedArticles(): LiveData<List<Article>>
}