package com.example.ktforfilemanager.paintphoto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.ktforfilemanager.base.RetrofitManager
import com.example.ktforfilemanager.base.api.ApiService
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PaintPhotoViewModel():ViewModel(){
    var list: MutableLiveData<List<UrLAndFilenameResponse>> = MutableLiveData()
    var bitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val apiManager = RetrofitManager.client.create(ApiService::class.java)


    fun getListData(){
        CoroutineScope(Dispatchers.IO).launch {
            //waint
            val result = apiManager.getFilenameAndUrl().await()
            val response=result
//            withContext(Dispatchers.Main){
//                list.value=response
//            }
            list.postValue(response)
        }
    }

    fun getBitmap(ur :UrLAndFilenameResponse){
        val url = URL(ur.fileUrl)

        CoroutineScope(Dispatchers.IO).launch {
            //waint
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            bitmap.postValue(image)
        }
    }
}
