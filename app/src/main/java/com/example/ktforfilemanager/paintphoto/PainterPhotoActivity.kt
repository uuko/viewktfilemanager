package com.example.ktforfilemanager.paintphoto

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.crashlytics.android.Crashlytics
import com.example.ktforfilemanager.R
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import com.google.firebase.analytics.FirebaseAnalytics


class PainterPhotoActivity : AppCompatActivity() {
    lateinit var imgView: ImageView
    private val viewModel = PaintPhotoViewModel()
    lateinit var  linearLayout: LinearLayout
    lateinit var  recyclerView: RecyclerView
    lateinit var adapter: PhotoGalleryAdapter;
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painter_photo)
        firebaseAnalytics= FirebaseAnalytics.getInstance(this)


//        imgView=findViewById(R.id.imgView)
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL




        recyclerView=findViewById(R.id.rrrr)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        var adapter=PhotoGalleryAdapter()
        recyclerView.adapter =adapter
        viewModel.getListData()
        // Create the observer which updates the UI.
        val nameObserver = Observer<List<UrLAndFilenameResponse>> {

            list ->
                Log.d("fuck","dddd")
                adapter.addList(list,this)
                adapter.notifyDataSetChanged()
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.list.observe(this, nameObserver)
    }



}
