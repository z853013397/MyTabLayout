package com.zeyuan.kyq.view;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SymptomAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.fragment.AddZLFragment;
import com.zeyuan.kyq.presenter.ConfirmSecondPresenter;
import com.zeyuan.kyq.presenter.FindSymptomPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/21.
 * 这个是查找症状
 */
public class FindSymtomActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, ViewDataListener {
    private static final String TAG = "FindSymtomActivity";
    private Map<String, List<String>> perfrom;
    private List<List<String>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_symptom);
        initData();
        initView();
    }

    private void initData() {
        datas = new ArrayList<>();
        perfrom = GlobalData.bodyPos;
        if (perfrom.size() != 14) {//这个是定死的 14个数据
            throw new RuntimeException("后台数据出错");
        }
//        LogUtil.i(TAG,"perform:" + perfrom.toString());
        Set<String> perfromItem = perfrom.keySet();
            for (String key : perfromItem) {
                List list = perfrom.get(key);
                datas.add(list);
        }


    }

    private ListView mListView;
    private RadioGroup left_rg;
//    private RadioGroup top_rg;

    private void initView() {
        initTitlebar(getString(R.string.find_symptom));
        left_rg = (RadioGroup) findViewById(R.id.left_rg);
//        top_rg = (RadioGroup) findViewById(R.id.top_rg);

        mListView = (ListView) findViewById(R.id.body_detail_listview);


        adapter = new SymptomAdapter(this, perfrom.get("19"));
        mListView.setAdapter(adapter);


        mListView.setOnItemClickListener(this);
//        top_rg.setOnCheckedChangeListener(this);
        left_rg.setOnCheckedChangeListener(this);
        left_rg.check(R.id.cancer_progress);
//        top_rg.check(R.id.after_operation);


    }

    private SymptomAdapter adapter;

    public void findSymtom() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("注意");
//        builder.setMessage("stepid是：" + UserinfoData.getStepID(this) + " \n癌肿是："
//                + UserinfoData.getCancerID(this) + " 分期是：" + UserinfoData.getPeriodID(this)
//                + " 症状id是 :" + PerformID);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
        if (isCancer) {
            new ConfirmSecondPresenter(FindSymtomActivity.this).getData();
        } else {
            new FindSymptomPresenter(FindSymtomActivity.this).getData();
        }

    }
//        });
//        builder.create().show();


//}

    private boolean isCancer = false;//这个表示是肿瘤进展

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.left_rg) {
            switch (checkedId) {
                case R.id.cancer_progress://肿瘤进展
                    adapter.update(perfrom.get("19"));
                    isCancer = true;
                    break;
                case R.id.blood://血液
                    adapter.update(perfrom.get("18"));
                    isCancer = false;
                    break;
                case R.id.all_body://全身
                    adapter.update(perfrom.get("16"));
                    isCancer = false;

                    break;
                case R.id.head://头部
                    adapter.update(perfrom.get("11"));
                    isCancer = false;

                    break;
                case R.id.neck://颈部
                    adapter.update(perfrom.get("15"));
                    isCancer = false;

                    break;
                case R.id.hand://四肢
                    adapter.update(perfrom.get("17"));
                    isCancer = false;

                    break;
                case R.id.chest://胸部
                    adapter.update(perfrom.get("12"));
                    isCancer = false;

                    break;
                case R.id.abdomen://腹部
                    adapter.update(perfrom.get("13"));
                    isCancer = false;

                    break;
                case R.id.pelvis://骨盆
                    adapter.update(perfrom.get("14"));
                    isCancer = false;
                    break;
                case R.id.complication: {//并发症
                    adapter.update(perfrom.get("20"));
                    isCancer = false;
                    break;
                }

                case R.id.blood_test: {//血常规
                    adapter.update(perfrom.get("22"));
                    isCancer = false;
                    break;
                }
                case R.id.urine: {//尿常规
                    adapter.update(perfrom.get("23"));
                    isCancer = false;
                    break;
                }
                case R.id.fb_test: {//粪便常规
                    adapter.update(perfrom.get("24"));
                    isCancer = false;
                    break;
                }
                case R.id.gangg: {//肝功能
                    adapter.update(perfrom.get("25"));
                    isCancer = false;

                    break;
                }
            }

        }
//        if (group.getId() == R.id.top_rg) {
//            switch (checkedId) {
//                case R.id.after_operation://手术
//
//                    break;
//                case R.id.baxiang_cure://靶向
//
//                    break;
//                case R.id.radiation_therapy://放疗
//
//                    break;
//                case R.id.chemotherapeutics://化疗
//
//                    break;
//                case R.id.empty_time://空窗
//
//                    break;
//            }
//        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String symptom = (String) mListView.getAdapter().getItem(position);//
        PerformID = symptom;
        LogUtil.i(TAG, symptom);
//        if (isCancer) {
//            new ConfirmSecondPresenter(this).getData();
//            return;
//        }
        //保存下地址 这个方法每天只调用一次。
        if (UserinfoData.compareTime(this)) {
            UserinfoData.saveRemindTime(this, System.currentTimeMillis() + "");
            addZL();
        }else {
            findSymtom();
        }
    }

    private void toResult(FindSymtomBean bean) {
        Intent intent = new Intent(this, DiagnosisResultActivity.class);
        intent.putExtra(Contants.FindSymtomBean, bean);
        intent.putExtra(Contants.PerformID, PerformID);
        startActivity(intent);//跳转到诊断结果
    }

    private String StepID;
    private String CancerID;
    private String PeriodID;//
//    private String PeriodType = UserinfoData.getPeriodID(this);//

    private String PerformID;

    public void setStepID(String StepID) {
        this.StepID = StepID;
    }

    public void setCancerID(String CancerID) {
        this.CancerID = CancerID;
    }

    public void setPeriodID(String periodID) {
        PeriodID = periodID;
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == NetNumber.CONFIM_SECOND) { //去肿瘤决策树
            map.put(Contants.StepID, StepID);
            map.put(Contants.CancerID, CancerID);
            map.put(Contants.PeriodID, PeriodID);
//            map.put(Contants.PeriodType, PeriodType);
        }
        if (tag == NetNumber.FIND_SYMPTOM) {//查症状
            map.put(Contants.StepID, UserinfoData.getStepID(this));
            map.put(Contants.CancerID, CancerID);
            map.put(Contants.PerformID, PerformID);
        }
        return map;
    }


    @Override
    public void toActivity(Object t, int tag) {
        if (tag == NetNumber.FIND_SYMPTOM) {//查症状
            FindSymtomBean bean = (FindSymtomBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                toResult(bean);
            }
        }
        if (tag == NetNumber.CONFIM_SECOND) {//去完善资料2
            WSZLBean bean = (WSZLBean) t;
            startActivity(new Intent(this, WSZLActivity.class).putExtra(Contants.WSZLBean, bean));
        }
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在查询该症状");
        mProgressDialog.show();
    }

    @Override
    public void hideLoading(int tag) {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(int tag) {
        mProgressDialog.dismiss();
    }

    private AddZLFragment zl;

    private void addZL() {
        isAttach = true;
        zl = new AddZLFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contents, zl, "tag");
        ft.commit();
    }

    private boolean isAttach;

    public void removeZL() {
        isAttach = false;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(zl);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (zl != null) {
//            removeZL();
//        }
        StepID = UserinfoData.getStepID(this);
        CancerID = UserinfoData.getCancerID(this);
        PeriodID = UserinfoData.getPeriodID(this);


    }

    @Override
    public void onBackPressed() {
        if (isAttach) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(zl);
            ft.commit();
            isAttach = false;
            return;
        }
        super.onBackPressed();
    }
}
