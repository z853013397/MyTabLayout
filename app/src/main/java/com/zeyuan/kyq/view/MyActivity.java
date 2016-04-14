package com.zeyuan.kyq.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.MyDataBean;
import com.zeyuan.kyq.presenter.GetMyCircleNumPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的圈子 帖子  评论/回复 收藏
 */
public class MyActivity extends BaseActivity implements View.OnClickListener, ViewDataListener {


    private static final String TAG = "MyActivity";
    private CircleImageView CircleImageView_avatar;//头像
    private TextView mTextView_name;//名字
//    private TextView mTextView_loc;//位置


    private TextView mTextView_circle_number;//圈子右边显示的数字

    /**
     * 收藏里面的4个圈子
     */
    private TextView mTextView_circle1;
    private TextView mTextView_circle2;
    private TextView mTextView_circle3;
    private TextView mTextView_circle4;


    private TextView mTextView_forum_number;//帖子右边显示的数字
    private TextView mTextView_lforum_des;//帖子里面的描述

    private TextView mTextView_reply_number;//评论和回复显示的数字
    private TextView mTextView_reply_des;//评论和回复里面的描述


    private TextView mTextView_collection_num;//收藏里面的数字
    private TextView mTextView_collection_des;//收藏里面的描述


    private TextView title;//当然是标题咯


    private RelativeLayout mRelativeLayout_forum;//帖子的点击
    private RelativeLayout mRelativeLayout_collection;//收藏的点击
    private RelativeLayout mRelativeLayout_circle;//圈子的点击
    private RelativeLayout mRelativeLayout_reply;//评论/回复的点击


    private void initView() {
        CircleImageView_avatar = (com.zeyuan.kyq.widget.CircleImageView) findViewById(R.id.avatar);
        mTextView_name = (TextView) findViewById(R.id.name);
        setInforName(mTextView_name);
//        mTextView_loc = (TextView)findViewById(R.id.loc);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.my));

        mTextView_circle_number = (TextView) findViewById(R.id.circle_number);
        mTextView_circle1 = (TextView) findViewById(R.id.circle1);
        mTextView_circle2 = (TextView) findViewById(R.id.circle2);
        mTextView_circle3 = (TextView) findViewById(R.id.circle3);
        mTextView_circle4 = (TextView) findViewById(R.id.circle4);


        mTextView_forum_number = (TextView) findViewById(R.id.forum_number);
        mTextView_lforum_des = (TextView) findViewById(R.id.forum_des);

        mTextView_reply_number = (TextView) findViewById(R.id.reply_number);
        mTextView_reply_des = (TextView) findViewById(R.id.reply_des);

        mTextView_collection_num = (TextView) findViewById(R.id.collection_num);
        mTextView_collection_des = (TextView) findViewById(R.id.collection_des);//收藏

        String avatarUrl = UserinfoData.getAvatarUrl(this);
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this).load(avatarUrl).signature(new IntegerVersionSignature(1)).into(CircleImageView_avatar);
        }
        mRelativeLayout_circle = (RelativeLayout) findViewById(R.id.circle);//圈子
//        上面都是文本 下面是点击
        mRelativeLayout_forum = (RelativeLayout) findViewById(R.id.forum);//帖子
        mRelativeLayout_reply = (RelativeLayout) findViewById(R.id.reply);//帖子
        mRelativeLayout_collection = (RelativeLayout) findViewById(R.id.collection);//s收藏
        initFocusTV();

    }

    private int followCircleSize;

    private void initFocusTV() {
        List<String> foucusCirlce = UserinfoData.getFocusCircle(this);
        if (foucusCirlce == null || foucusCirlce.isEmpty()) {
            return;
        }
        followCircleSize = foucusCirlce.size();

        switch (followCircleSize) {
            case 0: {
                mTextView_circle1.setVisibility(View.GONE);
                mTextView_circle2.setVisibility(View.GONE);
                mTextView_circle3.setVisibility(View.GONE);
                mTextView_circle4.setVisibility(View.GONE);
                break;
            }
            case 1: {
                mTextView_circle1.setVisibility(View.VISIBLE);
                mTextView_circle1.setText(getCircleName(foucusCirlce, 0));
                mTextView_circle2.setVisibility(View.GONE);
                mTextView_circle3.setVisibility(View.GONE);
                mTextView_circle4.setVisibility(View.GONE);
                break;
            }
            case 2: {
                mTextView_circle1.setVisibility(View.VISIBLE);
                mTextView_circle2.setVisibility(View.VISIBLE);

                mTextView_circle1.setText(getCircleName(foucusCirlce, 0));
                mTextView_circle2.setText(getCircleName(foucusCirlce, 1));
                mTextView_circle3.setVisibility(View.GONE);
                mTextView_circle4.setVisibility(View.GONE);


                break;
            }
            case 3: {
                mTextView_circle1.setVisibility(View.VISIBLE);
                mTextView_circle2.setVisibility(View.VISIBLE);
                mTextView_circle3.setVisibility(View.VISIBLE);

                mTextView_circle1.setText(getCircleName(foucusCirlce, 0));
                mTextView_circle2.setText(getCircleName(foucusCirlce, 1));
                mTextView_circle3.setText(getCircleName(foucusCirlce, 2));
                mTextView_circle4.setVisibility(View.GONE);
                break;
            }
            default: {

            }
            case 4: {
                mTextView_circle1.setVisibility(View.VISIBLE);
                mTextView_circle2.setVisibility(View.VISIBLE);
                mTextView_circle3.setVisibility(View.VISIBLE);
                mTextView_circle4.setVisibility(View.VISIBLE);

                mTextView_circle1.setText(getCircleName(foucusCirlce, 0));
                mTextView_circle2.setText(getCircleName(foucusCirlce, 1));
                mTextView_circle3.setText(getCircleName(foucusCirlce, 2));
                mTextView_circle4.setText(getCircleName(foucusCirlce, 3));
                break;
            }


        }


    }

    private String getCircleName(List<String> foucusCirlce, int position) {
        String circle = foucusCirlce.get(position);
        return MapDataUtils.getCircleValues(circle);
    }

    private void setTV(int position) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        AppManager.getAppManager().addActivity(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        new GetMyCircleNumPresenter(this).getData();

    }

    private void setListener() {
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        mRelativeLayout_circle.setOnClickListener(this);
        mRelativeLayout_forum.setOnClickListener(this);
        mRelativeLayout_reply.setOnClickListener(this);
        mRelativeLayout_collection.setOnClickListener(this);
        CircleImageView_avatar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
                break;
            }
            case R.id.forum: {
                startActivity(new Intent(this, MyForumActivity.class));
                break;
            }
            case R.id.reply: {//我的评论和回复
                LogUtil.i(TAG, "reply");
                startActivity(new Intent(this, MyReplyActivity.class));

                break;
            }

            case R.id.circle: {//我的圈子
//                Toast.makeText(this, "点击了圈子", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MyFosCircleActivity.class));

                break;
            }
            case R.id.collection: {//
//                Toast.makeText(this, "点击了收藏", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MyFlwForumActivity.class));

                break;
            }
            case R.id.avatar: {
                startActivity(new Intent(this, PatientDetailActivity.class));
                break;
            }
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
//        if (tag == 0) {
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, t.toString());
        MyDataBean bean = (MyDataBean) t;
        if (Contants.RESULT.equals(bean.getIResult())) {
            bindView(bean);
        }
    }

    private void bindView(MyDataBean bean) {
        /**
         * 数字设置
         */
//        String circleNum = bean.getCircleNum();
        mTextView_circle_number.setText(followCircleSize + "");
        String forumNumNum = bean.getForumNum();
        mTextView_forum_number.setText(forumNumNum);
        String favorNim = bean.getFavorNum();
        mTextView_collection_num.setText(favorNim);
        String replyNum = bean.getReplyNum();
        mTextView_reply_number.setText(replyNum);
        /**
         * 文本设置
         */
        String lasrForumTitle = bean.getLastForumTitle();//帖子
        mTextView_lforum_des.setText(lasrForumTitle);

        String lasrFavorTitle = bean.getLastFavorTitle();
        mTextView_collection_des.setText(lasrFavorTitle);

        String fromuser = bean.getReply().getFromuser();
        String touser = bean.getReply().getTouser();
        String content = bean.getReply().getContent();
        if (!TextUtils.isEmpty(content)) {
            mTextView_reply_des.setText(fromuser + "回复" + touser + ":" + content);
        }

    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }


    private void setInforName(TextView mTextView_name) {
        String inforName = UserinfoData.getInfoname(this);
        if (!TextUtils.isEmpty(inforName)) {
            mTextView_name.setText(inforName);
        }
    }
}
