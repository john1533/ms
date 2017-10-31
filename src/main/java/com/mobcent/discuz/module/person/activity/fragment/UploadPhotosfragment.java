package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCPopupListView;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupClickListener;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.constant.UploadConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.UploadFileTask;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UploadPhotosfragment extends BaseFragment {
    private int SEND = 0;
    private SaveAblumInfoTask ablumInfoTask;
    private ImageView addImg;
    private RelativeLayout addImgBox;
    private TextView addImgText;
    private int albumId = -1;
    private FinishListener finishListener;
    private String ids = "";
    private boolean isUpload = false;
    private int maxUploadPhotoNum;
    private MCPopupListView mcPopupListView;
    private PhotoManageHelper photoManageHelper;
    private List<PhotoModel> photosList;
    private String picDesc;
    private EditText picDescEditText;
    protected Map<String, PictureModel> picMap;
    private TextView picSelectText;

    class SaveAblumInfoTask extends AsyncTask<String, Void, BaseResultModel<Object>> {
        SaveAblumInfoTask() {
        }

        protected BaseResultModel<Object> doInBackground(String... arg0) {
            return new PostsServiceImpl(UploadPhotosfragment.this.activity.getApplicationContext()).saveAlbumInfo(UploadPhotosfragment.this.albumId, UploadPhotosfragment.this.picDesc, arg0[0]);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(UploadPhotosfragment.this.activity, result);
            UploadPhotosfragment.this.ids = "";
            UploadPhotosfragment.this.hideSoftKeyboard();
            if (result.getRs() == 1) {
                UploadPhotosfragment.this.activity.finish();
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.photosList = (List) getBundle().getSerializable(BaseIntentConstant.BUNDLE_PHOTOMODEL);
        this.picMap = new LinkedHashMap();
        this.photoManageHelper = new PhotoManageHelper(this.activity.getApplicationContext());
        this.maxUploadPhotoNum = 5;
        this.photoManageHelper.setMaxSelectNum(this.maxUploadPhotoNum);
    }

    protected String getRootLayoutName() {
        return "upload_photos_fragment";
    }

    protected void initViews(View rootView) {
        this.picSelectText = (TextView) findViewByName(rootView, "mc_forum_select_text");
        this.addImg = (ImageView) findViewByName(rootView, "mc_forum_add_img");
        this.picDescEditText = (EditText) findViewByName(rootView, "mc_forum_content_edit");
        this.addImgText = (TextView) findViewByName(rootView, "mc_forum_add_img_text");
        this.addImgBox = (RelativeLayout) findViewByName(rootView, "mc_forum_add_img_box");
        this.mcPopupListView = (MCPopupListView) findViewByName(rootView, "photos_popup_listview");
    }

    protected void initActions(View rootView) {
        this.picSelectText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (UploadPhotosfragment.this.mcPopupListView.getVisibility() == 8) {
                    UploadPhotosfragment.this.mcPopupListView.setVisibility(0);
                } else {
                    UploadPhotosfragment.this.mcPopupListView.setVisibility(8);
                }
            }
        });
        this.addImg.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (UploadPhotosfragment.this.albumId <= -1) {
                    MCToastUtils.toastByResName(UploadPhotosfragment.this.activity.getApplicationContext(), "mc_forum_publish_select_photos");
                } else if (UploadPhotosfragment.this.picMap.size() < UploadPhotosfragment.this.maxUploadPhotoNum) {
                    final String[] names = new String[]{UploadPhotosfragment.this.resource.getString("mc_forum_take_photo"), UploadPhotosfragment.this.resource.getString("mc_forum_gallery_local_pic")};
                    new Builder(UploadPhotosfragment.this.activity).setTitle(UploadPhotosfragment.this.resource.getString("mc_forum_publish_choose")).setItems(names, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (names[which].equals(UploadPhotosfragment.this.resource.getString("mc_forum_take_photo"))) {
                                UploadPhotosfragment.this.photoManageHelper.openPhotoGraph(UploadPhotosfragment.this.activity, 2, 0, UploadPhotosfragment.this.picMap);
                            } else if (names[which].equals(UploadPhotosfragment.this.resource.getString("mc_forum_gallery_local_pic"))) {
                                UploadPhotosfragment.this.photoManageHelper.openPhotoSelector(UploadPhotosfragment.this.activity, 2, 0, UploadPhotosfragment.this.picMap);
                            }
                        }
                    }).show();
                } else {
                    MCToastUtils.toast(UploadPhotosfragment.this.activity, MCStringBundleUtil.resolveString(UploadPhotosfragment.this.resource.getStringId("mc_forum_publish_choose_photo_num_max"), String.valueOf(UploadPhotosfragment.this.maxUploadPhotoNum), UploadPhotosfragment.this.activity));
                }
            }
        });
        this.mcPopupListView.setResource("mc_forum_personal_publish_bg", 40);
        LayoutParams layoutParams1 = new LayoutParams(MCPhoneUtil.getRawSize(this.activity, 1, 150.0f), -2);
        layoutParams1.addRule(11);
        layoutParams1.rightMargin = MCPhoneUtil.getRawSize(this.activity, 1, 8.0f);
        this.mcPopupListView.setPopupListViewLayoutParams(layoutParams1);
        this.mcPopupListView.init(createPhotos(), new PopupClickListener() {
            public void initTextView(TextView textView) {
                textView.setTextColor(UploadPhotosfragment.this.getResources().getColorStateList(UploadPhotosfragment.this.resource.getColorId("mc_forum_bubble_color")));
                textView.setTextSize(14.0f);
            }

            public void click(TextView textView, ImageView imageView, PopupModel popupModel, int position) {
                UploadPhotosfragment.this.mcPopupListView.setVisibility(8);
                UploadPhotosfragment.this.albumId = popupModel.getId();
                UploadPhotosfragment.this.picSelectText.setText(popupModel.getName());
            }

            public void hideView() {
            }
        });
        this.finishListener = new FinishListener() {
            public void cameraFinish(int type, Map<String, PictureModel> outerSelectMap, String outputPath, Bitmap bitmap) {
                UploadPhotosfragment.this.picMap.putAll(outerSelectMap);
                UploadPhotosfragment.this.updatePicture();
            }

            public void photoFinish(int type, Map<String, PictureModel> outerSelectMap) {
                UploadPhotosfragment.this.picMap.putAll(outerSelectMap);
                UploadPhotosfragment.this.updatePicture();
            }
        };
        this.photoManageHelper.registListener(this.finishListener);
    }

    private void updatePicture() {
        this.addImgBox.removeAllViews();
        this.addImgText.setVisibility(8);
        int count = this.picMap.size();
        int width = (MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()) - MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 76.0f)) / 5;
        int margin8 = MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 8.0f);
        int margin5 = MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 5.0f);
        if (this.picMap != null && !this.picMap.isEmpty() && this.picMap.size() <= this.maxUploadPhotoNum) {
            int i = -1;
            for (String key : this.picMap.keySet()) {
                i++;
                PictureModel model = (PictureModel) this.picMap.get(key);
                ImageView imageView = new ImageView(this.activity.getApplicationContext());
                LayoutParams pictureLps = new LayoutParams(width, width);
                pictureLps.leftMargin = ((width + margin8) * i) + margin5;
                imageView.setLayoutParams(pictureLps);
                imageView.setScaleType(ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(model.getAbsolutePath(), imageView);
                this.addImgBox.addView(imageView);
                if (count >= this.maxUploadPhotoNum) {
                    this.addImg.setVisibility(8);
                } else {
                    this.addImg.setVisibility(0);
                }
                final int position = i;
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        UploadPhotosfragment.this.photoManageHelper.openPhotoPreview(UploadPhotosfragment.this.activity, 2, position, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH);
                    }
                });
            }
            LayoutParams addLps = new LayoutParams(width, width);
            addLps.topMargin = margin8;
            addLps.leftMargin = ((this.picMap.size() * width) + ((this.picMap.size() + 1) * margin8)) + margin5;
            addLps.addRule(15);
            this.addImg.setLayoutParams(addLps);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.photoManageHelper.onActivityResult(this.activity, requestCode, resultCode, data);
    }

    private List<PopupModel> createPhotos() {
        List<PopupModel> list = new ArrayList();
        if (!MCListUtils.isEmpty(this.photosList)) {
            for (int i = 0; i < this.photosList.size(); i++) {
                PopupModel model = new PopupModel();
                model.setName(((PhotoModel) this.photosList.get(i)).getTitle());
                model.setId(((PhotoModel) this.photosList.get(i)).getAlbumId());
                list.add(model);
            }
        }
        return list;
    }

    private void upload() {
        String[] files = new String[this.picMap.size()];
        int i = 0;
        for (String key : this.picMap.keySet()) {
            files[i] = new String(((PictureModel) this.picMap.get(key)).getAbsolutePath());
            i++;
        }
        MCToastUtils.toastByResName(this.activity, "mc_forum_warn_upload_img");
        this.activity.finish();
        new UploadFileTask(this.activity.getApplicationContext(), files, UploadConstant.MODULE_ALBUM, "image", 0, 0, (long) this.albumId, new BaseRequestCallback<BaseResultModel<List<UploadPictureModel>>>() {
            public void onPreExecute() {
            }

            public void onPostExecute(BaseResultModel<List<UploadPictureModel>> result) {
                DZToastAlertUtils.toast(UploadPhotosfragment.this.activity, result);
                UploadPhotosfragment.this.isUpload = false;
                if (MCListUtils.isEmpty((List) result.getData())) {
                    MCToastUtils.toastByResName(UploadPhotosfragment.this.activity, "mc_forum_warn_update_fail");
                } else if (UploadPhotosfragment.this.sharedPreferencesDB.getForumType().equals(FinalConstant.PHPWIND)) {
                    UploadPhotosfragment.this.activity.finish();
                } else {
                    for (int j = 0; j < ((List) result.getData()).size(); j++) {
                        if (UploadPhotosfragment.this.ids.equals("")) {
                            UploadPhotosfragment.this.ids = ((UploadPictureModel) ((List) result.getData()).get(j)).getAid() + "";
                        } else {
                            UploadPhotosfragment.this.ids = UploadPhotosfragment.this.ids + AdApiConstant.RES_SPLIT_COMMA + ((UploadPictureModel) ((List) result.getData()).get(j)).getAid();
                        }
                    }
                    UploadPhotosfragment.this.picDesc = UploadPhotosfragment.this.picDescEditText.getText().toString();
                    UploadPhotosfragment.this.ablumInfoTask = new SaveAblumInfoTask();
                    UploadPhotosfragment.this.ablumInfoTask.execute(new String[]{UploadPhotosfragment.this.ids});
                }
            }
        }).execute(new Void[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.ablumInfoTask != null) {
            this.ablumInfoTask.cancel(true);
        }
        this.photoManageHelper.onDestroy();
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.resource.getString("mc_forum_photo_upload_image");
        List<TopBtnModel> rightModels = new ArrayList();
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button2";
        topBtnModel.title = this.resource.getString("mc_forum_photo_upload");
        topBtnModel.action = this.SEND;
        rightModels.add(topBtnModel);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action != UploadPhotosfragment.this.SEND) {
                    return;
                }
                if (UploadPhotosfragment.this.isUpload || UploadPhotosfragment.this.picMap.size() <= 0) {
                    MCToastUtils.toast(UploadPhotosfragment.this.activity, UploadPhotosfragment.this.resource.getString("mc_forum_uoliad_photos"));
                    return;
                }
                UploadPhotosfragment.this.isUpload = true;
                UploadPhotosfragment.this.upload();
            }
        });
    }

    public double div(double v1, double v2) {
        return v1 / v2;
    }
}
