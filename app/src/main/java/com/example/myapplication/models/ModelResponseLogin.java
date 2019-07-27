package com.example.myapplication.models;

public class ModelResponseLogin extends ModelResponse {
    private String student_name;
    private String token;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
