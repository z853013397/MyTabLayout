package com.zeyuan.kyq.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MedicineNumAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.presenter.SetPlanMedicinePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.xlistview.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/10.
 * 方案详情
 * 联合用药：
 * AllDesc ==> 方案描述
 * remark ==>备注
 * suit ==> 某个药的方案描述/  描述+适应症
 * <p/>
 * 单个用药：
 * 无AllDesc
 * remark ==>备注
 * suit ==> 某个药的方案描述/  描述+适应症
 */
public class CaseDetailActivity extends BaseActivity implements View.OnClickListener, ViewDataListener {

    private static final String TAG = "CaseDetailActivity";
    private CaseDetailBean mCaseDetailBean;
    private RelativeLayout ll_tip;
    private Button add_plan_drugs;//添加到计划用药

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casedetail);
        initTitlebar(getString(R.string.case_detail));
        mCaseDetailBean = (CaseDetailBean) getIntent().getSerializableExtra(Contants.CaseDetailBean);
        PlanStepID = mCaseDetailBean.getStepId();
        initView();
        initData();
        initListener();
    }

    private void initData() {
//        new GetSolutionDetailPresenter(this).getData();
        if (mCaseDetailBean != null) {
            bindView(mCaseDetailBean);
        }
    }

    private void initListener() {
        add_plan_drugs.setOnClickListener(this);
    }

    private TextView usage;//用法
    private TextView tv_tip;//备注
    private TextView drug_title;
    private TextView img_tip;
    private LinearLayout content;//主要内容的

    private void initView() {
        add_plan_drugs = (Button) findViewById(R.id.add_paln_drugs);
        content = (LinearLayout) findViewById(R.id.content);
        ll_dy = (RelativeLayout) findViewById(R.id.ll_dy);
        ll_tip = (RelativeLayout) findViewById(R.id.ll_tip);
        usage = (TextView) findViewById(R.id.tv_usage);
        img_tip = (TextView) findViewById(R.id.img_tip);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        drug_title = (TextView) findViewById(R.id.drug_title);
//        listView = (XListView) findViewById(R.id.listview);
//        list = new ArrayList<>();
//         adapter = new MedicineNumAdapter(this, list);
//        listView.setAdapter(adapter);
    }

    //    MedicineNumAdapter adapter;
//    XListView listView;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_paln_drugs: {
//                showToast("这个是添加计划用药！");
                new SetPlanMedicinePresenter(this).getData();
                break;
            }
        }

    }

    //    private String PlanStepID = "30";
//    private String StepID = UserinfoData.getStepID(this);//TemplateID
//    private String TemplateID;//模板id
    private String PlanStepID;//添加到计划用药的id

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
//        if (tag == 0) {//
//            map.put(Contants.StepID, StepID);
//            map.put(Contants.TemplateID, TemplateID);
//
//
//        }


        if (tag == NetNumber.SET_PLAN_MEDICINE) {//添加到计划用药
            map.put(Contants.PlanStepID, PlanStepID);
        }
        return map;
    }

    //    List<CaseDetailBean.MedicineNumEntity> list;
    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, t.toString());
        if (tag == NetNumber.SET_PLAN_MEDICINE) {
            BaseBean bean = (BaseBean) t;
            if (Contants.RESULT.equals(bean.iResult)) {
                showToast("添加成功");
                UserinfoData.planId = PlanStepID;
            }
        }


//        CaseDetailBean bean = (CaseDetailBean) t;
//
//        if (Contants.RESULT.equals(bean.getIResult())) {
//            bindView(bean);
//        }

    }

    RelativeLayout ll_dy;
    private static final String DY = "1";//单药

    private void bindView(CaseDetailBean bean) {
        String stepType = bean.getStepType();
        if (DY.equals(stepType)) {//说明单药
            ll_dy.setVisibility(View.GONE);//隐藏用法
        }
        String allDesc = bean.getAllDesc();
        if (!TextUtils.isEmpty(allDesc)) {
            usage.setText(bean.getAllDesc());
        }
        String remark = bean.getRemark();
        if (TextUtils.isEmpty(remark)) {//没有备注
            ll_tip.setVisibility(View.GONE);//隐藏备注
            drug_title.setVisibility(View.GONE);//同时隐藏 文本方案介绍
//            findViewById(R.id.line1).setVisibility(View.GONE);
        } else {
            tv_tip.setText(bean.getRemark());
        }
        List<CaseDetailBean.MedicineNumEntity> list = bean.getMedicineNum();
        if (list != null && list.size() > 0) {
            initMedicaView(list);
        }
    }

    private void initMedicaView(List<CaseDetailBean.MedicineNumEntity> list) {
//        String temp = "";
//        if (list == null || list.isEmpty()) {
//            return;
//        }

//        this.list.addAll(list);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        content.addView(listView);
//        return;
        for (int i = 0, j = list.size(); i < j; i++) {
            CaseDetailBean.MedicineNumEntity item = list.get(i);
//        for (CaseDetailBean.MedicineNumEntity item : list) {
            ViewGroup medicaDec = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_casedetails, content, false);//副作用过多显示3个
//            temp = temp + item.getCommonName() + "+";
            boolean isLast = (i == (j - 1));
            initView(medicaDec, item, isLast);

            content.addView(medicaDec);
        }
    }

    private void initView(View medicaDec, final CaseDetailBean.MedicineNumEntity item, boolean isLast) {


        TextView tv_title = (TextView) medicaDec.findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) medicaDec.findViewById(R.id.tv_desc);
        TextView effective = (TextView) medicaDec.findViewById(R.id.effective);
        TextView useage = (TextView) medicaDec.findViewById(R.id.use_durgs);
//        View emptView = medicaDec.findViewById(R.id.empty_view);
        if (isLast) {
            medicaDec.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        } else {
            medicaDec.findViewById(R.id.below_detail_line).setVisibility(View.GONE);
        }

        LinearLayout side_effect = (LinearLayout) medicaDec.findViewById(R.id.side_effect);//副作用
        LinearLayout ll_details = (LinearLayout) medicaDec.findViewById(R.id.ll_details);//详细说明


        ll_details.setOnClickListener(new View.OnClickListener() {//详细说明
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseDetailActivity.this, DrugActivity.class);
                intent.putExtra(Contants.Drug, item);
                startActivity(intent);
            }
        });
        useage.setText(item.getUsage());
        tv_title.setText(item.getCommonName() + "介绍");
        tv_desc.setText(item.getSuit());
        List<String> sideEf = item.getMaybeSideEffect();
        StringBuilder temp = new StringBuilder();
        if (sideEf.size() > 3) {
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    temp = temp.append(GlobalData.performValues.get(sideEf.get(i)));
                    continue;
                }
                temp = temp.append("," + GlobalData.performValues.get(sideEf.get(i)));
            }
        } else {
            for (int i = 0; i < sideEf.size(); i++) {
                if (i == 0) {
                    temp = temp.append(GlobalData.performValues.get(sideEf.get(i)));
                    continue;
                }
                temp = temp.append("," + GlobalData.performValues.get(sideEf.get(i)));
            }
        }
        if (TextUtils.isEmpty(temp)) {
//            effective.setText(R.string.none);
//            side_effect.setClickable(false);
        } else {
            effective.setText(temp);
//        effective.setText(item.getMaybeSideEffect().toString());
            side_effect.setOnClickListener(new View.OnClickListener() {//副作用的点击
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CaseDetailActivity.this, EffectActivity.class).
                            putExtra(Contants.List, (Serializable) item.getMaybeSideEffect())
                            .putExtra(Contants.CureConfID, item.getCureConfID());
//                .putExtra(Contants.CureConfID,ZYApplication.cureId.get("458"));
                    startActivity(intent);
                }
            });
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

    public void dismissFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment f = fm.findFragmentByTag("fragment");
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(f);
        ft.commit();
    }

}
