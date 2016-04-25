package com.zhu.xc.mytablelayout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by xc on 2016/4/13.
 *
 */
public class LinearLayoutHelper implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MyTabLayout";

    public interface OnPositionListerner {
        void getCurrentPosition(int position);//得到他当前没有滑动的位置

        void getLastPostion(int position);//得到他下一个滑动的位置
    }

    private OnPositionListerner mPositionListerner;


    public void setOnPositionListernen(OnPositionListerner mOnPositionListerner) {
        this.mPositionListerner = mOnPositionListerner;
    }

    private Context mContext;

    public LinearLayoutHelper(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
        mContext = linearLayout.getContext();
    }

    private LinearLayout linearLayout;
    private static final int CHILD_LEFT_MARGIN = 16;//dp
    private static final int CHILD_WIDTH = 40;//子控件的宽度 dp
    private static final int CHILD_hEIGHT = 40;//子控件的高度 dp
    private int left_right_padding = 150;//dp


    public void initView(int count) {
//        int width = dpToPx(left_right_padding) - dpToPx(CHILD_WIDTH) / 2;
        int padding = dpToPx(left_right_padding);
        if (count == 0)
            return;
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(CHILD_LEFT_MARGIN), 0, 0, 0);
            ImageView textView = new ImageView(mContext);
            textView.setImageResource(R.mipmap.bg_right);
//            textView.setText(i + "");
//            textView.setGravity(Gravity.CENTER);
            if (i == 0) {
//                params.setMargins(padding, 0, 0, 0);
            }
            if (i == count - 1) {
//                params.setMargins(dpToPx(CHILD_LEFT_MARGIN), 0, padding, 0);
            }
            linearLayout.addView(textView, params);
        }
    }


    private int dpToPx(int dps) {
        return Math.round(mContext.getResources().getDisplayMetrics().density * dps);
    }

    /**
     * 这儿 在position = 0 的时候 会自动滚动 中间
     *  这儿切记注意 left_right_padding 这个值乘以2 最好接近linearlayout的宽度 不能相隔太多 (已修复）
     * @param position
     * @param positionOffset
     * @return
     */
    public int calculateScrollX(int position, float positionOffset) {
        if (linearLayout == null)
            return 0;
        final View selectedChild = linearLayout.getChildAt(position);//选中的itm
        final View nextChild = position + 1 < linearLayout.getChildCount()//下一个view
                ? linearLayout.getChildAt(position + 1)
                : null;
        final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;//选中的item的宽度
        final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;//下一个view的宽度
        final int startPostion = linearLayout.getWidth() / 2;//positon = 0是 所在的位置
//        final int left = selectedChild.getLeft();
        final int scrollX = selectedChild.getLeft()
                + ((int) ((selectedWidth + nextWidth) * positionOffset * 0.5f))//
                + (selectedChild.getWidth() / 2)
                - startPostion;//滚动的x轴 长度

//        final int scrollWitdth = dpToPx(CHILD_LEFT_MARGIN) + selectedWidth;//每次viewpager滚动时头部滚动的距离 即子项目的宽度 和间距 也可以是
//
//        final int realScrollX = scrollX - (startPostion - dpToPx(left_right_padding));
//
//        int scrollCount = realScrollX / scrollWitdth;//这个说明 向左移动了多少个子view
//
//        Log.i(TAG, "滚动了" + scrollCount + "个子view");
//        Log.i(TAG, "中间的位置是：" + position);
//        final int count = linearLayout.getChildCount();//一共有多少个子view
//        //计算从中线点开始 左边和右边可以容纳多少个子view
//        final int leftInstance = startPostion + selectedWidth / 2;
//        int leftCount = (leftInstance / scrollWitdth) - 1;
//        if (leftInstance % (scrollWitdth) > selectedWidth) {
//            leftCount++;
//        }
//        Log.i(TAG, "leftCount: " + leftCount);
//        int leftGoneCount = 0;
//        int rightGoneCount;
//        if (scrollCount - leftCount > 0) {//这儿左边有view 隐藏了
//            leftGoneCount = scrollCount - leftCount;
//            rightGoneCount = count - leftGoneCount - leftCount * 2 - 1;
//            if (rightGoneCount <= 0) {
//                rightGoneCount = 0;
//            }
//        } else {
//            rightGoneCount = count - leftCount - 1 - scrollCount;
//        }


        if (mPositionListerner == null) {
            return scrollX;
        }
        if(positionOffset == 0.0f) {//已经滑动完毕
            mPositionListerner.getCurrentPosition(position);
            Log.i(TAG, "滑动完毕时的position ：" + position);
        }else {//正在滑动
            mPositionListerner.getLastPostion(position);
            Log.i(TAG, "上一个的位置是 ：" + position );
        }



//        Log.i(TAG, "左边隐藏了" + leftGoneCount + "个view");
//        Log.i(TAG, "右边隐藏了" + rightGoneCount + "个view");
//
//        if (mOnGoneViewCount != null) {
//            mOnGoneViewCount.getCurrentPosition(leftGoneCount);
//        }
//        if (mOnGoneViewCount != null) {
//        }

        return scrollX;
    }


    public void setupWithViewPager(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
        }
        PagerAdapter adapter = viewPager.getAdapter();
//        if (adapter == null) {
//            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
//        }
        int count = adapter.getCount();
        initView(count);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setScrollPosition(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setScrollPosition(int position, float positionOffset) {
        int scrollX = calculateScrollX(position, positionOffset);
        Log.i(TAG, "scrollX: " + scrollX);
        linearLayout.scrollTo(scrollX, 0);
    }

    public void setPositionColor(int position) {
        if (linearLayout == null) {
            throw new RuntimeException("linearLayout is no instantiation.");
        }
        View positionView = linearLayout.getChildAt(position);
        positionView.setBackgroundColor(Color.RED);
    }
}
