package com.example.myapplication.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterTeacherList;
import com.example.myapplication.model.ModelTeacher;

import java.util.ArrayList;

public class ActivityCourseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterTeacherList mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_temp);

        initComponent();
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTeachers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ArrayList<ModelTeacher> modelTeacher = new ArrayList<ModelTeacher>();

        TypedArray drw_arr = this.getResources().obtainTypedArray(R.array.people_images);
        modelTeacher.add(new ModelTeacher(
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
        ));
        modelTeacher.add(new ModelTeacher(
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
        ));
        modelTeacher.add(new ModelTeacher(
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
        ));
        modelTeacher.add(new ModelTeacher(
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
        ));
        //set data and list adapter
        mAdapter = new AdapterTeacherList(this, modelTeacher);
        recyclerView.setAdapter(mAdapter);


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterTeacherList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelTeacher obj, int position) {

            }
        });
    }
}
