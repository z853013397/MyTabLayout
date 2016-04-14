package com.zeyuan.kyq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.math.MathContext;

public class MyLayout extends LinearLayout {

    public MyLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
//        context.getSystemService().getDefaultDisplay().getHeight();
//        getContext().
    }

    public MyLayout(Context context) {
        super(context);
    }

    private OnSoftKeyboardListener onSoftKeyboardListener;
//    private Context mContext;
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (onSoftKeyboardListener != null) {
            final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
            final int oldSpec = getMeasuredHeight();
            // If layout became smaller, that means something forced it to resize. Probably soft keyboard :)
            if (oldSpec > newSpec){
                onSoftKeyboardListener.onShown();
            } else {
//                if(getHeight())
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                if((newSpec - oldSpec) > height/3)
                onSoftKeyboardListener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final void setOnSoftKeyboardListener(final OnSoftKeyboardListener listener) {
        this.onSoftKeyboardListener = listener;
    }

    // Simplest possible listener :)
    public interface OnSoftKeyboardListener {
        public void onShown();
        public void onHidden();
    }
}