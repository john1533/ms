package com.mobcent.discuz.module.topic.detail.fragment.adapter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.model.OtherPanelModel;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.msg.fragment.activity.ChatRoomActivity;
import com.mobcent.discuz.module.person.activity.ReportActivity;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.publish.fragment.activity.ReplyTopicActivity;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TopicDetail3CommonAdapter extends TopicDetail3BaseAdapter {
    private Builder builder;
    private boolean isQueryAuthor = false;
    protected Builder postsMoreDialog;
    protected PostsService postsService;
    private String quertAllStr;
    private String queryAuthorStr;
    private String sendMsgStr;
    protected Map<String, Integer[]> sizeMap = new HashMap();
    protected SupportAsyncTask supportAsyncTask;
    private String visitHomeStr;
    private String visitSelfStr;

    public TopicDetail3CommonAdapter(Context context, List<Object> detailList) {
        super(context, detailList);
        this.postsService = new PostsServiceImpl(context.getApplicationContext());
        this.sendMsgStr = this.resource.getString("mc_forum_send_msg");
        this.visitHomeStr = this.resource.getString("mc_forum_visit_user_home");
        this.queryAuthorStr = this.resource.getString("mc_forum_posts_by_author");
        this.quertAllStr = this.resource.getString("mc_forum_posts_by_all");
        this.visitSelfStr = this.resource.getString("mc_forum_visit_self_home");
    }

    public String[] convertListToArray(List<String> list) {
        String[] s = new String[list.size()];
        int j = list.size();
        for (int i = 0; i < j; i++) {
            s[i] = (String) list.get(i);
        }
        return s;
    }

    protected void updateImageView(final String imgUrl, final ImageView imageView, boolean isTopic) {
        if (new SettingSharePreference(this.context).isPicAvailable()) {
            ImageViewAware imgAware = new ImageViewAware(imageView);
            ImageLoader.getInstance().loadImage(imgUrl, new ImageSize(imgAware.getWidth(), imgAware.getHeight()), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, final Bitmap image) {
                    TopicDetail3CommonAdapter.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (image != null) {
                                if (imageView.getTag() == null || !((Boolean) imageView.getTag()).booleanValue()) {
                                    if (TopicDetail3CommonAdapter.this.sizeMap.get(imgUrl) == null) {
                                        TopicDetail3CommonAdapter.this.sizeMap.put(imgUrl, new Integer[]{Integer.valueOf(MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 45.0f)), Integer.valueOf(MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 45.0f))});
                                    }
                                } else if (TopicDetail3CommonAdapter.this.sizeMap.get(imgUrl) == null) {
                                    int margins;
                                    int rightMargins;
                                    int leftMargins;
                                    int topMargins;
                                    int imageWidth = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, (float) image.getWidth());
                                    int imageHeight = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, (float) image.getHeight());
                                    float ratio = ((float) imageWidth) / ((float) imageHeight);
                                    int displayWidth = MCPhoneUtil.getDisplayWidth(TopicDetail3CommonAdapter.this.context.getApplicationContext());
                                    if ("card".equals(TopicDetail3CommonAdapter.this.currStyle())) {
                                        margins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 32.0f);
                                    } else {
                                        margins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 30.0f);
                                    }
                                    if (displayWidth - margins < imageWidth) {
                                        imageWidth = displayWidth - margins;
                                        imageHeight = (int) (((float) imageWidth) / ratio);
                                    }
                                    TopicDetail3CommonAdapter.this.sizeMap.put(imgUrl, new Integer[]{Integer.valueOf(imageWidth), Integer.valueOf(imageHeight)});
                                    LayoutParams layoutParams = new LayoutParams(imageWidth, imageHeight);
                                    if ("card".equals(TopicDetail3CommonAdapter.this.currStyle())) {
                                        rightMargins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 8.0f);
                                        leftMargins = rightMargins;
                                        topMargins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 10.0f);
                                    } else {
                                        rightMargins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 15.0f);
                                        leftMargins = rightMargins;
                                        topMargins = MCPhoneUtil.getRawSize(TopicDetail3CommonAdapter.this.context.getApplicationContext(), 1, 15.0f);
                                    }
                                    layoutParams.setMargins(leftMargins, topMargins, rightMargins, 0);
                                    imageView.setLayoutParams(layoutParams);
                                }
                                imageView.setImageBitmap(image);
                            }
                        }
                    });
                }
            });
        }
    }

    protected void initSupportView(Map<String, OtherPanelModel> extraPanelModels, LinearLayout praiseBox, ImageButton praiseBtn, TextView praiseText) {
        if (extraPanelModels != null) {
            for (String key : extraPanelModels.keySet()) {
                if (FinalConstant.SUPPORT.equals(key)) {
                    praiseBox.setVisibility(0);
                    OtherPanelModel extraPanelModel = (OtherPanelModel) extraPanelModels.get(key);
                    int recommendAdd = extraPanelModel.getRecommendAdd();
                    if (recommendAdd == 0) {
                        praiseText.setVisibility(8);
                    } else {
                        praiseText.setVisibility(0);
                        praiseText.setText(String.valueOf(recommendAdd));
                    }
                    if (extraPanelModel.isHasRecommendAdd()) {
                        praiseBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_ico49_n"));
                        praiseText.setTextColor(this.context.getApplicationContext().getResources().getColorStateList(this.resource.getColorId("mc_forum_point_praise_color")));
                        return;
                    }
                    praiseBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_ico50_n"));
                    praiseText.setTextColor(this.context.getApplicationContext().getResources().getColorStateList(this.resource.getColorId("mc_forum_text4_normal_color")));
                    return;
                }
            }
        }
        praiseBox.setVisibility(8);
    }

    protected void supportAction(long tid, long pid, String type, Map<String, OtherPanelModel> extraPanel, ImageButton praiseBtn, TextView praiseText) {
        final long j = tid;
        final long j2 = pid;
        final String str = type;
        final ImageButton imageButton = praiseBtn;
        final TextView textView = praiseText;
        final Map<String, OtherPanelModel> map = extraPanel;
        praiseBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (LoginHelper.doInterceptor(TopicDetail3CommonAdapter.this.context, null, null)) {
                    TopicDetail3CommonAdapter.this.supportAsyncTask = new SupportAsyncTask(TopicDetail3CommonAdapter.this.context, j, j2, str, new BaseRequestCallback<BaseResultModel<Object>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<Object> result) {
                            if (result.getRs() == 1) {
                                TopicDetail3CommonAdapter.this.supportSuccess(imageButton, textView, map);
                            }
                        }
                    });
                    TopicDetail3CommonAdapter.this.supportAsyncTask.execute(new Void[0]);
                }
            }
        });
    }

    private void supportSuccess(ImageButton praiseBtn, TextView praiseText, Map<String, OtherPanelModel> extraPanel) {
        int recommendAdd = 0;
        if (extraPanel != null) {
            OtherPanelModel otherPanelModel = (OtherPanelModel) extraPanel.get(FinalConstant.SUPPORT);
            recommendAdd = otherPanelModel.getRecommendAdd() + 1;
            otherPanelModel.setRecommendAdd(recommendAdd);
            otherPanelModel.setHasRecommendAdd(true);
        }
        praiseBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_ico49_n"));
        praiseText.setVisibility(0);
        praiseText.setTextColor(this.context.getApplicationContext().getResources().getColorStateList(this.resource.getColorId("mc_forum_point_praise_color")));
        praiseText.setText(String.valueOf(recommendAdd));
    }

    protected void replyAction(View view, long topicId, long toReplyId) {
        final long j = topicId;
        final long j2 = toReplyId;
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                HashMap<String, Serializable> param = new HashMap();
                param.put("boardId", Long.valueOf(TopicDetail3CommonAdapter.this.boardId));
                param.put("boardName", TopicDetail3CommonAdapter.this.boardName);
                param.put("topicId", Long.valueOf(j));
                param.put("toReplyId", Long.valueOf(j2));
                param.put(IntentConstant.INTENT_IS_QUITE, Boolean.valueOf(true));
                param.put(FinalConstant.POSTS_USER_LIST, (ArrayList) TopicDetail3CommonAdapter.this.atUserList);
                if (LoginHelper.doInterceptor(TopicDetail3CommonAdapter.this.context, ReplyTopicActivity.class, param)) {
                    Intent intent = new Intent(TopicDetail3CommonAdapter.this.context, ReplyTopicActivity.class);
                    intent.putExtra("boardId", TopicDetail3CommonAdapter.this.boardId);
                    intent.putExtra("boardName", TopicDetail3CommonAdapter.this.boardName);
                    intent.putExtra("topicId", j);
                    intent.putExtra("toReplyId", j2);
                    intent.putExtra(IntentConstant.INTENT_IS_QUITE, true);
                    intent.putExtra(FinalConstant.POSTS_USER_LIST, (ArrayList) TopicDetail3CommonAdapter.this.atUserList);
                    TopicDetail3CommonAdapter.this.context.startActivity(intent);
                }
            }
        });
    }

    protected void moreAction(View view, long oid, long userId, Map<String, OtherPanelModel> managePanel) {
        if (this.builder == null) {
            this.builder = new Builder(this.context);
        }
        final View view2 = view;
        final long j = oid;
        final long j2 = userId;
        final Map<String, OtherPanelModel> map = managePanel;
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (view2.getTag() != null) {
                    List<String> moreList = (List) view2.getTag();
                    TopicDetail3CommonAdapter.this.builder.setTitle(TopicDetail3CommonAdapter.this.resource.getString("mc_forum_topic_function")).setItems(TopicDetail3CommonAdapter.this.convertListToArray(moreList), TopicDetail3CommonAdapter.this.getMoreDialogClickListener(moreList, j, j2, map)).show();
                    return;
                }
                TopicDetail3CommonAdapter.this.initPostsMoreDialog(j, j2, map);
                TopicDetail3CommonAdapter.this.postsMoreDialog.show();
            }
        });
    }

    protected void initPostsMoreDialog(long oid, long userId, Map<String, OtherPanelModel> managePanel) {
        if (this.postsMoreDialog == null) {
            List<String> postsMoreList = getManagePanelList(userId, managePanel);
            this.postsMoreDialog = new Builder(this.context).setTitle(this.resource.getString("mc_forum_topic_function")).setItems(convertListToArray(postsMoreList), getMoreDialogClickListener(postsMoreList, oid, userId, managePanel));
        }
    }

    protected List<String> getManagePanelList(long userId, Map<String, OtherPanelModel> managePanel) {
        List<String> postsMoreList = new ArrayList();
        if (managePanel != null) {
            for (String key : managePanel.keySet()) {
                postsMoreList.add(((OtherPanelModel) managePanel.get(key)).getTitle());
            }
        }
        if (userId != this.sDb.getUserId()) {
            postsMoreList.add(this.resource.getString("mc_forum_topic_function_report_topic"));
            postsMoreList.add(this.resource.getString("mc_forum_topic_function_report_user"));
        }
        return postsMoreList;
    }

    private DialogInterface.OnClickListener getMoreDialogClickListener(List<String> moreList, long oid, long userId, Map<String, OtherPanelModel> managePanel) {
        final List<String> list = moreList;
        final long j = oid;
        final long j2 = userId;
        final Map<String, OtherPanelModel> map = managePanel;
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TopicDetail3CommonAdapter.this.dealClick(list, which, j, j2, map);
            }
        };
    }

    private void dealClick(List<String> moreList, int which, long oid, long userId, Map<String, OtherPanelModel> managePanel) {
        HashMap<String, Serializable> params;
        Intent intent;
        if (((String) moreList.get(which)).equals(this.resource.getString("mc_forum_topic_function_report_topic"))) {
            params = new HashMap();
            params.put(IntentConstant.REPORT_TYPE, Integer.valueOf(1));
            params.put(IntentConstant.REPOR_OBJECT_ID, Long.valueOf(oid));
            if (LoginHelper.doInterceptor(this.context, ReportActivity.class, params)) {
                intent = new Intent(this.context, ReportActivity.class);
                intent.putExtra(IntentConstant.REPORT_TYPE, 1);
                intent.putExtra(IntentConstant.REPOR_OBJECT_ID, oid);
                this.context.startActivity(intent);
            }
        } else if (((String) moreList.get(which)).equals(this.resource.getString("mc_forum_topic_function_report_user"))) {
            params = new HashMap();
            params.put(IntentConstant.REPORT_TYPE, Integer.valueOf(3));
            params.put(IntentConstant.REPOR_OBJECT_ID, Long.valueOf(userId));
            if (LoginHelper.doInterceptor(this.context, ReportActivity.class, params)) {
                intent = new Intent(this.context, ReportActivity.class);
                intent.putExtra(IntentConstant.REPORT_TYPE, 3);
                intent.putExtra(IntentConstant.REPOR_OBJECT_ID, userId);
                this.context.startActivity(intent);
            }
        } else if (managePanel.containsKey(moreList.get(which))) {
            String key = (String) moreList.get(which);
            intent = new Intent(this.context, WebViewFragmentActivity.class);
            intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TITLE, ((OtherPanelModel) managePanel.get(key)).getTitle());
            intent.putExtra("webViewUrl", ((OtherPanelModel) managePanel.get(key)).getAction());
            this.context.startActivity(intent);
        }
    }

    protected void iconAction(View view, long userId, String userName, boolean isFollow) {
        final long j = userId;
        final String str = userName;
        final boolean z = isFollow;
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                TopicDetail3CommonAdapter.this.getUserFunctionDialog(j, str, z);
            }
        });
    }

    private List<String> initGroupActionData(long userId, boolean isFollow) {
        List<String> contentList = new ArrayList();
        if (this.isQueryAuthor) {
            contentList.add(this.quertAllStr);
        } else {
            contentList.add(this.queryAuthorStr);
        }
        if (!(this.sDb.getdiscusVersion().equals(FinalConstant.MC_DISCUZ_VERSION_20) || userId == this.currentUserId)) {
            contentList.add(this.sendMsgStr);
        }
        if (userId == this.currentUserId) {
            contentList.add(this.visitSelfStr);
        } else {
            contentList.add(this.visitHomeStr);
        }
        return contentList;
    }

    private void getUserFunctionDialog(long userId, String userName, boolean isFollow) {
        final String[] function = convertListToArray(initGroupActionData(userId, isFollow));
        final long j = userId;
        final String str = userName;
        new Builder(this.context).setTitle(this.context.getString(this.resource.getStringId("mc_forum_topic_function"))).setItems(function, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Serializable> param;
                Intent intent;
                if (function[which].equals(TopicDetail3CommonAdapter.this.sendMsgStr)) {
                    MsgUserListModel msgUserListModel = new MsgUserListModel();
                    msgUserListModel.setToUserId(j);
                    msgUserListModel.setToUserName(str);
                    param = new HashMap();
                    param.put(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                    if (LoginHelper.doInterceptor(TopicDetail3CommonAdapter.this.context, ChatRoomActivity.class, param)) {
                        intent = new Intent(TopicDetail3CommonAdapter.this.context, ChatRoomActivity.class);
                        intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                        TopicDetail3CommonAdapter.this.context.startActivity(intent);
                    }
                } else if (function[which].equals(TopicDetail3CommonAdapter.this.visitHomeStr) || function[which].equals(TopicDetail3CommonAdapter.this.visitSelfStr)) {
                    param = new HashMap();
                    param.put("userId", Long.valueOf(j));
                    if (LoginHelper.doInterceptor(TopicDetail3CommonAdapter.this.context, UserHomeActivity.class, param)) {
                        intent = new Intent(TopicDetail3CommonAdapter.this.context, UserHomeActivity.class);
                        intent.putExtra("userId", j);
                        TopicDetail3CommonAdapter.this.context.startActivity(intent);
                    }
                } else if (function[which].equals(TopicDetail3CommonAdapter.this.queryAuthorStr) || function[which].equals(TopicDetail3CommonAdapter.this.quertAllStr)) {
                    if (function[which].equals(TopicDetail3CommonAdapter.this.queryAuthorStr)) {
                        TopicDetail3CommonAdapter.this.isQueryAuthor = true;
                    } else {
                        TopicDetail3CommonAdapter.this.isQueryAuthor = false;
                    }
                    if (TopicDetail3CommonAdapter.this.listener != null) {
                        TopicDetail3CommonAdapter.this.listener.authorClick(TopicDetail3CommonAdapter.this.isQueryAuthor, j);
                    }
                }
            }
        }).show();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.topicRichList = getAllImageUrl(this.detailList, true);
        this.replyRichList = getAllImageUrl(this.detailList, false);
    }

    public ArrayList<RichImageModel> getAllImageUrl(List<Object> detailList, boolean isTopic) {
        ArrayList<RichImageModel> richImageModelList = new ArrayList();
        List<String> iconList = new ArrayList();
        if (!(detailList == null || detailList.size() == 0)) {
            int i = 0;
            while (i < detailList.size()) {
                RichImageModel model;
                if (isTopic && (detailList.get(i) instanceof PostsModel)) {
                    PostsModel postsModel = (PostsModel) detailList.get(i);
                    if (postsModel != null) {
                        if (!MCStringUtil.isEmpty(postsModel.getIcon())) {
                            iconList.add(postsModel.getIcon());
                        }
                        if (!(postsModel.getContent() == null || postsModel.getContent().isEmpty())) {
                            for (TopicContentModel topicContent : postsModel.getContent()) {
                                if (topicContent.getType() == 1) {
                                    model = new RichImageModel();
                                    model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl(topicContent.getOriginalInfo(), FinalConstant.RESOLUTION_BIG));
                                    richImageModelList.add(model);
                                }
                            }
                        }
                    }
                } else if (detailList.get(i) instanceof ReplyModel) {
                    ReplyModel replyModel = (ReplyModel) detailList.get(i);
                    if (replyModel != null) {
                        if (!MCStringUtil.isEmpty(replyModel.getIcon())) {
                            iconList.add(replyModel.getIcon());
                        }
                        if (!(replyModel.getReplyContent() == null || replyModel.getReplyContent().isEmpty())) {
                            for (TopicContentModel replyContent : replyModel.getReplyContent()) {
                                if (replyContent.getType() == 1) {
                                    model = new RichImageModel();
                                    model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl(replyContent.getOriginalInfo(), FinalConstant.RESOLUTION_BIG));
                                    richImageModelList.add(model);
                                }
                            }
                        }
                    }
                }
                i++;
            }
        }
        return richImageModelList;
    }

    public void share(int type) {
        if (this.postsModel != null) {
            MCShareModel shareModel = new MCShareModel();
            shareModel.setTitle(this.postsModel.getTitle());
            shareModel.setContent(getShareContent(this.postsModel));
            shareModel.setType(3);
            if (this.postsModel.getContent() != null && !this.postsModel.getContent().isEmpty()) {
                for (TopicContentModel model : this.postsModel.getContent()) {
                    if (model.getType() == 1) {
                        shareModel.setPicUrl(MCAsyncTaskLoaderImage.formatUrl(model.getOriginalInfo(), FinalConstant.RESOLUTION_SMALL));
                        break;
                    }
                }
            }
            shareModel.setLinkUrl(this.postsModel.getForumTopicUrl());
            shareModel.setSkipUrl(this.postsModel.getForumTopicUrl());
            shareModel.setDownloadUrl(this.resource.getString("mc_share_download_url"));
            HashMap<String, String> params = new HashMap();
            params.put("topicId", String.valueOf(this.postsModel.getTopicId()));
            params.put(MCShareConstant.PARAM_SHARE_FROM, MCShareConstant.FROM_PHP);
            params.put("baseUrl", this.resource.getString("mc_discuz_base_request_url"));
            params.put("style", String.valueOf(1));
            params.put(MCShareConstant.PARAM_CONTENT_TYPE, "topic");
            params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
            shareModel.setParams(params);
            switch (type) {
                case 1:
                    MCForumLaunchShareHelper.shareToWeChat(this.context, shareModel);
                    return;
                case 2:
                    MCForumLaunchShareHelper.shareToMoments(this.context, shareModel);
                    return;
                case 3:
                    MCForumLaunchShareHelper.share(this.context, shareModel);
                    return;
                default:
                    MCForumLaunchShareHelper.share(this.context, shareModel);
                    return;
            }
        }
    }

    private String getShareContent(PostsModel postsModel) {
        String shareContent = "";
        if (postsModel.getContent() == null) {
            return shareContent;
        }
        for (int i = 0; i < postsModel.getContent().size(); i++) {
            TopicContentModel contentModel = (TopicContentModel) postsModel.getContent().get(i);
            if (contentModel.getType() == 0) {
                shareContent = shareContent + contentModel.getInfor();
            }
        }
        if (shareContent.length() > 70) {
            return shareContent.substring(0, 70);
        }
        return shareContent;
    }

    public void onDestroy() {
        if (this.builder != null) {
            this.builder = null;
        }
        if (this.postsMoreDialog != null) {
            this.postsMoreDialog = null;
        }
    }
}
