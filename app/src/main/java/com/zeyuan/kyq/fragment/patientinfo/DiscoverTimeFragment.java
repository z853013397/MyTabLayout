package com.zeyuan.kyq.fragment.patientinfo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.widget.DoubleTvLayout;

import java.util.Calendar;

/**
 * 建立档案的选择患者确诊时间
 */
public class DiscoverTimeFragment extends PatientInfoFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location, container, false);
        initView();
        return rootView;
    }

    private TextView back, next_step;//下一步
    private TextView title;
    private DoubleTvLayout time;

    private void initView() {
        time = (DoubleTvLayout) findViewById(R.id.time);
        time.setOnClickListener(this);
//        time.setRightTxt(DataUtils.getDates());
        ImageView img = (ImageView) findViewById(R.id.btn_back);
        img.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.patient_info);
        back = (TextView) findViewById(R.id.text_back);
        back.setVisibility(View.VISIBLE);
        next_step = (Button) findViewById(R.id.next_step);
        back.setOnClickListener(this);
        next_step.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_back:
                if (onLastStepClickListener != null) {
                    onLastStepClickListener.onLastStepClickListener(this);
                }
                break;
            case R.id.next_step:
//                if(TextUtils.isEmpty(discoverTime)){
//                    Toast.makeText(getActivity(), "请选择确诊时间",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (onNextStepClickListener != null) {
                    if(!TextUtils.isEmpty(discoverTime)) {
                        setDiscoverTime(discoverTime);
                    }
                    next_step.setClickable(false);
                    onNextStepClickListener.onNextStepClickListener(this);
                }


                break;
            case R.id.time:
//                CityDialog dialog = new CityDialog();
//                dialog.setOnOnCitySelListener(this);
//                dialog.show(getActivity().getFragmentManager(), "city");

                showTimePicker();
                break;
            default:
                throw new RuntimeException();
        }
    }


    private String discoverTime;

//    @Override
//    public void getDataFromDialog(DialogFragment dialog, String data,int position) {
//        this.city = data;
//        time.setText(ZYApplication.values.get(data));
//    }


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

                        time.setRightTxt(picktimes);
                        String loadTime = DataUtils.showTimeToLoadTime(picktimes);
                        discoverTime = loadTime;
//                        if (context instanceof PatientInfoActivity) {
//                            getPatientInfoActivity().setDiscoverTime(loadTime);
//                        }
                    }
                }, year, month, day).show();
    }

    public void getResume() {
        if (next_step != null) {
            next_step.setClickable(true);
        }
    }

    private void setDiscoverTime(String time) {
        getPatientInfoActivity().setDiscoverTime(time);
    }
}
