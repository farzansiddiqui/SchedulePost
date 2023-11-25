package com.siddiqui.schedulepost.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //val okHttpClientBuilder = OkHttpClient.Builder()
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://graph.facebook.com/v18.0/").
        addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiInterface:RetrofitApi by lazy {
        retrofit.create(RetrofitApi::class.java)
    }

}