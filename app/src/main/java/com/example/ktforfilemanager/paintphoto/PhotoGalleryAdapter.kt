package com.example.ktforfilemanager.paintphoto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView


import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse


import com.example.ktforfilemanager.bigphoto.BigPhotoActivity

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.ktforfilemanager.R


import java.io.ByteArrayOutputStream


class PhotoGalleryAdapter : RecyclerView.Adapter<PhotoGalleryAdapter.MyViewHolder>() {
    private val viewModel = PaintPhotoViewModel()
    private lateinit var mContext :Context
    private  var ll:ArrayList<UrLAndFilenameResponse> = arrayListOf()
    companion object {
         var FILEPOSITION: String="FILEPOSITION"
          var FILELIST: String="FILELIST"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_painter_photo, parent, false)
        val holder=MyViewHolder(v)

        return MyViewHolder(v)
    }

    fun addList(list :List<UrLAndFilenameResponse>,mContext :Context){
        ll= ArrayList(list)
        this.mContext=mContext
    }
    override fun getItemCount(): Int {
        Log.d("gggg",""+ll.size)
        return ll.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageView = holder.imageView
        Log.d("urllllllll",ll.get(position).fileUrl)
        Glide.with(mContext).load(ll.get(position).fileUrl).into(imageView)
        val intent1  = Intent(mContext, BigPhotoActivity::class.java)
//        var blist:ArrayList<Bitmap> = arrayListOf()
        viewModel.getBitmap(ll.get(position))
        val nameObserver = Observer<Bitmap> {
                bitmap ->
            val bs = ByteArrayOutputStream()
            var compressB=bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)

            ll.get(position).bitmap=bs.toByteArray()
//            var bb=ll.get(position).bitmap
//            blist.add(bitmap)

            imageView.setOnClickListener {
                var bundle=Bundle()
//                bundle.putParcelable("BitmapImage",blist)
                bundle.putSerializable(FILELIST,ll)
                bundle.putInt(FILEPOSITION,position)
                intent1.putExtras(bundle)
                mContext.startActivity(intent1)
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.bitmap.observe(holder.itemView.context as LifecycleOwner, nameObserver)




    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageButton = itemView.findViewById(R.id.photo)
    }
}

