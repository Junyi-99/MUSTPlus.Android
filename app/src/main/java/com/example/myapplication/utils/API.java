package com.example.myapplication.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.DBHelper;
import com.example.myapplication.interfaces.IAPI;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;

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

    // new 完了可以设置强制更新。value为true时，不使用缓存
    public void setForceUpdate(boolean value) {
        this.forceUpdate = value;
    }


    //TODO： Error Handler 用来处理登录信息失效的状态，并且从数据库里抹去登录信息

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
        if (record.isEmpty() || forceUpdate) {
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
        Log.i("R_course", "Get Record");

        if (course == null || forceUpdate) {
            Log.i("R_course", "Request New Data");
            String raw = base.course(token, course_id);
            Log.i("R_course", raw);
            course = JSON.parseObject(raw, ModelCourse.class);
            if (course.getCode() == 0) {
                Log.i("R_course", "Saving Data");
                db.setCourseRecord(course_code, course_class, raw);
            }
            return course;
        } else {
            Log.i("R_course", "Found Old Data");
            return course;
        }
    }

    @Deprecated
    @Override
    public String course(String token, Integer course_id) throws IOException {
        // Please DO NOT USE THIS METHOD
        return null;
    }

    @Nullable
    public ModelResponseCourseComment course_comment_get(String token, Integer course_id) throws IOException {
        try {
            Log.i("R_course_comment_get", "Get Record");
            DBHelper db = new DBHelper(context);
            String record = db.getCourseCommentRecord(course_id, 7);
            if (record.isEmpty() || forceUpdate) {
                record = base.course_comment(token, course_id, APIOperation.GET, null, null, null);
                ModelResponseCourseComment comment = JSON.parseObject(record, ModelResponseCourseComment.class);
                if (comment.getCode() == 0) {
                    db.setCourseCommentRecord(course_id, record);
                }
            }
            return JSON.parseObject(record, ModelResponseCourseComment.class);
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public ModelResponseCourseComment course_comment_post(String token, Integer course_id, Double rank, String content) throws IOException {
        Log.i("R_course_comment_post", "Get Record");
        try {
            Log.i("R_course_comment_post", "Request New Data");
            String raw = base.course_comment(token, course_id, APIOperation.POST, rank, content, null);
            Log.i("R_course_comment_post", raw);
            return JSON.parseObject(raw, ModelResponseCourseComment.class);
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public ModelResponseCourseComment course_comment_delete(String token, Integer course_id, Integer comment_id) throws IOException {
        Log.i("R_course_comment_delete", "Get Record");
        try {
            Log.i("R_course_comment_delete", "Request New Data");
            String raw = base.course_comment(token, course_id, APIOperation.DELETE, null, null, comment_id);
            Log.i("R_course_comment_delete", raw);
            return JSON.parseObject(raw, ModelResponseCourseComment.class);
        } catch (JSONException e) {
            return null;
        }
    }

    @Deprecated
    @Override
    public String course_comment(String token, Integer course_id, APIOperation operation, @Nullable Double rank, @Nullable String content, @Nullable Integer comment_id) {
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

    @Nullable
    public ModelResponseNewsAll news_announcements_get(String token, Integer from, Integer count) throws IOException {
        Log.i("R_news_announcement_get", "Get Record");
        try {
            Log.i("R_news_announcement_get", "Request New Data");
            String raw = base.news_announcements(token, from, count);
            Log.i("R_news_announcement_get", raw);
            return JSON.parseObject(raw, ModelResponseNewsAll.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    @Override
    public String news_announcements(String token, Integer from, Integer count) throws IOException {
        return null;
    }

    @Nullable
    public ModelResponseNewsAll news_documents_get(String token, Integer from, Integer count) throws IOException {
        try {
            return JSON.parseObject(base.news_documents(token, from, count), ModelResponseNewsAll.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    @Override
    public String news_documents(String token, Integer from, Integer count) throws IOException {
        return null;
    }

    @Nullable
    public ModelResponseNewsAll news_all_get(String token, Integer from, Integer count) throws IOException {
        try {
            DBHelper db = new DBHelper(context);
            ModelResponseNewsAll record = db.getNewsRecord();
            Log.i("R_news_all_get", "Get Record");
            if (record == null || forceUpdate) {
                Log.i("R_news_all_get", "Request New Data");
                String raw = base.news_all(token, from, count);
                Log.i("R_news_all_get", raw);
                ModelResponseNewsAll news = JSON.parseObject(raw, ModelResponseNewsAll.class);

                if (news.getCode() == 0) {
                    Log.i("R_news_all_get", "Saving data");
                    db.setAPIRecord(APIs.NEWS_ALL, raw);
                }
                return news;
            } else {
                Log.i("R_news_all_get", "Found Old Data");
                return record;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    @Override
    public String news_all(String token, Integer from, Integer count) throws IOException {
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
        String record = db.getAPIRecord(APIs.BASIC_SEMESTER);
        Log.i("R_semester", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.semester();
            db.setAPIRecord(APIs.BASIC_SEMESTER, record);
            return record;
        } else {
            return record;
        }
    }

    public ModelResponseSemester semester_get() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecordTimeLimit(APIs.BASIC_SEMESTER, 10);
        ModelResponseSemester semester;
        Log.i("R_semester", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.semester();
            semester = JSON.parseObject(record, ModelResponseSemester.class);
            if (semester.getCode() == 0)
                db.setAPIRecord(APIs.BASIC_SEMESTER, record);
            return semester;
        } else {
            return JSON.parseObject(record, ModelResponseSemester.class);
        }
    }

    public ModelResponseWeek week_get() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecordTimeLimit(APIs.BASIC_WEEK, 1);
        ModelResponseWeek week;
        Log.i("R_week", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.week();
            week = JSON.parseObject(record, ModelResponseWeek.class);
            if (week.getCode() == 0)
                db.setAPIRecord(APIs.BASIC_WEEK, record);
            return week;
        } else {
            return JSON.parseObject(record, ModelResponseWeek.class);
        }
    }

    @Deprecated
    @Override
    public String week() throws IOException {
        return null;
    }
}
