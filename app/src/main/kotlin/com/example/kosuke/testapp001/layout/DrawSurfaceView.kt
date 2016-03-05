package com.example.kosuke.testapp001.layout

import android.view.SurfaceView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff.Mode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback

/**
 * Created by Kosuke on 2016/03/05.
 */

public class DrawSurfaceView : SurfaceView, Callback {

    var mHolder : SurfaceHolder = getHolder()
    var mPaint : Paint = Paint()
    var mPath : Path? = null
    var mLastDrawBitmap : Bitmap? = null
    var mLastDrawCanvas : Canvas? = null

    constructor(con: Context): super(con) {
        init()
    }

    constructor(con: Context, attrs: AttributeSet): super(con, attrs) {
        init()
    }

    fun init() {
        // 透過
        setZOrderOnTop(true)
        mHolder.setFormat(PixelFormat.TRANSPARENT)

        // コールバック
        mHolder.addCallback(this)

        // ペイント
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 6f
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        clearLastDrawBitmap()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mLastDrawBitmap!!.recycle()
    }

    fun clearLastDrawBitmap() {
        if (mLastDrawBitmap == null) {
            mLastDrawBitmap = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        }

        if (mLastDrawCanvas == null) {
            mLastDrawCanvas = Canvas(mLastDrawBitmap);
        }

        mLastDrawCanvas!!.drawColor(0, Mode.CLEAR);
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> onTouchDown(event.x, event.y)
            MotionEvent.ACTION_MOVE -> onTouchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> onTouchUp(event.x, event.y)
            else -> null
        }

        return true
    }

    fun onTouchDown(x: Float, y: Float) {
        mPath = Path()
        mPath!!.moveTo(x, y)
    }

    fun onTouchMove(x: Float, y: Float) {
        mPath!!.lineTo(x, y)
        drawLine(mPath!!)
    }

    fun onTouchUp(x: Float, y: Float) {
        mPath!!.lineTo(x, y)
        drawLine(mPath!!)
        mLastDrawCanvas!!.drawPath(mPath, mPaint)
    }

    fun drawLine(path: Path) {
        var canvas = mHolder.lockCanvas()
        canvas.drawColor(0, Mode.CLEAR)
        canvas.drawBitmap(mLastDrawBitmap, 0f, 0f, null)
        canvas.drawPath(path, mPaint)
        mHolder.unlockCanvasAndPost(canvas)
    }
}