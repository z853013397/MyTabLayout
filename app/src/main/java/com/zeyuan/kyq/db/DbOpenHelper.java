package com.zeyuan.kyq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/31.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "zeyuan.db";
    private static DbOpenHelper instance;

    public static final String USER_INFO_TABLE_CREATE = "CREATE TABLE "+
            UserInfoDb.TABlE_NAME + " (" +
            UserInfoDb.COLUMN_NAME + " TEXT,"+
            UserInfoDb.COLUMN_AGE + " TEXT," +
            UserInfoDb.COLUMN_SEX + " TEXT," +
            UserInfoDb.COLUMN_LOC + " TEXT);";

    private DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_INFO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

}