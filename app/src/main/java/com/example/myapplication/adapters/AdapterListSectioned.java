package com.example.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.News;
import com.example.myapplication.utils.Tools;


import java.util.List;

public class AdapterListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ARTICLE = 1;
    private final int FILE = 0;

    private List<News> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, News obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListSectioned(Context context, List<News> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public View lyt_parent;
        public TextView news_fac_dep;
        public TextView news_time;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.news_icon);
            name = (TextView) v.findViewById(R.id.news_title); // item people chat
            news_fac_dep = (TextView) v.findViewById(R.id.news_fac_dep);
            news_time = (TextView) v.findViewById(R.id.news_time);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FILE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_file, parent, false);
            return new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_article, parent, false);
            return new OriginalViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        News p = items.get(position);
        OriginalViewHolder view = (OriginalViewHolder) holder;
        view.name.setText(p.title);
        view.news_fac_dep.setText(p.faculty_department);
        view.news_time.setText(p.date);

        Tools.displayImageRound(ctx, view.image, p.image);
        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).type ? ARTICLE : FILE;
    }

    public void insertItem(int index, News people) {
        items.add(index, people);
        notifyItemInserted(index);
    }

}