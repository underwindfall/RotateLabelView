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
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Created by Qifan on 2019-12-02.
 */
private const val predicateErrorMessage = "RotateTextView must have all his attributes defined."
typealias Label = String

interface RotateLabelComponent {
    fun setLabel(text: Label)
    fun setLabel(@StringRes textRes: Int)
    fun setBackgroundColor(@ColorRes color: Int)
    fun setTextColor(@ColorRes color: Int)
    fun setTextSize(textSize: Float)
    fun setType(type: RotateLabelView.Type)
}

class RotateLabelView : View, RotateLabelComponent {
    private val DEFAULT_ROTATE_ANGLE = 45
    private val DEFAULT_TEXT_SIZE = 30f
    private val DEFAULT_BACKGROUND_COLOR = android.R.color.black
    private val DEFAULT_TEXT_COLOR = android.R.color.white
    private val DEFAULT_STYLE = Type.TOP_RIGHT

    private var contentTextSize = sp2px(DEFAULT_TEXT_SIZE)
    private lateinit var content: Label
    private var rotationAngle: Int = DEFAULT_ROTATE_ANGLE
    private val tagLengthSize: Float
        get() = (contentTextSize / sin(Math.toRadians(rotationAngle.toDouble()))).toFloat() * 1.2f
    private var backgroundColor: Int = ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR)
    private var textColor: Int = ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR)
    private var labelType: Type = DEFAULT_STYLE

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
                getInt(R.styleable.RotateLabelView_type, 0).apply {
                    labelType = when (this) {
                        0 -> Type.TOP_LEFT
                        1 -> Type.TOP_RIGHT
                        2 -> Type.BOTTOM_LEFT
                        3 -> Type.BOTTOM_RIGHT
                        else -> Type.TOP_RIGHT
                    }
                }

                backgroundColor = getColor(
                    R.styleable.RotateLabelView_background_color,
                    ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR)
                )

                textColor = getColor(
                    R.styleable.RotateLabelView_text_color,
                    ContextCompat.getColor(context, DEFAULT_TEXT_COLOR)
                )

                paint.apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                    color = backgroundColor
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
                    textSize = contentTextSize
                    color = textColor
                }
                textPaint.getFontMetrics(fontMetrics)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        refreshAttrs()
        drawBackground(canvas)
        drawText(canvas)
    }

    private fun refreshAttrs() {
        textPaint.apply {
            textSize = contentTextSize
            color = textColor
        }
        paint.apply {
            color = backgroundColor
        }
    }


    private fun drawBackground(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val path = when (labelType) {
            Type.TOP_RIGHT -> Path().apply {
                reset()
                lineTo(w, h)
                lineTo(w, h - tagLengthSize)
                lineTo(tagLengthSize, 0f)
                close()
            }
            Type.TOP_LEFT -> Path().apply {
                reset()
                moveTo(0f, h - tagLengthSize)
                lineTo(0f, h)
                lineTo(w, 0f)
                lineTo(w - tagLengthSize, 0f)
                close()
            }
            Type.BOTTOM_RIGHT -> Path().apply {
                reset()
                moveTo(0f, h)
                lineTo(tagLengthSize, h)
                lineTo(w, tagLengthSize)
                lineTo(w, 0f)
                close()
            }
            Type.BOTTOM_LEFT -> Path().apply {
                reset()
                moveTo(0f, 0f)
                lineTo(0f, tagLengthSize)
                lineTo(w - tagLengthSize, h)
                lineTo(w, h)
                close()
            }
        }

        canvas.apply {
            drawPath(path, paint)
        }
    }
    private fun drawText(canvas: Canvas) {
        val w = canvas.width
        val h = canvas.height
        val size = min(w, h)
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        val (angle, offset) = when (labelType) {
            Type.TOP_RIGHT -> Pair(rotationAngle.toFloat(), -textHeight / 2)
            Type.TOP_LEFT -> Pair(-rotationAngle.toFloat(), -textHeight / 2)
            Type.BOTTOM_RIGHT -> Pair(-rotationAngle.toFloat(), textHeight / 2)
            Type.BOTTOM_LEFT -> Pair(rotationAngle.toFloat(), textHeight / 2)
        }
        canvas.apply {
            save()
            rotate(angle, (size / 2).toFloat(), (size / 2).toFloat())
            val textBaseY =
                size / 2f - (fontMetrics.descent + fontMetrics.ascent) / 2f +offset
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
        val width = min(widthMeasureSpec, heightMeasureSpec)
        val size = measureWidth(width)
        setMeasuredDimension(size, size)
    }

    private fun measureWidth(size: Int): Int {
        val specMode = MeasureSpec.getMode(size)
        val specSize = MeasureSpec.getSize(size)
        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            val padding = paddingRight + paddingLeft
            textPaint.textSize = contentTextSize
            val textWidth = textPaint.measureText(content)
            var result =
                (padding + textWidth + tagLengthSize * cos(Math.toRadians(rotationAngle.toDouble()))).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
            result
        }
    }

    override fun setLabel(text: Label) {
        content = text
        invalidate()
    }

    override fun setLabel(@StringRes textRes: Int) {
        content = context.getString(textRes)
        invalidate()
    }

    override fun setBackgroundColor(@ColorRes color: Int) {
        backgroundColor = color
        invalidate()
    }

    override fun setTextColor(@ColorRes color: Int) {
        textColor = color
        invalidate()
    }

    override fun setTextSize(textSize: Float) {
        contentTextSize = sp2px(textSize)
        invalidate()
    }

    override fun setType(type: Type) {
        labelType = type
        invalidate()
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

    enum class Type {
        TOP_RIGHT,
        TOP_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }
}