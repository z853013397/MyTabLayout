package com.zhu.xc.mytablelayout;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private MyTabLayout tabLayout;
    int displayWidth;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new MyTabLayout(MainActivity.this));
        initView();
        displayWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    private void initView() {
        mViewPager = (ViewPager) MainActivity.this.findViewById(R.id.vg);
        linearLayout = (LinearLayout) findViewById(R.id.ll_content);
        LinearLayoutHelper helper = new LinearLayoutHelper(linearLayout);

//        tabLayout = (MyTabLayout) findViewById(R.id.table_layout);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.set
//       int width =  tabLayout.getWidth();
//        Log.i(TAG,"the width of tablayout :" +width);

        MyPagerAdapter adapter = new MyPagerAdapter();
//        tabLayout.setTabsFromPagerAdapter(adapter);//设置tablayout的标题来自adapter
//        final TabLayout.TabLayoutOnPageChangeListener listener =
//                new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
//        mViewPager.addOnPageChangeListener(listener);
        mViewPager.setAdapter(adapter);
        helper.setupWithViewPager(mViewPager);
//        tabLayout.setupWithViewPager(mViewPager);
//        tabLayout.setPositionColor(4);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        tabLayout.initView(8,displayWidth/2);
        LinkedList li;
//        CopyOnWriteArrayList
    }

    private int[] resource = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e, R.mipmap.f, R.mipmap.g, R.mipmap.h};
    // private ImageView[] images;

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return resource[position]+"";
        }
        @Override
        public int getCount() {
            return resource.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = new ImageView(MainActivity.this);
            img.setBackgroundResource(resource[position]);
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }


}
