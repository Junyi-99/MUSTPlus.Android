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

/**
 * @author Junyi
 */
public class ActivityCourseCommentNew extends AppCompatActivity {
    TextView textViewRate;
    RatingBar ratingBar;
    EditText editTextContent;
    ImageButton imageButtonCheck;
    private int courseId;
    /**
     * 对应的描述文本
     */
    private String[] ratingDescription = {
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
        courseId = intent.getIntExtra("course_id", -1);

        initComponent();
        initButtons();
    }

    /**
     * 仅限在UI线程外使用（因为方法内会切换回UI线程）
     * @param enable 设置控件是否启用
     */
    private void setEnableInThread(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageButtonCheck.setEnabled(enable);
                editTextContent.setEnabled(enable);
                ratingBar.setEnabled(enable);
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
                        comment = api.course_comment_post(login.getToken(), courseId, (double) ratingBar.getRating(), text);

                        if (comment == null) {
                            Snackbar.make(textViewRate, "错误：服务器返回空", Snackbar.LENGTH_SHORT).show();
                            setEnableInThread(true);
                            return;
                        }
                        if (comment.getCode() != 0) {
                            Snackbar.make(textViewRate, "错误：" + comment.getMsg(), Snackbar.LENGTH_SHORT).show();
                            setEnableInThread(true);
                            return;
                        }

                        setResult(666);
                        finish();

                    } catch (IOException e) {
                        Snackbar.make(textViewRate, "错误：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
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
        imageButtonCheck = (ImageButton) findViewById(R.id.image_button_check);
        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextContent.getText().toString();
                if (text.trim().isEmpty()) {
                    Snackbar.make(textViewRate, "评论内容不可以为空（0~512字符）", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (text.length() > 512) {
                    Snackbar.make(textViewRate, "评论内容太长！（0~512字符）", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                imageButtonCheck.setEnabled(false);
                editTextContent.setEnabled(false);
                ratingBar.setEnabled(false);
                publishComment(text);
            }
        });
    }

    private void initComponent() {
        editTextContent = (EditText) findViewById(R.id.edit_text_content);
        textViewRate = (TextView) findViewById(R.id.text_view_rate);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                textViewRate.setText(ratingDescription[(int) (rating * 2)]);


                SpringSystem springSystem = SpringSystem.create();
                Spring spring = springSystem.createSpring();
                spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50, 3));
                spring.setCurrentValue(1f);
                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 1f - (value * 0.5f);
                        textViewRate.setScaleY(scale);
                        textViewRate.setScaleX(scale);
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
