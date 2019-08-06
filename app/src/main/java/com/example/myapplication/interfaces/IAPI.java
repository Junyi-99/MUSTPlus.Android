package com.example.myapplication.interfaces;

import com.example.myapplication.utils.APIOperation;

import java.util.TreeMap;

// MUST Plus API 定义
public interface IAPI {

    String calc_sign(TreeMap<String, String> get_data, TreeMap<String, String> post_data);

    String auth_hash();

    String auth_login(String pubkey, String username, String password, String token, String cookies, String captcha);

    String auth_logout(String token);

    String course(String token, Integer course_id);

    String course_comment(String token, Integer course_id, APIOperation operation);

    String course_comment_thumbs_up(String token, Integer course_id, APIOperation operation);

    String course_comment_thumbs_down(String token, Integer course_id, APIOperation operation);

    String course_ftp(String token, Integer course_id, APIOperation operation);

    String news_all(String token, Integer from, Integer count);

    String news_faculty(String token, String faculty_name_zh, Integer from, Integer count);

    String news_department(String token, String department_name_zh, Integer from, Integer count);

    String teacher(String token, String name_zh);

    String timetable(String token, Integer intake, Integer week);
}
