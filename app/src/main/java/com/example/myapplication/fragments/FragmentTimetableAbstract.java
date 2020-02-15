package com.example.myapplication.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.activities.ActivityCourseDetails;
import com.example.myapplication.activities.ActivitySettings;
import com.example.myapplication.activities.ActivityWelcome;
import com.example.myapplication.models.ModelResponseSemester;
import com.example.myapplication.models.ModelResponseWeek;
import com.example.myapplication.models.ModelTimetable;
import com.example.myapplication.models.ModelTimetableCell;
import com.example.myapplication.utils.API.APIPersistence;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Junyi
 */
public class FragmentTimetableAbstract extends AbstractLazyLoadFragment {
    private final ArrayList<Animator> animators = new ArrayList<Animator>();
    int[] colourfulCell = {
            R.drawable.layout_bg_timetable_cell_blue,
            R.drawable.layout_bg_timetable_cell_green,
            R.drawable.layout_bg_timetable_cell_orange,
            R.drawable.layout_bg_timetable_cell_pink,
            R.drawable.layout_bg_timetable_cell_purple,
            R.drawable.layout_bg_timetable_cell_red,
            R.drawable.layout_bg_timetable_cell_teal,
            R.drawable.layout_bg_timetable_cell_yellow
    };
    private ModelResponseWeek week;
    private ModelResponseSemester semester;
    private String timetableRawJson = "";
    private View root;
    private View timeIndicator;
    private ViewGroup thisContainer;
    private ArrayList<TextView> timetableCellList = new ArrayList<TextView>();
    private ImageButton imageButtonMore;
    private ImageButton imageButtonSelectWeek;
    private ScrollView scrollViewVertical;
    private RelativeLayout relativeLayoutInnerContent;
    /**
     * 是否已经播放过动画
     */
    private boolean animated = false;
    private int scrollViewScrollingY = 0; // 上一次scrolling到哪里（这个变量为了中断 Spring 动画）
    private boolean scrollViewScrollingBreak = false;

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().density));
    }

    private void animate() {
        if (!animated && !timetableRawJson.isEmpty()) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(this.animators);
            animatorSet.start();

            // 定位到当前时间上的课程动画
            Spring spring = SpringSystem.create().createSpring();
            spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 5));
            spring.setCurrentValue((float) scrollViewVertical.getScrollY());
            spring.addListener(new SimpleSpringListener() {
                @Override
                public void onSpringActivate(Spring spring) {
                    super.onSpringActivate(spring);
                    scrollViewScrollingBreak = false;
                    scrollViewScrollingY = scrollViewVertical.getScrollY();
                }

                @Override
                public void onSpringUpdate(Spring spring) {
                    // 允许用户中断动画的写法
                    if (!scrollViewScrollingBreak) {
                        if (scrollViewVertical.getScrollY() != scrollViewScrollingY) {
                            scrollViewScrollingBreak = true;
                        } else {
                            int currentScrolling = (int) spring.getCurrentValue();
                            scrollViewVertical.scrollTo(0, currentScrolling);
                            scrollViewScrollingY = currentScrolling;
                        }
                    }
                }
            });
            int rowHeaderHeight = (int) getResources().getDimension(R.dimen.timetable_time_row_header_height);
            int columnHeaderHeight = (int) getResources().getDimension(R.dimen.timetable_column_header_height);

            Calendar calendar = Calendar.getInstance(); // 28800000 表示 8 小时，因为时间从 8 点开始
            int hour = calendar.get(Calendar.HOUR_OF_DAY);// 这里返回的是手机上的时间（北京时间）
            int minute = calendar.get(Calendar.MINUTE);
            float endValue = 0f;
            if (hour <= 22 && hour >= 8) {
                endValue = (hour - 8 - 6) * rowHeaderHeight + columnHeaderHeight + (int) (minute / 60.0 * rowHeaderHeight);
            }
            spring.setEndValue(endValue);
            // 定位到当前时间上的课程动画

            animated = true;
        } else {
            for (TextView button : timetableCellList) {
                button.setAlpha(1.f);
            }
        }
    }

    private void calculateLayout() throws ParseException {
        Log.e("TimetableRawJson",timetableRawJson);
        timetableCellList.clear();
        animators.clear();
        //relativeLayoutInnerContent.removeAllViews();


        ModelTimetable modelTimetable = JSON.parseObject(timetableRawJson, ModelTimetable.class);
        if (modelTimetable == null) {
            return;
        }


        // 这里用 Map 来保证每一个 course_code 对应一个颜色
        Map<String, Integer> courseColor = new HashMap<String, Integer>();
        int colourfulCellIndex = 0;
        for (final ModelTimetableCell cell : modelTimetable.getTimetable()) {
            if (!courseColor.containsKey(cell.getCourse_code())) {
                courseColor.put(cell.getCourse_code(), colourfulCell[colourfulCellIndex % colourfulCell.length]);
                colourfulCellIndex++;
            }
        }


        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(new SimpleDateFormat("MM-dd").parse(month + "-" + day));
        Long dateNowMillis = calendar.getTimeInMillis();


        int animationDelay = 0;
        for (final ModelTimetableCell cell : modelTimetable.getTimetable()) {
            calendar.setTime(new SimpleDateFormat("MM-dd").parse(cell.getDate_begin()));
            Long dateBeginMillis = calendar.getTimeInMillis();
            calendar.setTime(new SimpleDateFormat("MM-dd").parse(cell.getDate_end()));
            Long dateEndMillis = calendar.getTimeInMillis();

            if (dateEndMillis > dateBeginMillis) {
                if (dateNowMillis < dateBeginMillis || dateNowMillis > dateEndMillis) {
                    // 如果在区间外
                    //continue;
                }
            } else { // 跨年的情况
                if (dateNowMillis > dateBeginMillis || dateNowMillis < dateEndMillis) {
                    // 如果在区间外
                    continue;
                }
            }

            final TextView b = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.timetable_course_cell_new, thisContainer, false);

            // 取出 Map 中的元素，每一个 course_code 对应一种颜色
            // 防止空指针异常
            Integer resId = courseColor.get(cell.getCourse_code());
            if (resId != null) {
                b.setBackgroundResource(resId);
            }

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
            animator.setDuration(500).setStartDelay(animationDelay);
            animators.add(animator);
            animationDelay += 100;

            // These supply parameters to the parent of this view specifying how it should be arranged.
            // 也就是说，在 Android 5.0 (APIPersistence 21) 时，所有 Button 重叠在一起是因为
            // 之前使用的是 LinearLayout.LayoutParams，所以出现了问题
            // 在 Android 9.0 版本使用 LinearLayout.LayoutParams 就没问题
            // 个人推测是因为高版本的 Android 增加了兼容性
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            int marginLeft = (int) getResources().getDimension(R.dimen.timetable_header_width) * cell.getCellDayPosition() + convertDpToPx(2);
            int marginTop = (int) (getResources().getDimension(R.dimen.timetable_time_row_header_height) * cell.getCellLinePosition());
            params.setMargins(marginLeft, marginTop, 0, 0);

            params.width = (int) getResources().getDimension(R.dimen.timetable_time_cell_width);
            params.height = (int) (getResources().getDimension(R.dimen.timetable_time_row_header_height) * cell.duration());


            //String title = cell.getCourse_code() + "\n@" + cell.getClassroom() + "\n" + cell.getTeacher();

            // 1小时45分钟
            // 计算机组成原理\n@C402\n于建德
            // TRADITIONAL\n@C402\n于建德
            String title = cell.getCourse_name_zh() + "\n@" + cell.getClassroom() + "\n" + cell.getTeacher();


            b.setLayoutParams(params);
            b.setText(title);
            b.setAlpha(0.f);

            timetableCellList.add(b);
        }

        for (TextView b : timetableCellList) {
            relativeLayoutInnerContent.addView(b);
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
        // 更新表头（顶部）
        String[] days = getCurrentWeekDays();
        TextView textViewMonday = view.findViewById(R.id.text_view_monday);
        TextView textViewTuesday = view.findViewById(R.id.text_view_tuesday);
        TextView textViewWednesday = view.findViewById(R.id.text_view_wednesday);
        TextView textViewThursday = view.findViewById(R.id.text_view_thursday);
        TextView textViewFriday = view.findViewById(R.id.text_view_friday);
        TextView textViewSaturday = view.findViewById(R.id.text_view_saturday);
        TextView textViewSunday = view.findViewById(R.id.text_view_sunday);
        textViewMonday.setText(String.format("%s\n%s", getString(R.string.monday), days[0]));
        textViewTuesday.setText(String.format("%s\n%s", getString(R.string.tuesday), days[1]));
        textViewWednesday.setText(String.format("%s\n%s", getString(R.string.wednesday), days[2]));
        textViewThursday.setText(String.format("%s\n%s", getString(R.string.thursday), days[3]));
        textViewFriday.setText(String.format("%s\n%s", getString(R.string.friday), days[4]));
        textViewSaturday.setText(String.format("%s\n%s", getString(R.string.saturday), days[5]));
        textViewSunday.setText(String.format("%s\n%s", getString(R.string.sunday), days[6]));

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            default: // default is sunday
                textViewSunday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.MONDAY:
                textViewMonday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.TUESDAY:
                textViewTuesday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.WEDNESDAY:
                textViewWednesday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.THURSDAY:
                textViewThursday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.FRIDAY:
                textViewFriday.setBackgroundResource(R.color.timetable_column_active);
                break;
            case Calendar.SATURDAY:
                textViewSaturday.setBackgroundResource(R.color.timetable_column_active);
                break;
        }

        // 更新表头（左侧）
        int width = (int) getResources().getDimension(R.dimen.timetable_header_width) * 7;
        int height = convertDpToPx(2);
        int rowHeaderHeight = (int) getResources().getDimension(R.dimen.timetable_time_row_header_height); // 左侧单个表头高度
        for (int i = 1; i <= 14; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.width = width;
            params.height = height;

            int marginTop = rowHeaderHeight * i;
            params.setMargins(0, marginTop, 0, 0);

            final TextView divider = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.layout_timetable_time_divider, thisContainer, false);
            divider.setLayoutParams(params);
            relativeLayoutInnerContent.addView(divider);
        }

        // 更新学期和周数
        final TextView toolbarTitle = view.findViewById(R.id.toolbar_title);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    APIPersistence api = new APIPersistence(getContext());
                    semester = api.semester_get();
                    week = api.week_get();
                    FragmentActivity activity = getActivity();
                    if (activity != null && semester != null && week != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toolbarTitle.setText(String.format(getString(R.string.intake_week), semester.getSemester(), week.getWeek()));
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
    public View onCreateView(
            @NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("FragmentTimetable", "onCreateView");

        thisContainer = container;

        root = inflater.inflate(R.layout.fragment_timetable, container, false);

        relativeLayoutInnerContent = root.findViewById(R.id.relativeLayoutInnerContent);
        imageButtonMore = root.findViewById(R.id.imageButtonBack);
        imageButtonSelectWeek = root.findViewById(R.id.image_button_select_week);
        timeIndicator = root.findViewById(R.id.timeIndicator);
        scrollViewVertical = root.findViewById(R.id.scrollViewVertical);
        imageButtonSelectWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityWelcome.class);
                startActivity(intent);
            }
        });
        imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.fragment_timetable, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            default:
                                break;
                            case R.id.action_export:
                                break;
                            case R.id.action_logout:
                                DBHelper db = new DBHelper(getContext());
                                db.setLogout();
                                break;
                            case R.id.action_refresh:
                                break;
                            case R.id.action_settings:
                                Intent intent = new Intent(getActivity(), ActivitySettings.class);
                                startActivity(intent);
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

        new TaskUpdateTime().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //TODO: 这里逻辑混乱了啊，耦合度增加了，应该调用API类的方法，不应该绕过API类直接调用这个DBHelper
        DBHelper db = new DBHelper(getContext());
        timetableRawJson = db.getAPIRecord(APICONSTANT.TIMETABLE);
        try {
            calculateLayout();
        } catch (ParseException e) {
            Toast.makeText(getContext(), "错误：" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            try {
                calculateLayout();
            } catch (ParseException e) {
                Toast.makeText(getContext(), "错误：" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            animate(); // Buttons 默认都是 invisible 的，所以调用 animate() 让他们显示出来
        }
    }

    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();

    }

    private class TaskUpdateTime extends AsyncTask<Void, RelativeLayout.LayoutParams, String> {
        private double MS_TO_HOURS = (0.00000027777777777777777777777);
        private int width;
        private int height;
        private int rowHeaderHeight;
        private int columnHeaderHeight;
        private RelativeLayout.LayoutParams params;

        @Override
        protected void onPreExecute() {
            //width = (int) getResources().getDimension(R.dimen.timetable_time_cell_width);
            //height = convertDpToPx(2);
            rowHeaderHeight = (int) getResources().getDimension(R.dimen.timetable_time_row_header_height);
            columnHeaderHeight = (int) getResources().getDimension(R.dimen.timetable_column_header_height);
            params = (RelativeLayout.LayoutParams) timeIndicator.getLayoutParams();
            //params.width = width;
            //params.height = height;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(RelativeLayout.LayoutParams... values) {
            super.onProgressUpdate(values);
            // 28800000 表示 8 小时，因为时间从 8 点开始
            Calendar calendar = Calendar.getInstance();

            // 这里返回的是手机上的时间（北京时间）
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            if (hour <= 22 && hour >= 8) {
                int marginTop = (hour - 8) * rowHeaderHeight + columnHeaderHeight + (int) (minute / 60.0 * rowHeaderHeight);
                params.setMargins(0, marginTop, 0, 0);
            } else {
                int marginTop = columnHeaderHeight;
                params.setMargins(0, marginTop, 0, 0);
            }
            timeIndicator.requestLayout();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected String doInBackground(Void... voids) {
            while (true) {
                publishProgress();
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "";
                }
            }

        }
    }
}
