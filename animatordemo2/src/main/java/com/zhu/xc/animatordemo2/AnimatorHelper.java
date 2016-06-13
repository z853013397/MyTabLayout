package com.zhu.xc.animatordemo2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by xc on 2016/5/23.
 */
public class AnimatorHelper {
    private int width = 0;
    private int height = 0;


    private float transtionX;

    private float transtionY;

    private View view;

    public AnimatorHelper(View view) {
        this.view = view;
    }

    public void startAnimator() {
        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(view,"translationX",0,transtionX);
        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(view,"translationY",0,transtionY);
        if(height == 0) {
            height = view.getHeight();
        }

        if(width == 0) {
            width = view.getWidth();
        }

        ObjectAnimator heightAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(view),"height",view.getHeight(),height);
        ObjectAnimator widthAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(view),"width",view.getWidth(),width);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
        set.start();
//        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,-100f,0,-100f);//负数 向左 向上
//
////        　float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
////        　　float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
////        　　float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
////        　　float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
//        AnimationSet set = new AnimationSet(false);
//        set.addAnimation(animation);
//        set.addAnimation(translateAnimation);
////        animation.setDuration(300);
////        view.startAnimation(animation);
//        set.setDuration(3000);
//        view.startAnimation(set);
    }


    public void setEndAnimator() {
        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(view,"translationX",transtionX,0);
        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(view,"translationY",transtionY,0);
        ObjectAnimator heightAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(view),"height",view.getHeight(),height);
        ObjectAnimator widthAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(view),"width",view.getWidth(),width);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
        set.start();
    }

    /**
     * 动画要跑到的位置
     *
     * @param width      宽度
     * @param height     高度
     * @param transtionX x轴的偏移量
     * @param transtionY y轴的偏移量
     */
    public AnimatorHelper  setAnimatorParams(int width, int height, float transtionX, float transtionY) {
        this.width = width;
        this.height = height;
        this.transtionX = transtionX;
        this.transtionY = transtionY;
        return this;
    }

    private long duration = 300;

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public void setAnimatorWidth(int width) {
        this.width = width;
    }

    public void setAnimatorHeight(int height) {
        this.height = height;
    }

    public void setAnimatorTranstionX(float transtionX) {
        this.transtionX = transtionX;
    }

    public void setAnimatorTranstionY(float transtionY) {
        this.transtionY = transtionY;
    }


    class ViewAnimatorHelper {
        View view;

        public ViewAnimatorHelper(View view) {
            this.view = view;
        }

        public void setWidth(int width) {
            view.getLayoutParams().width = width;
            view.requestLayout();
        }


        public int getWidth() {
            return view.getLayoutParams().width;
        }


        public void setHeight(int height) {
            view.getLayoutParams().height = height;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }
    }
}
