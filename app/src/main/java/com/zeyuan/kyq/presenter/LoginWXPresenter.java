package com.zeyuan.kyq.presenter;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.LoginWXBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/15.
 * 新增/更新症状
 */
public class LoginWXPresenter {
    private GetDataBiz biz;
    private ViewDataListener mainFragmentView;


    public LoginWXPresenter(ViewDataListener mainFragmentView) {
        biz = new GetDataBiz();
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 通过code获取access_token
     */
    public void getData() {
        mainFragmentView.showLoading(1);

//        StringBuffer stringBuffer =new StringBuffer();
//        for(int i=0;i<10;i++){
//            stringBuffer.append("----");
//            stringBuffer.append(mainFragmentView.getParamInfo(i));
//        }

        biz.getData(mainFragmentView.getParamInfo(1), Contants.WEIXIN_CODE, new OkHttpClientManager.ResultCallback<LoginWXBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(LoginWXBean response) {
                mainFragmentView.hideLoading(1);
                getUserInfo(response.getAccess_token(), response.getOpenid());
            }
        });


    }

    private void getUserInfo(String access_token, String openid) {
        HashMap<String, String> params = new HashMap<>();
        {
            params.put("access_token", access_token);
            params.put("openid", openid);
        }
        biz.getData(params, Contants.WEIXIN__USERINFO, new OkHttpClientManager.ResultCallback<UserInfoWXBean>() {
            @Override
            public void onError(Request request, Exception e) {
                mainFragmentView.showError(1);
            }

            @Override
            public void onResponse(UserInfoWXBean response) {
                mainFragmentView.hideLoading(1);
//                mainFragmentView.toActivity(response, 1);
                //这儿做图片上传
                new LoginZYPresenter(mainFragmentView).getData(Contants.AppCfg.LOGIN_TYPE__WX, response);
            }
        });
    }
}
