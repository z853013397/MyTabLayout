package com.zeyuan.kyq.view;

import android.app.ProgressDialog;
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

import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.LoginZYBean;
import com.zeyuan.kyq.bean.LoginQQBean;
import com.zeyuan.kyq.presenter.LoginQQPresenter;
import com.zeyuan.kyq.presenter.LoginWXPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/14.
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewDataListener {
    private static final String TAG = "GuideActivity";

    private ViewPager vg;
    private RelativeLayout mRelativeLayout_weixin_login;
    private RelativeLayout mRelativeLayout_qq_login;

    public static Tencent mTencent;
//    private static final String mAppid = "1104903720";//qq登录的appid


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Contants.AppCfg.QQ__APPID, this.getApplicationContext());
        }
        regToWX();
        initView();
        vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));//如果有就去mainactivity
            }
        });
    }


    public static IWXAPI api;

    /**
     * 注册微信
     */
    public void regToWX() {
        api = WXAPIFactory.createWXAPI(this, Contants.AppCfg.WX__APPID, true);
        api.registerApp(Contants.AppCfg.WX__APPID);
    }

    private void initView() {
        mRelativeLayout_weixin_login = (RelativeLayout) findViewById(R.id.weixin_login);
        mRelativeLayout_qq_login = (RelativeLayout) findViewById(R.id.qq_login);
        mRelativeLayout_weixin_login.setOnClickListener(this);
        mRelativeLayout_qq_login.setOnClickListener(this);
        vg = (ViewPager) findViewById(R.id.vg);
        initImageView();
        if (images != null && images.length > 0) {
            GuidePagerAdapter adapter = new GuidePagerAdapter();
            vg.setAdapter(adapter);
            vg.setCurrentItem(20);
        }
    }

    private int[] resource = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
    private ImageView[] images;

    private void initImageView() {
        images = new ImageView[resource.length];
        for (int i = 0; i < resource.length; i++) {
            ImageView img = new ImageView(this);
            img.setBackgroundResource(resource[i]);
            images[i] = img;
        }
    }

    private String openId;
    private String loginType;
    private IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(LoginQQBean values) {
            LogUtil.i(TAG, "qqloginbean:" + values.toString());
            openId = values.getOpenid();
            LogUtil.i(TAG, "openid 是：" + openId);
            loginType = Contants.AppCfg.LOGIN__QQ_TYPE;
            if (TextUtils.isEmpty(openId)) {
                return;
            }
            new LoginQQPresenter(GuideActivity.this, values).getData();
        }
    };
    private boolean isWxLogin = false;//一个表示用来判断是否为微信登录

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_login://qq登录
                mTencent.login(this, "all", loginListener);
                if (dialog == null) {
                    dialog = new ProgressDialog(this);
                }
                dialog.setCancelable(false);
                dialog.setMessage("正在拉取授权");
                dialog.show();
                break;

            case R.id.weixin_login://微信登录
                weixinLogin();
                isWxLogin = true;
                if (dialog == null) {
                    dialog = new ProgressDialog(this);
                }
                dialog.setCancelable(false);
                dialog.setMessage("正在拉取授权");
                dialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void weixinLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = Contants.AppCfg.WX__SCOPE;
        req.state = Contants.AppCfg.WX__STATE;
        api.sendReq(req);
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.OpenID, openId);
            map.put(Contants.LoginType, loginType);//1微信 2qq
            map.put(Contants.AppType, Contants.AppCfg.APP_TYPE__ANDROID);
            map.put(Contants.UnionID, "");//微信公众号用的，暂时为空
        }

        if (tag == 1) {//向微信请求数据
            map.put(Contants.appid, Contants.AppCfg.WX__APPID);
            map.put(Contants.secret, Contants.AppCfg.WX__SECRET);
            map.put(Contants.code, code);
            map.put(Contants.grant_type, Contants.AppCfg.WX__GRANT_TYPE);
        }
        return map;
    }

    private String code;

    @Override
    public void toActivity(Object t, int position) {
        qqLogin((LoginZYBean) t);
    }

    private void qqLogin(LoginZYBean t) {
        LoginZYBean bean = t;
        LogUtil.i(TAG, "登录服务器返回的数据" + bean.toString());
        if (Contants.OK_DATA.equals(bean.getIResult())) {
            afterLogin(bean);
        } else {
            Toast.makeText(GuideActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void afterLogin(LoginZYBean bean) {
        if (!TextUtils.isEmpty(GlobalData.type)) {
            UserinfoData.saveType(this, GlobalData.type);
        }
        LogUtil.i(TAG, "nick name : " + GlobalData.mUserNickname);
        UserinfoData.saveInfoname(this, GlobalData.mUserNickname);


        LogUtil.i(TAG, "save nick name :" + UserinfoData.getInfoname(this));

//        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();


        UserinfoData.saveInfoID(this, bean.getInfoID());
//        SharePrefUtil.saveString(this, Contants.SAVE_INFO_ID, bean.getInfoID());//保存infoid
        String IsHaveLogin = bean.getIsHaveLogin();
        if ("1".equals(IsHaveLogin)) {
            GlobalData.IsHaveLogin = false;
        } else {
            GlobalData.IsHaveLogin = true;
        }


        String IsHaveCreateInfo = bean.getIsHaveCreateInfo();
        if (LoginZYBean.HAS_CREATE.equals(IsHaveCreateInfo)) {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));//如果有就去mainactivity
        } else {
            startActivity(new Intent(GuideActivity.this, PatientInfoActivity.class));//如果没有就去patientinfo
        }


//        boolean hasInfor = SharePrefUtil.getBoolean(this, Contants.hasRecord, false);//是否有档案
//        if (hasInfor) {
//            startActivity(new Intent(GuideActivity.this, MainActivity.class));//如果有就去mainactivity
//        } else {
//            startActivity(new Intent(GuideActivity.this, PatientInfoActivity.class));//如果没有就去patientinfo
//        }
        finish();
    }

    private ProgressDialog dialog;

    @Override
    public void showLoading(int tag) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("正在登录");
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog.setMessage("正在拉取授权");
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (dialog != null) {
            dialog.dismiss();
        }
        LogUtil.i(TAG, "error");
    }


    class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 40;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images[position % images.length]);
            return images[position % images.length];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images[position % images.length]);
            object = null;
        }
    }


    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Gson gson = new Gson();
            LoginQQBean qqLoginBean = gson.fromJson(response.toString(), LoginQQBean.class);
            doComplete(qqLoginBean);
        }

        protected void doComplete(LoginQQBean values) {
//            LogUtil.i(TAG,values.toString());

        }

        @Override
        public void onError(UiError e) {
            LogUtil.i(TAG, e.toString());
        }

        @Override
        public void onCancel() {
            LogUtil.i(TAG, "cancel");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i(TAG, "onactivityresult");
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == Constants.ACTIVITY_OK) {
//                 LogUtil.i(TAG,requestCode  +",," + resultCode);
                mTencent.handleLoginData(data, loginListener);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i(TAG, "onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume");
        if (isWxLogin && !TextUtils.isEmpty(GlobalData.code)) {//这个说明这儿是从微信登录返回来的数据
            code = GlobalData.code;
            new LoginWXPresenter(this).getData();
        }
    }


}
