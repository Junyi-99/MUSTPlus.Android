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
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.models.ModelResponse;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.API;
import com.example.myapplication.utils.APIs;
import com.example.myapplication.utils.Tools;

import java.io.IOException;

public class ActivityLogin extends AppCompatActivity {
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

    private void setComponentAvailable(final boolean available) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edit_text_captcha.setEnabled(available);
                edit_text_password.setEnabled(available);
                edit_text_username.setEnabled(available);
                button_login.setClickable(available);
                image_button_refresh.setClickable(available);
            }
        });
    }

    private void startLogin() {
        flagLoginInProcess = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setComponentAvailable(false);
                button_login.setText("正 在 登 录");
            }
        });
    }

    private void stopLogin() {
        flagLoginInProcess = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setComponentAvailable(true);
                button_login.setText("登 录");
            }
        });
    }

    private void startRefreshCaptchaAnimation() {
        Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true); // 设置保持动画最后的状态
        animation.setDuration(1000); // 设置动画时间
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator()); // 设置插入器
        flagRefreshCaptchaInProcess = true;
        // 这里不用 setComponentAvailable()
        // 因为刷新验证码的时候要允许用户输入用户名密码
        button_login.setClickable(false);
        image_button_refresh.setClickable(false);
        image_button_refresh.startAnimation(animation);
    }

    private void stopRefreshCaptchaAnimation() {
        flagRefreshCaptchaInProcess = false;
        button_login.setClickable(true);
        image_button_refresh.setClickable(true);
        image_button_refresh.clearAnimation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        button_login = findViewById(R.id.button_login);
        edit_text_captcha = findViewById(R.id.edit_text_captcha);
        edit_text_password = findViewById(R.id.edit_text_password);
        edit_text_username = findViewById(R.id.edit_text_username);
        image_view_captcha = findViewById(R.id.image_view_captcha);
        image_button_refresh = findViewById(R.id.image_button_refresh);
        text_view_login_hint = findViewById(R.id.text_view_login_hint);

        image_button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagRefreshCaptchaInProcess) {
                    if (Tools.isNetworkConnected(v.getContext())) {
                        startRefreshCaptchaAnimation();
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
                if (!flagLoginInProcess) {
                    if (Tools.isNetworkConnected(v.getContext())) {
                        //TODO: Validate the Input
                        startLogin();
                        login();
                    } else {
                        Log.d("CHECK NETWORK", "FALSE");
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        if (!flagLoginSuccessful) {
            System.exit(0);
        }
        super.onStop();
    }

    private void login() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {// 试一下状态机的写法
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

                            while (status != STATUS_END) {
                                switch (status) {
                                    case STATUS_BEGIN:
                                        if (modelAuthHash == null)
                                            status = STATUS_CAPTCHA_NOT_INITIALIZED;
                                        else
                                            status = STATUS_CAPTCHA_INITIALIZED;
                                        break;
                                    case STATUS_CAPTCHA_INITIALIZED:
                                        setHintText("正在请求登录");
                                        API api = new API(getApplicationContext());
                                        result = api.auth_login(modelAuthHash.getKey(),
                                                edit_text_username.getText().toString(),
                                                edit_text_password.getText().toString(),
                                                modelAuthHash.getToken(),
                                                modelAuthHash.getCookies(),
                                                edit_text_captcha.getText().toString());

                                        if (result.isEmpty()) {
                                            status = STATUS_RESULT_IS_NULL;
                                        } else {
                                            status = STATUS_RESULT_IS_NOT_NULL;
                                        }
                                        break;

                                    case STATUS_RESULT_IS_NOT_NULL:
                                        setHintText("正在解析数据");
                                        modelResponseLogin = JSON.parseObject(result, ModelResponseLogin.class);
                                        if (modelResponseLogin.getCode() != 0)
                                            status = STATUS_RET_CODE_ERROR;
                                        else  // 登陆成功，可以进行下一步操作
                                            status = STATUS_RET_CODE_OK;
                                        break;
                                    case STATUS_CAPTCHA_NOT_INITIALIZED:
                                        setHintText(R.string.login_hint_text);
                                        showError("请先刷新验证码");
                                        stopLogin();
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RESULT_IS_NULL:
                                        setHintText(R.string.login_hint_text);
                                        showError("服务器返回空 NULL");
                                        stopLogin();
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RET_CODE_ERROR:
                                        showError(modelResponseLogin.getMsg() + " " + modelResponseLogin.getDetail());
                                        setHintText(R.string.login_hint_text);
                                        stopLogin();
                                        status = STATUS_END;
                                        break;
                                    case STATUS_RET_CODE_OK:
                                        showMsg(modelResponseLogin.getStudent_name() + " " + modelResponseLogin.getToken());

                                        DBHelper db = new DBHelper(getApplicationContext());
                                        long ret = db.setAPIRecord(APIs.AUTH_LOGIN, result);

                                        setHintText("欢迎您，" + modelResponseLogin.getStudent_name() + "。正在更新您的课表，请稍后");
                                        timetable();

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

    // 获取课程表
    private void timetable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API api = new API(getApplicationContext());
                    String result = null;
                    result = api.timetable(modelResponseLogin.getToken(), 1909, 0);
                    ModelResponse response = JSON.parseObject(result, ModelResponse.class);
                    DBHelper db = new DBHelper(getApplicationContext());
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
                        stopLogin();
                        setHintText(R.string.login_hint_text);
                        showError(response.getMsg());
                    }

                    Log.d("TIMETABLEE", result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void refreshCaptcha() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            API api = new API(getApplicationContext());
                            String result = api.auth_hash();
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
                modelAuthHash = JSON.parseObject(result, ModelAuthHash.class);
                if (modelAuthHash == null) {
                    Toast.makeText(getApplicationContext(), "解析验证码失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] decodedString = Base64.decode(modelAuthHash.getCaptcha(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image_view_captcha.setImageBitmap(decodedByte);
                stopRefreshCaptchaAnimation();
            }
        });
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
                stopRefreshCaptchaAnimation();
            }
        });
    }
}
