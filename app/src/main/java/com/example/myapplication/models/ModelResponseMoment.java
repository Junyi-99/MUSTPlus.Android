package com.example.myapplication.models;

import java.util.List;

public class ModelResponseMoment {


    private Integer moment_id;
    private String student_id;
    private String avatar_url;
    private Integer avatar_img;
    private String nickname;
    private String publish_time;
    private String content;
    private Integer likes;
    private Integer comments;
    private List<String> img_url;

    public ModelResponseMoment() {

    }

    public ModelResponseMoment(Integer moment_id, String student_id, String avatar_url, Integer avatar_img, String nickname, String publish_time, String content, Integer likes, Integer comments, List<String> img_url) {
        this.moment_id = moment_id;
        this.student_id = student_id;
        this.avatar_url = avatar_url;
        this.avatar_img = avatar_img;
        this.nickname = nickname;
        this.publish_time = publish_time;
        this.content = content;
        this.likes = likes;
        this.comments = comments;
        this.img_url = img_url;
    }


    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(int avatar_img) {
        this.avatar_img = avatar_img;
    }

    public Integer getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(Integer moment_id) {
        this.moment_id = moment_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }
}

/*

{
    ""
}




*/