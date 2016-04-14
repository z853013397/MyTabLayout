package com.zeyuan.kyq.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.fragment.dialog.CancerTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.presenter.UpdatePatientDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.EditStepActivity;
import com.zeyuan.kyq.view.FindSymtomActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.List;
import java.util.Map;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-21
 * Time: 18:06
 * 没有癌肿  和 阶段完善资料1
 * FIXME
 */


public class AddZLFragment extends BaseFragment implements View.OnClickListener, DialogFragmentListener, ViewDataListener {
    private static final String TAG = "AddZLFragment";
//    private String cancerID;
//    private String stepID;
//    private String PeriodID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_zl, container, false);
        initView();
        setListener();
        return rootView;
    }

    private void setListener() {
    }

    private Button save;
    private TextView cancer_type;//癌症种类
    private TextView cure_case;//当前治疗方案
    private TextView period_start;//t分期
    private TextView linba;//n分期
    private TextView far_trsnsfo_case;//m分期
    private TextView digit;//分期

    private LinearLayout ll;//tnm分期所在的layout


    private void initView() {

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("完善资料");
        findViewById(R.id.btn_back).setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        cancer_type = (TextView) findViewById(R.id.cancer_type);
        ll = (LinearLayout) findViewById(R.id.ll);
        period_start = (TextView) findViewById(R.id.period_start);
        linba = (TextView) findViewById(R.id.linba);
        far_trsnsfo_case = (TextView) findViewById(R.id.far_trsnsfo_case);
        digit = (TextView) findViewById(R.id.digit);
        digit.setOnClickListener(this);

        period_start.setOnClickListener(this);
        linba.setOnClickListener(this);
        far_trsnsfo_case.setOnClickListener(this);


        cure_case = (TextView) findViewById(R.id.cure_case);
        save.setOnClickListener(this);
        cancer_type.setOnClickListener(this);
        cure_case.setOnClickListener(this);
    }

    private String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    private void setText(TextView textView, String content) {
        textView.setText(content);
    }

    private boolean flags = true;//ture表示显示数字分期 false 表示 打开tnm分期

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {//调用主页面 隐藏这个界面
                getActivityData().removeZL();
                break;
            }
            case R.id.save: {
                if (TextUtils.isEmpty(cancerID) || Contants.NO_DATA.equals(cancerID)) {
                    Toast.makeText(context, getString(R.string.choose_cancer_type), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(stepID) || Contants.NO_DATA.equals(stepID)) {
                    Toast.makeText(context, getString(R.string.choose_cure_case), Toast.LENGTH_SHORT).show();
                    return;
                }

//                digitID = UserinfoData.getPeriodID(context);
//                if (TextUtils.isEmpty(digitID) || Contants.NO_DATA.equals(digitID)) {
//                    Toast.makeText(context, getString(R.string.choose_digit), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                getActivityData().removeZL();


                String oldDigitId = UserinfoData.getPeriodID(context);
                String oldCancerId = UserinfoData.getCancerID(context);
                if (!oldDigitId.equals(digitID) || !oldCancerId.equals(cancerID)) {// 如果修改了分期 或者修改了癌肿 都要更新患者详情
                    bean = new PatientDetailBean();
                    if (!oldCancerId.equals(cancerID)) { //
//                        UserinfoData.saveCancerID(context,cancerID);
                        bean.setCancerID(cancerID);
                        setCancerId(cancerID);
                    }
                    bean.setPeriodID(digitID);
                    setPeriodID(digitID);
                    UserinfoData.savePeriodID(context, digitID);
                    bean.setPeriodType("1");
                    new UpdatePatientDetailPresenter(bean, this).getData();
                }

                getActivityData().findSymtom();

                break;
            }
            case R.id.cancer_type: {//癌症种类
                showCancerType();
                break;
            }
            case R.id.cure_case: {//当前治疗方式
                showChooseMedicaDialog();
                break;
            }
            case R.id.digit: {//请选择分期
                if (TextUtils.isEmpty(cancerID)) {
                    Toast.makeText(context, "请先填写癌肿", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (flags) {
                    showDigitDataDialog();
                } else {
                    openTNM();
                }
                break;
            }
            case R.id.period_start: {////原发肿瘤情况  T
                showDigitTDialog();
                break;
            }
            case R.id.far_trsnsfo_case: {//远处转移情况  M
                showDigitMDialog();
                break;
            }

            case R.id.linba: {//区域淋巴结情况 N
                showDigitNDialog();
                break;
            }
        }
    }

    private void showChooseMedicaDialog() {
        Intent intent = new Intent(context, EditStepActivity.class);
        intent.putExtra(Contants.CancerID, cancerID);
        startActivity(intent);
    }

    private void showCancerType() {
        CancerTypeDialog dialog = new CancerTypeDialog();
        dialog.setOnCancerTyperListener(this);
        dialog.show(getFragmentManager(), Contants.CANCER_DIALOG);

    }

    private String stepID;
    private String cancerID;
    private String digitID;


    @Override
    public void onResume() {
        super.onResume();
        stepID = UserinfoData.getStepID(context);
        if (cure_case != null && !TextUtils.isEmpty(stepID) && !Contants.NO_DATA.equals(stepID)) {
//            cure_case.setText(DataUtils.loadStringToShowString(stepID) + "(" + stepID + ")");
            cure_case.setText(DataUtils.loadStringToShowString(stepID));

        } else {
            cure_case.setText(R.string.no_data);
        }
        if (TextUtils.isEmpty(cancerID)) {
            cancerID = UserinfoData.getCancerID(context);
        }
        if (cancer_type != null && !TextUtils.isEmpty(cancerID)) {
//            cancer_type.setText(GlobalData.cancerValues.get(cancerID) + "(" + cancerID + ")");
            cancer_type.setText(GlobalData.cancerValues.get(cancerID));

        } else {
            cancer_type.setText(R.string.no_data);
        }
        if (TextUtils.isEmpty(digitID)) {
            digitID = UserinfoData.getPeriodID(context);
        }
        if (digit != null && !TextUtils.isEmpty(digitID)) {
//            digit.setText(MapDataUtils.getDigitValues(digitID) + "(" + digitID + ")");
            digit.setText(MapDataUtils.getDigitValues(digitID));

        } else {
            digit.setText(R.string.no_data);
        }

    }

    /**
     * 这个是显示数字分期
     */
    private void showDigitDataDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
    }


    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

//    private static final String digit_n = "digit_n";

    private void showDigitNDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
    }

    /**
     * M分期
     */
//    private static final String digit_m = "digit_m";
    private void showDigitMDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, cancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
    }


    private FindSymtomActivity getActivityData() {
        if (getActivity() instanceof FindSymtomActivity) {
            return (FindSymtomActivity) getActivity();
        }
        return null;
    }


    private void setCancerId(String cancerId) {
        getActivityData().setCancerID(cancerId);
    }


    private void setStepID(String StepID) {
        getActivityData().setStepID(StepID);
    }


    private void openTNM() {
        ll.setVisibility(View.VISIBLE);
    }

    private boolean isChangeCancerID(String cancerid) {
        if (cancerID.equals(cancerid)) {
            return false;
        }
        return true;
    }

//    private boolean isCancerChanged = false;

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment cancerType = fragmentManager.findFragmentByTag(Contants.CANCER_DIALOG);
        Fragment medicaDialog = fragmentManager.findFragmentByTag(Contants.MEDICA_DIALOG);

        Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
        Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
        Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
        Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);
        if (dialog == cancerType) {

//            if (isChangeCancerID(data)) {
            cancer_type.setText(GlobalData.cancerValues.get(data));//修改癌肿 把分期置为0.
            cancerID = data;
            digitID = "0";
            digit.setText("未知");
//                isCancerChanged = true;
//            }
//            UserinfoData.saveCancerID(context, data);
//            setCancerId(data);

        }
//        if (dialog == medicaDialog) {
//            cure_case.setText(DataUtils.loadStringToShowString(data));
//            SharePrefUtil.saveString(context,Contants.StepID,data);
//        }
        if (dialog == digitDataDialog) {//数字分期
            LogUtil.i(TAG, data);
            if (DigitDialog.SWITCH.equals(data)) {
                openTNM();
                flags = false;
                return;
            }
            String temp = GlobalData.DigitValues.get(data);
            digit.setText(temp);// 要现实保存按钮
            digitID = data;

        }

        if (dialog == digitTDialog) {
            LogUtil.i(TAG, "digitTDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
//            digitT = data;
//            String temp = ZYApplication.DigitValues.get(data);
            period_start.setText(getShow(data));
            tTemp = data;
            chooseTNMFinish();

        }

        if (dialog == digitNDialog) {
            LogUtil.i(TAG, "digitNDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
//            digitN = data;
//            String temp = ZYApplication.DigitValues.get(data);
            linba.setText(getShow(data));
            nTemp = data;
            chooseTNMFinish();

        }

        if (dialog == digitMDialog) {
            LogUtil.i(TAG, "digitMDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
//            digitM = data;
//            String temp = ZYApplication.DigitValues.get(data);
            far_trsnsfo_case.setText(getShow(data));
            mTemp = data;
            chooseTNMFinish();
        }
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     *
     * @param data
     * @return
     */
    private String getShow(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = GlobalData.DigitValues.get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    private void closeTNM() {
        ll.setVisibility(View.GONE);
    }

    private String tTemp;
    private String nTemp;
    private String mTemp;

    private PatientDetailBean bean;

    private void chooseTNMFinish() {
        if (!TextUtils.isEmpty(cancerID) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
            List<TNMObj> list = GlobalData.tnmObjs;
            int size = list.size();
            TNMObj tnmTmp = null;
            for (int i = 0; i < size; i++) {
                tnmTmp = list.get(i);
                if ((tnmTmp.getCancerId().equals(getCancerId())) &&
                        (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))

                        && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))

                        && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                    digitID = tnmTmp.getDigitId();
                    LogUtil.i(TAG, "digitid is :" + digitID);
                    String showdigitId = GlobalData.DigitValues.get(digitID);
//                    UserinfoData.savePeriodID(context, digitID);//保存digitid
                    digit.setText(showdigitId);
                    break;
                } else {
//                    UserinfoData.savePeriodID(context, "0");
                    digit.setText("未知");
                }
            }
        }
    }

    private String getCancerId() {
        return UserinfoData.getCancerID(context);
    }


    @Override
    public Map getParamInfo(int tag) {
        return null;
    }

    @Override
    public void toActivity(Object t, int tag) {
        UserinfoData.saveUserData(context, bean);
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


    private void setCancerID(String cancerID) {
        if (context instanceof FindSymtomActivity) {
            ((FindSymtomActivity) context).setCancerID(cancerID);
        }
    }


    private void setStepId(String cancerID) {
        if (context instanceof FindSymtomActivity) {
            ((FindSymtomActivity) context).setStepID(cancerID);
        }
    }


    private void setPeriodID(String cancerID) {
        if (context instanceof FindSymtomActivity) {
            ((FindSymtomActivity) context).setPeriodID(cancerID);
        }
    }
}
