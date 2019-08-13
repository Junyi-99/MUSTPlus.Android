package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

public class FragmentMine extends LazyLoadFragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Fragment Mine", "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment Mine", "onCreate");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment Mine", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment Mine", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment Mine", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment Mine", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment Mine", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment Mine", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment Mine", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment Mine", "onDetach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void onFirstVisible() {
        Log.e("FragmentMine", "onFirstVisible");
        super.onFirstVisible();
    }
}
