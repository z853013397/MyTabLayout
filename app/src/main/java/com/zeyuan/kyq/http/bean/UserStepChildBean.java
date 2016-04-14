package com.zeyuan.kyq.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.zeyuan.kyq.bean.SlaverCancer;
import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 */
public class UserStepChildBean implements Serializable {
    /**
     * 症状类型
     */
    public static final int PERFORM_OR_QUOTA__SYMPT = 1;

    /**
     * 指标类型
     */
    public static final int PERFORM_OR_QUOTA__QUOTA = 2;

    /**
     * 是指标记录么
     *
     * @return
     */
    public boolean isQuota() {
        return PerformORQuota == PERFORM_OR_QUOTA__QUOTA;
    }


    private String stepUid;


    public String getStepUid() {
        return stepUid;
    }

    public void setStepUid(String stepUid) {
        this.stepUid = stepUid;
    }

    protected long RecordTime;
    protected int PerformORQuota;//表示他是症状还是指标 1.是症状 2.是指标

    public long getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(long recordTime) {
        RecordTime = recordTime;
    }

    public int getPerformORQuota() {
        return PerformORQuota;
    }

    public void setPerformORQuota(int performORQuota) {
        PerformORQuota = performORQuota;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private String StepDetailPerformID;//这条记录的id
    private String Remark;
    private String PerformStep;//症状的ids

    public String getStepDetailPerformID() {
        return StepDetailPerformID;
    }

    public void setStepDetailPerformID(String stepDetailPerformID) {
        StepDetailPerformID = stepDetailPerformID;
    }

    public String getRemark() {
        if (!TextUtils.isEmpty(Remark)) {
            return DecryptUtils.decodeBase64(Remark);
        }
        return "";
    }

    public String getBase64Remark() {

        return Remark;
    }

    public void setRemark(String remark) {
        if (!TextUtils.isEmpty(remark)) {
            Remark = DecryptUtils.encode(remark);
        }else {
            Remark = null;
        }
    }

    public String getPerformStep() {
        return PerformStep;
    }

    public void setPerformStep(String performStep) {
        PerformStep = performStep;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private String QuotaID;
    private String CEA;
    private String TransferPos;
    private String MasterCancerWidth;
    private String MasterCancerLength;
    private String MasterCancerName;
    private List<SlaverCancer> SlaverCancerNum;

    public String getQuotaID() {
        return QuotaID;
    }

    public void setQuotaID(String quotaID) {
        QuotaID = quotaID;
    }

    public String getCEA() {
        return CEA;
    }

    public void setCEA(String CEA) {
        this.CEA = CEA;
    }

    public String getTransferPos() {
        return TransferPos;
    }

    public void setTransferPos(String transferPos) {
        TransferPos = transferPos;
    }

    public String getMasterCancerWidth() {
        return MasterCancerWidth;
    }

    public void setMasterCancerWidth(String masterCancerWidth) {
        MasterCancerWidth = masterCancerWidth;
    }

    public String getMasterCancerLength() {
        return MasterCancerLength;
    }

    public void setMasterCancerLength(String masterCancerLength) {
        MasterCancerLength = masterCancerLength;
    }

    public String getMasterCancerName() {
        return MasterCancerName;
    }

    public void setMasterCancerName(String masterCancerName) {
        MasterCancerName = masterCancerName;
    }

    public List<SlaverCancer> getSlaverCancerNum() {
        return SlaverCancerNum;
    }

    public void setSlaverCancerNum(List<SlaverCancer> slaverCancerNum) {
        SlaverCancerNum = slaverCancerNum;
    }


}
