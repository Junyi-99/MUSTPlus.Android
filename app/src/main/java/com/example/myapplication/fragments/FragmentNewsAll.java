package com.example.myapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterNewsListSectioned;
import com.example.myapplication.models.ModelNews;
import com.example.myapplication.models.ModelNewsImage;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.utils.API;
import com.example.myapplication.utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentNewsAll extends Fragment {
    private static int[] array_image_place = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
    };
    private static String[] array_title_place = {
            "澳科大於墨爾本舉辦《回歸盛世》影像展覽",
            "2019年澳門僱員信心及滿意度指數",
            "傑出華人數學家張益唐教授於澳科大报告",

    };
    private static String[] array_brief_place = {
            "澳門影像舘",
            "可持續發展研究所",
            "N211",
    };
    ArrayList<ModelNews> modelNewsItems;
    ModelResponseNewsAll modelResponseNewsAll;
    TypedArray drw_arr;
    private View this_view;
    private ViewPager view_pager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private RecyclerView recycler_view;
    private NestedScrollView nested_scroll_view;
    private AdapterNewsListSectioned mAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private TextView slider_image_title;
    private TextView slider_image_brief;
    private boolean isRefreshing = false;//是否刷新中

    // 第一次更新 News
    public void refreshNewsAll(final boolean force) {
        Log.e("RefreshNewsAll", "" + force);
        try {
            DBHelper helper = new DBHelper(getContext());
            API api = new API(getContext());
            api.setForceUpdate(force);
            ModelResponseLogin login = helper.getLoginRecord();
            if (login != null) {
                modelResponseNewsAll = api.news_all_get(helper.getLoginRecord().getToken(), 0, 20);
                if (modelResponseNewsAll != null && modelResponseNewsAll.getCode() == 0) {
                    modelNewsItems.clear();
                    modelNewsItems.addAll(modelResponseNewsAll.getNews_list());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            if (!force) { // 为啥这样写原理请见 ActivityCourseDetails 类似部分
                swipe_refresh_layout.setRefreshing(false);
                isRefreshing = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void swipeRefreshLayoutOnRefresh() {
        //检查是否处于刷新状态
        Log.d("API onRefresh", String.valueOf(swipe_refresh_layout.isRefreshing()) + " " + String.valueOf(isRefreshing));
        if (!isRefreshing) {
            isRefreshing = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshNewsAll(true);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_refresh_layout.setRefreshing(false);
                            isRefreshing = false;
                        }
                    });
                }
            }).start();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("NewsAll", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("NewsAll", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment ModelNews All", "onCreateView");
        this_view = inflater.inflate(R.layout.fragment_news_all, container, false);

        swipe_refresh_layout = (SwipeRefreshLayout) this_view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutOnRefresh();
            }
        });

        initComponent();

        swipe_refresh_layout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshNewsAll(false);
                FragmentActivity activity = getActivity();
                if (activity != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_refresh_layout.setRefreshing(false);
                            isRefreshing = false;
                        }
                    });
            }
        }).start();
        return this_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("NewsAll", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("NewsAll", "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("NewsAll", "onPause");

    }

    private void initComponent() {

        /*
         * ModelNews List
         * */

        modelNewsItems = new ArrayList<ModelNews>();
        modelNewsItems.add(new ModelNews("MUST+提示", "这里是你所在学院的新闻，下拉即可刷新新闻列表", "2019-06-21", true, "", R.drawable.image_junyi));

        mAdapter = new AdapterNewsListSectioned(this_view.getContext(), modelNewsItems);
        mAdapter.setOnItemClickListener(new AdapterNewsListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelNews obj, int position) {
                Snackbar.make(this_view, "Item " + obj.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this_view.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recycler_view = (RecyclerView) this_view.findViewById(R.id.recyclerViewNewsList);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setAdapter(mAdapter);
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                NestedScrollView scroller = (NestedScrollView) this_view.findViewById(R.id.nested_scroll_view);

                if (scroller != null) {

                    scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                            if (scrollY > oldScrollY) {
                                Log.i("recyclerView", "Scroll DOWN");
                            }
                            if (scrollY < oldScrollY) {
                                Log.i("recyclerView", "Scroll UP");
                            }

                            if (scrollY == 0) {
                                Log.i("recyclerView", "TOP SCROLL");
                            }

                            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                                Log.i("recyclerView", "BOTTOM SCROLL");
                                Snackbar.make(this_view, "触底，这里是加载更多刷新操作", Snackbar.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });


        slider_image_title = ((TextView) this_view.findViewById(R.id.title)); // findView之后放到变量里，防止下次再find一次，优化性能
        slider_image_brief = ((TextView) this_view.findViewById(R.id.brief));
        layout_dots = (LinearLayout) this_view.findViewById(R.id.layout_dots);
        view_pager = (ViewPager) this_view.findViewById(R.id.pager);

        adapterImageSlider = new AdapterImageSlider(this.getActivity(), new ArrayList<ModelNewsImage>());

        final List<ModelNewsImage> items = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            ModelNewsImage obj = new ModelNewsImage();
            obj.image = array_image_place[i];

            obj.imageDrw = getResources().getDrawable(obj.image);
            obj.name = array_title_place[i];
            obj.brief = array_brief_place[i];
            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        view_pager.setAdapter(adapterImageSlider);

        // displaying selected image first
        view_pager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);


        slider_image_title.setText(items.get(0).name);
        slider_image_brief.setText(items.get(0).brief);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                slider_image_title.setText(items.get(pos).name);
                slider_image_brief.setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        // 因为没有切换 fragment，adapter 不会调用 setUserVisibleHint
        startAutoSlider(adapterImageSlider.getCount());


    }

    // 给 Slider ModelNewsImage 添加下面的导航圆点
    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this.getContext());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);

            if (i == current)
                dots[current].setImageResource(R.drawable.shape_circle);
            else
                dots[i].setImageResource(R.drawable.shape_circle_outline);

            layout_dots.addView(dots[i]);
        }

    }

    // 自动滚动图片 view_pager 设置 currentItem 之后会触发 onPageSelected，
    // 所以下面的文字也会切换
    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = view_pager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                view_pager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<ModelNewsImage> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        // constructor
        private AdapterImageSlider(Activity activity, List<ModelNewsImage> items) {
            this.act = activity;
            this.items = items;
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public ModelNewsImage getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<ModelNewsImage> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ModelNewsImage o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.icon);
            RelativeLayout lyt_parent = (RelativeLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.image);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

        private interface OnItemClickListener {
            void onItemClick(View view, ModelNewsImage obj);
        }

    }
}
