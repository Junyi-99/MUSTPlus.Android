package com.example.myapplication.models;

public class ModelResponse {
    private int code;
    private String msg;
    private String detail;

    public String getDetail() {
        if (detail == null)
            return "";
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        if (msg == null)
            return "";
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
