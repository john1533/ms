package com.mobcent.discuz.module.article.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.CommentModel;
import com.mobcent.discuz.android.model.ContentModel;
import com.mobcent.discuz.android.model.ManagePanelModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.article.fragment.adapter.holder.ArticleCommentListAdapterHolder;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticleCommentListAdapter extends BaseAdapter implements FinalConstant {
    private CommentLisener commentLisener;
    private List<CommentModel> commentList = new ArrayList();
    private Context context;
    private LayoutInflater inflater;
    private MCResource resource;

    public interface CommentLisener {
        void onCommentClick(CommentModel commentModel);
    }

    public ArticleCommentListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
    }

    public List<CommentModel> getCommentList() {
        return this.commentList;
    }

    public void setCommentList(List<CommentModel> commentList) {
        this.commentList = commentList;
    }

    public int getCount() {
        return this.commentList.size();
    }

    public Object getItem(int position) {
        return this.commentList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CommentModel replyMolde = (CommentModel) this.commentList.get(position);
        convertView = getConverView(convertView);
        updateCommentView(replyMolde, (ArticleCommentListAdapterHolder) convertView.getTag());
        return convertView;
    }

    private void updateCommentView(final CommentModel replyMolde, ArticleCommentListAdapterHolder holder) {
        holder.getUserNameText().setText(replyMolde.getUserNickName());
        holder.getTimeText().setText(replyMolde.getTime());
        updatePostsDetailView(replyMolde.getContentList(), holder.getReplyContentBox());
        if (MCStringUtil.isEmpty(replyMolde.getIcon())) {
            holder.getIcomImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            updateImageView(replyMolde.getIcon(), holder.getIcomImg());
        }
        List<ManagePanelModel> list = replyMolde.getManagePanelModelList();
        for (int i = 0; i < list.size(); i++) {
            if (((ManagePanelModel) list.get(i)).getType().equals("quote")) {
                holder.getReplyBox().setVisibility(0);
            }
        }
        holder.getIcomImg().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HashMap<String, Serializable> param = new HashMap();
                param.put("userId", Integer.valueOf(replyMolde.getCommentUserId()));
                if (LoginHelper.doInterceptor(ArticleCommentListAdapter.this.context, null, param)) {
                    Intent intent = new Intent(ArticleCommentListAdapter.this.context.getApplicationContext(), UserHomeActivity.class);
                    intent.putExtra("userId", (long) replyMolde.getCommentUserId());
                    ArticleCommentListAdapter.this.context.startActivity(intent);
                }
            }
        });
        holder.getReplyBox().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ArticleCommentListAdapter.this.commentLisener != null) {
                    ArticleCommentListAdapter.this.commentLisener.onCommentClick(replyMolde);
                }
            }
        });
    }

    private View getConverView(View convertView) {
        if (convertView != null) {
            return convertView;
        }
        convertView = this.inflater.inflate(this.resource.getLayoutId("article_comment_item"), null);
        ArticleCommentListAdapterHolder replyHolder = new ArticleCommentListAdapterHolder();
        initReplyListAdapterHolder(convertView, replyHolder);
        convertView.setTag(replyHolder);
        return convertView;
    }

    private LinearLayout updatePostsDetailView(List<ContentModel> topicContentList, LinearLayout topicContentLayout) {
        if (topicContentList != null && topicContentList.size() > 0) {
            topicContentLayout.setVisibility(0);
            topicContentLayout.removeAllViews();
            int j = topicContentList.size();
            for (int i = 0; i < j; i++) {
                ContentModel topicContent = (ContentModel) topicContentList.get(i);
                View view;
                TextView topicInfoText;
                if (topicContent.getIdType() == 0) {
                    view = this.inflater.inflate(this.resource.getLayoutId("article_text_item"), null);
                    topicInfoText = (TextView) view.findViewById(this.resource.getViewId("mc_forum_topic_info_text"));
                    if (!topicContent.getContent().equals("")) {
                        topicInfoText.setText(topicContent.getContent());
                    }
                    DZFaceUtil.setStrToFace(topicInfoText, topicContent.getContent(), this.context);
                    topicContentLayout.addView(view);
                } else if (topicContent.getIdType() == 10) {
                    view = this.inflater.inflate(this.resource.getLayoutId("article_text_item"), null);
                    topicInfoText = (TextView) view.findViewById(this.resource.getViewId("mc_forum_topic_info_text"));
                    if (!topicContent.getContent().equals("")) {
                        topicInfoText.setText(topicContent.getContent());
                    }
                    DZFaceUtil.setStrToFace(topicInfoText, topicContent.getContent(), this.context);
                    view.setBackgroundResource(this.resource.getDrawableId("mc_forum_reply_bg2"));
                    topicContentLayout.addView(view);
                }
            }
            topicContentLayout.setVisibility(0);
        }
        return topicContentLayout;
    }

    private void updateImageView(String imgUrl, ImageView imageView) {
        if (new SettingSharePreference(this.context).isPicAvailable()) {
            ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL), imageView);
        }
    }

    private void initReplyListAdapterHolder(View convertView, ArticleCommentListAdapterHolder holder) {
        holder.setIcomImg((MCHeadIcon) convertView.findViewById(this.resource.getViewId("mc_forum_reply_user_img")));
        holder.setUserNameText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_reply_user_text")));
        holder.setTimeText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_time_text")));
        holder.setReplyContentBox((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_reply_content_layout")));
        holder.setReplyBox((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_reply_btn_box")));
    }

    public CommentLisener getCommentLisener() {
        return this.commentLisener;
    }

    public void setCommentLisener(CommentLisener commentLisener) {
        this.commentLisener = commentLisener;
    }
}
