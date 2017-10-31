package com.mobcent.lowest.android.ui.widget.scaleview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.lowest.android.ui.module.plaza.fragment.BaseFragment;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.android.ui.widget.gif.GifCache;
import com.mobcent.lowest.android.ui.widget.gif.GifCache.GifCallback;
import com.mobcent.lowest.android.ui.widget.gif.GifView;
import com.mobcent.lowest.android.ui.widget.gif.GifView.GifImageType;
import com.mobcent.lowest.android.ui.widget.gif.GifView.GifViewListener;
import com.mobcent.lowest.android.ui.widget.scaleview.ScaleView.ScaleViewListener;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImagePreviewFragment extends BaseFragment {
    private List<AsyncTask> asyncTaskList;
    private GifView gifImageview;
    private List<GifView> gifViewList;
    private String imageCacheKey = "imageviewer";
    private RichImageModel imageModel = null;
    private String imageUrl = null;
    private ImageView imageView;
    private ImageViewerFragmentSelectedListener imageViewerFragmentSelectedListener;
    private RelativeLayout imageviewerFragmentPagerItem;
    private ImageView pointLoadingFail;
    private MCProgressBar pointLoadingProgress;
    private ScaleView scaleView;

    public interface ImageViewerFragmentSelectedListener {
        ViewPager getViewPager();

        void onSelected();

        void onSingleTap();
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public RichImageModel getImageModel() {
        return this.imageModel;
    }

    public void setImageModel(RichImageModel imageModel) {
        this.imageModel = imageModel;
        this.imageUrl = imageModel.getImageUrl();
        this.imageCacheKey += this.imageUrl;
    }

    public static ImagePreviewFragment newInstance() {
        return new ImagePreviewFragment();
    }

    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            this.imageViewerFragmentSelectedListener = (ImageViewerFragmentSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnArticleSelectedListener");
        }
    }

    protected void initData() {
        this.asyncTaskList = new ArrayList();
        this.gifViewList = new ArrayList();
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.imageviewerFragmentPagerItem = (RelativeLayout) inflater.inflate(this.mcResource.getLayoutId("mc_forum_img_preview_fragment_pager_item"), container, false);
        this.scaleView = (ScaleView) this.imageviewerFragmentPagerItem.findViewById(this.mcResource.getViewId("mc_forum_point_imageview"));
        this.gifImageview = (GifView) this.imageviewerFragmentPagerItem.findViewById(this.mcResource.getViewId("mc_forum_gif_imageview"));
        this.pointLoadingProgress = (MCProgressBar) this.imageviewerFragmentPagerItem.findViewById(this.mcResource.getViewId("mc_forum_download_progress_bar"));
        this.pointLoadingFail = (ImageView) this.imageviewerFragmentPagerItem.findViewById(this.mcResource.getViewId("mc_forum_point_loading_fail"));
        ViewPager pager = this.imageViewerFragmentSelectedListener.getViewPager();
        int width = pager.getMeasuredWidth();
        int height = pager.getMeasuredHeight();
        if (width == 0 || height == 0) {
            width = MCPhoneUtil.getDisplayWidth(getActivity());
            height = MCPhoneUtil.getDisplayHeight(getActivity());
        }
        this.scaleView.setViewPager((ScaleViewPager) this.imageViewerFragmentSelectedListener.getViewPager());
        this.scaleView.setScaleViewWidth((float) width);
        this.scaleView.setScaleViewHeight((float) height);
        return this.imageviewerFragmentPagerItem;
    }

    protected void initWidgetActions() {
        this.scaleView.setOnScaleViewListener(new ScaleViewListener() {
            public void onSingleTapConfirmed() {
                if (ImagePreviewFragment.this.getActivity() != null && ImagePreviewFragment.this.isVisible()) {
                    ImagePreviewFragment.this.imageViewerFragmentSelectedListener.onSingleTap();
                }
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.pointLoadingProgress.show();
        if (this.imageUrl != null) {
            if (FinalConstant.PIC_GIF.equals(MCStringUtil.getExtensionName(this.imageUrl).toLowerCase())) {
                this.scaleView.setVisibility(8);
                this.gifImageview.setVisibility(0);
                GifCache inputStreamCache = new GifCache(getActivity(), this.imageUrl, new GifCallback() {
                    public void onGifLoaded(InputStream is, String url) {
                        if (is != null) {
                            ImagePreviewFragment.this.pointLoadingProgress.stop();
                            ImagePreviewFragment.this.pointLoadingProgress.setVisibility(8);
                            ImagePreviewFragment.this.gifImageview.setGifViewListener(new GifViewListener() {
                                public void onSingleTap(MotionEvent e) {
                                    ImagePreviewFragment.this.imageViewerFragmentSelectedListener.onSingleTap();
                                }

                                public void done(final boolean isFail) {
                                    ImagePreviewFragment.this.mHandler.post(new Runnable() {
                                        public void run() {
                                            if (isFail) {
                                                ImagePreviewFragment.this.pointLoadingFail.setVisibility(0);
                                            } else {
                                                ImagePreviewFragment.this.pointLoadingFail.setVisibility(8);
                                            }
                                        }
                                    });
                                }
                            });
                            ImagePreviewFragment.this.gifImageview.setVisibility(0);
                            ImagePreviewFragment.this.setImageView(ImagePreviewFragment.this.gifImageview);
                            ImagePreviewFragment.this.gifImageview.setGifImageType(GifImageType.SYNC_DECODER);
                            ImagePreviewFragment.this.gifImageview.setGifImage(is);
                            ImagePreviewFragment.this.gifViewList.add(ImagePreviewFragment.this.gifImageview);
                        }
                    }
                });
                this.asyncTaskList.add(inputStreamCache);
                inputStreamCache.execute(new Void[0]);
            } else {
                this.scaleView.setVisibility(0);
                this.gifImageview.setVisibility(8);
                setImageView(this.scaleView);
                loadImage(this.scaleView, this.imageCacheKey, this.imageUrl);
            }
            this.imageViewerFragmentSelectedListener.onSelected();
        }
    }

    public void loadImage(final ScaleView imageView, String key, String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, (ImageView) imageView, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ImagePreviewFragment.this.pointLoadingProgress.hide();
                imageView.setImageBitmap(loadedImage);
            }

            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ImagePreviewFragment.this.pointLoadingProgress.hide();
                ImagePreviewFragment.this.pointLoadingFail.setVisibility(0);
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (!(this.asyncTaskList == null || this.asyncTaskList.isEmpty())) {
            for (AsyncTask asyncTask : this.asyncTaskList) {
                asyncTask.cancel(true);
            }
        }
        if (!(this.gifViewList == null || this.gifViewList.isEmpty())) {
            for (int i = 0; i < this.gifViewList.size(); i++) {
                ((GifView) this.gifViewList.get(i)).free();
            }
        }
        this.gifViewList.clear();
        this.scaleView.resetView();
        this.scaleView.setImageBitmap(null);
    }
}
