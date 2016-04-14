package com.zeyuan.kyq.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zeyuan.kyq.bean.PatientDetailBean;

/**
 * Created by Administrator on 2015/8/31.
 */
public class UserInfoDb {
    public static final String TABlE_NAME = "user_info";
    public static final String COLUMN_NAME = "patient_name";
    public static final String COLUMN_AGE = "patient_age";
    public static final String COLUMN_LOC = "patient_loc";
    public static final String COLUMN_SEX = "patient_sex";
    private SQLiteDatabase db;

    public UserInfoDb(Context context) {

        db = DbOpenHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * @param patientDetailBean
     * 增加
     */
    public void savePatientInfo(PatientDetailBean patientDetailBean) {
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
//            values.put(UserInfoDb.COLUMN_NAME, patientDetailBean.name);
//            values.put(UserInfoDb.COLUMN_SEX, patientDetailBean.sex);
//            values.put(UserInfoDb.COLUMN_AGE, patientDetailBean.age);
//            values.put(UserInfoDb.COLUMN_LOC, patientDetailBean.city);
            db.insert(UserInfoDb.TABlE_NAME, null, values);
        }
    }

    /**
     * @param username
     * 删除
     */
    public void deletePatientInfo(String username) {
        if (db.isOpen()) {
            db.delete(UserInfoDb.TABlE_NAME, UserInfoDb.COLUMN_NAME + " = ?", new String[]{username});
        }
    }


    public PatientDetailBean getPatientInfo() {
        if(db.isOpen()) {
            PatientDetailBean patientDetailBean = new PatientDetailBean();
            Cursor cursor = db.rawQuery("select * from " + TABlE_NAME, null);
            while (cursor.moveToNext()) {
//                patientDetailBean.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
//                patientDetailBean.age = cursor.getString(cursor.getColumnIndex(COLUMN_AGE));
//                patientDetailBean.sex = cursor.getString(cursor.getColumnIndex(COLUMN_SEX));
//                patientDetailBean.city = cursor.getString(cursor.getColumnIndex(COLUMN_LOC));
            }
            return patientDetailBean;
        }
        return null;
    }


    public void replacePatientInfo() {

    }


    public void updataPatientName(String patientName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,patientName);
        if (db.isOpen()) {
            db.update(TABlE_NAME, values, null, null);
        }
    }

    public void updataPatientAge(String patientAge) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,patientAge);
        if (db.isOpen()) {
            db.update(TABlE_NAME, values, null, null);
        }
    }

}
