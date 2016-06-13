package com.zhu.xc.animatordemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnAllViewListener, MyLinearLayout.RemoveListener {

    //    public interface onViewClickListener {
//        void onViewClick(int position,List<View> viewList);
//    }
    private ListView listView;

    //    private
    private int[] imgSrc = {R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d,R.mipmap.e,R.mipmap.f,R.mipmap.g,R.mipmap.h,R.mipmap.i,R.mipmap.img4};
    private static final String TAG = "MainActivity";
    //    private ImageView img;
//    RecyclerView rcview;
    MyLinearLayout llLayout;
    private List<View> viewList;
    private HorizontalScrollView mHorizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llLayout = (MyLinearLayout) this.findViewById(R.id.ll_layout);//mScrollView
        llLayout.setRemoveListener(this);
        mHorizontalScrollView = (HorizontalScrollView) this.findViewById(R.id.hs);
        viewList = new ArrayList<>();

        for (int i = 0; i < imgSrc.length; i++) {
            final int position = i;
            View v = initSmallImageView(position);

            viewList.add(v);

        }
        initLL();
    }

    private void initLL() {
//        int position = 0;
        for(int i = 0,j = viewList.size();i < j;i ++ ) {
            final int position = i;
            View v = viewList.get(i);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name(position);
                }
            });
            llLayout.addView(v);
        }
    }

    private boolean flag = true;
    /**
     * 收起后最右边的一张图片x的位置
     */
    private static final int END_ANIMATION_TRANSLATIONX = 500;
    /**
     * 收起后每张图片的间隔
     */
    private static final int EACH_INSTANCE = 20;

    /**
     * 收起后y桌的位置
     */
    private static final int END_ANIMATION_TRANSLATIONY = 300;


    private static final int SINGLE_X = 200;


    private static final int SINGLE_Y = 200;


    private int openPostion = 0;

    private void name(int position) {
        boolean tag = false;
        for (int i = 0, j = viewList.size(); i < j; i++) {
            int temp = i;


            final View v = viewList.get(i);
            final int itemLeft = v.getLeft();
            final  int itemTop = v.getTop();

            final AnimatorHelper helper = new AnimatorHelper(v);
            if (flag) {
                if (i == position) {
                    tag = true;
                    openPostion = i;
                    helper.setAnimatorParams(dp2Px(60), dp2Px(100), SINGLE_X - itemLeft, SINGLE_Y - itemTop);
                    helper.startAnimator();
                    continue;
                }
                if (tag) {
                    temp--;
                }
                helper.setAnimatorParams(dp2Px(30), dp2Px(50), END_ANIMATION_TRANSLATIONX - itemLeft - EACH_INSTANCE * temp, END_ANIMATION_TRANSLATIONY);
                helper.startAnimator();
                mHorizontalScrollView.fullScroll(mHorizontalScrollView.FOCUS_UP);//滚动到顶部
                llLayout.setCan_smooth_detele(false);
                noScroll();
            } else {
                if (i == openPostion) {
                    AnimatorHelper helpers = new AnimatorHelper(v);
                    helpers.setAnimatorParams(dp2Px(60), dp2Px(100),0 - itemLeft , SINGLE_Y - itemTop);
                    helpers.startTopAnimator(v.getTranslationX(),0 - itemLeft);
                    helpers.setOnEndListener(new AnimatorHelper.OnEndListener() {
                        @Override
                        public void onEnd() {
                            viewList.remove(openPostion);
                            viewList.add(0,v);
                            llLayout.removeAllViews();
                            initLL();
                        }
                    });
                    continue;
                }

                temp ++ ;
                if(i < openPostion) {
//                    View nextView = viewList.get(i + 1);
                    helper.setAnimatorParams(dp2Px(60), dp2Px(100), END_ANIMATION_TRANSLATIONX - itemLeft - EACH_INSTANCE * temp, END_ANIMATION_TRANSLATIONY);
                    float transX = v.getTranslationX();
                    helper.startTopAnimator(transX, 0 + v.getWidth());


                }else {
                    helper.setAnimatorParams(dp2Px(60), dp2Px(100), v.getTranslationX(), END_ANIMATION_TRANSLATIONY);
                    helper.startEndAnimator();
                }



                enableScroll();

                llLayout.setCan_smooth_detele(true);

            }

        }
        flag = !flag;
    }

    private void noScroll() {
        mHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


    private void enableScroll() {
        mHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }


    private View initSmallImageView(int position) {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        ImageView img = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp2Px(60), dp2Px(100));
        img.setImageResource(imgSrc[position]);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
//        params.setMargins();
        img.setLayoutParams(params);
        relativeLayout.addView(img);
        return relativeLayout;
    }


    private int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //    private int i = 500;
    private static final int width = 50;

    private static final int stay_position = 700;

    @Override
    public void onItemClick(int position, List<View> viewList) {

    }

    @Override
    public void removeItem(int position) {
        viewList.remove(position);
    }

//    @Override
//    public void onClick(View v) {
//        ObjectAnimator widthAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(img),"width",img.getWidth(),600);
//        ObjectAnimator heightAnimator = ObjectAnimator.ofInt(new ViewAnimatorHelper(img),"height",img.getHeight(),700);
//        ObjectAnimator tXAnimator = ObjectAnimator.ofFloat(img,"translationX",img.getTranslationX(),700);
//        ObjectAnimator tYAnimator = ObjectAnimator.ofFloat(img,"translationY",img.getTranslationY(),700);
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(widthAnimator,heightAnimator,tXAnimator,tYAnimator);
//        set.start();

//        AnimatorHelper helper = new AnimatorHelper(img);
////        helper.setAnimatorWidth(200);
////        helper.setAnimatorHeight(400);
////        helper.setAnimatorTranstionX(10);
////        helper.setAnimatorTranstionY(100);
//
//
//        helper.setAnimatorParams(400,220,500,500);
//
//        helper.startAnimator();
//    }

//    class ViewAnimatorHelper {
//        View view;
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
//
//    }


//    public void Click(View v) {
////        AnimatorHelper helper = new AnimatorHelper(img);
////        helper.setAnimatorWidth(200);
////        helper.setAnimatorHeight(400);
////        helper.setAnimatorTranstionX(10);
////        helper.setAnimatorTranstionY(100);
//
//
//        helper.setAnimatorParams(400,220,500,300);
//        helper.setDuration(5000);
//        helper.startAnimator();
//    }

}
