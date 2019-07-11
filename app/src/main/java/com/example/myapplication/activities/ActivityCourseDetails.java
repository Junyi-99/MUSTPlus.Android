package com.example.myapplication.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterListSectioned;
import com.example.myapplication.model.ModelNews;

import java.util.ArrayList;

public class ActivityCourseDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_details_temp);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        initComponent();
    }

    private void initComponent() {

        modelNewsItems = new ArrayList<ModelNews>();

        drw_arr = this_view.getContext().getResources().obtainTypedArray(R.array.people_images);
        modelNewsItems.add(new ModelNews("商學院", "更改 消費者行為(BBAZ16401)D2的上課時間", "2019-06-21", false, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("教務處", "通告：領取2018年12月大學英語四六級考試成績報告單", "2019-06-19", false, "downContent('12293');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("通識教育部", "通識教育部招聘教學助理", "2019-06-18", true, "downContent('12292');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("教務處", "通告：2018/2019學年第二學期期末補考時間表", "2019-06-17", true, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("總務處", "學校商戶於暑假的營業時間", "2019-06-05", false, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("酒店與旅遊管理學院", "2019年畢業典禮事宜", "2019-06-04", true, "downContent('12294');", drw_arr.getResourceId(0, -1)));

        //set data and list adapter
        mAdapter = new AdapterListSectioned(this, modelNewsItems);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelNews obj, int position) {
                Snackbar.make(this_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
