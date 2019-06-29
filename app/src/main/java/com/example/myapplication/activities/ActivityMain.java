package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
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


    private ViewPager viewPager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 確認取消半透明設置。
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE); // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // 跟系統表示要渲染 system bar 背景。
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        // 禁止左右滑动切换 Page
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        // 设置Pages
        setupViewPager(viewPager);
//
//
        BottomNavigationView navView = findViewById(R.id.navigation);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_timetable, menu);
        return true;
    }
*/
    private void setupViewPager(ViewPager viewPager) {
        AdapterSectionsStatePager adapter = new AdapterSectionsStatePager(getSupportFragmentManager());
        adapter.addFragment(new FragmentTimetable(), "FragmentTimetable");
        adapter.addFragment(new FragmentNews(), "FragmentNews");
        adapter.addFragment(new FragmentMoments(), "FragmentMoments");
        adapter.addFragment(new FragmentMine(), "FragmentMine");
        viewPager.setAdapter(adapter);
    }

    // 设置当前 ViewPager 的 Fragment
    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber, true);
    }
}
