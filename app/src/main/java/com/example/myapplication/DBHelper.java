package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.APIs;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "test_db";//数据库名字
    private static final int DB_VERSION = 1;   // 数据库版本
    private static String TABLE_NAME = "api_persistence";// 表名

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public long updatePersistence(APIs api, String value) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put("id", api.i());
        values.put("api", api.v());
        values.put("value", value);
        long ret = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return ret;
    }

    // 从数据库中获取登录记录
    // 失败返回 null
    public ModelResponseLogin getLoginRecord() {
        ModelResponseLogin model = null;
        String querySQL =
                "SELECT value" +
                        " FROM " + TABLE_NAME +
                        " WHERE id='" + APIs.AUTH_LOGIN.i() + "'" +
                        " AND api='" + APIs.AUTH_LOGIN.v() + "'";
        Log.d("SQL", querySQL);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(querySQL, null);
        Log.d("COUNT", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            Log.d("MOVETOFIRST", "TRUE");
            model = JSON.parseObject(cursor.getString(0), ModelResponseLogin.class);
        }
        cursor.close();
        db.close();
        return model;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "api TEXT , " +
                "value TEXT , " +
                "time DATETIME DEFAULT CURRENT_TIMESTAMP ); ";

        String sql2 = "CREATE TABLE app (" +
                "latest_app_version TEXT PRIMARY KEY, " +
                "latest_db_version TEXT);";

        db.execSQL(sql1 + sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
