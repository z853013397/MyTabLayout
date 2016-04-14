package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.MedicineDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 15.	获取药物详情
 */
public class MedicineDetailPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public MedicineDetailPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.MEDICINE_DETAIL, new OkHttpClientManager.ResultCallback<MedicineDetailBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(MedicineDetailBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}
