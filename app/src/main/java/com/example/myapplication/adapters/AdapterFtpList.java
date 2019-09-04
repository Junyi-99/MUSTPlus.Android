package com.example.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.ModelFtp;

import java.util.List;

/**
 * @author Junyi
 */
public class AdapterFtpList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int None = 0;
    private final int NotNone = 1;

    private List<ModelFtp> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case None:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ftp_none, parent, false);
                return new OriginalViewHolder(v);
            case NotNone:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ftp, parent, false);
                return new OriginalViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ftp_none, parent, false);
                return new OriginalViewHolder(v);
        }
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView commentContent;
        public TextView commentUser;
        public TextView commentPublishTime;
        public TextView commentThumbsUp;
        public TextView commentThumbsDown;
        public ImageButton thumbsUp;
        public ImageButton thumbsDown;
        public View relativeParent;

        public ImageView imageViewStar1;
        public ImageView imageViewStar2;
        public ImageView imageViewStar3;
        public ImageView imageViewStar4;
        public ImageView imageViewStar5;


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

        }
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder .
     * @param position .
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {

            ModelFtp m = items.get(position);
            OriginalViewHolder v = (OriginalViewHolder) holder;


            v.thumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });

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

    public void insertItem(int index, ModelFtp people) {
        items.add(index, people);
        notifyItemInserted(index);
    }

    public interface OnItemClickListener {
        /**
         * 当 item 被 click
         *
         * @param view     父View
         * @param obj      被点击的对象
         * @param position 位置
         */
        void onItemClick(View view, ModelFtp obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterFtpList(Context context, List<ModelFtp> items) {
        this.items = items;
        this.ctx = context;
    }
}