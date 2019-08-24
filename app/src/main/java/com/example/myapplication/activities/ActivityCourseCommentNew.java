package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.API;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.io.IOException;

public class ActivityCourseCommentNew extends AppCompatActivity {
    TextView text_view_rate;
    RatingBar rating_bar;
    EditText edit_text_content;
    ImageButton image_button_check;
    private int course_id;
    // 对应的描述文本
    private String[] rating_description = {
            "害群之马",
            "一败涂地",
            "惨不忍睹",
            "胸无点墨",
            "差强人意",
            "中规中矩",
            "一般般",
            "还不错",
            "有意思",
            "太好了",
            "好极了",
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_comment_new);

        Intent intent = getIntent();
        course_id = intent.getIntExtra("course_id", -1);

        initComponent();
        initButtons();
    }

    // 仅限在UI线程外使用（因为方法内会切换回UI线程）
    private void setEnableInThread(boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image_button_check.setEnabled(true);
                edit_text_content.setEnabled(true);
                rating_bar.setEnabled(true);
            }
        });
    }

    private void publishComment(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper helper = new DBHelper(getApplicationContext());
                API api = new API(getApplicationContext());
                ModelResponseCourseComment comment;
                final ModelResponseLogin login = helper.getLoginRecord();
                if (login != null) {
                    try {
                        comment = api.course_comment_post(login.getToken(), course_id, (double) rating_bar.getRating(), text);

                        if (comment == null) {
                            Snackbar.make(text_view_rate, "错误：服务器返回空", Snackbar.LENGTH_SHORT).show();
                            setEnableInThread(true);
                            return;
                        }
                        if (comment.getCode() != 0) {
                            Snackbar.make(text_view_rate, "错误："+comment.getMsg(), Snackbar.LENGTH_SHORT).show();
                            setEnableInThread(true);
                            return;
                        }

                        setResult(666);
                        finish();

                    } catch (IOException e) {
                        Snackbar.make(text_view_rate, "错误：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        setEnableInThread(true);
                    }
                }
            }
        }).start();
    }

    private void initButtons() {
        // 返回按钮
        ((ImageButton) findViewById(R.id.image_button_more)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 询问是否退出再退出
                ActivityCourseCommentNew.super.onBackPressed();
            }
        });
        image_button_check = (ImageButton) findViewById(R.id.image_button_check);
        image_button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edit_text_content.getText().toString();
                if (text.trim().isEmpty()) {
                    Snackbar.make(text_view_rate, "评论内容不可以为空（0~512字符）", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (text.length() > 512) {
                    Snackbar.make(text_view_rate, "评论内容太长！（0~512字符）", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                image_button_check.setEnabled(false);
                edit_text_content.setEnabled(false);
                rating_bar.setEnabled(false);
                publishComment(text);
            }
        });
    }

    private void initComponent() {
        edit_text_content = (EditText) findViewById(R.id.edit_text_content);
        text_view_rate = (TextView) findViewById(R.id.text_view_rate);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);

        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                text_view_rate.setText(rating_description[(int) (rating * 2)]);


                SpringSystem springSystem = SpringSystem.create();
                Spring spring = springSystem.createSpring();
                spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50, 3));
                spring.setCurrentValue(1f);
                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 1f - (value * 0.5f);
                        //rating_bar.setTranslationY(value * 40);
                        text_view_rate.setScaleY(scale);
                        text_view_rate.setScaleX(scale);
                    }
                });
                spring.setEndValue(0f);
            }
        });


        Intent intent = getIntent();
        //course_id = intent.getIntExtra("course_id", 0);
        //course_code = intent.getStringExtra("course_code");
        //course_class = intent.getStringExtra("course_class");
    }
}
