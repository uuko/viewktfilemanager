package com.example.ktforfilemanager.waveview



import android.content.Context
import com.example.ktforfilemanager.waveview.WaveView
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import kotlin.random.Random


class WaveView(context: Context, attrs: AttributeSet) : View(context, attrs){

    private var mColor = resources.getColor(R.color.holo_red_light)

    private var mImageRadius = 50

    private var mWidth = 3

    private var mMaxRadius = 300

    private var mIsWave = false

    private val mAlphas:MutableList<Int> = ArrayList()

    private val mRadius :MutableList<Int> = ArrayList()


    private val mMoreAlphas:MutableList<Float> = ArrayList()

    private val mMoreRadius :MutableList<Int> = ArrayList()
    private val mMoreXorY :MutableList<WaveDataClass> = ArrayList()
    private val mColorArray :MutableList<Int> = ArrayList()
    private var mPaint: Paint = Paint()
    var  a =Random.nextInt(0, 3)



    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs) {
        init()
        val a = context.obtainStyledAttributes(attrs, com.example.ktforfilemanager.R.styleable.WaveView, defStyleAttr, 0)
        this.mColor = a.getColor(com.example.ktforfilemanager.R.styleable.WaveView_wave_color, mColor)
        this.mWidth = a.getInt(com.example.ktforfilemanager.R.styleable.WaveView_wave_width, mWidth)
        this.mImageRadius = a.getInt(com.example.ktforfilemanager.R.styleable.WaveView_wave_coreImageRadius, mImageRadius)
        a.recycle()
    }


    private fun init() {
        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setStrokeWidth(5F)
//        mColorArray.add(R.color.holo_blue_light)
//        mColorArray.add(R.color.holo_red_light)
//        mColorArray.add(R.color.holo_green_dark)
//        mColorArray.add(R.color.holo_orange_dark)
//                mAlphas.add(255);
//                mRadius.add(0);

    }
    fun start() {
        mIsWave = true
        invalidate()
    }


    fun stop() {
        mIsWave = false
    }


    fun isWave(): Boolean {
        return mIsWave
    }


    fun setColor(colorId: Int) {
        mColor = colorId
    }


    fun setWidth(width: Int) {
        mWidth = width
    }


    fun setMaxRadius(maxRadius: Int) {
        mMaxRadius = maxRadius
    }

    fun setImageRadius(imageRadius: Int) {
        mImageRadius = imageRadius
    }

    fun addWave() {
        mAlphas.add(255)
        mRadius.add(0)
    }

    fun addMoreWave(x:Float,y:Float){
        mMoreAlphas.add(255F)
        mMoreRadius.add(0)
        mMoreXorY.add(WaveDataClass(x,y))
    }

    fun clearWave(){
        mMoreAlphas.clear()
        mMoreRadius.clear()
        mMoreXorY.clear()
    }
    override fun onDraw(canvas: Canvas?) {
        mColorArray.add(R.color.holo_blue_light)
        mColorArray.add(R.color.holo_red_light)
        mColorArray.add(R.color.holo_green_dark)
        mColorArray.add(R.color.holo_orange_dark)

        Log.d("yyyy","mMoreAlphas"+mColorArray[a])


        for (i in mMoreAlphas.indices){
            Log.d("uuuuuu","mMoreAlphas"+mMoreAlphas[i])
            Log.d("uuuuuu","mMoreRadius"+ mMoreRadius[i])
            Log.d("77777777","mMoreRadius"+ mMoreXorY[i].x)
            when(i%4){
                0->mPaint.setColor(resources.getColor(R.color.holo_blue_light))
                1->mPaint.setColor(resources.getColor(R.color.holo_red_light))
                2->mPaint.setColor(resources.getColor(R.color.holo_green_dark))
                3->mPaint.setColor(resources.getColor(R.color.holo_orange_dark))
            }
            var mAlpha: Float = mMoreAlphas[i]
            mPaint.setAlpha(mAlpha!!.toInt())




            val radius = mMoreRadius[i]
            canvas!!.drawCircle(mMoreXorY[i].x.toFloat() , (mMoreXorY[i].y).toFloat(), (radius).toFloat(), mPaint)

            if (mAlpha > 0  && radius<mMaxRadius) {
                mAlpha = (255.0f * (1.0f - ( radius) * 2.0f / mMaxRadius)).toFloat()
                mMoreAlphas[i] = mAlpha
                mMoreRadius[i] = radius + 1
            } else if (mAlpha < 0 ) {

                mMoreRadius.removeAt(i)
                mMoreAlphas.removeAt(i)
            }
        }
        for (i in mAlphas.indices) {
            mPaint.setColor(mColor)
            var alpha: Int? = mAlphas[i]
            mPaint.setAlpha(alpha!!)

            val radius = mRadius[i]
            canvas!!.drawCircle((width / 2).toFloat() , (height / 2).toFloat(), (mImageRadius + radius).toFloat(), mPaint)

            if (alpha > 0 && mImageRadius + radius < mMaxRadius) {
                alpha = (255.0f * (1.0f - (mImageRadius + radius) * 1.0f / mMaxRadius)).toInt()
                mAlphas[i] = alpha
                mRadius[i] = radius + 1
            } else if (alpha < 0 && mImageRadius + radius > mMaxRadius) {

                mRadius.removeAt(i)
                mAlphas.removeAt(i)
            }

        }

        invalidate()
        // 判断当波浪圆扩散到指定宽度时添加新扩散圆
//        if (mRadius.get(mRadius.size - 1) == mWidth) {
//           addWave();
//        }
//        if (mIsWave) {
//            invalidate()
//        }
    }

    override fun invalidate() {
        if (hasWindowFocus()) {
            super.invalidate()
        }
    }
}