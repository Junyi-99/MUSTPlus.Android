package com.example.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

import com.example.myapplication.models.ModelCourseComment;

import java.util.List;

public class AdapterCourseComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 两种显示模式
    // None: 显示空评论之提示layout
    // NotNone: 正常评论内容layout
    private final int None = 0;
    private final int NotNone = 1;

    private List<ModelCourseComment> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ModelCourseComment obj, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case None:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment_none, parent, false);
                return new OriginalViewHolder(v);
            case NotNone:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment, parent, false);
                return new OriginalViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_comment_none, parent, false);
                return new OriginalViewHolder(v);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCourseComment(Context context, List<ModelCourseComment> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView comment_content;
        public TextView comment_user;
        public TextView comment_publish_time;
        public TextView comment_thumbs_up;
        public TextView comment_thumbs_down;
        public ImageButton thumbs_up;
        public ImageButton thumbs_down;
        public View relative_parent;

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
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {

            ModelCourseComment m = items.get(position);
            OriginalViewHolder v = (OriginalViewHolder) holder;

            v.comment_content.setText(m.getContent());
            v.comment_user.setText(m.getStudent_id());
            v.comment_publish_time.setText(m.getPublish_time());
            v.comment_thumbs_up.setText(String.valueOf(m.getThumbs_up()));
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
}