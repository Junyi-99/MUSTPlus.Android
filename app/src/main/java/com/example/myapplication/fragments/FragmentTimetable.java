package com.example.myapplication.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.activities.ActivityCourseDetails;
import com.example.myapplication.activities.ActivityLogin;
import com.example.myapplication.models.ModelTimetable;
import com.example.myapplication.models.ModelTimetableCell;
import com.example.myapplication.utils.APIs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentTimetable extends Fragment {
    private static final String TAG = "FragementTimeTable";
    private final ArrayList<Animator> animators = new ArrayList<Animator>();
    private String timetable_raw = "";
    private View this_view;
    private ViewGroup this_container;
    private ArrayList<TextView> timetable_cell_list = new ArrayList<TextView>();
    private ImageButton image_button_back;
    private boolean animated = false; // 是否已经播放过动画

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().density));
    }

    private void animate() {
        if (!animated && !timetable_raw.isEmpty()) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(this.animators);
            animatorSet.start();
            animated = true;
        } else {
            for (TextView button : timetable_cell_list) {
                button.setAlpha(1.f);
            }
        }
    }

    private void calculateLayout(String timetableRaw, ViewGroup container, LayoutInflater inflater, RelativeLayout relativeLayout) {
        timetable_cell_list.clear();

        ModelTimetable modelTimetable = JSON.parseObject(timetableRaw, ModelTimetable.class);
        if (modelTimetable == null)
            return;

        int delay = 0;

        for (final ModelTimetableCell cell : modelTimetable.getTimetable()) {
            //final Button b = (Button) inflater.inflate(R.layout.timetable_course_cell, container, false);
            final TextView b = (TextView) inflater.inflate(R.layout.timetable_course_cell_new, container, false);
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
            animators.add(animator);
            delay += 100;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.width = (int) getResources().getDimension(R.dimen.timetable_time_cell_width);
            params.height = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.duration());
            int marginLeft = (int) getResources().getDimension(R.dimen.timetable_header_width) * cell.getCellDayPosition() + convertDpToPx(2);
            int marginTop = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.getCellLinePosition());

            params.setMargins(marginLeft, marginTop, 0, 0);
            String title = cell.getCourse_name_zh() + "\n@" + cell.getClassroom();
            b.setLayoutParams(params);
            b.setText(title);
            b.setAlpha(0.f);

            timetable_cell_list.add(b);
            Log.d("单元格", title);
        }

        for (TextView b : timetable_cell_list) {
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
                text_view_sunday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.MONDAY:
                text_view_monday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.TUESDAY:
                text_view_tuesday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.WEDNESDAY:
                text_view_wednesday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.THURSDAY:
                text_view_thursday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.FRIDAY:
                text_view_friday.setBackgroundResource(R.color.grey_20);
                break;
            case Calendar.SATURDAY:
                text_view_saturday.setBackgroundResource(R.color.grey_20);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        this_view = inflater.inflate(R.layout.fragment_timetable, container, false);
        this_container = container;
        ((ImageButton) this_view.findViewById(R.id.image_button_select_week)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                startActivity(intent);
            }
        });
        image_button_back = (ImageButton) this_view.findViewById(R.id.image_button_back);
        image_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(getContext());
                db.setLogout();
            }
        });

        updateTableHeaders(this_view);

        //Toolbar toolbar = (Toolbar) this_view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return this_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LayoutInflater vi = LayoutInflater.from(getContext());
        RelativeLayout relativeLayout = (RelativeLayout) this_view.findViewById(R.id.relativeLayoutInnerContent);
        DBHelper db = new DBHelper(getContext());
        timetable_raw = db.getAPIRecord(APIs.TIMETABLE);
        calculateLayout(timetable_raw, this_container, vi, relativeLayout);
        animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来
        Log.d("Fragment ModelTimetable", "onStart");
    }
}
