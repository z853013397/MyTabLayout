package com.zeyuan.kyq.view;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.presenter.SyncConfPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.PraSync;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.wxapi.WXEntryActivity;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity implements ViewDataListener, PraSync.PraCallback {


    private static final String TAG = "SplashActivity";
//    private SyncConfPresenter mSyncConfPresenter = new SyncConfPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        NetWorkStatus();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        LogUtil.i(TAG, "你的手机分配给app的内存为：" + heapSize + "MB");
//        initData();


//        new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                toNextActivity();
//            }
//        }, 3000);
    }

    private void toNextActivity() {
//        String inforid = SharePrefUtil.getString(SplashActivity.this, Contants.SAVE_INFO_ID, null);
        String inforid = UserinfoData.getInfoID(this);
        String stepid = UserinfoData.getStepID(this);
        if (TextUtils.isEmpty(inforid)) {
            //是否没有infroid 和stepid 有就登录没有就去建立档案？？？
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));//没有就登录

        } else if (TextUtils.isEmpty(stepid)) {
//            boolean hasInfor = SharePrefUtil.getBoolean(SplashActivity.this, Contants.hasRecord, false);//是否有档案
//            if (hasInfor) {
//            } else {
            startActivity(new Intent(SplashActivity.this, PatientInfoActivity.class));//没有 就去建立档案
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));//没有 就去建立档案
//            }
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));//就去mainactivity
        }
        finish();
    }


    private void initData() {
        PraSync.praConifg(Contants.SYNC_CONFIG, SplashActivity.this, this);//保存配置信息
//        mSyncConfPresenter.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
//            map.put("InfoID", "2");
//            map.put("TsDiagnose", "5");//诊断结果时间戳
//            map.put("TsBodyPos", "5");//身体部位时间戳
//            map.put("TsCancerPos", "5");//肿瘤位置时间戳
//            map.put("TsCure", "5");//治疗手段时间戳
//            map.put("TsPerform", "5");//症状时间戳


        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        String data = (String) t;
        LogUtil.d(TAG, data);
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    @Override
    public void isFinish(boolean isFinish) {
        if (isFinish) {
            toNextActivity();
        }
    }

    @Override
    public void isFailed() {
        showToast("后台数据变动，请更新你的app");
        toNextActivity();
    }


    //代码

    private boolean NetWorkStatus() {

        boolean netSataus = false;
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        cwjManager.getActiveNetworkInfo();

        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
        }

        if (!netSataus) {
            AlertDialog.Builder b = new AlertDialog.Builder(this).setTitle("没有可用的网络")
                    .setMessage("是否对网络进行设置？");
            b.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//                    Intent mIntent = new Intent("/");
//                    ComponentName comp = new ComponentName(
//                            "com.android.settings",
//                            "com.android.settings.WirelessSettings");
//                    mIntent.setComponent(comp);
//                    mIntent.setAction("android.intent.action.VIEW");
//                    startActivityForResult(mIntent,0);  // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写

                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                    finish();
                    System.exit(0);
                }
            }).show();
        }
        return netSataus;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
