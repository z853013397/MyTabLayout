package com.zhu.xc.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xc on 2016/4/19.
 */
public class CirCleChangeView extends View {
    public CirCleChangeView(Context context) {
        this(context, null);

    }

    public CirCleChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);

    }

    private float radius;
    private Paint mPaint;


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300, 300, radius, mPaint);
//        Path p = new Path();
//        p.lineTo();
//        p.quadTo();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        ValueAnimator animator = ValueAnimator.ofFloat(0,100);
//        animator.setRepeatCount(20);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//              float values = (float) animation.getAnimatedValue();
//                radius = values;
//                postInvalidate();
//            }
//        });
//        animator.start();
//    }
}
