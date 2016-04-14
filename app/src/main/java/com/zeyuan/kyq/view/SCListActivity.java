package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.SimalarListBean;
import com.zeyuan.kyq.presenter.GetSimilarListPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-15
 * Time: 10:46
 * FIXME
 * <p>
 * 相似案例的列表项
 */

public class SCListActivity extends BaseActivity implements ViewDataListener, AdapterView.OnItemClickListener {
    private static final String TAG = "SCListActivity" ;
    private List<ForumListBean.ForumnumEntity> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simalar_case);
        initTitlebar("相似案例");
        initView();
        initData();
    }
    private ListView mListView;
    private ForumAdapter adapter;
    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        datas = new ArrayList<>();
        adapter = new ForumAdapter(this, datas);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        new GetSimilarListPresenter(this).getData();
    }

//    private String CureConfID;

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == NetNumber.SIMILAR_LIST) {//"CancerTypeID=30&StepID=556&CureConfID=4205"
            map.put(Contants.CancerTypeID, UserinfoData.getCancerID(this));
            map.put(Contants.CureConfID, MapDataUtils.getAllCureconfID(UserinfoData.getStepID(this)));
            map.put(Contants.StepID, UserinfoData.getStepID(this));
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        if (tag == NetNumber.SIMILAR_LIST) {
            SimalarListBean bean = (SimalarListBean) t;
            LogUtil.i(TAG, t.toString());
            if (Contants.RESULT.equals(bean.getIResult())) {
                bindView(bean);
            }
        }

    }

    private void bindView(SimalarListBean bean) {
        List<ForumListBean.ForumnumEntity> list = bean.getAllPostNum();
        if (list != null && list.size() > 0) {
            datas.clear();
            datas.addAll(list);
            adapter.notifyDataSetChanged();
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
        startActivity(new Intent(this, ForumDetailActivity.class).putExtra(Contants.index, String.valueOf(id)));//put something

    }
}
