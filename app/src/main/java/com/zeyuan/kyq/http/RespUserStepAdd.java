package com.zeyuan.kyq.http;

import com.zeyuan.kyq.http.bean.UserStepChildBean;

import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespUserStepAdd extends RespBase {

    private String AddStepUID;

    public String getAddStepUID() {
        return AddStepUID;
    }

    public void setAddStepUID(String addStepUID) {
        AddStepUID = addStepUID;
    }
}
