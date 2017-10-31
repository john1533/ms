package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.module.custom.widget.CustomBaseImg;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.List;

public class MomentsView extends RelativeLayout implements CustomStateDelegate {
    private Context context;
    private int horizontalSpacing;
    private int itemWidth;
    private ArrayList<RichImageModel> richImageModelList;
    private int verticalSpacing;
    private int width;

    public MomentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(int width, int horizontalSpacing, int verticalSpacing) {
        this.width = width;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    public void updateViews(List<String> urlList) {
        removeAllViews();
        if (MCListUtils.isEmpty((List) urlList)) {
            setVisibility(8);
            return;
        }
        this.richImageModelList = new ArrayList();
        for (int i = 0; i < urlList.size(); i++) {
            RichImageModel model = new RichImageModel();
            model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl((String) urlList.get(i), FinalConstant.RESOLUTION_BIG));
            this.richImageModelList.add(model);
        }
        this.itemWidth = (this.width - (this.horizontalSpacing * 2)) / 3;
        setLayoutParams(getResetLayoutParams(urlList.size()));
        int size = urlList.size();
        if (size == 1) {
            addView(createView(MCPhoneUtil.dip2px(this.context, 150.0f), MCPhoneUtil.dip2px(this.context, 150.0f), 0, 0, 0, 0, (String) urlList.get(0)));
        } else if (size == 2) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
        } else if (size == 3) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
        } else if (size == 4) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(3)));
        } else if (size == 5) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(3)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(4)));
        } else if (size == 6) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(3)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(4)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(5)));
        } else if (size == 7) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(3)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(4)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(5)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, 0, 0, (String) urlList.get(6)));
        } else if (size == 8) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(3)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(4)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(5)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, 0, 0, (String) urlList.get(6)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(7)));
        } else if (size == 9) {
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, 0, 0, (String) urlList.get(0)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(1)));
            addView(createView(this.itemWidth, this.itemWidth, 0, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(2)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, 0, 0, (String) urlList.get(3)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(4)));
            addView(createView(this.itemWidth, this.itemWidth, this.verticalSpacing + this.itemWidth, 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(5)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, 0, 0, (String) urlList.get(6)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, this.horizontalSpacing + this.itemWidth, 0, (String) urlList.get(7)));
            addView(createView(this.itemWidth, this.itemWidth, (this.verticalSpacing * 2) + (this.itemWidth * 2), 0, (this.horizontalSpacing * 2) + (this.itemWidth * 2), 0, (String) urlList.get(8)));
        }
    }

    private ImageView createView(int width, int height, int marginTop, int marginBottom, int marginLeft, int marginRight, final String imgUrl) {
        final String url = MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL);
        CustomBaseImg imageView = new CustomBaseImg(this.context);
        imageView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        LayoutParams lParams = new LayoutParams(width, height);
        lParams.addRule(9);
        lParams.topMargin = marginTop;
        lParams.bottomMargin = marginBottom;
        lParams.leftMargin = marginLeft;
        lParams.rightMargin = marginRight;
        imageView.setLayoutParams(lParams);
        imageView.setTag(url);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImagePreviewHelper.getInstance().startImagePreview(MomentsView.this.context, MomentsView.this.richImageModelList, MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_BIG), new ImageViewerListener() {
                    public void onViewerPageSelect(int position) {
                        super.onViewerPageSelect(position);
                    }

                    public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                        MCShareModel shareModel = new MCShareModel();
                        shareModel.setPicUrl(url);
                        shareModel.setImageFilePath("");
                        shareModel.setDownloadUrl(MCResource.getInstance(context.getApplicationContext()).getString("mc_share_download_url"));
                        shareModel.setType(1);
                        MCForumLaunchShareHelper.share(context, shareModel);
                    }
                });
            }
        });
        loadImg(imageView, url);
        return imageView;
    }

    public ViewGroup.LayoutParams getResetLayoutParams(int size) {
        ViewGroup.LayoutParams lps = getLayoutParams();
        if (lps == null) {
            lps = new ViewGroup.LayoutParams(-1, 0);
        }
        if (size == 1) {
            lps.height = MCPhoneUtil.dip2px(this.context, 150.0f);
        } else if (size == 2 || size == 3) {
            lps.height = this.itemWidth;
        } else if (size == 4 || size == 5 || size == 6) {
            lps.height = (this.itemWidth * 2) + this.horizontalSpacing;
        } else {
            lps.height = (this.itemWidth * 3) + (this.horizontalSpacing * 2);
        }
        return lps;
    }

    public void onResume() {
        dealAllItem(true);
    }

    public void onPause() {
        dealAllItem(false);
    }

    private void dealAllItem(boolean isResume) {
        int momentsCount = getChildCount();
        for (int i = 0; i < momentsCount; i++) {
            if (getChildAt(i) instanceof CustomBaseImg) {
                if (isResume) {
                    loadImg((CustomBaseImg) getChildAt(i), (String) ((CustomBaseImg) getChildAt(i)).getTag());
                } else {
                    ((CustomBaseImg) getChildAt(i)).setImageBitmap(null);
                }
            }
        }
    }

    public void loadImg(CustomBaseImg imgView, String imgUrl) {
        ImageLoader.getInstance().displayImage(imgUrl, (ImageView) imgView, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
        });
    }
}
