package com.zeyuan.kyq.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class ConfigBean {
    public Diagnose diagnose;
    public Perform perform;
    public BodyPos BodyPos;
    public Cure cure;
    public CancerPos CancerPos;

    /**
     * 同步配置信息
     */
    public static class Diagnose {
        public String maxtimestamp;
        public List<Fuzuoyong> data;
        public static class Fuzuoyong {
            public String did;
            public String cancerid;
            public String cancertypeid;
            public String maybeQuestion;
            public String stepid;
            public String performid;
            public String cure;
        }
    }



    public static class Perform {
        public String maxtimestamp;
        public List<Zhengzhuang> data;

        public static class Zhengzhuang {
            public String pid;
            public String performname;
        }
    }



    public static class BodyPos {
        public String maxtimestamp;
        public List<Weizhi> data;

        public static class Weizhi {
            public String BodyPosid;
            public String BodyPosname;
        }
    }


    public static class Cure {
        public String maxtimestamp;
        public List<FangAn> data;

        public static class FangAn {
            public String cureid;
            public String curename;
            public List<Step> step;

            public static class Step {
                public String StepID;
                public String Stepname;
            }
        }
    }


    public static class CancerPos {
        public String maxtimestamp;
        public List<WeizhiCure> data;
        public static class WeizhiCure {
            public String CancerPosID;
            public String Cancername;
            public List<CureType> type;

            public static class CureType {
                public String CancerTypeID;
                public String CancerTypename;
            }
        }
    }
}
