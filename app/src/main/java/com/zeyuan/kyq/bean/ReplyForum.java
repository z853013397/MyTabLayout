package com.zeyuan.kyq.bean;

/**
 * Created by Administrator on 2015/9/11.
 * 19.圈子—回复某个帖子
 */
public class ReplyForum {

    /**
     * iResult : 0
     * Commentid : 1009
     */

    private String iResult;
    private String Commentid;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCommentid(String Commentid) {
        this.Commentid = Commentid;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCommentid() {
        return Commentid;
    }


    @Override
    public String toString() {
        return "ReplyForum{" +
                "iResult='" + iResult + '\'' +
                ", Commentid='" + Commentid + '\'' +
                '}';
    }
}
