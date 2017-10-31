package com.mobcent.discuz.module.topic.detail.fragment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.BufferType;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.DZAdPositionHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseMiddleHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsGroupHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsLastHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseReplyGroupHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseReplyLastHolder;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi"})
public class TopicDetail2FragmentAdapter extends TopicDetailGroupAdapter {
    protected MCAudioUtils audioUtils;
    private String[] items;
    private AlertDialog longClickDialog;

    public TopicDetail2FragmentAdapter(Context context, List<Object> detailList, String TAG) {
        super(context, detailList);
        this.TAG = TAG;
        this.audioUtils = MCAudioUtils.getInstance(context.getApplicationContext());
    }

    public String getPostsGroupLayoutName() {
        return "topic_detail2_posts_group_item";
    }

    public String getReplyGroupLayoutName() {
        return "topic_detail2_reply_group_item";
    }

    public String getPostsLastLayoutName() {
        return "topic_detail2_posts_last_item";
    }

    public String getReplyLastLayoutName() {
        return "topic_detail2_reply_last_item";
    }

    public String getContentModelLayoutName() {
        return "topic_detail2_middle_item";
    }

    protected String currStyle() {
        return "card";
    }

    public void updatePostsGroupView(BasePostsGroupHolder holder, PostsModel postsModel) {
        if ("vote".equals(postsModel.getType())) {
            holder.getPollImg().setVisibility(0);
        } else {
            holder.getPollImg().setVisibility(8);
        }
        if (postsModel.getEssence() == 1) {
            holder.getEssenceImg().setVisibility(0);
        } else {
            holder.getEssenceImg().setVisibility(8);
        }
        if (postsModel.getTop() == 1) {
            holder.getTopImg().setVisibility(0);
        } else {
            holder.getTopImg().setVisibility(8);
        }
        holder.getTitleText().setText(postsModel.getTitle());
        holder.getReplyCountText().setText(postsModel.getReplies() + "");
        holder.getScanCountText().setText(postsModel.getHits() + "");
        holder.getTimeText().setText(MCDateUtil.getFormatTimeByWord(this.resource, postsModel.getCreateDate(), "yyyy-MM-dd HH:mm"));
        if (MCStringUtil.isEmpty(postsModel.getIcon())) {
            holder.getUserImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            initIcon(holder.getUserImg(), postsModel.getIcon(), true);
        }
        holder.getUserNameText().setText(postsModel.getUserNickName());
        holder.getUserRoleText().setText(showUserRelation(postsModel.isFollow(), postsModel.getUserId()));
        holder.getUserTileText().setText(postsModel.getUserTitle());
        if (postsModel.getActivityInfo() == null || MCStringUtil.isEmpty(postsModel.getActivityInfo().getType())) {
            holder.getActivityTopicBox().setVisibility(8);
        } else {
            updatePostsActivityView(postsModel.getActivityInfo(), holder, postsModel.getTopicId());
        }
        iconAction(holder.getUserImg(), postsModel.getUserId(), postsModel.getUserNickName(), postsModel.isFollow());
    }

    public void updateReplyGroupView(BaseReplyGroupHolder holder, ReplyModel replyModel) {
        if (MCStringUtil.isEmpty(replyModel.getIcon())) {
            holder.getUserImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            initIcon(holder.getUserImg(), replyModel.getIcon(), false);
        }
        holder.getUserNameText().setText(replyModel.getReplyName());
        holder.getUserRoleText().setText(showUserRelation(replyModel.isFollow(), replyModel.getReplyUserId()));
        holder.getUserTileText().setText(replyModel.getUserTitle());
        if (replyModel.getPosition() > 0) {
            holder.getReplyLabText().setVisibility(0);
            holder.getReplyLabText().setText(replyModel.getPosition() + this.resource.getString("mc_froum_posts_floor"));
        } else {
            holder.getReplyLabText().setVisibility(8);
        }
        iconAction(holder.getUserImg(), replyModel.getReplyUserId(), replyModel.getReplyName(), replyModel.isFollow());
    }

    public void updateReplyLastView(BaseReplyLastHolder holder, ReplyModel replyModel) {
        if (replyModel.getIsQuote() == 1) {
            holder.getQuoteText().setVisibility(0);
            String quote = replyModel.getQuoteContent();
            holder.getQuoteText().setText(quote, BufferType.SPANNABLE);
            DZFaceUtil.setStrToFace(holder.getQuoteText(), quote, this.context.getApplicationContext());
        } else {
            holder.getQuoteText().setVisibility(8);
        }
        if (MCStringUtil.isEmpty(replyModel.getMobileSign())) {
            holder.getSignText().setVisibility(8);
        } else {
            holder.getSignText().setText(replyModel.getMobileSign());
        }
        if (MCStringUtil.isEmpty(replyModel.getLocation())) {
            holder.getLocationBox().setVisibility(8);
        } else {
            holder.getLocationBox().setVisibility(0);
            holder.getLocationText().setText(replyModel.getLocation());
        }
        holder.getReplyTimeText().setText(MCDateUtil.getFormatTimeByWord(this.resource, replyModel.getPostsDate(), "yyyy-MM-dd HH:mm"));
        if (this.sDb.getdiscusVersion().equals(FinalConstant.MC_DISCUZ_VERSION_20)) {
            holder.getMoreBox().setVisibility(8);
        } else {
            holder.getMoreBtn().setTag(getManagePanelList(replyModel.getReplyUserId(), replyModel.getManagePanel()));
        }
        MCTouchUtil.createTouchDelegate(holder.getPraiseBtn(), 10);
        MCTouchUtil.createTouchDelegate(holder.getMoreBtn(), 10);
        initSupportView(replyModel.getExtraPanel(), holder.getPraiseBox(), holder.getPraiseBtn(), holder.getPraiseText());
        if (this.postsModel != null) {
            supportAction(this.postsModel.getTopicId(), replyModel.getReplyPostsId(), "post", replyModel.getExtraPanel(), holder.getPraiseBtn(), holder.getPraiseText());
            replyAction(holder.getReplyBtn(), this.postsModel.getTopicId(), replyModel.getReplyPostsId());
        }
        moreAction(holder.getMoreBtn(), replyModel.getReplyPostsId(), replyModel.getReplyUserId(), replyModel.getManagePanel());
        if (replyModel.getPosition() > 0) {
            holder.getMoreBox().setVisibility(0);
            holder.getReplyBox().setVisibility(0);
            return;
        }
        holder.getMoreBox().setVisibility(8);
        holder.getReplyBox().setVisibility(8);
    }

    public void updatePostsLastView(BasePostsLastHolder holder, PostsModel postsModel) {
        updatePostsPollView(postsModel, holder);
        if (MCStringUtil.isEmpty(postsModel.getLocation())) {
            holder.getLocationBox().setVisibility(8);
        } else {
            holder.getLocationBox().setVisibility(0);
            holder.getLocationText().setText(postsModel.getLocation());
        }
        if (this.sDb.getdiscusVersion().equals(FinalConstant.MC_DISCUZ_VERSION_20)) {
            holder.getMoreBox().setVisibility(8);
        } else {
            initPostsMoreDialog(postsModel.getTopicId(), postsModel.getUserId(), postsModel.getManagePanel());
        }
        if (MCStringUtil.isEmpty(postsModel.getMobileSign())) {
            holder.getSignText().setVisibility(8);
        } else {
            holder.getSignText().setText(postsModel.getMobileSign());
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_wechat_appid").trim()) || this.resource.getString("mc_wechat_appid").trim().startsWith("{")) {
            holder.getShareBox().setVisibility(8);
        } else {
            holder.getShareBox().setVisibility(0);
        }
        holder.getShareToMoments().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TopicDetail2FragmentAdapter.this.share(2);
            }
        });
        holder.getShareToWeChat().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TopicDetail2FragmentAdapter.this.share(1);
            }
        });
        MCTouchUtil.createTouchDelegate(holder.getPraiseBtn(), 10);
        MCTouchUtil.createTouchDelegate(holder.getMoreBtn(), 10);
        initSupportView(postsModel.getExtraPanel(), holder.getPraiseBox(), holder.getPraiseBtn(), holder.getPraiseText());
        initRateView(holder);
        supportAction(postsModel.getTopicId(), postsModel.getPostsId(), "topic", postsModel.getExtraPanel(), holder.getPraiseBtn(), holder.getPraiseText());
        moreAction(holder.getMoreBtn(), postsModel.getTopicId(), postsModel.getUserId(), postsModel.getManagePanel());
    }

    private void initIcon(ImageView iconImg, String icon, boolean isTopic) {
        iconImg.setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        if (!MCStringUtil.isEmpty(icon)) {
            updateImageView(MCAsyncTaskLoaderImage.formatUrl(icon, FinalConstant.RESOLUTION_SMALL), iconImg, isTopic);
        }
    }

    private String showUserRelation(boolean follow, long userId) {
        String text = "";
        if (userId == this.currentUserId || !follow) {
            return text;
        }
        return this.resource.getString("mc_forum_friend");
    }

    public void updateContentModelView(BaseMiddleHolder holder, TopicContentModel contentModel, boolean isTopic) {
        modifyContentView(holder, contentModel, isTopic);
        final TopicContentModel topicContentModel;
        SpannableString sp;
        switch (contentModel.getType()) {
            case -1:
                holder.getAdView().setImgAdWidth(MCPhoneUtil.getDisplayWidth(this.context) - (MCPhoneUtil.dip2px(this.context, 23.0f) * 2));
                holder.getAdView().showAd(this.TAG, DZAdPositionHelper.getMainPostPosition(this.context), "0");
                return;
            case 0:
                if (isTopic) {
                    holder.getTextView().setTextSize(0, this.context.getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_18")));
                } else {
                    holder.getTextView().setTextSize(0, this.context.getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_16")));
                }
                if (MCStringUtil.isEmpty(contentModel.getInfor())) {
                    holder.getTextView().setText("");
                    return;
                }
                holder.getTextView().setText(contentModel.getInfor());
                DZFaceUtil.setStrToFace(holder.getTextView(), contentModel.getInfor(), this.context.getApplicationContext());
                if (VERSION.SDK_INT >= 16) {
                    holder.getTextView().setTextIsSelectable(true);
                    return;
                }
                topicContentModel = contentModel;
                holder.getTextView().setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if (TopicDetail2FragmentAdapter.this.longClickDialog == null) {
                            TopicDetail2FragmentAdapter.this.items = new String[]{TopicDetail2FragmentAdapter.this.resource.getString("mc_forum_copy_content")};
                            TopicDetail2FragmentAdapter.this.longClickDialog = new Builder(TopicDetail2FragmentAdapter.this.context).setItems(TopicDetail2FragmentAdapter.this.items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (TopicDetail2FragmentAdapter.this.resource.getString("mc_forum_copy_content").equals(TopicDetail2FragmentAdapter.this.items[which])) {
                                        ((ClipboardManager) TopicDetail2FragmentAdapter.this.context.getSystemService("clipboard")).setText(topicContentModel.getInfor());
                                        MCToastUtils.toastByResName(TopicDetail2FragmentAdapter.this.context.getApplicationContext(), "mc_forum_tel_copy_to_clipboard");
                                    }
                                }
                            }).create();
                            TopicDetail2FragmentAdapter.this.longClickDialog.setCanceledOnTouchOutside(true);
                        }
                        TopicDetail2FragmentAdapter.this.longClickDialog.show();
                        return true;
                    }
                });
                return;
            case 1:
                String smallImageUrl = MCAsyncTaskLoaderImage.formatUrl(contentModel.getInfor(), FinalConstant.RESOLUTION_BIG);
                LayoutParams layoutParams;
                int topMargins;
                if (MCStringUtil.isEmpty(smallImageUrl)) {
                    int leftRightMargins;
                    layoutParams = new LayoutParams(MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 45.0f), MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 45.0f));
                    if ("card".equals(currStyle())) {
                        leftRightMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 8.0f);
                        topMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 10.0f);
                    } else {
                        leftRightMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 15.0f);
                        topMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 15.0f);
                    }
                    layoutParams.setMargins(leftRightMargins, topMargins, leftRightMargins, 0);
                    holder.getImgView().setLayoutParams(layoutParams);
                    holder.getImgView().setImageResource(this.resource.getDrawableId("mc_forum_add_new_img"));
                } else {
                    holder.getImgView().setTag(Boolean.valueOf(true));
                    holder.getImgView().setImageResource(this.resource.getDrawableId("mc_forum_add_new_img"));
                    if (this.sizeMap.get(smallImageUrl) != null) {
                        int rightMargins;
                        int leftMargins;
                        layoutParams = new LayoutParams(((Integer[]) this.sizeMap.get(smallImageUrl))[0].intValue(), ((Integer[]) this.sizeMap.get(smallImageUrl))[1].intValue());
                        if ("card".equals(currStyle())) {
                            rightMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 8.0f);
                            leftMargins = rightMargins;
                            topMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 10.0f);
                        } else {
                            rightMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 15.0f);
                            leftMargins = rightMargins;
                            topMargins = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 15.0f);
                        }
                        layoutParams.setMargins(leftMargins, topMargins, rightMargins, 0);
                        holder.getImgView().setLayoutParams(layoutParams);
                    }
                    updateImageView(smallImageUrl, holder.getImgView(), isTopic);
                }
                final boolean z = isTopic;
                final TopicContentModel topicContentModel2 = contentModel;
                holder.getImgView().setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        ArrayList<RichImageModel> tempRichList;
                        if (z) {
                            tempRichList = TopicDetail2FragmentAdapter.this.topicRichList;
                        } else {
                            tempRichList = TopicDetail2FragmentAdapter.this.replyRichList;
                        }
                        ImagePreviewHelper.getInstance().startImagePreview((Activity) TopicDetail2FragmentAdapter.this.context, tempRichList, MCAsyncTaskLoaderImage.formatUrl(topicContentModel2.getOriginalInfo(), FinalConstant.RESOLUTION_BIG), new ImageViewerListener() {
                            public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                                MCShareModel shareModel = new MCShareModel();
                                shareModel.setPicUrl(imgModel.getImageUrl());
                                shareModel.setImageFilePath(localPath);
                                shareModel.setDownloadUrl(TopicDetail2FragmentAdapter.this.resource.getString("mc_share_download_url"));
                                shareModel.setType(1);
                                MCForumLaunchShareHelper.share(context, shareModel);
                            }
                        });
                    }
                });
                return;
            case 2:
                topicContentModel = contentModel;
                holder.getVideoView().setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        if (!MCStringUtil.isEmpty(topicContentModel.getInfor())) {
                            Intent intent = new Intent(TopicDetail2FragmentAdapter.this.context, WebViewFragmentActivity.class);
                            intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TYPE, "video");
                            intent.putExtra("webViewUrl", topicContentModel.getInfor());
                            TopicDetail2FragmentAdapter.this.context.startActivity(intent);
                        }
                    }
                });
                return;
            case 3:
                holder.getPlayAudioImg().setImageResource(this.resource.getDrawableId("mc_forum_voice_chat_play"));
                holder.getPlayIngImg().setImageResource(this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                holder.getAudioLayout().setTag(contentModel.getInfor());
                final BaseMiddleHolder baseMiddleHolder = holder;
                holder.getAudioLayout().setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        if (baseMiddleHolder.getAudioLayout().getTag() != null && baseMiddleHolder.getAudioLayout().getTag().equals(TopicDetail2FragmentAdapter.this.audioUtils.getCurrentAudioUrl()) && TopicDetail2FragmentAdapter.this.audioUtils.isPlaying()) {
                            TopicDetail2FragmentAdapter.this.audioUtils.stopAudio();
                        } else {
                            TopicDetail2FragmentAdapter.this.audioUtils.playAudio((String) baseMiddleHolder.getAudioLayout().getTag());
                        }
                    }
                });
                return;
            case 4:
                if (MCStringUtil.isEmpty(contentModel.getInfor())) {
                    holder.getTextView().setText("");
                    return;
                }
                try {
                    sp = new SpannableString(contentModel.getInfor());
                    if (contentModel.getUrl() != null && MCStringUtil.isUrl(contentModel.getUrl())) {
                        sp.setSpan(new URLSpan(contentModel.getUrl().toString()), 0, contentModel.getInfor().length(), 33);
                    }
                    holder.getTextView().setText(sp);
                } catch (Exception e) {
                }
                topicContentModel = contentModel;
                holder.getTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(TopicDetail2FragmentAdapter.this.context, WebViewFragmentActivity.class);
                        intent.putExtra("webViewUrl", topicContentModel.getUrl());
                        TopicDetail2FragmentAdapter.this.context.startActivity(intent);
                    }
                });
                return;
            case 5:
                if (MCStringUtil.isEmpty(contentModel.getInfor())) {
                    holder.getTextView().setText("");
                    return;
                }
                try {
                    sp = new SpannableString(contentModel.getInfor());
                    if (contentModel.getUrl() != null && MCStringUtil.isUrl(contentModel.getUrl())) {
                        sp.setSpan(new URLSpan(contentModel.getUrl().toString()), 0, contentModel.getInfor().length(), 33);
                    }
                    holder.getTextView().setText(sp);
                } catch (Exception e2) {
                }
                if (!MCStringUtil.isEmpty(contentModel.getDesc())) {
                    holder.getTextView().append(contentModel.getDesc());
                }
                topicContentModel = contentModel;
                holder.getTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(TopicDetail2FragmentAdapter.this.context, WebViewFragmentActivity.class);
                        intent.putExtra("webViewUrl", topicContentModel.getUrl());
                        TopicDetail2FragmentAdapter.this.context.startActivity(intent);
                    }
                });
                return;
            default:
                return;
        }
    }

    private void modifyContentView(BaseMiddleHolder holder, TopicContentModel contentModel, boolean isTopic) {
        int bottomPadding = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 5.0f);
        if (isTopic) {
            holder.getLayout().setPadding(0, 0, 0, bottomPadding);
            holder.getTextView().setTextSize(18.0f);
        } else {
            holder.getLayout().setPadding(MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 36.0f), 0, MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 15.0f), bottomPadding);
            holder.getTextView().setTextSize(16.0f);
        }
        holder.getVideoView().setVisibility(0);
        holder.getTextView().setVisibility(0);
        holder.getImgView().setVisibility(0);
        holder.getAudioLayout().setVisibility(0);
        holder.getAdView().setVisibility(0);
        if (!(contentModel.getType() == 0 || contentModel.getType() == 4 || contentModel.getType() == 5)) {
            holder.getTextView().setVisibility(8);
        }
        if (contentModel.getType() != 1) {
            holder.getImgView().setVisibility(8);
        }
        if (contentModel.getType() != 3) {
            holder.getAudioLayout().setVisibility(8);
        }
        if (contentModel.getType() != 2) {
            holder.getVideoView().setVisibility(8);
        }
        if (contentModel.getType() != -1) {
            holder.getAdView().setVisibility(8);
        }
    }
}
