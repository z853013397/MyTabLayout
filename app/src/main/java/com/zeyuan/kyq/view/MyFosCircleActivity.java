package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.FindCircleDetailAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.presenter.FollowCirclePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我关注的圈子
 */
public class MyFosCircleActivity extends BaseActivity implements FindCircleDetailAdapter.FollowCircleListener, ViewDataListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MyFosCircleActivity";
    private ListView mListView;
    private FindCircleDetailAdapter adapter;
    private List data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fos_circle);
        initTitlebar(getString(R.string.foucs_circle));
        initData();
        initView();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        data = new ArrayList();
        initList();
        LogUtil.i(TAG, data.size());
        adapter = new FindCircleDetailAdapter(this, data);
        adapter.setListener(this);
    }

    private void initList() {
        List<String> focusCircles = UserinfoData.getFocusCircle(this);
        if (focusCircles == null) {
            return;
        }
        CityCancerForumBean.NumEntity num = null;
        LogUtil.i(TAG, focusCircles.toString());
        for (String item : focusCircles) {
            num = new CityCancerForumBean.NumEntity();
            num.setCircleID(item);
            num.setIsFollow(true);
            data.add(num);
        }
//        num.setCircleID("1001");
    }

    private String circleID;
    private String followOrcancel;////1关注 2.取消

    @Override
    public void followCircle(BaseAdapter adapter, boolean isFollow, int position) {
        circleID = (String) adapter.getItem(position);

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

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
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
    public void toActivity(Object t, int tag) {
        if (tag == NetNumber.FOLLOW_CIRCLE) {
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

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String circleId = (String) mListView.getAdapter().getItem(position);
        toCircle(circleId);
    }

    private void toCircle(String circleID) {
        Intent intent = new Intent(this, CircleActivity.class);
        intent.putExtra(Contants.CircleID, circleID);
        startActivity(intent);
    }

}
