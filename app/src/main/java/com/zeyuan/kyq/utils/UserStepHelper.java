package com.zeyuan.kyq.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.app.GlobalUserStepData;
import com.zeyuan.kyq.bean.AllStepBean;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.http.RespUserStepAdd;
import com.zeyuan.kyq.http.RespUserStepChildAdd;
import com.zeyuan.kyq.http.RespUserStepModify;
import com.zeyuan.kyq.http.bean.UserStepComparator;
import com.zeyuan.kyq.presenter.GetDataBiz;
import com.zeyuan.kyq.http.RespBase;
import com.zeyuan.kyq.http.RespGetAllStep;
import com.zeyuan.kyq.http.RespGetStepDetal;
import com.zeyuan.kyq.http.RespRunnable;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.view.ViewDataListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 * 封装服务器和阶段相关的各个接口
 */
public class UserStepHelper {
    //    private static int CODE_SEED = 0;
    public static final int ReqCode_UserStepAll = 1;
    public static final int ReqCode_UserStepDetails = 2;

    public static final int ReqCode_UserStepAdd = 3;
    public static final int ReqCode_UserStepMdf0 = 4;
    public static final int ReqCode_UserStepMdf1ConfirmDelSpace = 41;//更新确认，需要删除空窗期
    public static final int ReqCode_UserStepMdf2ConfirmDelChild = 42;//更新确认，更新时阶段内容需要被删除
    public static final int ReqCode_UserStepDel = 5;

    public static final int ReqCode_UserQuotaAdd = 6;
    public static final int ReqCode_UserQuotaMdf = 7;
    public static final int ReqCode_UserQuotaDel = 8;

    public static final int ReqCode_UserSymptAdd = 9;
    public static final int ReqCode_UserSymptMdf = 10;
    public static final int ReqCode_UserSymptDel = 11;


//    /**
//     * 请求服务器接口通用处理方法
//     *
//     * @param reqCode
//     * @param url
//     * @param map
//     * @param t
//     * @param resp
//     * @param <T>
//     */
//    private static <T> void reqSvrData(final int reqCode, String url, HashMap<String, String> map, T t, final RespRunnable resp, final ViewDataListener listener) {//,Comparator<? super T>
//        listener.showLoading(reqCode);
//        mGetDataBiz.getData(map, url, new OkHttpClientManager.ResultCallback<T>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                if (listener != null) {
//                    listener.hideLoading(reqCode);
//                    listener.showError(-1);
//                }
//            }
//
//            @Override
//            public void onResponse(T response) {
//                if (listener != null) {
//                    listener.hideLoading(reqCode);
//                }
//                resp.onResp(reqCode, response);
//            }
//        });
//    }

    private static GetDataBiz mGetDataBiz = new GetDataBiz();
    private static String mInfoID = UserinfoData.getInfoID();


    public static void setmInfoID(String mInfoID) {
        UserStepHelper.mInfoID = mInfoID;
    }

    public static abstract class OKResultCallback<T> extends OkHttpClientManager.ResultCallback {
        private ViewDataListener listener;
        private int reqCode;

        public OKResultCallback(int reqCode, ViewDataListener listener) {
            this.listener = listener;
            this.reqCode = reqCode;
        }

        public void showLoading() {
            listener.showLoading(reqCode);
        }

        @Override
        public void onError(Request request, Exception e) {
            if (listener != null) {
                listener.hideLoading(reqCode);
                listener.showError(-1);
            }
        }

        @Override
        public void onResponse(Object response) {
            if (listener != null) {
                listener.hideLoading(reqCode);
            }
            onResp(reqCode, (T) response);
        }

        public abstract void onResp(int reqCode, T resp);
    }

    /**
     * 请求服务器接口通用处理方法
     *
     * @param url
     * @param map
     * @param callback
     */
    private static void reqSvrData(String url, HashMap<String, String> map, final OKResultCallback callback) {//,Comparator<? super T>
        callback.showLoading();
        mGetDataBiz.getData(map, url, callback);
    }

    public static void safeMapPut(HashMap<String, String> map, String key, String val) {
        if (!TextUtils.isEmpty(val)) {
            map.put(key, val);
        }
    }

    /**
     * 请求获取用户所有阶段
     * OK
     */
    public static void reqUserStepAll(final ViewDataListener listener) {//RespGetAllStep

        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
        }

        reqSvrData(Contants.UserStep.GET_ALL_STEP, map, new OKResultCallback<RespGetAllStep>(ReqCode_UserStepAll, listener) {
            @Override
            public void onResp(int reqCode, RespGetAllStep resp) {
                if (resp.isOK()) {
                    //ggz:w
                    GlobalUserStepData.setUserStepList(resp.getStepNum());
                    listenerToActivity(listener, GlobalUserStepData.getUserStepList(), reqCode);
                }
            }
        });
    }

    /**
     * 请求获取用户阶段详情（所有指标和症状）
     * OK
     *
     * @param userStepId
     */
    public static void reqUserStepDetails(final ViewDataListener listener, final String userStepId) {

        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
            safeMapPut(map, "StepUID", userStepId);
        }
        reqSvrData(Contants.UserStep.GET_STEP_DETAIL, map, new OKResultCallback<RespGetStepDetal>(ReqCode_UserStepDetails, listener) {
            @Override
            public void onResp(int reqCode, RespGetStepDetal resp) {
                if (resp.isOK()) {
                    UserStepBean userStep = GlobalUserStepData.setUserStepDetails(userStepId, resp.getRecordNum());
                    listenerToActivity(listener, userStep, reqCode);
                }
            }
        });
    }

//    /**
//     * 验证要添加的阶段数据
//     *
//     * @param lstSrc
//     * @param beans
//     * @return 为null时表示验证通过，其他为验证错误消息
//     */
//    private static String validUserStepAdd(List<UserStepBean> lstSrc, final List<UserStepBean> beans) {
//
//
//        if (beans == null || beans.size() == 0) {
//            return "没有要添加的数据！";
//
//        }
//
//
//        if ((lstSrc == null || lstSrc.isEmpty())) {
//            if (beans.size() == 1) {
//                return null;//不用验证就是OK的
//            }
//        }
//
//        List<UserStepBean> lstDst = new ArrayList<>();
//        if ((lstSrc != null && !lstSrc.isEmpty())) {
//            UserStepBean temp = null;
//            int sizeSrc = lstSrc.size();
//            for (int i = 0; i < sizeSrc; i++) {
//                temp = lstSrc.get(i);
//                if (!TextUtils.isEmpty(temp.getStepID())) {
//                    lstDst.add(temp);
//                }
//            }
//        }
//
//        UserStepBean temp = null;
//        for (int i = 0; i < beans.size(); i++) {
//            temp = beans.get(i);
//
//            if (temp.getCompareDateBeg() >= temp.getCompareDateEnd()) {
//                return "开始日期必须早于结束日期";
//            }
//            //lstDst.add(temp);
//        }
//
//        Collections.sort(lstDst, new UserStepComparator());
//
//        UserStepBean temp0 = null;
//        UserStepBean temp1 = null;
//        int sizeDst = lstDst.size();
//        for (int i = 1; i < sizeDst; i++) {
//
//            temp0 = lstDst.get(i - 1);
//            temp1 = lstDst.get(i);
//            if (temp0.getCompareDateEnd() >= temp1.getCompareDateBeg()) {
//                if (temp0.$isEditor() || temp0.$isChanged()) {
//                    return "阶段所选时间与第" + temp1.$number() + "阶段冲突，请重新修改";
//                } else {
//                    return "阶段所选时间与第" + temp0.$number() + "阶段冲突，请重新修改";
//                }
//
////                return "阶段" + temp0.$number() + "和阶段" + temp1.$number() + "时间冲突";
////                return "阶段所选时间与第" + temp1.$number() + "阶段冲突，请重新修改";
//
////                return "阶段时间冲突";
//            }
//        }
//        return null;
//    }

    /**
     * 验证要添加的阶段数据
     *
     * @param lstSrc
     * @param beans
     * @return 为null时表示验证通过，其他为验证错误消息
     */
    public static ValidUserStepModify validUserStepAdd(List<UserStepBean> lstSrc, final List<UserStepBean> beans) {
        ValidUserStepModify result = new ValidUserStepModify();

        if (beans == null || beans.size() == 0) {
            return result.error("没有要添加的数据！");
        }


        if ((lstSrc == null || lstSrc.isEmpty())) {
            if (beans.size() == 1) {
                return result.pass();//不用验证就是OK的
            }
        }

        List<UserStepBean> lstDst = new ArrayList<>();
        if ((lstSrc != null && !lstSrc.isEmpty())) {
            UserStepBean temp = null;
            int sizeSrc = lstSrc.size();
            for (int i = 0; i < sizeSrc; i++) {
                temp = lstSrc.get(i);
                if (!TextUtils.isEmpty(temp.getStepID())) {
                    lstDst.add(temp);
                }
            }
        }

        UserStepBean temp = null;
        for (int i = 0; i < beans.size(); i++) {
            temp = beans.get(i);

            if (temp.getCompareDateBeg() >= temp.getCompareDateEnd()) {
                return result.error("开始日期必须早于结束日期");
            }
            //lstDst.add(temp);
        }

        Collections.sort(lstDst, new UserStepComparator());

        UserStepBean temp0 = null;
        UserStepBean temp1 = null;
//        List<UserStepBean> lstClashReali = new ArrayList<>();
        result.mNeedDelSpace = new ArrayList<>();
        int sizeDst = lstDst.size();
        for (int i = 1; i < sizeDst; i++) {

            temp0 = lstDst.get(i - 1);
            temp1 = lstDst.get(i);
            if (temp0.getCompareDateEnd() >= temp1.getCompareDateBeg()) {

                if (temp0.$isEditor() || temp0.$isChanged()) {
                    temp = temp1;
                } else {
                    temp = temp0;
                }

                if (temp.isSpace()) {
                    if (!result.mNeedDelSpace.contains(temp)) {
                        result.mNeedDelSpace.add(temp);
                    }
                } else {
                    return result.error("阶段所选时间与第" + temp.$number() + "阶段冲突，请重新修改");
                }
            }
        }
        if (result.mNeedDelSpace.isEmpty()) {
            return result.pass();
        }
        return result;
    }

    public static ValidUserStepModify validUserStepModify(List<UserStepBean> lstBeanAll) {

        ValidUserStepModify result = new ValidUserStepModify();

        if (lstBeanAll == null || lstBeanAll.isEmpty()) {
            return result.error("没有要更新的数据！");
        }

        int size = lstBeanAll.size();

        UserStepBean temp = null;
        //1.验证起始时间是否小于等于结束时间
        for (int i = 0; i < size; i++) {
            temp = lstBeanAll.get(i);

            if (temp.getCompareDateBeg() >= temp.getCompareDateEnd()) {
                return result.error("开始日期必须早于结束日期");
            }
        }

        if (size == 1) {
            return result.pass();
        }

        List<UserStepBean> lstBeanSpace = new ArrayList<UserStepBean>();
        List<UserStepBean> lstBeanReali = new ArrayList<UserStepBean>();
        for (int i = 0; i < size; i++) {
            temp = lstBeanAll.get(i);
            if (temp.isSpace()) {
                lstBeanSpace.add(temp);
            } else {
                lstBeanReali.add(temp);
            }
        }

        //2.验证用户阶段时间有木有重叠（暂时不考虑空窗期）
        int sizeReali = lstBeanReali.size();
        if (sizeReali > 1) {
            Collections.sort(lstBeanReali, new UserStepComparator());
            UserStepBean temp0 = null;
            UserStepBean temp1 = null;
            for (int i = 1; i < sizeReali; i++) {

                temp0 = lstBeanReali.get(i - 1);
                temp1 = lstBeanReali.get(i);
                if (temp0.getCompareDateEnd() > temp1.getCompareDateBeg()) {
                    if (temp0.$isEditor() || temp0.$isChanged()) {
                        return result.error("阶段所选时间与第" + temp1.$number() + "阶段冲突，请重新修改");
                    } else {
                        return result.error("阶段所选时间与第" + temp0.$number() + "阶段冲突，请重新修改");
                    }
//                    return result.error("阶段时间冲突");
//                    return result.error("阶段" + temp0.$number() + "和阶段" + temp1.$number() + "时间冲突");
                }
            }
        }


        List<UserStepBean> lstConflictSpace = new ArrayList<UserStepBean>();
        //3.判断哪些空窗期和用户阶段时间冲突(记录需要删除的空窗期)
        int sizeSpace = lstBeanSpace.size();
        UserStepBean space = null;
        UserStepBean reali = null;
        for (int iSpace = 0; iSpace < sizeSpace; iSpace++) {
            space = lstBeanSpace.get(iSpace);
            for (int iReali = 0; iReali < sizeReali; iReali++) {
                reali = lstBeanReali.get(iReali);
//                if ((space.getCompareDateBeg() > reali.getCompareDateBeg() && space.getCompareDateBeg() < reali.getCompareDateEnd())
//                        || (space.getCompareDateEnd() > reali.getCompareDateBeg() && space.getCompareDateEnd() < reali.getCompareDateEnd())) {
                if ((reali.getCompareDateBeg() >= space.getCompareDateBeg() && reali.getCompareDateBeg() <= space.getCompareDateEnd())
                        || (reali.getCompareDateEnd() >= space.getCompareDateBeg() && reali.getCompareDateEnd() <= space.getCompareDateEnd())) {
                    lstConflictSpace.add(space);
                }
            }
        }

        result.mNeedDelSpace = lstConflictSpace;

        return result.pass();
    }

    public static class TimeSlot {

        public final static long TIME_FIRST = 1;
        public final static long TIME_LAST = 0;
        public boolean isValid;
        public UserStepBean userStep;
        public long begTime;
        public long endTime;

        public TimeSlot(long begTime, long endTime) {
            this.begTime = begTime;
            this.endTime = endTime;
        }

        public TimeSlot(UserStepBean userStep) {
            this.userStep = userStep;
            this.isValid = true;
        }
    }

    /**
     * 判断要添加的用户症状和指标是否有效
     * 无效时需要添加空窗期
     *
     * @param recordTime
     * @return 有效时返回null, 否则返回需要创建空窗期的时间
     */
    public static TimeSlot validRecordTime(long recordTime) {
        List<UserStepBean> lstItem = GlobalUserStepData.getUserStepList();
        Collections.sort(lstItem, new UserStepComparator());
        if (lstItem == null || lstItem.isEmpty()) {
            return new TimeSlot(TimeSlot.TIME_FIRST, TimeSlot.TIME_LAST);
        }

        int size = lstItem.size();
        UserStepBean temp;
        long begTime = -1;
        long endTime = -1;
        for (int i = 0; i < size; i++) {
            temp = lstItem.get(i);
            if ((temp.getCompareDateBeg() <= recordTime) && (recordTime <= temp.getCompareDateEnd())) {
                return new TimeSlot(temp);
            }

            if (recordTime < temp.getCompareDateBeg()) {
                if (endTime == -1 || temp.getCompareDateBeg() < endTime) {
                    endTime = temp.getCompareDateBeg() - 1;
//                    endTime = DateTime.from(temp.getCompareDateBeg() * 1000).addDay(-1).toTimeSeconds();//前一天
                }
            }

            if (recordTime > temp.getCompareDateEnd()) {
                if (begTime == -1 || temp.getCompareDateEnd() > begTime) {
                    begTime = temp.getCompareDateEnd() + 1;
//                    begTime = DateTime.from(temp.getCompareDateEnd() * 1000).addDay(1).toTimeSeconds();//后一天
                }
            }
        }

        if (begTime == -1) {
            begTime = TimeSlot.TIME_FIRST;
        }
        if (endTime == -1) {
            begTime = TimeSlot.TIME_LAST;
        }

        return new TimeSlot(begTime, endTime);
    }


    public static String reqUserStepAddCancerPlan(final ViewDataListener listener, final List<UserStepBean> lstBeanAdd) {
        return reqUserStepAdd(listener, GlobalUserStepData.getUserStepList(), lstBeanAdd, true);
    }


    public static String reqUserStepAdd(final ViewDataListener listener, final List<UserStepBean> lstUsAll, final List<UserStepBean> bean) {
        return reqUserStepAdd(listener, lstUsAll, bean, false);
    }

    /**
     * 请求添加用户阶段(可添加一个或多个)
     * OK
     *
     * @param bean
     */
    private static String reqUserStepAdd(final ViewDataListener listener, final List<UserStepBean> lstUsAll, final List<UserStepBean> bean, boolean isCancerPlanType) {

        //先验证请求的数据是否有效
        ValidUserStepModify result = validUserStepAdd(GlobalUserStepData.getUserStepList(), bean);
        if (!result.valid) {
            return result.errmsg;
        }

        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
            safeMapPut(map, "UpdateStepType", "1");
            if (isCancerPlanType) {
                safeMapPut(map, "CancerPlanType", "1");
            }
            safeMapPut(map, "num", "" + bean.size());//共添加了几个阶段
            UserStepBean temp;
            for (int i = 0; i < bean.size(); i++) {
                temp = bean.get(i);
                safeMapPut(map, "StepID" + i, temp.getStepID());
                safeMapPut(map, "CureConfID" + i, temp.getCureConfID());

                if (temp.isBegTimeFirst()) {
                    safeMapPut(map, "StartTime" + i, UserStepBean.TIME_FIRST);
                } else {
                    safeMapPut(map, "StartTime" + i, "" + temp.getSvrBeginTime());
                }
//                safeMapPut(map, "" + i, "" + temp.());
                if (temp.isEndTimeLast()) {
                    safeMapPut(map, "EndTime" + i, UserStepBean.TIME_LAST);
                    safeMapPut(map, "IsNowStep" + i, "1");//告诉服务器这是最新阶段
                } else {
                    safeMapPut(map, "EndTime" + i, "" + temp.getSvrEndTime());
                }
                safeMapPut(map, "IsMedicineValid" + i, "" + temp.getIsMedicineValid());//对应UI阶段是否有效


            }
        }
        reqSvrData(Contants.UserStep.UPDATE_STEP_TIME, map, new OKResultCallback<RespUserStepAdd>(ReqCode_UserStepAdd, listener) {
            @Override//AddStepUID
            public void onResp(int reqCode, RespUserStepAdd resp) {
                if (resp.isOK()) {
                    //ggz:w
//                   UserStepBean beans= GlobalUserStepData.findLastUserStep();
//                    if(beans!=null) {
//                        beans.getStepID();
//                    }
                    //添加成功时，更新内存数据并排序(按顺序添加)
                    if (!TextUtils.isEmpty(resp.getAddStepUID())) {
                        String[] ids = resp.getAddStepUID().split(",");
                        for (int i = 0; i < ids.length; i++) {
                            bean.get(i).setStepUID(ids[i]);
                            bean.get(i).$isEditor(false);
//                            GlobalUserStepData.getUserStepList().add( bean.get(i));
                        }
                    }
                    listenerToActivity(listener, bean, reqCode);
                }
            }
        });
        return null;
    }

    //    private static List<UserStepBean> lstAll;
    private static List<UserStepBean> beans;
    private static ViewDataListener listener;
    private static String mDelQuotaID;
    private static String mDelPerformID;
    private static ValidUserStepModify mValidUserStepModify;


    /**
     * 请求更新用户阶段
     * (缺少阶段时间冲突处理)
     *
     * @param listener
     * @return
     */
    public static String reqUserStepMdf(final ViewDataListener listener, final List<UserStepBean> lstBeanMdf) {
        //     * @param lstBeanAll//, final List<UserStepBean> lstBeanAll
        //验证要修改的用户阶段
//        mValidUserStepModify = validUserStepModify(lstBeanAll);

        mValidUserStepModify = validUserStepModify(GlobalUserStepData.getUserStepList());

        if (!mValidUserStepModify.valid) {

            //时间验证不通过
            return mValidUserStepModify.errmsg;

        } else if (mValidUserStepModify.mNeedDelSpace != null && !mValidUserStepModify.mNeedDelSpace.isEmpty()) {

            UserStepHelper.beans = lstBeanMdf;
            UserStepHelper.listener = listener;
//            UserStepHelper.lstAll = lstBeanAll;
            //需要删除空窗期
            listener.toActivity(null, ReqCode_UserStepMdf1ConfirmDelSpace);
            return null;

        }

        return reqUserStepMdfOver(listener, null, false, lstBeanMdf);//, lstAll
    }

    public static String reqUserStepMdf1DelSpace() {
        return reqUserStepMdfOver(listener, mValidUserStepModify.mNeedDelSpace, false, beans);//, lstAll
    }

    public static String reqUserStepMdf2DelChild() {
        return reqUserStepMdfOver(listener, mValidUserStepModify.mNeedDelSpace, true, beans);//, lstAll
    }

    public static class ValidUserStepModify {
        public boolean valid;
        public String errmsg;
        public List<UserStepBean> mNeedDelSpace;

        public ValidUserStepModify error(String msg) {
            errmsg = msg;
            return this;
        }

        public ValidUserStepModify pass() {
            valid = true;
            return this;
        }

    }

    /**
     * @param listener
     * @param isConfirmedDelChild 是否确定，删除修改阶段时冲突的成员数据
     * @param beans
     * @return
     */
    public static String reqUserStepMdfOver(final ViewDataListener listener, final List<UserStepBean> needDelSpace, boolean isConfirmedDelChild, final List<UserStepBean> beans) {//, final List<UserStepBean> lstAll


        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
            if (needDelSpace != null && !needDelSpace.isEmpty()) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < needDelSpace.size(); i++) {
                    if (i != 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(needDelSpace.get(i).getStepUID());
                }

                safeMapPut(map, "IsDelStep", "1");//删除空窗期标识
                safeMapPut(map, "DelStepUID", stringBuilder.toString());//需要删除空窗期的ID列表
            }

            if (isConfirmedDelChild) {
                safeMapPut(map, "UpdateStepType", "2");
                safeMapPut(map, "DelQuotaID", mDelQuotaID);
                safeMapPut(map, "DelPerformID", mDelPerformID);
            } else {
                safeMapPut(map, "UpdateStepType", "1");
            }

            safeMapPut(map, "num", "" + beans.size());//共修改了几个阶段
            UserStepBean temp;
            for (int i = 0; i < beans.size(); i++) {
                temp = beans.get(i);
                safeMapPut(map, "StepUID" + i, beans.get(i).getStepUID());
                safeMapPut(map, "StepID" + i, beans.get(i).getStepID());
                safeMapPut(map, "CureConfID" + i, temp.getCureConfID());

                if (temp.isBegTimeFirst()) {
                    safeMapPut(map, "StartTime" + i, UserStepBean.TIME_FIRST);
                } else {
                    safeMapPut(map, "StartTime" + i, "" + temp.getSvrBeginTime());
                }
//                safeMapPut(map, "" + i, "" + temp.());
                if (temp.isEndTimeLast()) {
                    safeMapPut(map, "EndTime" + i, UserStepBean.TIME_LAST);

                    safeMapPut(map, "IsNowStep" + i, "1");//告诉服务器这是最新阶段
                } else {
                    safeMapPut(map, "EndTime" + i, "" + temp.getSvrEndTime());
                }
//                safeMapPut(map, "StartTime" + i, "" + beans.get(i).getSvrBeginTime());
//                safeMapPut(map, "EndTime" + i, "" + beans.get(i).getSvrEndTime());


                safeMapPut(map, "IsMedicineValid" + i, "" + temp.getIsMedicineValid());//对应UI阶段是否有效
            }
        }
        reqSvrData(Contants.UserStep.UPDATE_STEP_TIME, map, new OKResultCallback<RespUserStepModify>(ReqCode_UserStepMdf0, listener) {
            @Override
            public void onResp(int reqCode, RespUserStepModify resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据(按顺序添加)(直接更新的)

                    //移除被删除的阶段
                    if (needDelSpace != null) {
                        GlobalUserStepData.getUserStepList().removeAll(needDelSpace);
//                        lstAll.removeAll(needDelSpace);
                    }
                    //更新被修改的阶段
                    GlobalUserStepData.updateUsetStep(beans);

                    for (int i = 0; i < beans.size(); i++) {
                        beans.get(i).$isEditor(false);
                        beans.get(i).$isChanged(false);
                    }

                    listenerToActivity(listener, GlobalUserStepData.getUserStepList(), reqCode);
                } else if ((resp.getiResult() == RespBase.iResult_UserStepModifyConfirm) && (resp.getErrCode() == RespBase.ErrCode_UserStepModifyConfirm)) {

                    UserStepHelper.beans = beans;
                    UserStepHelper.listener = listener;

                    mDelQuotaID = resp.getDelQuotaID();
                    mDelPerformID = resp.getDelPerformID();

                    listenerToActivity(listener, null, ReqCode_UserStepMdf2ConfirmDelChild);
                }
            }
        });
        return null;
    }

    /**
     * 请求删除用户阶段
     * OK
     */

    public static void reqUserStepDel(final ViewDataListener listener, final UserStepBean group) {
//        String userQuotaId = child.getQuotaID();
        del(listener, ReqCode_UserStepDel, group, null);
    }

    /**
     * 请求添加用户指标
     * OK
     *
     * @param bean
     */
    public static void reqUserQuotaAdd(final ViewDataListener listener, final UserStepChildBean bean) {//, String userStepId
        TimeSlot timeSlot = validRecordTime(bean.getRecordTime());
        HashMap<String, String> map = new HashMap<>();
        {
            if (!timeSlot.isValid) {//需要添加空窗期时
                safeMapPut(map, "isBlank", "1");//需要创建空窗期时
                safeMapPut(map, "StartTime", "" + timeSlot.begTime);
                safeMapPut(map, "EndTime", "" + timeSlot.endTime);
            } else {
                safeMapPut(map, "isBlank", "0");
                safeMapPut(map, "StepUID", timeSlot.userStep.getStepUID());
            }

            safeMapPut(map, "InfoID", mInfoID);
            safeMapPut(map, "quotaCEA", bean.getCEA());
            safeMapPut(map, "transforpos", bean.getTransferPos());
            safeMapPut(map, "RecordTime", "" + bean.getRecordTime());
            safeMapPut(map, "MasterName", bean.getMasterCancerName());
            safeMapPut(map, "MasterLen", bean.getMasterCancerLength());
            safeMapPut(map, "MasterWidth", bean.getMasterCancerWidth());
            if (bean.getSlaverCancerNum() != null) {
                int size = bean.getSlaverCancerNum().size();
                safeMapPut(map, "quotaSlaverCancerNum", "" + size);
                for (int i = 0; i < size; i++) {
                    safeMapPut(map, "SlaveName" + i, bean.getSlaverCancerNum().get(i).getSlaveName());
                    safeMapPut(map, "SlaveWidth" + i, "" + bean.getSlaverCancerNum().get(i).getSlaveWidth());
                    safeMapPut(map, "SlaveLen" + i, "" + bean.getSlaverCancerNum().get(i).getSlaveLen());
                }
            } else {
                safeMapPut(map, "quotaSlaverCancerNum", "0");
            }
        }
        reqSvrData(Contants.UserStep.SET_QUOTA, map, new OKResultCallback<RespUserStepChildAdd>(ReqCode_UserQuotaAdd, listener) {
            @Override
            public void onResp(int reqCode, RespUserStepChildAdd resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据(按顺序添加)
                    bean.setQuotaID(resp.getAddQuotaID());
                    listenerToActivity(listener, bean, reqCode);
                }
            }
        });
    }

    /**
     * 请求更新用户指标
     *
     * @param bean
     */
    public static void reqUserQuotaMdf(final ViewDataListener listener, final String userStepId, final UserStepChildBean bean) {
        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
            safeMapPut(map, "StepUID", userStepId);

            safeMapPut(map, "quotaCEA", bean.getCEA());
            safeMapPut(map, "QuotaID", bean.getQuotaID());
            safeMapPut(map, "transforpos", bean.getTransferPos());
            safeMapPut(map, "RecordTime", "" + bean.getRecordTime());
            safeMapPut(map, "MasterName", bean.getMasterCancerName());
            safeMapPut(map, "MasterLen", bean.getMasterCancerLength());
            safeMapPut(map, "MasterWidth", bean.getMasterCancerWidth());
            if (bean.getSlaverCancerNum() != null) {
                int size = bean.getSlaverCancerNum().size();
                safeMapPut(map, "quotaSlaverCancerNum", "" + size);
                for (int i = 0; i < size; i++) {
                    safeMapPut(map, "SlaveName" + i, bean.getSlaverCancerNum().get(i).getSlaveName());
                    safeMapPut(map, "SlaveWidth" + i, "" + bean.getSlaverCancerNum().get(i).getSlaveWidth());
                    safeMapPut(map, "SlaveLen" + i, "" + bean.getSlaverCancerNum().get(i).getSlaveLen());
                }
            } else {
                safeMapPut(map, "quotaSlaverCancerNum", "0");
            }
        }

        reqSvrData(Contants.UserStep.SET_QUOTA, map, new OKResultCallback<RespBase>(ReqCode_UserQuotaMdf, listener) {
            @Override
            public void onResp(int reqCode, RespBase resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据(按顺序添加)
                    UserStepChildBean result = GlobalUserStepData.updateUserQuota(userStepId, bean);
                    listenerToActivity(listener, result, reqCode);
                }
            }
        });
    }

    /**
     * 请求删除用户指标
     * OK
     */

    public static void reqUserQuotaDel(final ViewDataListener listener, final UserStepBean group, final UserStepChildBean child) {
        del(listener, ReqCode_UserQuotaDel, group, child);

    }

    /**
     * 请求添加用户症状
     * OK
     *
     * @param bean
     */
    public static void reqUserSymptAdd(final ViewDataListener listener, final UserStepChildBean bean) {
        HashMap<String, String> map = new HashMap<>();
        {
            TimeSlot timeSlot = validRecordTime(bean.getRecordTime());
            if (!timeSlot.isValid) {
                safeMapPut(map, "isBlank", "1");//需要创建空窗期时
                safeMapPut(map, "StartTime", "" + timeSlot.begTime);
                safeMapPut(map, "EndTime", "" + timeSlot.endTime);
            } else {
                safeMapPut(map, "isBlank", "0");
                safeMapPut(map, "StepUID", timeSlot.userStep.getStepUID());
            }
            safeMapPut(map, "IsMedicineValid", "1");//空窗期是否有效???????
            safeMapPut(map, "InfoID", mInfoID);

//            safeMapPut(map,"StepUID", "" +);???
            safeMapPut(map, "RecordTime", "" + bean.getRecordTime());
            safeMapPut(map, "Perform", bean.getPerformStep());//用户选择症状id列表（逗号隔开的）
            safeMapPut(map, "Remark", bean.getBase64Remark());
        }

        reqSvrData(Contants.UserStep.SET_PERFRORM, map, new OKResultCallback<RespUserStepChildAdd>(ReqCode_UserSymptAdd, listener) {
            @Override
            public void onResp(int reqCode, RespUserStepChildAdd resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据(按顺序添加)
                    bean.setQuotaID(resp.getAddPerformID());
                    listenerToActivity(listener, null, reqCode);
                }
            }
        });

    }

    /**
     * 请求更新用户症状
     * OK
     *
     * @param bean
     */

    public static void reqUserSymptMdf(final ViewDataListener listener, final String userStepId, final UserStepChildBean bean) {
        HashMap<String, String> map = new HashMap<>();
        {
            safeMapPut(map, "InfoID", mInfoID);
            safeMapPut(map, "Perform", bean.getPerformStep());//用户选择症状id列表（逗号隔开的）
            safeMapPut(map, "RecordTime", "" + bean.getRecordTime());
            safeMapPut(map, "Remark", bean.getBase64Remark());
            safeMapPut(map, "StepDetailPerformID", "" + bean.getStepDetailPerformID());
            safeMapPut(map, "StepUID", userStepId);
        }

        reqSvrData(Contants.UserStep.SET_PERFRORM, map, new OKResultCallback<RespBase>(ReqCode_UserSymptMdf, listener) {
            @Override
            public void onResp(int reqCode, RespBase resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据(按顺序添加)
                    UserStepChildBean result = GlobalUserStepData.updateUserSympt(userStepId, bean);
                    listenerToActivity(listener, result, reqCode);
                }
            }
        });

    }

    /**
     * 请求删除用户症状
     * OK
     */

    public static void reqUserSymptDel(final ViewDataListener listener, final UserStepBean group, final UserStepChildBean child) {
        del(listener, ReqCode_UserSymptDel, group, child);
    }


    /**
     * 1:为删除指标
     */
    private static final int DEL_TYPE__USERQutoa = 1;
    /**
     * 2:为删除症状
     */
    private static final int DEL_TYPE__USERSympt = 2;
    /**
     * 3:为删除阶段
     */
    private static final int DEL_TYPE__USERStep = 3;

    private static void del(final ViewDataListener listener, int reqCode, final UserStepBean group, final UserStepChildBean child) {
        int delType = -1;

        HashMap<String, String> map = new HashMap<>(2);
        switch (reqCode) {
            case ReqCode_UserSymptDel:
                delType = DEL_TYPE__USERSympt;
                safeMapPut(map, "DelID", child.getStepDetailPerformID());
                break;
            case ReqCode_UserQuotaDel:
                delType = DEL_TYPE__USERQutoa;
                safeMapPut(map, "DelID", child.getQuotaID());
                break;
            case ReqCode_UserStepDel:
                delType = DEL_TYPE__USERStep;
                safeMapPut(map, "DelID", group.getStepUID());
                break;
        }


        {
            safeMapPut(map, "DelType", "" + delType);
        }

        reqSvrData(Contants.UserStep.DEL_STEP_QUOTA_PERFORM, map, new OKResultCallback<RespBase>(reqCode, listener) {
            @Override
            public void onResp(int reqCode, RespBase resp) {
                if (resp.isOK()) {
                    //添加成功时，更新内存数据
                    switch (reqCode) {
                        case ReqCode_UserSymptDel:
                            if (group != null) {
                                group.getChildList().remove(child);
                            }
//                            GlobalUserStepData.deleteUserSympt();
                            listenerToActivity(listener, null, reqCode);
                            break;
                        case ReqCode_UserQuotaDel:
                            if (group != null) {
                                group.getChildList().remove(child);
                            }
//                            GlobalUserStepData.deleteUserQuota();
                            listenerToActivity(listener, null, reqCode);
                            break;
                        case ReqCode_UserStepDel:
                            if (group != null) {
                                GlobalUserStepData.getUserStepList().remove(group);
                            }
                            listenerToActivity(listener, group, reqCode);
                            break;
                    }
                }
            }
        });

    }


    private static void listenerToActivity(ViewDataListener listener, Object obj, int reqCode) {
        if (listener != null) {
            listener.toActivity(obj, reqCode);
        }
    }
}


//    ViewDataListener listener

//    /**
//     * 验证用户阶段的起始时间是否有效
//     * @parem userStepId
//     * @param begTime
//     * @return
//     */
//    public boolean validUserStepBegTime(String userStepId,long begTime){
//        return false;
//    }
//
//    /**
//     * 验证用户阶段的截至时间是否有效
//     * @parem userStepId
//     * @param endTime
//     * @return
//     */
//    public boolean validUserStepEndTime(String userStepId,long endTime){
//        return false;
//    }
//public static <T> void display(T t) {
//    System.out.println(t.getClass());
//}

//    /**
//     * Test
//     */
//    public static void reqSvrDataTest(final ViewDataListener listener) {//,Comparator<? super T>
//        final int reqCode = ReqCode_UserStepAll;
//        listener.showLoading(reqCode);
//        HashMap<String, String> map = new HashMap<>();
//        {
//            safeMapPut(map,"InfoID", mInfoID);
//        }
//
//        mGetDataBiz.getData(map, Contants.UserStep.GET_ALL_STEP, new OkHttpClientManager.ResultCallback<RespGetAllStep>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                if (listener != null) {
//                    listener.hideLoading(reqCode);
//                    listener.showError(-1);
//                }
//            }
//
//            @Override
//            public void onResponse(RespGetAllStep response) {
//                if (listener != null) {
//                    listener.hideLoading(reqCode);
//                }
////                resp.onResp(reqCode, response);
//            }
//        });
//    }