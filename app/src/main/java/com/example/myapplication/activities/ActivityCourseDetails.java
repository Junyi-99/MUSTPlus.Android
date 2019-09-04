package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Junyi
 */
public class ActivityCourseDetails extends AppCompatActivity {
    private boolean isRefreshing = false;
    private int courseId;
    private String courseCode;
    private String courseClass;
    private TextView textViewCourseTitle;
    private TextView textViewCourseSchedule;
    private TextView textViewCourseCode;
    private TextView textViewCourseClass;
    private TextView textViewCourseFaculty;
    private TextView textViewCourseCredit;
    private RecyclerView recyclerViewTeacherList;
    private RecyclerView recyclerViewCourseComment;
    private RecyclerView recyclerViewFtpList;
    private AdapterTeacherList adapterTeacherList;
    private AdapterCourseCommentList adapterCourseCommentList;
    private AdapterFtpList adapterFtpList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<ModelTeacher> modelTeacherArrayList = new ArrayList<ModelTeacher>();
    private ArrayList<ModelCourseComment> modelCourseCommentArrayList = new ArrayList<ModelCourseComment>();
    private ArrayList<ModelFtp> modelFtpArrayList = new ArrayList<ModelFtp>();
    private ModelCourse modelCourse;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("REQ_RES_CODE", requestCode + ", " + resultCode);
        if (requestCode == 0 && resultCode == 666) {
            Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);


        initButtons();
        initComponent();
    }

    private void initButtons() {
        // 返回按钮
        ((ImageButton) findViewById(R.id.image_button_more)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCourseDetails.super.onBackPressed();
            }
        });
        ((FloatingActionButton) findViewById(R.id.floating_action_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCourseDetails.this, ActivityCourseCommentNew.class);
                intent.putExtra("course_id", courseId);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 评论系统无本地缓存
     * @param forceUpdate 是否强制刷新
     */
    private void refreshCourseComment(boolean forceUpdate) {
        try {
            DBHelper helper = new DBHelper(getApplicationContext());
            API api = new API(getApplicationContext());
            api.setForceUpdate(forceUpdate);
            final ModelResponseCourseComment responseCourseComment;
            final ModelResponseLogin login = helper.getLoginRecord();
            if (login != null) {
                responseCourseComment = api.course_comment_get(login.getToken(), this.courseId);
                if (responseCourseComment != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            modelCourseCommentArrayList.clear();
                            if (responseCourseComment.getComments() != null) {
                                modelCourseCommentArrayList.addAll(responseCourseComment.getComments());
                            }
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


    private void refreshCourse(final boolean forceUpdate) {
        try {
            DBHelper helper = new DBHelper(getApplicationContext());
            API api = new API(getApplicationContext());
            api.setForceUpdate(forceUpdate);
            modelCourse = api.course(helper.getLoginRecord().getToken(), courseId, courseCode, courseClass);

            if (modelCourse != null) {
                if (modelCourse.getCode() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewCourseFaculty.setText("评分：" + modelCourse.getRank());
                            textViewCourseCredit.setText("学分：" + modelCourse.getCredit());
                            modelTeacherArrayList.clear();
                            modelTeacherArrayList.addAll(modelCourse.getTeachers()); // 老师信息
                            Log.d(modelCourse.getName_zh(), modelCourse.getRank());
                            adapterTeacherList.notifyDataSetChanged();
                            // TODO:make sure that you're not calling
                            // notifyDataSetChanged(), setAdapter(Adapter), or swapAdapter(Adapter, boolean)
                            // for small updates. Those methods signal that the entire list content has changed,
                            // and will show up in Systrace as RV FullInvalidate. Instead, use SortedList or DiffUtil
                            // to generate minimal updates when content changes or is added.
                            for (ModelTeacher teacher : modelCourse.getTeachers()) {
                                Log.d("TEACHERS", teacher.getName_zh());
                            }

                            if (!forceUpdate) {
                                //这里可能会有疑问为什么要判断 forceUpdate
                                // 因为 forceUpdate 在目前看来，只有创建这个 Activity 的时候会设置成 false
                                // 来加载本地数据，forceUpdate 在 true 的时候就是下拉刷新的状态了，
                                // 下拉刷新后 SwipeRefreshLayout 的 Refreshing 应该由 refreshCourseComment
                                // 结束后停止。
                                // 我们要做的效果是，初次加载这个科目的时候，他不是得请求后端API吗，这个过程没有动画
                                // 的话，用户可能以为程序死掉了，我们给这个耗时的过程
                                // 加一个 Refreshing 的效果，
                                // 所以你看 refreshCourse(false); 前面有一句
                                // swipe_refresh_layout.setRefreshing(true);
                                // 我们刷新完了（forceUpdate=false的情况）在这里停下就好了
                                swipeRefreshLayout.setRefreshing(false); // 这个要在UI线程里操作
                                isRefreshing = false;
                            }
                        }
                    });
                    // TODO: 可以给老师信息列表加个动画
                } else {
                    /* 获取失败，请看错误代码*/
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void swipeRefreshLayoutOnRefresh() {
        //检查是否处于刷新状态
        Log.d("API onRefresh", String.valueOf(swipeRefreshLayout.isRefreshing()) + " " + String.valueOf(isRefreshing));
        if (!isRefreshing) {
            isRefreshing = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshCourse(true);
                    refreshCourseComment(true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false); // 这个要在UI线程里操作
                            isRefreshing = false;
                        }
                    });

                }
            }).start();
        }
    }

    private void initComponent() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutOnRefresh();
            }
        });
        textViewCourseTitle = (TextView) findViewById(R.id.course_title);
        textViewCourseSchedule = (TextView) findViewById(R.id.course_schedule);
        textViewCourseCode = (TextView) findViewById(R.id.course_code);
        textViewCourseClass = (TextView) findViewById(R.id.course_class);
        textViewCourseFaculty = (TextView) findViewById(R.id.course_faculty);
        textViewCourseCredit = (TextView) findViewById(R.id.course_credit);

        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", 0);
        courseCode = intent.getStringExtra("course_code");
        courseClass = intent.getStringExtra("course_class");

        textViewCourseTitle.setText(intent.getStringExtra("course_title"));
        textViewCourseSchedule.setText(intent.getStringExtra("course_schedule"));
        textViewCourseCode.setText("编号：" + courseCode);
        textViewCourseClass.setText("班级：" + courseClass);

        recyclerViewTeacherList = (RecyclerView) findViewById(R.id.recyclerViewTeacherList);
        recyclerViewTeacherList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTeacherList.setHasFixedSize(true);
        recyclerViewTeacherList.setNestedScrollingEnabled(false);

        recyclerViewCourseComment = (RecyclerView) findViewById(R.id.recyclerViewCourseComment);
        recyclerViewCourseComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourseComment.setHasFixedSize(true);
        recyclerViewCourseComment.setNestedScrollingEnabled(false);

        ModelCourseComment comment = new ModelCourseComment(
                0,"Junyi","Junyi","1709853D-I011-0021",96,
                0,4.5,"下拉即可刷新课程信息和评价","2019/8/8"
        );
        for (int i = 0; i < 1; i++) {
            modelCourseCommentArrayList.add(comment);
        }

        //set data and list adapter
        adapterTeacherList = new AdapterTeacherList(this, modelTeacherArrayList);
        adapterCourseCommentList = new AdapterCourseCommentList(this, modelCourseCommentArrayList);
        adapterFtpList = new AdapterFtpList(this, modelFtpArrayList);

        recyclerViewTeacherList.setAdapter(adapterTeacherList);
        recyclerViewCourseComment.setAdapter(adapterCourseCommentList);

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

        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshCourse(false);
                refreshCourseComment(false);
            }
        }).start();
    }
}
