package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;

public class FragmentNews extends Fragment {
    private static final String TAG = "FragementNews";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Inflate the layout for this fragment


        Button b=(Button) view.findViewById(R.id.button_fade_in);
        final ImageView imageView = (ImageView) view.findViewById(R.id.fade_in_image);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation startAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in_animation);
                imageView.startAnimation(startAnimation);
            }
        });


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment News", "onCreate");

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment News", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment News", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment News", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment News", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment News", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment News", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment News", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment News", "onDetach");
    }
}
