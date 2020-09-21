package com.example.ktforfilemanager.bigphoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.ktforfilemanager.R
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import android.graphics.Bitmap
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.graphics.BitmapFactory
import android.view.ViewParent
import androidx.viewpager.widget.ViewPager
import com.google.firebase.analytics.FirebaseAnalytics


class BigPhotoActivity : AppCompatActivity() {
    companion object {
        var FILEPOSITION: String="FILEPOSITION"
        var FILELIST: String="FILELIST"
    }

    lateinit var bigView:ImageView
    lateinit var pager:ViewPager
    private  var dataList:ArrayList<UrLAndFilenameResponse> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.ktforfilemanager.R.layout.activity_big_photo)

//        bigView=findViewById(com.example.ktforfilemanager.R.id.imageBig)
        pager=findViewById(R.id.pager)

        var bundle=intent.extras
        var position= bundle!!.getInt(FILEPOSITION)
//        val bitmapimage = intent.extras!!.getParcelable<Bitmap>("BitmapImage")
        dataList= bundle.getSerializable(FILELIST) as ArrayList<UrLAndFilenameResponse>
//        val b = BitmapFactory.decodeByteArray(
//            dataList.get(position).bitmap, 0, dataList.get(position).bitmap.size
//        )
//        bigView.setImageBitmap(b)


        var adapter =  FullScreenImageAdapter(this,dataList)
        pager.adapter=adapter
        pager.currentItem=position
    }
}
