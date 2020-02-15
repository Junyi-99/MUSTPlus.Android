package com.example.myapplication.interfaces;

import android.support.annotation.Nullable;

import com.example.myapplication.utils.API.APIOperation;

import java.io.IOException;
import java.util.TreeMap;

// MUST Plus APIPersistence 接口
public interface IAPI {

    // 计算 Sign 值
    String calcSign(TreeMap<String, String> get_data, TreeMap<String, String> post_data);

    String authHash() throws IOException;

    String authLogin(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException;

    String authLogout(String token) throws IOException;

    String course(String token, Integer course_id) throws IOException;

    /*
     * @param operation This param determine the action to the comment
     * @return String This returns the json response
     * */
    String courseComment(String token, Integer course_id, APIOperation operation, @Nullable Double rank, @Nullable String content, @Nullable Integer comment_id) throws IOException;

    String courseThumbsUp(String token, Integer comment_id, APIOperation operation) throws IOException;

    String courseThumbsDown(String token, Integer comment_id, APIOperation operation) throws IOException;

    String courseFtp(String token, Integer course_id, APIOperation operation);

    String newsAll(String token, Integer from, Integer count) throws IOException;

    String newsAnnouncements(String token, Integer from, Integer count) throws IOException;

    String newsDocuments(String token, Integer from, Integer count) throws IOException;

    String newsFaculty(String token, String faculty_name_zh, Integer from, Integer count);

    String newsDepartment(String token, String department_name_zh, Integer from, Integer count);

    String studentMe(String token) throws IOException;

    String teacher(String token, String name_zh);

    String timetable(String token, Integer intake, Integer week) throws IOException;

    String semester() throws IOException;

    String week() throws IOException;
}
