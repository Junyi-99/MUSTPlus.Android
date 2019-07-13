package com.example.myapplication.models;

public class ModelNews {
    public String faculty_department = "";
    public String title = "";
    public String date = "";
    public boolean type; // false: downContent, true: viewContent
    public String url = "";
    public int image;

    public ModelNews(String faculty_department, String title, String date, boolean type, String url) {
        this.faculty_department = faculty_department;
        this.title = title;
        this.date = date;
        this.type = type;
        this.url = url;

    }

    public ModelNews(String faculty_department, String title, String date, boolean type, String url, int image) {
        this.faculty_department = faculty_department;
        this.title = title;
        this.date = date;
        this.type = type;
        this.url = url;
        this.image = image;
    }
}
