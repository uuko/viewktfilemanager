package com.example.ktforfilemanager.bigphoto

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ktforfilemanager.R
import com.example.ktforfilemanager.base.pojo.UrLAndFilenameResponse
import com.ortiz.touchview.TouchImageView
import java.io.File

class FullScreenImageAdapter // constructor
    (
    private val _activity: Activity,
    private val mediaFileListModelArrayList: ArrayList<UrLAndFilenameResponse>
) :
    PagerAdapter() {
    override fun getCount(): Int {
        return mediaFileListModelArrayList.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = _activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout: View =
            inflater.inflate(R.layout.layout_fullscreen_image, container, false)
        val imgDisplay: TouchImageView =
            viewLayout.findViewById<View>(R.id.imgDisplay) as TouchImageView
        val b = BitmapFactory.decodeByteArray(
            mediaFileListModelArrayList.get(position).bitmap, 0, mediaFileListModelArrayList.get(position).bitmap.size
        )
        imgDisplay.setImageBitmap(b)
        (container as ViewPager).addView(viewLayout)
        return viewLayout
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        (container as ViewPager).removeView(`object` as RelativeLayout)
    }

}
