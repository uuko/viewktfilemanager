package com.example.ktforfilemanager.paintview

import android.view.MotionEvent

import android.R.attr.path
import android.R.attr.strokeWidth
import android.R.color
import android.content.Context
import android.graphics.*

import android.util.DisplayMetrics

import android.util.AttributeSet
import android.view.View

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.icu.lang.UCharacter.GraphemeClusterBreak.T









class PaintView (context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mMode = 0
    private var mX: Float = 0.toFloat()
    private var mY: Float = 0.toFloat()
    private var mPath: Path= Path()
    private val mPaint: Paint
    private lateinit var  mEraserPaint: Paint
    private var currentColor: Int = 0
    private var bgColor = DEFAULT_BG_COLOR
    private var strokeWidth: Float = 0F
    private var emboss: Boolean = false
    private var blur: Boolean = false
    private val mEmboss: MaskFilter
    private val mBlur: MaskFilter

    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)
    private lateinit var mBufferBitmap: Bitmap
    private lateinit var mBufferCanvas: Canvas

    private  var paths:MutableList<PaintPath> =arrayListOf()
    init {
        setFocusable(true);

        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)
        mPaint.setColor(DEFAULT_COLOR)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setStrokeJoin(Paint.Join.ROUND)
        mPaint.setStrokeCap(Paint.Cap.ROUND)
        mPaint.setXfermode(null)
        mPaint.setAlpha(0xff)

//
//        mEraserPaint = Paint()
//        mEraserPaint.setAlpha(0)
//        //这个属性是设置paint为橡皮擦重中之重
//        //这是重点
//        //下面这句代码是橡皮擦设置的重点
//        mEraserPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
//        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
//        mEraserPaint.setAntiAlias(true)
//        mEraserPaint.setDither(true)
//        mEraserPaint.setStyle(Paint.Style.STROKE)
//        mEraserPaint.setStrokeJoin(Paint.Join.ROUND)
//        mEraserPaint.setStrokeWidth(30F)
       mEmboss = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.4f, 6f, 3.5f)
        mBlur = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
    }



    fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSH_SIZE
    }

    fun normal() {
        emboss = false
        blur = false
    }

    fun eraser(){
        mMode=1
        mPaint.xfermode=PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    fun emboss() {
        emboss = true
        blur = false
    }

    fun blur() {
        emboss = false
        blur = true
    }

    fun clear() {
        bgColor = DEFAULT_BG_COLOR
        paths.clear()
        normal()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas!!.drawColor(bgColor)

        for (fp in paths) {
            mPaint.setColor(fp.currentColor)
            mPaint.setStrokeWidth(fp.paintWidth)
            mPaint.setMaskFilter(null)

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss)
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur)


            mCanvas!!.drawPath(fp.mPath, mPaint)



        }

        canvas.drawBitmap(mBitmap, 0F, 0F, mBitmapPaint)
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = PaintPath(currentColor, emboss, blur, strokeWidth, mPath)
        paths.add(fp)

        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y


    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
           mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)

            invalidate()
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }

    companion object {

        var BRUSH_SIZE = 20F
        val DEFAULT_COLOR = Color.RED
        val DEFAULT_BG_COLOR = Color.WHITE
        private val TOUCH_TOLERANCE = 4f
    }
}