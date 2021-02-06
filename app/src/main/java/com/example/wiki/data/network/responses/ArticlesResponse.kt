package com.example.wiki.data.network.responses


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArticlesResponse(
    @SerializedName("batchcomplete")
    val batchcomplete: Boolean,
    @SerializedName("continue")
    val continueX: Continue,
    @SerializedName("query")
    val query: Query?
): Serializable