package com.zeyuan.kyq.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.LoginZYBean;
import com.zeyuan.kyq.bean.UserInfoWXBean;
import com.zeyuan.kyq.presenter.LoginWXPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.PatientInfoActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CustomProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-04
 * Time: 16:44
 * FIXME
 * <p/>
 * 这个是微信所加的activity
 * <p/>
 * qq登录和微信登录
 * 微信登录要在请求获取openid 而qq登录直接返回openid
 * 2个接口 一个是请求微信的openid（tag== 0） 另一个是 登录公司服务器的接口（tag== 0）
 */


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
//    private static final String QQ_LOGIN_TYPE = "2";

    //    private static final String WEIXIN_LOGIN_TYPE = "1";
    public IWXAPI api;

//    private OkHttpClientManager.ResultCallback mResultCallback = new OkHttpClientManager.ResultCallback() {
//        @Override
//        public void onError(Request request, Exception e) {
//
//        }
//
//        @Override
//        public void onResponse(Object response) {
//
//        }
//    };
//    public static Tencent mTencent;


//    private static final String APP_ID = "wx02cca444de652c20";//微信
//    private static final String mAppid = "1104903720";//qq登录的appid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regToWX();
        api.handleIntent(getIntent(), this);
    }

//    private String openId;//设置参数 是为了测试

    /**
     * 注册微信
     */
    public void regToWX() {
        api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID, true);
        api.registerApp(Contants.AppCfg.WX__APPID);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.i(TAG, baseReq.toString());
    }

//    private String code;//微信登录返回的code

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.openId == null) {
                    String code = ((SendAuth.Resp) baseResp).code; //即为所需的code
                    LogUtil.i("WXEntryActivity", "return:" + code);
                    if (!TextUtils.isEmpty(code)) {
                        GlobalData.code = code;
                    }
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: {//分享取消
                finish();
            }
        }
    }
//
//    @Override
//    public Map getParamInfo(int tag) {
//        Map<String, String> map = new HashMap<>();
//
//        if (tag == 1) {//向微信请求数据
//            map.put(Contants.appid, Contants.AppCfg.WX__APPID);
//            map.put(Contants.secret, Contants.AppCfg.WX__SECRET);
//            map.put(Contants.code, code);
//            map.put(Contants.grant_type, Contants.AppCfg.WX__GRANT_TYPE);
//        }
//        if (tag == 0) {//登录公司服务器
//            map.put(Contants.OpenID, openId);
//            map.put(Contants.LoginType, Contants.AppCfg.LOGIN__WX_TYPE);//1微信 2qq
//            map.put(Contants.AppType, Contants.AppCfg.APP_TYPE__ANDROID);//1是ios 2 是android
//            map.put(Contants.UnionID, "");//微信公众号用的，暂时为空
//        }
//        return map;
//    }
//
//    @Override
//    public void toActivity(Object t, int position) {
//        if (position == NetNumber.VIEWDATALISTENER_TOACTIVITY_ZEYUAN) {//登录公司服务器
//            LoginZYBean bean = (LoginZYBean) t;
//            LogUtil.i(TAG, "登录公司服务器返回的数据" + bean.toString());
//            if (Contants.OK_DATA.equals(bean.getiResult())) {
//                afterLogin(bean);
//            } else {
//                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    /**
//     * 登录之后 保存好inforid 判断是否有档案 在判断
//     *
//     * @param bean
//     */
//    private void afterLogin(LoginZYBean bean) {
//        Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show();
//        SharePrefUtil.saveString(this, Contants.SAVE_INFO_ID, bean.getInfoID());//保存infoid
//        if (!TextUtils.isEmpty(GlobalData.type)) {
//            UserinfoData.saveType(this, GlobalData.type);
//        }
//        boolean hasInfor = SharePrefUtil.getBoolean(this, Contants.hasRecord, false);//是否有档案
//        if (hasInfor) {
//            startActivity(new Intent(WXEntryActivity.this, MainActivity.class));//如果有就去mainactivity
//        } else {
//            startActivity(new Intent(WXEntryActivity.this, PatientInfoActivity.class));//如果没有就去patientinfo
//        }
//        finish();
//    }
//
//    private CustomProgressDialog loginDialog;
//
//    @Override
//    public void showLoading(int tag) {
//        loginDialog = CustomProgressDialog.createCustomDialog(this);
//        loginDialog.setMessage("正在登录...");
//        loginDialog.setCanceledOnTouchOutside(false);
//        loginDialog.show();
//    }
//
//    @Override
//    public void hideLoading(int tag) {
//        loginDialog.dismiss();
//    }
//
//    @Override
//    public void showError(int tag) {
//        if (loginDialog != null)
//            loginDialog.dismiss();
//        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (loginDialog != null)
//            loginDialog.dismiss();
//        super.onDestroy();
//    }
}
