package com.zhu.xc.jsdemo;

import java.util.List;

/**
 * Created by xc on 2016/4/26.
 */
public class DemoBean {

    /**
     * total : 33
     * smokes : [{"uid":"6","power":"14","sid":"33","duration":"2","time":"1461636239","voltage":"5"},{"uid":"6","power":"14","sid":"32","duration":"2","time":"1461636242","voltage":"5"},{"uid":"6","power":"14","sid":"31","duration":"2","time":"1461636241","voltage":"5"},{"uid":"6","power":"14","sid":"30","duration":"2","time":"1461636240","voltage":"5"},{"uid":"6","power":"14","sid":"29","duration":"2","time":"1461636240","voltage":"5"},{"uid":"6","power":"14","sid":"28","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"27","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"26","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"25","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"24","duration":"2","time":"0","voltage":"5"}]
     * num : 1
     */

    private DataBean data;
    /**
     * data : {"total":"33","smokes":[{"uid":"6","power":"14","sid":"33","duration":"2","time":"1461636239","voltage":"5"},{"uid":"6","power":"14","sid":"32","duration":"2","time":"1461636242","voltage":"5"},{"uid":"6","power":"14","sid":"31","duration":"2","time":"1461636241","voltage":"5"},{"uid":"6","power":"14","sid":"30","duration":"2","time":"1461636240","voltage":"5"},{"uid":"6","power":"14","sid":"29","duration":"2","time":"1461636240","voltage":"5"},{"uid":"6","power":"14","sid":"28","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"27","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"26","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"25","duration":"2","time":"4294967295","voltage":"5"},{"uid":"6","power":"14","sid":"24","duration":"2","time":"0","voltage":"5"}],"num":1}
     * msg :
     * code : 1
     */

    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        private String total;
        private int num;
        /**
         * uid : 6
         * power : 14
         * sid : 33
         * duration : 2
         * time : 1461636239
         * voltage : 5
         */

        private List<SmokesBean> smokes;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<SmokesBean> getSmokes() {
            return smokes;
        }

        public void setSmokes(List<SmokesBean> smokes) {
            this.smokes = smokes;
        }

        public static class SmokesBean {
            private String uid;
            private String power;
            private String sid;
            private String duration;
            private String time;
            private String voltage;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getSid() {
                return sid;
            }

            public void setSid(String sid) {
                this.sid = sid;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getVoltage() {
                return voltage;
            }

            public void setVoltage(String voltage) {
                this.voltage = voltage;
            }
        }
    }
}
