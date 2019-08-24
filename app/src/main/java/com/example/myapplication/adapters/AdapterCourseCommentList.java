package com.example.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.ModelCourseComment;

import java.util.List;

public class AdapterCourseCommentList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 两种显示模式
    // None: 显示空评论之提示layout
    // NotNone: 正常评论内容layout
    private final int None = 0;
    private final int NotNone = 1;

    private List<ModelCourseComment> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterCourseCommentList(Context context, List<ModelCourseComment> items) {
        this.items = items;
        this.ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == NotNone) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment, parent, false);
            return new OriginalViewHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment_none, parent, false);
        return new OriginalViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {

            ModelCourseComment m = items.get(position);
            OriginalViewHolder v = (OriginalViewHolder) holder;

            v.comment_content.setText(m.getContent());

            // 有昵称显示昵称，没有昵称显示姓名
            if (m.getNickname() == null || m.getNickname().isEmpty()) {
                v.comment_user.setText(m.getName_zh());
            } else {
                v.comment_user.setText(m.getNickname());
            }

            v.comment_publish_time.setText(m.getPublish_time());
            v.comment_thumbs_up.setText(String.valueOf(m.getThumbs_up()));
            v.rating_bar.setRating(m.getRank().floatValue());
            v.thumbs_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
            v.comment_thumbs_down.setText(String.valueOf(m.getThumbs_down()));
            v.thumbs_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
            v.relative_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (items.isEmpty())
            return 1;
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.isEmpty() ? None : NotNone;
    }

    public void insertItem(int index, ModelCourseComment people) {
        items.add(index, people);
        notifyItemInserted(index);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ModelCourseComment obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        TextView comment_content;
        TextView comment_user;
        TextView comment_publish_time;
        TextView comment_thumbs_up;
        TextView comment_thumbs_down;
        ImageButton thumbs_up;
        ImageButton thumbs_down;
        View relative_parent;

        RatingBar rating_bar;


        public OriginalViewHolder(View v) {
            super(v);
            comment_content = (TextView) v.findViewById(R.id.comment_content);
            comment_user = (TextView) v.findViewById(R.id.comment_user);
            comment_publish_time = (TextView) v.findViewById(R.id.comment_publish_time);
            comment_thumbs_up = (TextView) v.findViewById(R.id.comment_thumbs_up);
            comment_thumbs_down = (TextView) v.findViewById(R.id.comment_thumbs_down);
            thumbs_up = (ImageButton) v.findViewById(R.id.thumbs_up);
            thumbs_down = (ImageButton) v.findViewById(R.id.thumbs_down);
            relative_parent = (View) v.findViewById(R.id.relative_parent);
            rating_bar = (RatingBar) v.findViewById(R.id.rating_bar);
        }
    }
}