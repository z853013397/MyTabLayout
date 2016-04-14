package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 * 我发布的帖子
 */
public class MyForumReleaseBean {

    /**
     * iResult : 0
     * ForumNum : [{"Title":"gghj","Index":"15","ReplyNum":"0","PostType":"0","PostTime":"1447758234"}]
     */

    private String iResult;
    /**
     * Title : gghj
     * Index : 15
     * ReplyNum : 0
     * PostType : 0
     * PostTime : 1447758234
     */

    private List<ForumBeanEntity> ForumNum;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setForumNum(List<ForumBeanEntity> ForumNum) {
        this.ForumNum = ForumNum;
    }

    public String getIResult() {
        return iResult;
    }

    public List<ForumBeanEntity> getForumNum() {
        return ForumNum;
    }

    public static class ForumBeanEntity {
        private String Title;
        private String Index;
        private String ReplyNum;
        private String PostType;
        private String PostTime;
        private String HeadImgUrl;

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            HeadImgUrl = headImgUrl;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setIndex(String Index) {
            this.Index = Index;
        }

        public void setReplyNum(String ReplyNum) {
            this.ReplyNum = ReplyNum;
        }

        public void setPostType(String PostType) {
            this.PostType = PostType;
        }

        public void setPostTime(String PostTime) {
            this.PostTime = PostTime;
        }

        public String getTitle() {
            return Title;
        }

        public String getIndex() {
            return Index;
        }

        public String getReplyNum() {
            return ReplyNum;
        }

        public String getPostType() {
            return PostType;
        }

        public String getPostTime() {
            return PostTime;
        }
    }
}
