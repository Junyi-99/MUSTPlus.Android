package com.example.myapplication.utils.API;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.DBHelper;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelResponse;
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;
import com.example.myapplication.models.ModelTimetable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/*
 * 返回 Model 的 API 类
 * 更高级的抽象
 * */
public class API {
    private boolean forceUpdate = false;
    private Context context;

    public API(@Nullable Context context, boolean forceUpdate) {
        this.context = context;
        this.forceUpdate = forceUpdate;
    }


    // new 完了可以设置强制更新。value为true时，不使用缓存
    public void setForceUpdate(boolean value) {
        this.forceUpdate = value;
    }

    @NotNull
    public ModelAuthHash authHash() throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.authHash();
        return JSON.parseObject(json, ModelAuthHash.class);
    }

    @Nullable
    public ModelResponseLogin authLogin(String pubkey, String username, String password, String token, String cookies, String captcha) {
        try {
            APIPersistence api = new APIPersistence(context, forceUpdate);
            String json = api.authLogin(pubkey, username, password, token, cookies, captcha);
            return JSON.parseObject(json, ModelResponseLogin.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ModelResponseWeek week_get() throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.week();
        return JSON.parseObject(json, ModelResponseWeek.class);
    }

    public ModelResponseSemester semester_get() throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.semester();
        return JSON.parseObject(json, ModelResponseSemester.class);
    }

    @Nullable
    public ModelResponseNewsAll news_all_get(String token, Integer from, Integer count) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.newsAll(token, from, count);
        return JSON.parseObject(json, ModelResponseNewsAll.class);
    }

    @Nullable
    public ModelResponseNewsAll news_documents_get(String token, Integer from, Integer count) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.newsDocuments(token, from, count);
        return JSON.parseObject(json, ModelResponseNewsAll.class);
    }


    @Nullable
    public ModelResponseNewsAll news_announcements_get(String token, Integer from, Integer count) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.newsAnnouncements(token, from, count);
        return JSON.parseObject(json, ModelResponseNewsAll.class);
    }

    @Nullable
    public ModelResponseCourseComment course_comment_post(String token, Integer course_id, Double rank, String content) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.courseComment(token, course_id, APIOperation.POST, rank, content, null);
        return JSON.parseObject(json, ModelResponseCourseComment.class);
    }

    @Nullable
    public ModelResponseCourseComment course_comment_delete(String token, Integer course_id, Integer comment_id) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.courseComment(token, course_id, APIOperation.DELETE, null, null, comment_id);
        return JSON.parseObject(json, ModelResponseCourseComment.class);
    }

    @Nullable
    public ModelResponseCourseComment course_comment_get(String token, Integer course_id) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.courseComment(token, course_id, APIOperation.GET, null, null, null);
        return JSON.parseObject(json, ModelResponseCourseComment.class);
    }

    public ModelTimetable timetable(){
        return timetable(null,null,null);
    }
    @Nullable
    public ModelTimetable timetable(String token, Integer intake, Integer week) {
        try {
            APIPersistence api = new APIPersistence(context, forceUpdate);
            String json = api.timetable(token, intake, week);
            return JSON.parseObject(json, ModelTimetable.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取 ModelCourse 数据
    // 如果获取成功（code==0）持久化到数据库
    // 否则不持久化
    // 调用者应该检测返回值是否为null，且调用 getCode() 方法检查是否有错误
    @Nullable
    public ModelCourse course_get(String token, Integer course_id) throws IOException {
        APIPersistence api = new APIPersistence(context, forceUpdate);
        String json = api.course(token, course_id);
        return JSON.parseObject(json, ModelCourse.class);
    }

    // 从数据库中获取登录记录
    // 失败返回 null
    @Nullable
    public ModelResponseLogin loginRecord() {
        try {
            APIPersistence api = new APIPersistence(context, false);
            String json = api.authLogin(null, null, null, null, null, null);
            return JSON.parseObject(json, ModelResponseLogin.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public ModelResponse logout(String token) {
        try {
            APIPersistence api = new APIPersistence(context, forceUpdate);
            String json = api.authLogout(token);
            return JSON.parseObject(json, ModelResponse.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
