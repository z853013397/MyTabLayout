package com.zeyuan.kyq.fragment.patientinfo;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.widget.DoubleTvLayout;

import java.util.Calendar;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-16
 * Time: 18:32
 * FIXME
 * 建立档案的最后一步
 */


public class PatientResultFragment extends PatientInfoFragment implements View.OnClickListener, DialogFragmentListener {
    private static final String TAG = "PatientResultFragment";
    private DoubleTvLayout current_case;//当前治疗方案
    private DoubleTvLayout case_time;//方案开始时间
    private DoubleTvLayout is_effect;//方案是否有效

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_result, container, false);
        initView();
        setLisenter();
        return rootView;
    }

    private void setLisenter() {
        current_case.setOnClickListener(this);
        case_time.setOnClickListener(this);
        is_effect.setOnClickListener(this);
        next_step1.setOnClickListener(this);
    }

    private Button next_step1;

    private void initView() {
        initTitle();
        current_case = (DoubleTvLayout) findViewById(R.id.current_case);
        case_time = (DoubleTvLayout) findViewById(R.id.case_time);
        is_effect = (DoubleTvLayout) findViewById(R.id.is_effect);
        next_step1 = (Button) findViewById(R.id.next_step1);

    }

    private void initTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("患者信息");
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setVisibility(View.GONE);
        TextView text_back = (TextView) findViewById(R.id.text_back);
        text_back.setVisibility(View.VISIBLE);
        text_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_case: {
                CureTypeDialog dilog = new CureTypeDialog();
                dilog.setDrugsNameListener(this);
                dilog.show(getFragmentManager(), "dialog");

                break;
            }
            case R.id.case_time: {
                showTimePicker();

                break;
            }
            case R.id.is_effect: {
                showEffectDialog();

                break;
            }
            case R.id.text_back: {
                if (onLastStepClickListener != null) {
                    onLastStepClickListener.onLastStepClickListener(this);
                }
                break;
            }
            case R.id.next_step1: {
                if (TextUtils.isEmpty(StepID)) {
                    Toast.makeText(getActivity(), "请输入当前治疗方案", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(IsMedicineValid)) {
                    setIsMedicineValid(IsMedicineValid);
                }
                if (!TextUtils.isEmpty(CureStartTime)) {
                    setCureStartTime(CureStartTime);
                }
                setStepID(StepID);
                SharePrefUtil.saveString(context, Contants.StepID, StepID);
                if (onNextStepClickListener != null) {
                    onNextStepClickListener.onNextStepClickListener(this);
                    next_step1.setClickable(false);
                }
                break;
            }
        }
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DATE);
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                StringBuilder pickTime = new StringBuilder().append(year).append("-")
                        .append((monthOfYear) < 10 ? "0" + (monthOfYear) : (monthOfYear))
                        .append("-")
                        .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth);
                String picktimes = new String(pickTime);
                case_time.setRightTxt(picktimes);
                String loadTime = DataUtils.showTimeToLoadTime(picktimes);
                CureStartTime = loadTime;
//                if (context instanceof PatientInfoActivity) {
//                    ((PatientInfoActivity) context).setDiscoverTime(loadTime);//后台没有加上方案开始时间
//                }
            }
        }, year, month, day).show();
    }

    private void showEffectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("方案是有有效");
        builder.setPositiveButton("有效", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                is_effect.setRightTxt("有效");
                IsMedicineValid = "1";
            }
        });
        builder.setNegativeButton("无效", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                is_effect.setRightTxt("无效");
                IsMedicineValid = "0";
            }
        });
        builder.create().show();
    }

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        LogUtil.i(TAG, data);
        String showData = MapDataUtils.getCureValues(data);
        current_case.setRightTxt(showData);
        StepID = data;
    }

    public void getResume() {
        if (next_step1 != null) {
            next_step1.setClickable(true);
        }
    }

    private String CureStartTime;

    public void setCureStartTime(String cureStartTime) {
        getPatientInfoActivity().setCureStartTime(cureStartTime);
    }

    private String StepID;

    public void setStepID(String stepID) {
        getPatientInfoActivity().setStepID(stepID);
    }

    private String IsMedicineValid;

    public void setIsMedicineValid(String isMedicineValid) {
        getPatientInfoActivity().setIsMedicineValid(isMedicineValid);
    }

}
