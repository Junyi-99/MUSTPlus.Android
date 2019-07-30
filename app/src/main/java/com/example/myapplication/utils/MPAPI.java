package com.example.myapplication.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MPAPI {
    private static String AUTH_SECRET = "flw4\\-t94!09tesldfgio30";
    private static String api_base_url = "http://mp.junyi.pw:8000/";
    private static String api_auth_hash = "auth/hash";
    private static String api_auth_login = "auth/login";
    private static String api_timetable = "timetable";

    public static String auth_hash() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = api_base_url + api_auth_hash;
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();//发送请求
        return response.body().string();
    }

    private static String MD5(String sourceStr) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger bigInt = new BigInteger(1, md.digest(sourceStr.getBytes()));
            s = String.format("%032x", bigInt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static String calc_sign(TreeMap<String, String> get_data, TreeMap<String, String> post_data) {
        StringBuilder get = new StringBuilder();
        if (get_data != null) {
            for (Map.Entry<String, String> entry : get_data.entrySet()) {
                get.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            get.substring(0, get.length() - 2);
        }

        StringBuilder post = new StringBuilder();
        if (post_data != null) {
            for (Map.Entry<String, String> entry : post_data.entrySet()) {
                post.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            post.substring(0, post.length() - 2);
        }
        return MD5(String.valueOf(get) + post + AUTH_SECRET);
    }

    public static String timetable(String token, Integer intake, Integer week) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = api_base_url + api_timetable;
        HttpUrl.Builder httpUrl = HttpUrl.parse(url).newBuilder();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", token);
        params.put("time", String.valueOf((long) (System.currentTimeMillis() / 1000)));
        params.put("intake", String.valueOf(intake));
        params.put("week", String.valueOf(week));
        params.put("sign", calc_sign(params, null));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().url(httpUrl.build()).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String auth_login(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = api_base_url + api_auth_login;
            RequestBody body = new FormBody.Builder()
                    .add("username", RSAUtils.encrypt(username))
                    .add("password", RSAUtils.encrypt(password))
                    .add("token", RSAUtils.encrypt(token))
                    .add("cookies", RSAUtils.encrypt(cookies))
                    .add("captcha", RSAUtils.encrypt(captcha))
                    .build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
