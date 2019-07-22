package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterSectionsStatePager;
import com.example.myapplication.fragments.FragmentMine;
import com.example.myapplication.fragments.FragmentMoments;
import com.example.myapplication.fragments.FragmentNews;
import com.example.myapplication.fragments.FragmentTimetable;

public class ActivityMain extends AppCompatActivity {
    private TextView mTextMessage;
    private FragmentTimetable fragmentTimetable;
    private FragmentNews fragmentNews;
    private FragmentMoments fragmentMoments;
    private FragmentMine fragmentMine;

    private ViewPager viewPager;

    // 设置当前 ViewPager 的 Fragment
    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber, true);
    }

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivityMain onCreate", "on create!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        BottomNavigationView navView = findViewById(R.id.navigation);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 初始化 Fragments
        fragmentTimetable = new FragmentTimetable();
        fragmentNews = new FragmentNews();
        fragmentMoments = new FragmentMoments();
        fragmentMine = new FragmentMine();

        // 设置Pages
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterSectionsStatePager adapter = new AdapterSectionsStatePager(getSupportFragmentManager());
        adapter.addFragment(fragmentTimetable, "FragmentTimetable");
        adapter.addFragment(fragmentNews, "FragmentNews");
        adapter.addFragment(fragmentMoments, "FragmentMoments");
        adapter.addFragment(fragmentMine, "FragmentMine");
        viewPager.setAdapter(adapter);
    }


}
