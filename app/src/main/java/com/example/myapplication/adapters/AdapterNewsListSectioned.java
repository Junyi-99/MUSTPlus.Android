package com.example.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.ModelNews;
import com.example.myapplication.utils.ItemAnimation;
import com.example.myapplication.utils.Tools;

import java.util.List;

public class AdapterNewsListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ARTICLE = 1;
    private final int FILE = 0;

    private boolean on_attach = true;
    private List<ModelNews> news;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private int lastPosition = -1;

    public AdapterNewsListSectioned(Context context, List<ModelNews> news) {
        if (news == null) {
            Log.e("AdapterError!", "news is null!");
        }
        this.news = news;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ModelNews p = news.get(position);
        OriginalViewHolder view = (OriginalViewHolder) holder;
        view.name.setText(p.getTitle());
        view.news_fac_dep.setText(p.getFac_dep());
        view.news_time.setText(p.getDate());

        Tools.displayImageRound(context, view.image, p.getImage());
        view.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, news.get(position), position);
                }
            }
        });

        //Log.d("Position", position + " " + lastPosition);
        if (position > lastPosition) {
            ItemAnimation.animate(view.itemView, on_attach ? position : -1, ItemAnimation.FADE_IN);
            lastPosition = position;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (position == news.size()) {

        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.news.get(position).getType() ? ARTICLE : FILE;
    }

    public void insertItem(int index, ModelNews people) {
        news.add(index, people);
        notifyItemInserted(index);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ModelNews obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        View root;
        TextView news_fac_dep;
        TextView news_time;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.icon);
            name = (TextView) v.findViewById(R.id.name);
            news_fac_dep = (TextView) v.findViewById(R.id.teacher_name_en);
            news_time = (TextView) v.findViewById(R.id.news_time);
            root = (View) v.findViewById(R.id.lyt_parent);
        }
    }
}