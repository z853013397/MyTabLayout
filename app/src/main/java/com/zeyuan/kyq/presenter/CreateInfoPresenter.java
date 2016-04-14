package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 主页的present
 */
public final class CreateInfoPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public CreateInfoPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0),Contants.CREATE_INFO, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
//                LogUtil.i("PatientInfoActivity", request.toString());
            }

            @Override
            public void onResponse(BaseBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}
