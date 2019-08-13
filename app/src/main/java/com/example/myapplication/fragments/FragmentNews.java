package com.example.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

public class FragmentNews extends LazyLoadFragment {
    boolean visible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_news = inflater.inflate(R.layout.fragment_news, container, false);

        ViewPager view_pager = (ViewPager) fragment_news.findViewById(R.id.view_pager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(4);
        TabLayout tabLayout = (TabLayout) fragment_news.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(view_pager);

        return fragment_news;
    }

    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();
        visible = true;
        Log.e("FragmentNews", "onFirstVisible");
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = new String[]{
                "全部",
                "学院",
                "通知",
                "文件"
        };

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new FragmentNewsAll();
            // 这里的position跟FragmentNewsUniversal的position是对应的
            return FragmentNewsUniversal.newInstance(position);
        }


        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
