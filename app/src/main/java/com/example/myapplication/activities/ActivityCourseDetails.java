package com.example.myapplication.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterCourseCommentList;
import com.example.myapplication.adapters.AdapterFtpList;
import com.example.myapplication.adapters.AdapterTeacherList;
import com.example.myapplication.models.ModelCourse;
import com.example.myapplication.models.ModelCourseComment;
import com.example.myapplication.models.ModelFtp;
import com.example.myapplication.models.ModelTeacher;
import com.example.myapplication.utils.API;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityCourseDetails extends AppCompatActivity {
    private TextView text_view_course_title;
    private TextView text_view_course_schedule;
    private TextView text_view_course_code;
    private TextView text_view_course_class;
    private TextView text_view_course_faculty;
    private TextView text_view_course_credit;

    private RecyclerView recyclerViewTeacherList;
    private RecyclerView recyclerViewCourseComment;
    private RecyclerView recyclerViewFtpList;

    private AdapterTeacherList adapterTeacherList;
    private AdapterCourseCommentList adapterCourseCommentList;
    private AdapterFtpList adapterFtpList;

    private ArrayList<ModelTeacher> modelTeacherArrayList = new ArrayList<ModelTeacher>();
    ;
    private ArrayList<ModelCourseComment> modelCourseCommentArrayList = new ArrayList<ModelCourseComment>();
    private ArrayList<ModelFtp> modelFtpArrayList = new ArrayList<ModelFtp>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_temp);


        initComponent();
        initButtons();


    }

    private void initButtons() {
        // 返回按钮
        ImageButton button = (ImageButton) findViewById(R.id.image_button_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCourseDetails.super.onBackPressed();
            }
        });

        /*FloatingActionsMenu floatingActionButton = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelCourseCommentArrayList.add(new ModelCourseComment(
                        2,
                        "1709853D-I011-0021",
                        0,
                        0,
                        4.89,
                        "sllo老师讲课太有意思啦！",
                        "2019-06-07 18:14:47"
                ));
                adapterCourseCommentList.notifyDataSetChanged();
            }
        });*/
    }

    private void initComponent() {


        text_view_course_title = (TextView) findViewById(R.id.course_title);
        text_view_course_schedule = (TextView) findViewById(R.id.course_schedule);
        text_view_course_code = (TextView) findViewById(R.id.course_code);
        text_view_course_class = (TextView) findViewById(R.id.course_class);
        text_view_course_faculty = (TextView) findViewById(R.id.course_faculty);
        text_view_course_credit = (TextView) findViewById(R.id.course_credit);

        Intent intent = getIntent();
        final int course_id = intent.getIntExtra("course_id", 0);
        final String course_code = intent.getStringExtra("course_code");
        final String course_class = intent.getStringExtra("course_class");

        text_view_course_title.setText(intent.getStringExtra("course_title"));
        text_view_course_schedule.setText(intent.getStringExtra("course_schedule"));
        text_view_course_code.setText("编号：" + course_code);
        text_view_course_class.setText("班级：" + course_class);


        //ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        //OverScrollDecoratorHelper.setUpStaticOverScroll(scrollView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        //OverScrollDecoratorHelper.setUpOverScroll(scrollView);


        recyclerViewTeacherList = (RecyclerView) findViewById(R.id.recyclerViewTeacherList);
        recyclerViewTeacherList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeacherList.setHasFixedSize(true);
        recyclerViewTeacherList.setNestedScrollingEnabled(false);

        recyclerViewCourseComment = (RecyclerView) findViewById(R.id.recyclerViewCourseComment);
        recyclerViewCourseComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourseComment.setHasFixedSize(true);
        recyclerViewCourseComment.setNestedScrollingEnabled(false);

        //recyclerViewFtpList = (RecyclerView) findViewById(R.id.recyclerViewFtpList);
        //recyclerViewFtpList.setLayoutManager(new LinearLayoutManager(this));
        //recyclerViewFtpList.setHasFixedSize(true);
        //recyclerViewFtpList.setNestedScrollingEnabled(false);


        TypedArray drw_arr = this.getResources().obtainTypedArray(R.array.people_images);
        for (double i = 0.0; i <= 5.0; i += 0.5) {
            modelCourseCommentArrayList.add(new ModelCourseComment(
                    0,
                    "Junyi",
                    (int) i * 3,
                    (int) i * 2,
                    i,
                    String.format("支持罗老师 %f星好评！", i),
                    "2019/7/" + String.format("%d", (int) i)
            ));
        }

        //set data and list adapter
        adapterTeacherList = new AdapterTeacherList(this, modelTeacherArrayList);
        adapterCourseCommentList = new AdapterCourseCommentList(this, modelCourseCommentArrayList);
        adapterFtpList = new AdapterFtpList(this, modelFtpArrayList);

        recyclerViewTeacherList.setAdapter(adapterTeacherList);
        recyclerViewCourseComment.setAdapter(adapterCourseCommentList);
        //adapterFtpList.setAdapter(adapterFtpList);


        // on item list clicked
        adapterTeacherList.setOnItemClickListener(new AdapterTeacherList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelTeacher obj, int position) {

            }
        });
        adapterCourseCommentList.setOnItemClickListener(new AdapterCourseCommentList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelCourseComment obj, int position) {

            }
        });

        // 更新数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBHelper helper = new DBHelper(getApplicationContext());
                    API api = new API(getApplicationContext());
                    final ModelCourse course = api.course(helper.getLoginRecord().getToken(), course_id, course_code, course_class);
                    if (course == null) {
                        // 无法获取课程数据
                    } else {
                        if (course.getCode() == 0) {
                            //获取成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text_view_course_faculty.setText("学院：" + course.getFaculty());
                                    text_view_course_credit.setText("学分：" + course.getCredit());
                                    modelTeacherArrayList.addAll(course.getTeachers()); // 老师信息
                                    //synchronized (adapterTeacherList) {
                                    adapterTeacherList.notifyDataSetChanged();
                                    //}
                                    for (ModelTeacher teacher : course.getTeachers()) {
                                        Log.d("TEACHERS", teacher.getName_zh());
                                    }
                                }
                            });
                            //TODO: 可以给老师信息列表加个动画
                        } else {
                            //获取失败，请看错误代码
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
