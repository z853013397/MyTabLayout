package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.HackyViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ShowPhotoActivity extends BaseActivity {
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpager);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        List<String> urls = (List<String>)getIntent().getSerializableExtra("list");
        int position = getIntent().getIntExtra("position",0);
        viewPager = (HackyViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(new MyViewPager(this,urls));
        viewPager.setCurrentItem(position);
    }

    class MyViewPager extends PagerAdapter {

//        private PhotoViewAttacher mAttacher;

//        private LayoutInflater inflater;
        List<String> urls;
        MyViewPager(Context context,List<String> urls) {
//            inflater = LayoutInflater.from(context);
            this.urls = urls;
        }
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(ShowPhotoActivity.this).load(urls.get(position)).signature(new IntegerVersionSignature(1)).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }
    }



}
