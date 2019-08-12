package com.example.myapplication.models;

import java.util.ArrayList;

public class ModelResponseNewsAll extends ModelResponse {
    private Integer records; // 当前记录条数

    private ArrayList<ModelNews> news_list;

    public ModelResponseNewsAll() {
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public ArrayList<ModelNews> getNews_list() {
        return news_list;
    }

    public void setNews_list(ArrayList<ModelNews> news_list) {
        this.news_list = news_list;
    }
}
