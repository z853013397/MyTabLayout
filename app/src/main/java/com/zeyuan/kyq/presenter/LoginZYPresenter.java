package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.UserInfoQQBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.bean.LoginZYBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 * 登录泽源公司服务器
 */
public final class LoginZYPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;

    public LoginZYPresenter(ViewDataListener mainFragmentView ) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

//    private String imgurl;

    /**
     * 连接网络获取数据
     */
    public void getData(int loginType, Object obj) {
        mainFragmentView.showLoading(0);

        Map<String, String> map = new HashMap<>();
        {
            switch (loginType) {
                case Contants.AppCfg.LOGIN_TYPE__QQ: {
                    UserInfoQQBean infoQQBean = (UserInfoQQBean) obj;
                    map.put(Contants.OpenID, infoQQBean.getOpenid());
                    GlobalData.mUserHeadUrl_ = infoQQBean.getFigureurl_qq_2();
                    GlobalData.type = "1";
                    GlobalData.mUserNickname = infoQQBean.getNickname();
                    map.put(Contants.LoginType, "" + Contants.AppCfg.LOGIN_TYPE__QQ);
                    break;
                }
                case Contants.AppCfg.LOGIN_TYPE__WX: {
                    UserInfoWXBean infoWxBean = (UserInfoWXBean) obj;
                    GlobalData.mUserHeadUrl_ = infoWxBean.getHeadimgurl();
                    GlobalData.mUserNickname = infoWxBean.getNickname();
                    GlobalData.type = "2";
                    map.put(Contants.OpenID, infoWxBean.getOpenid());
                    map.put(Contants.LoginType, "" + Contants.AppCfg.LOGIN_TYPE__WX);
                    break;
                }
            }


            map.put(Contants.AppType, Contants.AppCfg.APP_TYPE__ANDROID);
            map.put(Contants.UnionID, "");//微信公众号用的，暂时为空
        }
        biz.getData(map, Contants.LOGIN, new OkHttpClientManager.ResultCallback<LoginZYBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(0);
            }

            @Override
            public void onResponse(LoginZYBean response) {
                mainFragmentView.hideLoading(0);
                    mainFragmentView.toActivity(response, NetNumber.VIEWDATALISTENER_TOACTIVITY_ZEYUAN);
            }
        });
    }
}
