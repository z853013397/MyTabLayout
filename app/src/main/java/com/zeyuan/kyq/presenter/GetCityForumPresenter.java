package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 获取获取同城圈的帖子数量和人数
 */
public class GetCityForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;



    public GetCityForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public  void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0),Contants.CITY_FORUM, new OkHttpClientManager.ResultCallback<CityCancerForumBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(CityCancerForumBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}
