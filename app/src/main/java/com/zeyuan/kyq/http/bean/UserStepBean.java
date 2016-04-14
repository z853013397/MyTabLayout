package com.zeyuan.kyq.http.bean;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserStepHelper;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 */
public class UserStepBean {

    /**
     * StepUID : 54
     * StepID : 1000057
     * IsMedicineValid : 1
     * BeginTime : 1452960000
     * EndTime : 1453046399
     */
    private int $number;

    public int $number() {
        return $number;
    }

    public void $number(int $number) {
        this.$number = $number;
    }

    private boolean $isChanged;

    public boolean $isChanged() {
        return $isChanged;
    }

    public void $isChanged(boolean $isChanged) {
        this.$isChanged = $isChanged;
    }

//    public boolean isSpaceStep() {
//        return "0".equals(this.getStepID());
//    }

    private boolean $isEditor;

    public boolean $isEditor() {
        return $isEditor;
    }

    public void $isEditor(boolean $isEditor) {
        this.$isEditor = $isEditor;
    }

    private String StepUID;
    private String StepID;
    private String IsMedicineValid;
    private String BeginTime;
    private String EndTime;
    private List<UserStepChildBean> RecordNum;
    private String CureConfID;//ggz:w

    public String getCureConfID() {
//        StepID
        String cureConfId = MapDataUtils.getAllCureconfID(StepID);
        return cureConfId;
    }

    public void setCureConfID(String cureConfID) {
        CureConfID = cureConfID;
    }

    public String getStepUID() {
        return StepUID;
    }

    public void setStepUID(String stepUID) {
        StepUID = stepUID;
    }

    public boolean isSpace() {
        return "0".equals(StepID) && (!TextUtils.isEmpty(StepUID));
    }

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public int getIsMedicineValid() {
        if (TextUtils.isEmpty(IsMedicineValid)) {
            return 0;
        }
        return Integer.parseInt(IsMedicineValid);
    }

    public void setIsMedicineValid(int isMedicineValid) {
        IsMedicineValid = "" + isMedicineValid;
    }

    public final static String TIME_FIRST = "1";
    public final static String TIME_LAST = "0";

//    public long getBeginTime() {
//        if (TextUtils.isEmpty(BeginTime)) {
//            long nowTimeBeg = DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds();
//            BeginTime = "" + nowTimeBeg;
//            return nowTimeBeg;
//        }
//        if (TIME_FIRST.equals(BeginTime)) {
//            return 0;
//        }
//        return Long.parseLong(BeginTime);
//    }

//    public long getEndTime() {
//        if (TextUtils.isEmpty(EndTime)) {
//            long nowTimeBEnd = DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").addDay(1).toTimeSeconds() - 1;
//            EndTime = "" + nowTimeBEnd;
//            return nowTimeBEnd;
//        }
//        if (TIME_LAST.equals(EndTime)) {
//            return DateTime.now().toTimeSeconds();
//        }
//        return Long.parseLong(EndTime);
//    }

    public String getSvrBeginTime() {
        return BeginTime;
    }

    public String getSvrEndTime() {
        return EndTime;
    }

    //test
//    private long mCompareDateBeg;

    //获取客户端使用的起始时间
    public long getCompareDateBeg() {
        if (TIME_FIRST.equals(BeginTime)) {
            return 0;// mCompareDateBeg =
        }
        return Long.parseLong(BeginTime);//mCompareDateBeg =
    }

    //test
//    private long mCompareDateEnd;

    //获取客户端使用的结束时间
    public long getCompareDateEnd() {
        if (TIME_LAST.equals(EndTime)) {
            return  DateTime.now().toTimeSeconds();//mCompareDateEnd =
        }
        return  Long.parseLong(EndTime);//mCompareDateEnd =
    }

    public void setBeginTime(long beginTime) {
        BeginTime = "" + beginTime;
    }

    public boolean isBegTimeFirst() {
        return (TIME_FIRST.equals(BeginTime));
    }

    public boolean isEndTimeLast() {
        return (TIME_LAST.equals(EndTime));
    }

    public boolean isBegTimeNull() {
        if (TextUtils.isEmpty(BeginTime)) {
            return true;
        }
        return false;
    }

    public boolean isEndTimeNull() {
        if (TextUtils.isEmpty(EndTime)) {
            return true;
        }
        return false;
    }


    public void setEndTime(long endTime) {
        EndTime = "" + endTime;
    }

    public List<UserStepChildBean> getChildList() {
        return RecordNum;
    }

    public void setChildList(List<UserStepChildBean> recordNum) {
        RecordNum = recordNum;
    }
}
