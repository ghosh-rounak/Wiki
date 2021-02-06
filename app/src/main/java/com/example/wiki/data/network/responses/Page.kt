package com.example.wiki.data.network.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Page(
    @SerializedName("canonicalurl")
    val canonicalurl: String,
    @SerializedName("contentmodel")
    val contentmodel: String,
    @SerializedName("editurl")
    val editurl: String,
    @SerializedName("fullurl")
    val fullurl: String,
    @SerializedName("index")
    val index: Int,
    @SerializedName("lastrevid")
    val lastrevid: Int,
    @SerializedName("length")
    val length: Int,
    @SerializedName("ns")
    val ns: Int,
    @SerializedName("pageid")
    val pageid: Long,
    @SerializedName("pagelanguage")
    val pagelanguage: String,
    @SerializedName("pagelanguagedir")
    val pagelanguagedir: String,
    @SerializedName("pagelanguagehtmlcode")
    val pagelanguagehtmlcode: String,
    @SerializedName("terms")
    val terms: Terms?,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail?,
    @SerializedName("title")
    val title: String,
    @SerializedName("touched")
    val touched: String
): Serializable