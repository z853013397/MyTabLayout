package com.zeyuan.kyq.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Administrator on 2015/9/7.
 * fragment的父类
 */
public class BaseFragment extends Fragment {
    protected Context context;
    protected View rootView;
    protected OnDataFromFragment onDataFromFragment;


    public void setOnDataFromFragment(OnDataFromFragment onDataFromFragment) {
        this.onDataFromFragment = onDataFromFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View getRootView() {
        return rootView;
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }




    public interface OnDataFromFragment {
        void dataFromFragment(Fragment fragment,String data,String tag);
    }
}
