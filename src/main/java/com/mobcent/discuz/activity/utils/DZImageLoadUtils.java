package com.mobcent.discuz.activity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.BitmapImageCallback;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DZImageLoadUtils {
    private static Handler handler = new Handler();

    public static void loadImage(View img, String url) {
        loadImage(img, url, null);
    }

    public static void loadImage(final View img, String url, BitmapImageCallback delegate) {
        if (!TextUtils.isEmpty(url)) {
            final Context context = img.getContext().getApplicationContext();
            MCResource resource = MCResource.getInstance(context);
            if (url.startsWith("http")) {
                img.setTag(url);
                ImageLoader.getInstance().loadImage(url, new ImageSize(img.getWidth(), img.getHeight()), new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (img instanceof ImageView) {
                            ((ImageView) img).setImageBitmap(loadedImage);
                        } else if (img instanceof MCHeadIcon) {
                            ((MCHeadIcon) img).setImageBitmap(loadedImage);
                        } else {
                            img.setBackgroundDrawable(new BitmapDrawable(context.getResources(), loadedImage));
                        }
                    }
                });
                return;
            }
            img.setBackgroundResource(resource.getDrawableId(url));
        } else if (delegate != null) {
            delegate.onImageLoaded(null, url);
        }
    }

    public static DisplayImageOptions getHeadIconOptions() {
        return new Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(20)).build();
    }
}
