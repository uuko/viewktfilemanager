package com.example.ktforfilemanager.defview

import android.animation.AnimatorSet
import android.graphics.Bitmap
import android.net.Uri
import java.util.*
import android.R.attr.bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable


class UpDownObject {
    //  init view
    private  var initX : Int=0
    private  var initY : Int=0
    private lateinit var one: Integer
    private lateinit var random: Random
    private  var parentWidth : Int=0
    private  var parentHeight : Int=0
    private  var upDownWidth : Int=0
    private  var upDownHeight : Int=0

    private  var initSpeed : Float =0F
    private  var presentX : Float=0F
    private  var presentY : Float=0F
    private  var presentSpeed : Float=0F
    private var isSpeedRandom: Boolean = false//物体初始下降速度比例是否随机
    private var isSizeRandom: Boolean = false//物体初始大小比例是否随机
    private var bitmap : Bitmap
    var builder: Builder? = null

    //constuctor 1
    constructor(builder : Builder,parentWidth :Int,parentHeight : Int){
        random= Random()
        // get Parent Height / Width
        this.parentHeight=parentHeight
        this.parentWidth=parentWidth
        // random init X Y
        initX=random.nextInt(parentWidth)
        initY=random.nextInt(parentHeight)-parentHeight
        // now X Y
        presentX=initX.toFloat()
        presentY=initY.toFloat()
        // get Down Speed
        presentSpeed=builder.initSpeed
        // get Bitmap
        bitmap=builder.bitmap
        // Down Bitmap width & Height
        upDownWidth=bitmap!!.width
        upDownHeight=bitmap!!.height

        this.builder = builder;
        isSpeedRandom = builder.isSpeedRandom;
        isSizeRandom = builder.isSizeRandom;

        initSpeed = builder.initSpeed;
        randomSpeed();
        randomSize();

    }

    private fun randomSize() {
        if (isSizeRandom) {
            val r = (random.nextInt(10) + 1) * 0.1f
            val rW = r * builder!!.bitmap.width
            val rH = r * builder!!.bitmap.height
            bitmap = changeBitmapSize(builder!!.bitmap, rW.toInt(), rH.toInt())
        } else {
            bitmap = builder!!.bitmap
        }
        upDownWidth = bitmap.width
        upDownHeight = bitmap.height
    }

    //constuctor 2
    constructor(builder: Builder){
        this.builder = builder
        initSpeed = builder.initSpeed
        bitmap = builder.bitmap

        isSpeedRandom = builder.isSpeedRandom;
        isSizeRandom = builder.isSizeRandom;
    }

    open class Builder() {
        public var initSpeed: Float = 0F
        public  lateinit var bitmap: Bitmap

        constructor(bitmap: Bitmap,defaultSpeed:Float) : this() {
          this.bitmap =(bitmap);
          this.initSpeed=defaultSpeed
            Log.d("init",""+initSpeed)
        }

         var isSpeedRandom: Boolean = false
         var isSizeRandom: Boolean = false

        fun Builder(bitmap: Bitmap) {
            //省略部分代码...
            this.isSpeedRandom = false
            this.isSizeRandom = false
        }

        fun Builder(drawable: Drawable) {
            //省略部分代码...
            this.isSpeedRandom = false
            this.isSizeRandom = false
        }

        /**
         * 设置物体的初始下落速度
         * @param speed
         * @return
         */
        fun setSpeed(speed: Int): Builder {
            this.initSpeed = speed.toFloat()
            return this
        }

        /**
         * 设置物体的初始下落速度
         * @param speed
         * @param isRandomSpeed 物体初始下降速度比例是否随机
         * @return
         */
        fun setSpeed(speed: Int, isRandomSpeed: Boolean): Builder {
            this.initSpeed = speed.toFloat()
            this.isSpeedRandom = isRandomSpeed
            return this
        }



        /**
         * 设置物体大小
         * @param w
         * @param h
         * @param isRandomSize 物体初始大小比例是否随机
         * @return
         */
        fun setSize(w: Int, h: Int, isRandomSize: Boolean): Builder {
            this.bitmap = changeBitmapSize(this.bitmap, w, h)
            this.isSizeRandom = isRandomSize
            return this
        }

        constructor(drawable: Drawable) :this(){
            this.initSpeed = 10F
            this.bitmap = drawableToBitmap(drawable)
        }

        private fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                if (drawable.getOpacity() !== PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            return bitmap
        }
        fun setSize(w: Int, h: Int): Builder {
            this.bitmap = changeBitmapSize(this.bitmap, w, h)
            return this
        }
        fun changeBitmapSize(bitmap: Bitmap, w: Int, h: Int): Bitmap {
            val oldW = bitmap.width
            val oldH = bitmap.height
            // 计算缩放比例
            val scaleWidth = w.toFloat() / oldW
            val scaleHeight = h.toFloat() / oldH
            // 取得想要缩放的matrix参数
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            // 得到新的图片
            var bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true)
            return bitmap
        }


        fun setSpeed(speed: Float): Builder {
            this.initSpeed = speed
            return this
        }

        fun build(): UpDownObject {
            return UpDownObject(this)
        }
    }

    public fun changeBitmapSize(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val oldW = bitmap.width
        val oldH = bitmap.height
        // 计算缩放比例
        val scaleWidth = w.toFloat() / oldW
        val scaleHeight = h.toFloat() / oldH
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        var bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true)
        return bitmap
    }

    fun drawObject(canvas: Canvas) {
        moveObject()
        canvas.drawBitmap(bitmap, presentX, presentY, null)
    }

    /**
     * 移动物体对象
     */
    private fun moveObject() {
        moveY()
        if (presentY > parentHeight) {
            reset()
        }
    }


    private fun moveY() {
        presentY += presentSpeed
    }


    private fun reset() {
        presentY = -upDownHeight.toFloat()
        presentSpeed = 10F
        randomSpeed();

    }

    private fun randomSpeed() {
        if(isSpeedRandom){
            presentSpeed = (((random.nextInt(3)+1)*0.1+1)* initSpeed).toFloat()
        }else {
            presentSpeed = 10F;
        }
    }


}