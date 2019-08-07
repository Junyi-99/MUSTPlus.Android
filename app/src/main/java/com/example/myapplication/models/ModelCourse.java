package com.example.myapplication.models;

import java.util.ArrayList;

public class ModelCourse extends ModelResponse {


    private String course_code;
    private String course_class;
    private String name_zh;
    private String name_en;
    private String name_short;
    private Double credit;
    private String faculty;
    private ArrayList<ModelTeacher> teachers;
    private ArrayList<ModelSchedule> schedule;
    private String rank;

    public ModelCourse() {

    }

    public ModelCourse(String course_code, String course_class, String name_zh, String name_en, String name_short, Double credit, String faculty, ArrayList<ModelTeacher> teachers, ArrayList<ModelSchedule> schedule, String rank) {
        this.course_code = course_code;
        this.course_class = course_class;
        this.name_zh = name_zh;
        this.name_en = name_en;
        this.name_short = name_short;
        this.credit = credit;
        this.faculty = faculty;
        this.teachers = teachers;
        this.schedule = schedule;
        this.rank = rank;
    }

    public String getCourse_code() {
        if (course_code == null)
            return "UNKNOWN";
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_class() {
        if (course_class == null)
            return "UNKNOWN";
        return course_class;
    }

    public void setCourse_class(String course_class) {
        this.course_class = course_class;
    }

    public String getName_zh() {
        if (name_zh == null)
            return "UNKNOWN";
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public String getName_en() {
        if (name_en == null)
            return getName_zh();
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_short() {
        if (name_short == null)
            return "";
        return name_short;
    }

    public void setName_short(String name_short) {
        this.name_short = name_short;
    }

    public Double getCredit() {
        if (credit == null)
            return (double) 0.0f;
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getFaculty() {
        if (faculty == null)
            return "UNKNOWN";
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public ArrayList<ModelTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<ModelTeacher> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<ModelSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ModelSchedule> schedule) {
        this.schedule = schedule;
    }

    public String getRank() {
        if (rank == null)
            return "0.0";
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
