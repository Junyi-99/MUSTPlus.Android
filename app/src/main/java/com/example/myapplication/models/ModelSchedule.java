package com.example.myapplication.models;

public class ModelSchedule {
    private Integer intake;
    private String date_begin;
    private String date_end;
    private String time_begin;
    private String time_end;
    private Integer day_of_week;
    private String classroom;

    public ModelSchedule() {

    }

    public ModelSchedule(Integer intake, String date_begin, String date_end, String time_begin, String time_end, Integer day_of_week, String classroom) {
        this.intake = intake;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.time_begin = time_begin;
        this.time_end = time_end;
        this.day_of_week = day_of_week;
        this.classroom = classroom;
    }

    public Integer getIntake() {
        if (intake == null)
            return 0;
        return intake;
    }

    public void setIntake(Integer intake) {
        this.intake = intake;
    }

    public String getDate_begin() {
        if (date_begin == null)
            return "XX-XX";
        return date_begin;
    }

    public void setDate_begin(String date_begin) {
        this.date_begin = date_begin;
    }

    public String getDate_end() {
        if (date_end == null)
            return "XX-XX";
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getTime_begin() {
        if (time_begin == null)
            return "XX:XX";
        return time_begin;
    }

    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    public String getTime_end() {
        if (time_end == null)
            return "XX:XX";
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public Integer getDay_of_week() {
        if (day_of_week == null)
            return 1;
        return day_of_week;
    }

    public void setDay_of_week(Integer day_of_week) {
        this.day_of_week = day_of_week;
    }

    public String getClassroom() {
        if (classroom == null)
            return "UNKNOWN";
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
