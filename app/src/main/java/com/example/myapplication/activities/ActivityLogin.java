package com.example.myapplication.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.models.ModelResponse;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.API;
import com.example.myapplication.utils.APIs;
import com.example.myapplication.utils.Tools;

import java.io.IOException;

/**
 * @author Junyi
 */
public class ActivityLogin extends AppCompatActivity {
    public static boolean active = false;
    Button button_login;
    EditText edit_text_captcha;
    EditText edit_text_password;
    EditText edit_text_username;
    ImageView image_view_captcha;
    ImageButton image_button_refresh;
    TextView text_view_login_hint;

    ModelAuthHash modelAuthHash;
    ModelResponseLogin modelResponseLogin;

    boolean flagRefreshCaptchaInProcess = false;
    boolean flagLoginInProcess = false;
    boolean flagLoginSuccessful = false; // 判断用户是否登录成功

    private void setHintText(final int text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text_view_login_hint.setText(text);
            }
        });
    }

    private void setHintText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text_view_login_hint.setText(text);
            }
        });
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_new);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagLoginInProcess) {
                    if (Tools.isNetworkConnected(v.getContext())) {
                        //TODO: Validate the Input
                        //startLogin();

                    } else {
                        Log.d("CHECK NETWORK", "FALSE");
                    }
                }
            }
        });
    }


    // 获取课程表
    private void timetable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                API api = new API(getApplicationContext());
                DBHelper db = new DBHelper(getApplicationContext());
                try {
                    result = api.timetable(modelResponseLogin.getToken(), 1909, 0);
                    ModelResponse response = JSON.parseObject(result, ModelResponse.class);

                    if (response.getCode() == 0) {
                        // 成功
                        long ret = db.setAPIRecord(APIs.TIMETABLE, result);

                        setHintText("一切准备就绪，欢迎使用MUST+！");
                        Thread.sleep(1000);
                        flagLoginSuccessful = true;
                        exit();// 返回上一级
                    } else {
                        // 出错
                        if (response.getCode() == -7003) { // Cookie 过期
                            db.removeRecord(APIs.AUTH_LOGIN);
                        }
                        db.removeRecord(APIs.TIMETABLE);
                        //stopLogin();
                        setHintText(R.string.login_hint_text);
                        showError(response.getMsg());
                    }

                    Log.d("TIMETABLEE", result);
                } catch (IOException | InterruptedException e) {
                    db.removeRecord(APIs.TIMETABLE);
                    //stopLogin();
                    setHintText(R.string.login_hint_text);
                    showError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void exit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
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
                //stopRefreshCaptchaAnimation();
            }
        });
    }
}
