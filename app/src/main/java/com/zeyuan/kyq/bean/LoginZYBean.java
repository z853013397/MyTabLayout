package com.zeyuan.kyq.bean;

/**
 * Created by Administrator on 2016/1/8.
 *
 */
public class LoginZYBean {
    public static final String HAS_CREATE = "1";//创建过档案
//    public static final String No_CREATE = "2";//

    /**
     * iResult : 0
     * IsHaveCreateInfo : 1
     * IsHaveLogin : 0
     * InfoID : 9260
     * OpenID : 4F808E2BA69B1CEAD752FE92336E7DCF
     */

    private String iResult;
    private String IsHaveCreateInfo;//1是创建过  不是1 就是未创建 档案
    private String IsHaveLogin;//1.是否登录过
    private String InfoID;
    private String OpenID;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setIsHaveCreateInfo(String IsHaveCreateInfo) {
        this.IsHaveCreateInfo = IsHaveCreateInfo;
    }

    public void setIsHaveLogin(String IsHaveLogin) {
        this.IsHaveLogin = IsHaveLogin;
    }

    public void setInfoID(String InfoID) {
        this.InfoID = InfoID;
    }

    public void setOpenID(String OpenID) {
        this.OpenID = OpenID;
    }

    public String getIResult() {
        return iResult;
    }

    public String getIsHaveCreateInfo() {
        return IsHaveCreateInfo;
    }

    public String getIsHaveLogin() {
        return IsHaveLogin;
    }

    public String getInfoID() {
        return InfoID;
    }

    public String getOpenID() {
        return OpenID;
    }
}
