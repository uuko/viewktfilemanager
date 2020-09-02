package com.example.ktforfilemanager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth

import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View


class CounterView(context: Context, attrs: AttributeSet) : View(context, attrs),
    View.OnClickListener
     {

    private val mPaint: Paint

    private val mRect: Rect

    private var mCount: Int = 0
    private var  uuko:String="hello world"


    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRect = Rect()
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.setColor(Color.GREEN)
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPaint)
        mPaint.setColor(Color.YELLOW)
        mPaint.setTextSize(40F)
        val text = uuko
        mPaint.getTextBounds(text, 0, text.length, mRect)
        val textWidth = mRect.width()
        val textHeight = mRect.height()
        canvas.drawText(
            text,
            (getWidth() / 2 - textWidth / 2).toFloat(),
            (getHeight() / 2 + textHeight / 2).toFloat(),
            mPaint
        )
    }

    override fun onClick(v: View) {
        mCount++
        if (mCount%2==1){
            uuko="hello world"
        }else{
            uuko="goodNight world"
        }


        invalidate()
    }


}