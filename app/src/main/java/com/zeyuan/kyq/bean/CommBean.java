package com.zeyuan.kyq.bean;

import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.Serializable;
import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-25
 * Time: 09:56
 * FIXME普通决策树的结果详情
 */


public class CommBean implements Serializable{

    /**
     * iResult : 0
     * CommonPolicyTypeID : 8
     * CommonPolicyTypeName : 靶向副作用
     * describ : 对疼痛的处理采取主动预防用药。止痛剂应有规律按时给予，而不是必要时才给，下一次用药应在前一次药物药效消失之前给予，得以持续镇痛。通过正确治疗，除少数病例外都能得到良好的控制
     * solution : [{"stepid":"152","alias":"预防性脑照射"},{"stepid":"153","alias":"预防性脑照射"},{"stepid":"154","alias":"预防性脑照射"}]
     */

    private String iResult;
    private String CommonPolicyTypeID;
    private String CommonPolicyTypeName;
//    private String describ;
    private String Summary;

    public String getSummary() {
        return DecryptUtils.decodeBase64(Summary);
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    /**
     * stepid : 152
     * alias : 预防性脑照射
     */

    private List<CancerResuletBean.StepEntity> Step;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCommonPolicyTypeID(String CommonPolicyTypeID) {
        this.CommonPolicyTypeID = CommonPolicyTypeID;
    }

    public void setCommonPolicyTypeName(String CommonPolicyTypeName) {
        this.CommonPolicyTypeName = CommonPolicyTypeName;
    }

//    public void setDescrib(String describ) {
//        this.describ = describ;
//    }

    public void setSolution(List<CancerResuletBean.StepEntity> solution) {
        this.Step = solution;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCommonPolicyTypeID() {
        return CommonPolicyTypeID;
    }

    public String getCommonPolicyTypeName() {
        return CommonPolicyTypeName;
    }

//    public String getDescrib() {
//        return describ;
//    }

    public List<CancerResuletBean.StepEntity> getSolution() {
        return Step;
    }

//    public static class SolutionEntity {
//        private String stepid;
//        private String alias;
//
//        public void setStepid(String stepid) {
//            this.stepid = stepid;
//        }
//
//        public void setAlias(String alias) {
//            this.alias = alias;
//        }
//
//        public String getStepid() {
//            return stepid;
//        }
//
//        public String getAlias() {
//            return alias;
//        }
//    }
}
