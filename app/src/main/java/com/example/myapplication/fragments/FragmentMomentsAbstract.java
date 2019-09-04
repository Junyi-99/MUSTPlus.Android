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

public class FragmentMomentsAbstract extends AbstractLazyLoadFragment {

    private RecyclerView recycler_view_moment_list;
    private ArrayList<ModelResponseMoment> modelResponseMomentArrayList;
    private AdapterMomentList adapterMomentList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moments_temp, container, false);
        return view;
    }


    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();
        Log.e("FragmentMoments", "onFirstVisible");
    }
}
