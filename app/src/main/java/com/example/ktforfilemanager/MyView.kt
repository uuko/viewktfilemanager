package com.example.ktforfilemanager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth

import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.ViewGroup
import com.example.ktforfilemanager.defview.UpDownObject
import com.example.ktforfilemanager.myview.MyViewDataClass
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T






class
MyView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private val mPaint: Paint
    private var fallObjects: MutableList<MyViewDataClass>? = ArrayList()
    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec);
        var heightMode = MeasureSpec.getMode(heightMeasureSpec);
        var widthSize = MeasureSpec.getSize(widthMeasureSpec);
        var heightSize = MeasureSpec.getSize(heightMeasureSpec);


        val childView = getChildAt(0)
        val lp = childView.layoutParams
        measureChild(childView, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec)
    }
//
//    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var child=childCount-1
        for (i in 0.. child){
            var chView=getChildAt(i)
            var  width:Int = chView.measuredWidth
            var  height :Int = chView.measuredHeight

            var  left = (r - width) / 2;
            var  top = (b - height) / 2;
            var  right =  left + width
            var  bottom = top + height

            var myViewDataClass=MyViewDataClass(l.toFloat(),t.toFloat(),r.toFloat(),b.toFloat())
            fallObjects?.add(myViewDataClass)
            chView.layout(left, top, right,bottom);
            invalidate()
        }
    }
    override fun onDraw(canvas: Canvas) {
        mPaint.setColor(Color.YELLOW)
//        canvas.drawRect(0F, 0F, width.toFloat() ,height.toFloat(), mPaint)
//        mPaint.setColor(Color.BLUE)
//        mPaint.setTextSize(100.0F)
//        val text = "Hello View"
//        canvas.drawText(text, 0F, height.toFloat()/2, mPaint)

//        mPaint.setColor(Color.GREEN)
//        var child=childCount-1
////        val childView = getChildAt(0)
////        childView.layout(20, 20, childView.measuredWidth, childView.measuredHeight)
//        for (i in 0.. child){
//            canvas.drawRect(fallObjects!!.get(i).l,fallObjects!!.get(i).t,fallObjects!!.get(i).r,fallObjects!!.get(i).b,mPaint);
//        }
    }
}