package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.FlwForumBean;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.presenter.MyFlwForumPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class MyFlwForumActivity extends BaseActivity implements ViewDataListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MyFlwForumActivity";
    private ListView mListView;
    private ForumAdapter adapter;
    private List<ForumListBean.ForumnumEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreply);
        initData();
        AppManager.getAppManager().addActivity(this);
        initTitlebar(getString(R.string.my_flw));
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        adapter = new ForumAdapter(this, data);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        data = new ArrayList<>();
        new MyFlwForumPresenter(this).getData();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, t.toString());
        FlwForumBean bean = (FlwForumBean) t;
        findViewById(R.id.pd).setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        if (Contants.RESULT.equals(bean.getIResult())) {
            List<ForumListBean.ForumnumEntity> list = bean.getForumNum();
            if (list != null && list.size() > 0) {
                data.addAll(list);
                adapter.update(data);
            } else {
                mListView.setVisibility(View.GONE);
                emptyCommnFragment(R.mipmap.empty_follow,getString(R.string.no_follow_tv1),getString(R.string.no_follow_tv2),getString(R.string.no_follow_tv3));
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
        String index = adapter.getItem(position);
        Intent intent = new Intent(this, ForumDetailActivity.class);
        intent.putExtra(Contants.index, index);
        startActivity(intent);
    }
}
