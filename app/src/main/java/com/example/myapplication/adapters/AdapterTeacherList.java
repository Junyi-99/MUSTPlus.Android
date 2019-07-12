package com.example.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import com.example.myapplication.model.ModelTeacher;
import com.example.myapplication.utils.Tools;


import java.util.List;

public class AdapterTeacherList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ModelTeacher> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ModelTeacher obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterTeacherList(Context context, List<ModelTeacher> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView teacher_avatar;
        public TextView teacher_name_zh;
        public TextView teacher_name_en;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            teacher_avatar = (ImageView) v.findViewById(R.id.teacher_avatar);
            teacher_name_zh = (TextView) v.findViewById(R.id.teacher_name_zh); // item people chat
            teacher_name_en = (TextView) v.findViewById(R.id.teacher_name_en);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new OriginalViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ModelTeacher t = items.get(position);


        OriginalViewHolder view = (OriginalViewHolder) holder;

        view.teacher_name_zh.setText(t.getName_zh());
        view.teacher_name_en.setText(t.getName_en());
        Tools.displayImageRound(ctx, view.teacher_avatar, t.getAvatar_img());

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

    public void insertItem(int index, ModelTeacher people) {
        items.add(index, people);
        notifyItemInserted(index);
    }

}