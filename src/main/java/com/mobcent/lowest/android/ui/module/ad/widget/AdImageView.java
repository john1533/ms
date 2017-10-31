package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class AdImageView extends BasePicView {
    private String TAG = "AdImageView";

    public AdImageView(Context context) {
        super(context);
    }

    protected void loadPic(String url, final AdProgress progress) {
        if (url != null && !"".equals(url)) {
            if (progress != null) {
                progress.show();
            }
            ImageLoader.getInstance().displayImage(url, (ImageView) this, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (progress != null) {
                        progress.hide();
                    }
                }
            });
        }
    }

    protected void recyclePic() {
        setImageBitmap(null);
    }

    protected void setShowWidthReal(int showWidth) {
    }
}
