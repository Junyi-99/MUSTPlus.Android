package com.example.myapplication.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.activities.ActivityCourseDetails;
import com.example.myapplication.models.ModelTimetable;
import com.example.myapplication.models.ModelTimetableCell;
import com.example.myapplication.utils.APIs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentTimetable extends Fragment {
    private static final String TAG = "FragementTimeTable";
    private final List<Animator> animatorList = new ArrayList<Animator>();
    private ArrayList<Button> buttonArrayList = new ArrayList<Button>();
    private ImageButton image_button_back;

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().density));
    }

    private void animate() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(this.animatorList);
        animatorSet.start();
    }

    private void calculateLayout(String timetableRaw, ViewGroup container, LayoutInflater inflater, RelativeLayout relativeLayout) {
        buttonArrayList.clear();

        ModelTimetable modelTimetable = JSON.parseObject(timetableRaw, ModelTimetable.class);
        if (modelTimetable == null)
            return;

        int delay = 0;

        for (final ModelTimetableCell cell : modelTimetable.getTimetable()) {
            final Button b = (Button) inflater.inflate(R.layout.timetable_course_cell, container, false);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActivityCourseDetails.class);
                    intent.putExtra("course_title", cell.getCourse_name_zh());
                    intent.putExtra("course_schedule", cell.getSchedule());
                    intent.putExtra("course_code", cell.getCourse_code());
                    intent.putExtra("course_id", cell.getCourse_id());
                    intent.putExtra("course_class", cell.getCourse_class());
                    intent.putExtra("course_faculty", "学院");
                    intent.putExtra("course_credit", "0.0");
                    intent.putExtra("teacher", cell.getTeacher());

                    startActivity(intent);
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

            buttonArrayList.add(b);

        }

        for (Button b : buttonArrayList) {
            relativeLayout.addView(b);
        }
    }

    private String[] getCurrentWeekDays() {
        DateFormat format = new SimpleDateFormat("MM.dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }


    private void updateTableHeaders(final View view) {
        String[] days = getCurrentWeekDays();
        TextView text_view_monday = (TextView) view.findViewById(R.id.text_view_monday);
        TextView text_view_tuesday = (TextView) view.findViewById(R.id.text_view_tuesday);
        TextView text_view_wednesday = (TextView) view.findViewById(R.id.text_view_wednesday);
        TextView text_view_thursday = (TextView) view.findViewById(R.id.text_view_thursday);
        TextView text_view_friday = (TextView) view.findViewById(R.id.text_view_friday);
        TextView text_view_saturday = (TextView) view.findViewById(R.id.text_view_saturday);
        TextView text_view_sunday = (TextView) view.findViewById(R.id.text_view_sunday);
        text_view_monday.setText("周一\n" + days[0]);
        text_view_tuesday.setText("周二\n" + days[1]);
        text_view_wednesday.setText("周三\n" + days[2]);
        text_view_thursday.setText("周四\n" + days[3]);
        text_view_friday.setText("周五\n" + days[4]);
        text_view_saturday.setText("周六\n" + days[5]);
        text_view_sunday.setText("周日\n" + days[6]);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                text_view_sunday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.MONDAY:
                text_view_monday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.TUESDAY:
                text_view_tuesday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.WEDNESDAY:
                text_view_wednesday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.THURSDAY:
                text_view_thursday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.FRIDAY:
                text_view_friday.setBackgroundResource(R.color.grey_10);
                break;
            case Calendar.SATURDAY:
                text_view_saturday.setBackgroundResource(R.color.grey_10);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        image_button_back = (ImageButton) view.findViewById(R.id.image_button_back);
        image_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(getContext());
                db.setLogout();

            }
        });

        updateTableHeaders(view);

        if (savedInstanceState != null) {
            // Restore last state

        } else {

        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarTimetable);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        LayoutInflater vi = LayoutInflater.from(getContext());
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutInnerContent);
        DBHelper db = new DBHelper(getContext());
        String timetableRaw = db.getAPIRecord(APIs.TIMETABLE);

        calculateLayout(timetableRaw, container, vi, relativeLayout);

        animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment ModelTimetable", "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment ModelTimetable", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment ModelTimetable", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment ModelTimetable", "onResume");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Fragment ModelTimetable", "onSaveInstanceState 存一些状态");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment ModelTimetable", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment ModelTimetable", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment ModelTimetable", "onDestroyView 保存状态");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment ModelTimetable", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment ModelTimetable", "onDetach");
    }
}
