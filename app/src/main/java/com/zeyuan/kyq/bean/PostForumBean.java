package com.zeyuan.kyq.bean;

/**
 * Created by Administrator on 2015/9/11.
 *
 * 20.圈子--发布帖子
 */
public class PostForumBean {


    /**
     * iResult : 0
     * index : 1001
     * CircleID : 10
     */

    private String iResult;
    private String index;
    private String CircleID;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setCircleID(String CircleID) {
        this.CircleID = CircleID;
    }

    public String getIResult() {
        return iResult;
    }

    public String getIndex() {
        return index;
    }

    public String getCircleID() {
        return CircleID;
    }

    @Override
    public String toString() {
        return "PostForumBean{" +
                "iResult='" + iResult + '\'' +
                ", index='" + index + '\'' +
                ", CircleID='" + CircleID + '\'' +
                '}';
    }
}
