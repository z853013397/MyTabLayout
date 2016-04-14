package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.CancerForumBean;
import com.zeyuan.kyq.bean.SimalarListBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.SIMILAR_LIST15/1NetNumber.SIMILAR_LIST/15.
 * 14.获取相似案例列表。
 */
public class GetSimilarListPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetSimilarListPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.SIMILAR_LIST);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.SIMILAR_LIST), Contants.SIMILAR_LIST, new OkHttpClientManager.ResultCallback<SimalarListBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.SIMILAR_LIST);

            }

            @Override
            public void onResponse(SimalarListBean response) {
                mainFragmentView.hideLoading(NetNumber.SIMILAR_LIST);
                mainFragmentView.toActivity(response, NetNumber.SIMILAR_LIST);
            }
        });
    }
}
