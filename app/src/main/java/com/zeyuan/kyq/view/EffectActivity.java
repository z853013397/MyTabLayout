package com.zeyuan.kyq.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.presenter.GetCommDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 副作用
 */
public class EffectActivity extends BaseActivity implements AdapterView.OnItemClickListener, ViewDataListener {


    private static final String TAG = "EffectActivity";
    List<String> MaybeEffect;
    private String CureConfID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_side_effect);
        MaybeEffect = (List<String>) getIntent().getSerializableExtra(Contants.List);
        CureConfID = getIntent().getStringExtra(Contants.CureConfID);
//        Type = getIntent().getStringExtra(Contants.Type);
        initView();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.listview);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("副作用");
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview);
        for (String item : MaybeEffect) {
            adapter.add(GlobalData.performValues.get(item));
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PerformID = MaybeEffect.get(position);
//        Intent intent = new Intent(EffectActivity.this, ResultDetailActivity.class);
//        intent.putExtra(Contants.PerformID, performId);
//        intent.putExtra(Contants.CureConfID,CureConfID);
//        startActivity(intent);
        new GetCommDetailPresenter(this).getData();

    }

    private String PerformID;
    private String Type = "2";//type为1通过 查症状进副作用结果详情，
                        // type为2通过 副作用列表/小医生进副作用结果详情

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 3) {//普通决策树
            map.put(Contants.CureConfID, CureConfID);
            map.put(Contants.PerformID, PerformID);
            map.put(Contants.Type, Type);

        }
        LogUtil.i(TAG, map.toString());
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        if (tag == 3) {//普通决策树
            CommBean bean = (CommBean) t;
            Intent intent = new Intent(EffectActivity.this, ResultDetailActivity.class);
            intent.putExtra(Contants.CommBean, bean);
            intent.putExtra(Contants.PerformID, PerformID);
            startActivity(intent);
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
