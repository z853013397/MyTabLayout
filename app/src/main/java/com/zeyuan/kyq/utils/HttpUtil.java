package com.zeyuan.kyq.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2015/11/18.
 */
public  final class HttpUtil {
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static final boolean  hasNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null){

            return networkInfo.isAvailable();
        }
        return false;
    }


}
