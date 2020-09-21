package com.example.ktforfilemanager.paintview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


public class PPView extends View {
    private Bitmap cacheBitmap;// 画纸
    private Canvas cacheCanvas;// 创建画布、画家
    private Path path;// 绘图的路径
    public Paint paint;// 画笔
    private float preX, preY;// 之前的XY的位置，用于下面的手势移动
    private int view_width, view_height;// 屏幕的高度与宽度

    public PPView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint();
        cacheCanvas = new Canvas();
        // 获取屏幕的高度与宽度
        view_width = context.getResources().getDisplayMetrics().widthPixels;
        view_height = context.getResources().getDisplayMetrics().heightPixels;
        cacheBitmap = Bitmap.createBitmap(view_width, view_height,
                Bitmap.Config.ARGB_8888);// 建立图像缓冲区用来保存图像
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);// 设置画笔的默认颜色
        paint.setStyle(Paint.Style.STROKE);// 设置画笔的填充方式为无填充、仅仅是画线
        paint.setStrokeWidth(1);// 设置画笔的宽度为1

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);// 把cacheBitmap画到DrawView上
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 获取触摸位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {// 获取触摸的各个瞬间
            case MotionEvent.ACTION_DOWN:// 手势按下
                path.moveTo(x, y);// 绘图的起始点
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx > 5 || dy > 5) {// 用户要移动超过5像素才算是画图，免得手滑、手抖现象
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                    cacheCanvas.drawPath(path, paint);// 绘制路径
                }
                break;
            case MotionEvent.ACTION_UP:
                path.reset();
                break;
        }
        invalidate();
        return true;
    }



}
