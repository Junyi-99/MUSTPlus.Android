package com.example.myapplication.utils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MPAPI {

    private static String api_base_url = "http://mp.junyi.pw:8000/";
    private static String api_hash = "auth/hash";
    private static String api_login = "auth/login";

    public static String auth_hash() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = api_base_url + api_hash;
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();//发送请求
        return response.body().string();
    }

    public static String auth_login(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = api_base_url + api_login;
            RequestBody body = new FormBody.Builder()
                    .add("username", RSAUtils.encrypt(pubkey, username))
                    .add("password", RSAUtils.encrypt(pubkey, password))
                    .add("token", RSAUtils.encrypt(pubkey, token))
                    .add("cookies", RSAUtils.encrypt(pubkey, cookies))
                    .add("captcha", RSAUtils.encrypt(pubkey, captcha))
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
