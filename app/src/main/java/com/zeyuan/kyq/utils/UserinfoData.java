package com.zeyuan.kyq.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.zeyuan.kyq.bean.PatientDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 * 个人用户信息 内存中没有就去硬盘中拿。
 */
public class UserinfoData {
    private static String StepID;
    public static String performid;//小医生的症状
    public static String planId;//小医生的症状
    private static String discoverTime;
    private static String sInfoID;
    private static String InfoName;
    private static String CancerID;
    private static String CityID;
    private static String ProvinceID;
    private static String PeriodID;
    private static String AvatarUrl;


    public static String getStepID() {
        return StepID;
    }

    /**
     * 得到stepid 内存中没有就去硬盘中拿。
     *
     * @param context
     * @return
     */
    public static String getStepID(Context context) {
        if (TextUtils.isEmpty(StepID)) {
            String stepID = SharePrefUtil.getString(context, Contants.StepID, null);
            if (TextUtils.isEmpty(stepID)) {
                return null;
            } else {
                StepID = stepID;
            }
        }
        return StepID;
    }

    public static void saveStepID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.StepID, data);
        StepID = data;
    }

    public static void saveDiscoverTime(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.DiscoverTime, data);
        discoverTime = data;
    }

    public static String getDiscoverTime(Context context) {
        if (TextUtils.isEmpty(discoverTime)) {
            String stepID = SharePrefUtil.getString(context, Contants.DiscoverTime, null);
            if (TextUtils.isEmpty(stepID)) {
                return null;
            } else {
                discoverTime = stepID;
            }
        }
        return discoverTime;
    }

    //    /**
//     * 得到infoid
//     *
//     * @param context
//     * @return
//     */
//    public static String getInfoID(Context context) {
//        String inforid = SharePrefUtil.getString(context, Contants.SAVE_INFO_ID, null);
//        if (TextUtils.isEmpty(inforid)) {
//            return null;
//        }
//        return inforid;
////        return "9270";
//    }
//ggz+

    public static String getInfoID() {
        return sInfoID;
    }
    //~

    /**
     * 得到infoid
     *
     * @param context
     * @return
     */
    public static String getInfoID(Context context) {
        //ggz+
        if (TextUtils.isEmpty(sInfoID)) {
            String inforid = SharePrefUtil.getString(context, Contants.SAVE_INFO_ID, null);
            if (TextUtils.isEmpty(inforid)) {
                sInfoID = "9840";//为了小m的测试
            } else {
                sInfoID = inforid;
            }
        }
        return sInfoID;
        //~
    }
//    /**
//     * 保存是否创建过
//     * @param context
//     * @param data
//     */
//    public static void saveRecord(Context context, String data) {
//        SharePrefUtil.saveString(context, Contants.hasRecord, data);
//
//    }

//    public static String getRecord(Context context) {
//        String cancerID = SharePrefUtil.getString(context, Contants.hasRecord, null);
//        if (TextUtils.isEmpty(cancerID)) {
//            return null;
//        }
//        return cancerID;
//    }


    public static void saveInfoID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.SAVE_INFO_ID, data);
        sInfoID = data;
    }


    public static void saveInfoname(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.InfoName, data);
        InfoName = data;
    }


    public static String getInfoname(Context context) {
        if (TextUtils.isEmpty(InfoName)) {
            String cancerID = SharePrefUtil.getString(context, Contants.InfoName, null);
            if (TextUtils.isEmpty(cancerID)) {
                return null;
            } else {
                InfoName = cancerID;
            }
        }
        return InfoName;
    }

    /**
     * 这个是qq登录还是微信登录
     *
     * @param context
     * @param data
     */
    public static void saveType(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.type, data);

    }

    public static String getType(Context context) {
        String cancerID = SharePrefUtil.getString(context, Contants.type, null);
        if (TextUtils.isEmpty(cancerID)) {
            return null;
        }
        return cancerID;
    }


    public static String getCancerID(Context context) {
        if (TextUtils.isEmpty(CancerID)) {
            String cancerID = SharePrefUtil.getString(context, Contants.CancerID, null);
            if (TextUtils.isEmpty(cancerID)) {
                return null;
            } else {
                CancerID = cancerID;
            }
        }

        return CancerID;
    }

    public static void saveCancerID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.CancerID, data);
        CancerID = data;
    }


    public static String getCityID(Context context) {
        if (TextUtils.isEmpty(CityID)) {
            String cityID = SharePrefUtil.getString(context, Contants.CityID, null);
            if (TextUtils.isEmpty(cityID)) {
                return "0";
            } else {
                CityID = cityID;
            }
        }
        return CityID;
    }

    public static void saveCityID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.CityID, data);
        CityID = data;
    }


    public static String getProvinceID(Context context) {
        if (TextUtils.isEmpty(ProvinceID)) {
            String provinceID = SharePrefUtil.getString(context, Contants.ProvinceID, null);
            if (TextUtils.isEmpty(provinceID)) {
                return null;
            } else {
                ProvinceID = provinceID;
            }
        }
        return ProvinceID;
    }

    public static void saveProvinceID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.ProvinceID, data);
        ProvinceID = data;
    }


    public static String getPeriodID(Context context) {
        if (TextUtils.isEmpty(PeriodID)) {
            String provinceID = SharePrefUtil.getString(context, Contants.PeriodID, null);
            if (TextUtils.isEmpty(provinceID)) {
                return "0";
            } else {
                PeriodID = provinceID;
            }
        }
        return PeriodID;
    }

    public static void savePeriodID(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.PeriodID, data);
        PeriodID = data;
    }

    public static void saveAvatarUrl(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.Avatar, data);
        AvatarUrl = data;
    }


    public static String getAvatarUrl(Context context) {
        if (TextUtils.isEmpty(AvatarUrl)) {
            String avatarUrl = SharePrefUtil.getString(context, Contants.Avatar, "");
            if (TextUtils.isEmpty(avatarUrl)) {
                return "";
            } else {
                AvatarUrl = avatarUrl;
            }
        }
        return AvatarUrl;
    }

    /**
     * 保存关注的圈子
     *
     * @param context
     * @param list
     */
    public static void saveFocusCirlce(Context context, List<String> list) {

        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                sb.append("," + str);
            }
        }
        SharePrefUtil.saveString(context, Contants.CircleID, sb.toString());
    }

    private static List<String> followCirlces;

    /**
     * 得到关注的圈子
     *
     * @param context
     * @return
     */
    public static List<String> getFocusCircle(Context context) {

        List<String> list = new ArrayList<>();
        String temp = SharePrefUtil.getString(context, Contants.CircleID, null);
        if (TextUtils.isEmpty(temp)) {
            return null;
        }
        String[] strings = temp.split(",");
        for (String str : strings) {
            list.add(str);
        }
        return list;
    }

    public static void removeFocusCircle(Context context, String circleId) {
        List<String> focusCircle = getFocusCircle(context);
        if (!focusCircle.contains(circleId)) {
            return;
        }
//        int index = focusCircle.indexOf(circleId);
        focusCircle.remove(circleId);
        saveFocusCirlce(context, focusCircle);
    }


    public static void addFocusCircle(Context context, String circleId) {
        List<String> focusCircle = getFocusCircle(context);
        focusCircle.add(circleId);
        saveFocusCirlce(context, focusCircle);
    }


    public static void saveRemindTime(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        SharePrefUtil.saveString(context, Contants.RemindTime, data);
    }

    public static String getRemindTime(Context context) {
        String avatarUrl = SharePrefUtil.getString(context, Contants.RemindTime, "");
        if (TextUtils.isEmpty(avatarUrl)) {
            return "";
        }
        return avatarUrl;
    }

    private static final long day_mills = 24 * 60 * 60 * 1000;//一天的毫秒数

    public static boolean compareTime(Context context) {
        String remindTime = getRemindTime(context);
        if (TextUtils.isEmpty(remindTime)) {
            return true;
        }
        long saveTime = Long.valueOf(remindTime);
        if (System.currentTimeMillis() - saveTime >= day_mills) {
            return true;
        }
        return false;
    }


    public static void saveUserData(Context context, PatientDetailBean patientDetailBean) {

        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
        UserinfoData.saveStepID(context, patientDetailBean.getStepID());
        UserinfoData.saveCancerID(context, patientDetailBean.getCancerID());
        UserinfoData.saveDiscoverTime(context, patientDetailBean.getDiscoverTime());
        UserinfoData.savePeriodID(context, patientDetailBean.getPeriodID());
        UserinfoData.saveAvatarUrl(context, patientDetailBean.getHeadimgurl());
        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
        UserinfoData.saveProvinceID(context, patientDetailBean.getProvince());
        UserinfoData.saveCityID(context, patientDetailBean.getCity());
    }


    public static void clearMermory(Context context) {
        StepID = null;
        planId = null;
        performid = null;
        sInfoID = null;
        discoverTime = null;
        InfoName = null;
        CancerID = null;
        CityID = null;
        ProvinceID = null;
        PeriodID = null;
        AvatarUrl = null;
        SharePrefUtil.cleanData(context);
    }


    public final static void saveFirstGuide(Context context) {
        SharePrefUtil.saveBoolean(context, "first_guide", true);
    }

    public final static boolean getFirstGuide(Context context) {
       return SharePrefUtil.getBoolean(context, "first_guide", false);
    }
}
