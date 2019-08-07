package com.example.myapplication.models;

public class ModelResponseLogin extends ModelResponse {
    private String student_name;
    private String token;

    public ModelResponseLogin() {
        
    }

    public String getStudent_name() {
        if (student_name == null)
            return "";
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getToken() {
        if (token == null)
            return "";
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
