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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.utils.Tools;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityLogin extends AppCompatActivity {
    ImageButton image_button_refresh;
    ImageView image_view_captcha;
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

                } else {
                    Log.d("CHECK NETWORK", "FALSE");
                }
                //String response = run("http://mp.junyi.pw:8000/auth/hash");
                //Log.d("RESPONSE", response);
            }
        });
    }

    private void refreshCaptcha() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        String url = getString(R.string.api_base_url) + getString(R.string.api_hash);

                        Request request = new Request.Builder().url(url).build();
                        try {
                            Response response = client.newCall(request).execute();//发送请求
                            String result = response.body().string();
                            updateCaptcha(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError(e.getMessage());
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
                    return; // TODO: ALERT MESSAGE BOX
                }
                byte[] decodedString = Base64.decode(modelAuthHash.getCaptcha(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image_view_captcha.setImageBitmap(decodedByte);
                stopAnimation();
            }
        });
    }


    private void showError(final String result) {
        //stopAnimation();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("ERROR", result);
            }
        });
    }
}
