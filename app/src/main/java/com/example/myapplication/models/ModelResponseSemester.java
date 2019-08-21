package com.example.myapplication.models;

public class ModelResponseSemester extends ModelResponse {
    private Integer semester;

    public ModelResponseSemester() {

    }

    public Integer getSemester() {
        if (semester == null)
            return 0;
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}
