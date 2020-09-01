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

    private val mBounds: Rect

    private var mCount: Int = 0


    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBounds = Rect()
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.setColor(Color.BLUE)
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPaint)
        mPaint.setColor(Color.YELLOW)
        mPaint.setTextSize(30F)
        val text = mCount.toString()
        mPaint.getTextBounds(text, 0, text.length, mBounds)
        val textWidth = mBounds.width()
        val textHeight = mBounds.height()
        canvas.drawText(
            text,
            (getWidth() / 2 - textWidth / 2).toFloat(),
            (getHeight() / 2 + textHeight / 2).toFloat(),
            mPaint
        )
    }

    override fun onClick(v: View) {
        mCount++
        invalidate()
    }


}