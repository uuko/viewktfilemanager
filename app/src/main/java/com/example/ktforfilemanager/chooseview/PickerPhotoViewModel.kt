package com.example.ktforfilemanager.chooseview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import java.net.ConnectException

class PickerPhotoViewModel (): ViewModel(){
    var showProgress: MutableLiveData<PickerPhotoResponse> = MutableLiveData()

    fun getListData(fileOrgName :String,path:String,userName:String="uu"){
        CoroutineScope(Dispatchers.IO).launch {
            //waint
           try {
               val client = OkHttpClient().newBuilder()
                   .build()
               val mediaType = MediaType.parse("text/plain")
               val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                   .addFormDataPart(
                       "file", fileOrgName,
                       RequestBody.create(
                           MediaType.parse("application/octet-stream"),
                           File(path)
                       )
                   )
                   .addFormDataPart("userName", userName)
                   .build()
               val request = Request.Builder()
                   .url("http://192.168.1.227:8080/uploadFile")
                   .method("POST", body)
                   .build()
               val response = client.newCall(request).execute()

               Log.d("aaa",response.toString())
               if (response.isSuccessful){
                   var p=PickerPhotoResponse("ok",false)

                   showProgress.postValue(p)
               }
               else{
                   var p=PickerPhotoResponse(response.message(),false)
                   showProgress.postValue(p)
               }
           }catch (e : ConnectException){
               var p=PickerPhotoResponse("internnet connection",false)
               showProgress.postValue(p)
           }

        }
    }
}