package com.example.ktforfilemanager

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


import android.os.Handler
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP

import android.view.View
import android.widget.TextView


import android.media.MediaPlayer
import com.example.ktforfilemanager.waveview.WaveView
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

import android.widget.ImageView
import com.example.ktforfilemanager.ballview.BallView
import com.example.ktforfilemanager.defview.UpDownObject
import com.example.ktforfilemanager.defview.UpDownView


class ProgressActivity : AppCompatActivity() {
    private lateinit var head: ImageView
    private lateinit var wave: WaveView
    private lateinit var scaleAnimation: ScaleAnimation
    private lateinit var mPlayer: MediaPlayer
    private lateinit var fallingView:View
    private lateinit var textView : TextView
    private  var timeSet =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * 按下去擴散
        * */
//        setContentView(R.layout.wave)

//        scaleAnimation = ScaleAnimation(
//            1.2f,
//            1f,
//            1.2f,
//            1f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        )
//        scaleAnimation.setDuration(500)
//        scaleAnimation.setFillAfter(true)
//        wave = findViewById<View>(R.id.wave) as WaveView
//        head = findViewById<View>(R.id.head) as ImageView
//        head.setOnClickListener(View.OnClickListener {
//        wave.addWave()
//        head.startAnimation(scaleAnimation)
////        wave.start()
////        wave.addWave()
//
////            if (mPlayer.isPlaying()) {
////                mPlayer.stop()
////                try {
////                    mPlayer.prepare()
////                } catch (e: IOException) {
////                    e.printStackTrace()
////                }
////
////            }
////            mPlayer.start()
//        })
//        wave.start()


        /*
        * Ball
        * */
        setContentView(R.layout.ball)
        var snowPaint = Paint()
        snowPaint.setColor(Color.BLACK)
        snowPaint.setStyle(Paint.Style.FILL)
        var bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        var bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawCircle(25F, 25F, 25F, snowPaint)


        /*
        *
        * 米奇雨
        * */
//        setContentView(R.layout.updown)
//
//        var snowPaint = Paint()
//        snowPaint.setColor(Color.BLACK)
//        snowPaint.setStyle(Paint.Style.FILL)
//        var bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
//        var bitmapCanvas = Canvas(bitmap)
//        bitmapCanvas.drawCircle(25F, 25F, 25F, snowPaint)
//
//
//        val builder = UpDownObject.Builder(resources.getDrawable(com.example.ktforfilemanager.R.drawable.ic_mickey))
//       // val builder = UpDownObject.Builder(bitmap,10F)
//        val fallObject = builder
//            .setSpeed(10,true)
//            .setSize(100,100,true)
//            .build()
//
//        fallingView = findViewById<View>(R.id.updownView) as UpDownView
//        textView=findViewById(R.id.text)
//        textView.setOnTouchListener(mOnTouchListener)
//
//
//        (fallingView as UpDownView).addFallObject(fallObject, 100)//添加50个雪球对象
//
//        fallingView.setOnClickListener({
//            it.visibility=View.GONE
//            goToThread()
//        })



       // showProgressDialog()
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        wave.setImageRadius(head.width / 2)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x:Float
        var y:Float
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                 x = event.x.toFloat()
                 y = event.y.toFloat()
                wave.addMoreWave(x,y)
            }
//            MotionEvent.ACTION_MOVE ->{
//                 x = event.x as Int
//                 y = event.y as Int
//            }


        }
        return true
    }

    private fun  goToThread(){
        Thread{
            run{
                timeSet++
            }
        }.start()
        Handler().postDelayed({
            goToThread()
            if (timeSet>10){
                timeSet=0
                fallingView.visibility=View.VISIBLE
                fallingView.setOnClickListener({
                    it.visibility=View.GONE
                    goToThread()
                })
            }
        }, 1000)


    }
    val mOnTouchListener = object : View.OnTouchListener {
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event!!.action and MotionEvent.ACTION_MASK) {
                //設置事件
                ACTION_DOWN -> { timeSet=0 }

                ACTION_POINTER_DOWN -> {
                  timeSet=0
                }

                ACTION_POINTER_UP -> { timeSet=0}
            }
            //通知 ViewGroup 要接收此事件，事件將不往下傳遞
            return true
        }
    }
    private fun showProgressDialog() {
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = this.getLayoutInflater()

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(com.example.ktforfilemanager.R.layout.dialog_signin, null))

        builder.create()

        builder.show()
    }
}
