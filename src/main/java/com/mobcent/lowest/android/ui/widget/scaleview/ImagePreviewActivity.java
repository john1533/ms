package com.mobcent.lowest.android.ui.widget.scaleview;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.lowest.android.ui.widget.gif.GifCache;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewFragment.ImageViewerFragmentSelectedListener;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerSizeListener;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePreviewActivity extends FragmentActivity implements ImageViewerFragmentSelectedListener {
    public static final String INTENT_ADPOSITION = "adPosition";
    public static final String INTENT_CURRENT_URL = "currentUrl";
    public static final String INTENT_IMAGE_LIST = "richImageList";
    private TextView countText;
    private RichImageModel currentImageModel = null;
    private String currentImageUrl = null;
    protected int currentPosition = 0;
    private AsyncTaskDownloadImage downTask = null;
    private int imageIndex;
    private List<RichImageModel> imageUrlList;
    private Button imageViewerDownload;
    private RelativeLayout imageViewerTopbar;
    private Button imgShare;
    private ScaleViewPager mPager;
    private ImagePreviewFragmentAdapter pagerAdapter;
    private MCResource resource;
    private ImageViewerSizeListener viewSizeListener = new ImageViewerSizeListener() {
        public void onViewerSizeListener(List<RichImageModel> richImageModels) {
            ImagePreviewActivity.this.imageUrlList.clear();
            ImagePreviewActivity.this.imageUrlList.addAll(richImageModels);
            ImagePreviewActivity.this.pagerAdapter.setImageUrlList(ImagePreviewActivity.this.imageUrlList);
            ImagePreviewActivity.this.pagerAdapter.notifyDataSetChanged();
        }
    };

    private class AsyncTaskDownloadImage extends AsyncTask<Void, Void, Boolean> {
        private String savePath;

        private AsyncTaskDownloadImage() {
            this.savePath = null;
        }

        protected Boolean doInBackground(Void... params) {
            File file;
            String fileName = MCImageUtil.getPathName(ImagePreviewActivity.this.currentImageUrl);
            if (FinalConstant.PIC_GIF.equals(MCStringUtil.getExtensionName(fileName).toLowerCase())) {
                file = new File(MCLibIOUtil.getImageCachePathByApp(ImagePreviewActivity.this.getApplicationContext()) + GifCache.getHash(ImagePreviewActivity.this.currentImageUrl));
            } else {
                file = ImageLoader.getInstance().getDiskCache().get(ImagePreviewActivity.this.currentImageUrl);
            }
            if (file == null || !file.exists()) {
                return Boolean.valueOf(false);
            }
            if (FinalConstant.PIC_GIF.equals(MCStringUtil.getExtensionName(fileName).toLowerCase())) {
                this.savePath = MCLibIOUtil.addToSysGallery(ImagePreviewActivity.this.getBaseContext(), file.getPath(), fileName + ".gif");
            } else {
                this.savePath = MCLibIOUtil.addToSysGallery(ImagePreviewActivity.this.getBaseContext(), file.getPath(), fileName + ".jpg");
            }
            return Boolean.valueOf(this.savePath != null);
        }

        protected void onPostExecute(Boolean isSaveSucc) {
            if (isSaveSucc.booleanValue()) {
                MCToastUtils.toastByResName(ImagePreviewActivity.this.getApplicationContext(), "mc_forum_add_to_sys_gallery_succ");
                if (this.savePath != null) {
                    MCLibIOUtil.sendDownSucc(ImagePreviewActivity.this.getBaseContext(), MCLibIOUtil.getFilePathByContentResolver(ImagePreviewActivity.this.getBaseContext(), Uri.parse(this.savePath)));
                    return;
                }
                return;
            }
            MCToastUtils.toastByResName(ImagePreviewActivity.this.getApplicationContext(), "mc_forum_add_to_sys_gallery_fail");
        }
    }

    private class ViewPageChangeListener implements OnPageChangeListener {
        private ViewPageChangeListener() {
        }

        public void onPageScrollStateChanged(int status) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            ImagePreviewActivity.this.currentPosition = position;
            ImagePreviewFragment currentImageViewerFragment = ImagePreviewActivity.this.getCurrentFragment();
            if (currentImageViewerFragment != null) {
                ImagePreviewActivity.this.currentImageUrl = currentImageViewerFragment.getImageUrl();
                ImagePreviewActivity.this.currentImageModel = currentImageViewerFragment.getImageModel();
            }
            if (ImagePreviewHelper.getInstance().getListener() != null) {
                ImagePreviewHelper.getInstance().getListener().onViewerPageSelect(ImagePreviewActivity.this.currentPosition);
            }
            ImagePreviewActivity.this.resetScaleView(ImagePreviewActivity.this.currentPosition - 1);
            ImagePreviewActivity.this.resetScaleView(ImagePreviewActivity.this.currentPosition + 1);
            ImagePreviewActivity.this.countText.setText((ImagePreviewActivity.this.currentPosition + 1) + "/" + ImagePreviewActivity.this.imageUrlList.size());
        }
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.resource = MCResource.getInstance(this);
        initData();
        initViews();
        initWidgetActions();
    }

    protected void initData() {
        overridePendingTransition(this.resource.getAnimId("mc_forum_fade_in"), this.resource.getAnimId("mc_forum_fade_out"));
        Intent intent = getIntent();
        if (intent != null) {
            this.imageUrlList = (ArrayList) intent.getSerializableExtra("richImageList");
            this.imageIndex = getImageIndex(intent.getStringExtra(INTENT_CURRENT_URL));
            this.currentPosition = this.imageIndex;
        }
    }

    private int getImageIndex(String currentUrl) {
        if (!(MCListUtils.isEmpty(this.imageUrlList) || TextUtils.isEmpty(currentUrl))) {
            int count = this.imageUrlList.size();
            for (int i = 0; i < count; i++) {
                if (((RichImageModel) this.imageUrlList.get(i)).getImageUrl().equals(currentUrl)) {
                    return i;
                }
            }
        }
        return 0;
    }

    protected void initViews() {
        setContentView(this.resource.getLayoutId("mc_forum_img_preview_fragment_pager"));
        this.pagerAdapter = new ImagePreviewFragmentAdapter(getSupportFragmentManager());
        this.pagerAdapter.setImageUrlList(this.imageUrlList);
        this.mPager = (ScaleViewPager) findViewById(this.resource.getViewId("mc_forum_imageviewer_pager"));
        this.mPager.setAdapter(this.pagerAdapter);
        this.mPager.setCurrentItem(this.imageIndex);
        this.mPager.setOnPageChangeListener(new ViewPageChangeListener());
        this.mPager.setPageMargin((int) getResources().getDimension(this.resource.getDimenId("mc_forum_image_detail_pager_margin")));
        this.imageViewerTopbar = (RelativeLayout) findViewById(this.resource.getViewId("mc_forum_imageviewer_bar"));
        this.imageViewerDownload = (Button) findViewById(this.resource.getViewId("mc_forum_img_download"));
        this.imgShare = (Button) findViewById(this.resource.getViewId("mc_forum_img_share"));
        this.countText = (TextView) findViewById(this.resource.getViewId("mc_forum_image_counts_text"));
    }

    protected void initWidgetActions() {
        this.imageViewerDownload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!ImagePreviewHelper.getInstance().getListener().downPermssion()) {
                    MCToastUtils.toastByResName(ImagePreviewActivity.this.getApplicationContext(), "mc_forum_error_permission_download");
                } else if (ImagePreviewActivity.this.currentImageUrl != null && !"".equals(ImagePreviewActivity.this.currentImageUrl)) {
                    if (ImagePreviewActivity.this.downTask == null || ImagePreviewActivity.this.downTask.isCancelled()) {
                        if (ImagePreviewActivity.this.downTask == null) {
                            ImagePreviewActivity.this.downTask = new AsyncTaskDownloadImage();
                        }
                        ImagePreviewActivity.this.downTask.execute(new Void[0]);
                        MCToastUtils.toastByResName(ImagePreviewActivity.this.getApplicationContext(), "mc_forum_downloading");
                    }
                }
            }
        });
        this.imgShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImagePreviewActivity.this.share();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        ImagePreviewHelper.getInstance().setOnViewSizeListener(this.viewSizeListener);
    }

    protected void onStop() {
        super.onStop();
        ImagePreviewHelper.getInstance().setOnViewSizeListener(null);
    }

    private void share() {
        String imagePath = MCLibIOUtil.getImageCachePath(getApplicationContext()) + MCAsyncTaskLoaderImage.getHash(this.currentImageUrl);
        if (!MCStringUtil.isEmpty(this.currentImageUrl)) {
            ImagePreviewHelper.getInstance().getListener().sharePic(this, this.currentImageModel, imagePath);
        }
    }

    private ImagePreviewFragment getCurrentFragment() {
        ImagePreviewFragment insObject = (ImagePreviewFragment)this.pagerAdapter.instantiateItem(this.mPager, this.mPager.getCurrentItem());
        if (insObject instanceof ImagePreviewFragment) {
            return insObject;
        }
        return null;
    }

    protected void onDestroy() {
        super.onDestroy();
        ImagePreviewHelper.getInstance().setListener(null);
    }

    public ViewPager getViewPager() {
        return this.mPager;
    }

    public void onSingleTap() {
        onBackPressed();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(17432576, 17432577);
    }

    public void onSelected() {
        if (this.currentPosition == this.imageIndex) {
            ImagePreviewFragment imageViewerFragment = this.pagerAdapter.getItem(this.currentPosition);
            this.currentImageUrl = imageViewerFragment.getImageUrl();
            this.currentImageModel = imageViewerFragment.getImageModel();
        }
        this.countText.setText((this.currentPosition + 1) + "/" + this.imageUrlList.size());
    }

    private void resetScaleView(int position) {
        ImagePreviewFragment leftFragment = this.pagerAdapter.getItem(position);
        if (leftFragment != null && (leftFragment.getImageView() instanceof ScaleView)) {
            ((ScaleView) leftFragment.getImageView()).resetView();
        }
    }
}
