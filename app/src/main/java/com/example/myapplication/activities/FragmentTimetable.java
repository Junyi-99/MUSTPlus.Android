package com.example.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;

import com.example.myapplication.R;
import com.example.myapplication.utils.Timetable;
import com.example.myapplication.utils.TimetableCell;

import java.util.ArrayList;
import java.util.List;


public class FragmentTimetable extends Fragment {
    private static final String TAG = "FragementTimeTable";
    private final List<Animator> animatorList = new ArrayList<Animator>();

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().density));
    }

    private void animate() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(this.animatorList);
        animatorSet.start();
    }

    private void calculateLayout(String timetableRaw, ViewGroup container, LayoutInflater inflater, RelativeLayout relativeLayout) {
        Timetable timetable = JSON.parseObject(timetableRaw, Timetable.class);
        if (timetable == null)
            return;

        int delay = 0;

        for (TimetableCell cell : timetable.getTimetable()) {
            final Button b = (Button) inflater.inflate(R.layout.timetable_course_cell, container, false);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animate();
                }
            });

            Animator animator = ObjectAnimator.ofFloat(b, "alpha", 0.f, 0.5f, 1.f);
            animator.setDuration(500).setStartDelay(delay);
            animatorList.add(animator);
            delay += 100;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            int offsetHeight = convertDpToPx(10);
            int offsetMarginLeft = -(int) (getResources().getDimension(R.dimen.timetable_time_cell_width) - getResources().getDimension(R.dimen.timetable_header_width)) / 2; // negative
            int offsetMarginTop = -convertDpToPx(5);
            params.width = (int) getResources().getDimension(R.dimen.timetable_time_cell_width); // 补齐由于margin而缺少的宽高
            params.height = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.duration()) + offsetHeight;
            int marginLeft = (int) getResources().getDimension(R.dimen.timetable_header_width) * cell.getCellDayPosition() + offsetMarginLeft;
            int marginTop = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.getCellLinePosition()) + offsetMarginTop;
            params.setMargins(marginLeft, marginTop, 0, 0);
            String title = cell.getCourse_name_zh() + "\n@" + cell.getClassroom();
            b.setLayoutParams(params);
            b.setText(title);
            b.setAlpha(0.f);



            relativeLayout.addView(b);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarTimetable);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final Button button = (Button) view.findViewById(R.id.course1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater vi = LayoutInflater.from(getContext());
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutInnerContent);
                relativeLayout.removeView(button);
                String timetableRaw = "{\"code\": 0, \"msg\": \"\", \"timetable\": [{\"day\": \"04\", \"time_begin\": \"13:05\", \"time_end\": \"14:50\", \"course_code\": \"GSLS111\", \"course_name_zh\": \"\\u5927\\u5b78\\u82f1\\u8a9e (\\u807d\\u8aaa I)\", \"course_class\": \"D13\", \"classroom\": \"B504\", \"teacher\": \"\\u5289\\u6587\\u82f1\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 1}, {\"day\": \"06\", \"time_begin\": \"13:05\", \"time_end\": \"14:50\", \"course_code\": \"GSLS111\", \"course_name_zh\": \"\\u5927\\u5b78\\u82f1\\u8a9e (\\u807d\\u8aaa I)\", \"course_class\": \"D13\", \"classroom\": \"C506\", \"teacher\": \"\\u5289\\u6587\\u82f1\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 1}, {\"day\": \"03\", \"time_begin\": \"13:05\", \"time_end\": \"14:50\", \"course_code\": \"GSER111\", \"course_name_zh\": \"\\u5927\\u5b78\\u82f1\\u8a9e (\\u7cbe\\u8b80 I)\", \"course_class\": \"D05\", \"classroom\": \"C507\", \"teacher\": \"\\u7a0b\\u6587\\u9e97\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 2}, {\"day\": \"02\", \"time_begin\": \"13:05\", \"time_end\": \"14:50\", \"course_code\": \"GSER111\", \"course_name_zh\": \"\\u5927\\u5b78\\u82f1\\u8a9e (\\u7cbe\\u8b80 I)\", \"course_class\": \"D05\", \"classroom\": \"C509\", \"teacher\": \"\\u7a0b\\u6587\\u9e97\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 2}, {\"day\": \"05\", \"time_begin\": \"13:05\", \"time_end\": \"14:50\", \"course_code\": \"GSER111\", \"course_name_zh\": \"\\u5927\\u5b78\\u82f1\\u8a9e (\\u7cbe\\u8b80 I)\", \"course_class\": \"D05\", \"classroom\": \"C509\", \"teacher\": \"\\u7a0b\\u6587\\u9e97\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 2}, {\"day\": \"02\", \"time_begin\": \"10:00\", \"time_end\": \"12:40\", \"course_code\": \"MA101\", \"course_name_zh\": \"\\u5fae\\u7a4d\\u5206 I\", \"course_class\": \"D1\", \"classroom\": \"C308\", \"teacher\": \"\\u767d\\u9e97\\u5e73\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 3}, {\"day\": \"05\", \"time_begin\": \"09:00\", \"time_end\": \"10:45\", \"course_code\": \"MA101\", \"course_name_zh\": \"\\u5fae\\u7a4d\\u5206 I\", \"course_class\": \"D1\", \"classroom\": \"C308\", \"teacher\": \"\\u767d\\u9e97\\u5e73\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 3}, {\"day\": \"01\", \"time_begin\": \"11:00\", \"time_end\": \"12:45\", \"course_code\": \"CN103\", \"course_name_zh\": \"\\u8a08\\u7b97\\u6a5f\\u7a0b\\u5e8f\\u8a2d\\u8a08 I\", \"course_class\": \"D1\", \"classroom\": \"C308\", \"teacher\": \"\\u7f85\\u5c11\\u9f8d\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 4}, {\"day\": \"02\", \"time_begin\": \"17:00\", \"time_end\": \"18:45\", \"course_code\": \"CN103\", \"course_name_zh\": \"\\u8a08\\u7b97\\u6a5f\\u7a0b\\u5e8f\\u8a2d\\u8a08 I\", \"course_class\": \"D1\", \"classroom\": \"C408\\uff08\\u5be6\\u9a57\\u5ba4\\uff09\", \"teacher\": \"\\u7f85\\u5c11\\u9f8d\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 4}, {\"day\": \"01\", \"time_begin\": \"15:00\", \"time_end\": \"16:45\", \"course_code\": \"GWC001\", \"course_name_zh\": \"\\u897f\\u65b9\\u6587\\u5316\\u901a\\u8ad6\", \"course_class\": \"D03\", \"classroom\": \"N214\", \"teacher\": \"\\u9867\\u885b\\u6c11,\\u8a31\\u5e73,\\u8d99\\u6797\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 5}, {\"day\": \"03\", \"time_begin\": \"16:30\", \"time_end\": \"19:00\", \"course_code\": \"GMS001\", \"course_name_zh\": \"\\u79d1\\u6280\\u5927\\u5e2b\\u8b1b\\u5ea7\", \"course_class\": \"D01\", \"classroom\": \"N101\", \"teacher\": \"\\u5f90\\u61ff,\\u5f90\\u66c9\\u8ecd\", \"date_begin\": \"9-4\", \"date_end\": \"12-16\", \"course_id\": 6}]}";
                //timetableRaw = "";
                calculateLayout(timetableRaw, container, vi, relativeLayout);
               /* Button bb = (Button) vi.inflate(R.layout.timetable_course_cell, container, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.width = (int) getResources().getDimension(R.dimen.timetable_header_width); // 补齐由于margin而缺少的宽高
                params.height = (int) (getResources().getDimension(R.dimen.timetable_time_cell_height) + convertDpToPx(10));
                int marginLeft = (int) getResources().getDimension(R.dimen.timetable_header_width) * 3;
                int marginTop = (int) (getResources().getDimension(R.dimen.timetable_time_cell_height) * 2) - convertDpToPx(5);
                //3,2 714 297
                params.setMargins(marginLeft, marginTop, 0, 0);
                bb.setLayoutParams(params);
                bb.setText("asklfj");
                relativeLayout.addView(bb);*/


            }
        });

        button.callOnClick();
        animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment Timetable", "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment Timetable", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment Timetable", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment Timetable", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment Timetable", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment Timetable", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment Timetable", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment Timetable", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment Timetable", "onDetach");
    }
}
