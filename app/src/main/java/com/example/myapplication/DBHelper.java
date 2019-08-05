package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.myapplication.utils.MPAPI;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "test_db";//数据库名字
    private static final int DB_VERSION = 1;   // 数据库版本
    public static String TABLE_NAME = "api_persistence";// 表名

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public long updatePersistence(int id, String api, String value) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put("id", MPAPI.api_auth_login_id);
        values.put("api", MPAPI.api_auth_login);
        values.put("value", value);
        long ret = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return ret;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "api TEXT , " +
                "value TEXT); ";

        String sql2 = "CREATE TABLE app (" +
                "latest_app_version TEXT PRIMARY KEY, " +
                "latest_db_version TEXT);";

        db.execSQL(sql1 + sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
