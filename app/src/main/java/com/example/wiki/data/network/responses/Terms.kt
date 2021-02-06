package com.example.wiki.data.network.responses


import com.google.gson.annotations.SerializedName

data class Terms(
    @SerializedName("description")
    val description: List<String>?
)