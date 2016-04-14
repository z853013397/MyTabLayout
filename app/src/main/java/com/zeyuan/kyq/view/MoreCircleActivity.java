package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.FindCircleDetailAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.presenter.FollowCirclePresenter;
import com.zeyuan.kyq.presenter.GetCancerForumPresenter;
import com.zeyuan.kyq.presenter.GetCityForumPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同城圈和抗癌圈的人数和数量
 */
public class MoreCircleActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewDataListener, AdapterView.OnItemClickListener, FindCircleDetailAdapter.FollowCircleListener {

    private static final String TAG = "SearchCircleActivity";
    //    private FindCircleDetailAdapter cancerAdapter;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_circle);
        initTitlebar("更多圈子");
        initView();
        initData();
    }

    private void initData() {
        new GetCancerForumPresenter(this).getData();//获取抗癌圈的人数和数量(他的tag是1)
        new GetCityForumPresenter(this).getData();//获取同城圈的人数和数量
    }


    private void getCityCircle() {
        new GetCityForumPresenter(this).getData();//获取同城圈的人数和数量
    }

    private void getCancerCircle() {
        new GetCancerForumPresenter(this).getData();//获取抗癌圈的人数和数量(他的tag是1)
    }


    private ListView mListView;
    private FindCircleDetailAdapter leftAdapter;
    private FindCircleDetailAdapter rightdapter;

    private List<CityCancerForumBean.NumEntity> leftData;
    private List<CityCancerForumBean.NumEntity> rightData;

    private void initView() {
        leftData = new ArrayList<>();
        rightData = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.listview);
        leftAdapter = new FindCircleDetailAdapter(this, leftData);
        leftAdapter.setListener(this);
        rightdapter = new FindCircleDetailAdapter(this, rightData);
        rightdapter.setListener(this);
        mListView.setAdapter(leftAdapter);
        rg = (RadioGroup) findViewById(R.id.circle);
        rg.setOnCheckedChangeListener(this);
        rg.check(R.id.tongcheng);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tongcheng:
                mListView.setAdapter(leftAdapter);
                break;

            case R.id.kangai:
//                rightdapter.update(rightData);
                mListView.setAdapter(rightdapter);
                break;
        }
    }

    private String circleID;
    private String followOrcancel;////1关注 2.取消

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 0) {

        }
//
        if (tag == 1) {//获取抗癌圈的人数和数量(他的tag是1)
            map.put(Contants.CancerID, UserinfoData.getCancerID(this));
        }

        if (tag == NetNumber.FOLLOW_CIRCLE) {
            map.put(Contants.CircleID, circleID);
            map.put(Contants.followOrcancel, followOrcancel);//1关注 2.取消
            String type = "";
            if (Integer.valueOf(circleID) < 10000) {
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
            CityCancerForumBean bean = (CityCancerForumBean) t;
            List<CityCancerForumBean.NumEntity> list = bean.getNum();
            if (list != null && list.size() > 0) {
                list = setFollow(list);
                leftData.addAll(list);
                leftAdapter.update(leftData);
            }

            LogUtil.i(TAG, "这个是同城圈的数据：" + bean);
        }
        if (position == 1) {//获取抗癌圈的人数和数量(他的tag是1)
            CityCancerForumBean bean = (CityCancerForumBean) t;
            LogUtil.i(TAG, "这个是抗癌圈的数据：" + bean);
            List list = bean.getNum();
            if (list != null && list.size() > 0) {
                list = setFollow(list);
                rightData.addAll(list);
                rightdapter.update(rightData);
            }
        }
        if (position == NetNumber.FOLLOW_CIRCLE) {
            FollowBean bean = (FollowBean) t;
            if (Contants.OK_DATA.equals(bean.getIResult())) {
                if ("1".equals(followOrcancel)) {
                    UserinfoData.addFocusCircle(this, circleID);
                    Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
//                    leftAdapter.update();
                } else {
                    UserinfoData.removeFocusCircle(this, circleID);
                    Toast.makeText(this, "取消关注成功", Toast.LENGTH_SHORT).show();
//                    leftAdapter.update();
                }
            } else {
//            Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List setFollow(List<CityCancerForumBean.NumEntity> list) {
        List<String> focusCircle = UserinfoData.getFocusCircle(this);
        if (focusCircle == null || focusCircle.size() == 0) {
            return list;
        }
        for (CityCancerForumBean.NumEntity entity : list) {
            if (focusCircle.contains(entity.getCircleID())) {
                entity.setIsFollow(true);
            } else {
                entity.setIsFollow(false);
            }
        }
        return list;
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

    private void toCircle(String circleID) {
        Intent intent = new Intent(this, CircleActivity.class);
        intent.putExtra(Contants.CircleID, circleID);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String circleId = (String) mListView.getAdapter().getItem(position);
        toCircle(circleId);
    }

    @Override
    public void followCircle(BaseAdapter adapter, boolean isFollow, int position) {
        circleID = (String) adapter.getItem(position);
        if (adapter == leftAdapter) {
            circleID = leftAdapter.getItem(position);
        }
        if (adapter == rightdapter) {
            circleID = rightdapter.getItem(position);
        }
        if (isFollow) {
            followOrcancel = "1";
        } else {
            followOrcancel = "2";
        }
        folowCircle();
    }


    private void folowCircle() {
        new FollowCirclePresenter(this).getData();
    }
}
