package com.example.myapplication.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myapplication.R;
import com.example.myapplication.fragments.FragmentMineAbstract;
import com.example.myapplication.fragments.FragmentMomentsAbstract;
import com.example.myapplication.fragments.FragmentNewsAbstract;
import com.example.myapplication.fragments.FragmentTimetableAbstract;

import java.util.ArrayList;
import java.util.List;

public class AdapterSectionsPager extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = {
            R.string.tab_text_timetable,
            R.string.tab_text_news,
            R.string.tab_text_moments,
            R.string.tab_text_mine
    };
    private final Context mContext;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();


    public AdapterSectionsPager(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return new FragmentTimetableAbstract();
            case 1:
                return new FragmentNewsAbstract();
            case 2:
                return new FragmentMomentsAbstract();
            case 3:
                return new FragmentMineAbstract();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }
   /* public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }*/
}
