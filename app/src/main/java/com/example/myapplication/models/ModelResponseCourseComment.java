package com.example.myapplication.models;

import java.util.ArrayList;

public class ModelResponseCourseComment extends ModelResponse {
    private ArrayList<ModelCourseComment> comments;

    public ModelResponseCourseComment() {

    }

    public ArrayList<ModelCourseComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<ModelCourseComment> comments) {
        this.comments = comments;
    }
}
