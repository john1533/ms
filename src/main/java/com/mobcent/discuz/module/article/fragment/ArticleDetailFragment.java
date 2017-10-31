package com.mobcent.discuz.module.article.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCMultiBottomView;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnSendDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnShareDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.SendModel;
import com.mobcent.discuz.android.model.ArticleModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ContentModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.service.ArticleService;
import com.mobcent.discuz.android.service.DraftService;
import com.mobcent.discuz.android.service.impl.ArticleServiceImpl;
import com.mobcent.discuz.android.service.impl.DraftServiceImpl;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.module.article.fragment.activity.ArticleCommentListActivity;
import com.mobcent.discuz.module.article.fragment.adapter.ArticleDetailAdapter;
import com.mobcent.discuz.module.article.fragment.adapter.ArticleDetailAdapter.OnPageClickLisener;
import com.mobcent.discuz.module.article.fragment.adapter.holder.PostsAudioAdapterHolder;
import com.mobcent.discuz.module.article.fragment.task.ArticleCommentTask;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils.AudioPlayListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullListOnScrollListener;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragment extends BaseFragment implements FinalConstant, OnSendDelegate, OnShareDelegate, SlideDelegate {
    private int COMMENTList = 1;
    private ArticleDetailAdapter adapter;
    private long aid;
    private ArticleInfoTask articleInfoTask;
    private ArticleModel articleModel;
    private ArticleService articleService;
    private MCMultiBottomView commentBottomView;
    private TopicDraftModel draftModel;
    private DraftService draftService;
    private boolean isComment = false;
    private int page = 1;
    private PullToRefreshListView pullToRefreshListView;
    private char splitChar = FinalConstant.SPLIT_CHAR;
    private char tagImg = FinalConstant.TAG_IMG;

    private class ArticleInfoTask extends AsyncTask<Object, Void, BaseResultModel<ArticleModel>> {
        private ArticleInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ArticleDetailFragment.this.hideSoftKeyboard();
        }

        protected BaseResultModel<ArticleModel> doInBackground(Object... params) {
            return ArticleDetailFragment.this.articleService.getArticleDetail(((Long) params[0]).longValue(), ((Integer) params[1]).intValue());
        }

        protected void onPostExecute(BaseResultModel<ArticleModel> result) {
            super.onPostExecute(result);
            ArticleDetailFragment.this.pullToRefreshListView.onRefreshComplete();
            if (result != null) {
                ArticleDetailFragment.this.pullToRefreshListView.removeFooterLayout();
                ArticleDetailFragment.this.articleModel = (ArticleModel) result.getData();
                ArticleDetailFragment.this.adapter.setArticleModel((ArticleModel) result.getData());
                ArticleDetailFragment.this.adapter.setPage((long) ArticleDetailFragment.this.page);
                ArticleDetailFragment.this.adapter.notifyDataSetInvalidated();
                ArticleDetailFragment.this.adapter.notifyDataSetChanged();
                if (ArticleDetailFragment.this.articleModel == null || ArticleDetailFragment.this.articleModel.getAllowComment() != 0) {
                    ArticleDetailFragment.this.commentBottomView.setVisibility(0);
                } else {
                    ArticleDetailFragment.this.commentBottomView.setVisibility(8);
                }
            }
        }
    }

    protected String getRootLayoutName() {
        return "article_detail_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.aid = getBundle().getLong("aid");
        this.articleService = new ArticleServiceImpl(this.activity.getApplicationContext());
        this.articleModel = new ArticleModel();
        if (this.adapter == null) {
            this.adapter = new ArticleDetailAdapter(this.activity);
        }
        this.draftService = new DraftServiceImpl(this.activity.getApplicationContext());
        this.draftModel = (TopicDraftModel) getBundle().getSerializable(IntentConstant.INTENT_TOPIC_DRAFMODEL);
        if (this.draftModel == null) {
            this.draftModel = new TopicDraftModel();
            this.draftModel.setCommonId(this.aid);
            this.draftModel.setType(3);
            List draftList = this.draftService.queryDraftModel(this.draftModel);
            if (MCListUtils.isEmpty(draftList)) {
                this.draftModel = null;
            } else {
                this.draftModel = (TopicDraftModel) draftList.get(0);
            }
        }
    }

    protected void initViews(View rootView) {
        this.pullToRefreshListView = (PullToRefreshListView) findViewByName(rootView, "mc_forum_lv");
        this.pullToRefreshListView.setAdapter(this.adapter);
        this.commentBottomView = (MCMultiBottomView) findViewByName(rootView, "bottom_bar_box");
        this.commentBottomView.setOuterRightShow(new int[]{11});
        this.commentBottomView.setOuterLeftShow(new int[]{8});
        this.commentBottomView.setInnerShow(null);
        this.commentBottomView.setOnSendDelegate(this);
        this.commentBottomView.setOnShareDelegate(this);
        this.pullToRefreshListView.setScrollListener(new PausePullListOnScrollListener(ImageLoader.getInstance(), false, true));
        if (this.draftModel != null) {
            ((EditText) findViewByName(this.commentBottomView, "reply_edit")).setText(this.draftModel.getContent());
            this.aid = this.draftModel.getCommonId();
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = this.resource.getString("mc_forum_article_title");
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button2";
        if (this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle())) {
            topBtnModel.title = this.resource.getString("mc_forum_comment");
        } else {
            topSettingModel.title = this.moduleModel.getTitle();
        }
        topBtnModel.action = this.COMMENTList;
        rightModels.add(topBtnModel);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == ArticleDetailFragment.this.COMMENTList) {
                    Intent intent = new Intent(ArticleDetailFragment.this.activity, ArticleCommentListActivity.class);
                    intent.putExtra("aid", ArticleDetailFragment.this.aid);
                    intent.putExtra("allowComment", ArticleDetailFragment.this.articleModel.getAllowComment());
                    ArticleDetailFragment.this.activity.startActivity(intent);
                }
            }
        });
    }

    protected void initActions(View rootView) {
        this.pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                ArticleDetailFragment.this.getArticleTask();
            }
        });
        this.pullToRefreshListView.onRefresh();
        this.adapter.setOnPageClickLisener(new OnPageClickLisener() {
            public void onClick(boolean isNext) {
                if (isNext) {
                    ArticleDetailFragment.this.page = ArticleDetailFragment.this.page + 1;
                } else {
                    ArticleDetailFragment.this.page = ArticleDetailFragment.this.page - 1;
                }
                ArticleDetailFragment.this.getArticleTask();
            }
        });
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).registerListener(new AudioPlayListener() {
            public void onAudioStatusChange(String url, String path, int status, int percent) {
                int childCount = ArticleDetailFragment.this.pullToRefreshListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    PostsAudioAdapterHolder holder = ArticleDetailFragment.this.adapter.getAudioAdapterHolder();
                    if (holder != null && (holder instanceof PostsAudioAdapterHolder)) {
                        PostsAudioAdapterHolder mHolder = holder;
                        ImageView statusImg = mHolder.getPlayStautsImg();
                        ImageView playImg = mHolder.getPlayingImg();
                        if (!(statusImg == null || statusImg.getTag() == null || !statusImg.getTag().equals(url))) {
                            switch (status) {
                                case 1:
                                    statusImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_play"));
                                    mHolder.getDownProgressBar().show();
                                    break;
                                case 2:
                                    mHolder.getDownProgressBar().hide();
                                    statusImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_suspend"));
                                    switch (percent % 3) {
                                        case 0:
                                            playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img1"));
                                            break;
                                        case 1:
                                            playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img2"));
                                            break;
                                        case 2:
                                            playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img3"));
                                            break;
                                        default:
                                            playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                            break;
                                    }
                                case 3:
                                    statusImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_suspend"));
                                    mHolder.getDownProgressBar().hide();
                                    break;
                                case 6:
                                    mHolder.getDownProgressBar().hide();
                                    playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                    break;
                                case 7:
                                    mHolder.getDownProgressBar().hide();
                                    playImg.setImageResource(ArticleDetailFragment.this.resource.getDrawableId("mc_forum_voice_chat_img0"));
                                    Toast.makeText(ArticleDetailFragment.this.activity.getApplicationContext(), ArticleDetailFragment.this.resource.getString("mc_forum_audio_play_error"), 0).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void getArticleTask() {
        if (this.articleInfoTask != null) {
            this.articleInfoTask.cancel(true);
        }
        this.articleInfoTask = new ArticleInfoTask();
        this.articleInfoTask.execute(new Object[]{Long.valueOf(this.aid), Integer.valueOf(this.page)});
    }

    public void sendReply(int type, final SendModel sendModel) {
        if (!LoginHelper.doInterceptor(this.activity, null, null)) {
            return;
        }
        if (MCStringUtil.isEmpty(sendModel.getWordModel().getContent().toString())) {
            Toast.makeText(this.activity.getApplicationContext(), this.resource.getString("mc_forum_publish_min_length_error"), 1).show();
            this.isComment = false;
        } else if (!this.isComment) {
            this.isComment = true;
            new ArticleCommentTask(this.activity, new BaseRequestCallback<BaseResultModel<Object>>() {
                public void onPreExecute() {
                    ArticleDetailFragment.this.commentBottomView.hidePanel();
                }

                public void onPostExecute(BaseResultModel<Object> result) {
                    DZToastAlertUtils.toast(ArticleDetailFragment.this.activity, result);
                    if (result != null) {
                        if (result.getRs() == 1) {
                            if (result.getAlert() == 0) {
                                MCToastUtils.toastByResName(ArticleDetailFragment.this.activity.getApplicationContext(), "mc_forum_article_comment_sucessed");
                            }
                            ArticleDetailFragment.this.draftService.deleteDraftModel(ArticleDetailFragment.this.draftModel);
                        } else {
                            ArticleDetailFragment.this.saveDraftModel(sendModel.getWordModel().getContent());
                        }
                        ArticleDetailFragment.this.isComment = false;
                    }
                }
            }, new ArticleServiceImpl(this.activity).createCommentJson("reply", this.aid, "aid", sendModel.getWordModel().getContent().trim(), this.splitChar + "", this.tagImg + "", 0)).execute(new Void[0]);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).release();
        if (this.articleInfoTask != null) {
            this.articleInfoTask.cancel(true);
        }
        saveDraftModel(((EditText) findViewByName(this.commentBottomView, "reply_edit")).getText().toString());
    }

    private void saveDraftModel(String content) {
        if (this.draftModel != null || !TextUtils.isEmpty(content)) {
            if (this.draftModel != null) {
                this.draftModel.setContent(content);
            } else if (this.draftModel == null) {
                this.draftModel = new TopicDraftModel();
                this.draftModel.setCommonId(this.aid);
                this.draftModel.setType(3);
                this.draftModel.setContent(content);
                this.draftModel.setTitle(content);
            }
            this.draftService.saveDraftModel(this.draftModel);
        }
    }

    public void share(int type) {
        if (this.articleModel != null) {
            MCShareModel shareModel = new MCShareModel();
            shareModel.setTitle(this.articleModel.getTitle());
            shareModel.setContent(this.articleModel.getSummary());
            shareModel.setType(3);
            shareModel.setSkipUrl(this.articleModel.getAriticleUrl());
            if (this.articleModel != null && this.articleModel.getContentList() != null && !this.articleModel.getContentList().isEmpty()) {
                for (ContentModel model : this.articleModel.getContentList()) {
                    if ("image".equals(model.getType())) {
                        String str = "";
                        shareModel.setPicUrl(MCAsyncTaskLoaderImage.formatUrl(model.getSource(), FinalConstant.RESOLUTION_SMALL));
                        break;
                    }
                }
            }
            shareModel.setDownloadUrl(this.resource.getString("mc_share_download_url"));
            MCForumLaunchShareHelper.share(this.activity, shareModel);
        }
    }

    public boolean isSlideFullScreen() {
        if (this.commentBottomView.getFacePager() == null || this.commentBottomView.getFacePager().getVisibility() != 0) {
            return true;
        }
        return false;
    }
}
