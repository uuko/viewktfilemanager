package com.example.ktforfilemanager.chooseview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ktforfilemanager.R
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent

import android.provider.MediaStore


import android.app.Activity
import android.content.ContentUris
import android.graphics.BitmapFactory

import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import com.example.ktforfilemanager.paintphoto.PaintPhotoViewModel
import okhttp3.*
import java.io.File
import java.lang.Exception
import android.R.string.cancel
import android.app.ProgressDialog
import android.content.Context


class PickerPhotoActivity : AppCompatActivity() {
    private val viewModel = PickerPhotoViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_photo)
        var btn2=findViewById<Button>(R.id.btn2)
        btn2.setOnClickListener {
            val intent = Intent()
            /* 開啟Pictures畫面Type設定為image */
            intent.type = "image/*"
            /* 使用Intent.ACTION_GET_CONTENT這個Action */
            intent.action = Intent.ACTION_GET_CONTENT
            /* 取得相片後返回本畫面 */
            startActivityForResult(intent, 1)
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1//這裡的requestCode是我自己設定的,就是確定返回到那個Activity的標誌
            -> if (resultCode === Activity.RESULT_OK) {//resultcode是setResult裡面設定的code值
                var path= FileUtils.getPath(this,data?.data)
                var ff= File(path);
                Log.d("aaaa",path.toString())
                Log.d("aaaawqweqq",path.lastIndexOf("/").toString()+path.lastIndexOf(".").toString())
                Log.d("aaaa",path.substring(path.lastIndexOf("/")+1,path.lastIndexOf(".")))
                var fileOrgName=path.substring(path.lastIndexOf("/")+1)

                viewModel.getListData(fileOrgName,path)
                showProgressDialog("loading",this)
                val nameObserver = Observer<PickerPhotoResponse> {
                        list ->
                            if (!list.progressShow){
                                dismissProgressDialog()
                                if (list.message=="ok"){
                                    var bitmap = BitmapFactory.decodeFile( ff.path )
                                    var img=findViewById<ImageView>(R.id.ff)
                                    img.setImageBitmap(bitmap)
                                }else{
                                    Toast.makeText(this,list.message,Toast.LENGTH_LONG)
                                }

                            }
                }

                // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
                viewModel.showProgress.observe(this, nameObserver)

            }

            }
        }
    lateinit var progressDialog:ProgressDialog
    fun showProgressDialog(s: String,c:Context) {

         progressDialog = ProgressDialog(this)
        progressDialog.setMessage("waiting")
        /*dialog彈出後會點擊螢幕或物理返回鍵，dialog不消失*/
        progressDialog.setCancelable(false)
        /*dialog彈出後會點擊螢幕，dialog不消失；點擊物理返回鍵dialog消失*/
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel()
        }

    }
    }

