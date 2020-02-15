package com.example.myapplication.utils.API;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.DBHelper;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;

import java.io.IOException;

// MUSTPlus APIPersistence
// 带数据持久化的 API 类，但仍然返回 String
// 请求这里面的方法，会自动进行持久化判断
// 如果需要获取新数据则调用 APIAbstract 中的方法，并且持久化到数据库中
// 所有方法都有可能返回NULL
public class APIPersistence extends APIAbstract {
    private enum Records {
        AUTH_LOGIN,
        AUTH_LOGOUT,
        AUTH_HASH,
        COURSE,
        TIMETABLE,
        SEMESTER,
        WEEK,
        NEWS_ALL,
        NEWS_DEPARTMENT,
        NEWS_FACULTY,
        NEWS_DOCUMENT
    }


    private boolean forceUpdate = false;
    private Context context;

    public APIPersistence(@Nullable Context context, boolean forceUpdate) {
        this.context = context;
        this.forceUpdate = forceUpdate;
    }

    public APIPersistence(@Nullable Context context) {
        this.context = context;
    }

    // new 完了可以设置强制更新。value为true时，不使用缓存
    public void setForceUpdate(boolean value) {
        this.forceUpdate = value;
    }


    //TODO： Error Handler 用来处理登录信息失效的状态，并且从数据库里抹去登录信息
    @Override
    public String authHash() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.AUTH_HASH.ordinal());
        if (record.isEmpty() || forceUpdate) {
            record = super.authHash();
            db.setAPIRecord(Records.AUTH_HASH.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String authLogin(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.AUTH_LOGIN.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.authLogin(pubkey, username, password, token, cookies, captcha);
            db.setAPIRecord(Records.AUTH_LOGIN.ordinal(), record);
        }
        return record;
    }

    @Override
    public String authLogout(String token) throws IOException {
        DBHelper db = new DBHelper(context);
        db.setAPIRecord(Records.AUTH_LOGIN.ordinal(), "");
        db.setAPIRecord(Records.TIMETABLE.ordinal(), "");
        return super.authLogout(token);
    }

    @Nullable
    @Override
    public String course(String token, Integer course_id) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getCourseRecord(course_id);
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.course(token, course_id);
            db.setCourseRecord(course_id, record);
        }
        return record;
    }

    @Nullable
    @Override
    public String courseComment(String token, Integer course_id, APIOperation operation, @Nullable Double rank, @Nullable String content, @Nullable Integer comment_id) throws IOException {
        DBHelper db = new DBHelper(context);
        if (operation == APIOperation.GET) { // using cache
            String record = db.getCourseCommentRecord(course_id, 7);
            if (token != null && (record.isEmpty() || forceUpdate)) {
                record = super.courseComment(token, course_id, operation, rank, content, comment_id);
                db.setCourseCommentRecord(course_id, record); // update record
            }
            return record;
        } else { // POST or DELETE not use cache
            return super.courseComment(token, course_id, operation, rank, content, comment_id);
        }
    }

    @Nullable
    @Override
    public String courseFtp(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Nullable
    @Override
    public String newsAnnouncements(String token, Integer from, Integer count) throws IOException {
        return null;
    }

    @Nullable
    @Override
    public String newsDocuments(String token, Integer from, Integer count) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.NEWS_DOCUMENT.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.newsDocuments(token, from, count);
            db.setAPIRecord(Records.NEWS_DOCUMENT.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String newsAll(String token, Integer from, Integer count) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.NEWS_ALL.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.newsAll(token, from, count);
            db.setAPIRecord(Records.NEWS_ALL.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String newsFaculty(String token, String faculty_name_zh, Integer from, Integer count) {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.NEWS_FACULTY.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.newsFaculty(token, faculty_name_zh, from, count);
            db.setAPIRecord(Records.NEWS_FACULTY.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String newsDepartment(String token, String department_name_zh, Integer from, Integer count) {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.NEWS_DEPARTMENT.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.newsDepartment(token, department_name_zh, from, count);
            db.setAPIRecord(Records.NEWS_DEPARTMENT.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String teacher(String token, String name_zh) {
        return null;
    }

    @Nullable
    @Override
    public String timetable(String token, Integer intake, Integer week) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.TIMETABLE.ordinal());
        if (token != null && (record.isEmpty() || forceUpdate)) {
            record = super.timetable(token, intake, week);
            db.setAPIRecord(Records.TIMETABLE.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String semester() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.SEMESTER.ordinal());
        if (record.isEmpty() || forceUpdate) {
            record = super.semester();
            db.setAPIRecord(Records.SEMESTER.ordinal(), record);
        }
        return record;
    }

    @Nullable
    @Override
    public String week() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.WEEK.ordinal());
        if (record.isEmpty() || forceUpdate) {
            record = super.week();
            db.setAPIRecord(Records.WEEK.ordinal(), record);
        }
        return record;
    }
}
