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
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CityDialogLeftAdapter;
import com.zeyuan.kyq.adapter.CityDialogRightAdapter;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-11
 * Time: 12:40
 * FIXME
 */


public class CityDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "CityDialog";
    private ListView leftListview;
    private ListView rightListview;
    private CityDialogLeftAdapter leftAdapter;
    private List<String> rightData;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private Map<String, List<String>> cityData;
    private View rootView;
    private CityDialogRightAdapter rightAdapter;

    private DialogFragmentListener onCitySelListener;


    public void setOnOnCitySelListener(DialogFragmentListener onCitySelListener) {
        this.onCitySelListener = onCitySelListener;
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

    private void initData() {
        cityData = GlobalData.cityData;
        LogUtil.i("MainActivity",cityData.size());
        leftData = new ArrayList<>();
        rightData = new ArrayList<>();
        Set<String> set = cityData.keySet();
        for (String key : set) {
            leftData.add(key);
        }
        List list = cityData.get(leftData.get(0));
        rightData.addAll(list);
    }
    private TextView title;
    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        title = (TextView) rootView.findViewById(R.id.title);
        title.setText("请选择城市");
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        /**
         * 左边的listview
         *
         */
        leftAdapter = new CityDialogLeftAdapter(getActivity(), leftData);
        leftListview.setAdapter(leftAdapter);

        /**
         * 右边的listview
         */
        rightAdapter = new CityDialogRightAdapter(getActivity(), rightData);
        rightListview.setAdapter(rightAdapter);


        leftListview.setOnItemClickListener(this);
        rightListview.setOnItemClickListener(this);
        leftAdapter.setSelectedPosition(0);//设置第一项变白

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle: {
                dismiss();
                break;
            }
            case R.id.confirm: {
                if (onCitySelListener != null) {
                    if (TextUtils.isEmpty(province)) {
                        province = leftData.get(0);
                    }
                    LogUtil.i(TAG, MapDataUtils.getCityValue(province) + "   " + MapDataUtils.getCityValue(city));

                    onCitySelListener.getDataFromDialog(this, city, Integer.valueOf(province));//取巧了 第一个城市 第二个是省份
                }
                dismiss();

                break;
            }
        }
    }
    private String city;
    private String province;

    private int selPosition;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position)
                    return;
                selPosition = position;//防止重复点击出现bug

                leftAdapter.setSelectedPosition(position);//设置点击变白
                province = leftAdapter.getItem(position);
                List list = cityData.get(leftData.get(position));
                rightData.clear();
                rightData.addAll(list);
                rightAdapter.update(rightData);


                break;
            }


            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectChoose(position);//设置选中
                city = rightAdapter.getItem(position);
                break;
            }
        }
    }
}
