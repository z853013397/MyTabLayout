package com.zhu.xc.animatordemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by xc on 2016/5/23.
 */
public class AnimatorHelper implements Animator.AnimatorListener {
    public interface OnEndListener{
        void onEnd();
    }

    private float width = 0;
    private float height = 0;

private OnEndListener onEndListener;


    public void setOnEndListener(OnEndListener onEndListener) {
        this.onEndListener = onEndListener;
    }

    private float transtionX;

    private float transtionY;

    private View view;

    public AnimatorHelper(View view) {
        this.view = view;
    }

    public void startAnimator() {
        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(view,"translationX",0,transtionX);
        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, transtionY);
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, width / view.getWidth());
        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, height / view.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
        set.start();
    }


    public void startEndAnimator() {
        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(view,"translationX",transtionX,0);
        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(view, "translationY", transtionY, 0);
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(view, "scaleX", width / view.getWidth(), 1.0f);
        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(view, "scaleY", height / view.getHeight(), 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
        set.addListener(this);
        set.start();
    }


    public void startTopAnimator(float startX,float endX) {
        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(view,"translationX",startX,endX);
        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(view, "translationY", transtionY, 0);
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(view, "scaleX", width / view.getWidth(), 1.0f);
        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(view, "scaleY", height / view.getHeight(), 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
        set.addListener(this);
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

    private long duration = 3000;

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

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (onEndListener != null) {
            onEndListener.onEnd();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


//    class ViewAnimatorHelper {
//        View view;
//
//        public ViewAnimatorHelper(View view) {
//            this.view = view;
//        }
//
//        public void setWidth(int width) {
//            view.getLayoutParams().width = width;
//            view.requestLayout();
//        }
//
//
//        public int getWidth() {
//            return view.getLayoutParams().width;
//        }
//
//
//        public void setHeight(int height) {
//            view.getLayoutParams().height = height;
//            view.requestLayout();
//        }
//
//        public int getHeight() {
//            return view.getLayoutParams().height;
//        }
//    }
}
