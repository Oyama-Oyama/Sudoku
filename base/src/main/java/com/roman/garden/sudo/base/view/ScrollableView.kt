package com.roman.garden.sudo.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class ScrollableView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

//    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//        super.requestDisallowInterceptTouchEvent(true)
//    }


    var startX:Float = 0.0f
    var startY:Float = 0.0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
//                requestDisallowInterceptTouchEvent(true)
              startX = event.rawX
              startY = event.rawY
            }
            MotionEvent.ACTION_MOVE ->{
                val offsetX = event.rawX - startX
                val offsetY = event.rawY - startY
//                this@ScrollableView.translationX = offsetX
//                this@ScrollableView.translationY = offsetY
//                this@ScrollableView.
                this@ScrollableView.scrollBy(-1 * offsetX.toInt(), -1 * offsetY.toInt())
                startX = event.rawX
                startY = event.rawY
            }
            else -> return false
        }
        return true//super.onTouchEvent(event)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val paint = Paint().apply {
            textSize = 72.0f
            color = Color.GREEN
        }
        canvas?.drawText("test scrollable view", 200.0f, 200.0f, paint)


    }


}