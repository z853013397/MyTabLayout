package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * 获取获取抗癌圈的帖子数量和人数
 */
public class GetCancerForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public GetCancerForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(1),Contants.CANCER_FORUM, new OkHttpClientManager.ResultCallback<CityCancerForumBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(CityCancerForumBean response) {
                mainFragmentView.hideLoading(0);
                mainFragmentView.toActivity(response,1);
            }
        });
    }
}
