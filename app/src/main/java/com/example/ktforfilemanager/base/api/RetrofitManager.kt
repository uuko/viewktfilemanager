package com.example.ktforfilemanager.base

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager private constructor(){
    private val retrofit : Retrofit
    private val httpClient:OkHttpClient= OkHttpClient()

    //初始化
    init {
        retrofit=Retrofit.Builder()
            .baseUrl("http://\uFEFF192.168.1.227:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    companion object{
        private val manager=RetrofitManager()
        val client :Retrofit
            get()= manager.retrofit
    }
}