package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.models.ModelMomentPicture;

import java.util.List;

public class AdapterMomentPicsList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 两种显示模式
    // None: 显示空评论之提示layout
    // NotNone: 正常评论内容layout
    private final int None = 0;
    private final int NotNone = 1;

    private List<ModelMomentPicture> items;
    private Context ctx;
    private AdapterMomentList.OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case None:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_pic_none, parent, false);
                return new AdapterMomentPicsList.OriginalViewHolder(v);
            case NotNone:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_pic, parent, false);
                return new AdapterMomentPicsList.OriginalViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment_pic_none, parent, false);
                return new AdapterMomentPicsList.OriginalViewHolder(v);
        }
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_view_moment_pic;

        public OriginalViewHolder(View v) {
            super(v);
            image_view_moment_pic = (ImageView) v.findViewById(R.id.image_view_moment_pic);
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!items.isEmpty()) {
            ModelMomentPicture m = items.get(position);
            AdapterMomentPicsList.OriginalViewHolder v = (AdapterMomentPicsList.OriginalViewHolder) holder;
            Bitmap bitmap = m.getImg_bitmap();




            float cornerRadius = 50.0f;
            // Initialize a new RoundedBitmapDrawable object to make ImageView rounded corners
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    ctx.getResources(),
                    bitmap
            );

            // Set the RoundedBitmapDrawable corners radius
            roundedBitmapDrawable.setCornerRadius(cornerRadius);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
            roundedBitmapDrawable.setAntiAlias(true);

            // Set the ImageView image as drawable object
            v.image_view_moment_pic.setImageDrawable(roundedBitmapDrawable);
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

    public void insertItem(int index, ModelMomentPicture picture) {
        items.add(index, picture);
        notifyItemInserted(index);
    }

    public interface OnItemClickListener {
        /**
         * 当 item 被 click
         * @param view 父View
         * @param obj 被点击的对象
         */
        void onItemClick(View view, ModelMomentPicture obj, int position);
    }

    public void setOnItemClickListener(final AdapterMomentList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterMomentPicsList(Context context, List<ModelMomentPicture> items) {
        this.items = items;
        this.ctx = context;
    }
}
