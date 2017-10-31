package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.UploadPhotosActivity;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFallNew;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseUserPhotosFragment extends BaseFragment {
    private int UPLOAD = 1;
    protected int aid;
    protected Handler handler;
    protected boolean isHasNext = true;
    private MoreTask moreTask;
    protected int page = 1;
    protected int pageSize = 20;
    protected List<PhotoModel> photoList;
    protected PullToRefreshWaterFallNew pullToRefreshWaterFall;
    private RefreshTask refreshTask;
    protected boolean showPic = true;
    protected long userId;

    class MoreTask extends AsyncTask<Void, Void, BaseResultModel<List<PhotoModel>>> {
        MoreTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected BaseResultModel<List<PhotoModel>> doInBackground(Void... params) {
            return BaseUserPhotosFragment.this.getPhotoList();
        }

        protected void onPostExecute(BaseResultModel<List<PhotoModel>> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(BaseUserPhotosFragment.this.activity.getApplicationContext(), result);
            List<PhotoModel> list = (List) result.getData();
            if (list == null || list.isEmpty()) {
                BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(3);
                return;
            }
            BaseUserPhotosFragment.this.photoList.addAll(list);
            if (result.getHasNext() == 1) {
                BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(0);
            } else {
                BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(3);
            }
            BaseUserPhotosFragment.this.isHasNext = false;
            BaseUserPhotosFragment.this.pullToRefreshWaterFall.onDrawWaterFall(list, 1);
            if (ImagePreviewHelper.getInstance().getOnViewSizeListener() != null) {
                ImagePreviewHelper.getInstance().getOnViewSizeListener().onViewerSizeListener(BaseUserPhotosFragment.this.parseImgList(BaseUserPhotosFragment.this.photoList));
            }
        }
    }

    class RefreshTask extends AsyncTask<Void, Void, BaseResultModel<List<PhotoModel>>> {
        RefreshTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected BaseResultModel<List<PhotoModel>> doInBackground(Void... params) {
            return BaseUserPhotosFragment.this.getPhotoList();
        }

        protected void onPostExecute(BaseResultModel<List<PhotoModel>> result) {
            BaseUserPhotosFragment.this.pullToRefreshWaterFall.onRefreshComplete();
            DZToastAlertUtils.toast(BaseUserPhotosFragment.this.activity.getApplicationContext(), result);
            List<PhotoModel> list = (List) result.getData();
            if (list == null || list.isEmpty()) {
                BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(3);
                return;
            }
            BaseUserPhotosFragment.this.photoList.clear();
            BaseUserPhotosFragment.this.photoList.addAll(list);
            list.clear();
            BaseUserPhotosFragment.this.pullToRefreshWaterFall.onDrawWaterFall(BaseUserPhotosFragment.this.photoList, 0);
            if (result.getHasNext() == 1) {
                BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(0);
                return;
            }
            BaseUserPhotosFragment.this.isHasNext = false;
            BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshComplete(3);
        }
    }

    public abstract BaseResultModel<List<PhotoModel>> getPhotoList();

    protected String getRootLayoutName() {
        return "user_photo_list_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userId = getBundle().getLong("userId");
        this.handler = new Handler();
        this.photoList = new ArrayList();
    }

    private void checkPicModel() {
        Builder exitAlertDialog = new Builder(this.activity).setTitle(this.resource.getStringId("mc_forum_dialog_tip")).setMessage(this.resource.getStringId("mc_forum_warn_pic_list_title"));
        exitAlertDialog.setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BaseUserPhotosFragment.this.showPic = true;
            }
        });
        exitAlertDialog.setNegativeButton(this.resource.getStringId("mc_forum_dialog_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BaseUserPhotosFragment.this.showPic = false;
            }
        }).create();
        if (new SettingSharePreference(this.activity).isPicAvailable()) {
            this.showPic = true;
            return;
        }
        this.showPic = false;
        exitAlertDialog.show();
    }

    protected void initViews(View rootView) {
        this.pullToRefreshWaterFall = (PullToRefreshWaterFallNew) findViewByName(rootView, "mc_forum_list");
        this.pullToRefreshWaterFall.setColumnCount(2);
        this.pullToRefreshWaterFall.initView(this.activity);
    }

    protected void initActions(View rootView) {
        this.pullToRefreshWaterFall.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                BaseUserPhotosFragment.this.page = 1;
                BaseUserPhotosFragment.this.refreshTask = new RefreshTask();
                BaseUserPhotosFragment.this.refreshTask.execute(new Void[0]);
                BaseUserPhotosFragment.this.addTask(BaseUserPhotosFragment.this.refreshTask);
            }
        });
        this.pullToRefreshWaterFall.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BaseUserPhotosFragment baseUserPhotosFragment = BaseUserPhotosFragment.this;
                baseUserPhotosFragment.page++;
                BaseUserPhotosFragment.this.moreTask = new MoreTask();
                BaseUserPhotosFragment.this.moreTask.execute(new Void[0]);
                BaseUserPhotosFragment.this.addTask(BaseUserPhotosFragment.this.moreTask);
            }
        });
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.resource.getString("mc_forum_user_photo_num_label");
        if ((this instanceof UserPhotoFragment) && this.userId == this.sharedPreferencesDB.getUserId()) {
            List<TopBtnModel> rightModels = new ArrayList();
            TopBtnModel topBtnModel = new TopBtnModel();
            topBtnModel.icon = "mc_forum_top_bar_button2";
            topBtnModel.title = this.resource.getString("mc_forum_photo_upload");
            topBtnModel.action = this.UPLOAD;
            rightModels.add(topBtnModel);
            topSettingModel.rightModels = rightModels;
        }
        dealTopBar(topSettingModel);
        registerTopListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == BaseUserPhotosFragment.this.UPLOAD) {
                    Intent intent = new Intent(BaseUserPhotosFragment.this.activity, UploadPhotosActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_PHOTOMODEL, (ArrayList) BaseUserPhotosFragment.this.photoList);
                    BaseUserPhotosFragment.this.activity.startActivity(intent);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.refreshTask != null) {
            this.refreshTask.cancel(true);
        }
        if (this.moreTask != null) {
            this.moreTask.cancel(true);
        }
    }

    protected void loadImage(final ImageView img, String imgUrl) {
        img.setTag(imgUrl);
        ImageViewAware imgAware = new ImageViewAware(img);
        ImageLoader.getInstance().loadImage(imgUrl, new ImageSize(imgAware.getWidth(), imgAware.getHeight()), new SimpleImageLoadingListener() {
            public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                BaseUserPhotosFragment.this.handler.post(new Runnable() {
                    public void run() {
                        if (BaseUserPhotosFragment.this.getActivity() != null && BaseUserPhotosFragment.this.isVisible() && img.getTag() != null && img.getTag().equals(imageUri)) {
                            TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(17170445), new BitmapDrawable(BaseUserPhotosFragment.this.activity.getResources(), loadedImage)});
                            td.startTransition(350);
                            img.setImageDrawable(td);
                        }
                    }
                });
            }
        });
    }

    protected void previewBigImg(int position) {
        ImagePreviewHelper.getInstance().startImagePreview(this.activity, parseImgList(this.photoList), ((PhotoModel) this.photoList.get(position)).getOriginalUrl(), new ImageViewerListener() {
            public void onViewerPageSelect(int position) {
                if (position == BaseUserPhotosFragment.this.photoList.size() - 1 && BaseUserPhotosFragment.this.isHasNext) {
                    BaseUserPhotosFragment.this.pullToRefreshWaterFall.onBottomRefreshExt();
                }
            }

            public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                MCShareModel shareModel = new MCShareModel();
                shareModel.setPicUrl(imgModel.getImageUrl());
                shareModel.setImageFilePath(localPath);
                shareModel.setDownloadUrl(BaseUserPhotosFragment.this.resource.getString("mc_share_download_url"));
                shareModel.setType(1);
                MCForumLaunchShareHelper.share(context, shareModel);
            }
        });
    }

    private ArrayList<RichImageModel> parseImgList(List<PhotoModel> photoList) {
        ArrayList<RichImageModel> imgList = new ArrayList();
        int count = photoList.size();
        for (int i = 0; i < count; i++) {
            RichImageModel imgModel = new RichImageModel();
            imgModel.setImageUrl(MCAsyncTaskLoaderImage.formatUrl(((PhotoModel) photoList.get(i)).getOriginalUrl(), FinalConstant.RESOLUTION_BIG));
            imgList.add(imgModel);
        }
        return imgList;
    }
}
