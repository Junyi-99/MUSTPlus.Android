package com.example.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterMomentList;
import com.example.myapplication.models.ModelResponseMoment;

import java.util.ArrayList;

public class FragmentMoments extends Fragment {

    private RecyclerView recyclerViewMomentList;
    private ArrayList<ModelResponseMoment> modelResponseMomentArrayList;
    private AdapterMomentList adapterMomentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moments, container, false);
        recyclerViewMomentList = (RecyclerView) view.findViewById(R.id.recyclerViewMomentList);
        recyclerViewMomentList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewMomentList.setHasFixedSize(true);
        recyclerViewMomentList.setNestedScrollingEnabled(false);

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
        recyclerViewMomentList.setAdapter(adapterMomentList);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment Moments", "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment Moments", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment Moments", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment Moments", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment Moments", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment Moments", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment Moments", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment Moments", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment Moments", "onDetach");
    }
}
