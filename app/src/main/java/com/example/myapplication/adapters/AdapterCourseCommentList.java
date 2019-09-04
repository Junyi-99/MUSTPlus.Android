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

/**
 * @author Junyi
 */
public class AdapterCourseCommentList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 显示空评论之提示Layout
     */
    private final int None = 0;
    /**
     * 正常评论内容layout
     */
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

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder   。
     * @param position 。
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {

            ModelCourseComment m = items.get(position);
            OriginalViewHolder v = (OriginalViewHolder) holder;

            v.commentContent.setText(m.getContent());

            // 有昵称显示昵称，没有昵称显示姓名
            if (m.getNickname() == null || m.getNickname().isEmpty()) {
                v.commentUser.setText(m.getName_zh());
            } else {
                v.commentUser.setText(m.getNickname());
            }

            v.commentPublishTime.setText(m.getPublish_time());
            v.commentThumbsUp.setText(String.valueOf(m.getThumbs_up()));
            v.ratingBar.setRating(m.getRank().floatValue());
            v.thumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
            v.commentThumbsDown.setText(String.valueOf(m.getThumbs_down()));
            v.thumbsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
            v.relativeParent.setOnClickListener(new View.OnClickListener() {
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
        if (items.isEmpty()) {
            return 1;
        }
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
        /**
         * 当 item 被 click
         *
         * @param view     父View
         * @param obj      被点击的对象
         * @param position 位置
         */
        void onItemClick(View view, ModelCourseComment obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        TextView commentContent;
        TextView commentUser;
        TextView commentPublishTime;
        TextView commentThumbsUp;
        TextView commentThumbsDown;
        ImageButton thumbsUp;
        ImageButton thumbsDown;
        View relativeParent;

        RatingBar ratingBar;


        public OriginalViewHolder(View v) {
            super(v);
            commentContent = (TextView) v.findViewById(R.id.comment_content);
            commentUser = (TextView) v.findViewById(R.id.comment_user);
            commentPublishTime = (TextView) v.findViewById(R.id.comment_publish_time);
            commentThumbsUp = (TextView) v.findViewById(R.id.comment_thumbs_up);
            commentThumbsDown = (TextView) v.findViewById(R.id.comment_thumbs_down);
            thumbsUp = (ImageButton) v.findViewById(R.id.thumbs_up);
            thumbsDown = (ImageButton) v.findViewById(R.id.thumbs_down);
            relativeParent = (View) v.findViewById(R.id.relative_parent);
            ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        }
    }
}