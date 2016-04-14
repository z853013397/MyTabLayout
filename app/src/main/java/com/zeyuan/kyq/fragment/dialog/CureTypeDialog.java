package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CureTypeLeftAdapter;
import com.zeyuan.kyq.adapter.CureTypeRightAdapter;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/6.
 * <p/>
 * 这个是选药物的dialog
 * 已不复写
 */
public class CureTypeDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
//    /**
//     * 这个是dialog的类型。
//     */
//    public static final String TYPE_MEDICA = "cure";//这个是选药
//
//    public static final String CancerPos = "CancerPos";//这个是选药
//
//    public static final String CITY = "City";//这个是城市
    public CureTypeDialog() {

    }
//    public CureTypeDialog(String cancerId) {
//        this.cancerId = cancerId;
//    }
    private static final String TAG = "ChooseMedicaDialog";
//    private static final String TYPE = "type";
//    private String type;
    private ListView leftListview;
    private ListView rightListview;
    private CureTypeLeftAdapter leftAdapter;
//    private List<List<String>> mRightDatas;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private LinkedHashMap<String, List<String>> cureData;

    private DialogFragmentListener drugsNameListener;
    private CureTypeRightAdapter rightAdapter;

//    public static CureTypeDialog newInstance(String param1) {
//        CureTypeDialog fragment = new CureTypeDialog();
//        Bundle args = new Bundle();
//        args.putString(TYPE, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        if(getArguments() != null) {
//           type =  getArguments().getString(TYPE);
//        }
//        super.onCreate(savedInstanceState);
//    }

//    public interface DrugsNameListener {
//        void getDrugsName(List data, int id);
//    }

    public void setDrugsNameListener(DialogFragmentListener drugsNameListener) {
        this.drugsNameListener = drugsNameListener;
    }

//    private List<String>[] data;//这个数组表示多个右边选中的项目。data[0]就是右边listview中选中的每个item项目

    private void initData() {
        leftData = new ArrayList<>();
        cureData = GlobalData.cancerCure.get(getCancerid());//这个填上对应癌症
        if (cureData == null || cureData.size() == 0) {
            cureData = GlobalData.cancerCure.get("0");//这个填上对应癌症

//            Toast.makeText(getActivity(), "没有对应癌肿数据", Toast.LENGTH_SHORT).show();
//            return;
        }

        Set<String> leftSet = cureData.keySet();//
        for (String str : leftSet) {//把set转换为list
//            List<String> datas = new ArrayList<>();
            leftData.add(str);
        }

        /**
         * 初始化data
         */
//        data = new List[leftData.size()];//
//        for (int i = 0; i < leftData.size(); i++) {
//            List<String> datas = new ArrayList<>();
//            data[i] = datas;
//        }


//        mRightDatas = new ArrayList<>();//new出一个数组里面装着他们的adapter
        /**
         * 得到左边的键
         */
//        for (int i = 0; i < leftData.size(); i++) {
//            List<String> datas = cureData.get(leftData.get(i));
////            LogUtil.i(TAG,rightSet.size()+ "");
////            List<String> datas = new ArrayList<>();
////            for (String str : rightSet) { //把set转换为list
////                datas.add(str);
////            }
//            mRightDatas.add(datas);
//        }
    }

    private String cancerId;


    private String getCancerid() {
        if (TextUtils.isEmpty(cancerId)) {
            cancerId = UserinfoData.getCancerID(getActivity());
            if (TextUtils.isEmpty(cancerId)) {
                Toast.makeText(getActivity(), "没有选择癌肿", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }

        return cancerId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);
        initData();
        initView();
        dialog.setContentView(rootView);
        return dialog;
    }

    private View rootView;

    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);

        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        /**
         * 左边的listview
         *
         */

        leftAdapter = new CureTypeLeftAdapter(getActivity(), leftData);
        leftListview.setAdapter(leftAdapter);

        /**
         * 右边的listview
         */

        rightAdapter = new CureTypeRightAdapter(getActivity(), cureData.get(leftData.get(0)));
        rightListview.setAdapter(rightAdapter);


        leftListview.setOnItemClickListener(this);
        rightListview.setOnItemClickListener(this);
        leftAdapter.setSelectedPosition(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();
                break;
            case R.id.confirm:
                if (drugsNameListener != null) {
                    if (!TextUtils.isEmpty(cure)) {
//                        UserinfoData.saveStepID(getActivity(), cure);
                        drugsNameListener.getDataFromDialog(this, cure, 0);
                    }
                }
                dismiss();
                break;
        }
    }

    private int selPosition = 0;
    private String cure;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position)
                    return;
                selPosition = position;//防止重复点击出现bug
                leftAdapter.setSelectedPosition(position);//这个是为了点击效果(点击变白)
                String chooseItem = leftAdapter.getItem(position);
                List<String> list = cureData.get(chooseItem);
                rightAdapter.update(list);
                break;
            }
            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectChoose(position);//设置选中

                cure = rightAdapter.getItem(position);
                break;
            }
        }
    }

//    /**
//     * 没有判断checkDrugs.size()是否大于0 在调用前判断下
//     */
//    private String getAllDrugs() {
////        String result = "";
////        data
//        List<String> list = new ArrayList();
//        for (int i = 0; i < data.length; i++) {
//            for (String str : data[i]) {
//                list.add(str);
//            }
//        }
//        LogUtil.i(TAG, "选中药物的个数是：" + list.size());
////        List<String> list2 = new ArrayList();
////        for(int i = 0;i < data.length;i ++) {
////           String str =  ZYApplication.values.get(list.get(i));
////
////        }
//        return DataUtils.listToLoadString(list);
//    }


    public void setCancerID(String cancerID) {
            cancerId = cancerID;
    }

}
