package com.example.myapplication.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.activities.ActivityCourseDetails;
import com.example.myapplication.activities.ActivityLogin;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;
import com.example.myapplication.models.ModelTimetable;
import com.example.myapplication.models.ModelTimetableCell;
import com.example.myapplication.utils.API;
import com.example.myapplication.utils.APIs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentTimetable extends LazyLoadFragment {
    private final ArrayList<Animator> animators = new ArrayList<Animator>();
    private ModelResponseWeek week;
    private ModelResponseSemester semester;
    private String timetable_raw = "";
    private View root;
    private ViewGroup this_container;
    private ArrayList<TextView> timetable_cell_list = new ArrayList<TextView>();
    private ImageButton image_button_more;
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

            // These supply parameters to the parent of this view specifying how it should be arranged.
            // 也就是说，在 Android 5.0 (API 21) 时，所有 Button 重叠在一起是因为
            // 之前使用的是 LinearLayout.LayoutParams，所以出现了问题
            // 在 Android 9.0 版本使用 LinearLayout.LayoutParams 就没问题
            // 个人推测是因为高版本的 Android 增加了兼容性
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            int marginLeft = (int) getResources().getDimension(R.dimen.timetable_header_width) * cell.getCellDayPosition() + convertDpToPx(2);
            int marginTop = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.getCellLinePosition());
            params.setMargins(marginLeft, marginTop, 0, 0);

            params.width = (int) getResources().getDimension(R.dimen.timetable_time_cell_width);
            params.height = (int) (getResources().getDimension(R.dimen.timetable_time_label_height) * cell.duration());

            Log.e("CalculateMargins", "" + marginLeft + ", " + marginTop);
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
                text_view_sunday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.MONDAY:
                text_view_monday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.TUESDAY:
                text_view_tuesday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.WEDNESDAY:
                text_view_wednesday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.THURSDAY:
                text_view_thursday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.FRIDAY:
                text_view_friday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.SATURDAY:
                text_view_saturday.setBackgroundResource(R.color.timetable_column_active);
                break;
        }

        final TextView toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API api = new API(getContext());
                    semester = api.semester_get();
                    week = api.week_get();
                    FragmentActivity activity = getActivity();
                    if (activity != null && semester != null && week != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toolbar_title.setText(
                                        "学期 " + semester.getSemester() +
                                                " 第 " + week.getWeek() +
                                                " 周");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("FragmentTimetable", "onHiddenChanged");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("FragmentTimetable", "onAttach");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FragmentTimetable", "onCreate");

    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

        Log.d("FragmentTimetable", "onCreateView");
        root = inflater.inflate(R.layout.fragment_timetable, container, false);
        this_container = container;
        ((ImageButton) root.findViewById(R.id.image_button_select_week)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                startActivity(intent);
            }
        });
        image_button_more = (ImageButton) root.findViewById(R.id.image_button_more);
        image_button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.fragment_timetable, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_export:
                                break;
                            case R.id.action_logout:
                                DBHelper db = new DBHelper(getContext());
                                db.setLogout();
                                break;
                            case R.id.action_refresh:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
        updateTableHeaders(root);
        onFirstVisible(); // 默认首先加载课表，如果没这句话就懒加载不了了
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("FragmentTimetable", "onViewCreated");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FragmentTimetable", "onResume");
        Log.e("FragmentTimetable", "onFirstVisible");
        LayoutInflater vi = LayoutInflater.from(getContext());
        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.relativeLayoutInnerContent);
        DBHelper db = new DBHelper(getContext());
        timetable_raw = db.getAPIRecord(APIs.TIMETABLE);


        calculateLayout(timetable_raw, this_container, vi, relativeLayout);
        animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FragmentTimetable", "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FragmentTimetable", "onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("FragmentTimetable", "onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FragmentTimetable", "onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FragmentTimetable", "onDetach");

    }


    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();

    }
}
