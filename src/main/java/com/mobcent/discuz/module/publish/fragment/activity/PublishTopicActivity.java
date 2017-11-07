package com.mobcent.discuz.module.publish.fragment.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.service.impl.helper.PostsServiceImplHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.UploadFileTask;
import com.mobcent.discuz.base.widget.MCSetVisibleView;
import com.mobcent.discuz.module.publish.adapter.PublishTopicTypeListAdapter;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PublishTopicActivity extends BasePublishTopicAudioActivity {
    private final int ISANONYMOUS = 2;
    private final int ISHIDDEN = 1;
    private final int ISONLYAUTHOR = 3;
    private final int ISTOPIC = 1;
    protected String action = "new";
    private ImageView addImg;
    private RelativeLayout addImgBox;
    private TextView addImgText;
    protected List<ClassifiedModel> classifiedModeLoadDatalList;
    protected final int contentMaxLen = 7000;
    private FinishListener finishListener;
    protected boolean isClassified = false;
    protected boolean isPublishTopic = true;
    protected int maxUploadPhotoNum;
    protected PhotoManageHelper photoManageHelper;
    private List<String> picPath;
    protected ImageView publicIcon;
    protected ClassifiedModel publicModel;
    protected PublishAsyncTask publishAsyncTask;
    private RelativeLayout setVisibleBox;
    private Button setVisibleBtn;
    private LinearLayout setVisibleVuleBox;
    protected List<UploadPictureModel> uploadList;
    private RelativeLayout visibleBox;
    private List<CheckBox> visibleList;

    class PublishAsyncTask extends AsyncTask<Object, Void, BaseResultModel<Object>> {
        TopicDraftModel draftModel;

        PublishAsyncTask(TopicDraftModel draftModel) {
            this.draftModel = draftModel;
        }

        protected void onPreExecute() {
            ((InputMethodManager) PublishTopicActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PublishTopicActivity.this.titleEdText.getWindowToken(), 0);
            ((InputMethodManager) PublishTopicActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PublishTopicActivity.this.contentEdText.getWindowToken(), 0);
        }

        protected BaseResultModel<Object> doInBackground(Object... params) {
            return PublishTopicActivity.this.postsService.publishTopic((String)params[0], "new");
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            if (result == null) {
                return;
            }
            if (result.getRs() == 1) {
                PublishTopicActivity.this.finish();
                PublishTopicActivity.this.sharedPreferencesDB.setRefresh(true);
                if (result.getAlert() != 1 || MCStringUtil.isEmpty(result.getErrorInfo())) {
                    MCToastUtils.toastByResName(PublishTopicActivity.this, "mc_forum_publish_succ", 0);
                } else {
                    MCToastUtils.toast(PublishTopicActivity.this, result.getErrorInfo(), 0);
                }
                PublishTopicActivity.this.photoManageHelper.onDestroy();
                PublishTopicActivity.this.deleteDraft(this.draftModel);
                return;
            }
            if (!MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toast(PublishTopicActivity.this, result.getErrorInfo(), 0);
            }
            PublishTopicActivity.this.draftService.saveDraftModel(this.draftModel);
        }
    }

    class TopicOnCheckedChangeListener implements OnCheckedChangeListener {
        TopicOnCheckedChangeListener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            for (int i = 0; i < PublishTopicActivity.this.visibleList.size(); i++) {
                if (!((CheckBox) PublishTopicActivity.this.visibleList.get(i)).isChecked()) {
                    ((CheckBox) PublishTopicActivity.this.visibleList.get(i)).setButtonDrawable(PublishTopicActivity.this.resource.getDrawable("mc_forum_publish_icons6_n"));
                    switch (((CheckBox) PublishTopicActivity.this.visibleList.get(i)).getId()) {
                        case 1:
                            PublishTopicActivity.this.isCheckedPermissionModel.setIsHidden(0);
                            break;
                        case 2:
                            PublishTopicActivity.this.isCheckedPermissionModel.setIsAnonymous(0);
                            break;
                        case 3:
                            PublishTopicActivity.this.isCheckedPermissionModel.setIsOnlyAuthor(0);
                            break;
                        default:
                            break;
                    }
                }
                switch (((CheckBox) PublishTopicActivity.this.visibleList.get(i)).getId()) {
                    case 1:
                        PublishTopicActivity.this.isCheckedPermissionModel.setIsHidden(1);
                        break;
                    case 2:
                        PublishTopicActivity.this.isCheckedPermissionModel.setIsAnonymous(1);
                        break;
                    case 3:
                        PublishTopicActivity.this.isCheckedPermissionModel.setIsOnlyAuthor(1);
                        break;
                }
                ((CheckBox) PublishTopicActivity.this.visibleList.get(i)).setButtonDrawable(PublishTopicActivity.this.resource.getDrawable("mc_forum_publish_icons6_h"));
            }
        }
    }

    protected void initDatas() {
        super.initDatas();
        this.picMap = new LinkedHashMap();
        this.visibleList = new ArrayList();
        initExitAlertDialog();
        this.uploadList = new ArrayList();
        this.picPath = new ArrayList();
        this.photoManageHelper = new PhotoManageHelper(getApplicationContext());
        this.maxUploadPhotoNum = 10;
        this.photoManageHelper.setMaxSelectNum(this.maxUploadPhotoNum);
    }

    protected void initViews() {
        super.initViews();
        this.addImg = (ImageView) findViewByName("mc_forum_add_img");
        this.addImgText = (TextView) findViewByName("mc_forum_add_img_text");
        this.addImgBox = (RelativeLayout) findViewByName("mc_forum_add_img_box");
        this.visibleBox = (RelativeLayout) findViewByName("mc_forum_visible_box");
        this.setVisibleVuleBox = (LinearLayout) findViewByName("mc_forum_set_visible_vule_box");
        this.setVisibleBox = (RelativeLayout) findViewByName("mc_forum_setvisible_box");
        this.visibleBox = (RelativeLayout) findViewById(this.resource.getViewId("mc_forum_visible_box"));
        if (this.topicDraftModel == null) {
            if (this.publishType == 3) {
                this.voiceViewBox.setVisibility(0);
            } else if (this.publishType == 1) {
                this.photoManageHelper.openPhotoSelector(this, 2);
            } else if (this.publishType == 2) {
                this.photoManageHelper.openPhotoGraph(this, 2);
            }
        }
        if (this.configComponentModel != null && !this.configComponentModel.isShowTopicTitle()) {
            this.titleEdText.setVisibility(8);
            findViewByName("mc_forum_title_img").setVisibility(8);
        }
    }

    protected void initActions() {
        super.initActions();
//        this.cancleBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
//                PublishTopicActivity.this.hideSoftKeyboard();
//                if (PublishTopicActivity.this.isPublishTopic && PublishTopicActivity.this.exitCheckChanged()) {
//                    PublishTopicActivity.this.exitAlertDialog.show();
//                } else {
//                    PublishTopicActivity.this.finish();
//                }
//            }
//        });
        this.addImg.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (PublishTopicActivity.this.boardId <= 0) {
                    MCToastUtils.toastByResName(PublishTopicActivity.this, "mc_forum_publish_select_board", 0);
                } else if (PublishTopicActivity.this.allowPostImage != 1) {
                    MCToastUtils.toastByResName(PublishTopicActivity.this.getApplicationContext(), "mc_forum_image_permission", 0);
                } else if (PublishTopicActivity.this.picMap.size() < PublishTopicActivity.this.maxUploadPhotoNum) {
                    final String[] names = new String[]{PublishTopicActivity.this.resource.getString("mc_forum_take_photo"), PublishTopicActivity.this.resource.getString("mc_forum_gallery_local_pic")};
                    new Builder(PublishTopicActivity.this).setTitle(PublishTopicActivity.this.resource.getString("mc_forum_publish_choose")).setItems(names, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (names[which].equals(PublishTopicActivity.this.resource.getString("mc_forum_take_photo"))) {
                                PublishTopicActivity.this.photoManageHelper.openPhotoGraph(PublishTopicActivity.this, 2, 0, PublishTopicActivity.this.picMap);
                            } else if (names[which].equals(PublishTopicActivity.this.resource.getString("mc_forum_gallery_local_pic"))) {
                                PublishTopicActivity.this.photoManageHelper.openPhotoSelector(PublishTopicActivity.this, 2, 0, PublishTopicActivity.this.picMap);
                            }
                        }
                    }).show();
                } else {
                    PublishTopicActivity.this.toast(MCStringBundleUtil.resolveString(PublishTopicActivity.this.resource.getStringId("mc_forum_publish_choose_photo_num_max"), String.valueOf(PublishTopicActivity.this.maxUploadPhotoNum), PublishTopicActivity.this));
                }
            }
        });
        this.finishListener = new FinishListener() {
            public void cameraFinish(int type, Map<String, PictureModel> outerSelectMap, String outputPath, Bitmap bitmap) {
                if (type >= 1) {
                    PublishTopicActivity.this.publicModel.setClassifiedValue(outputPath);
                    PublishTopicActivity.this.publicIcon.setBackgroundDrawable(null);
                    PublishTopicActivity.this.isClassified = true;
                    PublishTopicActivity.this.uploadPic();
                    return;
                }
                PublishTopicActivity.this.picMap.putAll(outerSelectMap);
                PublishTopicActivity.this.sortId = 0;
                PublishTopicActivity.this.updatePicture();
            }

            public void photoFinish(int type, Map<String, PictureModel> outerSelectMap) {
                if (type >= 1) {
                    for (String key : outerSelectMap.keySet()) {
                        PublishTopicActivity.this.publicModel.setClassifiedValue(((PictureModel) outerSelectMap.get(key)).getAbsolutePath());
                        PublishTopicActivity.this.isClassified = true;
                        PublishTopicActivity.this.uploadPic();
                    }
                    return;
                }
                PublishTopicActivity.this.picMap.putAll(outerSelectMap);
                PublishTopicActivity.this.sortId = 0;
                PublishTopicActivity.this.updatePicture();
            }
        };
        this.photoManageHelper.registListener(this.finishListener);
        this.visibleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (PublishTopicActivity.this.boardId > 0) {
                    PublishTopicActivity.this.showSetVisibleLayout();
                } else {
                    MCToastUtils.toastByResName(PublishTopicActivity.this.getApplicationContext(), "mc_forum_publish_select_board", 0);
                }
            }
        });

//        this.sendBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
//                if (!PublishTopicActivity.this.publishIng) {
//                    PublishTopicActivity.this.publishIng = true;
//                    PublishTopicActivity.this.imm.hideSoftInputFromWindow(PublishTopicActivity.this.contentEdText.getWindowToken(), 2);
//                    PublishTopicActivity.this.publicTopic();
//                }
//            }
//        });


        this.setVisibleBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                PublishTopicActivity.this.setVisibleBox.setVisibility(8);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.sendMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!PublishTopicActivity.this.publishIng) {
                    PublishTopicActivity.this.publishIng = true;
                    PublishTopicActivity.this.imm.hideSoftInputFromWindow(PublishTopicActivity.this.contentEdText.getWindowToken(), 2);
                    PublishTopicActivity.this.publicTopic();
                }
                return true;
            }
        });
        return true;
    }

    private void updatePicture() {
        this.addImgBox.removeAllViews();
        this.addImgText.setVisibility(8);
        int count = this.picMap.size();
        int width = (MCPhoneUtil.getDisplayWidth(this) - MCPhoneUtil.getRawSize(this, 1, 76.0f)) / 6;
        int margin8 = MCPhoneUtil.getRawSize(this, 1, 8.0f);
        if (this.picMap != null && !this.picMap.isEmpty() && this.picMap.size() <= this.maxUploadPhotoNum) {
            int i = -1;
            for (String key : this.picMap.keySet()) {
                i++;
                PictureModel model = (PictureModel) this.picMap.get(key);
                ImageView imageView = new ImageView(this);
                LayoutParams pictureLps = new LayoutParams(width, width);
                if (i < 5) {
                    pictureLps.leftMargin = (i * width) + (i * margin8);
                } else {
                    pictureLps.topMargin = width + margin8;
                    pictureLps.leftMargin = ((i - 5) * width) + ((i - 5) * margin8);
                }
                imageView.setLayoutParams(pictureLps);
                imageView.setScaleType(ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(model.getAbsolutePath(), imageView);
                this.addImgBox.addView(imageView);
                if (count >= 10) {
                    this.addImg.setVisibility(8);
                } else {
                    this.addImg.setVisibility(0);
                }
                final int position = i;
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        PublishTopicActivity.this.photoManageHelper.openPhotoPreview(PublishTopicActivity.this, 2, position, PhotoManageHelper.PREVIEW_NORMAL_ALBUM_PATH);
                    }
                });
            }
            LayoutParams addLps = new LayoutParams(width, width);
            if (this.picMap.size() <= 5) {
                addLps.topMargin = margin8;
                addLps.leftMargin = (this.picMap.size() * width) + ((this.picMap.size() + 1) * margin8);
                addLps.addRule(15);
            } else {
                addLps.leftMargin = ((this.picMap.size() - 5) * width) + ((this.picMap.size() - 4) * margin8);
                addLps.topMargin = (margin8 * 2) + width;
            }
            this.addImg.setLayoutParams(addLps);
        }
    }

    public double div(double v1, double v2) {
        return v1 / v2;
    }

    private void showSetVisibleLayout() {
        hideSoftKeyboard();
        if (this.permissionModelPostInfo == null || this.permissionModelPostInfo.getTopicPermissionModel() == null) {
            toast(this.resource.getString("mc_forum_rapid_publish_visible_setting_none"));
            return;
        }
        this.setVisibleBox.setVisibility(0);
        showCheckBox(this.permissionModelPostInfo, 1);
    }

    protected void showCheckBox(PermissionModel permissionModelPostInfo, int topicOrPost) {
        PermissionModel topicSettingModel;
        this.setVisibleVuleBox.removeAllViews();
        this.visibleList = new ArrayList();
        if (topicOrPost == 1) {
            topicSettingModel = permissionModelPostInfo.getTopicPermissionModel();
        } else {
            topicSettingModel = permissionModelPostInfo.getPostPermissionModel();
        }
        if (topicSettingModel.getIsHidden() == 1) {
            if (this.isCheckedPermissionModel.getIsHidden() == 1) {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_posting_hidden"), 1, true));
            } else {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_posting_hidden"), 1, false));
            }
        }
        if (topicSettingModel.getIsAnonymous() == 1) {
            if (this.isCheckedPermissionModel.getIsAnonymous() == 1) {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_posting_anonymous"), 2, true));
            } else {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_posting_anonymous"), 2, false));
            }
        }
        if (topicSettingModel.getIsOnlyAuthor() == 1) {
            if (this.isCheckedPermissionModel.getIsOnlyAuthor() == 1) {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_only_the_author_posts"), 3, true));
            } else {
                this.visibleList.add(new MCSetVisibleView((Context) this, true).create(this.resource.getString("mc_forum_only_the_author_posts"), 3, false));
            }
        }
        if (this.visibleList.size() > 0) {
            for (int i = 0; i < this.visibleList.size(); i++) {
                this.setVisibleVuleBox.addView((View) this.visibleList.get(i));
                ImageView imageView = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 1);
                imageView.setBackgroundResource(this.resource.getDrawableId("mc_forum_wire"));
                imageView.setLayoutParams(lp);
                this.setVisibleVuleBox.addView(imageView);
                ((CheckBox) this.visibleList.get(i)).setOnCheckedChangeListener(new TopicOnCheckedChangeListener());
            }
            this.setVisibleBtn = new Button(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MCPhoneUtil.dip2px(getApplicationContext(), 60.0f), MCPhoneUtil.dip2px(getApplicationContext(), 34.0f));
            layoutParams.topMargin = MCPhoneUtil.dip2px(getApplicationContext(), 10.0f);
            layoutParams.bottomMargin = MCPhoneUtil.dip2px(getApplicationContext(), 10.0f);
            layoutParams.gravity = 17;
            this.setVisibleBtn.setLayoutParams(layoutParams);
            this.setVisibleBtn.setIncludeFontPadding(false);
            this.setVisibleBtn.setTextSize(0, getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_14")));
            this.setVisibleBtn.setText(this.resource.getString("mc_forum_dialog_confirm"));
            this.setVisibleBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_login_button2"));
            this.setVisibleBtn.setGravity(17);
            this.setVisibleBtn.setTextColor(getResources().getColorStateList(this.resource.getColorId("mc_forum_text1_normal_color")));
            this.setVisibleBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PublishTopicActivity.this.setVisibleBox.setVisibility(8);
                }
            });
            this.setVisibleVuleBox.addView(this.setVisibleBtn);
        }
    }

    protected boolean publicTopic() {
        this.content = this.contentEdText.getText().toString();
        if (this.boardId <= 0) {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_select_board");
            this.publishIng = false;
            return false;
        } else if (!checkTitleAndContent()) {
            this.publishIng = false;
            return false;
        } else if (this.isOnlytTopicType != 1 || this.classificationTypeId >= 1) {
            finish();
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_publish", 0);
            hideSoftKeyboard();
            if (this.picMap.size() > 0) {
                uploadPic();
            } else if (this.hasAudio) {
                uploadAudio();
            } else {
                uploadAudioSucc();
            }
            return true;
        } else {
            this.publishIng = false;
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_classified_typename_not_be_null");
            return false;
        }
    }

    protected void uploadAudioSucc() {
        String contentStr = this.postsService.createContentJson(this.content, "ß", "á", this.audioPath, this.uploadList);

        String publishJson = PostsServiceImplHelper.createPublishNormalJsonStr(getApplicationContext(), this.boardId, this.title, contentStr, 0, 0, "", 1, getAid(), this.classificationTypeId, this.classificationTopId, this.isCheckedPermissionModel);
        this.publishAsyncTask = new PublishAsyncTask(convertDraftModel());
        this.publishAsyncTask.execute(new Object[]{publishJson});
    }

    protected String getAid() {
        this.aid = "";
        if (this.uploadList != null) {
            for (int i = 0; i < this.uploadList.size(); i++) {
                UploadPictureModel model = (UploadPictureModel) this.uploadList.get(i);
                if (model != null) {
                    if (this.picPath != null && this.picPath.size() > 0) {
                        for (int j = 0; j < this.picPath.size(); j++) {
                            if (((String) this.picPath.get(j)).equals(model.getPicPath())) {
                                if (this.aid.equals("")) {
                                    this.aid = model.getAid() + "";
                                } else {
                                    this.aid += AdApiConstant.RES_SPLIT_COMMA + model.getAid();
                                }
                            }
                        }
                    } else if (this.aid.equals("")) {
                        this.aid = model.getAid() + "";
                    } else {
                        this.aid += AdApiConstant.RES_SPLIT_COMMA + model.getAid();
                    }
                }
            }
        }
        return this.aid;
    }

    public boolean checkTitleAndContent() {
        if (this.configComponentModel == null || this.configComponentModel.isShowTopicTitle()) {
            this.title = this.titleEdText.getText().toString();
            if (MCStringUtil.isEmpty(this.title)) {
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_title_length_error");
                return false;
            } else if (MCStringUtil.isEmpty(this.content) && !this.hasAudio && this.picMap.size() <= 0) {
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_length_error");
                return false;
            }
        }
        if (this.content.length() > 20) {
            this.title = this.content.substring(0, 20);
        } else {
            this.title = this.content;
        }
        if (MCStringUtil.isEmpty(this.title)) {
            this.title = this.resource.getString("mc_forum_no_title") + this.boardName;
        }
        if (!MCStringUtil.isEmpty(this.content) || this.hasAudio || this.picMap.size() > 0) {
            return true;
        }
        MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_length_error");
        return false;
    }

    private boolean exitCheckChanged() {
        if (this.isSave || (this.titleEdText.getText().toString().trim().equals("") && this.contentEdText.getText().toString().trim().equals("") && ((this.picMap == null || this.picMap.size() <= 0) && MCStringUtil.isEmpty(this.audioPath)))) {
            return false;
        }
        return true;
    }

    protected void uploadPic() {
        String[] files = (this.publicModel == null || MCStringUtil.isEmpty(this.publicModel.getClassifiedValue()) || !this.isClassified) ? getPicPath() : new String[]{new String(this.publicModel.getClassifiedValue())};
        new UploadFileTask(getApplicationContext(), files, "forum", "image", this.boardId, (long) this.sortId, 0, new BaseRequestCallback<BaseResultModel<List<UploadPictureModel>>>() {
            public void onPreExecute() {
            }

            public void onPostExecute(BaseResultModel<List<UploadPictureModel>> result) {
                DZToastAlertUtils.toast(PublishTopicActivity.this.getApplicationContext(), result);
                if (MCListUtils.isEmpty((List) result.getData()) || result.getRs() != 1) {
                    if (result.getAlert() == 0) {
                        MCToastUtils.toastByResName(PublishTopicActivity.this.getApplicationContext(), "mc_forum_warn_update_fail");
                    }
                    PublishTopicActivity.this.draftService.saveDraftModel(PublishTopicActivity.this.convertDraftModel());
                } else if (PublishTopicActivity.this.publicModel == null || !PublishTopicActivity.this.isClassified) {
                    PublishTopicActivity.this.uploadList = (List) result.getData();
                    if (PublishTopicActivity.this.hasAudio) {
                        PublishTopicActivity.this.uploadAudio();
                    } else {
                        PublishTopicActivity.this.uploadAudioSucc();
                    }
                } else {
                    PublishTopicActivity.this.publicIcon.setImageBitmap(MCImageUtil.getBitmapFromMedia(PublishTopicActivity.this.getApplicationContext(), PublishTopicActivity.this.publicModel.getClassifiedValue(), 100, 100));
                    if (((List) result.getData()).get(0) != null) {
                        String path;
                        if (SharedPreferencesDB.getInstance(PublishTopicActivity.this.getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND)) {
                            path = ((UploadPictureModel) ((List) result.getData()).get(0)).getPicPath();
                        } else {
                            path = ((UploadPictureModel) ((List) result.getData()).get(0)).getAid() + "";
                        }
                        PublishTopicActivity.this.publicModel.setClassifiedValue(path);
                        PublishTopicActivity.this.publicModel.setClassifiedAid(((UploadPictureModel) ((List) result.getData()).get(0)).getAid() + "");
                    }
                    PublishTopicActivity.this.isClassified = false;
                }
            }
        }).execute(new Void[0]);
    }

    protected void restoreView(TopicDraftModel draft) {
        if (MCStringUtil.isEmpty(this.boardName)) {
            this.boardName = draft.getBoardName();
        }
        if (this instanceof ReplyTopicActivity) {
            this.topicId = draft.getCommonId();
        } else {
            this.boardId = draft.getCommonId();
            this.title = draft.getTitle();
            this.titleEdText.setText(this.title);
        }
        this.typeName = draft.getTypeName();
        this.isCheckedPermissionModel = draft.getIsCheckedSettingModel();
        if (!(this.boradText == null || MCStringUtil.isEmpty(this.boardName))) {
            this.boradText.setText(this.boardName);
        }
        if (!MCStringUtil.isEmpty(this.typeName)) {
            this.typeText.setVisibility(0);
            this.typeText.setText(this.typeName);
        }
        if (!MCListUtils.isEmpty(draft.getTypeList())) {
            this.typeList = draft.getTypeList();
            this.typeAdapter = new PublishTopicTypeListAdapter(this, this.typeList);
            this.typeListView.setAdapter(this.typeAdapter);
            this.typeAdapter.notifyDataSetChanged();
        }
        DZFaceUtil.setStrToFace(this.contentEdText, draft.getContent(), getApplicationContext());
        this.audioPath = draft.getVoicePath();
        if (!MCStringUtil.isEmpty(this.audioPath)) {
            this.hasAudio = true;
            isPlayVoice(true);
        }
        if (draft.getPicPath() != null && draft.getPicPath().length > 0) {
            for (int i = 0; i < draft.getPicPath().length; i++) {
                PictureModel model = new PictureModel();
                model.setAbsolutePath(draft.getPicPath()[i]);
                this.picMap.put(draft.getPicPath()[i], model);
            }
            updatePicture();
        }

        getSettingModel();
    }

    protected void onDestroy() {
        super.onDestroy();
        hideSoftKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.photoManageHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.multiFaceBox != null && this.multiFaceBox.getVisibility() == 0) {
            this.multiFaceBox.setVisibility(8);
            return false;
        } else if (this.setVisibleBox != null && this.setVisibleBox.getVisibility() == 0) {
            this.setVisibleBox.setVisibility(8);
            return false;
        } else if (this.voiceViewBox != null && this.voiceViewBox.getVisibility() == 0) {
            this.voiceViewBox.setVisibility(8);
            return false;
        } else if (!this.isPublishTopic || !exitCheckChanged()) {
            return super.onKeyDown(keyCode, event);
        } else {
            this.exitAlertDialog.show();
            return true;
        }
    }
}
