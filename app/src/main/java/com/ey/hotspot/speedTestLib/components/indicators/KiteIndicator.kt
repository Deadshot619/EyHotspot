
package com.ey.hotspot.speedTestLib.components.indicators

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Path

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */

class KiteIndicator(context: Context) : Indicator<KiteIndicator>(context) {

    private val indicatorPath = Path()
    private var bottomY: Float = 0.toFloat()
    private val moveLeftToRight = 50


    init {
        width = dpTOpx(12f)
    }

    override fun getBottom(): Float {
        return bottomY
    }

    override fun draw(canvas: Canvas, degree: Float) {
        canvas.save()
        canvas.rotate(90f + degree, getCenterX(), getCenterY())
        canvas.drawPath(indicatorPath, indicatorPaint)
        canvas.restore()
    }

    override fun updateIndicator() {
        indicatorPath.reset()
        indicatorPath.moveTo(getCenterX(), getViewSize() / 7f + speedometer!!.padding.toFloat())
        bottomY = getViewSize() * .5f + speedometer!!.padding

        //left point
        indicatorPath.lineTo(getCenterX() - width, bottomY - moveLeftToRight - 10)

//      bottom point
        indicatorPath.lineTo(getCenterX(), bottomY + width - moveLeftToRight)

//        Right point
        indicatorPath.lineTo(getCenterX() + width, bottomY - moveLeftToRight - 10)

        indicatorPaint.color = color
    }

    override fun setWithEffects(withEffects: Boolean) {
        if (withEffects && !speedometer!!.isInEditMode)
            indicatorPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)
        else
            indicatorPaint.maskFilter = null
    }
}