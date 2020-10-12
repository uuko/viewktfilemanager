package com.example.ktforfilemanager

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


import android.os.Handler
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP

import android.view.View


import android.media.MediaPlayer
import com.example.ktforfilemanager.waveview.WaveView
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

import com.example.ktforfilemanager.ballview.BallView
import com.example.ktforfilemanager.defview.UpDownObject
import com.example.ktforfilemanager.defview.UpDownView
import android.util.DisplayMetrics

import com.example.ktforfilemanager.paintview.PaintView

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.opengl.Visibility
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import com.example.ktforfilemanager.paintview.PPView

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getSystemService
import com.example.ktforfilemanager.chooseview.PickerPhotoActivity
import java.io.File
import java.io.FileOutputStream


class ProgressActivity : AppCompatActivity() {
    private lateinit var head: ImageView
    private lateinit var wave: WaveView
    private lateinit var scaleAnimation: ScaleAnimation
    private lateinit var mPlayer: MediaPlayer
    private lateinit var fallingView:View
    private lateinit var textView : TextView
    private  var timeSet =0
    private lateinit var  paintView: PPView
    private val Pen = 1
    private val Eraser = 2
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_paint)
        paintView = findViewById(R.id.paintView)
        var water:Button=findViewById(R.id.water)
        var turn : Button=findViewById(R.id.turn)
        var eraser : Button=findViewById(R.id.eraser)
        var back : Button=findViewById(R.id.back)
        var go : Button=findViewById(R.id.go)
        var save : Button=findViewById(R.id.save)
        var next : Button=findViewById(R.id.next)
        var prgress_width:SeekBar=findViewById(R.id.widthprogress)
        var eraser_width:SeekBar=findViewById(R.id.eraserprogress)
        var transparentbar:SeekBar=findViewById(R.id.transparenctbar)
        var colorRed:Button=findViewById(R.id.red)
        var colorYellow:Button=findViewById(R.id.yellow)
        var chColorLayout:View=findViewById(R.id.chColorLayout)
        var orange:View=findViewById(R.id.orange);
        var green:View=findViewById(R.id.green);
        var blue:View=findViewById(R.id.blue);
        var black:View=findViewById(R.id.black);
        var horse:ImageView=findViewById(R.id.horse);

        var drawable = horse.getDrawable();
        var isMode=false
        if(drawable != null){
            isMode=true
        }else{
            isMode=false
        }



        turn.setOnClickListener {
            var intent=Intent(this,PickerPhotoActivity::class.java)
            startActivity(intent)
        }
        var cl=Color.BLACK
        var clickit=false
        var clickNext=false
        eraser.isEnabled=false
        water.setOnClickListener {
            horse.setImageDrawable(this.getDrawable(R.drawable.ic_unicorn))

                isMode=true

        }
        save.setOnClickListener {
            viewSaveToImage(paintView)
        }
        var a=10
        var trans=255
        var percent=0
        go.setOnClickListener {
            eraser_width.visibility=View.GONE
            clickit=!clickit
            if (clickit){
                eraser.isEnabled=false
                eraser.setBackgroundResource(R.drawable.eraser)
                go.setBackgroundResource(R.drawable.ic_paintbrush_sl)
                transparentbar.visibility=View.VISIBLE
                prgress_width.visibility=View.VISIBLE
                chColorLayout.visibility=View.VISIBLE
            }
            else{
                eraser.isEnabled=true
                eraser.setBackgroundResource(R.drawable.eraser)
                go.setBackgroundResource(R.drawable.ic_paintbrush)
                transparentbar.visibility=View.GONE
                prgress_width.visibility=View.GONE
                chColorLayout.visibility=View.GONE
            }


            paintView.setColor(cl,trans)
            paintView.setWidth(a)
            prgress_width.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    a=p0!!.progress
                    paintView.setWidth(a)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }
                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
            transparentbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    percent=p0!!.progress
                    trans=(256/100)*percent
                    paintView.setColor(cl,trans)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }


            } )
            colorRed.setOnClickListener {
                cl=Color.RED
                paintView.setColor(cl,trans)
                Log.d("color","red"+cl+trans)
            }
            colorYellow.setOnClickListener {
                cl=Color.YELLOW
                paintView.setColor(cl,trans)
            }

            black.setOnClickListener {
                cl=Color.BLACK
                paintView.setColor(cl,trans)
            }


            orange.setOnClickListener {
                cl=Color.MAGENTA
                paintView.setColor(cl,trans)
            }
            green.setOnClickListener {
                cl=Color.GREEN
                paintView.setColor(cl,trans)
            }

            blue.setOnClickListener {
                cl=Color.BLUE
                paintView.setColor(cl,trans)
            }
            Log.d("color","activity"+cl+"tt"+trans)
        }

        back.setOnClickListener {
            paintView.back()
        }

        next.setOnClickListener {
            paintView.next()
        }
        var progress=0
        eraser.setOnClickListener {

            clickNext=!clickNext

            if (clickNext){
                eraser.setBackgroundResource(R.drawable.eraser_slected)
                go.setBackgroundResource(R.drawable.ic_paintbrush)
                go.isEnabled=false
                prgress_width.visibility=View.GONE
                chColorLayout.visibility=View.GONE
                transparentbar.visibility=View.GONE
                eraser_width.visibility=View.VISIBLE
                paintView.setWidth(progress)
                eraser_width.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        progress=p0!!.progress
                        paintView.setWidth(progress)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }
                })

            }else{
                eraser.setBackgroundResource(R.drawable.eraser)
                go.setBackgroundResource(R.drawable.ic_paintbrush)
                go.isEnabled=true
                prgress_width.visibility=View.GONE
                eraser_width.visibility=View.GONE
            }
            paintView.eraserIt(isMode)

        }

//        var button : Button=findViewById(R.id.eraser)
//        val metrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(metrics)
//        paintView.init(metrics)
//        button.setOnClickListener { it->paintView.eraser()}
        /*
        * ProgressView
        * */
//        setContentView(R.layout.progress)
//         showProgressDialog()

        /*
        * def View test
        * */
//        setContentView(R.layout.progress)
        /*
        * 按下去擴散
        * */
//        setContentView(R.layout.wave)
//
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
//        setContentView(R.layout.ball)
//        var snowPaint = Paint()
//        snowPaint.setColor(Color.BLACK)
//        snowPaint.setStyle(Paint.Style.FILL)
//        var bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
//        var bitmapCanvas = Canvas(bitmap)
//        bitmapCanvas.drawCircle(25F, 25F, 25F, snowPaint)


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
//        val builder = UpDownObject.Builder(resources.getDrawable(R.drawable.ic_mickey))
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




    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        wave.setImageRadius(head.width / 2)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        var x:Float
//        var y:Float
//        if ((event!!.action== MotionEvent.ACTION_DOWN) || (event!!.action== MotionEvent.ACTION_MOVE) ){
//            x = event.x.toFloat()
//            y = event.y.toFloat()
//            wave.addMoreWave(x,y)
//        }

//
//        else{
//
//            when(event!!.action){
//
//                MotionEvent.ACTION_DOWN -> {
//                    x = event.x.toFloat()
//                    y = event.y.toFloat()
//                    wave.addMoreWave(x,y)
//                }
//                MotionEvent.ACTION_MOVE ->{
//
//                    x = event.x
//                    y = event.y
//                    wave.addMoreWave(x,y)
//                }
//
//         return true
//
//
//
//
//    }

//    private fun  goToThread(){
//        Thread{
//            run{
//                timeSet++
//            }
//        }.start()
//        Handler().postDelayed({
//            goToThread()
//            if (timeSet>10){
//                timeSet=0
//                fallingView.visibility=View.VISIBLE
//                fallingView.setOnClickListener({
//                    it.visibility=View.GONE
//                    goToThread()
//                })
//            }
//        }, 1000)
//
//
//    }
//    val mOnTouchListener = object : View.OnTouchListener {
//        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
//            when (event!!.action and MotionEvent.ACTION_MASK) {
//                //設置事件
//                ACTION_DOWN -> { timeSet=0 }
//
//                ACTION_POINTER_DOWN -> {
//                  timeSet=0
//                }
//
//                ACTION_POINTER_UP -> { timeSet=0}
//            }
//            //通知 ViewGroup 要接收此事件，事件將不往下傳遞
//            return true
//        }
//    }
//    private fun showProgressDialog() {
//        val builder = AlertDialog.Builder(this)
//
//        val inflater = this.getLayoutInflater()
//
//        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
//
//        builder.create()
//
//        builder.show()
//    }


    fun viewSaveToImage(view: PPView) {


        val cachebmp = view.loadBitmapFromView()



        try {

            var appDir =  File(Environment.getExternalStorageDirectory(), "Boohee");
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            val inflater = LayoutInflater.from(this)
            val v = inflater.inflate(R.layout.alertdialog_use, null)
            var fileName="";
            var fos: FileOutputStream
            //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
            AlertDialog.Builder(this)
                .setTitle("輸入新檔案名稱或是用預設名稱(test.png)")
                .setView(v)
                .setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                    val editText = v.findViewById(R.id.editText1) as EditText
                    fileName=editText.text.toString()
                    Toast.makeText(
                        applicationContext, "新檔案名稱" +
                                editText.text.toString(), Toast.LENGTH_SHORT
                    ).show()

                    if (fileName.isEmpty()) {
                        fileName="test"
                    }
                    val file = File(appDir, fileName+".PNG")
                    fos = FileOutputStream(file)
                    cachebmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.flush()
                    fos.close()
                })
                .show()



        } catch (e: Exception) {
            e.printStackTrace()
        }

        view.destroyDrawingCache()
    }




}
