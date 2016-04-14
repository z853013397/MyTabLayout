package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.KnowledgeAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.BasePage;
import com.zeyuan.kyq.page.kangfu.SideEffectPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 * 从主页的(除了副作用)更多进来的页面
 */
public class MainMoreActivity extends BaseActivity {
    public static final String INDEX = "index";
    private int type = 0;
    private ViewPager mViewPager;
    private List<BasePage> data;
    private SideEffectPage mSideEffectPage1;
    private SideEffectPage mSideEffectPage2;
    private SideEffectPage mSideEffectPage3;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ActionBar ab = getSupportActionBar();
        type = getIntent().getIntExtra(INDEX, 0);
        initDataType(1);
        initView();
//        setListener();
    }

    private void initDataType(int tag) {
//        datas = new ArrayList();
//        switch (tag) {
//            case 1: {
//                type = "1";
//                break;
//            }
//            case 2: {
//                type = "2";
//                break;
//            }
//            case 3: {
//                type = "3";
//                break;
//            }
//            default:
//                throw new RuntimeException("数据多余出错");
//        }
        data = new ArrayList<>();
        mSideEffectPage1 = new SideEffectPage(this, "3");
        mSideEffectPage2 = new SideEffectPage(this, "1");
        mSideEffectPage3 = new SideEffectPage(this, "2");
        data.add(mSideEffectPage1);
        data.add(mSideEffectPage2);
        data.add(mSideEffectPage3);
    }


//    private KnowledgeAdapter adapter;

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tbl);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);//初始化

        //设置tablayout选项卡监听
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        MyPagerAdapter mAdapter = new MyPagerAdapter(data);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//设置tablayout的标题来自adapter
        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager.addOnPageChangeListener(listener);//设置viewpager滚动的监听和tablayout一致
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(type);
    }

    class MyPagerAdapter extends PagerAdapter {
        List<BasePage> datas;

        public MyPagerAdapter(List<BasePage> datas) {
            this.datas = datas;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "康复与营养";
                case 1:
                    return "副作用";
                case 2:
                    return "知识库";
            }
            return datas.get(position).toString();
        }

        @Override
        public int getCount() {
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = datas.get(position).getRootView();
            ((ViewPager) container).addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
