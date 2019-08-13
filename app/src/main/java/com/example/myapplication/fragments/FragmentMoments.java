package com.example.myapplication.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterMomentList;
import com.example.myapplication.models.ModelResponseMoment;

import java.util.ArrayList;

public class FragmentMoments extends LazyLoadFragment {

    private RecyclerView recycler_view_moment_list;
    private ArrayList<ModelResponseMoment> modelResponseMomentArrayList;
    private AdapterMomentList adapterMomentList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moments_temp, container, false);
        /*recycler_view_moment_list = (RecyclerView) view.findViewById(R.id.recycler_view_moment_list);
        recycler_view_moment_list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_view_moment_list.setHasFixedSize(true);
        recycler_view_moment_list.setNestedScrollingEnabled(false);

        modelResponseMomentArrayList = new ArrayList<ModelResponseMoment>();

        modelResponseMomentArrayList.add(new ModelResponseMoment(
                0,
                "1709853D-I011-0021",
                "http://www.baidu.com",
                R.drawable.losiolong,
                "没有毛驴的阿凡提",
                "今天 22:37",
                "用RecyclerView实现九宫格的布局，除了常规的LinearLayout之外，还有比较常用的GridLayoutManager和StaggeredGridLayoutManager",
                15,
                2,
                new ArrayList<String>() {
                    {
                        add("1");
                        add("2");
                        add("3");
                    }
                }
        ));

        adapterMomentList = new AdapterMomentList(view.getContext(), modelResponseMomentArrayList);
        recycler_view_moment_list.setAdapter(adapterMomentList);
        // Inflate the layout for this fragment*/
        return view;
    }


    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();
        Log.e("FragmentMoments", "onFirstVisible");
    }
}
