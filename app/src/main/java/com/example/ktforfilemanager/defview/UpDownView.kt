package com.example.ktforfilemanager.defview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.AttributeSet
import android.util.Log
import javax.annotation.Nullable
import android.view.ViewTreeObserver
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class UpDownView : View {


    companion object {

        private val defaultWidth = 600//默认宽度
        private val defaultHeight = 1000//默认高度
        private val intervalTime = 5//重绘间隔时间
    }

    private  var viewHeight : Int=0
    private  var viewWidth : Int=0
    private  var waitingTime : Int = 5

    private lateinit var drawPaint: Paint
    private var  objectY : Int=0
    private var  objectX : Int =100

    private lateinit var mContext: Context
    private lateinit var mAttr: AttributeSet

    private var fallObjects: MutableList<UpDownObject>? = null
   constructor(context: Context,@Nullable attr :AttributeSet) : super(context,attr){
       mContext=context
       mAttr=attr
       init()
   }

    private fun init() {
//        drawPaint= Paint()
//        drawPaint.color=(Color.MAGENTA)
//        drawPaint.style=Paint.Style.FILL
//        objectY=0

        fallObjects = ArrayList()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var mHeight : Int=measureSize(defaultHeight,heightMeasureSpec)
        var mWidth : Int=measureSize(defaultWidth,widthMeasureSpec)

        setMeasuredDimension(mWidth,mHeight)

        viewHeight=mHeight
        viewWidth=mWidth

    }

    private fun measureSize(defaultSize : Int,measureSpec: Int): Int {

        var resultSize : Int =defaultSize
        var measureMode = MeasureSpec.getMode(measureSpec)
        var measureSize = MeasureSpec.getSize(measureSpec)

        when (measureMode){
            MeasureSpec.EXACTLY -> resultSize=measureSize
            MeasureSpec.AT_MOST -> Math.min(resultSize,measureSize)
        }
        return  resultSize
    }





    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (fallObjects!!.size > 0) {
            for (i in fallObjects!!.indices) {
                fallObjects!![i].drawObject(canvas)
            }

            handler.postDelayed(runnable,5L)
        }
    }


    private val runnable = Runnable { invalidate() }


    fun addFallObject(fallObject: UpDownObject, num: Int) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                for (i in 0 until num) {
                    val newFallObject = UpDownObject(fallObject.builder!!, viewWidth, viewHeight)
                    fallObjects!!.add(newFallObject)
                }
                invalidate()
                return true
            }
        })
    }
//    var direction=true
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//
//        canvas!!.drawCircle(objectX.toFloat(),objectY.toFloat(), 25F,drawPaint)
//        if (objectX>viewWidth){
//            objectX=0
//        }
//        if(!direction){
//            Log.d("qqqqqqqqwwq","fff")
//            handler.postDelayed(corunnable, intervalTime.toLong())
//        }else{
//            handler.postDelayed(runnable, intervalTime.toLong())
//        }
//
//    }
//    private var  corunnable= Runnable {
//        var tempY=objectY-15
//
//        objectY=tempY
//        Log.d("qqqqqqqqwwq",""+objectY)
//        if (objectY<0){
//            Log.d("qqqqqqqqwwq","ttt"+objectY)
//            direction=true
//            objectX+=15
//            objectY=0
//
//        }
//        invalidate()
//    }
//    private var  runnable= Runnable {
//        objectY=objectY+15
//        if (objectY>viewHeight){
//            direction=false
//            objectX+=15
//        }
//        invalidate()
//    }



}