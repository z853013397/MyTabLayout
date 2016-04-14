package com.zeyuan.kyq.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/8/24.
 * 打印管理类
 */
public final class LogUtil {
    private static final int FLAG = 1;


    public static void i(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void i(String TAG, boolean mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, int mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, float mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void e(String TAG, String mes) {
        if (FLAG > 0)
            Log.e(TAG, mes);
    }

    public static void v(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void d(String TAG, String mes) {
        if (FLAG > 0)
            Log.d(TAG, mes);
    }
}
