package com.example.myapplication.models;

public class ModelResponse {
    private Integer code;
    private String msg;
    private String detail;

    public ModelResponse() {

    }

    public String getDetail() {
        if (detail == null)
            return "";
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCode() {
        if (code == null)
            return -1;
        return code;
    }

    public void setCode(Integer code) {
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
