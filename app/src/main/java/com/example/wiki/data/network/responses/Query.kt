package com.example.wiki.data.network.responses


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Query(
    @SerializedName("pages")
    val pages: List<Page>?,
    @SerializedName("redirects")
    val redirects: List<Redirect>
): Serializable