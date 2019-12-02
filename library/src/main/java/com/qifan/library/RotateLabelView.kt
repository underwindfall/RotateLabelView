package com.qifan.library

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Created by Qifan on 2019-12-02.
 */
private const val predicateErrorMessage = "RotateTextView must have all his attributes defined."

class RotateLabelView : View {
    private val DEFAULT_ROTATE_ANGLE = 45
    private val DEFAULT_TEXT_SIZE = 30f
    private val DEFAULT_BACKGROUND_COLOR = android.R.color.black
    private val DEFAULT_TEXT_COLOR = android.R.color.white

    private var contentTextSize = sp2px(DEFAULT_TEXT_SIZE)
    private lateinit var content: String
    private var rotationAngle: Int = DEFAULT_ROTATE_ANGLE
    private var tagLengthSize: Float = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fontMetrics = Paint.FontMetrics()

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initView(attributeSet)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(attrs)
    }

    private fun initView(attributeSet: AttributeSet?) {
        attributeSet?.let {
            withStyleableRes(it, R.styleable.RotateLabelView) {
                check(
                    hasValue(R.styleable.RotateLabelView_label)
                ) { predicateErrorMessage }

                getString(R.styleable.RotateLabelView_label)?.apply { content = this }

                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = getColor(
                        R.styleable.RotateLabelView_background_color,
                        ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR)
                    )
                }

                textPaint.apply {
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    contentTextSize =
                        getDimension(
                            R.styleable.RotateLabelView_text_size,
                            sp2px(DEFAULT_TEXT_SIZE)
                        )
                    rotationAngle =
                        getInt(R.styleable.RotateLabelView_rotation_angle, DEFAULT_ROTATE_ANGLE)
                    tagLengthSize =
                        (contentTextSize / sin(Math.toRadians(rotationAngle.toDouble()))).toFloat() * 1.2f
                    textSize = getDimension(R.styleable.RotateLabelView_text_size, contentTextSize)
                    color = getColor(
                        R.styleable.RotateLabelView_text_color,
                        ContextCompat.getColor(context, DEFAULT_TEXT_COLOR)
                    )
                }
                textPaint.getFontMetrics(fontMetrics)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawText(canvas)
    }


    private fun drawBackground(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val path = Path().apply {
            reset()
            lineTo(w, h)
            lineTo(w, h - tagLengthSize)
            lineTo(tagLengthSize, 0f)
            close()
        }
        canvas.apply {
            drawPath(path, paint)
        }
    }

    private fun drawText(canvas: Canvas) {
        val w = canvas.width
        val h = canvas.height
        val size = min(w, h)
        canvas.apply {
            save()
            rotate(rotationAngle.toFloat(), (size / 2).toFloat(), (size / 2).toFloat())
            val textHeight = fontMetrics.descent - fontMetrics.ascent
            val textBaseY =
                size / 2f - (fontMetrics.descent + fontMetrics.ascent) / 2f - (textHeight) / 2
            canvas.drawText(
                content,
                (paddingLeft + (size - paddingLeft - paddingRight) / 2).toFloat(),
                textBaseY,
                textPaint
            )
            restore()
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val width = min(widthMeasureSpec, heightMeasureSpec)
//        val size = measureWidth(width)
//        setMeasuredDimension(size, size)
        val size = max(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)
    }

    private fun measureWidth(size: Int): Int {
        val specMode = MeasureSpec.getMode(size)
        val specSize = MeasureSpec.getSize(size)
        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            val padding = paddingRight + paddingLeft
            val textWidth = textPaint.measureText(content)
            var result = ((padding + textWidth) * sqrt(2.00)).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
            result
        }
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * Get a typedArray to obtain styledAttributes and recycle it
     */
    private inline fun View.withStyleableRes(
        set: AttributeSet, @StyleableRes attrs: IntArray,
        init: TypedArray.() -> Unit
    ) {
        val typedArray = context.theme.obtainStyledAttributes(set, attrs, 0, 0)
        try {
            typedArray.init()
        } finally {
            typedArray.recycle()
        }
    }
}