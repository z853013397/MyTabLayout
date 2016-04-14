package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

/**
 * Created by Administrator on 2015/10/15.
 * 53.	圈子—我的帖子(我发表的帖子/评论我的)
 */
public class GetMyForumPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public GetMyForumPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 连接网络获取数据
     */
    public void getData() {
        mainFragmentView.showLoading(0);
        biz.getData(mainFragmentView.getParamInfo(0), Contants.GET_MY_FORUM, new OkHttpClientManager.ResultCallback<MyForumReleaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);

            }

            @Override
            public void onResponse(MyForumReleaseBean response) {
                mainFragmentView.toActivity(response,0);
                mainFragmentView.hideLoading(0);
            }
        });
    }
}
