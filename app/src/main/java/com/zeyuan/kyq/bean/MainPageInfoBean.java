package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/2.
 * 获取当前主页的bean
 * GetMainPage
 */
public class MainPageInfoBean extends BaseBean {

    /**
     * StepUID : 5005
     * StepID : Step1
     * CureConfID : 2501
     * StepUserSelectIDs : ["123","234"]
     * DiscoverTime : 1234756789
     * SimilarcaseNum : [{"title":"title1","imageUrl":"http://www.url.com/1","index":"1111222","author":"abc"},{"title":"title1","imageUrl":"http://www.url.com/1","index":"11112222332","author":"abc的"}]
     * RecoverCenterNum : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * Knowlege : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * Effect : [{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"},{"title":"title1223213","imageUrl":"http://www.url.com/12dadas","Replynum":"1153123","index":"11112222332"}]
     * NurseNum : [{"Pageid":"1","StepID":"2313"},{"Pageid":"2","effectid":["2313","2314","2315"]},{"Pageid":"3","Performid":"300"}]
     */

//    private String StepUID;
//    private String StepID;
//    private String CureConfID;
//    private String DiscoverTime;
    private List<String> StepUserSelectIDs;//联合用药的id
    /**
     * title : title1
     * imageUrl : http://www.url.com/1
     * index : 1111222
     * author : abc
     */

    private List<SimilarcaseNumEntity> SimilarcaseNum;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> RecoverCenterNum;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> Knowlege;
    /**
     * title : title1223213
     * imageUrl : http://www.url.com/12dadas
     * Replynum : 1153123
     * index : 11112222332
     */

    private List<MainItemEntity> Effect;
    /**
     * Pageid : 1
     * StepID : 2313
     */



    private List<NurseNumEntity> NurseNum;

//    public void setStepUID(String StepUID) {
//        this.StepUID = StepUID;
//    }

//    public void setStepID(String StepID) {
//        this.StepID = StepID;
//    }

//    public void setCureConfID(String CureConfID) {
//        this.CureConfID = CureConfID;
//    }

//    public void setDiscoverTime(String DiscoverTime) {
//        this.DiscoverTime = DiscoverTime;
//    }

    public void setStepUserSelectIDs(List<String> StepUserSelectIDs) {
        this.StepUserSelectIDs = StepUserSelectIDs;
    }

    public void setSimilarcaseNum(List<SimilarcaseNumEntity> SimilarcaseNum) {
        this.SimilarcaseNum = SimilarcaseNum;
    }

    public void setRecoverCenterNum(List<MainItemEntity> RecoverCenterNum) {
        this.RecoverCenterNum = RecoverCenterNum;
    }

    public void setKnowlege(List<MainItemEntity> Knowlege) {
        this.Knowlege = Knowlege;
    }

    public void setEffect(List<MainItemEntity> Effect) {
        this.Effect = Effect;
    }

    public void setNurseNum(List<NurseNumEntity> NurseNum) {
        this.NurseNum = NurseNum;
    }

//    public String getStepUID() {
//        return StepUID;
//    }

//    public String getStepID() {
//        return StepID;
//    }

//    public String getCureConfID() {
//        return CureConfID;
//    }

//    public String getDiscoverTime() {
//        return DiscoverTime;
//    }

    public List<String> getStepUserSelectIDs() {
        return StepUserSelectIDs;
    }

    public List<SimilarcaseNumEntity> getSimilarcaseNum() {
        return SimilarcaseNum;
    }

    public List<MainItemEntity> getRecoverCenterNum() {
        return RecoverCenterNum;
    }

    public List<MainItemEntity> getKnowlege() {
        return Knowlege;
    }

    public List<MainItemEntity> getEffect() {
        return Effect;
    }

    public List<NurseNumEntity> getNurseNum() {
        return NurseNum;
    }

    private UpEntity Up;

    public UpEntity getUp() {
        return Up;
    }

    public void setUp(UpEntity up) {
        Up = up;
    }

    public static class UpEntity {
        private String u;//是否需要更新，数字，1代表强制更新，2代表不强制更新，3代表不更新
        private String m;//弹出文本框消息内容，字符串
        private String l;//需要更新的url地址
        private String v;//最新的版本号  字符串


        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getU() {
            return u;
        }

        public void setU(String u) {
            this.u = u;
        }
    }


    public static class SimilarcaseNumEntity {
        private String Title;
        private String Headimgurl;
        private String Index;
        private String Author;

        public void setTitle(String title) {
            this.Title = title;
        }

        public void setImageUrl(String imageUrl) {
            this.Headimgurl = imageUrl;
        }

        public void setIndex(String index) {
            this.Index = index;
        }

        public void setAuthor(String author) {
            this.Author = author;
        }

        public String getTitle() {
            return Title;
        }

        public String getImageUrl() {
            return Headimgurl;
        }

        public String getIndex() {
            return Index;
        }

        public String getAuthor() {
            return Author;
        }
    }
//
//    public static class RecoverCenterNumEntity {
//        private String title;
//        private String imageUrl;
//        private String Replynum;
//        private String index;
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public void setReplynum(String Replynum) {
//            this.Replynum = Replynum;
//        }
//
//        public void setIndex(String index) {
//            this.index = index;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public String getReplynum() {
//            return Replynum;
//        }
//
//        public String getIndex() {
//            return index;
//        }
//    }

    public static class MainItemEntity {
        private String Title;//标题
        private String ThumbURL;//图片请求地址
        private String PageViewNum;
        private String ArticleIndex;

        public void setTitle(String title) {
            this.Title = title;
        }

        public void setImageUrl(String imageUrl) {
            this.ThumbURL = imageUrl;
        }

        public void setReplynum(String Replynum) {
            this.PageViewNum = Replynum;
        }

        public void setIndex(String index) {
            this.ArticleIndex = index;
        }

        public String getTitle() {
            return Title;
        }

        public String getImageUrl() {
            return ThumbURL;
        }

        public String getReplynum() {
            return PageViewNum;
        }

        public String getIndex() {
            return ArticleIndex;
        }

        @Override
        public String toString() {
            return "MainItemEntity{" +
                    "title='" + Title + '\'' +
                    ", imageUrl='" + ThumbURL + '\'' +
                    ", Replynum='" + PageViewNum + '\'' +
                    ", index='" + ArticleIndex + '\'' +
                    '}';
        }
    }

//    public static class EffectEntity {
//        private String title;
//        private String imageUrl;
//        private String Replynum;
//        private String index;
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public void setReplynum(String Replynum) {
//            this.Replynum = Replynum;
//        }
//
//        public void setIndex(String index) {
//            this.index = index;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public String getReplynum() {
//            return Replynum;
//        }
//
//        public String getIndex() {
//            return index;
//        }
//    }

    public static class NurseNumEntity {
        private String Pageid;
        private String StepID;
        private String Performid;
        private String Content;
        private String Type;//1.默认文本
        // 2.普通文本
        //3.文章
        //4.帖子


        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getPerformid() {
            return Performid;
        }

        public void setPerformid(String performid) {
            Performid = performid;
        }

        public void setPageid(String Pageid) {
            this.Pageid = Pageid;
        }

        public void setStepID(String StepID) {
            this.StepID = StepID;
        }

        public String getPageid() {
            return Pageid;
        }

        public String getStepID() {
            return StepID;
        }
    }
}
