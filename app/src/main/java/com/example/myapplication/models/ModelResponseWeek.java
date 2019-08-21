package com.example.myapplication.models;

public class ModelResponseWeek extends ModelResponse {
    private Integer week;

    public ModelResponseWeek() {

    }

    public Integer getWeek() {
        if (week == null)
            return 0;
        return week;
    }

    public void setWeek(Integer semester) {
        this.week = semester;
    }
}
