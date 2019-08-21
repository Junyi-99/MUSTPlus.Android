package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class ActivityCourseCommentNew extends AppCompatActivity {
    SeekBar seek_bar_t;
    SeekBar seek_bar_f;
    RatingBar rating_bar;
    ImageView image_view_star_1;
    ImageView image_view_star_2;
    ImageView image_view_star_3;
    ImageView image_view_star_4;
    ImageView image_view_star_5;


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
                SpringSystem springSystem = SpringSystem.create();
                Spring spring = springSystem.createSpring();
                spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(seek_bar_t.getProgress(), seek_bar_f.getProgress()));

                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        float scale = 1f - (value * 0.5f);
                        //rating_bar.setTranslationY(value * 40);
                        rating_bar.setScaleY(scale);
                        rating_bar.setScaleX(scale);
                    }
                });
                spring.setEndValue(1f);
            }
        });
    }

    private void initComponent() {
        final TextView textView = (TextView) findViewById(R.id.textView_indicator);
        seek_bar_t = (SeekBar) findViewById(R.id.seek_bar_t);
        seek_bar_t.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());

            }
        });
        seek_bar_f = (SeekBar) findViewById(R.id.seek_bar_f);
        seek_bar_f.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("T:" + seek_bar_t.getProgress() + " F:" + seek_bar_f.getProgress());

            }
        });
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("------------", "当前的评价等级：" + rating);

            }
        });


        Intent intent = getIntent();
        //course_id = intent.getIntExtra("course_id", 0);
        //course_code = intent.getStringExtra("course_code");
        //course_class = intent.getStringExtra("course_class");
    }
}
