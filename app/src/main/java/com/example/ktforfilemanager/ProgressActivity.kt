package com.example.ktforfilemanager

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP

import android.view.View
import android.widget.TextView

import com.example.ktforfilemanager.defview.UpDownObject
import com.example.ktforfilemanager.defview.UpDownView





class ProgressActivity : AppCompatActivity() {

    private lateinit var fallingView:View
    private lateinit var textView : TextView
    private  var timeSet =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress)
//
//        var snowPaint = Paint()
//        snowPaint.setColor(Color.BLACK)
//        snowPaint.setStyle(Paint.Style.FILL)
//        var bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
//        var bitmapCanvas = Canvas(bitmap)
//        bitmapCanvas.drawCircle(25F, 25F, 25F, snowPaint)
//
////初始化一个雪球样式的fallObject
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
//



       // showProgressDialog()
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
