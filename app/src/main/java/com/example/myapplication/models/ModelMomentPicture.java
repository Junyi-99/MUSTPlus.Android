package com.example.myapplication.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

public class ModelMomentPicture {
    private String img_url;
    private String hash;
    private Bitmap img_bitmap;
    private Context context;

    public ModelMomentPicture() {
    }

    public void download() {
        // TODO: 实现下载图片功能
    }

    // 构造函数（Context：必须，img_url：图片地址必须，hash：图片哈希为了本地化防止频繁访问服务器）
    public ModelMomentPicture(Context context, String img_url, String hash) {
        this.context = context;
        this.img_url = img_url;
        this.hash = hash;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Bitmap getImg_bitmap() {
        // 先在本地hash里找找，如果有直接用本地的，否则从网络下载并存到本地

        // TODO: Remove this
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_13);
        return bm;
        // TODO: Remove this
        
        //return img_bitmap;
    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        this.img_bitmap = img_bitmap;
    }
}
