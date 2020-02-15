package com.example.myapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterNewsListSectioned;
import com.example.myapplication.models.ModelNews;
import com.example.myapplication.models.ModelNewsImage;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelResponseNewsAll;
import com.example.myapplication.utils.API.APIPersistence;
import com.example.myapplication.utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Junyi
 */
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
    public boolean activate = false;
    TypedArray drwArr;
    private ArrayList<ModelNews> modelNewsItems;
    private ModelResponseNewsAll modelResponseNewsAll;
    private View thisView;
    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private AdapterImageSlider adapterImageSlider;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private AdapterNewsListSectioned mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private TextView sliderImageTitle;
    private TextView sliderImageBrief;
    private boolean isRefreshing = false;



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
        thisView = inflater.inflate(R.layout.fragment_news_all, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) thisView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TaskNewsRefresh(true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        initComponent();

        new TaskNewsRefresh(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return thisView;
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
        modelNewsItems = new ArrayList<ModelNews>();
        modelNewsItems.add(new ModelNews("MUST+提示", "这里是你所在学院的新闻，下拉即可刷新新闻列表", "2019-06-21", true, "", R.drawable.image_junyi));
        mAdapter = new AdapterNewsListSectioned(thisView.getContext(), modelNewsItems);
        mAdapter.setOnItemClickListener(new AdapterNewsListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelNews obj, int position) {
                Snackbar.make(thisView, "Item " + obj.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(thisView.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView = (RecyclerView) thisView.findViewById(R.id.recyclerViewNewsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                NestedScrollView scroller = (NestedScrollView) thisView.findViewById(R.id.nested_scroll_view);
                if (scroller != null) {
                    scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollx, int scrolly, int oldscrollx, int oldscrolly) {
                           /* if (scrolly > oldscrolly) {
                                Log.i("recyclerView", "Scroll DOWN");
                            }
                            if (scrolly < oldscrolly) {
                                Log.i("recyclerView", "Scroll UP");
                            }
                            if (scrolly == 0) {
                                Log.i("recyclerView", "TOP SCROLL");
                            }*/
                            if (scrolly == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                                Log.e("recyclerView", "BOTTOM SCROLL,加载更多");
                                Snackbar.make(thisView, "触底，这里是加载更多刷新操作", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // findView之后放到变量里，防止下次再find一次，优化性能
        sliderImageTitle = ((TextView) thisView.findViewById(R.id.title));
        sliderImageBrief = ((TextView) thisView.findViewById(R.id.brief));
        layoutDots = (LinearLayout) thisView.findViewById(R.id.layout_dots);
        viewPager = (ViewPager) thisView.findViewById(R.id.pager);

        // 轮播图片部分
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
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layoutDots, adapterImageSlider.getCount(), 0);

        sliderImageTitle.setText(items.get(0).name);
        sliderImageBrief.setText(items.get(0).brief);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                sliderImageTitle.setText(items.get(pos).name);
                sliderImageBrief.setText(items.get(pos).brief);
                addBottomDots(layoutDots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 因为没有切换 fragment，adapter 不会调用 setUserVisibleHint
        startAutoSlider(adapterImageSlider.getCount());
    }

    /**
     * 给 Slider ModelNewsImage 添加下面的导航圆点
     *
     * @param layoutDots .
     * @param size       .
     * @param current    .
     */
    private void addBottomDots(LinearLayout layoutDots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this.getContext());
            int widthHeight = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(widthHeight, widthHeight));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);

            if (i == current) {
                dots[current].setImageResource(R.drawable.shape_circle);
            } else {
                dots[i].setImageResource(R.drawable.shape_circle_outline);
            }

            layoutDots.addView(dots[i]);
        }

    }

    /**
     * 自动滚动图片 view_pager 设置 currentItem 之后会触发 onPageSelected，
     * 所以下面的文字也会切换
     *
     * @param count .
     */
    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) {
                    pos = 0;
                }
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
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
            RelativeLayout lytParent = (RelativeLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.image);
            lytParent.setOnClickListener(new View.OnClickListener() {
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
            /**
             * 当 item 被 click
             *
             * @param view 父View
             * @param obj  被点击的对象
             */
            void onItemClick(View view, ModelNewsImage obj);
        }

    }

    private class TaskNewsRefresh extends AsyncTask<Void, String, String> {
        private boolean forceUpdate;
        private TaskNewsRefresh(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
            isRefreshing = true;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()) {
                Toast.makeText(getContext(), "错误：" + s, Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            isRefreshing = false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            swipeRefreshLayout.setRefreshing(false);
            isRefreshing = false;
        }


        @Override
        protected String doInBackground(Void... voids) {
            try {
                DBHelper helper = new DBHelper(getContext());
                ModelResponseLogin login = helper.getLoginRecord();

                if (login != null) {
                    APIPersistence api = new APIPersistence(getContext());
                    api.setForceUpdate(forceUpdate);
                    modelResponseNewsAll = api.news_all_get(login.getToken(), 0, 20);
                    if (modelResponseNewsAll != null && modelResponseNewsAll.getCode() == 0) {
                        modelNewsItems.clear();
                        modelNewsItems.addAll(modelResponseNewsAll.getNews_list());
                    }
                } else {
                    return "您还未登录，请登录后重试";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return "";
        }
    }
}
