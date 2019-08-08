package com.example.myapplication.utils;

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
    // 方法介绍：用来给 HTTP请求 添加 token time sign 三个参数，使用方法很简单
    // 只要传入 HttpUrl.Builder 和 TreeMap<String, String> 和 token 即可
    // 因为Java内部使用指针，传入的 httpUrl 和 params 都是指针形式
    // 所以在这个方法内 put() 和 addQueryParameter() 会影响到 caller 那边（也就是外面）的值
    // 相当于（但严格意义上不是）引用传递了
    private void boxing(HttpUrl.Builder httpUrl, TreeMap<String, String> params, String token) {
        params.put("token", token);
        params.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("sign", calc_sign(params, null));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }
    }

    // boxing 的另一种写法，内部帮你获得 HttpUrl.Builder
    private HttpUrl.Builder boxing(String url, TreeMap<String, String> params, String token) {
        HttpUrl.Builder httpUrl = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        boxing(httpUrl, params, token);
        return httpUrl;
    }

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
        String url = APIs.BASE_URL.v() + APIs.AUTH_HASH.v();

        OkHttpClient client = new OkHttpClient();
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
            String url = APIs.BASE_URL.v() + APIs.AUTH_LOGIN.v();

            OkHttpClient client = new OkHttpClient();
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
    public String course(String token, Integer course_id) throws IOException {
        String url = APIs.BASE_URL.v() + APIs.COURSE_.v() + course_id;

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpUrl = boxing(url, new TreeMap<String, String>(), token);

        Request request = new Request.Builder().url(httpUrl.build()).build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();

        if (body == null)
            throw new IOException("NULL ERROR");
        else
            return body.string();
    }


    @Override
    public String course_comment(String token, Integer course_id, APIOperation operation) throws IOException {
        String url = APIs.BASE_URL.v() + APIs.COURSE_.v() + course_id + APIs.COURSE_COMMENT.v();
        OkHttpClient client = new OkHttpClient();
        switch (operation) {
            case GET:
                HttpUrl.Builder httpUrl = boxing(url, new TreeMap<String, String>(), token);
                Request request = new Request.Builder().url(httpUrl.build()).build();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                if (body == null)
                    throw new IOException("NULL ERROR");
                else
                    return body.string();
            case POST:
                break;
            case DELETE:
                break;
        }
        throw new IOException("NULL ERROR");
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
        String url = APIs.BASE_URL.v() + APIs.TIMETABLE.v();
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpUrl = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("intake", String.valueOf(intake));
        params.put("week", String.valueOf(week));

        boxing(httpUrl, params, token);

        Request request = new Request.Builder().url(httpUrl.build()).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null)
            throw new IOException("NULL ERROR");
        else
            return body.string();
    }
}
