package com.zeyuan.kyq.app;

import android.text.TextUtils;

import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.http.bean.UserStepChildComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 * 阶段帮助类
 */
public class GlobalUserStepData {
    private static List<UserStepBean> sUserStepList;//=new ArrayList<>()

    public static void setUserStepList(List<UserStepBean> userStepList) {
        sUserStepList = userStepList;
        if (userStepList == null)
            userStepList = new ArrayList<>();
    }

    public static List<UserStepBean> getUserStepList() {
        return sUserStepList;
    }

    public static UserStepBean setUserStepDetails(String userStepId, List<UserStepChildBean> userStepChildList) {

        UserStepBean userStep = getUserStepById(userStepId);
        if (userStep != null) {
            if (userStepChildList != null) {
                Collections.sort(userStepChildList, new UserStepChildComparator(true));
            }
            userStep.setChildList(userStepChildList);
            return userStep;
        }
        return null;//
    }

    public static UserStepBean getUserStepById(String userStepId) {
        if (sUserStepList != null && !sUserStepList.isEmpty()) {
            UserStepBean temp = null;
            for (int i = 0; i < sUserStepList.size(); i++) {
                temp = sUserStepList.get(i);
                if ((!TextUtils.isEmpty(temp.getStepUID())) && temp.getStepUID().equals(userStepId)) {
                    return temp;
                }
            }
        }
        return null;
    }

    public static UserStepChildBean getUserStepSymptById(String userStepId, String userSymptId) {
        if (sUserStepList != null && !sUserStepList.isEmpty()) {
            UserStepBean userStep = getUserStepById(userStepId);
            if (userStep != null) {
                List<UserStepChildBean> lstChild = userStep.getChildList();
                if (lstChild != null && !lstChild.isEmpty()) {
                    UserStepChildBean temp;
                    UserStepChildBean temp1;
                    for (int i = 0; i < lstChild.size(); i++) {
                        temp = lstChild.get(i);
                        if (UserStepChildBean.PERFORM_OR_QUOTA__SYMPT == temp.getPerformORQuota()) {
                            temp1 = (UserStepChildBean) temp;
                            if (temp1.getStepDetailPerformID().equals(userSymptId)) {
                                return temp1;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static UserStepChildBean getUserStepQuotaById(String userStepId, String userQuotaId) {
        if (sUserStepList != null && !sUserStepList.isEmpty()) {
            UserStepBean userStep = getUserStepById(userStepId);
            if (userStep != null) {
                List<UserStepChildBean> lstChild = userStep.getChildList();
                if (lstChild != null && !lstChild.isEmpty()) {
                    UserStepChildBean temp;
                    UserStepChildBean temp1;
                    for (int i = 0; i < lstChild.size(); i++) {
                        temp = lstChild.get(i);
                        if (UserStepChildBean.PERFORM_OR_QUOTA__QUOTA == temp.getPerformORQuota()) {
                            temp1 = (UserStepChildBean) temp;
                            if (temp1.getQuotaID().equals(userQuotaId)) {
                                return temp1;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    public static UserStepChildBean updateUserSympt(String userStepId, UserStepChildBean bean) {
        UserStepChildBean result = getUserStepSymptById(userStepId, bean.getStepDetailPerformID());
        if (result != null) {
            result.setRecordTime(bean.getRecordTime());
            result.setPerformORQuota(bean.getPerformORQuota());
            result.setPerformStep(bean.getPerformStep());
            result.setRemark(bean.getRemark());
            result.setStepDetailPerformID(bean.getStepDetailPerformID());

//            try {
//                PropertyUtils.copyProperties(result, bean);
//                return result;
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }

    public static UserStepChildBean updateUserQuota(String userStepId, UserStepChildBean bean) {
        UserStepChildBean result = getUserStepQuotaById(userStepId, bean.getQuotaID());
        if (result != null) {
            result.setCEA(bean.getCEA());
            result.setMasterCancerLength(bean.getMasterCancerLength());
            result.setMasterCancerName(bean.getMasterCancerName());
            result.setMasterCancerWidth(bean.getMasterCancerWidth());
            result.setQuotaID(bean.getQuotaID());
            result.setTransferPos(bean.getTransferPos());
            result.setPerformORQuota(bean.getPerformORQuota());
            result.setRecordTime(bean.getRecordTime());
            result.setSlaverCancerNum(bean.getSlaverCancerNum());
//            try {
//                PropertyUtils.copyProperties(result, bean);
//                return result;
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }


    public static void updateUsetStep(List<UserStepBean> beans) {
        if (beans != null && beans.size() > 0) {
            UserStepBean temp0;
            UserStepBean temp1;
            for (int i = 0; i < beans.size(); i++) {
                temp0 = beans.get(i);
                temp1 = getUserStepById(temp0.getStepUID());
                if (temp1 != null) {
                    temp1.setIsMedicineValid(temp0.getIsMedicineValid());
                    temp1.setStepID(temp0.getStepID());
                    if (temp0.isBegTimeFirst()) {
                        temp1.setBeginTime(Long.parseLong(UserStepBean.TIME_FIRST));
                    } else {
                        temp1.setBeginTime(temp0.getCompareDateBeg());
                    }
                    if (temp0.isEndTimeLast()) {
                        temp1.setEndTime(Long.parseLong(UserStepBean.TIME_LAST));
                    } else {
                        temp1.setEndTime(temp0.getCompareDateEnd());
                    }
                    temp1.setStepUID(temp0.getStepUID());

//                    temp1.setChildList(temp0.getChildList());

//                    try {
//                        PropertyUtils.copyProperties(temp1, temp0);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }

    public static void deleteUserStep(String userStepId) {
        UserStepBean item = getUserStepById(userStepId);
        sUserStepList.remove(item);
    }

    public static void deleteUserQuota(String userStepId, UserStepChildBean bean) {
        UserStepBean step = getUserStepById(userStepId);
        UserStepChildBean quota = getUserStepQuotaById(userStepId, bean.getQuotaID());
        step.getChildList().remove(quota);
    }

    public static void deleteUserSympt(String userStepId, UserStepChildBean bean) {
        UserStepBean step = getUserStepById(userStepId);
        UserStepChildBean sympt = getUserStepSymptById(userStepId, bean.getStepDetailPerformID());
        step.getChildList().remove(sympt);
    }


    /**
     * 从内存数据中找到最新阶段
     *
     * @return
     */
    public static UserStepBean findLastUserStep() {
        if (sUserStepList == null || sUserStepList.isEmpty())
            return null;

        int size = sUserStepList.size();
        UserStepBean temp;
        for (int i = 0; i < size; i++) {
            temp = sUserStepList.get(i);
            if (temp.isEndTimeLast()) {
                return temp;
            }
        }
        return null;
    }
}
