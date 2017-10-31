package com.mobcent.discuz.module.topic.detail.fragment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCMultiBottomView;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnRecordDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnSendDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.RecordModel;
import com.mobcent.discuz.activity.view.MCMultiBottomView.SendModel;
import com.mobcent.discuz.activity.view.MCPopupListDialog;
import com.mobcent.discuz.activity.view.MCPopupListDialog.PopModel;
import com.mobcent.discuz.activity.view.MCPopupListDialog.PopupListDialogListener;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.module.publish.fragment.activity.ReplyTopicActivity;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetailBaseAdapter.ClickListener;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetailGroupAdapter;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseMiddleHolder;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils.AudioPlayListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshExpandableListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshExpandableListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshExpandableListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullExpandListOnScrollListener;
import com.mobcent.lowest.base.utils.MCActivityUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public abstract class TopicDetailBaseFragment extends TopicDetailRequestFragment {
    protected TopicDetailGroupAdapter adapter;
    private MCPopupListDialog popDialog;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        setHasOptionsMenu(true);
        this.boardId = getBundle().getLong("boardId", 0);
        this.boardName = getBundle().getString("boardName");
        if (MCStringUtil.isEmpty(this.boardName)) {
            this.boardName = this.resource.getString("mc_forum_topic_detail");
        }
        this.isRequestEdit = getBundle().getBoolean(IntentConstant.INTENT_TOPIC_DETAIL_REQUEST_EDIT);
        if (this.settingModel == null || this.settingModel.getPostlistOrderby() != 0) {
            this.showState = 2;
        } else {
            this.showState = 1;
        }
        this.postsService = new PostsServiceImpl(this.activity.getApplicationContext());
        this.detailList = new ArrayList();
        this.atUserList = new ArrayList();
        this.isCheckedPermissionModel = new PermissionModel();
        this.popupList = new ArrayList();
        this.higherReplyStr = this.resource.getString("mc_forum_posts_higher_relpy");//高级回复
        this.favorTopicStr = this.resource.getString("mc_forum_posts_favorites");//收藏本帖
        this.cancelFavorStr = this.resource.getString("mc_forum_posts_cancle_favorites");//取消收藏
        this.skipPageStr = this.resource.getString("mc_forum_posts_to_page");//跳转页码
        this.openBrowserStr = this.resource.getString("mc_forum_posts_browser");//浏览器打开
        this.copyUrlStr = this.resource.getString("mc_forum_copy_url_browser");//复制链接
        this.ascCheckStr = this.resource.getString("mc_forum_posts_by_asc");//正序查看
        this.descCheckStr = this.resource.getString("mc_forum_posts_by_desc");//倒序查看
        this.nightStr = this.resource.getString("forum_detail_night");
        this.dayStr = this.resource.getString("forum_detail_day");


        ReplyTopicActivity.setReplyRetrunDelegate(this);
        this.sharedPreferencesDB.setRefresh(false);
        getAppApplication().setRateSucc(false);
    }

    protected String getRootLayoutName() {
        return "topic_detail_fragment";
    }

    protected void initViews(View rootView) {
        this.listView = (PullToRefreshExpandableListView) findViewByName(rootView, "topic_detail_list");
        this.listView.setGroupIndicator(null);
        this.bottomView = (MCMultiBottomView) findViewByName(rootView, "bottom_bar_box");
        this.bottomView.setAtUserList(this.atUserList);
        if (this.isRequestEdit) {
            this.bottomView.requestEditFocus();
            ((InputMethodManager) this.activity.getSystemService("input_method")).toggleSoftInput(0, 2);
        }
        updateAdView(this.inflater, Integer.valueOf(this.resource.getString("mc_forum_posts_detail_top_position")).intValue());
        this.listView.setScrollListener(new PausePullExpandListOnScrollListener(ImageLoader.getInstance(), false, false));
        if (this.draftModel != null) {
            restoreView(this.draftModel);
        }
    }

    private void updateAdView(LayoutInflater inflater, int adPosition) {
        View adLayout = inflater.inflate(this.resource.getLayoutId("ad_view"), null);
        this.adView = (AdView) adLayout.findViewById(this.resource.getViewId("adView"));
        this.adView.setImgAdWidth(MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
        this.adView.showAd(adPosition);
        this.listView.addHeaderView(adLayout, null, false);
    }

    public void onResume() {
        boolean z = true;
        super.onResume();
        if (this.settingModel != null) {
            boolean z2;
            this.bottomView.setRecordPerm(this.settingModel.getPostAudioLimit() != -1);
            MCMultiBottomView mCMultiBottomView = this.bottomView;
            if (this.settingModel.getAllowAt() > 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            mCMultiBottomView.setAtPerm(z2);
            this.bottomView.setShowLocation(this.settingModel.getIsShowLocationPost());
        }
        this.bottomView.setSendPerm(this.topicIsExist);
        if (!(this.permissionModel == null || this.permissionModel.getPostPermissionModel() == null)) {
            this.isCheckedPermissionModel = this.permissionModel.getPostPermissionModel();
            if (this.isCheckedPermissionModel.getPostInfo() == null || this.isCheckedPermissionModel.getPostInfo().get(Long.valueOf(this.boardId)) == null) {
                this.bottomView.setPicPerm(true);
            } else {
                if (((PermissionModel) this.permissionModel.getPostPermissionModel().getPostInfo().get(Long.valueOf(this.boardId))).getAllowPostImage() != 1) {
                    z = false;
                }
                this.bottomView.setPicPerm(z);
            }
        }
        if (this.sharedPreferencesDB.isRefresh()) {
            this.sharedPreferencesDB.setRefresh(false);
            if (this.listView != null) {
                this.listView.onRefresh(false);
            }
        }
        if (getAppApplication().isRateSucc()) {
            getAppApplication().setRateSucc(false);
            if (this.listView != null) {
                this.listView.onRefresh(false);
            }
        }
    }

    protected void initActions(View rootView) {
        this.adapter.setBoardId(this.boardId);
        this.adapter.setBoardName(this.boardName);
        this.adapter.setListener(new ClickListener() {
            public void authorClick(boolean isQueryAuthor, long userId) {
                if (isQueryAuthor) {
                    TopicDetailBaseFragment.this.authorId = userId;
                } else {
                    TopicDetailBaseFragment.this.authorId = 0;
                }
                if (TopicDetailBaseFragment.this.listView != null) {
                    TopicDetailBaseFragment.this.listView.onRefresh(false);
                }
            }
        });
        this.listView.setOnGroupClickListener(new OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        this.listView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                TopicDetailBaseFragment.this.dataEvent(1);
            }
        });
        this.listView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                TopicDetailBaseFragment.this.dataEvent(2);
            }
        });
        this.listView.onRefresh();
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).registerListener(new AudioPlayListener() {
            public void onAudioStatusChange(String url, String path, int status, int percent) {
                int childCount = TopicDetailBaseFragment.this.listView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = TopicDetailBaseFragment.this.listView.getChildAt(i);
                    if (view.getTag() != null && (view.getTag() instanceof BaseMiddleHolder)) {
                        BaseMiddleHolder holder = (BaseMiddleHolder) view.getTag();
                        if (holder.getAudioLayout().getTag() != null && holder.getAudioLayout().getTag().equals(url)) {
                            switch (status) {
                                case 1:
                                    holder.getPlayAudioImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_suspend"));
                                    holder.getProgressBar().show();
                                    break;
                                case 2:
                                    holder.getProgressBar().hide();
                                    holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_suspend"));
                                    switch (percent % 3) {
                                        case 0:
                                            holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img1"));
                                            break;
                                        case 1:
                                            holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img2"));
                                            break;
                                        case 2:
                                            holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img3"));
                                            break;
                                        default:
                                            holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                            break;
                                    }
                                case 3:
                                    holder.getPlayAudioImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_play"));
                                    holder.getProgressBar().hide();
                                    break;
                                case 6:
                                    holder.getProgressBar().hide();
                                    holder.getPlayAudioImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_play"));
                                    holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                    break;
                                case 7:
                                    holder.getPlayAudioImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_play"));
                                    holder.getProgressBar().hide();
                                    holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                    MCToastUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), TopicDetailBaseFragment.this.resource.getString("mc_forum_audio_play_error"));
                                    break;
                                default:
                                    break;
                            }
                        }
                        holder.getProgressBar().hide();
                        holder.getPlayAudioImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_play"));
                        holder.getPlayIngImg().setImageResource(TopicDetailBaseFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                    }
                }
            }
        });
        this.bottomView.setOnSendDelegate(new OnSendDelegate() {
            public void sendReply(int type, final SendModel sendModel) {
                TopicDetailBaseFragment.this.bottomView.hidePanel();
                new Thread() {
                    public void run() {
                        final BaseResultModel<Object> baseResultModel = TopicDetailBaseFragment.this.reply(sendModel);
                        TopicDetailBaseFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                DZToastAlertUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), baseResultModel);
                                if (baseResultModel.getRs() == 0) {
                                    TopicDetailBaseFragment.this.saveDraftModel(sendModel);
                                    TopicDetailBaseFragment.this.restoreView(TopicDetailBaseFragment.this.draftModel);
                                    if (MCStringUtil.isEmpty(baseResultModel.getErrorInfo())) {
                                        MCToastUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), TopicDetailBaseFragment.this.resource.getString("mc_forum_reply_fail"));
                                        return;
                                    } else {
                                        MCToastUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), baseResultModel.getErrorInfo());
                                        return;
                                    }
                                }
                                TopicDetailBaseFragment.this.draftService.deleteDraftModel(TopicDetailBaseFragment.this.draftModel);
                            }
                        });
                    }
                }.start();
                TopicDetailBaseFragment.this.createReplyView(sendModel);
            }
        });
        this.bottomView.setOnRecordDelegate(new OnRecordDelegate() {
            public void sendRecord(int type, final RecordModel recordModel) {
                TopicDetailBaseFragment.this.bottomView.hidePanel();
                new Thread() {
                    public void run() {
                        final BaseResultModel<Object> baseResultModel = TopicDetailBaseFragment.this.reply(recordModel);
                        TopicDetailBaseFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                DZToastAlertUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), baseResultModel);
                                if (baseResultModel.getRs() == 0) {
                                    TopicDetailBaseFragment.this.saveDraftModel(recordModel);
                                    if (MCStringUtil.isEmpty(baseResultModel.getErrorInfo())) {
                                        MCToastUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), TopicDetailBaseFragment.this.resource.getString("mc_forum_reply_fail"));
                                        return;
                                    } else {
                                        MCToastUtils.toast(TopicDetailBaseFragment.this.activity.getApplicationContext(), baseResultModel.getErrorInfo());
                                        return;
                                    }
                                }
                                TopicDetailBaseFragment.this.draftService.deleteDraftModel(TopicDetailBaseFragment.this.draftModel);
                            }
                        });
                    }
                }.start();
                TopicDetailBaseFragment.this.createReplyView(recordModel);
            }
        });
        this.listView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                TopicDetailBaseFragment.this.hideSoftKeyboard();
                return false;
            }
        });
    }

    private void dataEvent(int requestId) {
        this.loadDataAsyncTask = new LoadDataAsyncTask(requestId);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        AdManager.getInstance().recyclAdByTag(this.TAG);
        AdManager.getInstance().recyclAdContainerByTag(this.TAG);
        if (this.adView != null) {
            this.adView.free();
        }
        if (this.bottomView != null) {
            this.bottomView.onDestroy();
        }
        if (this.loadDataAsyncTask != null) {
            this.loadDataAsyncTask.cancel(true);
        }
        if (this.favoriteAsyncTask != null) {
            this.favoriteAsyncTask.cancel(true);
        }
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).release();
    }

    public boolean isChildInteruptBackPress() {
        if (this.bottomView == null || this.bottomView.onKeyDown(true)) {
            return false;
        }
        return true;
    }

    protected void componentDealTopbar() {
        this.topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        if (this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle())) {
            this.topSettingModel.title = this.boardName;
        } else {
            this.topSettingModel.title = this.moduleModel.getTitle();
        }
        this.topSettingModel.isTitleClickAble = false;
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button12";
        topBtnModel.action = this.MORE;
        rightModels.add(topBtnModel);
        TopBtnModel topBtnModel1 = new TopBtnModel();
        topBtnModel1.icon = "mc_forum_top_bar_button16";
        topBtnModel1.action = this.SHARE;
        rightModels.add(topBtnModel1);
        this.topSettingModel.rightModels = rightModels;
        dealTopBar(this.topSettingModel);
        registerTopListener(l);
    }

    final OnClickListener l = new OnClickListener() {
        public void onClick(View v) {
//            TopBtnModel t = (TopBtnModel) v.getTag();
//            if (t.action == TopicDetailBaseFragment.this.MORE) {

                if (TopicDetailBaseFragment.this.topicIsExist) {
                    if (TopicDetailBaseFragment.this.popDialog == null) {
                        TopicDetailBaseFragment.this.popDialog = new MCPopupListDialog(TopicDetailBaseFragment.this.activity);
                        TopicDetailBaseFragment.this.popDialog.setOnItemClickListener(new PopupListDialogListener() {
                            public void onItemClick(PopModel popModel, int position) {
                                if (popModel.itemName.equals(TopicDetailBaseFragment.this.higherReplyStr)) {
                                    if (LoginHelper.doInterceptor(TopicDetailBaseFragment.this.activity, null, null)) {
                                        TopicDetailBaseFragment.this.onRelpyClick(TopicDetailBaseFragment.this.draftModel);
                                    }
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.favorTopicStr)) {
                                    if (LoginHelper.doInterceptor(TopicDetailBaseFragment.this.activity, null, null)) {
                                        if (TopicDetailBaseFragment.this.favoriteAsyncTask != null) {
                                            TopicDetailBaseFragment.this.favoriteAsyncTask.cancel(true);
                                        }
                                        TopicDetailBaseFragment.this.favoriteAsyncTask = new FavoriteAsyncTask("favorite");
                                        TopicDetailBaseFragment.this.favoriteAsyncTask.execute(new Void[0]);
                                    }
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.cancelFavorStr)) {
                                    if (TopicDetailBaseFragment.this.favoriteAsyncTask != null) {
                                        TopicDetailBaseFragment.this.favoriteAsyncTask.cancel(true);
                                    }
                                    TopicDetailBaseFragment.this.favoriteAsyncTask = new FavoriteAsyncTask(FinalConstant.DEL_FAVORITE_ACTION);
                                    TopicDetailBaseFragment.this.favoriteAsyncTask.execute(new Void[0]);
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.skipPageStr)) {
                                    View gopageView = TopicDetailBaseFragment.this.inflater.inflate(TopicDetailBaseFragment.this.resource.getLayoutId("goto_page_dialog"), null);
                                    final EditText pageEditText = (EditText) gopageView.findViewById(TopicDetailBaseFragment.this.resource.getViewId("mc_forum_go_to_select_page"));
                                    TextView currPageText = (TextView) gopageView.findViewById(TopicDetailBaseFragment.this.resource.getViewId("mc_forum_select_page"));
                                    if (TopicDetailBaseFragment.this.totalNum % 10 == 0) {
                                        TopicDetailBaseFragment.this.pageCount = TopicDetailBaseFragment.this.totalNum / TopicDetailBaseFragment.this.pageSize;
                                    } else {
                                        TopicDetailBaseFragment.this.pageCount = (TopicDetailBaseFragment.this.totalNum / TopicDetailBaseFragment.this.pageSize) + 1;
                                    }
                                    currPageText.setText(TopicDetailBaseFragment.this.currentPage + "/" + TopicDetailBaseFragment.this.pageCount);
                                    pageEditText.setText(TopicDetailBaseFragment.this.currentPage + "");
                                    pageEditText.setInputType(2);
                                    TopicDetailBaseFragment.this.builder = new Builder(TopicDetailBaseFragment.this.activity);
                                    TopicDetailBaseFragment.this.builder.setView(gopageView);
                                    TopicDetailBaseFragment.this.builder.setNeutralButton(TopicDetailBaseFragment.this.resource.getString("mc_forum_dialog_cancel"), null);
                                    TopicDetailBaseFragment.this.builder.setPositiveButton(TopicDetailBaseFragment.this.resource.getString("mc_forum_dialog_confirm"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            TopicDetailBaseFragment.this.currentPage = Integer.parseInt(pageEditText.getText().toString());
                                            TopicDetailBaseFragment.this.dataEvent(3);
                                        }
                                    });
                                    TopicDetailBaseFragment.this.builder.show();
                                    pageEditText.addTextChangedListener(new TextWatcher() {
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        }

                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        public void afterTextChanged(Editable s) {
                                            String newPageStr = pageEditText.getText().toString();
                                            if (!newPageStr.trim().equals("")) {
                                                if (!MCStringUtil.isNumeric(newPageStr)) {
                                                    pageEditText.setText(TopicDetailBaseFragment.this.currentPage + "");
                                                } else if (Integer.parseInt(newPageStr) > TopicDetailBaseFragment.this.pageCount) {
                                                    pageEditText.setText(TopicDetailBaseFragment.this.currentPage + "");
                                                }
                                            }
                                        }
                                    });
                                    pageEditText.setSelection(pageEditText.getText().toString().length());
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.openBrowserStr)) {
                                    if (TopicDetailBaseFragment.this.postsModel != null) {
                                        Intent intent = new Intent(TopicDetailBaseFragment.this.activity, WebViewFragmentActivity.class);
                                        MCLogUtil.e(TopicDetailBaseFragment.this.TAG, "postsModel.getForumTopicUrl()=" + TopicDetailBaseFragment.this.postsModel.getForumTopicUrl());
                                        intent.putExtra("webViewUrl", TopicDetailBaseFragment.this.postsModel.getForumTopicUrl());
                                        intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TITLE, TopicDetailBaseFragment.this.postsModel.getTitle());
                                        TopicDetailBaseFragment.this.activity.startActivity(intent);
                                    }
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.copyUrlStr)) {
                                    if (TopicDetailBaseFragment.this.postsModel != null && !MCStringUtil.isEmpty(TopicDetailBaseFragment.this.postsModel.getForumTopicUrl())) {
                                        ((ClipboardManager) TopicDetailBaseFragment.this.activity.getSystemService("clipboard")).setText(TopicDetailBaseFragment.this.postsModel.getTitle() + " " + TopicDetailBaseFragment.this.postsModel.getForumTopicUrl());
                                        MCToastUtils.toastByResName(TopicDetailBaseFragment.this.activity.getApplicationContext(), "mc_forum_tel_copy_to_clipboard");
                                    }
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.ascCheckStr)) {
                                    TopicDetailBaseFragment.this.showState = 1;
                                    if (TopicDetailBaseFragment.this.loadDataAsyncTask != null) {
                                        TopicDetailBaseFragment.this.loadDataAsyncTask.cancel(true);
                                    }
                                    TopicDetailBaseFragment.this.loadDataAsyncTask = new LoadDataAsyncTask(1);
                                    TopicDetailBaseFragment.this.loadDataAsyncTask.execute(new Void[0]);
                                } else if (popModel.itemName.equals(TopicDetailBaseFragment.this.descCheckStr)) {
                                    TopicDetailBaseFragment.this.showState = 2;
                                    if (TopicDetailBaseFragment.this.loadDataAsyncTask != null) {
                                        TopicDetailBaseFragment.this.loadDataAsyncTask.cancel(true);
                                    }
                                    TopicDetailBaseFragment.this.loadDataAsyncTask = new LoadDataAsyncTask(1);
                                    TopicDetailBaseFragment.this.loadDataAsyncTask.execute(new Void[0]);
                                }else if (popModel.itemName.equals(TopicDetailBaseFragment.this.dayStr)||popModel.itemName.equals(TopicDetailBaseFragment.this.nightStr)) {
                                    boolean isLight = sharedPreferencesDB.isLight();
                                    activity.setTheme(MCActivityUtils.getTheme(activity, isLight));
                                    sharedPreferencesDB.setLight(!isLight);
//                                    activity.recreate();
                                }
                            }
                        });
                    }
                    TopicDetailBaseFragment.this.popDialog.setPopList(TopicDetailBaseFragment.this.createPopList());
                    TopicDetailBaseFragment.this.popDialog.show(v);
                }
//            } else if (t.action == TopicDetailBaseFragment.this.SHARE) {
//                if (TopicDetailBaseFragment.this.topicIsExist) {
//                    TopicDetailBaseFragment.this.adapter.share(3);
//                }
//            } else if (t.action == -2 && TopicDetailBaseFragment.this.listView != null) {
//                TopicDetailBaseFragment.this.listView.onRefresh();
//            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        List<PopModel> popList = createPopList();
//        for(PopModel model:popList){
//            menu.add(model.itemName).setIcon(resource.getDrawableId(model.drawableName));
//        }



        inflater.inflate(resource.getMenuId("menu_topic_detail"), menu);
        ((MyActionProvider) MenuItemCompat.getActionProvider(menu.findItem(resource.getViewId("menu_popup_view")))).setOnClickListener(l);


        super.onCreateOptionsMenu(menu, inflater);
    }


    public static class MyActionProvider extends ActionProvider {
        /** Context wrapper. */
        private ContextWrapper mContextWrapper;
        MCResource resource;
        OnClickListener onClickListener;


        public MyActionProvider(Context context) {
            super(context);
            mContextWrapper = (ContextWrapper)context;
            resource = MCResource.getInstance(context);
        }

        @Override
        public View onCreateActionView() {
            // Inflate the action view to be shown on the action bar.
            LayoutInflater layoutInflater = LayoutInflater.from(mContextWrapper);
            View view = layoutInflater.inflate(resource.getLayoutId("my_action_provider"), null);
            ImageView popupView = (ImageView)view.findViewById(resource.getViewId("popup_view"));
            popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v);
                }
            });

            return view;
        }

        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        /**
         * show the popup menu.
         *
         * @param v
         */
        private void showPopup(View v) {
            MCToastUtils.toast(mContextWrapper,"show Pop up!");
        }
    }




    /**
     * 初始化弹出菜单
     */
    public List<PopModel> createPopList() {
        List<PopModel> popList = new ArrayList();
        PopModel popupModel1 = new PopModel();
        popupModel1.itemName = this.higherReplyStr;
        popupModel1.drawableName = "mc_forum_icon33";
        popList.add(popupModel1);
        PopModel popupModel2 = new PopModel();
        if (this.postsModel == null || this.postsModel.getIsFavor() != 1) {
            popupModel2.itemName = this.favorTopicStr;
            popupModel2.drawableName = "mc_forum_icon34";
        } else {
            popupModel2.itemName = this.cancelFavorStr;
            popupModel2.drawableName = "mc_forum_ico34_d";
        }
        popList.add(popupModel2);
        PopModel popupModel3 = new PopModel();
        popupModel3.itemName = this.skipPageStr;
        popupModel3.drawableName = "mc_forum_icon35";
        popList.add(popupModel3);
        PopModel popupModel4 = new PopModel();
        if (this.showState == 1) {
            popupModel4.itemName = this.descCheckStr;
        } else if (this.showState == 2) {
            popupModel4.itemName = this.ascCheckStr;
        }
        popupModel4.drawableName = "mc_forum_icon36";
        popList.add(popupModel4);
        PopModel popupModel5 = new PopModel();
        popupModel5.itemName = this.copyUrlStr;
        popupModel5.drawableName = "mc_forum_icon46";
        popList.add(popupModel5);
        PopModel popupModel6 = new PopModel();
        popupModel6.itemName = this.openBrowserStr;
        popupModel6.drawableName = "mc_forum_icon37";
        popList.add(popupModel6);

        if(sharedPreferencesDB.isLight()){
            popupModel6 = new PopModel();
            popupModel6.itemName = this.nightStr;
            popupModel6.drawableName = "mc_forum_icon37";
            popList.add(popupModel6);
        }else{
            popupModel6 = new PopModel();
            popupModel6.itemName = this.dayStr;
            popupModel6.drawableName = "mc_forum_icon37";
            popList.add(popupModel6);
        }

        return popList;
    }

    protected void nofityAdapter() {
        this.adapter.notifyDataSetChanged();
    }

    protected void expandAllView() {
        int groupCount = this.adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            this.listView.expandGroup(i);
        }
    }

    protected void setAtUserList(List<UserInfoModel> list) {
        this.adapter.setAtUserList(this.atUserList);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.bottomView.onActivityResult(requestCode, resultCode, data);
    }
}
