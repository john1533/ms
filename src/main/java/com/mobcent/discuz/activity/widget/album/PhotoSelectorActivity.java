package com.mobcent.discuz.activity.widget.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.ClickListener;
import com.mobcent.discuz.activity.widget.cropImage.CropImageActivity;
import com.mobcent.discuz.android.model.PictureAlbumModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint({"NewApi"})
public class PhotoSelectorActivity extends BaseFragmentActivity {
    public static final String SELECT_TYPE = "selectType";
    private static Activity activity;
    private static PhotoManageHelper photoManageHelper;
    private String TAG;
    private PhotoGridViewAdapter adapter;
    private PhotoAlbumAdapter albumAdapter;
    private List<PictureAlbumModel> albumList;
    private ListView albumListView;
    private RelativeLayout albumListViewBox;
    private Button backBtn;
    private ClickListener clickListener = new ClickListener() {
        public boolean changeStatus(PictureModel pictureModel) {
            PhotoSelectorActivity.this.numText.setText(String.valueOf(PhotoSelectorActivity.this.innerSelectMap.size()));
            for (int i = 0; i < PhotoSelectorActivity.this.gridView.getChildCount(); i++) {
                PhotoGridViewAdapter.PhotoGridViewAdapterHolder holder = (PhotoGridViewAdapter.PhotoGridViewAdapterHolder) PhotoSelectorActivity.this.gridView.getChildAt(i).getTag();
                if (holder.getPictureImg().getTag().equals(pictureModel.getAbsolutePath())) {
                    if (PhotoSelectorActivity.this.innerSelectMap.containsKey(pictureModel.getAbsolutePath())) {
                        holder.getIndicatorImg().setVisibility(View.VISIBLE);
                    } else {
                        holder.getIndicatorImg().setVisibility(View.GONE);
                    }
                    return true;
                }
            }
            return false;
        }
    };
    private String currentAlbumPath;
    private TextView finishText;
    private GridView gridView;
    private Map<String, PictureModel> innerSelectMap;
    private boolean isCallBackRightNow;
    private TextView numText;
    private TextView photoAlubmText;
    private TextView photoText;
    private TranslateAnimation popupAnimation;
    private Button previewBtn;
    private int selectType;
    private TranslateAnimation shrinkAnimation;

    private class PhotoAlbumAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        private class PhotoAlbumAdapterHolder {
            private ImageView imageView;
            private TextView textView;

            private PhotoAlbumAdapterHolder() {
            }

            public ImageView getImageView() {
                return this.imageView;
            }

            public void setImageView(ImageView imageView) {
                this.imageView = imageView;
            }

            public TextView getTextView() {
                return this.textView;
            }

            public void setTextView(TextView textView) {
                this.textView = textView;
            }
        }

        public PhotoAlbumAdapter() {
            this.inflater = LayoutInflater.from(PhotoSelectorActivity.this.getApplicationContext());
        }

        public int getCount() {
            return PhotoSelectorActivity.this.albumList.size();
        }

        public Object getItem(int position) {
            return PhotoSelectorActivity.this.albumList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoAlbumAdapterHolder holder;
            PictureAlbumModel pictureAlbumModel = (PictureAlbumModel) PhotoSelectorActivity.this.albumList.get(position);
            if (convertView == null) {
                convertView = this.inflater.inflate(PhotoSelectorActivity.this.resource.getLayoutId("photo_album_dialog_item"), null);
                holder = new PhotoAlbumAdapterHolder();
                initViews(convertView, holder);
                convertView.setTag(holder);
            } else {
                holder = (PhotoAlbumAdapterHolder) convertView.getTag();
            }
            holder.getImageView().setTag(pictureAlbumModel.getFirstPicPath());
            loadImage(holder.getImageView());
            String folderName = "";
            int indexPosition = pictureAlbumModel.getFolderPath().lastIndexOf("/");
            if (indexPosition < 0) {
                folderName = pictureAlbumModel.getFolderPath();
            } else {
                folderName = pictureAlbumModel.getFolderPath().substring(indexPosition + 1, pictureAlbumModel.getFolderPath().length());
            }
            holder.getTextView().setText(folderName + "(" + pictureAlbumModel.getSize() + ")");
            return convertView;
        }

        private void initViews(View convertView, PhotoAlbumAdapterHolder holder) {
            holder.setImageView((ImageView) convertView.findViewById(PhotoSelectorActivity.this.resource.getViewId("album_first_img")));
            holder.setTextView((TextView) convertView.findViewById(PhotoSelectorActivity.this.resource.getViewId("album_name_text")));
        }

        private void loadImage(final ImageView imageView) {
            ImageLoader.getInstance().loadImage((String) imageView.getTag(), new ImageSize(200, 200), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageView.getTag().equals(imageUri)) {
                        imageView.setImageBitmap(loadedImage);
                    }
                }
            });
        }
    }

    public class PhotoGridViewAdapter extends BaseAdapter {
        private Context context;
        private List<PictureModel> currentList;
        private LayoutInflater inflater;
        private boolean isLoad = true;

        public class PhotoGridViewAdapterHolder {
            private ImageView indicatorImg;
            private RelativeLayout layout;
            private ImageView pictureImg;

            public RelativeLayout getLayout() {
                return this.layout;
            }

            public void setLayout(RelativeLayout layout) {
                this.layout = layout;
            }

            public ImageView getPictureImg() {
                return this.pictureImg;
            }

            public void setPictureImg(ImageView pictureImg) {
                this.pictureImg = pictureImg;
            }

            public ImageView getIndicatorImg() {
                return this.indicatorImg;
            }

            public void setIndicatorImg(ImageView indicatorImg) {
                this.indicatorImg = indicatorImg;
            }
        }

        public void setLoad(boolean isLoad) {
            this.isLoad = isLoad;
        }

        public PhotoGridViewAdapter(Context context, String albumPath) {
            this.context = context;
            this.inflater = LayoutInflater.from(context.getApplicationContext());
            this.currentList = (List) PhotoSelectorActivity.photoManageHelper.getAllMap().get(albumPath);
            if (this.currentList == null) {
                this.currentList = new ArrayList();
            }
        }

        public void setAlbumPath(String albumPath) {
            this.currentList = (List) PhotoSelectorActivity.photoManageHelper.getAllMap().get(albumPath);
            if (this.currentList == null) {
                this.currentList = new ArrayList();
            }
        }

        public int getCount() {
            return this.currentList.size();
        }

        public Object getItem(int position) {
            return this.currentList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PictureModel pictureModel = (PictureModel) this.currentList.get(position);
            pictureModel.setPosition(position);
            convertView = getPhotoGridView(convertView);
            PhotoGridViewAdapterHolder holder = (PhotoGridViewAdapterHolder) convertView.getTag();
            holder.getPictureImg().setImageBitmap(null);
            holder.getPictureImg().setTag(pictureModel.getAbsolutePath());
            if (this.isLoad) {
                loadImageByTag(holder.getPictureImg());
            }
            if (PhotoSelectorActivity.photoManageHelper != null) {
                Map<String, PictureModel> innerSelectMap = PhotoSelectorActivity.photoManageHelper.getInnerSelectMap();
                if (!(innerSelectMap == null || innerSelectMap.isEmpty())) {
                    if (innerSelectMap.containsKey(pictureModel.getAbsolutePath())) {
                        holder.getIndicatorImg().setVisibility(View.VISIBLE);
                    } else {
                        holder.getIndicatorImg().setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
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

        private View getPhotoGridView(View convertView) {
            if (convertView == null) {
                convertView = this.inflater.inflate(PhotoSelectorActivity.this.resource.getLayoutId("photo_details_item"), null);
                PhotoGridViewAdapterHolder holder = new PhotoGridViewAdapterHolder();
                initPhotoGridViewAdapterHolder(convertView, holder);
                convertView.setTag(holder);
                return convertView;
            }
//            holder = (PhotoGridViewAdapterHolder) convertView.getTag();//TODO
            return convertView;
        }

        private void initPhotoGridViewAdapterHolder(View convertView, PhotoGridViewAdapterHolder holder) {
            int width = (MCPhoneUtil.getDisplayWidth(this.context.getApplicationContext()) - (MCPhoneUtil.getRawSize(this.context, 1, 5.0f) * 4)) / 3;
            LayoutParams layoutParams = new LayoutParams(width, width);
            holder.setLayout((RelativeLayout) convertView.findViewById(PhotoSelectorActivity.this.resource.getViewId("photo_box")));
            holder.getLayout().setLayoutParams(layoutParams);
            holder.setPictureImg((ImageView) convertView.findViewById(PhotoSelectorActivity.this.resource.getViewId("photo_img")));
            holder.setIndicatorImg((ImageView) convertView.findViewById(PhotoSelectorActivity.this.resource.getViewId("photo_indicator_img")));
        }
    }

    protected String getLayoutName() {
        return "photo_selector_activity";
    }

    public static void setManageHelper(PhotoManageHelper helper) {
        photoManageHelper = helper;
    }

    protected void initDatas() {
        super.initDatas();
        this.TAG = "PhotoSelectorActivity";
        this.currentAlbumPath = PhotoManageHelper.ALL;
        activity = this;
        Intent intent = getIntent();
        if (intent != null) {
            this.selectType = intent.getIntExtra("selectType", 2);
            this.isCallBackRightNow = intent.getBooleanExtra(PhotoManageHelper.CALL_BACK_RIGHT_NOW, false);
        }
        this.albumList = photoManageHelper.getAlbumList();
        this.innerSelectMap = photoManageHelper.getInnerSelectMap();
    }

    protected boolean isContainTopBar() {
        return false;
    }

    protected void initViews() {
        this.backBtn = (Button) findViewByName("back_btn");
        this.previewBtn = (Button) findViewByName("photo_preview");
        this.photoAlubmText = (TextView) findViewByName("photo_album_text");
        this.gridView = (GridView) findViewByName("photo_grid_view");
        this.photoText = (TextView) findViewByName("photo_list_text");
        this.numText = (TextView) findViewByName("photo_num_text");
        this.finishText = (TextView) findViewByName("photo_finish_text");
        this.albumListViewBox = (RelativeLayout) findViewByName("photo_album_listview_box");
        this.albumListView = (ListView) findViewByName("photo_album_listview");
        this.albumAdapter = new PhotoAlbumAdapter();
        this.albumListView.setAdapter(this.albumAdapter);
        this.numText.setText(String.valueOf(this.innerSelectMap.size()));
        if (this.selectType == 1) {
            this.finishText.setVisibility(View.GONE);
            this.numText.setVisibility(View.GONE);
        } else {
            this.finishText.setVisibility(View.VISIBLE);
            this.numText.setVisibility(View.VISIBLE);
        }
        if (this.isCallBackRightNow) {
            this.finishText.setText(this.resource.getString("mc_forum_photo_sbumit"));
        } else {
            this.finishText.setText(this.resource.getString("mc_forum_photo_finish"));
        }
        initPreviewBtn();
    }

    private void initPreviewBtn() {
        if (this.innerSelectMap.isEmpty()) {
            this.previewBtn.setVisibility(View.GONE);
        } else {
            this.previewBtn.setVisibility(View.VISIBLE);
        }
    }

    protected void initActions() {
        this.adapter = new PhotoGridViewAdapter(getApplicationContext(), this.currentAlbumPath);
        this.gridView.setAdapter(this.adapter);
        photoManageHelper.registListener(this.clickListener);
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PictureModel pictureModel = (PictureModel) PhotoSelectorActivity.this.gridView.getAdapter().getItem(position);
                if (PhotoSelectorActivity.this.selectType == 1) {
                    CropImageActivity.setManageHelper(PhotoSelectorActivity.photoManageHelper);
                    Intent intent = new Intent(PhotoSelectorActivity.this.getApplicationContext(), CropImageActivity.class);
                    intent.putExtra("inputPath", pictureModel.getAbsolutePath());
                    PhotoSelectorActivity.this.startActivity(intent);
                    PhotoSelectorActivity.activity.finish();
                    return;
                }
                PhotoGridViewAdapter.PhotoGridViewAdapterHolder holder = (PhotoGridViewAdapter.PhotoGridViewAdapterHolder) view.getTag();
                if (PhotoSelectorActivity.this.innerSelectMap.containsKey(pictureModel.getAbsolutePath())) {
                    holder.getIndicatorImg().setVisibility(View.GONE);
                    PhotoSelectorActivity.this.innerSelectMap.remove(pictureModel.getAbsolutePath());
                } else if (PhotoSelectorActivity.this.innerSelectMap.size() < PhotoSelectorActivity.photoManageHelper.getMaxSelectNum()) {
                    holder.getIndicatorImg().setVisibility(View.VISIBLE);
                    PhotoSelectorActivity.this.innerSelectMap.put(pictureModel.getAbsolutePath(), pictureModel);
                } else {
                    MCToastUtils.toast(PhotoSelectorActivity.this.getApplicationContext(), MCStringBundleUtil.resolveString(PhotoSelectorActivity.this.resource.getStringId("mc_forum_photo_beyond_num"), String.valueOf(PhotoSelectorActivity.photoManageHelper.getMaxSelectNum()), PhotoSelectorActivity.this.getApplicationContext()));
                    return;
                }
                PhotoSelectorActivity.this.initPreviewBtn();
                PhotoSelectorActivity.this.numText.setText(String.valueOf(PhotoSelectorActivity.this.innerSelectMap.size()));
            }
        });
        this.gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                PhotoSelectorActivity.photoManageHelper.openPhotoPreview(PhotoSelectorActivity.this, 2, position, PhotoSelectorActivity.this.currentAlbumPath);
                return false;
            }
        });
        this.finishText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoSelectorActivity.photoManageHelper.closePhotoSelector();
                PhotoSelectorActivity.this.finish();
            }
        });
        this.photoText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoSelectorActivity.this.setAlbumListView(PhotoSelectorActivity.this.albumListViewBox.getVisibility() == View.VISIBLE);
            }
        });
        this.albumListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PictureAlbumModel pictureAlbumModel = (PictureAlbumModel) PhotoSelectorActivity.this.albumListView.getAdapter().getItem(position);
                if (!PhotoSelectorActivity.this.currentAlbumPath.equals(pictureAlbumModel.getFolderPath())) {
                    PhotoSelectorActivity.this.currentAlbumPath = pictureAlbumModel.getFolderPath();
                    String folderName = "";
                    int indexPosition = PhotoSelectorActivity.this.currentAlbumPath.lastIndexOf("/");
                    if (indexPosition < 0) {
                        folderName = pictureAlbumModel.getFolderPath();
                    } else {
                        folderName = pictureAlbumModel.getFolderPath().substring(indexPosition + 1, pictureAlbumModel.getFolderPath().length());
                    }
                    PhotoSelectorActivity.this.photoAlubmText.setText(folderName);
                    PhotoSelectorActivity.this.adapter.setAlbumPath(pictureAlbumModel.getFolderPath());
                    PhotoSelectorActivity.this.adapter.notifyDataSetChanged();
                }
                PhotoSelectorActivity.this.setAlbumListView(true);
            }
        });
        this.albumListViewBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                PhotoSelectorActivity.this.setAlbumListView(true);
                return true;
            }
        });
        this.previewBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                PhotoSelectorActivity.photoManageHelper.setFromSelector(true);
                PhotoSelectorActivity.photoManageHelper.openPhotoPreview(PhotoSelectorActivity.this, 2, -1, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH);
            }
        });
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (PhotoSelectorActivity.this.albumListView == null || PhotoSelectorActivity.this.albumListViewBox.getVisibility() != View.VISIBLE) {
                    PhotoSelectorActivity.this.finish();
                } else {
                    PhotoSelectorActivity.this.setAlbumListView(true);
                }
            }
        });
        this.gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, false));
        this.albumListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, false));
    }

    private void setAlbumListView(boolean isVisiable) {
        int y1 = MCPhoneUtil.dip2px(400.0f);
        if (this.popupAnimation == null) {
//            this.popupAnimation = new TranslateAnimation(0.0f, 0.0f, (float) y1, (float) null);//TODO
            this.popupAnimation = new TranslateAnimation(0.0f, 0.0f, (float) y1, 0.0f);
            this.popupAnimation.setDuration(200);
            this.popupAnimation.setFillAfter(true);
            this.popupAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    PhotoSelectorActivity.this.albumListViewBox.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                }
            });
        }
        if (this.shrinkAnimation == null) {
//            this.shrinkAnimation = new TranslateAnimation(0.0f, 0.0f, (float) null, (float) y1);//TODO
            this.shrinkAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) y1);
            this.shrinkAnimation.setDuration(200);
            this.shrinkAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    PhotoSelectorActivity.this.albumListViewBox.setVisibility(View.GONE);
                }
            });
        }
        if (isVisiable) {
            this.albumListViewBox.startAnimation(this.shrinkAnimation);
            return;
        }
        this.albumListViewBox.setVisibility(View.VISIBLE);
        this.albumListViewBox.startAnimation(this.popupAnimation);
    }

    protected void onDestroy() {
        super.onDestroy();
        photoManageHelper.removeListener(this.clickListener);
        this.popupAnimation = null;
        this.shrinkAnimation = null;
        photoManageHelper = null;
    }

    public void onBackPressed() {
        if (this.albumListView == null || this.albumListViewBox.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        } else {
            setAlbumListView(true);
        }
    }

    public static void finishActivity() {
        if (activity != null) {
            activity.finish();
        }
    }
}
