package com.example.myapplication.utils.API;

import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.DBHelper;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;

import java.io.IOException;

/*
 * 返回 Model 的 API 类
 * 更高级的抽象
 * */
public class API {
    public ModelResponseWeek week_get() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecordTimeLimit(APICONSTANT.BASIC_WEEK, 1);
        ModelResponseWeek week;
        Log.i("R_week", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.week();
            week = JSON.parseObject(record, ModelResponseWeek.class);
            if (week.getCode() == 0)
                db.setAPIRecord(APICONSTANT.BASIC_WEEK, record);
            return week;
        } else {
            return JSON.parseObject(record, ModelResponseWeek.class);
        }
    }

    public ModelResponseSemester semester_get() throws IOException {
        DBHelper db = new DBHelper(context);
        String record = db.getAPIRecordTimeLimit(APICONSTANT.BASIC_SEMESTER, 10);
        ModelResponseSemester semester;
        Log.i("R_semester", record);
        if (record.isEmpty() || forceUpdate) {
            record = base.semester();
            semester = JSON.parseObject(record, ModelResponseSemester.class);
            if (semester.getCode() == 0)
                db.setAPIRecord(APICONSTANT.BASIC_SEMESTER, record);
            return semester;
        } else {
            return JSON.parseObject(record, ModelResponseSemester.class);
        }
    }
    @Nullable
    public ModelResponseNewsAll news_all_get(String token, Integer from, Integer count) throws IOException {
        try {
            DBHelper db = new DBHelper(context);
            ModelResponseNewsAll record = db.getNewsRecord();
            Log.i("R_news_all_get", "Get Record");
            if (record == null || forceUpdate) {
                Log.i("R_news_all_get", "Request New Data");
                String raw = base.newsAll(token, from, count);
                Log.i("R_news_all_get", raw);
                ModelResponseNewsAll news = JSON.parseObject(raw, ModelResponseNewsAll.class);

                if (news.getCode() == 0) {
                    Log.i("R_news_all_get", "Saving data");
                    db.setAPIRecord(APICONSTANT.NEWS_ALL, raw);
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

    @Nullable
    public ModelResponseNewsAll news_documents_get(String token, Integer from, Integer count) throws IOException {
        try {
            return JSON.parseObject(base.newsDocuments(token, from, count), ModelResponseNewsAll.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Nullable
    public ModelResponseNewsAll news_announcements_get(String token, Integer from, Integer count) throws IOException {
        Log.i("R_news_announcement_get", "Get Record");
        try {
            Log.i("R_news_announcement_get", "Request New Data");
            String raw = base.newsAnnouncements(token, from, count);
            Log.i("R_news_announcement_get", raw);
            return JSON.parseObject(raw, ModelResponseNewsAll.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public ModelResponseCourseComment course_comment_post(String token, Integer course_id, Double rank, String content) throws IOException {
        Log.i("R_course_comment_post", "Get Record");
        try {
            Log.i("R_course_comment_post", "Request New Data");
            String raw = base.courseComment(token, course_id, APIOperation.POST, rank, content, null);
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
            String raw = base.courseComment(token, course_id, APIOperation.DELETE, null, null, comment_id);
            Log.i("R_course_comment_delete", raw);
            return JSON.parseObject(raw, ModelResponseCourseComment.class);
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public ModelResponseCourseComment course_comment_get(String token, Integer course_id) throws IOException {
        try {
            Log.i("R_course_comment_get", "Get Record");
            DBHelper db = new DBHelper(context);
            String record = db.getCourseCommentRecord(course_id, 7);
            if (record.isEmpty() || forceUpdate) {
                record = base.courseComment(token, course_id, APIOperation.GET, null, null, null);
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
    // 从数据库中获取登录记录
    // 失败返回 null
    @Nullable
    public ModelResponseLogin getLoginRecord() {
        try {
            return JSON.parseObject(getAPIRecord(APICONSTANT.AUTH_LOGIN), ModelResponseLogin.class);
        } catch (JSONException e) {
            return null;
        }
    }

    // 获取 NewsAll 的数据记录
    @Nullable
    public ModelResponseNewsAll getNewsRecord() {
        try {
            return JSON.parseObject(getAPIRecordTimeLimit(APICONSTANT.NEWS_ALL, 1), ModelResponseNewsAll.class);
        } catch (JSONException e) {
            return null;
        }
    }

}
