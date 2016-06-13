package com.zhu.xc.animatordemo;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by xc on 2016/5/30.
 */
public class MyLinearLayout extends LinearLayout {
    private static final String TAG = "MyLinearLayout";
    private static final int INVALID_POSITION = -1;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }
    LayoutTransition transition;
    private void init(Context context) {
//        itemHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        transition = new LayoutTransition();
//        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,
//                transition.getAnimator(LayoutTransition.CHANGE_APPEARING));
//        transition.setAnimator(LayoutTransition.APPEARING,
//                null);
        transition.setAnimator(LayoutTransition.DISAPPEARING,
                null);
//        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
//                null);

        setLayoutTransition(transition);
    }


    public int pointToPosition(int x, int y) {
        Rect frame = new Rect();
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return i;
                }
            }
        }
        return INVALID_POSITION;
    }


    /**
     * 当前滑动的ListView　position
     */
    private int slidePosition;
    /**
     * 手指按下X的坐标
     */
    private int downY;
    /**
     * 手指按下Y的坐标
     */
    private int downX;
    /**
     * 屏幕宽度
     */
    private int itemHeight;
    /**
     * ListView的item
     */
    private View itemView;
    /**
     * 滑动类
     */
    private Scroller scroller;
    private static final int SNAP_VELOCITY = 600;
    /**
     * 速度追踪对象
     */
    private VelocityTracker velocityTracker;
    /**
     * 是否响应滑动，默认为不响应
     */
    private boolean isSlide = false;
    /**
     * 认为是用户滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 移除item后的回调接口
     */
    private RemoveListener mRemoveListener;


    public void setRemoveListener(RemoveListener mRemoveListener) {
        this.mRemoveListener = mRemoveListener;
    }

    /**
     * 是否可以滑动删除
     */
    private boolean can_smooth_detele = false;


    public void setCan_smooth_detele(boolean can_smooth_detele) {
        this.can_smooth_detele = can_smooth_detele;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                addVelocityTracker(event);

                // 假如scroller滚动还没有结束，我们直接返回
                if (!scroller.isFinished()) {
                    return false;
                }
                downX = (int) event.getX();
                downY = (int) event.getY();

                slidePosition = pointToPosition(downX, downY);

                // 无效的position, 不做任何处理
                if (slidePosition == INVALID_POSITION) {
                    return false;
                }

                // 获取我们点击的item view
                itemView = getChildAt(slidePosition);
                itemHeight = itemView.getHeight();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
//                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
//                        || (Math.abs(event.getY() - downY) > mTouchSlop && Math
//                        .abs(event.getX() - downX) < mTouchSlop)) {
                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
                        || downY - event.getY() > mTouchSlop && can_smooth_detele) {
                    isSlide = true;
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
        }

        return false;
    }


    /**
     * 处理我们拖动ListView item的逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            requestDisallowInterceptTouchEvent(true);
            addVelocityTracker(ev);
            final int action = ev.getAction();
            int x = (int) ev.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:

                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(cancelEvent);
                    if (x > downY) {
                        break;
                    }


                    int deltaX = downY - x;
                    downY = x;

                    // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                    itemView.scrollBy(0, deltaX);

                    return true;  //拖动的时候ListView不滚动
                case MotionEvent.ACTION_UP:
                    int velocityX = getScrollVelocity();
//                    if (velocityX > SNAP_VELOCITY) {
//                        scrollRight();
//                    } else if (velocityX < -SNAP_VELOCITY) {
//                        scrollLeft();
//                    } else {
                    scrollByDistanceX();
//                    }

                    recycleVelocityTracker();
                    // 手指离开的时候就不响应左右滚动
                    isSlide = false;
                    break;
            }
        }

        //否则直接交给ListView来处理onTouchEvent事件
        return super.onTouchEvent(ev);
    }
    private boolean isDelete = false;//是否是删除

    /**
     * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
     */
    private void scrollByDistanceX() {
        // 如果向左滚动的距离大于屏幕的二分之一，就让其删除
        if (itemView.getScrollY() >= itemHeight / 2) {
            scrollTop();
            isDelete = true;

        } else {
            // 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动
            itemView.scrollTo(0, 0);

            isDelete = false;
        }

    }

    private void scrollTop() {
        final int delta = (itemHeight - itemView.getScrollY());

        Log.i(TAG,"delta :" +  delta + " itemView.getScrollY() " + itemView.getScrollY() + " height :" + itemHeight);
//        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(0, itemView.getScrollY(), 0, delta,
                300);
        postInvalidate(); // 刷新itemView
    }


    /**
     * 添加用户的速度跟踪器
     *
     * @param event
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }


    @Override
    public void computeScroll() {
        if (scroller == null) {
            super.computeScroll();
            return;
        }

        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (scroller.computeScrollOffset()) {
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            // 滚动动画结束的时候调用回调接口
            if (scroller.isFinished() && isDelete) {
                removeView(itemView);
                if (mRemoveListener != null) {
                    mRemoveListener.removeItem(slidePosition);
                }
            }
            postInvalidate();
        }
    }


    /**
     * 移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }


    public interface RemoveListener {
        void removeItem(int position);
    }

    @Override
    public void removeView(View view) {
//        setLayoutTransition(null);

        super.removeView(view);
//        setLayoutTransition(transition);
    }


    @Override
    public void addView(View child, int index) {
//        setLayoutTransition(null);
        super.addView(child, index);
//        setLayoutTransition(transition);
    }
}
