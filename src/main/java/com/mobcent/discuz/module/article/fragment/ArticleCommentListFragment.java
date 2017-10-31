package com.mobcent.discuz.module.article.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCMultiBottomView;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnSendDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.SendModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.service.ArticleService;
import com.mobcent.discuz.android.service.DraftService;
import com.mobcent.discuz.android.service.impl.ArticleServiceImpl;
import com.mobcent.discuz.android.service.impl.DraftServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.module.article.fragment.adapter.ArticleCommentListAdapter;
import com.mobcent.discuz.module.article.fragment.adapter.ArticleCommentListAdapter.CommentLisener;
import com.mobcent.discuz.module.article.fragment.task.ArticleCommentTask;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullListOnScrollListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArticleCommentListFragment extends BaseFragment implements FinalConstant, OnSendDelegate {
    private ArticleCommentListAdapter adapter;
    private int allowComment;
    private long artileId;
    private MCMultiBottomView bottomView;
    private ArrayList<CommentModel> commentList;
    private CommentListTask commentListTask;
    private TopicDraftModel draftModel;
    private DraftService draftService;
    private boolean isComment = false;
    private boolean isRefresh = true;
    private int page = 1;
    private int pageSize = 10;
    private PullToRefreshListView pullToRefreshListView;
    private long quoteId;
    private char splitChar = FinalConstant.SPLIT_CHAR;
    private char tagImg = FinalConstant.TAG_IMG;

    private class CommentListTask extends AsyncTask<Void, BaseResultModel<List<CommentModel>>, BaseResultModel<List<CommentModel>>> {
        private CommentListTask() {
        }

        protected BaseResultModel<List<CommentModel>> doInBackground(Void... params) {
            ArticleService articleService = new ArticleServiceImpl(ArticleCommentListFragment.this.activity.getApplicationContext());
            return articleService.commentList(articleService.creteCommentListJson(ArticleCommentListFragment.this.artileId, "aid", ArticleCommentListFragment.this.page, ArticleCommentListFragment.this.pageSize));
        }

        protected void onPostExecute(BaseResultModel<List<CommentModel>> result) {
            super.onPostExecute(result);
            if (ArticleCommentListFragment.this.isRefresh) {
                ArticleCommentListFragment.this.pullToRefreshListView.onRefreshComplete();
            }
            if (result == null) {
                ArticleCommentListFragment.this.pullToRefreshListView.onBottomRefreshComplete(3);
            } else if (MCStringUtil.isEmpty(result.getErrorCode()) || MCStringUtil.isEmpty(result.getErrorInfo())) {
                if (ArticleCommentListFragment.this.isRefresh) {
                    ArticleCommentListFragment.this.commentList.clear();
                    ArticleCommentListFragment.this.commentList.addAll((Collection) result.getData());
                } else {
                    ArticleCommentListFragment.this.commentList.addAll((Collection) result.getData());
                }
                ArticleCommentListFragment.this.adapter.setCommentList(ArticleCommentListFragment.this.commentList);
                if (ArticleCommentListFragment.this.isRefresh) {
                    ArticleCommentListFragment.this.adapter.notifyDataSetInvalidated();
                    ArticleCommentListFragment.this.adapter.notifyDataSetChanged();
                }
                if (result.getHasNext() == 1) {
                    ArticleCommentListFragment.this.pullToRefreshListView.onBottomRefreshComplete(0);
                } else {
                    ArticleCommentListFragment.this.pullToRefreshListView.onBottomRefreshComplete(3);
                }
                ArticleCommentListFragment.this.quoteId = 0;
                ArticleCommentListFragment.this.page = ArticleCommentListFragment.this.page + 1;
            } else {
                Toast.makeText(ArticleCommentListFragment.this.activity.getApplicationContext(), result.getErrorInfo(), 0).show();
                ArticleCommentListFragment.this.pullToRefreshListView.onBottomRefreshComplete(3);
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.artileId = getBundle().getLong("aid", 0);
        this.allowComment = getArguments().getInt("allowComment", 1);
        this.commentList = new ArrayList();
        if (this.adapter == null) {
            this.adapter = new ArticleCommentListAdapter(this.activity);
        }
        this.draftService = new DraftServiceImpl(this.activity.getApplicationContext());
        this.draftModel = new TopicDraftModel();
        this.draftModel.setCommonId(this.artileId);
        this.draftModel.setType(3);
        List draftList = this.draftService.queryDraftModel(this.draftModel);
        if (MCListUtils.isEmpty(draftList)) {
            this.draftModel = null;
        } else {
            this.draftModel = (TopicDraftModel) draftList.get(0);
        }
    }

    protected String getRootLayoutName() {
        return "article_comment_list_fragment";
    }

    protected void initViews(View rootView) {
        this.pullToRefreshListView = (PullToRefreshListView) findViewByName(rootView, "mc_forum_lv");
        this.bottomView = (MCMultiBottomView) findViewByName(rootView, "bottom_bar_box");
        this.bottomView.setOuterRightShow(new int[]{11});
        this.bottomView.setOuterLeftShow(null);
        this.bottomView.setInnerShow(null);
        this.bottomView.setOnSendDelegate(this);
        this.pullToRefreshListView.setAdapter(this.adapter);
        this.pullToRefreshListView.setScrollListener(new PausePullListOnScrollListener(ImageLoader.getInstance(), false, true));
        this.adapter.setCommentLisener(new CommentLisener() {
            public void onCommentClick(CommentModel model) {
                ArticleCommentListFragment.this.quoteId = (long) model.getCommentPostsId();
                ArticleCommentListFragment.this.bottomView.requestEditFocus();
                ArticleCommentListFragment.this.showSoftKeyboard();
            }
        });
        if (this.allowComment == 0) {
            this.bottomView.setVisibility(8);
        } else {
            this.bottomView.setVisibility(0);
        }
    }

    protected void initActions(View rootView) {
        this.pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                ArticleCommentListFragment.this.page = 1;
                ArticleCommentListFragment.this.isRefresh = true;
                ArticleCommentListFragment.this.commentListTask = new CommentListTask();
                ArticleCommentListFragment.this.commentListTask.execute(new Void[0]);
            }
        });
        this.pullToRefreshListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                ArticleCommentListFragment.this.isRefresh = false;
                ArticleCommentListFragment.this.commentListTask = new CommentListTask();
                ArticleCommentListFragment.this.commentListTask.execute(new Void[0]);
            }
        });
        this.pullToRefreshListView.onRefresh();
    }

    public void onResume() {
        super.onResume();
        if (this.draftModel != null) {
            ((EditText) findViewByName(this.bottomView, "reply_edit")).setText(this.draftModel.getContent());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.commentListTask != null) {
            this.commentListTask.cancel(true);
        }
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).release();
        saveDraftModel(((EditText) findViewByName(this.bottomView, "reply_edit")).getText().toString());
    }

    private void saveDraftModel(String content) {
        if (this.draftModel != null || !TextUtils.isEmpty(content)) {
            if (this.draftModel != null) {
                this.draftModel.setContent(content);
            } else if (this.draftModel == null) {
                this.draftModel = new TopicDraftModel();
                this.draftModel.setCommonId(this.artileId);
                this.draftModel.setType(3);
                this.draftModel.setTitle(content);
                this.draftModel.setContent(content);
            }
            this.draftService.saveDraftModel(this.draftModel);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_comment_list_title");
        dealTopBar(topSettingModel);
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
                    ArticleCommentListFragment.this.bottomView.hidePanel();
                }

                public void onPostExecute(BaseResultModel<Object> result) {
                    DZToastAlertUtils.toast(ArticleCommentListFragment.this.activity, result);
                    if (result == null) {
                        return;
                    }
                    if (result.getRs() == 1) {
                        ArticleCommentListFragment.this.isComment = false;
                        if (result.getAlert() == 0) {
                            MCToastUtils.toastByResName(ArticleCommentListFragment.this.activity.getApplicationContext(), "mc_forum_article_comment_sucessed");
                        }
                        ArticleCommentListFragment.this.pullToRefreshListView.onRefresh();
                        ArticleCommentListFragment.this.draftService.deleteDraftModel(ArticleCommentListFragment.this.draftModel);
                        return;
                    }
                    ArticleCommentListFragment.this.saveDraftModel(sendModel.getWordModel().getContent());
                }
            }, new ArticleServiceImpl(this.activity).createCommentJson("reply", this.artileId, "aid", sendModel.getWordModel().getContent(), this.splitChar + "", this.tagImg + "", (int) this.quoteId)).execute(new Void[0]);
        }
    }
}
