package com.zeyuan.kyq.fragment.patientinfo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.fragment.dialog.CancerTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.DoubleTvLayout;

/**
 * 建立档案的设置癌症种类
 */
public class CancerTypeFragment extends PatientInfoFragment implements View.OnClickListener, DialogFragmentListener {

    //    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

//    public static CancerTypeFragment newInstance(String param1, String param2) {
//        CancerTypeFragment fragment = new CancerTypeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cancer_type, container, false);
        initView();
        return rootView;
    }

    private DoubleTvLayout cancerType;//选择癌症种类
    private TextView next_step;//下一步

    private ImageView img;

    private void initView() {
        img = (ImageView) findViewById(R.id.btn_back);
        img.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.patient_info);
        cancerType = (DoubleTvLayout) findViewById(R.id.cancer_type);
        cancerType.setLeftTxt("请选择癌种");
        next_step = (TextView) findViewById(R.id.next_step);

        cancerType.setOnClickListener(this);
        next_step.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_back:
//                if(onLastStepClickListener != null) {
//                    onLastStepClickListener.onLastStepClickListener(this);
//                }
//                break;
            case R.id.cancer_type: {
                CancerTypeDialog dialog = new CancerTypeDialog();
                dialog.setOnCancerTyperListener(this);
                dialog.show(getActivity().getFragmentManager(), "dialog");
                break;
            }
            case R.id.next_step: {//下一步
//                String cancerType = this.cancerType.getText().toString().trim();
                if (TextUtils.isEmpty(cancerTypes)) {
                    Toast.makeText(getActivity(), getString(R.string.choose_cancer_type), Toast.LENGTH_SHORT).show();
//                    getPatientInfoActivity().setCancerID(cancerTypes);
                    return;
                }
                getPatientInfoActivity().setCancerID(cancerTypes);
//                SharePrefUtil.saveString(getActivity(), Contants.CancerID, cancerTypes);
                UserinfoData.saveCancerID(getActivity(),cancerTypes);
                if (onNextStepClickListener != null) {
                    onNextStepClickListener.onNextStepClickListener(this);
                    next_step.setClickable(false);
                }

                break;
            }
            default:
                throw new RuntimeException();
        }
    }

    private String cancerTypes;


    @Override
    public void getDataFromDialog(DialogFragment dialog, String data,int positon) {
        cancerTypes = data;
        setCancerID(data);
        this.cancerType.setRightTxt(GlobalData.cancerValues.get(data));
    }




//    @Override
    public void getResume() {
        LogUtil.i("MainActivity", "onresume");
        if (next_step != null) {
            next_step.setClickable(true);
        }
    }

    private void setCancerID(String id) {
        getPatientInfoActivity().setCancerID(id);
    }
}
