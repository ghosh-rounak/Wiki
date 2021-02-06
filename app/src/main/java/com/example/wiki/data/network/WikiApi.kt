package com.example.wiki.data.network

import com.example.wiki.data.network.responses.ArticlesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WikiApi {


    @GET("api.php")
    suspend fun getSearchedArticles(
        @Query(
            "action"
        ) action: String,
        @Query(
            "format"
        ) format: String,
        @Query(
            "prop"
        ) prop: String,
        @Query(
            "generator"
        ) generator: String,
        @Query(
            "redirects"
        ) redirects: Int,
        @Query(
            "formatversion"
        ) formatversion: Int,
        @Query(
            "piprop"
        ) piprop: String,
        @Query(
            "pithumbsize"
        ) pithumbsize: Int,
        @Query(
            "pilimit"
        ) pilimit: Int,
        @Query(
            "wbptterms"
        ) wbptterms: String,
        @Query(
            "gpssearch"
        ) gpssearch: String,
        @Query(
            "gpslimit"
        ) gpslimit: Int,
        @Query(
            "inprop"
        ) inprop: String
    ): Response<ArticlesResponse>





    companion object{
        operator fun invoke(

        ) : WikiApi{

            //Create Logger
            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okkHttpclient = OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS) //best practice is to reduce timeout and show retry on failure
                .addInterceptor(logger)//logcat->verbose->okhttp  (debug process)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://en.wikipedia.org//w/")  //BASE URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WikiApi::class.java)
        }
    }

}