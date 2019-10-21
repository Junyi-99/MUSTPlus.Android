package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterSectionsPager;
import com.example.myapplication.fragments.LazyFragmentMine;
import com.example.myapplication.fragments.FragmentMomentsAbstract;
import com.example.myapplication.fragments.FragmentNewsAbstract;
import com.example.myapplication.fragments.FragmentTimetableAbstract;

/**
 * @author Junyi
 */
public class ActivityMain extends AppCompatActivity {
    private TextView mTextMessage;
    private FragmentTimetableAbstract fragmentTimetable;
    private FragmentNewsAbstract fragmentNews;
    private FragmentMomentsAbstract fragmentMoments;
    private LazyFragmentMine fragmentMine;

    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_timetable:
                    setViewPager(0);
                    return true;
                case R.id.navigation_news:
                    setViewPager(1);
                    return true;
                case R.id.navigation_moments:
                    setViewPager(2);
                    return true;
                case R.id.navigation_mine:
                    setViewPager(3);
                    return true;
            }
            return false;
        }
    };

    // 设置当前 ViewPager 的 Fragment
    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AdapterSectionsPager adapterSectionsPager = new AdapterSectionsPager(this, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapterSectionsPager);
        viewPager.setOffscreenPageLimit(4); // 有效改善用户体验
        mTextMessage = findViewById(R.id.message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
