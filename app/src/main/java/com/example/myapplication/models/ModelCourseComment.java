package com.example.myapplication.models;

public class ModelCourseComment {
    private Integer comment_id;
    private String nickname;
    private String name_zh;
    private String student_id;
    private Integer thumbs_up;
    private Integer thumbs_down;
    private Double rank;
    private String content;
    private String publish_time;

    public ModelCourseComment() {

    }

    public ModelCourseComment(Integer comment_id, String nickname, String name_zh, String student_id, Integer thumbs_up, Integer thumbs_down, Double rank, String content, String publish_time) {
        this.comment_id = comment_id;
        this.student_id = student_id;
        this.nickname = nickname;
        this.name_zh = name_zh;
        this.thumbs_up = thumbs_up;
        this.thumbs_down = thumbs_down;
        this.rank = rank;
        this.content = content;
        this.publish_time = publish_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName_zh() {
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public Integer getThumbs_up() {
        if (thumbs_up == null)
            return 0;
        return thumbs_up;
    }

    public void setThumbs_up(Integer thumbs_up) {
        this.thumbs_up = thumbs_up;
    }

    public Integer getThumbs_down() {
        if (thumbs_down == null)
            return 0;
        return thumbs_down;
    }

    public void setThumbs_down(Integer thumbs_down) {
        this.thumbs_down = thumbs_down;
    }

    public Double getRank() {
        if (rank == null)
            return 0.0;
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
}
