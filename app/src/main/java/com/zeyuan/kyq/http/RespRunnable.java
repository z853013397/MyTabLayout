package com.zeyuan.kyq.http;

/**
 * Created by guogzhao on 16/1/18.
 */
public interface RespRunnable<T> {
//     void onResp(int reqCode,Object response);

     void onResp(int reqCode,T resp);
}
