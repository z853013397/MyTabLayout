package com.zeyuan.kyq.http;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespBase {
    protected int iResult;
    protected int ErrCode;
    public static final int iResult_UserStepModifyConfirm = -1;
    public static final int ErrCode_UserStepModifyConfirm = 999;


    public boolean isOK() {
        return iResult == 0;
    }

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int errCode) {
        ErrCode = errCode;
    }

    public int getiResult() {
        return iResult;
    }

    public void setiResult(int iResult) {
        this.iResult = iResult;
    }


//    protected int infoid;
//    public int getInfoId() {
//        return infoid;
//    }
//    public int getInfoid() {
//        return infoid;
//    }

//    public void setInfoid(int infoid) {
//        this.infoid = infoid;
//    }
}
