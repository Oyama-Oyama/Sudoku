//package com.roman.gurdan.sudo.pro.view
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Outline
//import android.graphics.Path
//import android.graphics.RectF
//import android.os.Build
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewOutlineProvider
//import androidx.annotation.RequiresApi
//import androidx.appcompat.widget.AppCompatImageView
//import com.roman.gurdan.sudo.pro.R
//
//class CircleImageView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null
//) : AppCompatImageView(context, attrs) {
//
//
//    private var mRadius: Int = 0
//    private val viewOutlineProvider: ViewOutlineProvider by lazy {
//        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//        object : ViewOutlineProvider() {
//            override fun getOutline(view: View?, outline: Outline?) {
//                val width = width
//                val height = height
//                outline?.setRoundRect(0, 0, width, height, mRadius.toFloat())
//            }
//
//        }
//    }
//    private var path: Path?
//    private var rect: RectF?
//
//
//    init {
//        val obtainStyledAttributes =
//            context.obtainStyledAttributes(attrs, R.styleable.CircleImageViewV3)
//        obtainStyledAttributes.let {
//            mRadius =
//                it.getInt(
//                    R.styleable.CircleImageViewV3_CustomizeRadiusV3,
//                    (getContext().resources.getDimension(R.dimen.dp_10)).toInt()
//                )
//            it.recycle()
//            path = Path()
//            rect = RectF()
//            if (ModuleUtils.isRoundCornerMask()) { //只对小度做圆角处理
//                setRound(mRadius)
//            }
//        }
//    }
//
//    //设置圆角图片
//    fun setRound(radius: Int) = apply {
//        val isChange = radius != mRadius
//        mRadius = radius
//        if (mRadius != 0) {
//            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
//                outlineProvider = viewOutlineProvider
//                clipToOutline = true
//
//            }
//            val width = width.toFloat()
//            val height = height.toFloat()
//            rect?.set(0f, 0f, width, height)
//            path?.reset()
//            rect?.let { path?.addRoundRect(it,mRadius.toFloat(),mRadius.toFloat(),Path.Direction.CW) }
//        } else {
//            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
//                clipToOutline = false
//            }
//        }
//
//        if (isChange) {
//            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
//                invalidateOutline()
//            }
//        }
//
//    }
//
//    override fun draw(canvas: Canvas?){
//        var clip = false
//        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT && mRadius > 0) {
//            clip = true
//            canvas?.save()
//            path?.let { canvas?.clipPath(it) }
//
//        }
//        super.draw(canvas)
//        if (clip) {
//            canvas?.restore()
//        }
//    }
//
//
//}