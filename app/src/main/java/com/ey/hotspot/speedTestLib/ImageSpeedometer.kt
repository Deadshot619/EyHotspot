package com.ey.hotspot.speedTestLib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import com.ey.hotspot.R

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
open class ImageSpeedometer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Speedometer(context, attrs, defStyleAttr) {

    private var imageSpeedometer: Drawable? = null
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * change the color of the center circle.
     */
    var centerCircleColor: Int
        get() = circlePaint.color
        set(centerCircleColor) {
            circlePaint.color = centerCircleColor
            if (isAttachedToWindow)
                invalidate()
        }

    /**
     * change the width of the center circle.
     */
    var centerCircleRadius = dpTOpx(20f)
        set(centerCircleRadius) {
            field = centerCircleRadius
            if (isAttachedToWindow)
                invalidate()
        }

    init {
        init()
        initAttributeSet(context, attrs)
    }

    override fun defaultSpeedometerValues() {
        backgroundCircleColor = 0
    }

    override fun defaultGaugeValues() {}

    private fun init() {
        circlePaint.color = 0xFF444444.toInt()
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ImageSpeedometer, 0, 0)

        //Set center circle color
        circlePaint.color = a.getColor(R.styleable.SpeedView_sv_centerCircleColor, circlePaint.color)

        //Set radius of center circle
        centerCircleRadius = a.getDimension(R.styleable.SpeedView_sv_centerCircleRadius, centerCircleRadius)

        imageSpeedometer = a.getDrawable(R.styleable.ImageSpeedometer_sv_image)
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        updateBackgroundBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        drawSpeedUnitText(canvas)
        drawIndicator(canvas)
        drawNotes(canvas)
        canvas.drawCircle(size * .5f, size * .5f, centerCircleRadius, circlePaint)
    }

    override fun updateBackgroundBitmap() {
        val c = createBackgroundBitmapCanvas()

        if (imageSpeedometer != null) {
            imageSpeedometer!!.setBounds(viewLeft.toInt() + padding, viewTop.toInt() + padding, viewRight.toInt() - padding, viewBottom.toInt() - padding)
            imageSpeedometer!!.draw(c)
        }
        drawTicks(c)
    }

    fun getImageSpeedometer(): Drawable? {
        return imageSpeedometer
    }

    /**
     * set background speedometer image, Preferably be square.
     * @param imageResource image id.
     */
    fun setImageSpeedometer(imageResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setImageSpeedometer(context.getDrawable(imageResource))
        else
            setImageSpeedometer(context.resources.getDrawable(imageResource))
    }

    /**
     * set background speedometer image, Preferably be square.
     * @param imageSpeedometer image drawable.
     */
    fun setImageSpeedometer(imageSpeedometer: Drawable?) {
        this.imageSpeedometer = imageSpeedometer
        updateBackgroundBitmap()
    }

    /**
     * set background speedometer image, Preferably be square.
     * @param bitmapImage image bitmap.
     */
    fun setImageSpeedometer(bitmapImage: Bitmap) {
        setImageSpeedometer(BitmapDrawable(context.resources, bitmapImage))
    }
}