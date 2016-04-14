package com.zeyuan.kyq.view;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ResultAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.fragment.empty.EmptyResulDetalFragment;
import com.zeyuan.kyq.presenter.GetSolutionDetailPresenter;
import com.zeyuan.kyq.presenter.SetPlanMedicinePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomExpandableListView;
import com.zeyuan.kyq.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/6.
 * 结果详情--
 * 肿瘤决策树和普通决策树 装数据就好
 * 只有肿瘤决策树有添加到计划用药和小圆点（待定）
 * 肿瘤 决策树 进入结果详情 需要传入TemplateID  getSulutionDetail
 * 普通决策树进入结果详情    不需要传入TemplateID
 * 从资料确认2进入结果详情需要传templateid
 */
public class ResultDetailActivity extends BaseActivity implements ViewDataListener, ResultAdapter.CheckBoxCheckListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, View.OnClickListener {
    private static final String TAG = "ResultDetailActivity";
    private static final String BXY_ID = "4205";
    private CustomExpandableListView topListview;//可能的治疗方案listview
    private CustomExpandableListView bottomListview;//未做基因者可尝试的listview
    private ResultAdapter topAdapter;
    private ResultAdapter bottomAdapter;
    private Button confirm;//底部的按钮
    private boolean isCancer;//判断试普通决策树（false） 还是 肿瘤决策树（true）


    private void initData() {
        topData = new ArrayList<>();
        bottomData = new ArrayList<>();
        topAdapter = new ResultAdapter(this, topData, isCancer);
        topAdapter.setCheckBoxCheckListener(this);
        topListview.setAdapter(topAdapter);

        bottomAdapter = new ResultAdapter(this, bottomData, isCancer);
        bottomAdapter.setCheckBoxCheckListener(this);
        bottomListview.setAdapter(bottomAdapter);
        if (isCancer) {
//            new GetResultPresenter(this).getData();
            PolicyType = "1";
            bindView(mCancerResuletBean);
        } else {
            PolicyType = "2";
//            new GetCommDetailPresenter(this).getData();
            bindView(mCommBean);
        }
    }

    private CommBean mCommBean;//有这个说明是普通决策树
    private CancerResuletBean mCancerResuletBean;
    private boolean isEffect = false;//false表示 不是从副作用进来的 true表示从副作用进来的
    private TextView tv_pos_cure;//可能的治疗方案文本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer_development);
        initTitlebar(getString(R.string.result_detail));
        Intent inetent = getIntent();
//        CureConfID = inetent.getStringExtra(Contants.CureConfID);
        PerformID = inetent.getStringExtra(Contants.PerformID);
//        TemplateID = inetent.getStringExtra(Contants.TemplateID);
//        if(PerformID != null) {
//            isEffect = true;
//        }
//        isCancer = getIntent().getBooleanExtra(Contants.IS_CANCER,true);//
        mCommBean = (CommBean) inetent.getSerializableExtra(Contants.CommBean);
        mCancerResuletBean = (CancerResuletBean) inetent.getSerializableExtra(Contants.CancerResuletBean);
        if (mCommBean == null) {
            isCancer = true;
        }
        initView();
        initData();


        setListener();
    }

    private void setListener() {
        topListview.setOnGroupClickListener(this);
        topListview.setOnChildClickListener(this);
        bottomListview.setOnGroupClickListener(this);
        bottomListview.setOnChildClickListener(this);
        confirm.setOnClickListener(this);
    }

    private TextView cancel_decri;//头部描述的文本
    private TextView conclusion;//确诊条件
    private TextView check_gene;//是否进行基因检查
    private TextView type_name;//最顶部的文本
    private TextView confirm_condition;//文本确诊条件
    private TextView tv_tip;//文本确诊条件

    private void initView() {
        cancel_decri = (TextView) findViewById(R.id.cancel_decri);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_pos_cure = (TextView) findViewById(R.id.pos_cure);

        check_gene = (TextView) findViewById(R.id.check_gene);
        type_name = (TextView) findViewById(R.id.type_name);
        confirm_condition = (TextView) findViewById(R.id.confirm_condition);
        conclusion = (TextView) findViewById(R.id.conclusion);
        confirm = (Button) findViewById(R.id.confirm);
        topListview = (CustomExpandableListView) findViewById(R.id.top_listview);
        bottomListview = (CustomExpandableListView) findViewById(R.id.bottom_listview);
        bottomListview.setGroupIndicator(null);//设置没有 指示符
        topListview.setGroupIndicator(null);//设置没有 指示符
        if (isCancer) {
            List<CancerResuletBean.StepEntity> list = mCancerResuletBean.getStep();
//            for (CancerResuletBean.StepEntity entity : list) {
//                if (BXY_ID.equals(entity.getCureConfID())) {//上面没有靶向药就显示
//                    check_gene.setVisibility(View.GONE);
//                } else {
//                    check_gene.setVisibility(View.VISIBLE);
//                }
//            }


        } else {
            bottomListview.setVisibility(View.GONE);
            check_gene.setVisibility(View.GONE);
        }


    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//            case R.id.top_listview://顶部的listview可能的治疗方案
//                startActivity(new Intent(this, CaseDetailActivity.class));
//
//
//                break;
//            case R.id.bottom_listview://底部的listview 未做基因者可尝试
//
//
//                break;
//        }
//    }

//    private String checkName;

    private String StepID;//TemplateID
    private String TemplateID;//模板id

    private String CureConfID;
    private String PerformID;
    private String PolicyType;

    /**
     * 肿瘤决策树为空的情况
     */
    private void emptyCancerFragment() {
        EmptyResulDetalFragment fragment = new EmptyResulDetalFragment();
        fragment.setListener(new EmptyResulDetalFragment.OnFinishListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl, fragment);
        ft.commit();
    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 0) {//点击 进入方案详情 需要的templateid
            map.put(Contants.StepID, StepID);
            map.put(Contants.PolicyType, PolicyType);

            if (!TextUtils.isEmpty(TemplateID)) {
                map.put(Contants.TemplateID, TemplateID);
            }

            if (!TextUtils.isEmpty(CureConfID)) {
                map.put(Contants.CureConfID, CureConfID);
            }

            if (!TextUtils.isEmpty(PerformID)) {
                map.put(Contants.PerformID, PerformID);
            }
        }

        if (tag == NetNumber.SET_PLAN_MEDICINE) {//添加到计划用药
            map.put(Contants.PlanStepID, PlanStepID);
        }
//        if (tag == 3) {//普通决策树
//            if(isEffect) {
//                map.put(Contants.CureConfID, CureConfID);
//                map.put(Contants.PerformID, PerformID);
//            }
//        }
        LogUtil.i(TAG, map.toString());
        return map;
    }

    private List<CancerResuletBean.StepEntity> topData;
    private List<CancerResuletBean.StepEntity> bottomData;

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, t.toString());
        if (tag == 0) {//点击某项 请求数据
            CaseDetailBean bean = (CaseDetailBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                if (bean == null) {
                    LogUtil.i(TAG, "bean is null");
                    return;
                }
                bean.setStepId(StepID);
                startActivity(new Intent(this, CaseDetailActivity.class).putExtra(Contants.CaseDetailBean, bean));
            }
        }


        if (tag == NetNumber.SET_PLAN_MEDICINE) {//添加到计划用药
            BaseBean bean = (BaseBean) t;
            if (Contants.RESULT.equals(bean.iResult)) {
                showToast("添加成功");
                UserinfoData.planId = PlanStepID;
            }
        }
//        if (tag == 3) {//普通决策树
//            CommBean bean = (CommBean) t;
//            bindView(bean);
//        }
    }

    private void bindView(CommBean bean) {
        confirm.setVisibility(View.GONE);
        findViewById(R.id.line1).setVisibility(View.GONE);
        findViewById(R.id.line).setVisibility(View.GONE);
        findViewById(R.id.line2).setVisibility(View.GONE);

        tv_tip.setVisibility(View.GONE);

        String describ = bean.getSummary();
        cancel_decri.setText(describ);

        confirm_condition.setVisibility(View.GONE);
        conclusion.setVisibility(View.GONE);


        String CommonPolicyTypeName = bean.getCommonPolicyTypeName();
        type_name.setText(CommonPolicyTypeName);

        List<CancerResuletBean.StepEntity> list = bean.getSolution();
        if (list != null && list.size() > 0) {
            topData.addAll(list);
            topAdapter.update(topData);
        } else {
            tv_pos_cure.setText(R.string.no_result_cure);
            if (TextUtils.isEmpty(describ) && TextUtils.isEmpty(CommonPolicyTypeName)) {
                findViewById(R.id.whole_content).setVisibility(View.GONE);
                emptyCommnFragment(-1, null, null, null);
            }
        }
    }


    private void bindView(CancerResuletBean bean) {
        String conclusiong = bean.getConclusion();
        String summary = bean.getSummary();
        conclusion.setText(conclusiong);
//        if(!TextUtils.isEmpty(summary)){
            cancel_decri.setText(summary);
//        }
        List<CancerResuletBean.StepEntity> list = bean.getStep();
        if (list != null && list.size() > 0) {
            for (CancerResuletBean.StepEntity item : list) {
                if ("0".equals(item.getIsTry())) {//非试药
                    topData.add(item);
                } else {
                    bottomData.add(item);
                }
            }
            if (isHasBXY(topData)) {
                check_gene.setVisibility(View.VISIBLE);
            } else {
                check_gene.setVisibility(View.GONE);
            }
            topAdapter.update(topData);
            if (bottomData.size() > 0) {
                tv_tip.setVisibility(View.VISIBLE);
            } else {
                tv_tip.setVisibility(View.GONE);
            }
            bottomAdapter.update(bottomData);
            confirm.setVisibility(View.VISIBLE);
        } else {
            //改成未找到治疗方案
            tv_pos_cure.setText("未找到治疗方案");
            if (TextUtils.isEmpty(conclusiong) && TextUtils.isEmpty(summary)) {
                confirm.setVisibility(View.GONE);
                findViewById(R.id.whole_content).setVisibility(View.GONE);
                emptyCancerFragment();
            }


        }

    }

    private CustomProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        mProgressDialog = CustomProgressDialog.createCustomDialog(this);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private String PlanStepID;

    /**
     * 列表选择后 这个回调会调用
     *
     * @param adapter
     * @param groupPosition
     * @param childPosition
     * @param item
     */
    @Override
    public void checkItem(BaseExpandableListAdapter adapter, int groupPosition, int childPosition, String item) {
        LogUtil.i(TAG, "groupPosition:" + groupPosition + "childPosition:" + childPosition + "item :" + item);
        if (TextUtils.isEmpty(item)) {//这个说明他是取消了checkbox的选择
            confirm.setText("请选择方案");
            return;
        }
        PlanStepID = item;
        //判断试哪一个的listview
        confirm.setText("添加到计划用药");
        if (adapter == topAdapter && topAdapter != null) {
            bottomAdapter.setSelectChildPosition();//清除这个项目的选中效果
        }

        if (adapter == bottomAdapter && bottomAdapter != null) {
            topAdapter.setSelectChildPosition();//清除这个项目的选中效果
        }


    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        LogUtil.i(TAG, "onGroupClick");
        CancerResuletBean.StepEntity group = null;
        if (parent == topListview) {
            group = topAdapter.getGroup(groupPosition);
        }

        if (parent == bottomListview) {
            group = bottomAdapter.getGroup(groupPosition);
        }

        if (!TextUtils.isEmpty(group.getCombineid())) {//说明他有展开项 说明他有组合用药
            if (parent == topListview) {
                topListview.setSelectedGroup(groupPosition);
            }
            if (parent == bottomListview) {

                bottomListview.setSelectedGroup(groupPosition);
            }
            return false;
        }
        String cureconfid = group.getCureConfID();
        if (!TextUtils.isEmpty(cureconfid)) {
            CureConfID = cureconfid;
        }

        StepID = group.getStepid();
        TemplateID = group.getTemplateid();
        new GetSolutionDetailPresenter(this).getData();

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        LogUtil.i(TAG, "onChildClick");

        CancerResuletBean.StepEntity.CombineStepidEntity child = null;
        if (parent == topListview) {
            child = topAdapter.getChild(groupPosition, childPosition);
        }
        if (parent == bottomListview) {
            child = bottomAdapter.getChild(groupPosition, childPosition);
        }
        TemplateID = child.getTemplateid();

        StepID = child.getStepid();
        new GetSolutionDetailPresenter(this).getData();
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm: {
                if (TextUtils.isEmpty(PlanStepID)) {
                    Toast.makeText(this, "请选择方案", Toast.LENGTH_SHORT).show();
                    return;
                }

                new SetPlanMedicinePresenter(this).getData();

                break;
            }
        }
    }

    /**
     * 上面有靶向药就不显示
     *
     * @param topData
     * @return true 显示 false 不显示
     */
    private boolean isHasBXY(List<CancerResuletBean.StepEntity> topData) {
        for (CancerResuletBean.StepEntity entity : topData) {
            String stepid = entity.getStepid();
            if (!TextUtils.isEmpty(stepid)) {
                String cureConfID = MapDataUtils.getAllCureconfID(stepid);
                LogUtil.i(TAG, stepid);
                if (BXY_ID.equals(cureConfID)) {
                    return false;
                }
            }
            List<CancerResuletBean.StepEntity.CombineStepidEntity> list = entity.getCombineStepid();
            if (list != null && list.size() > 0) {
                for (CancerResuletBean.StepEntity.CombineStepidEntity entity1 : list) {
                    String stepid2 = entity1.getStepid();
                    if (!TextUtils.isEmpty(stepid2)) {
                        String cureConfID = MapDataUtils.getAllCureconfID(stepid2);
                        LogUtil.i(TAG, cureConfID);
                        if (BXY_ID.equals(cureConfID)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    public void getStepId() {


    }
}
