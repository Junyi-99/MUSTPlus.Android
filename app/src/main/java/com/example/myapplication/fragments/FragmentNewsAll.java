package com.example.myapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterListSectioned;
import com.example.myapplication.models.ModelNews;
import com.example.myapplication.models.ModelNewsImage;
import com.example.myapplication.utils.Tools;

import java.util.ArrayList;
import java.util.List;


public class FragmentNewsAll extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View this_view;
    private ViewPager view_pager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private RecyclerView recyclerView;
    private AdapterListSectioned mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    List<ModelNews> modelNewsItems;
    TypedArray drw_arr;
    private TextView slider_image_title;
    private TextView slider_image_brief;
    private boolean isRefresh = false;//是否刷新中

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this_view = inflater.inflate(R.layout.fragment_news_all, container, false);
        return this_view;
        /*mSwipeLayout = (SwipeRefreshLayout) this_view.findViewById(R.id.swipeLayout);

        //设置进度条的颜色主题，最多能设置四种 加载颜色是循环播放的，只要没有完成刷新就会一直循环，
        mSwipeLayout.setColorSchemeColors(Color.rgb(25, 118, 210));
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置下拉刷新的监听
        mSwipeLayout.setOnRefreshListener(this);

        initComponent();
        Log.d("Fragment ModelNews All", "onCreateView");
        // Inflate the layout for this fragment
        return this_view;*/
    }

    private void initComponent() {
        /*
         * ModelNews List
         * */
        recyclerView = (RecyclerView) this_view.findViewById(R.id.recyclerViewNewsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this_view.getContext()));
        recyclerView.setHasFixedSize(true);
        modelNewsItems = new ArrayList<ModelNews>();

        drw_arr = this_view.getContext().getResources().obtainTypedArray(R.array.people_images);
        modelNewsItems.add(new ModelNews("商學院", "更改 消費者行為(BBAZ16401)D2的上課時間", "2019-06-21", false, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("教務處", "通告：領取2018年12月大學英語四六級考試成績報告單", "2019-06-19", false, "downContent('12293');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("通識教育部", "通識教育部招聘教學助理", "2019-06-18", true, "downContent('12292');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("教務處", "通告：2018/2019學年第二學期期末補考時間表", "2019-06-17", true, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("總務處", "學校商戶於暑假的營業時間", "2019-06-05", false, "downContent('12294');", drw_arr.getResourceId(0, -1)));
        modelNewsItems.add(new ModelNews("酒店與旅遊管理學院", "2019年畢業典禮事宜", "2019-06-04", true, "downContent('12294');", drw_arr.getResourceId(0, -1)));

        //set data and list adapter
        mAdapter = new AdapterListSectioned(this_view.getContext(), modelNewsItems);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelNews obj, int position) {
                Snackbar.make(this_view, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        /*
         * Set Slider ModelNewsImage
         *
         * */
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


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<ModelNewsImage> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, ModelNewsImage obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<ModelNewsImage> items) {
            this.act = activity;
            this.items = items;
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

            ImageView image = (ImageView) v.findViewById(R.id.teacher_avatar);
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

    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    /*
     * 监听器SwipeRefreshLayout.OnRefreshListener中的方法，当下拉刷新后触发
     */
    public void onRefresh() {
        //检查是否处于刷新状态
        if (!isRefresh) {
            isRefresh = true;
            //模拟加载网络数据，这里设置2秒，正好能看到2色进度条
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    //显示或隐藏刷新进度条
                    mSwipeLayout.setRefreshing(false);
                    //mSwipeLayout.setRefreshing(true);
                    //修改adapter的数据
                    //data.add("这是新添加的数据");
                    modelNewsItems.add(new ModelNews("电竞学院", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "2019-06-05", false, "downContent('12294');", drw_arr.getResourceId(0, -1)));
                    mAdapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }, 2000);
        }
    }
}
