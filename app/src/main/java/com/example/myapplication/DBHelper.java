package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.APIs;
import com.example.myapplication.utils.Tools;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "test_db";//数据库名字
    private static final int DB_VERSION = 1;   // 数据库版本
    private static String TABLE_NAME = "api_persistence";// 表名

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void removeRecord(APIs api) {
        updatePersistence(api, "", 0);
    }

    private long updatePersistence(APIs api, String value, long time) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put("id", api.i());
        values.put("api", api.v());
        values.put("value", value);
        values.put("time", time);

        long ret = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return ret;
    }

    public long updatePersistence(APIs api, String value) {
        return updatePersistence(api, value, Tools.getUTCTimestamp());
    }

    // 从数据库中获取登录记录
    // 失败返回 null
    public ModelResponseLogin getLoginRecord() {
        //model = JSON.parseObject(cursor.getString(0), ModelResponseLogin.class);
        return null;
    }

    // 从数据库中获取API记录
    // 失败返回 空字符串
    public String getRecord(APIs api) {
        String result, time;
        String querySQL
                = "SELECT value, time" +
                " FROM " + TABLE_NAME +
                " WHERE id='" + api.i() + "'" +
                " AND api='" + api.v() + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            time = cursor.getString(1);
            Log.d("TYPE", "Record found");
            if (Tools.getUTCTimestamp() - Integer.valueOf(time) > 60 * 60 * 24 * 7) {
                Log.d("TYPE", "Expired");
                return "";
            } else {
                Log.d("TYPE", "Valid");
                return result;
            }
        } else {
            Log.d("TYPE", "No Record found");
            result = "";
        }
        cursor.close();
        db.close();
        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "api TEXT , " +
                "value TEXT , " +
                "time INTEGER); ";

        String sql2 = "CREATE TABLE app (" +
                "latest_app_version TEXT PRIMARY KEY, " +
                "latest_db_version TEXT);";

        String sql3 = "CREATE TABLE config (" +
                "key TEXT PRIMARY KEY, " +
                "value TEXT);";

        db.execSQL(sql1 + sql2 + sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
