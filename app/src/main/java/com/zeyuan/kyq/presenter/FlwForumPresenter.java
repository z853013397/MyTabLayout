package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2NetNumber.FLW_FORUM15/1NetNumber.FLW_FORUM/15.
 * 收藏某个帖子
 */
public final class FlwForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public FlwForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(NetNumber.FLW_FORUM);
        biz.getData(mainFragmentView.getParamInfo(NetNumber.FLW_FORUM), Contants.FOLLOW_FORUM, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(NetNumber.FLW_FORUM);
            }

            @Override
            public void onResponse(BaseBean response) {
                mainFragmentView.hideLoading(NetNumber.FLW_FORUM);
                mainFragmentView.toActivity(response,NetNumber.FLW_FORUM);
            }
        });
    }
}
