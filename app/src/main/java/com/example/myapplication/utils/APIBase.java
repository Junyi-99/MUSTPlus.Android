package com.example.myapplication.utils;

import android.util.Log;

import com.example.myapplication.interfaces.IAPI;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

// MUSTPlus API 网络层
// 由 API 类调用，用来获取新数据
// **这个类无数据持久化的功能**
// API类 与 APIBase类 都要实现 IAPI 接口
public class APIBase implements IAPI {
    @Override
    public String calc_sign(TreeMap<String, String> get_data, TreeMap<String, String> post_data) {
        StringBuilder get = new StringBuilder();
        if (get_data != null) {
            for (Map.Entry<String, String> entry : get_data.entrySet()) {
                get.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            get = get.deleteCharAt(get.length() - 1);
        }

        StringBuilder post = new StringBuilder();
        if (post_data != null) {
            for (Map.Entry<String, String> entry : post_data.entrySet()) {
                post.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            post = get.deleteCharAt(post.length() - 1);
        }

        return Tools.MD5(String.valueOf(get) + post + AUTH_SECRET);
    }

    @Override
    public String auth_hash() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = APIs.BASE_URL.v() + APIs.AUTH_HASH.v();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();//发送请求
        ResponseBody body = response.body();
        if (body == null)
            throw new IOException("NULL ERROR");
        else
            return body.string();
    }

    @Override
    public String auth_login(String pubkey, String username, String password, String token, String cookies, String captcha) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = APIs.BASE_URL.v() + APIs.AUTH_LOGIN.v();
            RequestBody body = new FormBody.Builder()
                    .add("username", RSAUtils.encrypt(username))
                    .add("password", RSAUtils.encrypt(password))
                    .add("token", RSAUtils.encrypt(token))
                    .add("cookies", RSAUtils.encrypt(cookies))
                    .add("captcha", RSAUtils.encrypt(captcha))
                    .build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null)
                throw new IOException("NULL ERROR");
            else
                return responseBody.string();
        } catch (InvalidKeySpecException e) {
            throw new IOException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new IOException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new IOException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public String auth_logout(String token) {
        return null;
    }

    @Override
    public String course(String token, Integer course_id) {
        return null;
    }

    @Override
    public String course_comment(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_comment_thumbs_up(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_comment_thumbs_down(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String course_ftp(String token, Integer course_id, APIOperation operation) {
        return null;
    }

    @Override
    public String news_all(String token, Integer from, Integer count) {
        return null;
    }

    @Override
    public String news_faculty(String token, String faculty_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String news_department(String token, String department_name_zh, Integer from, Integer count) {
        return null;
    }

    @Override
    public String teacher(String token, String name_zh) {
        return null;
    }

    @Override
    public String timetable(String token, Integer intake, Integer week) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = APIs.BASE_URL.v() + APIs.TIMETABLE.v();
        HttpUrl.Builder httpUrl = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", token);
        params.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("intake", String.valueOf(intake));
        params.put("week", String.valueOf(week));
        params.put("sign", calc_sign(params, null));
        Log.d("TI", url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().url(httpUrl.build()).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null)
            throw new IOException("NULL ERROR");
        else
            return body.string();
    }
}
