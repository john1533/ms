package com.mobcent.lowest.android.ui.widget.scaleview;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

public class ImagePreviewHelper {
    private static ImagePreviewHelper helper;
    private ImageViewerListener listener;
    private ImageViewerSizeListener onViewSizeListener;

    public static abstract class ImageViewerListener {
        public void onViewerPageSelect(int position) {
        }

        public boolean downPermssion() {
            return true;
        }

        public void sharePic(Context context, RichImageModel imgModel, String localPath) {
        }
    }

    public interface ImageViewerSizeListener {
        void onViewerSizeListener(List<RichImageModel> list);
    }

    public static ImagePreviewHelper getInstance() {
        if (helper == null) {
            helper = new ImagePreviewHelper();
        }
        return helper;
    }

    public void startImagePreview(Context context, ArrayList<RichImageModel> imgList, String clickUrl, ImageViewerListener listener) {
        startImagePreview(context, imgList, clickUrl, 0, listener);
    }

    public void startImagePreview(Context context, ArrayList<RichImageModel> imgList, String clickUrl, int adPosition, ImageViewerListener listener) {
        setListener(listener);
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra("richImageList", imgList);
        intent.putExtra(ImagePreviewActivity.INTENT_CURRENT_URL, clickUrl);
        if (adPosition != 0) {
            intent.putExtra("adPosition", adPosition);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    public ImageViewerListener getListener() {
        if (this.listener == null) {
            this.listener = new ImageViewerListener() {
            };
        }
        return this.listener;
    }

    public void setListener(ImageViewerListener listener) {
        this.listener = listener;
    }

    public ImageViewerSizeListener getOnViewSizeListener() {
        return this.onViewSizeListener;
    }

    public void setOnViewSizeListener(ImageViewerSizeListener onViewSizeListener) {
        this.onViewSizeListener = onViewSizeListener;
    }
}
