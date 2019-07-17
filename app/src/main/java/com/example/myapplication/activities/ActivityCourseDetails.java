package com.example.myapplication.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterCourseCommentList;
import com.example.myapplication.adapters.AdapterFtpList;
import com.example.myapplication.adapters.AdapterTeacherList;
import com.example.myapplication.models.ModelCourseComment;
import com.example.myapplication.models.ModelFtp;
import com.example.myapplication.models.ModelTeacher;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class ActivityCourseDetails extends AppCompatActivity {

    private RecyclerView recyclerViewTeacherList;
    private AdapterTeacherList adapterTeacherList;
    private RecyclerView recyclerViewCourseComment;
    private AdapterCourseCommentList adapterCourseCommentList;
    private RecyclerView recyclerViewFtpList;
    private AdapterFtpList adapterFtpList;

    private ArrayList<ModelTeacher> modelTeacherArrayList;
    private ArrayList<ModelCourseComment> modelCourseCommentArrayList;
    private ArrayList<ModelFtp> modelFtpArrayList;

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

        FloatingActionsMenu floatingActionButton = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
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
        });
    }

    private void initComponent() {

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

        modelTeacherArrayList = new ArrayList<ModelTeacher>();
        modelCourseCommentArrayList = new ArrayList<ModelCourseComment>();
        modelFtpArrayList = new ArrayList<ModelFtp>();

        TypedArray drw_arr = this.getResources().obtainTypedArray(R.array.people_images);

        ModelTeacher modelTeacher1 = new ModelTeacher(
                "罗绍龙",
                "Lo Sio Long",
                "IT",
                "http://www.baidu.com",
                drw_arr.getResourceId(0, -1),
                null,
                null,
                null,
                null,
                null
        );
        modelTeacherArrayList.add(modelTeacher1);
        modelTeacherArrayList.add(modelTeacher1);
        modelTeacherArrayList.add(modelTeacher1);
        modelTeacherArrayList.add(modelTeacher1);

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
    }
}
