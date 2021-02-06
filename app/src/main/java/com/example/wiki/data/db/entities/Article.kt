package com.example.wiki.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles_table")
data class Article(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "pageid")
        val pageid:Long,
        @ColumnInfo(name = "title")
        val title: String?,
        @ColumnInfo(name = "thumbnail")
        val thumbnail: String?,
        @ColumnInfo(name = "description")
        val description: String?,
        @ColumnInfo(name = "fullurl")
        val fullurl: String?
): Serializable