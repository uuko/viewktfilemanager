package com.example.ktforfilemanager.base.api

import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("searchAllFile")
    fun getFilenameAndUrl(): Deferred<List<UrLAndFilenameResponse>>

}