package com.zeyuan.kyq;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Base64;
import android.util.Log;

import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testBase64() {
//        String path = "/sdcard/";
        String str1 = "5L2T6YOo5Ly9546b5YiA6YCC5a6c6IK655mM44CB6IKd55mM44CB6IOw6IW655mM44CB6IK+5LiK6IW66IK/55ik44CB6IK+55mM44CB57q16ZqU6IK/55ik44CB6IW56Iac5ZCO6IK/55ik5Y+K55uG6IWU6IK/55ik562J55qE5rK755aX44CC5L2T6YOo5Ly9546b5YiA5Y+v5Y2V54us55So5LqO5bCP6IK/55ik5qC55rK75rK755aX77yM5Lmf5Y+v57uT5ZCI5aSa56eN5pS+55aX5oqA5pyv44CCDQogICAgICDkvZPpg6jkvL3njpvliIDlnKjmsrvnlpfml7bkuI3lvIDliIDjgIHkuI3purvphonvvIzml6DliJvkvKTjgIHlia/kvZznlKjovbvvvIzml6DpnIDkvY/pmaLvvIzlm6DmraTnibnliKvpgILlrpzlubTogIHkvZPlvLHkuI3og73ogJDlj5fmiYvmnK/nmoTmgqPogIXmiJblm6DmgZDmg6fmi5Lnu53miYvmnK/nmoTmgqPogIXnmoTmsrvnlpfjgII=";
        byte[] b = str1.getBytes();
        String s = new String(Base64.decode(b, Base64.DEFAULT));
        LogUtil.i("MainActivity", "解码的数据是:" + s);

//        File[] files = null;
//        files = new File(path).listFiles();
//        for(int i = 0;i < files.length;i ++) {
//            LogUtil.i("MainActivity",files[i].getPath());
//        }
//        int i = 3;
//        getSome(i);
//        LogUtil.i("MainActivity",i);


        String str = "Hello!";
// 在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
        String strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        Log.i("Test", "encode >>>" + strBase64);

// 这里 encodeToString 则直接将返回String类型的加密数据
        String enToStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        Log.i("Test", "encodeToString >>> " + enToStr);

// 对base64加密后的数据进行解密
        Log.i("Test", "decode >>>" + new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT)));
    }


    public void testMillis() {
        String time = "1648715133".concat("000");
        LogUtil.i("MainActivity", time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = format.format(new Date(Long.parseLong(time)));
        LogUtil.i("MainActivity", str);
    }


    public void testCode() {
        String code = DataUtils.getVersionName(getContext());
        LogUtil.i("MainActivity", "版本号是：" + code);
    }


    public void testInsert() {
        String imageurl = "123544.png";
        StringBuilder builder = new StringBuilder(imageurl);
        int index = builder.indexOf(".");
        builder.insert(index, "thumb");
        LogUtil.i("MainActivity", "新的url是：" + builder);
    }
}