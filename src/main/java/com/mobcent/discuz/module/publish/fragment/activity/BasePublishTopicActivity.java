package com.mobcent.discuz.module.publish.fragment.activity;

import android.app.AlertDialog.Builder;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ClassifyTypeModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigFastPostModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.service.DraftService;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.DraftServiceImpl;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.android.util.DZPoiUtil;
import com.mobcent.discuz.android.util.DZPoiUtil.PoiDelegate;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.widget.MCEditText;
import com.mobcent.discuz.module.publish.adapter.PublishTopicTypeListAdapter;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate.ClickPoiItemLisenter;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasePublishTopicActivity extends BaseFragmentActivity implements IntentConstant {
    protected TextView addImgText;
    protected String aid;
    protected int allowPostAttachment = 1;
    protected int allowPostImage = 1;
    protected String audioPath;
    protected String baseLocationStr;
    protected long boardId;
    private List<ConfigFastPostModel> boardList;
    protected String boardName;
    protected TextView boradText;
    protected LinearLayout bottomBarBox;
//    protected Button cancleBtn;
    protected int classificationTopId;
    protected int classificationTypeId;
    protected String classificationTypeName;
    protected ConfigComponentModel configComponentModel;
    protected String content;
    protected MCEditText contentEdText;
    protected LinearLayout contentLayout;
    protected long draftId = 1;
    protected DraftService draftService;
    protected Builder exitAlertDialog;
    protected long initBoardId;
    protected PermissionModel isCheckedPermissionModel;
    protected boolean isLocationSucc = false;
    protected int isOnlytTopicType;
    protected boolean isSave = false;
    protected double latitude;
    protected LinearLayout locationBox;
    protected boolean locationComplete = false;
    protected String locationStr;
    protected TextView locationText;
    private MCLocationUtil locationUtil;
    protected ImageView loctionImg;
    protected double longitude;
    private Handler mHandler = new Handler();
    protected PermissionModel permissionModelPostInfo;
    protected Map<String, PictureModel> picMap;
    private DZPoiUtil poiUtil;
    private List<String> pois;
    protected PostsService postsService;
    protected boolean publishIng = false;
    protected int publishType;
    protected int requireLocation = 0;
//    protected Button sendBtn;
    protected SharedPreferencesDB sharedPreferencesDB;
    protected String title;
    protected EditText titleEdText;
//    protected TextView topBarTitleText;
    protected TopicDraftModel topicDraftModel;
    protected long topicId;
    protected PublishTopicTypeListAdapter typeAdapter;
    protected List<ClassifyTypeModel> typeList;
    protected ListView typeListView;
    protected String typeName;
    protected TextView typeText;
    protected RelativeLayout voiceBox;
    protected RelativeLayout voiceViewBox;
    protected MenuItem sendMenuItem;

    protected void initDatas() {
        super.initDatas();
        this.typeList = (List) getIntent().getSerializableExtra(IntentConstant.INTENT_CLASSIFICATIONTYPE_LIST);
        this.initBoardId = getIntent().getLongExtra("boardId", 0);
        this.boardName = getIntent().getStringExtra("boardName");
        this.publishType = getIntent().getIntExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 0);
        this.configComponentModel = (ConfigComponentModel) getIntent().getSerializableExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL);
        this.topicDraftModel = (TopicDraftModel) getIntent().getSerializableExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL);
        this.boardId = this.initBoardId;
        if (this.configComponentModel != null) {
            this.boardList = this.configComponentModel.getFastPostList();
            if (!MCListUtils.isEmpty(this.boardList) && this.boardList.size() == 1) {
                this.boardId = ((ConfigFastPostModel) this.boardList.get(0)).getFid();
                this.boardName = ((ConfigFastPostModel) this.boardList.get(0)).getName();
            }
        }
        if (!(this.boardId <= 0 || this.permissionModel == null || this.permissionModel.getPostInfo() == null || this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId)) == null)) {
            this.isOnlytTopicType = ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel().getIsOnlyTopicType();
            this.typeList = ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel().getClassifyTypeList();
        }
        this.postsService = new PostsServiceImpl(getApplicationContext());
        this.pois = new ArrayList();
        getSettingModel();
        if (this.permissionModelPostInfo != null) {
            this.allowPostImage = this.permissionModelPostInfo.getTopicPermissionModel().getAllowPostImage();
            this.allowPostAttachment = this.permissionModelPostInfo.getTopicPermissionModel().getAllowPostAttachment();
        } else {
            this.allowPostImage = 1;
            this.allowPostAttachment = 1;
        }
        this.isCheckedPermissionModel = new PermissionModel();
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(getApplicationContext());
        this.draftService = new DraftServiceImpl(getApplicationContext());
    }

    protected void getSettingModel() {
        if (this.permissionModel != null && this.permissionModel.getPostInfo() != null && this.boardId > 0) {
            this.permissionModelPostInfo = (PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId));
        }
    }

    protected String getLayoutName() {
        return "publish_topic_activity";
    }

    protected void initViews() {
//        this.cancleBtn = (Button) findViewByName("mc_forum_back_btn");
//        this.topBarTitleText = (TextView) findViewByName("mc_forum_public_label_text");
//        this.sendBtn = (Button) findViewByName("mc_forum_public_btn");

        this.boradText = (TextView) findViewByName("mc_forum_publish_board_text");
        this.typeText = (TextView) findViewByName("mc_forum_publish_type_text");
        this.titleEdText = (EditText) findViewByName("mc_forum_title_edit");
        this.contentEdText = (MCEditText) findViewByName("mc_forum_content_edit");
        this.typeListView = (ListView) findViewByName("mc_forum_board_lv");
        this.contentLayout = (LinearLayout) findViewByName("mc_forum_top_content_box");
        this.locationBox = (LinearLayout) findViewByName("mc_forum_location_box");
        this.loctionImg = (ImageView) findViewByName("mc_forum_location_img");
        this.locationText = (TextView) findViewByName("mc_forum_loction_text");
        this.bottomBarBox = (LinearLayout) findViewByName("mc_forum_bottom_bar_box");
        initLocalLocation();
        if (!new SettingSharePreference(getApplicationContext()).isLocationOpen(this.sharedPreferencesDB.getUserId())) {
            this.locationComplete = true;
            this.locationBox.setVisibility(8);
        }
        if (this.boardId <= 0 || this.topicDraftModel != null) {
            this.boradText.setText(this.boardName);
        } else {
            this.boradText.setVisibility(8);
            findViewByName("mc_forum_type_line").setVisibility(8);
        }
        if (!MCListUtils.isEmpty(this.typeList)) {
            this.typeAdapter = new PublishTopicTypeListAdapter(this, this.typeList);
            this.typeListView.setAdapter(this.typeAdapter);
        } else if (this.boardId > 0) {
            findViewByName("mc_forum_content_top_box").setVisibility(8);
        } else {
            this.typeText.setVisibility(8);
        }
        this.titleEdText.clearFocus();
        this.contentEdText.clearFocus();


//        mToolbar.setTitle("");
//        mToolbar.setNavigationIcon(resource.getDrawableId("mc_forum_top_bar_button1"));
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BasePublishTopicActivity.this.finish();
            }
        });
        if(getSupportActionBar() != null){
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        if ((this.settingModel == null || this.settingModel.getIsShowLocationPost() != 1) && this.settingModel.getIsShowLocationTopic() != 1) {
        if (this.settingModel == null || (this.settingModel.getIsShowLocationPost() != 1 && this.settingModel.getIsShowLocationTopic() != 1)) {
            this.requireLocation = 0;
            return;
        }
        getLocation();
        this.requireLocation = 1;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(resource.getMenuId("menu_post_topic"), menu);
        sendMenuItem = menu.findItem(resource.getViewId("send_msg_img1"));
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    protected void initActions() {
        this.titleEdText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BasePublishTopicActivity.this.voiceViewBox.getVisibility() == 8) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    BasePublishTopicActivity.this.bottomBarBox.setVisibility(8);
                    BasePublishTopicActivity.this.showSoftKeyboard();
                    return;
                }
                BasePublishTopicActivity.this.hideSoftKeyboard();
            }
        });
        this.contentEdText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BasePublishTopicActivity.this.voiceViewBox.getVisibility() == 8) {
                    BasePublishTopicActivity.this.bottomBarBox.setVisibility(0);
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    BasePublishTopicActivity.this.showSoftKeyboard();
                    return;
                }
                BasePublishTopicActivity.this.hideSoftKeyboard();
            }
        });

//        this.cancleBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
//                BasePublishTopicActivity.this.finish();
//            }
//        });


        this.boradText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BasePublishTopicActivity.this.hideSoftKeyboard();
                if (BasePublishTopicActivity.this.initBoardId <= 0) {
                    Intent intent = new Intent(BasePublishTopicActivity.this, SelectBoardFragmentActivity.class);
                    intent.putStringArrayListExtra(IntentConstant.INTENT_BOARDLIST, (ArrayList) BasePublishTopicActivity.this.boardList);
                    intent.putExtra("boardId", BasePublishTopicActivity.this.boardId);
                    BasePublishTopicActivity.this.startActivityForResult(intent, 0);
                }
            }
        });
        this.typeText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BasePublishTopicActivity.this.hideSoftKeyboard();
                if (BasePublishTopicActivity.this.typeList == null || BasePublishTopicActivity.this.typeList.size() <= 0) {
                    BasePublishTopicActivity.this.typeListView.setVisibility(8);
                    BasePublishTopicActivity.this.toast(BasePublishTopicActivity.this.resource.getString("mc_forum_no_classcation_type"));
                    return;
                }
                BasePublishTopicActivity.this.hideSoftKeyboard();
                if (BasePublishTopicActivity.this.typeListView.getVisibility() == 0) {
                    BasePublishTopicActivity.this.typeListView.setVisibility(8);
                } else {
                    BasePublishTopicActivity.this.typeListView.setVisibility(0);
                }
            }
        });
        this.locationBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (new SettingSharePreference(BasePublishTopicActivity.this.getApplicationContext()).isLocationOpen(BasePublishTopicActivity.this.sharedPreferencesDB.getUserId())) {
                    BasePublishTopicActivity.this.getLocation();
                } else {
                    MCToastUtils.toastByResName(BasePublishTopicActivity.this.getApplicationContext(), "mc_forum_location_setting_flag", 1);
                }
            }
        });
        this.locationBox.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                BasePublishTopicActivity.this.loctionImg.setImageResource(BasePublishTopicActivity.this.resource.getDrawableId("mc_forum_publish_icons4_n"));
                BasePublishTopicActivity.this.locationText.setText(BasePublishTopicActivity.this.resource.getString("mc_forum_rapid_publish_show_location"));
                BasePublishTopicActivity.this.requireLocation = 0;
                return true;
            }
        });
        PoiItemDelegate.getInstance().setClickPoiItemLisenter(new ClickPoiItemLisenter() {
            public void clickItem(String poi) {
                BasePublishTopicActivity.this.locationStr += poi;
                BasePublishTopicActivity.this.locationText.setText(BasePublishTopicActivity.this.locationStr);
                BasePublishTopicActivity.this.loctionImg.setImageResource(BasePublishTopicActivity.this.resource.getDrawableId("mc_forum_publish_icons4_h"));
            }
        });
    }

    public void updateTypeView(ClassifyTypeModel model) {
        this.classificationTypeId = model.getId();
        this.classificationTypeName = model.getName();
        this.typeListView.setVisibility(8);
        if (model.isSelect()) {
            this.typeText.setText(this.classificationTypeName);
        } else {
            this.typeText.setText("");
        }
    }

    public void updateView(BoardChild boardModel) {
        getSettingModel();
        this.typeAdapter = new PublishTopicTypeListAdapter(this, this.typeList);
        this.boardId = boardModel.getBoardId();
        this.boardName = boardModel.getBoardName();
        if (this.boradText != null) {
            this.boradText.setText(boardModel.getBoardName());
        }
    }

    protected void getLocation() {
        if (this.requireLocation == 1) {
            this.locationComplete = true;
            this.loctionImg.setImageResource(this.resource.getDrawableId("mc_forum_publish_icons4_n"));
            if (this.isLocationSucc && this.pois != null && !this.pois.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), PoiFragmentAcitvity.class);
                intent.putExtra(PoiFragmentAcitvity.POI_LIST, (ArrayList) this.pois);
                startActivity(intent);
                return;
            }
            return;
        }
        this.loctionImg.setImageResource(this.resource.getDrawableId("mc_forum_publish_icons4_h"));
        this.requireLocation = 1;
        this.locationText.setText(getResources().getString(this.resource.getStringId("mc_forum_obtain_location_info_warn")));
        if (this.locationUtil.getLocationClient() == null) {
            this.locationText.setText(getResources().getString(this.resource.getStringId("mc_forum_location_fail_warn")));
            return;
        }
        this.locationComplete = false;
        this.locationUtil.requestLocation(new LocationDelegate() {
            public void onReceiveLocation(final BDLocation locationModel) {
                BasePublishTopicActivity.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (locationModel != null) {
                            BasePublishTopicActivity.this.longitude = locationModel.getLongitude();
                            BasePublishTopicActivity.this.latitude = locationModel.getLatitude();
                            BasePublishTopicActivity.this.baseLocationStr = locationModel.getCity() + locationModel.getDistrict() + locationModel.getStreet();
                            BasePublishTopicActivity.this.locationStr = BasePublishTopicActivity.this.baseLocationStr;
                            BasePublishTopicActivity.this.locationText.setText(BasePublishTopicActivity.this.locationStr);
                            BasePublishTopicActivity.this.isLocationSucc = true;
                            BasePublishTopicActivity.this.locationComplete = true;
                            BasePublishTopicActivity.this.sharedPreferencesDB.saveLocation(locationModel);
                            return;
                        }
                        BasePublishTopicActivity.this.locationComplete = true;
                        BasePublishTopicActivity.this.locationText.setText(BasePublishTopicActivity.this.getResources().getString(BasePublishTopicActivity.this.resource.getStringId("mc_forum_location_fail_warn")));
                    }
                });
            }
        });
        this.poiUtil.requestPoi(new PoiDelegate() {
            public void onReceivePoi(List<String> poi) {
                if (poi != null && !poi.isEmpty()) {
                    BasePublishTopicActivity.this.pois.clear();
                    if (poi != null && !poi.isEmpty()) {
                        BasePublishTopicActivity.this.pois.addAll(poi);
                    }
                }
            }
        });
    }

    private void initLocalLocation() {
        this.locationUtil = MCLocationUtil.getInstance(this);
        this.poiUtil = DZPoiUtil.getInstance(getApplicationContext());
        BDLocation locationModel = this.locationUtil.getCacheLocation();
        if (locationModel != null) {
            this.longitude = locationModel.getLongitude();
            this.latitude = locationModel.getLatitude();
            this.locationStr = locationModel.getAddrStr();
            this.locationComplete = true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                try {
                    this.classificationTopId = data.getIntExtra(IntentConstant.CLASSIFICATIONTOP_ID, -1);
                    updateView((BoardChild) data.getSerializableExtra(IntentConstant.INTENT_SELECT_BOARD));
                    PermissionModel topicPer = null;
                    if (!(this.permissionModel == null || this.permissionModel.getPostInfo() == null || this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId)) == null)) {
                        topicPer = ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel();
                    }
                    List<ClassifyTypeModel> classficationTypeModelList = topicPer.getClassifyTypeList();
                    if (classficationTypeModelList.size() < 1) {
                        this.typeListView.setVisibility(8);
                    } else {
                        this.typeList = classficationTypeModelList;
                        this.typeAdapter.setTypeList(classficationTypeModelList);
                        this.typeListView.setAdapter(this.typeAdapter);
                        this.typeAdapter.notifyDataSetInvalidated();
                        this.typeAdapter.notifyDataSetChanged();
                    }
                    if (!(this.typeList == null || this.typeList.size() <= 0 || (this instanceof PublishPollTopicActivity))) {
                        this.isOnlytTopicType = topicPer.getIsOnlyTopicType();
                        this.typeText.setVisibility(0);
                        findViewByName("mc_forum_type_line").setVisibility(0);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void initExitAlertDialog() {
        this.exitAlertDialog = new Builder(this).setTitle(this.resource.getStringId("mc_forum_dialog_tip")).setMessage(this.resource.getStringId("mc_forum_warn_publish_title"));
        this.exitAlertDialog.setPositiveButton(this.resource.getStringId("mc_forum_warn_photo_ok"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BasePublishTopicActivity.this.draftService.saveDraftModel(BasePublishTopicActivity.this.convertDraftModel());
                BasePublishTopicActivity.this.finish();
            }
        });
        this.exitAlertDialog.setNegativeButton(this.resource.getStringId("mc_forum_warn_photo_cancel"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BasePublishTopicActivity.this.deleteDraft(BasePublishTopicActivity.this.topicDraftModel);
                BasePublishTopicActivity.this.finish();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (this.topicDraftModel != null) {
            restoreView(this.topicDraftModel);
        }
    }

    protected TopicDraftModel convertDraftModel() {
        TopicDraftModel draft = new TopicDraftModel();
        if (this.topicDraftModel != null) {
            draft.setId(this.topicDraftModel.getId());
        }
        this.isSave = true;
        this.content = this.contentEdText.getText().toString();
        this.title = this.titleEdText.getText().toString();
        if (this instanceof ReplyTopicActivity) {
            draft.setCommonId(this.topicId);
        } else {
            draft.setCommonId(this.boardId);
        }
        draft.setBoardName(this.boardName);
        if (this.isCheckedPermissionModel != null) {
            draft.setIsCheckedSettingModel(this.isCheckedPermissionModel);
        }
        draft.setTitle(this.title);
        draft.setContent(this.content);
        draft.setTypeName(this.typeText.getText().toString());
        if (this.picMap == null || this.picMap.size() != 0 || this.topicDraftModel == null || this.topicDraftModel.getPicPath() == null || this.topicDraftModel.getPicPath().length <= 0) {
            draft.setPicPath(getPicPath());
        } else {
            draft.setPicPath(this.topicDraftModel.getPicPath());
        }
        draft.setVoicePath(this.audioPath);
        if (this instanceof ReplyTopicActivity) {
            draft.setType(2);
        } else if (this instanceof PublishTopicActivity) {
            draft.setType(1);
        } else if ((this instanceof ClassifyTopicActivity) && this.classificationTopId <= 0) {
            draft.setType(1);
        }
        if (!MCListUtils.isEmpty(this.typeList)) {
            draft.setTypeList(this.typeList);
        }
        draft.setTypeId((long) this.classificationTypeId);
        draft.setLocation(this.locationStr);
        return draft;
    }

    protected String[] getPicPath() {
        String[] files = new String[this.picMap.size()];
        int i = 0;
        for (String key : this.picMap.keySet()) {
            files[i] = new String(((PictureModel) this.picMap.get(key)).getAbsolutePath());
            i++;
        }
        return files;
    }

    protected void deleteDraft(TopicDraftModel draft) {
        this.isSave = false;
        this.draftService.deleteDraftModel(draft);
    }

    protected void restoreView(TopicDraftModel draft) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.typeListView == null || this.typeListView.getVisibility() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        this.typeListView.setVisibility(8);
        return true;
    }

    public boolean isSlideAble() {
        return false;
    }

//    protected boolean isContainTopBar() {
//        return false;
//    }
}
