package com.example.myapplication;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "test_db";//数据库名字
    private static final int DB_VERSION = 1;   // 数据库版本
    public static String TABLE_NAME = "person";// 表名
    public static String FIELD_ID = "id";// 列名
    public static String FIELD_NAME = "name";// 列名

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE api_persistance (" +
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
