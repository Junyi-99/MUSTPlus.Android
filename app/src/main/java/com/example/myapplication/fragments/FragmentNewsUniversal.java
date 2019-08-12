package com.example.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterNewsListSectioned;
import com.example.myapplication.models.ModelNews;

import java.util.ArrayList;
import java.util.List;


public class FragmentNewsUniversal extends Fragment {

    //当前View是否创建了。
    protected boolean isViewInitiated;
    //当前数据是否加载了。
    protected boolean isDataInitiated;
    List<ModelNews> modelNewsItems = new ArrayList<ModelNews>();
    ;
    private View this_view;
    private RecyclerView recycler_view;
    private AdapterNewsListSectioned mAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private boolean isRefresh = false;//是否刷新中

    // 当用户看到这个页面的时候，再去加载数据
    // 当用户看到这个页面的时候，再去加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public void getData() {
        modelNewsItems.add(new ModelNews("商學院", "更改 消費者行為(BBAZ16401)D2的上課時間", "2019-06-21", false, "downContent('12294');", R.drawable.image_junyi));
        modelNewsItems.add(new ModelNews("教務處", "通告：領取2018年12月大學英語四六級考試成績報告單", "2019-06-19", false, "downContent('12293');", R.drawable.image_junyi));
        modelNewsItems.add(new ModelNews("通識教育部", "通識教育部招聘教學助理", "2019-06-18", true, "downContent('12292');", R.drawable.image_junyi));
        modelNewsItems.add(new ModelNews("教務處", "通告：2018/2019學年第二學期期末補考時間表", "2019-06-17", true, "downContent('12294');", R.drawable.image_junyi));
        modelNewsItems.add(new ModelNews("總務處", "學校商戶於暑假的營業時間", "2019-06-05", false, "downContent('12294');", R.drawable.image_junyi));
        modelNewsItems.add(new ModelNews("酒店與旅遊管理學院", "2019年畢業典禮事宜", "2019-06-04", true, "downContent('12294');", R.drawable.image_junyi));
        mAdapter.notifyDataSetChanged();
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        Log.d("Fragment News", "" + getUserVisibleHint() + isViewInitiated);
        if (getUserVisibleHint() && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            getData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }
    // 当用户看到这个页面的时候，再去加载数据
    // 当用户看到这个页面的时候，再去加载数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this_view = inflater.inflate(R.layout.fragment_news_universal, container, false);

        swipe_refresh_layout = (SwipeRefreshLayout) this_view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //检查是否处于刷新状态
                if (!isRefresh) {
                    isRefresh = true;
                    //模拟加载网络数据，这里设置2秒，正好能看到2色进度条
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            //显示或隐藏刷新进度条
                            swipe_refresh_layout.setRefreshing(false);
                            //mSwipeLayout.setRefreshing(true);
                            //修改adapter的数据
                            //data.add("这是新添加的数据");
                            mAdapter.notifyDataSetChanged();
                            isRefresh = false;
                        }
                    }, 2000);
                }
            }
        });

        initComponent();
        Log.d("Fragment ModelNews All", "onCreateView");

        return this_view;
    }

    private void initComponent() {
        mAdapter = new AdapterNewsListSectioned(this_view.getContext(), modelNewsItems);
        mAdapter.setOnItemClickListener(new AdapterNewsListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelNews obj, int position) {
                Snackbar.make(this_view, "Item " + obj.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        recycler_view = (RecyclerView) this_view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this_view.getContext()));
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(mAdapter);
    }


}
