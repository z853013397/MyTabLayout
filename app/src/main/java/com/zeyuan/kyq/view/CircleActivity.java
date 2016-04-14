package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.presenter.FollowCirclePresenter;
import com.zeyuan.kyq.presenter.GetForumListPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 进入某个圈子
 * 获取某个圈子下的帖子列表
 * 进入这个activity 必须带上circleid 的参数
 * 才能拉数据
 */
public class CircleActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, AdapterView.OnItemClickListener, XListView.IXListViewListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "CircleActivity";
    private XListView listview;
    private ForumAdapter mForumAdapter;
    private List data;
    private CheckBox isfollow;//是否关注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        initTitlebar(getString(R.string.froum_list));
        CircleID = getIntent().getStringExtra(Contants.CircleID);//测试注释了
        if (TextUtils.isEmpty(CircleID)) {
            throw new RuntimeException("CircleActivity error entrance!");
        }
        data = new ArrayList();
        initData();
        initView();
    }

    private void initData() {
        new GetForumListPresenter(this).getData();
    }

    private TextView circleName;
    private TextView topicNum;//话题
    private TextView number;//人数

    public void initView() {
        isfollow = (CheckBox) findViewById(R.id.isfollow);
        setFollowStatus();
        topicNum = (TextView) findViewById(R.id.topic_number);
        number = (TextView) findViewById(R.id.number);
        circleName = (TextView) findViewById(R.id.circle_name).findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        listview = (XListView) findViewById(R.id.listview);
        listview.setPullRefreshEnable(true);
        listview.setPullLoadEnable(true);
        listview.setAutoLoadEnable(true);
        listview.setXListViewListener(this);
        listview.setRefreshTime(getTime());
//        initHeaderListView();
//        listview.addHeaderView(headerListView);
        mForumAdapter = new ForumAdapter(this, data);
        listview.setAdapter(mForumAdapter);
        listview.setOnItemClickListener(this);
        isfollow.setOnCheckedChangeListener(this);
    }

    private void setFollowStatus() {
        List<String> focusCircles = UserinfoData.getFocusCircle(this);
        if (focusCircles.contains(CircleID)) {
            isfollow.setChecked(true);
        } else {
            isfollow.setChecked(false);
        }
    }

//    private void initHeaderListView() {
//        headerListView = new MyListView(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        adapter = new TopForumAdapter(this);
//        headerListView.setAdapter(adapter);
//        headerListView.setOnItemClickListener(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private String CircleID;
    private String followOrcancel;////1关注 2

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 0) {
            map.put(Contants.CircleID, CircleID);
        }

        if (tag == NetNumber.FOLLOW_CIRCLE) {
            map.put(Contants.CircleID, CircleID);
            map.put(Contants.followOrcancel, followOrcancel);//1关注 2.取消
            String type = "";
            if (Integer.valueOf(CircleID) < 10000) {
                type = "1";
            } else {
                type = "2";
            }
            map.put("type", type);//1.同城圈 2.抗癌圈  circle< 10000则是同城圈 >=10000则是抗癌圈
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {
            ForumListBean bean = (ForumListBean) t;
            LogUtil.i(TAG, bean.toString());

            if (Contants.OK_DATA.equals(bean.getIResult())) {
                circleName.setText(MapDataUtils.getCircleValues(CircleID));
                topicNum.setText(getString(R.string.forum_num) + bean.getForumNum());
                number.setText(getString(R.string.person_num) + bean.getUserNum());
                List list = bean.getForumnum();
                if (list != null && list.size() > 0) {
                    data.addAll(list);
                    mForumAdapter.update(data);
                }
            }
        }

        if (position == NetNumber.FOLLOW_CIRCLE) {
            FollowBean bean = (FollowBean) t;
            if (Contants.OK_DATA.equals(bean.getIResult())) {
                if ("1".equals(followOrcancel)) {
                    UserinfoData.addFocusCircle(this, CircleID);
                    Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
//                    leftAdapter.update();
                } else {
                    UserinfoData.removeFocusCircle(this, CircleID);
                    Toast.makeText(this, "取消关注成功", Toast.LENGTH_SHORT).show();
//                    leftAdapter.update();
                }
            } else {
//            Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        setProgressGone();
    }

    @Override
    public void showError(int tag) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == listview) {
            startActivity(new Intent(this, ForumDetailActivity.class).putExtra(Contants.index, String.valueOf(id)));
        }
    }

    /**
     * 这个上拉刷新的功能
     */
    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
    }

    /**
     * 这个下啦加载的功能
     */
    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();

            }
        }, 2000);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 停止刷新 恢复原状
     */
    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime(getTime());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {//关注
            followOrcancel = "1";
        } else {//取消关注
            followOrcancel = "2";
        }
        folowCircle();
    }

    private void folowCircle() {
        new FollowCirclePresenter(this).getData();
    }
}
