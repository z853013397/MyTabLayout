package com.zeyuan.kyq.http;

import com.zeyuan.kyq.http.bean.UserStepBean;

import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespGetAllStep extends RespBase {

    protected List<UserStepBean> StepNum;

    public List<UserStepBean> getStepNum() {
        return StepNum;
    }

    public void setStepNum(List<UserStepBean> lstUserStep) {
        StepNum = lstUserStep;
    }
}
