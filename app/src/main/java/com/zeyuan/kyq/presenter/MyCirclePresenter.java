package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 获取我的圈子
 */
public class MyCirclePresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public MyCirclePresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }


    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.MY_CIRCLE, new OkHttpClientManager.ResultCallback<MyCircleBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(MyCircleBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}
