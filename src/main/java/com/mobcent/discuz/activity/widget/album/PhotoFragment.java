package com.mobcent.discuz.activity.widget.album;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.Map;

public class PhotoFragment extends BaseFragment {
    private TextView addText;
    private TextView addText1;
    private FinishListener finishListener;
    private Handler handler = new Handler();
    private PhotoManageHelper helper;
    private LinearLayout layout;
    private LinearLayout layout1;
    private HorizontalScrollView scrollView;
    private HorizontalScrollView scrollView1;
    private Map<String, PictureModel> selectTempMap;
    private Map<String, PictureModel> selectTempMap1;

    protected String getRootLayoutName() {
        return "photo_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "PhotoFragment";
        this.helper = new PhotoManageHelper(this.activity.getApplicationContext());
    }

    protected void initViews(View rootView) {
        this.addText = (TextView) findViewByName(rootView, "add_photo");
        this.scrollView = (HorizontalScrollView) findViewByName(rootView, "photo_scroll_view");
        this.layout = (LinearLayout) findViewByName(rootView, "selected_layout");
        this.addText1 = (TextView) findViewByName(rootView, "add_photo1");
        this.scrollView1 = (HorizontalScrollView) findViewByName(rootView, "photo_scroll_view1");
        this.layout1 = (LinearLayout) findViewByName(rootView, "selected_layout1");
    }

    protected void initActions(View rootView) {
        this.addText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoFragment.this.helper.openPhotoSelector(PhotoFragment.this.activity, 2, 0, PhotoFragment.this.selectTempMap);
            }
        });
        this.addText1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoFragment.this.helper.openPhotoSelector(PhotoFragment.this.activity, 2, 1, PhotoFragment.this.selectTempMap1);
            }
        });
        this.finishListener = new FinishListener() {
            public void photoFinish(int type, Map<String, PictureModel> outerSelectMap) {
                int i;
                if (type == 1) {
                    PhotoFragment.this.selectTempMap1 = outerSelectMap;
                    PhotoFragment.this.layout1.removeAllViews();
                    i = -1;
                    for (String key : outerSelectMap.keySet()) {
                        i++;
                        ImageView imageView = new ImageView(PhotoFragment.this.activity);
                        imageView.setLayoutParams(new LayoutParams(MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f), MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f)));
                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        imageView.setTag(key);
                        PhotoFragment.this.loadImageByTag(imageView);
                        final int position = i;
                        imageView.setOnClickListener(new OnClickListener() {
                            public void onClick(View arg0) {
                                PhotoFragment.this.helper.openPhotoPreview(PhotoFragment.this.activity, 2, position, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH, 1, PhotoFragment.this.selectTempMap1);
                            }
                        });
                        PhotoFragment.this.layout1.addView(imageView);
                        ImageView imageView2 = new ImageView(PhotoFragment.this.activity);
                        imageView2.setLayoutParams(new LayoutParams(MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 20.0f), MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f)));
                        imageView2.setScaleType(ScaleType.CENTER_CROP);
                        imageView2.setBackgroundColor(0x000);//TODO
                        PhotoFragment.this.layout1.addView(imageView2);
                    }
                    return;
                }
                PhotoFragment.this.selectTempMap = outerSelectMap;
                PhotoFragment.this.layout.removeAllViews();
                i = -1;
                for (String key2 : outerSelectMap.keySet()) {
                    i++;
                    ImageView imageView = new ImageView(PhotoFragment.this.activity);
                    imageView.setLayoutParams(new LayoutParams(MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f), MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f)));
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    imageView.setTag(key2);
                    PhotoFragment.this.loadImageByTag(imageView);
//                    position = i;
                    final int position = i;//TODO
                    imageView.setOnClickListener(new OnClickListener() {
                        public void onClick(View arg0) {
                            PhotoFragment.this.helper.openPhotoPreview(PhotoFragment.this.activity, 2, position, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH, 0, PhotoFragment.this.selectTempMap);
                        }
                    });
                    PhotoFragment.this.layout.addView(imageView);
                    ImageView imageView2 = new ImageView(PhotoFragment.this.activity);
                    imageView2.setLayoutParams(new LayoutParams(MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 20.0f), MCPhoneUtil.getRawSize(PhotoFragment.this.activity.getApplicationContext(), 1, 100.0f)));
                    imageView2.setScaleType(ScaleType.CENTER_CROP);
                    imageView2.setBackgroundColor(0x000);//TODO
                    PhotoFragment.this.layout.addView(imageView2);
                }
            }

            public void cameraFinish(int type, Map<String, PictureModel> map, String outputPath, Bitmap bitmap) {
            }
        };
        this.helper.registListener(this.finishListener);
    }

    public void loadImageByTag(final ImageView img) {
        ImageLoader.getInstance().loadImage((String) img.getTag(), new ImageSize(200, 200), new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (img.getTag().equals(imageUri)) {
                    img.setImageBitmap(loadedImage);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.helper.onDestroy();
    }

    protected void componentDealTopbar() {
        super.componentDealTopbar();
        dealTopBar(createTopSettingModel());
        registerTopListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoFragment.this.isChildInteruptBackPress();
            }
        });
    }
}
