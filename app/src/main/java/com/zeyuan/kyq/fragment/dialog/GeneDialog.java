package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.PatientDetailActivity;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-29
 *显示一个可以多选的dialog
 * Time: 20:06
 * FIXME
 */
public class GeneDialog extends DialogFragment implements View.OnClickListener {
    private View rootView;
    private List<String> mData;
    private DialogFragmentListener mDialogFragmentListener;


//    public static GeneDialog newInstance(Bundle args) {
////        Bundle args = new Bundle();
//        GeneDialog fragment = new GeneDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }

    private List<String> isChoosedData;

    public GeneDialog() {
    }
    private int type;
    /**
     * @param mData 显示的数据
     * @param isChoosedData 进来选择的数据可以为空
     */
    public GeneDialog(List<String> mData,List<String> isChoosedData,int type) {
        this.type = type;
        this.isChoosedData = isChoosedData;
        this.mData = mData;
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            isChoosedData = (List<String>) bundle.getSerializable(Contants.List);
//        }
//    }

    public void setDialogFragmentListener(DialogFragmentListener dialogFragmentListener) {
        mDialogFragmentListener = dialogFragmentListener;
    }

    private Button confirm;
    private Button cancle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.gene_dialog, null);
        initData();
        initView();
        dialog.setContentView(rootView);
        return dialog;
    }

    private List<String> checkGene;
    private FlowLayout flowLayout;
    private TextView tv_title;

    private void initView() {
        confirm = (Button) rootView.findViewById(R.id.confirm);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        tv_title = (TextView) rootView.findViewById(R.id.title);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        confirm.setOnClickListener(this);
        cancle.setOnClickListener(this);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.fl);
        if (mData == null || mData.size() == 0) {
            return;
        }
        for (final String string : mData) {
            CheckBox cb = (CheckBox) LayoutInflater.from(getActivity()).inflate(R.layout.checkbox, null);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkGene.add(string);
                    } else {
                        if (checkGene.contains(string)) {
                            checkGene.remove(string);
                        }
                    }
                }
            });
            if (isChoosedData != null) {
                if (isChoosedData.contains(string)) {
                    cb.setChecked(true);
                }
            }
            if (type == Contants.diolog_type) {
                cb.setText(GlobalData.transferpos.get(string));
            }else {
                cb.setText(GlobalData.geneValues.get(string));
            }
            flowLayout.addView(cb);
        }


    }

    private void initData() {
//        mData = new ArrayList<>();
        checkGene = new ArrayList<>();

//        List<String> data =;
//        if (data != null && data.size() > 0) {
//            mData.addAll(data);
//        }
    }


//    private String getCancerId() {
//        if (getActivity() instanceof PatientDetailActivity) {
//            return ((PatientDetailActivity) getActivity()).getCancerId();
//        }
//
//        //这儿 还注意增加判断 还有其他的activity
//        String cancerId = UserinfoData.getCancerID(getActivity());
//        return cancerId;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm: {
                if (mDialogFragmentListener != null) {
                    if (checkGene != null) {
                        if (checkGene.size() == 0) {
                            mDialogFragmentListener.getDataFromDialog(this, "", 0);//解决取消的问题
                        } else {
                            mDialogFragmentListener.getDataFromDialog(this, listToLoadingString(checkGene), 0);
                        }
                    }
                }

            }
            case R.id.cancle: {

            }
            dismiss();

        }
    }

    @NonNull
    private String listToLoadingString(List<String> checkGene) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < checkGene.size(); i++) {
            if (i == 0) {
                sb.append(checkGene.get(i));
                continue;
            }
            sb.append("," + checkGene.get(i));
        }

        return sb.toString();
    }
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }

}
