package com.example.myapplication.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.myapplication.EmptyRecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterCourseComment;
import com.example.myapplication.adapters.AdapterTeacherList;
import com.example.myapplication.models.ModelCourseComment;
import com.example.myapplication.models.ModelTeacher;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ActivityCourseDetails extends AppCompatActivity {

    private RecyclerView recyclerViewTeacherList;
    private AdapterTeacherList adapterTeacherList;
    private RecyclerView recyclerViewCourseComment;
    private AdapterCourseComment adapterCourseComment;
    private ArrayList<ModelTeacher> modelTeacherArrayList;
    private ArrayList<ModelCourseComment> modelCourseCommentArrayList;

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

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
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
                adapterCourseComment.notifyDataSetChanged();
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
        recyclerViewCourseComment = (RecyclerView) findViewById(R.id.recyclerViewCourseComment);
        recyclerViewCourseComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourseComment.setHasFixedSize(true);

        recyclerViewTeacherList.setNestedScrollingEnabled(false);
        recyclerViewCourseComment.setNestedScrollingEnabled(false);

        modelTeacherArrayList = new ArrayList<ModelTeacher>();
        modelCourseCommentArrayList = new ArrayList<ModelCourseComment>();

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


        //set data and list adapter
        adapterTeacherList = new AdapterTeacherList(this, modelTeacherArrayList);
        recyclerViewTeacherList.setAdapter(adapterTeacherList);

        adapterCourseComment = new AdapterCourseComment(this, modelCourseCommentArrayList);
        recyclerViewCourseComment.setAdapter(adapterCourseComment);

        // on item list clicked
        adapterTeacherList.setOnItemClickListener(new AdapterTeacherList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelTeacher obj, int position) {

            }
        });
        adapterCourseComment.setOnItemClickListener(new AdapterCourseComment.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelCourseComment obj, int position) {

            }
        });
    }
}
