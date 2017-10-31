package com.mobcent.discuz.activity.widget.album;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewFragment;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewFragment.ImageViewerFragmentSelectedListener;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewFragmentAdapter;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.android.ui.widget.scaleview.ScaleView;
import com.mobcent.lowest.android.ui.widget.scaleview.ScaleViewPager;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoPreviewActivity extends BaseFragmentActivity implements ImageViewerFragmentSelectedListener {
    public static final String ALBUM_PATH = "albumPath";
    public static final String POSITION = "position";
    public static final String PREVIEW_TYPE = "previewType";
    private static PhotoManageHelper photoManageHelper;
    private final String TAG = "PhotoPreviewActivity";
    private String albumPath;
    private Button backBtn;
    private TextView currNumText;
    private List<PictureModel> currentList;
    private int currentPosition = 0;
    private TextView finishText;
    private List<RichImageModel> imageUrlList;
    private int index = 0;
    private Map<String, PictureModel> innerSelectMap;
    private boolean isCallBackRightNow;
    private TextView numText;
    private ImagePreviewFragmentAdapter pagerAdapter;
    private int previewType = 2;
    private Button selectBtn;
    private int totalSize = 0;
    private ScaleViewPager viewPager;

    protected String getLayoutName() {
        return "photo_preview_activity";
    }

    public static void setManageHelper(PhotoManageHelper helper) {
        photoManageHelper = helper;
    }

    protected void initDatas() {
        super.initDatas();
        this.albumPath = this.resource.getString("mc_forum_photo_album_all");
        Intent intent = getIntent();
        if (intent != null) {
            this.index = intent.getIntExtra("position", -1);
            this.albumPath = intent.getStringExtra(ALBUM_PATH);
            this.previewType = intent.getIntExtra(PREVIEW_TYPE, 2);
            this.isCallBackRightNow = intent.getBooleanExtra(PhotoManageHelper.CALL_BACK_RIGHT_NOW, false);
        }
        this.innerSelectMap = photoManageHelper.getInnerSelectMap();
        this.currentList = new ArrayList();
        if (this.previewType == 2) {
            if (MCStringUtil.isEmpty(this.albumPath)) {
                this.currentList.clear();
                for (String key : this.innerSelectMap.keySet()) {
                    this.currentList.add(this.innerSelectMap.get(key));
                }
            } else {
                this.currentList = (List) photoManageHelper.getAllMap().get(this.albumPath);
            }
            if (this.index == -1) {
                this.index = 0;
            }
        }
        this.totalSize = this.currentList.size();
        this.imageUrlList = new ArrayList();
        for (PictureModel model : this.currentList) {
            RichImageModel richImageModel = new RichImageModel();
            richImageModel.setImageUrl(model.getAbsolutePath());
            this.imageUrlList.add(richImageModel);
        }
    }

    protected boolean isContainTopBar() {
        return false;
    }

    protected void initViews() {
        this.backBtn = (Button) findViewByName("back_btn");
        this.viewPager = (ScaleViewPager) findViewByName("photo_viewpager");
        this.selectBtn = (Button) findViewByName("photo_select_btn");
        this.currNumText = (TextView) findViewByName("photo_curr_num_text");
        this.numText = (TextView) findViewByName("photo_num_text");
        this.finishText = (TextView) findViewByName("photo_finish_text");
        this.pagerAdapter = new ImagePreviewFragmentAdapter(getSupportFragmentManager());
        this.pagerAdapter.setImageUrlList(this.imageUrlList);
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setCurrentItem(this.index);
        initSelect(this.viewPager.getCurrentItem());
        this.numText.setText(String.valueOf(this.innerSelectMap.size()));
        this.currNumText.setText(replaceText(this.currentPosition, this.totalSize));
        if (this.isCallBackRightNow) {
            this.finishText.setText(this.resource.getString("mc_forum_photo_sbumit"));
        } else {
            this.finishText.setText(this.resource.getString("mc_forum_photo_finish"));
        }
    }

    private String replaceText(int num1, int num2) {
        return MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_photo_curr_num"), new String[]{String.valueOf(num1 + 1), String.valueOf(num2)}, getApplicationContext());
    }

    private void initSelect(int position) {
        this.currentPosition = position;
        if (!MCListUtils.isEmpty(this.currentList)) {
            ImagePreviewFragment currentImageViewerFragment = getCurrentFragment();
            if (currentImageViewerFragment != null) {
                if (this.innerSelectMap.containsKey(currentImageViewerFragment.getImageModel().getImageUrl())) {
                    this.selectBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_camera_icon6_h"));
                } else {
                    this.selectBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_camera_icon6_n"));
                }
            }
            resetScaleView(this.currentPosition - 1);
            resetScaleView(this.currentPosition + 1);
            this.currNumText.setText(replaceText(this.currentPosition, this.totalSize));
        }
    }

    private ImagePreviewFragment getCurrentFragment() {
        ImagePreviewFragment insObject = (ImagePreviewFragment)this.pagerAdapter.instantiateItem(this.viewPager, this.viewPager.getCurrentItem());
        if (insObject == null || !(insObject instanceof ImagePreviewFragment)) {
            return null;
        }
        return insObject;
    }

    private void resetScaleView(int position) {
        ImagePreviewFragment leftFragment = this.pagerAdapter.getItem(position);
        if (leftFragment != null && (leftFragment.getImageView() instanceof ScaleView)) {
            ((ScaleView) leftFragment.getImageView()).resetView();
        }
    }

    protected void initActions() {
        this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                PhotoPreviewActivity.this.initSelect(position);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.selectBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                ImagePreviewFragment currentImageViewerFragment = PhotoPreviewActivity.this.getCurrentFragment();
                if (currentImageViewerFragment != null) {
                    RichImageModel richImageModel = currentImageViewerFragment.getImageModel();
                    PictureModel pictureModel = new PictureModel();
                    pictureModel.setAbsolutePath(richImageModel.getImageUrl());
                    if (PhotoPreviewActivity.this.innerSelectMap.containsKey(richImageModel.getImageUrl())) {
                        PhotoPreviewActivity.this.selectBtn.setBackgroundResource(PhotoPreviewActivity.this.resource.getDrawableId("mc_forum_camera_icon6_n"));
                        PhotoPreviewActivity.this.innerSelectMap.remove(richImageModel.getImageUrl());
                    } else if (PhotoPreviewActivity.this.innerSelectMap.size() < PhotoPreviewActivity.photoManageHelper.getMaxSelectNum()) {
                        PhotoPreviewActivity.this.selectBtn.setBackgroundResource(PhotoPreviewActivity.this.resource.getDrawableId("mc_forum_camera_icon6_h"));
                        PhotoPreviewActivity.this.innerSelectMap.put(pictureModel.getAbsolutePath(), pictureModel);
                    } else {
                        MCToastUtils.toast(PhotoPreviewActivity.this.getApplicationContext(), MCStringBundleUtil.resolveString(PhotoPreviewActivity.this.resource.getStringId("mc_forum_photo_beyond_num"), String.valueOf(PhotoPreviewActivity.photoManageHelper.getMaxSelectNum()), PhotoPreviewActivity.this.getApplicationContext()));
                        return;
                    }
                    if (PhotoPreviewActivity.photoManageHelper.getClickListener() != null) {
                        PhotoPreviewActivity.photoManageHelper.getClickListener().changeStatus(pictureModel);
                    }
                    PhotoPreviewActivity.this.numText.setText(String.valueOf(PhotoPreviewActivity.this.innerSelectMap.size()));
                }
            }
        });
        this.finishText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoPreviewActivity.photoManageHelper.closePhotoPreview();
                PhotoPreviewActivity.this.finish();
            }
        });
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoPreviewActivity.this.finish();
            }
        });
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void onSelected() {
        this.currNumText.setText(replaceText(this.currentPosition, this.totalSize));
    }

    public void onSingleTap() {
        onBackPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
        photoManageHelper = null;
    }
}
