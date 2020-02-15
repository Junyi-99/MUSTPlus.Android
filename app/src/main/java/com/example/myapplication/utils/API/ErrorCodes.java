package com.example.myapplication.utils.API;

// 错误代码列表
public enum ErrorCodes {
    OK(0, "正常"),
    WARNING(1, "警告（一般可忽略）"),
    INTERNAL_ERROR(2, "内部错误"),
    PAGE_NOT_FOUND(3, "页面未找到 （404）"),
    MISSING_FIELD(4, "缺少字段"),
    LOGIN_OTHER_ERROR(-900, "其他错误"),
    LOGIN_RSA_ERROR(-901, "登录 RSA 校验失败"),
    LOGIN_USERNAME_INVALID(-902, "登录用户名无效"),
    LOGIN_PASSWORD_ERROR(-903, "密码错误"),
    LOGIN_CAPTCHA_ERROR(-904, "验证码错误"),
    LOGIN_FIELD_ERROR(-905, "个别字段错误"),
    AUTH_UNKNOWN_ERROR(-1000, "未知错误"),
    AUTH_SIGN_VERIFICATION_FAILED(-1001, "sign 验证失败"),
    AUTH_TIME_INVALID(-1002, "请求已过期"),
    AUTH_TOKEN_INVALID(-1003, "token 无效"),
    AUTH_REQUEST_METHOD_ERROR(-1004, "请求方法错误"),
    AUTH_VALIDATE_ARGUMENT_ERROR(-1005, "校验参数错误"),
    COURSE_UNKNOWN_ERROR(-4000, "课程 未知错误"),
    COURSE_COMMENT_UNKNOWN_ERROR(-4001, "评论 未知错误"),
    COURSE_ID_NOT_FOUNT(-4002, "未找到课程ID"),
    COURSE_COMMENT_CONTENT_ILLEGAL(-4003, "要评论的内容无效（非法评论内容）"),
    COURSE_COMMENT_CONTENT_TOO_LONG(-4004, "评论内容太长"),
    COURSE_COMMENT_CONTENT_EMPTY(-4005, "评论内容不能为空"),
    COURSE_COMMENT_ID_NOT_FOUND(-4006, "未找到该评论ID"),
    COURSE_COMMENT_RANK_INVALID(-4007, "给课程的打分值不合法"),
    COURSE_RECORD_FROM_INVALID(-4008, "获取评论时，从第from条开始获取，from参数无效"),
    COURSE_FTP_ID_NOT_FOUNT(-4100, "课程ID未找到"),
    COURSE_FTP_HOST_ILLEGAL(-4101, "FTP 主机名非法"),
    COURSE_FTP_USERNAME_ILLEGAL(-4102, "FTP 用户名非法"),
    COURSE_FTP_PASSWORD_ILLEGAL(-4103, "FTP 密码非法"),
    NEWS_UNKNOWN_ERROR(-4200, "资讯 未知错误"),
    TEACHER_ID_NOT_FOUNT(-5000, "未找到教师"),
    PROFILE_UNKNOWN_ERROR(-6000, "未知错误"),
    PROFILE_NICKNAME_ILLEGAL(-6001, "昵称非法"),
    PROFILE_SIGNATURE_ILLEGAL(-6002, "个性签名非法"),
    PROFILE_AVATAR_INVALID(-6003, "头像URL不合法"),
    PROFILE_REFRESH_USER_NOT_FOUND(-6004, "从COES获取最新信息失败：找不到用户"),
    PROFILE_REFRESH_COOKIES_EXPIRED(-6005, "从COES获取最新信息失败：用户Cookie失效"),
    TIMETABLE_UNKNOWN_EXCEPTION(-7000, "未知异常"),
    TIMETABLE_SEMESTER_INVALID(-7001, "学期无效"),
    TIMETABLE_WEEK_INVALID(-7002, "周数无效"),
    TIMETABLE_COOKIE_EXPIRED(-7003, "timetable 请求中发现 coes Cookie 过期"),
    SPACE_USERNAME_INVALID(-8000, "用户名无效"),
    OTHER_ARGUMENT_INVALID(-9000, "其它类型参数错误"),
    TIMETABLE(600, "timetable");

    private int index;
    private String value;

    ErrorCodes(int index, String value) {
        this.index = index;
        this.value = value;
    }

    // Get Index
    public int i() {
        return index;
    }

    // Get Value
    public String v() {
        return this.value;
    }
}