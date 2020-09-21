package com.example.ktforfilemanager.base.pojo

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by uuko on 2020-09-17
 */
data class UrLAndFilenameResponse(
    var fileName: String,
    var fileUrl: String,
    var status:Boolean=true,
     var bitmap: ByteArray):Serializable


//Parceable