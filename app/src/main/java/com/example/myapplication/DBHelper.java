package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelResponse;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.utils.Tools;

import org.jetbrains.annotations.NotNull;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "must_plus";//数据库名字
    private static final int DB_VERSION = 2;   // 数据库版本
    private static String TABLE_APIS = "apis";// 表名
    private static String TABLE_CONFIG = "configs";// 表名
    private static String TABLE_COURSE = "courses";// 表名
    private static String TABLE_TEACHER = "teachers";// 表名
    private static String TABLE_STUDENT = "students";// 表名
    private static String TABLE_COURSE_COMMENT = "course_comments";// 表名
    private final int API_EXPIRE_DAYS = 3; // API_PERSISTENCE记录 3天后 失效
    private final int COURSE_EXPIRES_DAY = 30; // COURSE记录 30天后 失效
    private final int TEACHER_EXPIRES_DAY = 30; // TEACHER记录 30天后 失效
    private final int STUDENT_EXPIRES_DAY = 7; // STUDENT记录 7天后 失效

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
     * Set Id Value Time Record 有冲突默认覆盖
     * @param id: 要设置的ID
     * @param value: 要设置json
     * @param table: 你要操作的表
     */
    private long setIVTRecord(final int id, final String json, final String table) {

        // 禁止缓存 code != 0 的 Response
        try {
            ModelResponse decode = JSON.parseObject(json, ModelResponse.class);
            if (decode == null || decode.getCode() != 0) {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("id", id);
        values.put("value", json);
        values.put("time", Tools.getUTCTimestamp());

        long ret = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return ret;
    }

    /*
     * 获取 Id Value Time 类型的表的 Value 数据，并且限制時間，超時的數據返回空字符串
     * @param days: 限制在距离今天 days 天内的数据
     * @param table: 你要操作的表
     */
    private String getIVTRecord(final int id, final int days, final String table) {
        String result = "";
        String querySQL
                = "SELECT value FROM " + table +
                " WHERE id = " + id +
                " AND time > " + (Tools.getUTCTimestamp() - 60 * 60 * 24 * days);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor.moveToFirst())
            result = cursor.getString(0);

        cursor.close();
        db.close();
        return result;
    }

    public long setAPIRecord(int id, String value) {
        return setIVTRecord(id, value, TABLE_APIS);
    }

    public String getAPIRecord(int id) {
        return getIVTRecord(id, API_EXPIRE_DAYS, TABLE_APIS);
    }

    public long setCourseRecord(final int course_id, final String json_value) {
        return setIVTRecord(course_id, json_value, TABLE_COURSE);
    }

    public String getCourseRecord(final int course_id) {
        return getIVTRecord(course_id, COURSE_EXPIRES_DAY, TABLE_COURSE);
    }

    public long setCourseCommentRecord(final int course_id, final String json_value) {
        return setIVTRecord(course_id, json_value, TABLE_COURSE_COMMENT);
    }

    public String getCourseCommentRecord(int course_id, int days) {
        return getIVTRecord(course_id, days, TABLE_COURSE_COMMENT);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_apis = "CREATE TABLE " + TABLE_APIS + " (" +
                "id INTEGER PRIMARY KEY," +
                "value TEXT," +
                "time INTEGER); ";

        String sql_config = "CREATE TABLE " + TABLE_CONFIG + " (" +
                "config TEXT PRIMARY KEY," +
                "value TEXT);";

        String sql_courses = "CREATE TABLE " + TABLE_COURSE + " (" +
                "id INTEGER PRIMARY KEY," +
                "value TEXT," +
                "time INTEGER);";

        String sql_comments = "CREATE TABLE " + TABLE_COURSE_COMMENT + " (" +
                "id INTEGER PRIMARY KEY," +
                "value TEXT," +
                "time INTEGER);";

        String sql_teachers = "CREATE TABLE " + TABLE_TEACHER + " (" +
                "name_zh TEXT PRIMARY KEY," +
                "value TEXT," +
                "time INTEGER);";

        String sql_students = "CREATE TABLE " + TABLE_STUDENT + " (" +
                "student_id TEXT PRIMARY KEY," +
                "value TEXT," +
                "time INTEGER);";


        db.execSQL(sql_apis);
        db.execSQL(sql_config);
        db.execSQL(sql_courses);
        db.execSQL(sql_comments);
        db.execSQL(sql_teachers);
        db.execSQL(sql_students);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("onUpgrade", "" + oldVersion + "," + newVersion);
        switch (oldVersion) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;

        }
    }
}
