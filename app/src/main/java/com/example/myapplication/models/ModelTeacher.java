package com.example.myapplication.models;

public class ModelTeacher {
    private int avatar_img;
    private String position;
    private String email;
    private String office_room;
    private String office_hour;
    private String name_zh;
    private String name_en;
    private String faculty;
    private String avatar_url;
    private ModelCourse courses;

    public ModelTeacher(String name_zh, String name_en, String faculty, String avatar_url, int avatar_img, String position, String email, String office_room, String office_hour, ModelCourse courses) {
        this.name_zh = name_zh;
        this.name_en = name_en;
        this.faculty = faculty;
        this.avatar_url = avatar_url;
        this.avatar_img = avatar_img;
        this.position = position;
        this.email = email;
        this.office_room = office_room;
        this.office_hour = office_hour;
        this.courses = courses;
    }

    public String getName_zh() {
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOffice_room() {
        return office_room;
    }

    public void setOffice_room(String office_room) {
        this.office_room = office_room;
    }

    public String getOffice_hour() {
        return office_hour;
    }

    public void setOffice_hour(String office_hour) {
        this.office_hour = office_hour;
    }

    public ModelCourse getCourses() {
        return courses;
    }

    public void setCourses(ModelCourse courses) {
        this.courses = courses;
    }


    public int getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(int avatar_img) {
        this.avatar_img = avatar_img;
    }


}
