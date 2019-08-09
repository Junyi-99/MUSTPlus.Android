package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.myapplication.models.ModelResponseCourseComment;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelTeacher;
import com.example.myapplication.utils.API;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityCourseDetails extends AppCompatActivity {
    private boolean isRefreshing = false;
    private int course_id;
    private String course_code;
    private String course_class;
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
    private SwipeRefreshLayout swipe_refresh_layout;

    private ArrayList<ModelTeacher> modelTeacherArrayList = new ArrayList<ModelTeacher>();
    private ArrayList<ModelCourseComment> modelCourseCommentArrayList = new ArrayList<ModelCourseComment>();
    private ArrayList<ModelFtp> modelFtpArrayList = new ArrayList<ModelFtp>();
    private ModelCourse modelCourse;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_temp);


        initButtons();
        initComponent();
    }

    private void initButtons() {
        // 返回按钮
        ((ImageButton) findViewById(R.id.image_button_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCourseDetails.super.onBackPressed();
            }
        });
    }

    // 评论系统无本地缓存
    private void refreshCourseComment() {
        try {
            DBHelper helper = new DBHelper(getApplicationContext());
            API api = new API(getApplicationContext());
            final ModelResponseCourseComment responseCourseComment;
            final ModelResponseLogin login = helper.getLoginRecord();
            if (login != null) {
                responseCourseComment = api.course_comment_get(login.getToken(), this.course_id);
                if (responseCourseComment != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            modelCourseCommentArrayList.clear();
                            if (responseCourseComment.getComments() != null)
                                modelCourseCommentArrayList.addAll(responseCourseComment.getComments());
                            adapterCourseCommentList.notifyDataSetChanged();
                        }
                    });
                }
            } else {
                //TODO： 通知用户token失效
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void refreshCourse(final boolean force_update) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("API refreshCourse", "entry");
                try {
                    DBHelper helper = new DBHelper(getApplicationContext());
                    API api = new API(getApplicationContext());
                    if (force_update)
                        api.setForceUpdate(true);
                    modelCourse = api.course(helper.getLoginRecord().getToken(), course_id, course_code, course_class);

                    if (modelCourse != null) {
                        if (modelCourse.getCode() == 0) {// 获取成功
                            runOnUiThread(new Runnable() { // 更新界面元素
                                @Override
                                public void run() {
                                    text_view_course_faculty.setText("评分：" + modelCourse.getRank());
                                    text_view_course_credit.setText("学分：" + modelCourse.getCredit());
                                    modelTeacherArrayList.clear();
                                    modelTeacherArrayList.addAll(modelCourse.getTeachers()); // 老师信息
                                    Log.d(modelCourse.getName_zh(), modelCourse.getRank());
                                    adapterTeacherList.notifyDataSetChanged();
                                    //TODO:make sure that you're not calling
                                    // notifyDataSetChanged(), setAdapter(Adapter), or swapAdapter(Adapter, boolean)
                                    // for small updates. Those methods signal that the entire list content has changed,
                                    // and will show up in Systrace as RV FullInvalidate. Instead, use SortedList or DiffUtil
                                    // to generate minimal updates when content changes or is added.
                                    for (ModelTeacher teacher : modelCourse.getTeachers()) {
                                        Log.d("TEACHERS", teacher.getName_zh());
                                    }
                                    if (!force_update) {
                                        //这里可能会有疑问为什么要判断 force_update
                                        // 因为 force_update 在目前看来，只有创建这个 Activity 的时候会设置成 false
                                        // 来加载本地数据，force_update 在 true 的时候就是下拉刷新的状态了，
                                        // 下拉刷新后 SwipeRefreshLayout 的 Refreshing 应该由 refreshCourseComment
                                        // 结束后停止。
                                        // 我们要做的效果是，初次加载这个科目的时候，他不是得请求后端API吗，这个过程没有动画
                                        // 的话，用户可能以为程序死掉了，我们给这个耗时的过程
                                        // 加一个 Refreshing 的效果，
                                        // 所以你看 refreshCourse(false); 前面有一句
                                        // swipe_refresh_layout.setRefreshing(true);
                                        // 我们刷新完了（force_update=false的情况）在这里停下就好了
                                        swipe_refresh_layout.setRefreshing(false);
                                        isRefreshing = false;
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

    public void swipeRefreshLayoutOnRefresh() {
        //检查是否处于刷新状态
        Log.d("API onRefresh", String.valueOf(swipe_refresh_layout.isRefreshing()) + " " + String.valueOf(isRefreshing));
        if (!isRefreshing) {
            isRefreshing = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshCourse(true);
                    refreshCourseComment();

                    swipe_refresh_layout.setRefreshing(false);
                    isRefreshing = false;
                }
            }).start();
        }
    }

    private void initComponent() {

        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutOnRefresh();
            }
        });


        text_view_course_title = (TextView) findViewById(R.id.course_title);
        text_view_course_schedule = (TextView) findViewById(R.id.course_schedule);
        text_view_course_code = (TextView) findViewById(R.id.course_code);
        text_view_course_class = (TextView) findViewById(R.id.course_class);
        text_view_course_faculty = (TextView) findViewById(R.id.course_faculty);
        text_view_course_credit = (TextView) findViewById(R.id.course_credit);

        Intent intent = getIntent();
        course_id = intent.getIntExtra("course_id", 0);
        course_code = intent.getStringExtra("course_code");
        course_class = intent.getStringExtra("course_class");

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


        ModelCourseComment comment = new ModelCourseComment(
                0,
                "Junyi",
                96,
                0,
                4.5,
                "下拉即可刷新课程信息和课程评价哦~下拉即可刷新课程信息和课程评价哦~",
                "2019/8/8"
        );
        for (int i = 0; i < 25; i++) {
            modelCourseCommentArrayList.add(comment);
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

        swipe_refresh_layout.setRefreshing(true);
        refreshCourse(false);
    }
}
