package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CustomBaseImg extends ImageView implements CustomStateDelegate {
    public String TAG;
    private SimpleImageLoadingListener loadingListener;
    private Handler mHandler;
    int width;

    public CustomBaseImg(Context context) {
        this(context, null);
    }

    public CustomBaseImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "CustomBaseImg";
        this.mHandler = new Handler();
        this.width = -1;
        this.loadingListener = new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                FadeInBitmapDisplayer.animate((ImageView) view, 300);
            }
        };
        setScaleType(ScaleType.CENTER_CROP);
    }

    public void loadImg(String imgUrl) {
        imgUrl = dealImgUrl(imgUrl);
        setTag(imgUrl);
        ImageLoader.getInstance().displayImage(imgUrl, (ImageView) this, this.loadingListener);
    }

    public void loadImg(String imgUrl, int width) {
        this.width = width;
        imgUrl = dealImgUrl(imgUrl);
        setTag(imgUrl);
        ImageLoader.getInstance().displayImage(imgUrl, (ImageView) this, getRoundedOptions(width), this.loadingListener);
    }

    public void onResume() {
        if (getTag() != null && (getTag() instanceof String)) {
            if (this.width != -1) {
                loadImg((String) getTag(), this.width);
            } else {
                loadImg((String) getTag());
            }
        }
    }

    public void onPause() {
        setImageBitmap(null);
    }

    public void setImgWidth(int width) {
        this.width = width;
    }

    public void setTag(Object tag) {
        if (tag instanceof String) {
            String url = (String) tag;
            MCAsyncTaskLoaderImage.getInstance(getContext());
            tag = MCAsyncTaskLoaderImage.formatUrl(url, FinalConstant.RESOLUTION_BIG);
        }
        super.setTag(tag);
    }

    public String dealImgUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        MCAsyncTaskLoaderImage.getInstance(getContext());
        return MCAsyncTaskLoaderImage.formatUrl(url, FinalConstant.RESOLUTION_BIG);
    }

    private DisplayImageOptions getRoundedOptions(int radius) {
        return new Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(radius)).build();
    }
}
