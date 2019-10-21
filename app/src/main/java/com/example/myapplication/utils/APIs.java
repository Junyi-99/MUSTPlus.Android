package com.example.myapplication.utils;

// API列表
// 用来规定 API 的 ID 和 对应URL
public enum APIs {
    BASE_URL(1, "http://zh.junyi.pw:6002/"),
    AUTH_HASH(100, "auth/hash"),
    AUTH_LOGIN(101, "auth/login"),
    AUTH_LOGOUT(102, "auth/logout"),
    COURSE_(200, "course/"),
    COURSE_FTP(201, "/ftp"),
    COURSE_COMMENT(301, "/comment"),
    COURSE_COMMENT_THUMBS_UP(302, "/comment/thumbs_up"),
    COURSE_COMMENT_THUMBS_DOWN(303, "/comment/thumbs_down"),
    NEWS_(400, "news/"),
    NEWS_FACULTY_(401, "faculty/"),
    NEWS_DEPARTMENT_(402, "department/"),
    NEWS_BANNERS(403, "banners"),
    NEWS_ALL(404, "all"),
    NEWS_ANNOUNCEMENTS(405, "announcements"),
    NEWS_DOCUMENTS(406, "documents"),
    TEACHER(500, "teacher"),
    TIMETABLE(600, "timetable"),
    BASIC_SEMESTER(700, "basic/semester"),
    BASIC_WEEK(701, "basic/week");
    private int index;
    private String value;

    APIs(int index, String value) {
        this.index = index;
        this.value = value;
    }

    // Set Index
    public void si(int index) {
        this.index = index;
    }

    // Get Index
    public int i() {
        return index;
    }

    // Get Value
    public String v() {
        return this.value;
    }

    // Set Value
    public void sv(String value) {
        this.value = value;
    }
}
