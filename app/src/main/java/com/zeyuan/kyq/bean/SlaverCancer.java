package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/23.
 */
public class SlaverCancer implements Serializable {
    private int SlaverCancerLength;
    private int SlaverCancerWidth;
    private String SlaverCancerName;

    public int getSlaveLen() {
        return SlaverCancerLength;
    }

    public int getSlaveWidth() {
        return SlaverCancerWidth;
    }

    public String getSlaveName() {
        return SlaverCancerName;
    }

    public void setSlaveLen(int slaveLen) {
        SlaverCancerLength = slaveLen;
    }

    public void setSlaveWidth(int slaveWidth) {
        SlaverCancerWidth = slaveWidth;
    }

    public void setSlaveName(String slaveName) {
        SlaverCancerName = slaveName;
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.SlaverCancerLength);
//        dest.writeInt(this.SlaverCancerWidth);
//        dest.writeString(this.SlaverCancerName);
//    }
//
//    public SlaverCancer() {
//    }
//
//    protected SlaverCancer(Parcel in) {
//        this.SlaverCancerLength = in.readInt();
//        this.SlaverCancerWidth = in.readInt();
//        this.SlaverCancerName = in.readString();
//    }
//
//    public static final Parcelable.Creator<SlaverCancer> CREATOR = new Parcelable.Creator<SlaverCancer>() {
//        public SlaverCancer createFromParcel(Parcel source) {
//            return new SlaverCancer(source);
//        }
//
//        public SlaverCancer[] newArray(int size) {
//            return new SlaverCancer[size];
//        }
//    };
}
