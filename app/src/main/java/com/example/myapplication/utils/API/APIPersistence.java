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
public class APIPersistence extends APIAbstract {
    private enum Records {
        AUTH_LOGIN,
        COURSE,
    }


    private boolean forceUpdate = false;
    private Context context;

    public APIPersistence(@Nullable Context context) {
        this.context = context;
    }

    // new 完了可以设置强制更新。value为true时，不使用缓存
    public void setForceUpdate(boolean value) {
        this.forceUpdate = value;
    }


    //TODO： Error Handler 用来处理登录信息失效的状态，并且从数据库里抹去登录信息

    @Override
    public String authLogin(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(Records.AUTH_LOGIN.ordinal());
        if (record.isEmpty() || forceUpdate) {
            record = authLogin(pubkey, username, password, token, cookies, captcha);
            db.setAPIRecord(Records.AUTH_LOGIN.ordinal(), record);
        }
        return record;
    }


    @Override
    public String course(String token, Integer course_id) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getCourseRecord(course_id);
        if (record.isEmpty() || forceUpdate) {
            record = course(token, course_id);
            db.setCourseRecord(course_id, record);
        }
        return record;
    }


    @Override
    public String courseComment(String token, Integer course_id, APIOperation operation, @Nullable Double rank, @Nullable String content, @Nullable Integer comment_id) throws IOException {
        DBHelper db = new DBHelper(context);

        if (operation == APIOperation.GET) { // using cache
            String record = db.getCourseCommentRecord(course_id, 7);
            if (record.isEmpty() || forceUpdate) {
                record = courseComment(token, course_id, operation, rank, content, comment_id);
                db.setCourseCommentRecord(course_id, record); // update record
            }
            return record;
        } else { // POST or DELETE not use cache
            return courseComment(token, course_id, operation, rank, content, comment_id);
        }
    }

    @Override
    public String courseCommentThumbsUp(@NonNull String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String courseCommentThumbsDown(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String courseFtp(String token, Integer course_id, APIOperation operation) {
        return null;
    }


    @Override
    public String newsAnnouncements(String token, Integer from, Integer count) throws IOException {
        return null;
    }


    @Override
    public String newsDocuments(String token, Integer from, Integer count) throws IOException {
        return null;
    }


    @Override
    public String newsAll(String token, Integer from, Integer count) throws IOException {
        return null;
    }

    @Override
    public String newsFaculty(String token, String faculty_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String newsDepartment(String token, String department_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String teacher(String token, String name_zh) {
        return null;
    }

    @Override
    public String timetable(String token, Integer intake, Integer week) throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(APICONSTANT.TIMETABLE);
        Log.i("R_timetable", record);
        if (record.isEmpty() || forceUpdate) {
            return base.timetable(token, intake, week);
        } else {
            return record;
        }
    }

    @Override
    public String semester() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecord(APICONSTANT.BASIC_SEMESTER);
        Log.i("R_semester", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.semester();
            db.setAPIRecord(APICONSTANT.BASIC_SEMESTER, record);
            return record;
        } else {
            return record;
        }
    }


    @Override
    public String week() throws IOException {
        return null;
    }
}
