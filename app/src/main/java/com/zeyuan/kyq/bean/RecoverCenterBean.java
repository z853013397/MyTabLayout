package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 * 获取当前用户康复中心详情
 */
public class RecoverCenterBean {

    /**
     * iResult : 0
     * Title : Title1
     * Author : Author1
     * Content : Content1
     * CircleID : 101
     * PostTime : 123456789
     * UrlNum : [{"Url":"http://www.url.com/1"},{"Url":"http://www.url.com/12"},{"Url":"http://www.url.com/13"}]
     */

    private String iResult;
    private String Title;
    private String Author;
    private String Content;
    private int CircleID;
    private int PostTime;
    private List<UrlNumEntity> UrlNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setAuthor(String Author) {
        this.Author = Author;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setCircleID(int CircleID) {
        this.CircleID = CircleID;
    }

    public void setPostTime(int PostTime) {
        this.PostTime = PostTime;
    }

    public void setUrlNum(List<UrlNumEntity> UrlNum) {
        this.UrlNum = UrlNum;
    }

    public String getIResult() {
        return iResult;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getContent() {
        return Content;
    }

    public int getCircleID() {
        return CircleID;
    }

    public int getPostTime() {
        return PostTime;
    }

    public List<UrlNumEntity> getUrlNum() {
        return UrlNum;
    }

    public static class UrlNumEntity {
        /**
         * Url : http://www.url.com/1
         */

        private String Url;

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getUrl() {
            return Url;
        }
    }
    @Override
    public String toString() {
        return "RecoverCenterBean [iResult=" + iResult + ", Title=" + Title
                + ", Author=" + Author + ", Content=" + Content + ", CircleID="
                + CircleID + ", PostTime=" + PostTime + ", UrlNum=" + UrlNum
                + "]";
    }
}
