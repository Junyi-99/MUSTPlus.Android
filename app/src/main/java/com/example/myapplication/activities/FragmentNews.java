package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import java.util.ArrayList;
import java.util.List;

public class FragmentNews extends Fragment {
    private static final String TAG = "FragementNews";

    private void setupViewPager(ViewPager viewPager) {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new FragmentNewsAll(), "All");
        adapter.addFragment(new FragmentMine(), "資訊科技學院");
        adapter.addFragment(new FragmentMine(), "商學院");
        adapter.addFragment(new FragmentMine(), "法學院");
        adapter.addFragment(new FragmentMine(), "中醫藥學院");
        adapter.addFragment(new FragmentMine(), "酒店與旅遊管理學院");
        adapter.addFragment(new FragmentMine(), "人文藝術學院");
        adapter.addFragment(new FragmentMine(), "醫學院");
        adapter.addFragment(new FragmentMine(), "國際學院");
        adapter.addFragment(new FragmentMine(), "藥學院");
        adapter.addFragment(new FragmentMine(), "研究生院");
        adapter.addFragment(new FragmentMine(), "通識教育部");
        adapter.addFragment(new FragmentMine(), "持續教育學院");
        adapter.addFragment(new FragmentMine(), "健康科學學院");


        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Inflate the layout for this fragment
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


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

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
