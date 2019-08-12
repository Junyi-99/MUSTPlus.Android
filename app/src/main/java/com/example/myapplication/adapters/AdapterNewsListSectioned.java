package com.example.myapplication.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import com.example.myapplication.utils.Tools;

import java.util.List;

public class AdapterNewsListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final long DURATION_IN_FADE_ID = 250;
    private final int ARTICLE = 1;
    private final int FILE = 0;

    private boolean on_attach = true;
    private List<ModelNews> news;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public AdapterNewsListSectioned(Context context, List<ModelNews> news) {
        this.news = news;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
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

  /*  @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }*/

    private void animate(View view, int position) {
        Log.d("Position", String.valueOf(position));
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        animator.setDuration(DURATION_IN_FADE_ID)
                .setStartDelay(position == 0 ? 0 : (position * DURATION_IN_FADE_ID / 3));
        animatorSet.play(animator);
        animatorSet.start();
        /*ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setStartDelay(position == 0 ? 0 : (position * DURATION_IN_FADE_ID / 3));
        animatorAlpha.setDuration(DURATION_IN_FADE_ID);
        animatorSet.play(animatorAlpha);
        animatorSet.start();*/

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ModelNews p = news.get(position);
        OriginalViewHolder view = (OriginalViewHolder) holder;
        view.name.setText(p.getTitle());
        view.news_fac_dep.setText(p.getFaculty_department());
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

        // Set Animation
        animate(view.itemView, position);

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