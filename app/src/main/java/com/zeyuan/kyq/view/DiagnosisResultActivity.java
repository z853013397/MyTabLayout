package com.zeyuan.kyq.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.DiagnosisResultAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CommBean;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.presenter.ConfirmPerformPresenter;
import com.zeyuan.kyq.presenter.ConfirmSecondPresenter;
import com.zeyuan.kyq.presenter.GetCommDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomProgressDialog;
import com.zeyuan.kyq.widget.FlowLayout;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/29.
 * 诊断结果
 */
public class DiagnosisResultActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, ViewDataListener {
    private static final String TAG = "DiagnosisResultActivity";
    private MyListView listView;//诊断结果
    //    private MyListView bottomlistView;//底部的相似案例listview
    //    private MyGridView mMyGridView;//伴随症状
    private Button confirm;//伴随症状的确认
    private List<FindSymtomBean.CommPolicyEntity> result;//诊断结果
    private DiagnosisResultAdapter adapter;//诊断结果的adapter
    private LinearLayout ll;//隐藏的伴随症状layout
    private List<String> bsZy;//伴随症状的数据
    private FlowLayout mFlowLayout;
    private List<String> chooseItem;
    private String PerformID;
    private TextView symptom_title;//显示选择的主症状
    private TextView bs_symptom;//显示伴随症状的文本 展开和收起
    private ImageView img;
    private Button confirm_systom;//确认有改症状的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_result);
        initTitlebar(getString(R.string.diagnosis_result));
        initData();
        initView();
    }


    private void initData() {
        maybe_desc = (TextView) findViewById(R.id.maybe_desc);
        result = new ArrayList<>();
        chooseItem = new ArrayList<>();
        bsZy = new ArrayList<>();
        Intent intent = getIntent();
        FindSymtomBean bean = (FindSymtomBean) intent.getSerializableExtra(Contants.FindSymtomBean);
        PerformID = intent.getStringExtra(Contants.PerformID);
        List temp = bean.getCommPolicy();

        if (temp == null || temp.isEmpty()) {//这儿显示为空
            emptyCommnFragment(R.mipmap.no_result, getString(R.string.empty_dignose1), "", "");
            findViewById(R.id.content).setVisibility(View.GONE);
            return;
        }


        String hasCancer = bean.getIsHaveCancerProcess();
        if ("1".equals(hasCancer)) {//说明有肿瘤进展
            FindSymtomBean.CommPolicyEntity cancerPos = new FindSymtomBean.CommPolicyEntity();
            cancerPos.setCommPolicyName("肿瘤进展");
            result.add(cancerPos);
        }
        List<FindSymtomBean.CommPolicyEntity> list = bean.getCommPolicy();
        if (list != null && list.size() > 0) {
            maybe_desc.setVisibility(View.VISIBLE);
            result.addAll(list);
            for (FindSymtomBean.CommPolicyEntity item : list) {//遍历每个结果
                List<String> bsSymptom = item.getWithPerform();//拿出每个结果的伴随症状
                for (String str : bsSymptom) {
                    if (!bsZy.contains(str)) {
                        bsZy.add(str);
                    }
                }
            }
        } else {//没有结果
            if (!"1".equals(hasCancer)) {
                confirm_systom.setVisibility(View.GONE);
                findViewById(R.id.whole_content).setVisibility(View.GONE);
                emptyCommnFragment(-1, null, null, null);
            }
        }

    }


    private void openImg() {
        ObjectAnimator.ofFloat(img, "rotation", 90, 0).setDuration(200).start();
    }

    private void closeImg() {
        ObjectAnimator.ofFloat(img, "rotation", 0, 90).setDuration(200).start();
    }


    private TextView maybe_desc;

    private void initView() {
        mFlowLayout = (FlowLayout) findViewById(R.id.fls);

        symptom_title = (TextView) findViewById(R.id.symptom_title);
        bs_symptom = (TextView) findViewById(R.id.bs_symptom);
        bs_symptom.setClickable(false);
        img = (ImageView) findViewById(R.id.img);
        img.setRotation(90);
        ll = (LinearLayout) findViewById(R.id.ll);
        listView = (MyListView) findViewById(R.id.listview);
//        bottomlistView = (MyListView) findViewById(R.id.bottom_listview);
        confirm = (Button) findViewById(R.id.confirm);
        confirm_systom = (Button) findViewById(R.id.confirm_systom);

        adapter = new DiagnosisResultAdapter(this, result);
        listView.setAdapter(adapter);
        addBsSymptom();
        initSymptomTitle();
        bs_symptom.setOnClickListener(this);
//        bottomlistView.setOnItemClickListener(this);
        listView.setOnItemClickListener(this);
        confirm.setOnClickListener(this);
        confirm_systom.setOnClickListener(this);
        if (bsZy != null && bsZy.size() == 0) {//说命没有伴随症状
            setBZSZGone();
            img.setVisibility(View.GONE);
        }
    }

    private void addBsSymptom() {
        for (final String item : bsZy) {//伴随症状
            CheckBox checkBox = (CheckBox) LayoutInflater.from(this).inflate(R.layout.checkbox, null);
//            CheckBox checkBox = (CheckBox) v;
            checkBox.setText(GlobalData.performValues.get(item));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        chooseItem.add(item);
                    } else {
                        if (chooseItem.contains(item)) {
                            chooseItem.remove(item);
                        }
                    }
                }
            });
            mFlowLayout.addView(checkBox);
        }
    }

    private void initSymptomTitle() {
        String stringFormat = getResources().getString(R.string.dr_co_tips);
        String content = String.format(stringFormat, GlobalData.performValues.get(PerformID));
        symptom_title.setText(content);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.listview:
                clickTopListView(position);
                break;

            case R.id.bottom_listview:
                clickForumDetail();
                break;

        }
    }

    private void clickForumDetail() {
        startActivity(new Intent(this, ForumDetailActivity.class));
    }

    private void clickTopListView(int position) {
        String str = adapter.getItem(position);
        LogUtil.i(TAG, "点击的诊断结果是：" + str);
        if ("肿瘤进展".equals(str)) {
            new ConfirmSecondPresenter(this).getData();
        } else {
            CommPolicyID = result.get(position).getCommPolicyID();
            new GetCommDetailPresenter(this).getData();
        }

    }

    private List<String> data;

    /**
     * 没有伴随症状 隐藏ui
     */
    private void setBZSZGone() {
        ll.setVisibility(View.GONE);
        bs_symptom.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm: {//界面确定的点击事件
                openImg();
                ll.setVisibility(View.GONE);
//                bs_symptom.setClickable(true);
//                data = mFollowSymptomAdapter.getChooseList();//将result排序
                if (chooseItem != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < chooseItem.size(); i++) {
                        if (i == 0) {
                            sb.append(GlobalData.performValues.get(chooseItem.get(i)));
                            continue;
                        }
                        sb.append("," + GlobalData.performValues.get(chooseItem.get(i)));
                    }

                    bs_symptom.setText("是否伴随以下症状：" + sb);
                }
                break;
            }
            case R.id.bs_symptom: {

                if (View.VISIBLE == ll.getVisibility()) {
                    openImg();
                    ll.setVisibility(View.GONE);

                } else {
                    closeImg();
                    ll.setVisibility(View.VISIBLE);
                }
                break;
            }

            case R.id.confirm_systom: {
                confirmPerform();
                break;
            }
        }
    }


    private int compareList(List<String> list1, List<String> list2) {
        int temp = 0;
        for (String str1 : list1) {
            for (String str2 : list2) {
                if (str1.equals(str2)) {
                    temp++;
                }
            }
        }
        return temp;
    }

    private void confirmPerform() {
        new ConfirmPerformPresenter(this).getData();
    }

    private String CancerID = UserinfoData.getCancerID(this);//癌症的id
    private String StepID = UserinfoData.getStepID(this);//当前阶段ID
    private String PeriodType = "1";//分期类型，表示数字分期还是TNM分期  数字分期PeriodType为1，TNM分期PeriodType为2
    private String PeriodID = UserinfoData.getPeriodID(this);

    private String CommPolicyID;
    private String Type = "1";//通过查症状进入的副作用

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == NetNumber.CONFIM_SECOND) {//完善资料2的页面数据
            map.put(Contants.StepID, StepID);
            map.put(Contants.CancerID, CancerID);
            map.put(Contants.PeriodID, PeriodID);
            map.put(Contants.PeriodType, PeriodType);
        }

        if (tag == NetNumber.CONFIRM_SYMPTOM) {//确认有该症状
            map.put(Contants.ConfirmPerformID, PerformID);
        }


        if (tag == 3) {//普通决策树

            map.put(Contants.CommPolicyID, CommPolicyID);//普通决策树的id，查症状进 结果详情
            map.put(Contants.Type, Type);

        }
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        if (tag == NetNumber.CONFIM_SECOND) {
            WSZLBean bean = (WSZLBean) t;
            startActivity(new Intent(this, WSZLActivity.class).putExtra(Contants.WSZLBean, bean));              //这儿跳转到完善资料
        }
        if (tag == NetNumber.CONFIRM_SYMPTOM) {
            BaseBean bean = (BaseBean) t;
            if ("0".equals(bean.iResult)) {
                showToast("添加成功");
                UserinfoData.performid = PerformID;
                confirm_systom.setVisibility(View.GONE);
            }

        }
        if (tag == 3) {//普通决策树
            CommBean bean = (CommBean) t;
            startActivity(new Intent(this, ResultDetailActivity.class).putExtra(Contants.IS_CANCER, false)
                            .putExtra(Contants.CommBean, bean)
            );


        }
    }

    private CustomProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
//        if (tag == 3) {//普通决策树
        mProgressDialog = CustomProgressDialog.createCustomDialog(this);
        mProgressDialog.show();
//        }
    }

    @Override
    public void hideLoading(int tag) {
//        if (tag == 3) {//普通决策树
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
//        }
    }

    @Override
    public void showError(int tag) {
//        if (tag == 3) {//普通决策树
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
//        }
    }
}
