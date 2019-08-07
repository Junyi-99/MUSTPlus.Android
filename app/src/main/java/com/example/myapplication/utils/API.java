package com.example.myapplication.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.interfaces.IAPI;
import com.example.myapplication.models.ModelCourse;

import java.io.IOException;
import java.util.TreeMap;

// MUSTPlus API
// 带数据持久化
// 请求这里面的方法，会自动进行持久化判断
// 如果需要获取新数据则调用 APIBase 中的方法，并且持久化到数据库中
public class API implements IAPI {
    private APIBase base = new APIBase();
    private boolean forceUpdate = false;
    private Context context;

    public API(@Nullable Context context) {
        this.context = context;
    }

    public void setForceUpdate(boolean value) {
        this.forceUpdate = value;
    }

    @Override
    public String calc_sign(TreeMap<String, String> get_data, TreeMap<String, String> post_data) {
        return base.calc_sign(get_data, post_data);
    }

    @Override
    public String auth_hash() throws IOException {
        return base.auth_hash();
    }

    @Override
    public String auth_login(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(APIs.AUTH_LOGIN);
        if (record.isEmpty()) {
            return base.auth_login(pubkey, username, password, token, cookies, captcha);
        } else {
            return record;
        }
    }

    @Override
    public String auth_logout(String token) {
        return null;
    }

    // 获取 ModelCourse 数据
    // 如果获取成功（code==0）持久化到数据库
    // 否则不持久化
    // 调用者应该检测返回值是否为null，且调用 getCode() 方法检查是否有错误
    @Nullable
    public ModelCourse course(String token, Integer course_id, String course_code, String course_class) throws IOException {
        DBHelper db = new DBHelper(context);
        ModelCourse course = db.getCourseRecord(course_code, course_class);
        if (course == null) {
            Log.d("API Course", "Request new data");
            String raw = base.course(token, course_id);
            Log.d("API Course", raw);
            course = JSON.parseObject(raw, ModelCourse.class);
            if (course.getCode() == 0) {
                Log.d("API Course", "Saving data");
                db.setCourseRecord(course_code, course_class, raw);
            }
            return course;
        } else {
            Log.d("API Course", "Found old data");
            return course;
        }
    }

    @Deprecated
    @Override
    public String course(String token, Integer course_id) throws IOException {
        // Please DO NOT USE THIS METHOD
        return null;
    }

    @Override
    public String course_comment(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_comment_thumbs_up(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_comment_thumbs_down(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_ftp(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String news_all(String token, Integer from, Integer count) {
        return null;
    }

    @Override
    public String news_faculty(String token, String faculty_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String news_department(String token, String department_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String teacher(String token, String name_zh) {
        return null;
    }

    @Override
    public String timetable(String token, Integer intake, Integer week) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(APIs.TIMETABLE);
        if (record.isEmpty()) {
            return base.timetable(token, intake, week);
        } else {
            return record;
        }
    }
}
