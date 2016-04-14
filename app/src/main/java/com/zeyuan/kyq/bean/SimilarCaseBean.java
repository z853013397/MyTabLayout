package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 * 10.获取当前用户相似案例详情
 */
public class SimilarCaseBean  {

    /**
     * iResult : 0
     * CircleId : 10001
     * data : [{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441690384,"Index":"1005","ReplyNum":"998","PostType ":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441630384,"Index":"1001","ReplyNum":"998","PostType":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441660384,"Index":"1002","ReplyNum":"998","PostType":"2","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441690384,"Index":"2005","ReplyNum":"998","PostType":"1","Title":"标题"},{"ImageUrl":"http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg","Author":"作者","PostTime":1441630384,"Index":"2001","CircleId":"10001","ReplyNum":"998","PostType":"1","Title":"标题"}]
     */

    private String iResult;
    private String CircleId;
    private List<DataEntity> data;

    public void setIResult(String iResult) {
        this.iResult = iResult;
    }

    public void setCircleId(String CircleId) {
        this.CircleId = CircleId;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getIResult() {
        return iResult;
    }

    public String getCircleId() {
        return CircleId;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * ImageUrl : http://img0.imgtn.bdimg.com/it/u=3181282067,3590745083&fm=21&gp=0.jpg
         * Author : 作者
         * PostTime : 1441690384
         * Index : 1005
         * ReplyNum : 998
         * PostType  : 2
         * Title : 标题
         */

        private String ImageUrl;
        private String Author;
        private int PostTime;
        private String Index;
        private String ReplyNum;
        private String PostType;
        private String Title;

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public void setPostTime(int PostTime) {
            this.PostTime = PostTime;
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

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public String getAuthor() {
            return Author;
        }

        public int getPostTime() {
            return PostTime;
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

        public String getTitle() {
            return Title;
        }
    }


    @Override
    public String toString() {
        return "SimilarCaseBean [iResult=" + iResult + ", CircleId=" + CircleId
                + ", data=" + data + "]";
    }
}
