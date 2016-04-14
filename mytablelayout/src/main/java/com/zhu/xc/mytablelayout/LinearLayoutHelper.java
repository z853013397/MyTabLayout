package com.zhu.xc.mytablelayout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;


/**
 * Created by xc on 2016/4/13.
 */
public class LinearLayoutHelper implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MyTabLayout";
    private Context mContext;

    public LinearLayoutHelper(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
        mContext = linearLayout.getContext();
    }

    private LinearLayout linearLayout;
    private static final int CHILD_LEFT_MARGIN = 16;//dp
    private static final int CHILD_WIDTH = 40;//子控件的宽度 dp
    private static final int CHILD_hEIGHT = 40;//子控件的高度 dp
    private int left_right_padding = 200;//dp


    public void initView(int count) {
        int width = dpToPx(left_right_padding) - dpToPx(CHILD_WIDTH) / 2;
        Log.i(TAG, "count:" + count);
        if (count == 0)
            return;
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(CHILD_WIDTH), dpToPx(CHILD_hEIGHT));
            params.setMargins(dpToPx(CHILD_LEFT_MARGIN), 0, 0, 0);
            TextView textView = new TextView(mContext);
            textView.setBackgroundResource(R.mipmap.bg_right);
            textView.setText(i + "");
            textView.setGravity(Gravity.CENTER);
            if (i == 0) {
                params.setMargins(width - textView.getWidth() / 2, 0, 0, 0);
            }
            if (i == count - 1) {
                params.setMargins(dpToPx(CHILD_LEFT_MARGIN), 0, width, 0);
            }
            linearLayout.addView(textView, params);
        }
    }


    private int dpToPx(int dps) {
        return Math.round(mContext.getResources().getDisplayMetrics().density * dps);
    }

    public int calculateScrollX(int position, float positionOffset) {
        if (linearLayout == null)
            return 0;
        final View selectedChild = linearLayout.getChildAt(position);//选中的itm
        final View nextChild = position + 1 < linearLayout.getChildCount()//下一个view
                ? linearLayout.getChildAt(position + 1)
                : null;
        final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;//选中的item的宽度
        final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;//下一个view的宽度

        return selectedChild.getLeft()
                + ((int) ((selectedWidth + nextWidth) * positionOffset * 0.5f))//
                + (selectedChild.getWidth() / 2)
                - linearLayout.getWidth() / 2;//
    }


    public void setupWithViewPager(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
        }
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
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
