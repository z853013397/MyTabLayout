package com.zeyuan.kyq.bean;

/**
 * Created by Administrator on 2015/9/6.
 */
public class BaseBean {
    public String iResult;
    public String ErrMsg;
    public String uid;
    public String infoid;
    public String NowTime;
    public String InfoId;

    @Override
    public String toString() {
        return "BaseBean [iResult=" + iResult + ", ErrMsg=" + ErrMsg + ", uid="
                + uid + ", infoid=" + infoid + ", NowTime=" + NowTime + "]";
    }
}
