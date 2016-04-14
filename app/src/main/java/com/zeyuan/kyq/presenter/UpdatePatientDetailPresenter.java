package com.zeyuan.kyq.presenter;

import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.OkHttpClientManager;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.PatientDetailActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 20NetNumber.UPDATE_PATIENT5/NetNumber.UPDATE_PATIENT0/NetNumber.UPDATE_PATIENT6.
 * 更行阶段详情
 */
public class UpdatePatientDetailPresenter {


    private static final int AVATAR_CHANGE = 1 << 0;//头像修改
    private static final int NAME_CHANGE = 1 << 1;//姓名
    private static final int SEX_CHANGE = 1 << 2;//性别
    private static final int AGE_CHANGE = 1 << 3;//age
    private static final int LOCATION_CHANGE = 1 << 4;//location
    private static final int CANCER_TYPE_CHANGE = 1 << 5;//cancer_type
    private static final int CANCER_POS = 1 << 6;//转移

    private static final int GENE_CHANGE = 1 << 7;//gene
    private static final int DISCOVER_TIME_CHANGE = 1 << 8;//discoverTime
    private static final int TNM_CHANGE = 1 << 9;//period type he id 分期

    private GetDataBiz biz;
    private ViewDataListener viewDataListener;
    PatientDetailBean bean;

    public UpdatePatientDetailPresenter(PatientDetailBean bean, ViewDataListener viewDataListener) {
        this.bean = bean;
        biz = new GetDataBiz();
        this.viewDataListener = viewDataListener;
    }

    public void getData() {
//        viewDataListener.showLoading(NetNumber.UPDATE_PATIENT);
        biz.getData(initData(bean), Contants.UPDATE_PATIENT_DETAIL, new OkHttpClientManager.ResultCallback<BaseBean>() {
            @Override
            public void onError(Request request, Exception e) {
//                viewDataListener.showError(NetNumber.UPDATE_PATIENT);
            }

            @Override
            public void onResponse(BaseBean response) {
                viewDataListener.toActivity(response, NetNumber.UPDATE_PATIENT);
//                viewDataListener.hideLoading(NetNumber.UPDATE_PATIENT);
            }
        });
    }


//    private String InfoName;
//    private String Headimgurl;
//    private String CancerID;
//    private String TransferPos;
//    private String Gene;
//    private String DiscoverTime;
//    private String Sex;//0是nv NetNumber.UPDATE_PATIENT是男
//    private String Age;
//    private String City;
//    private String ProvinceID;
//    private String digitT;
//    private String digitN;
//    private String digitM;
//    private String Digit;
//    private String PeriodType;//NetNumber.UPDATE_PATIENT 是数字分期 2 是tnm分期
//    private String PeriodID;

    private int setBit = 0;//这个表示那个数据项目修改了。便于使后台少做一次查询

    private Map initData(PatientDetailBean bean) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID());
        String cancerid = bean.getCancerID();
        if (!TextUtils.isEmpty(cancerid)) {//修改癌种后，提交未知的分期和基因突变的信息（0）
            setBit += CANCER_TYPE_CHANGE;
            map.put(Contants.CancerID, cancerid);
            String gene = bean.getGene();
            if (TextUtils.isEmpty(gene)) {
                bean.setGene("0");
            }
            String PeriodID = bean.getPeriodID();
            if (TextUtils.isEmpty(PeriodID)) {
                bean.setPeriodID("0");
            }
            String PeriodType = bean.getPeriodType();
            if (TextUtils.isEmpty(PeriodType)) {
                bean.setPeriodType("1");
            }
        }
        String InfoName = bean.getInfoName();
        if (!TextUtils.isEmpty(InfoName)) {
            map.put(Contants.InfoName, InfoName);
            setBit += NAME_CHANGE;
        }
        String Headimgurl = bean.getHeadimgurl();
        if (!TextUtils.isEmpty(Headimgurl)) {
            setBit += AVATAR_CHANGE;
//            LogUtil.i(TAG, Headimgurl + "图片为空么");
            map.put(Contants.Headimgurl, Headimgurl);
        }
        String TransferPos = bean.getTransferPos();
        if (!TextUtils.isEmpty(TransferPos)) {
            setBit += CANCER_POS;
            map.put(Contants.TransferPos, TransferPos);
        }
        String Gene = bean.getGene();
        if (!TextUtils.isEmpty(Gene)) {
            setBit += GENE_CHANGE;
            map.put(Contants.Gene, Gene);
        }
        String DiscoverTime = bean.getDiscoverTime();
        if (!TextUtils.isEmpty(DiscoverTime)) {
            setBit += DISCOVER_TIME_CHANGE;
            map.put(Contants.DiscoverTime, DiscoverTime);
        }
        String Sex = bean.getSex();
        if (!TextUtils.isEmpty(Sex)) {
            setBit += SEX_CHANGE;
            map.put(Contants.Sex, Sex);
        }
        String Age = bean.getAge();
        if (!TextUtils.isEmpty(Age)) {
            setBit += AGE_CHANGE;
            map.put(Contants.Age, Age);
        }
        String City = bean.getCity();
        String ProvinceID = bean.getProvince();
        if (!TextUtils.isEmpty(City)) {
            setBit += LOCATION_CHANGE;
//            UserinfoData.saveCityID(this, City);
            //保存city 和provinceid 在toActivity中保存
            map.put(Contants.City, City);
            map.put(Contants.ProvinceID, ProvinceID);
        }
        String PeriodID = bean.getPeriodID();
        String PeriodType = bean.getPeriodType();
        if (!TextUtils.isEmpty(PeriodID) && !TextUtils.isEmpty(PeriodType)) {
            setBit += TNM_CHANGE;
            map.put(Contants.PeriodID, PeriodID);
            map.put(Contants.PeriodType, PeriodType);
        }
        map.put(Contants.SetBit, "" + setBit);//这个用来标记 是哪些项目修改了
        return map;
    }

}
