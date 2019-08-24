package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utils.API;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class ActivityCourseCommentNew extends AppCompatActivity {
    TextView text_view_rate;
    RatingBar rating_bar;
    EditText edit_text_content;

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


        initComponent();
        initButtons();
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
        ((ImageButton) findViewById(R.id.image_button_check)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edit_text_content.getText().toString();
                if(text.trim().isEmpty()){

                }
                else if (text.length()>4096){

                }
                //API api = new API(getApplicationContext());


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
