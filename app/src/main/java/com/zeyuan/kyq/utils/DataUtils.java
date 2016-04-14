package com.zeyuan.kyq.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.zeyuan.kyq.app.GlobalData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/28.
 * 数据工具类 把后台数据转为显示的数据和把显示数据转为上传给后台需要的数据
 */
public final class DataUtils {
    private static final String TAG = "ChooseMedicaDialog";

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int ch = -1;
        byte[] buffer = new byte[1024 * 4];
        while ((ch = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, ch);
        }
        baos.flush();
        baos.close();
        inputStream.close();
        String responseXml = baos.toString();
        return responseXml;
    }

    public String getPostString(String path, Context context) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(500);
        connection.setReadTimeout(500);
        connection.connect();
        if (connection.getResponseCode() != 200) {
            Toast.makeText(context, "没网", Toast.LENGTH_SHORT).show();
            return null;
        }
        InputStream inputStream = connection.getInputStream();
        String s = inputStreamToString(inputStream);
        return s;
    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 由stepUID组成的list转化为显示的string
     * 如：易瑞沙，特罗凯，紫杉醇
     *
     * @param list 这个list里面装的是 药物id组成的list
     * @return 需要显示的string字符串，使用时请判断string是否为空
     */

    public static String listToString(List<String> list) {
        String result = "";
        LogUtil.i(TAG, "选中药物的个数是：" + list.size());
        if (list == null && list.size() == 0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = GlobalData.cureValues.get(list.get(i));
            if (i == 0) {
                result = str;
            } else {
                result = result + ",".concat(str);
            }
        }
        return result;

    }

    /**
     * 由stepUID组成的list转化为要提交给后台的string
     * 如5001,5002
     *
     * @param list 由stepUID组成的list
     * @return 后台需要的string
     */
    public static String listToLoadString(List<String> list) {
        String result = "";
        if (list == null && list.size() == 0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (i == 0) {
                result = str;
            } else {
                result = result + ",".concat(str);
            }
        }
        return result;
    }


    /**
     * 把后台传的String转化为显示的String
     * 如5001,5002 转化为 易瑞沙，特罗凯
     *
     * @param loadString 需要转化的数据
     * @return 显示在屏幕的数据
     */
    public static String loadStringToShowString(String loadString) {
        String result = "";
        String[] strings = loadString.split(",");
        for (int i = 0; i < strings.length; i++) {
            String str = GlobalData.cureValues.get(strings[i]);
//            if(TextUtils.isEmpty(str)){
//                return loadString;
//            }

            if (i == 0) {
                result = str;
            } else {
                result = result + ",".concat(str);
            }
        }
        return result;
    }

    /**
     * 把在桌面是显示的时间 如2015-07-08转化为需要上传给服务器的时间（秒数）
     *
     * @return
     */
    public static String showTimeToLoadTime(String showtime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String loadingTime = "";
        try {
            Date time = format.parse(showtime);
            long timenum = time.getTime();
            loadingTime = String.valueOf(timenum / 1000);
//            loadingTime = temp.substring(0, temp.length() - 3);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
        return loadingTime;
    }


    /**
     * 这样处理是后台给的数据是秒
     * @param date 后台给的数据加上“000”
     * @return 返回一个需要的日期字符串
     */
    public static String getDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }


    public static String getDateDetail(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }


    public static String getDates() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 得到当前的日期
     *
     * @return 日期
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到后台返回数据的日期 几号
     *
     * @param inputtime 秒数
     * @return 几号
     */
    public static int getInputDay(String inputtime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Integer.valueOf(inputtime + "000"));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前的小时 如2015-11-19 17:10 得到17
     *
     * @return 小时
     */
    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到后台返回数据的日期 几号 如2015-11-19 17:10 得到17
     *
     * @param inputtime 秒数
     * @return 几号
     */
    public static int getInputHour(String inputtime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Integer.valueOf(inputtime + "000"));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到当前的分钟 如2015-11-19 17:10 得到10
     *
     * @return 分钟
     */
    public static int getCurrentMin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 得到后台的分钟
     *
     * @return 分钟
     */
    public static int getInputMin(String inputTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Integer.valueOf(inputTime + "000"));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 判断是否显示日期
     *
     * @param inputTime
     * @return true表示超过10天 false 没有超过10天
     */
    public static boolean isShowTime(String inputTime) {
        long showTime = Integer.valueOf(inputTime);
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
        if (day > 10) {
            return true;
        }
        return false;
    }

    /**
     * > 10天显示日期
     * < 10天 显示几天前 其中以前显示昨天
     * < 1天显示 多少小时前
     * <  1小时 显示多少分钟前
     * < 5分钟 显示刚刚
     *
     * @param "后台传的秒数"
     * @return 所需要显示的字符串
     */
    public static String showDay(String inputtime) {
        if (isShowTime(inputtime)) {//是否超过10天
            return getDate(inputtime);
        }
        //没有超过10天
        int day = getCurrentDay() - getInputDay(inputtime);//判断后台输入的时间和现在的时间相隔的天数
        if (day > 2) { //大于2天 小于10天
            return day + "天前";
        }
        if (day >= 1 && day <= 2) { //大于1 小鱼2
            return "昨天";
        }
        //剩下的都是在1天之类
        int hour = getCurrentHour() - getInputHour(inputtime);//相差的小时
        if (hour >= 1) {//超过一个小时
            return hour + "小时前";
        }
        //剩下的都是在1小时之类
        int min = getCurrentMin() - getInputMin(inputtime);//相差的分钟
        if (min < 5) {
            return "刚刚";
        }

        if (min >= 5) {
            return min + "分钟前";
        }

        return null;
    }


    public static String getVersionName(Context context) {
        String version = "1.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


}
