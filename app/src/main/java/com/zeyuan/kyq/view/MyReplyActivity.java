package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MyReplyAdapter;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.presenter.GetMyReplyListPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/6.
 * 我的回复列表
 */
public class MyReplyActivity extends BaseActivity implements AdapterView.OnItemClickListener, ViewDataListener {
    private static final String TAG = "MyReplyActivity";
    private MyReplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_myreply);
        initTitlebar(getString(R.string.comment_reply));
        initData();
        initView();
    }

    private void initData() {
        data = new ArrayList();
        new GetMyReplyListPresenter(this).getData();

    }

    private ListView listview;
    private List<MyReplyListBean.ReplyNumEntity> data;

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        adapter = new MyReplyAdapter(this, data);
        listview.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ForumDetailActivity.class);
        intent.putExtra(Contants.index, adapter.getItem(position));
        startActivity(intent);
    }

    private String page = "1";

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == NetNumber.GET_MY_REPLY_LIST) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.page, page);

        }

        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        if (tag == NetNumber.GET_MY_REPLY_LIST) {
            LogUtil.i(TAG, t.toString());
            MyReplyListBean bean = (MyReplyListBean) t;
            findViewById(R.id.pd).setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            List<MyReplyListBean.ReplyNumEntity> list = bean.getReplyNum();
            if (list != null && list.size() > 0) {
                    data.addAll(list);
                adapter.update(list);
            }else {//显示空白提醒
                listview.setVisibility(View.GONE);
                emptyCommnFragment(R.mipmap.empty_reply, getString(R.string.no_reply_tv1), getString(R.string.no_reply_tv2), getString(R.string.no_reply_tv3));
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
}
