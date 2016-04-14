package com.zeyuan.kyq.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.fragment.patientinfo.CancerTypeFragment;
import com.zeyuan.kyq.fragment.patientinfo.DiscoverTimeFragment;
import com.zeyuan.kyq.fragment.patientinfo.LcGnFragment;
import com.zeyuan.kyq.fragment.patientinfo.PatientInfoFragment;
import com.zeyuan.kyq.fragment.patientinfo.PatientResultFragment;
import com.zeyuan.kyq.presenter.CreateInfoPresenter;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PatientInfoActivity extends BaseActivity implements PatientInfoFragment.OnNextStepClickListener, PatientInfoFragment.OnLastStepClickListener, ViewDataListener {
    private static final String TAG = "PatientInfoActivity";

    private CancerTypeFragment cancerTypeFragment;//患者信息设置癌症种类
    private DiscoverTimeFragment mDiscoverTimeFragment;//设置患者确诊时间
    private LcGnFragment mLcGnFragment;//设置地点和分期
    private PatientResultFragment mPatientResultFragment;//最后一步
    private List<PatientInfoFragment> fragments;
    //    private CreateInfoPresenter createInfoPresenter = new CreateInfoPresenter(this);

    private CDNHelper mCDNHelper;
    private Callback mResultCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();
                if (tempFile == null) {
                    tempFile = new File(Environment.getExternalStorageDirectory(), "head.png");
                }
                fos = new FileOutputStream(tempFile);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
            } catch (Exception e) {

            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                }

                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }
//                File file = new File(Environment.getExternalStorageDirectory(), "head.png");
                if (tempFile == null) {
                    tempFile = new File(Environment.getExternalStorageDirectory(), "head.png");
                }
                mCDNHelper.uploadFile(tempFile.getPath(), getImgName(PatientInfoActivity.this, true), mSaveCallback);
            }

        }
    };

    private SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(String s) {
            LogUtil.i(TAG, s);
            Headimgurl = mCDNHelper.getResourseURL();
            if (!TextUtils.isEmpty(Headimgurl)) {
                UserinfoData.saveAvatarUrl(PatientInfoActivity.this, Headimgurl);
            }
        }

        @Override
        public void onProgress(String s, int i, int i1) {
            LogUtil.i(TAG, s + "  " + i + " " + i1);
        }

        @Override
        public void onFailure(String s, OSSException e) {
            LogUtil.i(TAG, s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        mCDNHelper = new CDNHelper(this);
        loadImage();
//            LogUtil.i(TAG, "测试其他数据： " + ZYApplication.values.get("560"));
        cancerTypeFragment = new CancerTypeFragment();
        cancerTypeFragment.setOnNextStepClickListener(this);
        fragments = new ArrayList<>();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        ft.add(R.id.id_content, cancerTypeFragment, "cancerType");
//        ft.addToBackStack("dj");//不知为何无反应
        ft.commit();
        fragments.add(cancerTypeFragment);
    }

    private void loadImage() {
        // 判断是否第一次创建账号
        if (true) {
            //如果第一次，下载到SD卡
            // 上传头像到CDN
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (TextUtils.isEmpty(GlobalData.mUserHeadUrl_)) {
                        return;
                    }
                    CDNHelper.downloadFile(GlobalData.mUserHeadUrl_, Environment.getExternalStorageState(), mResultCallback);
                }
            }.start();
        }
    }


    @Override
    public void onLastStepClickListener(Fragment fragment) {
        onBackPressed();
    }


    @Override
    public void onNextStepClickListener(Fragment fragment) {
        if (fragment == cancerTypeFragment) {//点击癌症种类中的下一步
            if (mDiscoverTimeFragment == null) {
                mDiscoverTimeFragment = new DiscoverTimeFragment();
                mDiscoverTimeFragment.setOnNextStepClickListener(this);
                mDiscoverTimeFragment.setOnLastStepClickListener(this);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            ft.hide(cancerTypeFragment).add(R.id.id_content, mDiscoverTimeFragment, "location");
//            ft.addToBackStack(null);
            ft.commit();
            fragments.add(mDiscoverTimeFragment);
        }


        if (fragment == mDiscoverTimeFragment) {//点击位置中的下一步
            if (mLcGnFragment == null) {
                mLcGnFragment = new LcGnFragment();
                mLcGnFragment.setOnLastStepClickListener(this);
                mLcGnFragment.setOnNextStepClickListener(this);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            ft.hide(mDiscoverTimeFragment).add(R.id.id_content, mLcGnFragment, "stepfragment");
//            ft.addToBackStack(null);
//            getFragmentManager().popBackStack();
            ft.commit();
            fragments.add(mLcGnFragment);
        }
        if (fragment == mLcGnFragment) {//这个是地点和分期中的完成
//            Toast.makeText(PatientInfoActivity.this,"点击了完成",Toast.LENGTH_SHORT).show();
//            LogUtil.i(TAG, "点击了完成");
            if (mPatientResultFragment == null) {
                mPatientResultFragment = new PatientResultFragment();
                mPatientResultFragment.setOnLastStepClickListener(this);
                mPatientResultFragment.setOnNextStepClickListener(this);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            ft.hide(mLcGnFragment).add(R.id.id_content, mPatientResultFragment, "infofragment");
//            ft.addToBackStack(null);
//            getFragmentManager().popBackStack();
            ft.commit();
            fragments.add(mPatientResultFragment);
        }

        if (fragment == mPatientResultFragment) {//最后一步的完成
            new CreateInfoPresenter(this).getData();//调用接口

        }


    }

    @Override
    public void onBackPressed() {
        if (fragments.size() > 1) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            ft.remove(fragments.get(fragments.size() - 1)).show(fragments.get(fragments.size() - 2));
            fragments.get(fragments.size() - 2).getResume();
            fragments.remove(fragments.size() - 1);
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    private File tempFile;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tempFile != null) {
            tempFile.delete();
        }
    }

    /**
     * 这些是填写完，之后赋值的信息
     */
    private String InfoName = GlobalData.mUserNickname;
    private String CancerID;
    private String DiscoverTime;
    private String Headimgurl = null;
    private String Province = "110000";
    private String CureStartTime;
    private String StepID;
    private String City = "110100";
    private String PeriodType ;//1 是数字分期 2 是tnm分期
    private String PeriodID = "0";
    private String IsMedicineValid = "1";

    public void setIsMedicineValid(String isMedicineValid) {
        IsMedicineValid = isMedicineValid;
    }

    public void setHeadimgurl(String headimgurl) {
        Headimgurl = headimgurl;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public void setCureStartTime(String cureStartTime) {
        CureStartTime = cureStartTime;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPeriodType(String periodType) {
        PeriodType = periodType;
    }

    public void setPeriodID(String periodID) {
        PeriodID = periodID;
    }

    public void setDiscoverTime(String discoverTime) {
        DiscoverTime = discoverTime;
    }

    public void setInfoName(String infoName) {
        InfoName = infoName;
    }

    public void setCancerID(String CancerID) {
        this.CancerID = CancerID;
    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            if (!TextUtils.isEmpty(InfoName)) {
                UserinfoData.saveInfoname(this, InfoName);
                map.put(Contants.InfoName, InfoName);//这个是用来判断先产生的用户
            }
            map.put(Contants.CancerID, CancerID);
            map.put(Contants.City, City);
            UserinfoData.saveCityID(this,City);
            if (!TextUtils.isEmpty(PeriodID)) {
                map.put(Contants.PeriodID, PeriodID);
            }
            if (!TextUtils.isEmpty(PeriodType)) {
                map.put(Contants.PeriodType, PeriodType);
            }
            if (!TextUtils.isEmpty(DiscoverTime)) {
                map.put(Contants.DiscoverTime, DiscoverTime);
            } else {
                String time = String.valueOf(System.currentTimeMillis() / 1000);
//                time = time.substring(0, 11);
                map.put(Contants.DiscoverTime, time);
            }
            if (TextUtils.isEmpty(Headimgurl)) {
                map.put(Contants.Headimgurl, "badiu.png");

            } else {
                map.put(Contants.Headimgurl, Headimgurl);
            }
            map.put(Contants.Province, Province);
            UserinfoData.saveProvinceID(this, Province);

            if (!TextUtils.isEmpty(CureStartTime)) {//当前阶段的时间
                map.put(Contants.CureStartTime, CureStartTime);
            } else {
                String time = String.valueOf(System.currentTimeMillis() / 1000);
                map.put(Contants.CureStartTime, time);
            }
            map.put(Contants.StepID, StepID);
            map.put(Contants.IsMedicineValid, IsMedicineValid);

            LogUtil.i(TAG, "创建档案请求的数据是：" + map.toString());
        }
        return map;
    }


    /**
     * 输入的数据不同 返回也不同
     *
     * @param t
     * @param position
     */
    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {
            BaseBean bean = (BaseBean) t;
            LogUtil.i(TAG, bean.toString());
            dialog.dismiss();
            if (Contants.RESULT.equals(bean.iResult)) {
                Toast.makeText(this, R.string.cret_info_sucess, Toast.LENGTH_SHORT).show();
                SharePrefUtil.saveString(this, Contants.InfoName, InfoName);//保存用户名
                SharePrefUtil.saveString(this, Contants.SAVE_INFO_ID, bean.infoid);
                SharePrefUtil.saveBoolean(this, Contants.hasRecord, true);//保存信息说明档案也完成
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(this, MainActivity.class));//这个只是为了测试
                finish();
            }
        }
    }

    private CustomProgressDialog dialog;

    @Override
    public void showLoading(int tag) {
        dialog = CustomProgressDialog.createCustomDialog(this);
        dialog.setMessage("正在创建...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void hideLoading(int tag) {
        dialog.dismiss();

    }

    @Override
    public void showError(int tag) {
        dialog.dismiss();
        LogUtil.i(TAG, "error");
//            Toast.makeText(this, "数据出错", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));//这个只是为了测试
        finish();
    }
}
