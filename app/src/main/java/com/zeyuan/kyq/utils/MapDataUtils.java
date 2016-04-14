package com.zeyuan.kyq.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zeyuan.kyq.app.GlobalData;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MapDataUtils {
    public static final String BXY_ID = "4205";//靶向药id
    public static final String HLY_ID = "4206";//化疗药id

    /**
     * 这个是给的联合用药的id 显示对应的值
     *
     * @param id
     * @return
     */
    public static String getStepUionName(String id) {
//        List<String> stepList = GlobalData.stepUinon.get(id);
//        if (stepList == null || stepList.size() == 0) {
//            return null;
//        }
//        StringBuilder temp = new StringBuilder();
//        for (String item : stepList) {
//            String string = GlobalData.cureValues.get(item);
//            if (temp.length() == 0) {//第一次添加 不加加号
//                temp.append(string);
//            } else {
//                temp.append("+" + string);
//            }
//        }
//        return temp.toString();
        String string = GlobalData.cureValues.get(id);
        return string;
    }


    /**
     * 根据后台给的stepid 来显示药物
     *
     * @param id
     * @return
     */
    public static String getStepName(String id) {
        return GlobalData.cureValues.get(id);
    }

    /**
     * 后台给的是联合用药的id 和 药物的id组合在一起
     *
     * @param id
     * @return
     */
    public static String getAllStepName(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        String temp = getStepName(id);
        if (TextUtils.isEmpty(temp)) {
            return getStepUionName(id);
        }
        return temp;
    }


//    /**
//     * 得到药物的cureconfid
//     * 4205 为靶向药  4206为化疗药
//     *
//     * @param id
//     * @return
//     */
//    public static String getcureconfId(String id) {
//        String cureconfID = GlobalData.cureId.get(id);
//        return cureconfID;
//    }


    /**
     * 是否为靶向药
     *
     * @param id 药物和这联合用药的id
     * @return true 为靶向药 false不是靶向药
     */
    public static boolean isBXY(String id) {
        if (BXY_ID.equals(getAllCureconfID(id))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据症状的id 得到对应的症状
     *
     * @param id
     * @return
     */
    public static String getPerform(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        return GlobalData.performValues.get(id);
    }

    public static String getCityValue(String id) {

        return GlobalData.cityValues.get(id);
    }

    /**
     * 根据药物id 获取 cureconfid
     *
     * @param id
     * @return
     */
    public static String getAllCureconfID(String id) {
        String stepCureid = GlobalData.cureId.get(id);
        if (!TextUtils.isEmpty(stepCureid)) {
            return stepCureid;
        }
        return "";
    }

    /**
     * 得到治疗方案的值
     *
     * @param id
     * @return
     */
    public static String getCureValues(String id) {
        return GlobalData.cureValues.get(id);
    }

    /**
     * 得到圈子的值
     *
     * @param id
     * @return
     */
    public static String getCircleValues(String id) {
        return GlobalData.circleValues.get(id);
    }


    public static LinkedHashMap getBodyPos() {
        return GlobalData.bodyPos;
    }

//    public static String getBodyValues(String id) {
////        ZYApplication.performValues.get(id);
//        return ZYApplication.performValues.get(id);
//    }

//    public static String getDigitValues(String id ) {
//        if (TextUtils.isEmpty(id)) {
//            return null;
//        }
//        return GlobalData.DigitValues.get(id);
//    }


    public static String showTransData(String data) {
//        String[] strings = data.split(",");
//        StringBuilder sb = new StringBuilder();
//        if (strings.length > 0) {
//            for (String str : strings) {
//                if (sb.length() == 0) {
//                    sb.append(GlobalData.transferpos.get(str));
//                }else {
//                    sb.append("," + GlobalData.transferpos.get(str));
//                }
//            }
//        }
//        return sb.toString();
        return getMapValuesStr(GlobalData.transferpos, data);
    }


    public static String getDigitValues(String id) {
        return GlobalData.DigitValues.get(id);
    }


    public static String getOtherStepValues(String id) {
        return GlobalData.otherStep.get(id);
    }

    public static String getPerfromValues(String ids) {
        return getMapValuesStr(GlobalData.performValues, ids);
    }

    /**
     * 把由许多id组成的由逗号分开的项， 转换成显示的
     * 如 556,557 转换成 易瑞沙，特罗凯
     *
     * @param map    id 对用 的values
     * @param idsStr 由逗号分隔id 的字符串
     * @return 显示由逗号隔开的字符串
     */
    public static String getMapValuesStr(Map<String, String> map, String idsStr) {
        if (TextUtils.isEmpty(idsStr)) {
            return "";
        }
        String[] strings = idsStr.split(",");
        StringBuilder sb = new StringBuilder();
        if (strings.length > 0) {
            for (String str : strings) {
                if (sb.length() == 0) {
                    sb.append(map.get(str));
                } else {
                    sb.append("," + map.get(str));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 把list转换成由逗号隔开的string
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                if (sb.length() > 0) {
//                    break;
                    sb.append(",");
                }
//                else {
                sb.append(str);
//                }
            }
        }


        return sb.toString();
    }


    public static String getTransPosValues(String ids) {
        return getMapValuesStr(GlobalData.transferpos, ids);
    }

    public static String getCancerValues(String ids) {
        return getMapValuesStr(GlobalData.cancerValues, ids);
    }


}
