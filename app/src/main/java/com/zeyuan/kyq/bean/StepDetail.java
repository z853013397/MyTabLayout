package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-10-28
 * Time: 14:03
 * FIXME
 * 在阶段详情中点开某个阶段显示的阶段详情
 */


public class StepDetail {


    /**
     * iResult : 0
     * RecordNum : [{"RecordTime":"1377683879","Remark":"","PerformORQuota":"1","PerformStep":[]},{"CEA":"41","PerformORQuota":"2","RecordTime":"0","MasterCancerWidth":"11.000000","MasterCancerLength":"32.000000","MasterCancerName":"1111111111111111111111","SlaverCancerNum":[{"SlaverCancerLength":"11","SlaverCancerWidth":"32","SlaverCancerName":"主肿瘤大小"}]}]
     */

    private String iResult;
    /**
     * RecordTime : 1377683879
     * Remark :
     * PerformORQuota : 1
     * PerformStep : []
     */

    private List<RecordNumEntity> RecordNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setRecordNum(List<RecordNumEntity> RecordNum) {
        this.RecordNum = RecordNum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<RecordNumEntity> getRecordNum() {
        return RecordNum;
    }

    public static class RecordNumEntity {
        private String RecordTime;
        private String Remark;
        private String PerformORQuota;
        private List<?> PerformStep;

        public void setRecordTime(String RecordTime) {
            this.RecordTime = RecordTime;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public void setPerformORQuota(String PerformORQuota) {
            this.PerformORQuota = PerformORQuota;
        }

        public void setPerformStep(List<?> PerformStep) {
            this.PerformStep = PerformStep;
        }

        public String getRecordTime() {
            return RecordTime;
        }

        public String getRemark() {
            return Remark;
        }

        public String getPerformORQuota() {
            return PerformORQuota;
        }

        public List<?> getPerformStep() {
            return PerformStep;
        }
    }
}
