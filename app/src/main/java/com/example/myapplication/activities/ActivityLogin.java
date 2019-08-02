package com.example.myapplication.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.MPAPI;
import com.example.myapplication.utils.Tools;

import java.io.IOException;

public class ActivityLogin extends AppCompatActivity {
    ImageButton image_button_refresh;
    ImageView image_view_captcha;
    EditText edit_text_username;
    EditText edit_text_password;
    EditText edit_text_captcha;
    Button button_login;
    ModelAuthHash modelAuthHash;
    boolean animating = false;

    private void startAnimation() {
        Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true); // 设置保持动画最后的状态
        animation.setDuration(1000); // 设置动画时间
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator()); // 设置插入器
        animating = true;
        image_button_refresh.startAnimation(animation);
    }

    private void stopAnimation() {
        animating = false;

        image_button_refresh.clearAnimation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image_view_captcha = (ImageView) findViewById(R.id.image_view_captcha);
        image_button_refresh = (ImageButton) findViewById(R.id.image_button_refresh);
        edit_text_username = (EditText) findViewById(R.id.edit_text_username);
        edit_text_password = (EditText) findViewById(R.id.edit_text_password);
        edit_text_captcha = (EditText) findViewById(R.id.edit_text_captcha);
        button_login = (Button) findViewById(R.id.button_login);

        image_button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animating) {
                    if (Tools.isNetworkConnected(v.getContext())) {
                        startAnimation();
                        refreshCaptcha();
                    } else {
                        // TODO: POPUP MESSAGE BOX
                    }
                } else {
                    Log.d("TEST", "FORBIDDEN");
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Tools.isNetworkConnected(v.getContext())) {
                    //TODO: Validate the Input
                    DBHelper db = new DBHelper(v.getContext());
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("id", 1);
                    values.put("api", "aaa");
                    values.put("value", "{\"code\":0}");
                    long ret = sqLiteDatabase.insert("api_persistance", null, values);
                    Log.d("SQLITE", String.valueOf(ret));
                    if (ret == -1) {
                        Log.d("log", "sqlitedatabase error");
                    }

                    //login();
                } else {
                    Log.d("CHECK NETWORK", "FALSE");
                }
                //String response = run("http://mp.junyi.pw:8000/auth/hash");
                //Log.d("RESPONSE", response);
            }
        });
    }

    private void login() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try { // 试一下状态机的写法
                            int status = 0;
                            final int STATUS_BEGIN = 0;
                            final int STATUS_END = 1;
                            final int STATUS_OTHER_ERROR = 2;
                            final int STATUS_CAPTCHA_NOT_INITIALIZED = 3;
                            final int STATUS_CAPTCHA_INITIALIZED = 4;
                            final int STATUS_RESULT_IS_NULL = 5;
                            final int STATUS_RESULT_IS_NOT_NULL = 6;
                            final int STATUS_RET_CODE_OK = 7;
                            final int STATUS_RET_CODE_ERROR = 8;
                            String result = null;
                            ModelResponseLogin login = null;
                            while (status != STATUS_END) {
                                switch (status) {
                                    case STATUS_BEGIN:
                                        if (modelAuthHash == null)
                                            status = STATUS_CAPTCHA_NOT_INITIALIZED;
                                        else
                                            status = STATUS_CAPTCHA_INITIALIZED;
                                        break;
                                    case STATUS_CAPTCHA_INITIALIZED:
                                        result = MPAPI.auth_login(modelAuthHash.getKey(),
                                                edit_text_username.getText().toString(),
                                                edit_text_password.getText().toString(),
                                                modelAuthHash.getToken(),
                                                modelAuthHash.getCookies(),
                                                edit_text_captcha.getText().toString());
                                        if (result == null) {
                                            status = STATUS_RESULT_IS_NULL;
                                        } else {
                                            status = STATUS_RESULT_IS_NOT_NULL;
                                        }
                                        break;

                                    case STATUS_RESULT_IS_NOT_NULL:
                                        login = JSON.parseObject(result, ModelResponseLogin.class);
                                        if (login.getCode() != 0)
                                            status = STATUS_RET_CODE_ERROR;
                                        else  // 登陆成功，可以进行下一步操作
                                            status = STATUS_RET_CODE_OK;
                                        break;
                                    case STATUS_CAPTCHA_NOT_INITIALIZED:
                                        showError("请先刷新验证码");
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RESULT_IS_NULL:
                                        showError("result is null!");
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RET_CODE_ERROR:
                                        showError(login.getMsg() + " " + login.getDetail());
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RET_CODE_OK:
                                        showMsg(login.getStudent_name() + " " + login.getToken());
                                        status = STATUS_END;
                                        break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError(e.getMessage());
                        }
                    }
                }
        ).start();
    }

    private void refreshCaptcha() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = MPAPI.auth_hash();
                            updateCaptcha(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError("刷新验证码失败:" + e.getMessage());
                        }
                    }
                }
        ).start();
    }


    private void updateCaptcha(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("RESULT", result);
                modelAuthHash = JSON.parseObject(result, ModelAuthHash.class);
                if (modelAuthHash == null) {
                    Log.d("JSON ERROR", "JSON ERROR");
                    Toast.makeText(getApplicationContext(), "解析验证码失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] decodedString = Base64.decode(modelAuthHash.getCaptcha(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image_view_captcha.setImageBitmap(decodedByte);
                stopAnimation();
            }
        });
    }

    private void showMsg(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showError(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result != null)
                    Log.d("ERROR", result);
                else
                    Log.d("Error", "");
                Toast.makeText(getApplicationContext(), "错误：" + result, Toast.LENGTH_LONG).show();
                stopAnimation();
            }
        });
    }
}
