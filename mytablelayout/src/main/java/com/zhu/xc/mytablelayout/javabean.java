package com.zhu.xc.mytablelayout;

import java.util.List;

/**
 * Created by xc on 2016/5/25.
 */
public class javabean {

    /**
     * listExerciseItem : [{"bigItemId":3897,"bigItemType":1,"bigItemTitle":"一.单选题","bigItemScore":30,"bigItemDesc":"","smallItemList":[{"itemId":"23401","smallItemSeq":"10","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23401&topic_id=53951","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=53951&smallItemId=23401","difficulty":"3"},{"itemId":"23400","smallItemSeq":"9","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23400&topic_id=54419","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54419&smallItemId=23400","difficulty":"3"},{"itemId":"23399","smallItemSeq":"8","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23399&topic_id=54420","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54420&smallItemId=23399","difficulty":"3"},{"itemId":"23398","smallItemSeq":"7","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23398&topic_id=54421","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54421&smallItemId=23398","difficulty":"3"},{"itemId":"23396","smallItemSeq":"5","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23396&topic_id=54423","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54423&smallItemId=23396","difficulty":"3"},{"itemId":"23397","smallItemSeq":"6","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23397&topic_id=54424","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54424&smallItemId=23397","difficulty":"3"},{"itemId":"23395","smallItemSeq":"4","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23395&topic_id=54425","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54425&smallItemId=23395","difficulty":"3"},{"itemId":"23394","smallItemSeq":"3","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23394&topic_id=54427","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54427&smallItemId=23394","difficulty":"3"},{"itemId":"23393","smallItemSeq":"2","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23393&topic_id=54509","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54509&smallItemId=23393","difficulty":"3"},{"itemId":"23392","smallItemSeq":"1","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23392&topic_id=54511","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54511&smallItemId=23392","difficulty":"3"}]}]
     */

    /**
     * bigItemId : 3897
     * bigItemType : 1
     * bigItemTitle : 一.单选题
     * bigItemScore : 30
     * bigItemDesc :
     * smallItemList : [{"itemId":"23401","smallItemSeq":"10","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23401&topic_id=53951","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=53951&smallItemId=23401","difficulty":"3"},{"itemId":"23400","smallItemSeq":"9","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23400&topic_id=54419","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54419&smallItemId=23400","difficulty":"3"},{"itemId":"23399","smallItemSeq":"8","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23399&topic_id=54420","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54420&smallItemId=23399","difficulty":"3"},{"itemId":"23398","smallItemSeq":"7","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23398&topic_id=54421","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54421&smallItemId=23398","difficulty":"3"},{"itemId":"23396","smallItemSeq":"5","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23396&topic_id=54423","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54423&smallItemId=23396","difficulty":"3"},{"itemId":"23397","smallItemSeq":"6","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23397&topic_id=54424","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54424&smallItemId=23397","difficulty":"3"},{"itemId":"23395","smallItemSeq":"4","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23395&topic_id=54425","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54425&smallItemId=23395","difficulty":"3"},{"itemId":"23394","smallItemSeq":"3","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23394&topic_id=54427","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54427&smallItemId=23394","difficulty":"3"},{"itemId":"23393","smallItemSeq":"2","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23393&topic_id=54509","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54509&smallItemId=23393","difficulty":"3"},{"itemId":"23392","smallItemSeq":"1","url":"http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23392&topic_id=54511","detailUrl":"http://test.api.fe520.com/html5/topic?topic_id=54511&smallItemId=23392","difficulty":"3"}]
     */

    private List<ListExerciseItemBean> listExerciseItem;


    public List<ListExerciseItemBean> getListExerciseItem() {
        return listExerciseItem;
    }

    public void setListExerciseItem(List<ListExerciseItemBean> listExerciseItem) {
        this.listExerciseItem = listExerciseItem;
    }

    public static class ListExerciseItemBean {
        private int bigItemId;
        private int bigItemType;
        private String bigItemTitle;
        private int bigItemScore;
        private String bigItemDesc;
        /**
         * itemId : 23401
         * smallItemSeq : 10
         * url : http://test.api.fe520.com/html5/exercise/exercise?smallitem_id=23401&topic_id=53951
         * detailUrl : http://test.api.fe520.com/html5/topic?topic_id=53951&smallItemId=23401
         * difficulty : 3
         */

        private List<SmallItemListBean> smallItemList;

        public int getBigItemId() {
            return bigItemId;
        }

        public void setBigItemId(int bigItemId) {
            this.bigItemId = bigItemId;
        }

        public int getBigItemType() {
            return bigItemType;
        }

        public void setBigItemType(int bigItemType) {
            this.bigItemType = bigItemType;
        }

        public String getBigItemTitle() {
            return bigItemTitle;
        }

        public void setBigItemTitle(String bigItemTitle) {
            this.bigItemTitle = bigItemTitle;
        }

        public int getBigItemScore() {
            return bigItemScore;
        }

        public void setBigItemScore(int bigItemScore) {
            this.bigItemScore = bigItemScore;
        }

        public String getBigItemDesc() {
            return bigItemDesc;
        }

        public void setBigItemDesc(String bigItemDesc) {
            this.bigItemDesc = bigItemDesc;
        }

        public List<SmallItemListBean> getSmallItemList() {
            return smallItemList;
        }

        public void setSmallItemList(List<SmallItemListBean> smallItemList) {
            this.smallItemList = smallItemList;
        }

        public static class SmallItemListBean {
            private String itemId;
            private String smallItemSeq;
            private String url;
            private String detailUrl;
            private String difficulty;

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getSmallItemSeq() {
                return smallItemSeq;
            }

            public void setSmallItemSeq(String smallItemSeq) {
                this.smallItemSeq = smallItemSeq;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getDifficulty() {
                return difficulty;
            }

            public void setDifficulty(String difficulty) {
                this.difficulty = difficulty;
            }
        }
    }
}
