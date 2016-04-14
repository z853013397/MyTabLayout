package com.zeyuan.kyq.view;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.fragment.dialog.ChooseMedicaDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.presenter.AddSymptomPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.utils.UserinfoData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/8.
 * 添加症状
 */
public class RecordSymptomActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, DialogFragmentListener {
    private static final String TAG = "AddSymptomActivity";
    private RelativeLayout show_time;
    private RelativeLayout choose_symptom;
    private EditText remark;
    private TextView symptom_time;//这个是时间
    private TextView righttv;//标题右边的保存
    private AddSymptomPresenter addSymptomPresenter = new AddSymptomPresenter(this);
    private TextView symptom;//这个是症状的textview
    private boolean isEdit = false;//true 说明是 编辑症状 false 表示新增
    private UserStepChildBean mUserStepChildBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);
        initTitlebar(getString(R.string.record_symptom));
        mUserStepChildBean = (UserStepChildBean) getIntent().getSerializableExtra(Contants.StepCfg.EDIT_SYMPTOM_TAG);
        if (mUserStepChildBean != null) {
            isEdit = true;
        }
        initView();

        UserStepHelper.reqUserStepAll(this);//防止直接添加症状时阶段数据为空
    }

    private String times;
    private Button save;

    private void initView() {
        remark = (EditText) findViewById(R.id.remark);
        symptom = (TextView) findViewById(R.id.symptom);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        symptom_time = (TextView) findViewById(R.id.symptom_time);
        show_time = (RelativeLayout) findViewById(R.id.show_time);
        choose_symptom = (RelativeLayout) findViewById(R.id.choose_symptom);
        choose_symptom.setOnClickListener(this);
        show_time.setOnClickListener(this);
        if (isEdit) {
//            show_time.setEnabled(false);
            String performIds = mUserStepChildBean.getPerformStep();
            String performValues = MapDataUtils.getPerfromValues(performIds);
            if (!TextUtils.isEmpty(performValues)) {
                symptom.setText(performValues);
            }

            //String time = "" + mUserStepChildBean.getRecordTime();
            dateTimeNew = DateTime.from(mUserStepChildBean.getRecordTime() * 1000);
            //time = DataUtils.getDate(time);
//            if (!TextUtils.isEmpty(time)) {
            symptom_time.setText(dateTimeNew.toDateString());
//            }

//            String strRemarks = mUserStepChildBean.getRemark();
//            if (!TextUtils.isEmpty(strRemarks)) {
            remark.setText(mUserStepChildBean.getRemark());
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_time:
                if (isEdit) {
                    showToast("时间不能修改哦");
                    break;
                }
                showTimePicker();
                break;
            case R.id.save:
                saveSymptom();
                break;
            case R.id.choose_symptom:
//                ChooseMedicaDialog dialog = new ChooseMedicaDialog();
                showAddSypDialog();
                break;
        }
    }

    private void showAddSypDialog() {
        String cancerid = UserinfoData.getCancerID(this);
        LinkedHashMap<String, List<String>> performData = MapDataUtils.getBodyPos();//这个填上对应癌症
        ChooseMedicaDialog dialog = new ChooseMedicaDialog(performData);
        dialog.setDrugsNameListener(this);
        dialog.show(getFragmentManager(), Contants.MEDICA_DIALOG);
    }

    private DateTime dateTimeNew;

    private void showTimePicker() {
        DateTime dateTime = DateTime.now();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTime dateTimeNewTemp = DateTime.from(year, monthOfYear, dayOfMonth);
                if (dateTimeNewTemp.isRealPastTime()) {
                    dateTimeNew = dateTimeNewTemp;
                    symptom_time.setText(dateTimeNew.toDateString());
                } else {
                    showToast("不能选择未来日期");
                }
            }
        }, dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).show();
    }

    //    private String drugs;
//    private String time;
    private String strRemark;

    /**
     * 保存症状
     */
    private void saveSymptom() {
        //这儿判断下有些数据是否为空
//        drugs = symptom.getText().toString().trim();
//        time = symptom_time.getText().toString().trim();
//        if (TextUtils.isEmpty(drugs)) {
//            showToast("症状不能为空");
//            return;
//        }


        if (dateTimeNew == null) {
            showToast("时间不能为空");
            return;
        }
//        if (isEdit) {
        strRemark = remark.getText().toString();
        if (TextUtils.isEmpty(mCurrdata) && TextUtils.isEmpty(strRemark)) {
            showToast("症状和备注至少填一项");
            return;
        }
        commitData();
//        } else {
//            reqAddSymptom();
//        }
    }

    private void commitData() {
        UserStepChildBean bean = new UserStepChildBean();
        {
            bean.setPerformORQuota(UserStepChildBean.PERFORM_OR_QUOTA__SYMPT);
//            if(dateTimeNew == null) {
//                bean.setRecordTime(mUserStepChildBean.getRecordTime());
//            } else {
            bean.setRecordTime(dateTimeNew.toTimeSeconds());
//            }
            bean.setRemark(strRemark);
//            LogUtil.i(TAG, mUserStepChildBean.getStepDetailPerformID());
        }
        bean.setPerformStep(mCurrdata);

        if (isEdit) {
            if (TextUtils.isEmpty(mCurrdata)) {//这个说明是编辑 确没有修改
                bean.setPerformStep(mUserStepChildBean.getPerformStep());
            }
            bean.setStepDetailPerformID(mUserStepChildBean.getStepDetailPerformID());
            UserStepHelper.reqUserSymptMdf(this, mUserStepChildBean.getStepUid(), bean);
        } else {
            UserStepHelper.reqUserSymptAdd(this, bean);

        }
    }

//    private void reqAddSymptom() {
//        UserStepChildBean bean = new UserStepChildBean();
//        {
//            bean.setPerformORQuota(UserStepChildBean.PERFORM_OR_QUOTA__SYMPT);
//            bean.setRecordTime(dateTimeNew.toTimeSeconds());
//            bean.setPerformStep(mCurrdata);
//            bean.setRemark(remark.getText().toString());
//        }
//        UserStepHelper.reqUserSymptAdd(this, bean);
//    }


    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

//    @Override
//    public void getDrugsName(List list, int id) {
////        symptom.setText(name);
//
//        LogUtil.d(TAG,"选择症状是：" + list.toString());
//    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
//        if (tag == 0) {
//            map.put("InfoID", "15");
//            map.put("StepUID", "2");//是哪个步骤
//            map.put("strremark", "3");
//            map.put("RecordTime", "2,3");//药物的id 中间用逗号隔开
//            map.put("RecordNnum", "2015-1-2");
//            map.put("performStep0", "2015-2-2");
//        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        switch (position) {
            case UserStepHelper.ReqCode_UserSymptAdd:
                Toast.makeText(RecordSymptomActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case UserStepHelper.ReqCode_UserSymptMdf:
                Toast.makeText(RecordSymptomActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }

    private String mCurrdata;

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
//        String showData = DataUtils.loadStringToShowString(data);
        mCurrdata = data;
        StringBuilder stb = new StringBuilder();

        String[] ids = data.split(",");
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (i != 0) {
                    stb.append(",");
                }
                stb.append(GlobalData.performValues.get(ids[i]));
            }
        }
        symptom.setText(stb.toString());
        LogUtil.i(TAG, "症状对应的数据传过来的是：" + data);
    }
}
