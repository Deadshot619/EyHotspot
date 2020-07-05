package com.ey.hotspot.ui.speed_test.test_result;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

/**
 * this Library build By Anas Altair
 * see it on <a href="https://github.com/anastr/SpeedView">GitHub</a>
 */
public class NormalIndicator2 extends Indicator<NormalIndicator> {

    private Path indicatorPath = new Path();
    private float bottomY;

    public NormalIndicator2(Context context) {
        super(context);
        updateIndicator();
    }

    @Override
    protected float getDefaultIndicatorWidth() {
        return dpTOpx(12f);
    }

    @Override
    public float getBottom() {
        return bottomY;
    }

    @Override
    public void draw(Canvas canvas, float degree) {
        canvas.save();
        canvas.rotate(90f + degree, getCenterX(), getCenterY());
        canvas.drawPath(indicatorPath, indicatorPaint);
        canvas.restore();
    }

    @Override
    protected void updateIndicator() {
        indicatorPath.reset();
        indicatorPath.moveTo(getCenterX(), getPadding()/.2f);
        bottomY = getViewSize() * 3f / 5f + getPadding();
        indicatorPath.lineTo(getCenterX() - getIndicatorWidth(), bottomY);
        Log.d("PATH Y",  " "+(bottomY));
        Log.d("PATH X",  " "+(getCenterX() - getDefaultIndicatorWidth()));
        indicatorPath.lineTo(getCenterX() + getIndicatorWidth(), bottomY);
        Log.d("PATH X",  " "+(getCenterX() + getDefaultIndicatorWidth()));
        RectF rectF = new RectF(getCenterX() - getIndicatorWidth(), bottomY - getIndicatorWidth()
                , getCenterX() + getIndicatorWidth(), bottomY + getIndicatorWidth());
        indicatorPath.addArc(rectF, 0f, 180f);

        indicatorPaint.setColor(getIndicatorColor());
    }

    @Override
    protected void setWithEffects(boolean withEffects) {
        if (withEffects && !isInEditMode()) {
            indicatorPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
        } else {
            indicatorPaint.setMaskFilter(null);
        }
    }
}
