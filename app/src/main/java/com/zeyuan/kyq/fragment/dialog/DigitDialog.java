package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.DigitAdapter;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.PatientDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 * 分期的dialog
 */
public class DigitDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TYPE = "type";

    public static final String DIGIT = "digit";//数字分期
    public static final String DIGIT_T = "digitT";//t分期
    public static final String DIGIT_N = "digitN";//n分期
    public static final String DIGIT_M = "digitM";//m分期
    public static final String SWITCH = "switch";//切换分期


    public static final String AGE = "age";

    private DialogFragmentListener listener;
    private View rootView;

    public void setListener(DialogFragmentListener listener) {
        this.listener = listener;
    }

    public DigitDialog() {
    }

    private static final String CANCER_ID = "cancerid";
    private String CancerID;

    public static DigitDialog newInstance(String param, String cancerId) {
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        args.putString(CANCER_ID, cancerId);
        DigitDialog fragment = new DigitDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
            CancerID = getArguments().getString(CANCER_ID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_digit, null);
        initView();
        initData();
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
//        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle);

        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private ListView listview;
    private TextView switch_digit;//切换分期
    //    private Button finish;//完成
    private List<String> data;//listview中的数据
    private DigitAdapter adapter;
    private TextView title;
    private void initView() {
        listview = (ListView) rootView.findViewById(R.id.listview);
        switch_digit = (TextView) rootView.findViewById(R.id.switch_digit);
        title = (TextView) rootView.findViewById(R.id.title);

        if (DIGIT.equals(type)) {
            switch_digit.setText("切换TNM分期");
        } else {
            switch_digit.setText("切换数字分期");

        }
//        finish = (Button) rootView.findViewById(R.id.finish);

        /**
         * 设置监听
         */
        switch_digit.setOnClickListener(this);
//        finish.setOnClickListener(this);
    }
    private boolean isAge = true;
    private void initData() {
        data = new ArrayList<>();
        List<String> list;


        if (DIGIT.equals(type)) {
            list = GlobalData.digitData.get(CancerID);
        } else if (DIGIT_T.equals(type)) {
            list = GlobalData.digitT.get(CancerID);
        } else if (DIGIT_N.equals(type)) {
            list = GlobalData.digitN.get(CancerID);
        } else if (DIGIT_M.equals(type)) {//剩下的就是m了
            list = GlobalData.digitM.get(CancerID);
        } else {
            title.setText("请选择年龄");
            isAge = false;
            switch_digit.setVisibility(View.GONE);
            list = new ArrayList<>();
            for (int i = 0; i < 99; i++) {
                list.add(i + "");
            }
        }


        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
        adapter = new DigitAdapter(getActivity(), data,isAge);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }

//    /**
//     * 从Activity 中得到cancerId
//     *
//     * @return
//     */
//    private String CancerID {
//        if (getActivity() instanceof PatientDetailActivity) {
//            return ((PatientDetailActivity) getActivity()).CancerID;
//        }
//
//        //这儿 还注意增加判断 还有其他的activity
//        String cancerId = UserinfoData.getCancerID(getActivity());
//        return cancerId;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_digit: {
                if (listener != null) {
                    listener.getDataFromDialog(this, SWITCH, 0);
                }
                dismiss();
                break;
            }
            default:
                throw new RuntimeException("数据不匹配");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String chooseId = adapter.getItem(position);
        if (chooseId == null)
            return;
        if (listener != null) {
            listener.getDataFromDialog(this, chooseId, 0);
        }
        dismiss();
    }

//    public void setListener(AddZLFragment addZLFragment) {
//
//    }
}
