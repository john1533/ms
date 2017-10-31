package com.mobcent.discuz.module.msg.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.CommentAtListFragmentAdapterHolder;
import com.mobcent.discuz.module.publish.fragment.activity.ReplyTopicActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.List;

public abstract class BaseCommentAtListFragmentAdapter extends BaseListAdatper<CommentAtModel, CommentAtListFragmentAdapterHolder> {
    protected static final int COMMON_IMAGE = 2;
    protected static final int ICON_IMAGE = 1;
    protected static final int PAGE_FROM_AT = 2;
    protected static final int PAGE_FROM_COMMENT = 1;
    protected ConfigComponentModel componentModel;

    public BaseCommentAtListFragmentAdapter(Context context, List<CommentAtModel> list, ConfigComponentModel componentModel) {
        super(context, list);
        this.componentModel = componentModel;
    }

    protected void initViews(View convertView, CommentAtListFragmentAdapterHolder holder) {
        holder.setIconImg((MCHeadIcon) convertView.findViewById(this.resource.getViewId("user_icon_img")));
        holder.setNameText((TextView) convertView.findViewById(this.resource.getViewId("user_name_text")));
        holder.setTimeText((TextView) convertView.findViewById(this.resource.getViewId("time_text")));
        holder.setReplyLayout((LinearLayout) convertView.findViewById(this.resource.getViewId("reply_layout")));
        holder.setReplyCotentText((TextView) convertView.findViewById(this.resource.getViewId("reply_content_text")));
        holder.setReplyUrlImg((ImageView) convertView.findViewById(this.resource.getViewId("reply_url_img")));
        holder.setTopicLayout((LinearLayout) convertView.findViewById(this.resource.getViewId("topic_layout")));
        holder.setTopicCotentText((TextView) convertView.findViewById(this.resource.getViewId("topic_content_text")));
        holder.setTopicUrlImg((ImageView) convertView.findViewById(this.resource.getViewId("topic_url_img")));
        holder.setCheckBtn((Button) convertView.findViewById(this.resource.getViewId("check_btn")));
        holder.setReplyBtn((Button) convertView.findViewById(this.resource.getViewId("reply_btn")));
    }

    protected void initViewDatas(CommentAtListFragmentAdapterHolder holder, final CommentAtModel commentAtModel, int position) {
        if (MCStringUtil.isEmpty(commentAtModel.getIconUrl())) {
            holder.getIconImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            updateImage(holder.getIconImg(), commentAtModel.getIconUrl(), 1);
        }
        holder.getNameText().setText(commentAtModel.getReplyUserName());
        holder.getTimeText().setText(MCDateUtil.getFormatTimeByWord(this.resource, Long.valueOf(commentAtModel.getTime()).longValue(), "yyyy-MM-dd HH:mm"));
        if (MCStringUtil.isEmpty(commentAtModel.getReplyContent()) && MCStringUtil.isEmpty(commentAtModel.getReplyUrl())) {
            holder.getReplyLayout().setVisibility(8);
        } else {
            holder.getReplyLayout().setVisibility(0);
            if (MCStringUtil.isEmpty(commentAtModel.getReplyContent())) {
                holder.getReplyCotentText().setVisibility(8);
            } else {
                holder.getReplyCotentText().setVisibility(0);
                DZFaceUtil.setStrToFace(holder.getReplyCotentText(), commentAtModel.getReplyContent(), this.context.getApplicationContext());
            }
            if (MCStringUtil.isEmpty(commentAtModel.getReplyUrl())) {
                holder.getReplyUrlImg().setImageBitmap(null);
                holder.getReplyUrlImg().setVisibility(8);
            } else {
                holder.getReplyUrlImg().setVisibility(0);
                updateImage(holder.getReplyUrlImg(), commentAtModel.getReplyUrl(), 2);
            }
        }
        if (MCStringUtil.isEmpty(commentAtModel.getTopicContent()) && MCStringUtil.isEmpty(commentAtModel.getTopicUrl())) {
            holder.getTopicLayout().setVisibility(8);
        } else {
            holder.getTopicLayout().setVisibility(0);
            if (MCStringUtil.isEmpty(commentAtModel.getTopicContent())) {
                holder.getTopicCotentText().setVisibility(8);
            } else {
                holder.getTopicCotentText().setVisibility(0);
                DZFaceUtil.setStrToFace(holder.getTopicCotentText(), commentAtModel.getTopicContent(), this.context.getApplicationContext());
            }
            if (MCStringUtil.isEmpty(commentAtModel.getTopicUrl())) {
                holder.getTopicUrlImg().setImageBitmap(null);
                holder.getTopicUrlImg().setVisibility(8);
            } else {
                holder.getTopicUrlImg().setVisibility(0);
                updateImage(holder.getTopicUrlImg(), commentAtModel.getTopicUrl(), 2);
            }
        }
        holder.getCheckBtn().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BaseCommentAtListFragmentAdapter.this.context.getApplicationContext(), TopicDetailActivity.class);
                intent.putExtra("boardId", commentAtModel.getBoardId());
                intent.putExtra("boardName", commentAtModel.getBoardName());
                intent.putExtra("topicId", commentAtModel.getTopicId());
                BaseCommentAtListFragmentAdapter.this.context.startActivity(intent);
            }
        });
        holder.getReplyBtn().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BaseCommentAtListFragmentAdapter.this.context, ReplyTopicActivity.class);
                intent.putExtra("boardId", commentAtModel.getBoardId());
                intent.putExtra("topicId", commentAtModel.getTopicId());
                intent.putExtra("boardName", commentAtModel.getBoardName());
                intent.putExtra("toReplyId", commentAtModel.getReplyRemindId());
                intent.putExtra(IntentConstant.INTENT_IS_QUITE, true);
                BaseCommentAtListFragmentAdapter.this.context.startActivity(intent);
            }
        });
    }

    protected CommentAtListFragmentAdapterHolder instanceHolder() {
        return new CommentAtListFragmentAdapterHolder();
    }

    private void updateImage(final ImageView imageView, String imgUrl, final int type) {
        if (new SettingSharePreference(this.context).isPicAvailable()) {
            ImageViewAware imgAware = new ImageViewAware(imageView);
            ImageLoader.getInstance().loadImage(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL), new ImageSize(imgAware.getWidth(), imgAware.getHeight()), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, final Bitmap bitmap) {
                    super.onLoadingComplete(imageUri, view, bitmap);
                    imageView.post(new Runnable() {
                        public void run() {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                imageView.setImageBitmap(bitmap);
                            } else if (type == 1) {
                                imageView.setImageBitmap(MCHeadIcon.getHeadIconBitmap(BaseCommentAtListFragmentAdapter.this.context));
                            }
                        }
                    });
                }
            });
        }
    }
}
