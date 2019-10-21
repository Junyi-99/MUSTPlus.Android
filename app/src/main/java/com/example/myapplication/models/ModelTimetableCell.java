package com.example.myapplication.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ModelTimetableCell {
    private int day;
    private Date time_begin;
    private Date time_end;
    private String course_code;
    private String course_name_zh;
    private String course_class;
    private String classroom;
    private String teacher;
    private Date date_begin;
    private Date date_end;
    private int course_id;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm", Locale.CHINA);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    private double MS_TO_HOURS = (0.00000027777777777777777777777);

    public ModelTimetableCell() {
        // 这里不能写 GMT+8 ，写了的话会少8个小时
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    // 获取开始时间（小时）
    public int getHourBegin() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time_begin);
        return cal.get(Calendar.HOUR);
    }

    public int getMonth_begin() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_begin);
        return cal.get(Calendar.MONTH);
    }

    public int getDay_begin() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_begin);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth_end() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_end);
        return cal.get(Calendar.MONTH);
    }

    public int getDay_end() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_end);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public double duration() {
        return (time_end.getTime() - time_begin.getTime()) * MS_TO_HOURS;
    }

    // 获取应该在哪一列 [0, 6]
    public int getCellDayPosition() {
        return day - 1;
    }

    // 获取应该在哪一行 8点到22点，对应 [0, 13] 行
    public double getCellLinePosition() {
        // 28800000 表示 8 小时，因为时间从 8 点开始
        return (time_begin.getTime() - 28800000) * MS_TO_HOURS;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime_begin() {
        return timeFormat.format(time_begin);
    }

    public void setTime_begin(String time_begin) {
        try {
            this.time_begin = timeFormat.parse(time_begin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTime_end() {
        return timeFormat.format(time_end);
    }

    public void setTime_end(String time_end) {
        try {
            this.time_end = timeFormat.parse(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getCourse_code() {
        if (course_code.isEmpty())
            return "";
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_name_zh() {

        return course_name_zh;
    }

    public void setCourse_name_zh(String course_name_zh) {
        this.course_name_zh = course_name_zh;
    }

    public String getCourse_class() {
        return course_class;
    }

    public void setCourse_class(String course_class) {
        this.course_class = course_class;
    }

    public String getClassroom() {
        if (classroom == null)
            return "";
        int p = classroom.indexOf("（");
        if (p != -1) {
            classroom = classroom.substring(0, p);
        }
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDate_begin() {
        return dateFormat.format(date_begin);
    }

    public void setDate_begin(String date_begin) {
        try {
            this.date_begin = dateFormat.parse(date_begin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSchedule() {
        Log.e("ModelTimetableCell", date_begin.toString());
        return dateFormat2.format(date_begin) + "-" + dateFormat2.format(date_end) + " " +
                getTime_begin() + " - " + getTime_end() + "\n@" + getClassroom();
    }

    public String getDate_end() {
        return dateFormat.format(date_end);
    }

    public void setDate_end(String date_end) {
        try {
            this.date_end = dateFormat.parse(date_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}