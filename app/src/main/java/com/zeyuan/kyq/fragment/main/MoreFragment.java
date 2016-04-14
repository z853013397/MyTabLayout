package com.zeyuan.kyq.fragment.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.mob.tools.utils.UIHandler;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataCleanManager;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.AboutActivity;
import com.zeyuan.kyq.view.GuideActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.MyActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.socialization.Socialization;

public class MoreFragment extends TabFragment implements Handler.Callback, View.OnClickListener {
    private static final String TAG = "MoreFragment";
    private static final String FILE_NAME = "/pic_lovely_cats.jpg";
    private String testImage;
    private OnekeyShare oks;
    private static final String shareURl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.zeyuan.kyq";
    private TextView name;
    private TextView nameOther;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        initView();
        ShareSDK.initSDK(context);
        ShareSDK.registerService(Socialization.class);
        new Thread() {
            public void run() {
                initImagePath();
                UIHandler.sendEmptyMessageDelayed(1, 100, MoreFragment.this);
            }
        }.start();
        return rootView;

    }

    private View to_recommend_friend_layout;
    private ImageView exit;

    private void initView() {
        exit = (ImageView) findViewById(R.id.exit);
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(this);
        to_recommend_friend_layout = findViewById(R.id.to_recommend_friend_layout);
        to_recommend_friend_layout.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        nameOther = (TextView) findViewById(R.id.name_other);

        initTitle();
        avatar = (CircleImageView) findViewById(R.id.avatar);
        showAvatar();

        findViewById(R.id.patient_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MyActivity.class));
            }
        });

        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AboutActivity.class));
            }
        });

        setInforName(name);
//        String inforname = UserinfoData.getInfoname(context);
//        if (!TextUtils.isEmpty(inforname)) {
//            name.setText(inforname);
//        }
        String type = UserinfoData.getType(context);
        if ("1".equals(type)) {
            nameOther.setText("QQ账号登录");
        } else {
            nameOther.setText("微信账号登录");
        }

    }


    private void initTitle() {
        findViewById(R.id.btn_back).setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.more);
    }

    private File shareFileImage;

    private void initImagePath() {
        try {
            String cachePath = com.mob.tools.utils.R.getCachePath(context, null);
            testImage = cachePath + FILE_NAME;
            shareFileImage = new File(Environment.getExternalStorageDirectory(), "share.png");
            if (!shareFileImage.exists()) {
                shareFileImage.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher);
                FileOutputStream fos = new FileOutputStream(shareFileImage);
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            testImage = null;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        initOnekeyShare();
        return false;
    }

    // Socialization服务依赖OnekeyShare组件，此方法初始化一个OnekeyShare对象
    // 此方法的代码从DemoPage中复制而来
    private void initOnekeyShare() {
        oks = new OnekeyShare();
        oks.setAddress("oks.setAddress");

        oks.setTitle(getString(R.string.ssdk_oks_share));// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitleUrl(shareURl);// titleUrl是标题的网络链接，仅在人人网和QQ空间使用


        oks.setText(getString(R.string.share_content)); // text是分享文本，所有平台都需要这个字段

        oks.setImagePath(shareFileImage.getPath());// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImageUrl("http://avatar.csdn.net/E/F/9/1_alexbxp.jpg");

        oks.setUrl(shareURl);// url仅在微信（包括好友和朋友圈）中使用

        oks.setFilePath(shareFileImage.getPath());

        oks.setComment(getString(R.string.ssdk_oks_share));// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setSite(getString(R.string.app_name));// site是分享此内容的网站名称，仅在QQ空间使用

        oks.setSiteUrl(shareURl);//siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setVenueName("抗癌圈");
        oks.setVenueDescription("抗癌圈的Description");
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                // 改写twitter分享内容中的text字段，否则会超长，
                // 因为twitter会将图片地址当作文本的一部分去计算长度
                if ("Twitter".equals(platform.getName())) {
                    paramsToShare.setText("分享内容摘要");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_recommend_friend_layout:
                oks.show(context);
                break;

            case R.id.exit: {
                showExitDialog();
                break;
            }
        }
    }

    private void showExitDialog() {
        new AlertView("是否退出登录?", null, "取消", null,
                new String[]{"确定"},
                context, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtil.i(TAG, position);
                if (0 == position) {
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            super.run();
//                        }
//                    }.start();
                    clearData();

//                    UserinfoData.clearMermory();

                    startActivity(new Intent(context, GuideActivity.class));
                    ((MainActivity) context).finish();
                }
            }
        }).setCancelable(true).show();
    }

    private void clearData() {
        UserinfoData.clearMermory(context);
//        DataCleanManager.cleanSharedPreference(context);
    }


    private void setInforName(TextView mTextView_name) {
        String inforName = UserinfoData.getInfoname(context);
        if (!TextUtils.isEmpty(inforName)) {
            mTextView_name.setText(inforName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (name != null && !name.getText().equals(UserinfoData.getInfoname(context))) {
            setInforName(name);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareFileImage != null) {
            shareFileImage.delete();
        }
    }
}
