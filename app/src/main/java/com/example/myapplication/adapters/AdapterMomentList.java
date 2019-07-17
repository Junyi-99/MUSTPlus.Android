package com.example.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.ModelMoment;
import com.example.myapplication.models.ModelMoment;
import com.example.myapplication.models.ModelMomentPicture;
import com.example.myapplication.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class AdapterMomentList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 两种显示模式
    // None: 显示空评论之提示layout
    // NotNone: 正常评论内容layout
    private final int None = 0;
    private final int NotNone = 1;

    private List<ModelMoment> items;
    private Context ctx;
    private AdapterMomentList.OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case None:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_none, parent, false);
                return new AdapterMomentList.OriginalViewHolder(v);
            case NotNone:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment, parent, false);
                return new AdapterMomentList.OriginalViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_none, parent, false);
                return new AdapterMomentList.OriginalViewHolder(v);
        }
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_view_avatar;
        public TextView text_view_nickname;
        public TextView text_view_publish_time;
        public TextView text_view_content;
        public RecyclerView recycler_view_pics;


        public OriginalViewHolder(View v) {
            super(v);
            image_view_avatar = (ImageView) v.findViewById(R.id.image_view_avatar);
            text_view_nickname = (TextView) v.findViewById(R.id.text_view_nickname);
            text_view_publish_time = (TextView) v.findViewById(R.id.text_view_publish_time);
            text_view_content = (TextView) v.findViewById(R.id.text_view_content);
            recycler_view_pics = (RecyclerView) v.findViewById(R.id.recycler_view_pics);
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {


            ModelMoment m = items.get(position);
            AdapterMomentList.OriginalViewHolder v = (AdapterMomentList.OriginalViewHolder) holder;

            //v.image_view_avatar.setImageResource();
            v.text_view_nickname.setText(m.getNickname());
            v.text_view_content.setText(m.getContent());
            v.text_view_publish_time.setText(m.getPublish_time());
            Tools.displayImageRound(ctx, v.image_view_avatar, m.getAvatar_img());
            ArrayList<ModelMomentPicture> modelMomentPictureArrayList = new ArrayList<ModelMomentPicture>();


            ModelMomentPicture modelMomentPicture = new ModelMomentPicture(
                    ctx,
                    "http://www.baidu.com",
                    "hash"
            );
            // TODO: 动态添加数据
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);
            modelMomentPictureArrayList.add(modelMomentPicture);

            AdapterMomentPicsList adapterMomentPicsList = new AdapterMomentPicsList(
                    ctx,
                    modelMomentPictureArrayList
            );
            v.recycler_view_pics.setLayoutManager(new GridLayoutManager(ctx, 3));
            v.recycler_view_pics.setHasFixedSize(true);
            v.recycler_view_pics.setNestedScrollingEnabled(false);
            v.recycler_view_pics.setAdapter(adapterMomentPicsList);
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

    public void insertItem(int index, ModelMoment people) {
        items.add(index, people);
        notifyItemInserted(index);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ModelMoment obj, int position);
    }

    public void setOnItemClickListener(final AdapterMomentList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterMomentList(Context context, List<ModelMoment> items) {
        this.items = items;
        this.ctx = context;
    }
}
