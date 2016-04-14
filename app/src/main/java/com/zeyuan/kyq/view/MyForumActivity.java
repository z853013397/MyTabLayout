package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ReleaseForumAdapter;
import com.zeyuan.kyq.app.AppManager;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.presenter.GetMyForumPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的 打开的activity
 * <p>
 * 里面是个listview
 */
public class MyForumActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MyForumActivity";
    //    private RadioGroup rg;
    private ReleaseForumAdapter adapter;
    private ListView mListView;
    private List<MyForumReleaseBean.ForumBeanEntity> datas;//这个是数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_myreply);
        datas = new ArrayList<>();
        initView();
        initTitlebar(getString(R.string.my_forum));
        initData();
        setListener();
    }

    private void initData() {
        new GetMyForumPresenter(this).getData();
    }


    private void setListener() {
//        rg.setOnCheckedChangeListener(this);
//        rg.check(R.id.forum);
    }


//    private RadioButton mRadioButton_forum;
//    private RadioButton mRadioButton_reply;
//    private ImageView back;

    private void initView() {
//        mRadioButton_forum = (RadioButton) findViewById(R.id.forum);
//        mRadioButton_reply = (RadioButton) findViewById(R.id.reply);
//        back = (ImageView) findViewById(R.id.btn_back);
//        back.setOnClickListener(this);
//        rg = (RadioGroup) findViewById(R.id.rg);

//        view1 = findViewById(R.id.view1);
//        view2 = findViewById(R.id.view2);
        mListView = (ListView) findViewById(R.id.listview);
        datas = new ArrayList<>();
        adapter = new ReleaseForumAdapter(this, datas);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
//        adapter = new ForumAdapter(this);
//        mListView.setAdapter(adapter);
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.forum:
//                view1.setVisibility(View.VISIBLE);
//                view2.setVisibility(View.INVISIBLE);
//                mRadioButton_forum.setSelected(true);
//                mRadioButton_reply.setSelected(false);
//
//                break;
//            case R.id.reply:
//                view1.setVisibility(View.INVISIBLE);
//                view2.setVisibility(View.VISIBLE);
//                mRadioButton_forum.setSelected(false);
//                mRadioButton_reply.setSelected(true);
//
//                break;
//        }
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_back:
//                finish();
//                break;
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {//这个是我发布的帖子
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {//这个是我发布的帖子
            MyForumReleaseBean bean = (MyForumReleaseBean) t;
            LogUtil.i(TAG, "发布帖子返回的数据： " + bean.toString());

            if (Contants.OK_DATA.equals(bean.getIResult())) {
                findViewById(R.id.pd).setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                List list = bean.getForumNum();
                if (list != null && list.size() > 0) {
                    datas.addAll(list);
                    adapter.update(datas);
                } else {
                    mListView.setVisibility(View.GONE);
                    emptyCommnFragment(R.mipmap.empty_forum, getString(R.string.empty_forum_tv1), getString(R.string.empty_forum_tv2), getString(R.string.empty_forum_tv3));
                }


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
        intent.putExtra(Contants.index, index);//帖子id
        startActivity(intent);
    }
}
