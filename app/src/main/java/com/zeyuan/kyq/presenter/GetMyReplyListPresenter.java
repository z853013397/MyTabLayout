package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.GET_MY_REPLY_LIST15/1NetNumber.GET_MY_REPLY_LIST/15.
 * 新增/更新症状
 */
public final class GetMyReplyListPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetMyReplyListPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.GET_MY_REPLY_LIST);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.GET_MY_REPLY_LIST), Contants.GET_MY_REPLY_LIST, new OkHttpClientManager.ResultCallback<MyReplyListBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.GET_MY_REPLY_LIST);
            }

            @Override
            public void onResponse(MyReplyListBean response) {
                mainFragmentView.hideLoading(NetNumber.GET_MY_REPLY_LIST);
                mainFragmentView.toActivity(response,NetNumber.GET_MY_REPLY_LIST);
            }
        });
    }
}
