package com.zeyuan.kyq.fragment.patientinfo;

import android.animation.LayoutTransition;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.DoubleTvLayout;

import java.util.List;

/**
 * 建立档案的最后三个fragment
 * 填写地址和分期的fragment
 */
public class LcGnFragment extends PatientInfoFragment implements View.OnClickListener, DialogFragmentListener {
    private static final String TAG = "LcGnFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_step, container, false);
        initView();
        setListener();
        return rootView;
    }

    LinearLayout ll;
    LinearLayout whole_content;

    private void setListener() {
        currentCase.setOnClickListener(this);
        time.setOnClickListener(this);
        isEffect.setOnClickListener(this);
        next_step2.setOnClickListener(this);
        next_step1.setOnClickListener(this);
        show_gene.setOnClickListener(this);
        location.setOnClickListener(this);
    }

    private TextView currentCase;//填写分期 第一个选择
    private TextView time;//填写分期 第二个选择
    private TextView isEffect;//填写分期 第三个选择
    private DoubleTvLayout location;//患者所在地
    private Button next_step1;//下一步
    private Button next_step2;//下一步
    private ImageView img;//显示的小医生头像
    private View need_dismiss;//需要消失和显示的整体组件
    private FrameLayout show_gene;//底部的显示分期
    private TextView tv_gene;
    private LinearLayout ll2;//分期的第二个条目
    private LinearLayout ll3;//分期的第三个条目
    private TextView first_digit;//第一个左边的文本
    private TextView digit_desc;//分期的说明文本

    private void initView() {
        initTitle();
        ll = (LinearLayout) findViewById(R.id.ll);
        whole_content = (LinearLayout) findViewById(R.id.whole_content);
        tv_gene = (TextView) findViewById(R.id.tv_gene);
        location = (DoubleTvLayout) findViewById(R.id.location);
//        location.setRightTxt(context.getString(R.string.beijing));
        show_gene = (FrameLayout) findViewById(R.id.show_gene);
        img = (ImageView) findViewById(R.id.img);

        next_step1 = (Button) findViewById(R.id.next_step1);
        need_dismiss = LayoutInflater.from(context).inflate(R.layout.view_lcgn, null);
        next_step2 = (Button) need_dismiss.findViewById(R.id.next_step2);
        digit_desc = (TextView) need_dismiss.findViewById(R.id.digit_id);
        String temp = getString(R.string.digit_desc_txt, "未知");
        digit_desc.setText(temp);
        ll2 = (LinearLayout) need_dismiss.findViewById(R.id.ll2);
        ll3 = (LinearLayout) need_dismiss.findViewById(R.id.ll3);
        first_digit = (TextView) need_dismiss.findViewById(R.id.first_digit);
        first_digit.setText("请选择分期");

        LayoutTransition mLayoutTransition = new LayoutTransition();
        ll.setLayoutTransition(mLayoutTransition);
        whole_content.setLayoutTransition(mLayoutTransition);
//        ll.removeView(need_dismiss);
        currentCase = (TextView) need_dismiss.findViewById(R.id.current_case);
        time = (TextView) need_dismiss.findViewById(R.id.time);
        isEffect = (TextView) need_dismiss.findViewById(R.id.is_effect);

    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("患者信息");
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setVisibility(View.GONE);
        TextView text_back = (TextView) findViewById(R.id.text_back);
        text_back.setVisibility(View.VISIBLE);
        text_back.setOnClickListener(this);
    }

    private boolean flag = true;
    private boolean flags = true;//ture表示显示数字分期 false 表示显示t分期

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_back:
                if (onLastStepClickListener != null) {
                    onLastStepClickListener.onLastStepClickListener(this);
                }
                break;
            case R.id.next_step1:
            case R.id.next_step2:
                if (!TextUtils.isEmpty(PeriodID) && !TextUtils.isEmpty(PeriodType)) {
//                    Toast.makeText(context,"请选择分期",Toast.LENGTH_SHORT).show();
//                    return;
                    setPeriodType(PeriodType);
                    setPeriodID(PeriodID);
                }
                if (onNextStepClickListener != null) {
                    onNextStepClickListener.onNextStepClickListener(this);
                    next_step1.setClickable(false);
                    next_step2.setClickable(false);
                }
                break;

            case R.id.time://区域淋巴结 第二个
                showDigitNDialog();
                break;
            case R.id.is_effect://远处转移 第三个
                showDigitMDialog();
                break;

            case R.id.current_case: {//第一个
                if (flags) {
                    showDigitDataDialog();
                } else {
                    showDigitTDialog();
                }
                break;
            }
            case R.id.show_gene: {//填写分期的点击
                if (flag) {
                    openGene();
                    flag = false;
                } else {
                    closeGene();
                    flag = true;
                }
                break;
            }
            case R.id.location: {
                CityDialog dialog = new CityDialog();
                dialog.setOnOnCitySelListener(this);
                dialog.show(getActivity().getFragmentManager(), Contants.CITY_DIALOG);
                break;
            }

            default:
                throw new RuntimeException();
        }
    }

    /**
     * 当显示填写分期时，点击隐藏分期
     */
    private void closeGene() {
        whole_content.removeView(need_dismiss);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll.removeView(img);
                ll.addView(img, 0);
                next_step1.setVisibility(View.VISIBLE);
            }
        }, 500);
        setDrawableLeft(tv_gene, R.mipmap.pat_up);
    }

    /**
     * 点击填写分期 显示组件
     */
    private void openGene() {
//        img.setVisibility(View.GONE);
        ll.removeView(img);
        next_step1.setVisibility(View.GONE);
//        ll.removeView(need_dismiss);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                whole_content.removeView(need_dismiss);
                whole_content.addView(need_dismiss);
            }
        }, 500);
//        need_dismiss.setVisibility(View.VISIBLE);
        setDrawableLeft(tv_gene, R.mipmap.pat_down);
    }

    private void setDrawableLeft(TextView v, int id) {
        Drawable drawable = getResources().getDrawable(id);
// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        v.setCompoundDrawables(drawable, null, null, null);
    }




    private String city;

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        FragmentManager fragmentManager = getFragmentManager();

        Fragment citydialog = fragmentManager.findFragmentByTag(Contants.CITY_DIALOG);

        Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
        Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
        Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
        Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);

        if (dialog == citydialog) {//取巧了 第一个城市 第二个是省份
            city = data;
            setCity(data);
            setProvince(String.valueOf(position));
            location.setRightTxt(MapDataUtils.getCityValue(data));
            return;
        }


//
        if (dialog == digitDataDialog) {//数字分期
            if (DigitDialog.SWITCH.equals(data)) {
                ll2.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.VISIBLE);
                flags = false;
                first_digit.setText(R.string.digit_t);
                currentCase.setText("");
                return;
            }
            PeriodType = "1";
            PeriodID = data;
            String temp = MapDataUtils.getDigitValues(data);
            String temp1 = getString(R.string.digit_desc_txt, temp);
            currentCase.setText(temp);
            digit_desc.setText(temp1);
        }

        if (dialog == digitTDialog) {
            if (DigitDialog.SWITCH.equals(data)) {
                setDigitGone();
                return;
            }
            getPatientInfoActivity().setPeriodType("2");
            currentCase.setText(getShow(data));
            tTemp = data;
            chooseTNMFinish();
        }

        if (dialog == digitNDialog) {
            PeriodType = "2";
            if (DigitDialog.SWITCH.equals(data)) {
                setDigitGone();
                return;
            }
            time.setText(getShow(data));
            nTemp = data;
            chooseTNMFinish();
        }

        if (dialog == digitMDialog) {
            if (DigitDialog.SWITCH.equals(data)) {
                setDigitGone();
            }
            isEffect.setText(getShow(data));
            PeriodType = "2";
            mTemp = data;
            chooseTNMFinish();
        }


    }

    private void setDigitGone() {
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        flags = true;
        first_digit.setText(R.string.choose_digit);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LogUtil.e("error","ondestroy");
//        ll.removeAllViews();
//        ll.removeView(img);
//        whole_content.removeView(need_dismiss);
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        ll.removeView(img);
//        whole_content.removeView(need_dismiss);
//    }


    /**
     * 这个是显示数字分期
     */
    private void showDigitDataDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT,UserinfoData.getCancerID(context));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
    }


    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T,UserinfoData.getCancerID(context));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

//    private static final String digit_n = "digit_n";

    private void showDigitNDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N,UserinfoData.getCancerID(context));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
    }

    /**
     * M分期
     */
//    private static final String digit_m = "digit_m";
    private void showDigitMDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M,UserinfoData.getCancerID(context));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
    }
    private String cancerId = UserinfoData.getCancerID(context);
    private String tTemp;
    private String nTemp;
    private String mTemp;

    private void chooseTNMFinish() {
        if (!TextUtils.isEmpty(cancerId) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
            List<TNMObj> list = GlobalData.tnmObjs;
            int size = list.size();
            TNMObj tnmTmp = null;
            for (int i = 0; i < size; i++) {
                tnmTmp = list.get(i);
                if ((tnmTmp.getCancerId().equals(cancerId)) &&
                        (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))

                        && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))

                        && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                    String digitID = tnmTmp.getDigitId();
                    String showdigitId = GlobalData.DigitValues.get(digitID);
//                    currentCase.setText(ZYApplication.DigitValues.get(showdigitId));
//                    setPeriodID(digitID);
                    PeriodID = digitID;
//                    digit_desc.setText(ZYApplication.DigitValues.get(showdigitId));

                    String temp1 = getString(R.string.digit_desc_txt, showdigitId);
                    LogUtil.i(TAG,temp1);
                    digit_desc.setText(temp1);
                    break;
                } else {
                    PeriodID = "";
                    String temp = getString(R.string.digit_desc_txt, "未知");
                    digit_desc.setText(temp);
                }


            }
        }
    }
    public void getResume() {
        if (next_step1 != null) {
            next_step1.setClickable(true);
        }
        if (next_step2 != null) {
            next_step2.setClickable(true);
        }
    }

    private String PeriodType;
    private void setPeriodType(String periodType) {
        getPatientInfoActivity().setPeriodType(periodType);
    }
    private String PeriodID;

    private void setPeriodID(String periodType) {
        getPatientInfoActivity().setPeriodID(periodType);
    }


    private void setCity(String periodType) {
        getPatientInfoActivity().setCity(periodType);
    }
    private void setProvince(String periodType) {
        getPatientInfoActivity().setProvince(periodType);
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
}
