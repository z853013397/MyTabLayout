package com.zeyuan.kyq.app;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.fragment.empty.NoResultFragment;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/9/6.
 */
public class BaseActivity extends AppCompatActivity implements NoResultFragment.OnClickCircleLister {
    private static List<BaseActivity> activities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(activities == null) {
            activities = new ArrayList<>();
        }
    }

    @Override
    public void onClickCircle() {
        AppManager.getAppManager().finishAllActivity();
    }
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }


    public class ClickBack implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    protected void initTitlebar(String title) {
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(new ClickBack());
        TextView title_tv = (TextView) findViewById(R.id.title);
        title_tv.setText(title);
    }

    /**
     * 统一设置progress为pd 内容为whole_content
     * 隐藏掉等待的进度条
     */
    protected void setProgressGone() {
        findViewById(R.id.pd).setVisibility(View.GONE);
        findViewById(R.id.whole_content).setVisibility(View.VISIBLE);
    }


    /**
     * 释放imageview的图片内存
     *
     * @param imageView
     */
    public void clearBitmapMomery(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageView.setImageDrawable(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 释放view的背景内存
     *
     * @param view
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void clearBackgroundMemery(View view) {
        if (view != null && view.getBackground() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) view.getBackground()).getBitmap();
            view.setBackground(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }

    /**
     * @param textView 要设置的textview
     * @param string   字符串
     *                 工具方法 抽取出来的
     */
    protected void setTextString(TextView textView, String string) {
        if (!TextUtils.isEmpty(string)) {
            textView.setText(string);
        } else {
            textView.setText(R.string.no_data);
        }
    }

    private int index = 0;

    /**
     * 上传图片的命名规则
     *
     * @param context
     * @param isAvatar true 为头像上传 false 为其他类型图片上传
     * @return
     */
    protected String getImgName(Context context, boolean isAvatar) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserinfoData.getInfoID(context));//infoid
        if (isAvatar) {//模块名称 头像是HeadImg 其余都是ForumImg
            builder.append("HeadImg");
        } else {
            builder.append("ForumImg");
        }

        builder.append(System.currentTimeMillis());//当前时间戳 微秒
        String random = getRandom();
        builder.append(random);//随机码（0~999）
        builder.append(index++);
        builder.append("2");//标记码  android 为2 ios为1
        if (isAvatar) {
            return builder.toString() + ".png";
        } else {
            return builder.toString() + ".png";
        }
    }




    /**
     * 小图的url
     */
    private static final String THUMB = "thumb";
    protected String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }


    /**
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    protected String getRandom() {
        Random random = new Random();
        int i = random.nextInt(1000000);
//        int i = 1 + (int) (Math.random() * 1000000);
        if (i / 100 > 0) {
            return i + "";
        } else if (i / 10 > 0) {
            return "0" + i;
        } else {
            return "00" + i;
        }
    }
    protected void emptyCommnFragment(int resId,String str1,String str2,String str3) {
        NoResultFragment fragment = new NoResultFragment();
        fragment.setResId(resId);
        fragment.setStr_tv1(str1);
        fragment.setStr_tv2(str2);
        fragment.setStr_tv3(str3);
        fragment.setOnClickCircleLister(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl, fragment);
        ft.commit();
    }

}
