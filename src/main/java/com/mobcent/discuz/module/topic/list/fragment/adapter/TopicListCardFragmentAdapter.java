package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.list.fragment.activity.TopicListActivty;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCColorUtil;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class TopicListCardFragmentAdapter extends BaseTopicListFragmentAdapter {
    public boolean isTopic = true;

    public TopicListCardFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel, boolean isTopic) {
        super(context, topicList, componentModel);
        this.isTopic = isTopic;
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel topicModel = topicList.get(position);
        if (this.isTopic) {
            convertView = getTopicView(convertView);
        } else {
            convertView = getPortalView(convertView);
        }
        TopicListFragmentAdapterHolder holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        String signStr = "";
        String titleStr = "";
        if (topicModel instanceof AnnoModel) {
            AnnoModel annoModel = (AnnoModel) topicModel;
            signStr = this.resource.getString("mc_forum_announce_mark");
            if (annoModel.getAnnoStartDate() >= 0) {
                holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, annoModel.getAnnoStartDate(), "yyyy-MM-dd HH:mm"));
            } else {
                holder.getTimeTextView().setText("");
            }
            holder.getNameTextView().setGravity(16);
            holder.getNameTextView().setText(annoModel.getAuthor());
            holder.getReplyBoxView().setVisibility(8);
            holder.getReadBoxView().setVisibility(8);
            holder.getThumbnailView().setVisibility(8);
        } else {
            if (topicModel.getLastReplyDate() >= 0) {
                holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, topicModel.getLastReplyDate(), "yyyy-MM-dd HH:mm"));
            } else {
                holder.getTimeTextView().setText("");
            }
            holder.getNameTextView().setText(topicModel.getUserName());
            holder.getReadTextView().setText(topicModel.getHits() + "");
            holder.getReplyTextView().setText(topicModel.getReplies() + "");
            if (this.isTopic) {
                holder.getReplyBoxView().setVisibility(0);
                holder.getReadBoxView().setVisibility(0);
                if (!(((Activity) this.context) instanceof TopicListActivty)) {
                    holder.getTopImageView().setVisibility(8);
                } else if (topicModel.getTop() == 1) {
                    holder.getTopImageView().setVisibility(0);
                } else {
                    holder.getTopImageView().setVisibility(8);
                }
                if (topicModel.getEssence() == 1) {
                    holder.getHighlightImageView().setVisibility(0);
                    holder.getTopImageView().setVisibility(8);
                } else {
                    holder.getHighlightImageView().setVisibility(8);
                }
                if (topicModel.getHot() == 1) {
                    signStr = signStr + this.resource.getString("mc_forum_hot_posts_mark");
                }
                if (topicModel.getVote() == 1) {
                    signStr = signStr + this.resource.getString("mc_forum_poll_mark");
                }
            }
            if (this.summaryLength > 0) {
                holder.getDescTextView().setVisibility(0);
                holder.getDescTextView().setText(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength));
                if (!MCStringUtil.isEmpty(topicModel.getLocation()) && topicModel.getDistance() >= 0.0d) {
                    holder.getLocationBox().setVisibility(0);
                    int distance = (int) (topicModel.getDistance() / 1000.0d);
                    String s = "";
                    if (distance == 0) {
                        s = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str1"), ((int) topicModel.getDistance()) + "", this.context);
                    } else {
                        s = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str2"), distance + "", this.context);
                    }
                    holder.getLocationText().setText(s);
                } else if (this.isTopic) {
                    holder.getLocationBox().setVisibility(8);
                }
            }
            titleStr = MCStringUtil.subString(signStr + topicModel.getTitle(), this.titleLength);
            int signEndPosition = signStr.length();
//            holder.getTitleTextView().setText(titleStr);
            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
                holder.getTitleTextView().setText(titleStr);
            }else{//繁体
                holder.getTitleTextView().setText(ChineseHelper.convertToTraditionalChinese(titleStr));
            }
            MCColorUtil.setTextViewPart(this.context, holder.getTitleTextView(), titleStr, 0, signEndPosition, "mc_forum_text6_normal_color");
            holder.getThumbnailView().setBackgroundResource(this.resource.getDrawableId("mc_forum_add_new_img"));
            if (topicModel.getPicPath() == null || topicModel.getPicPath().trim().equals("") || this.imagePosition == 0) {
                holder.getThumbnailView().setVisibility(8);
            } else {
                holder.getThumbnailView().setVisibility(0);
                displayThumbnailImage(false, topicModel.getPicPath(), (ImageView) holder.getThumbnailView());
            }
        }
        setOnClickListener(convertView, topicModel);
        return convertView;
    }

    private View getTopicView(View convertView) {
        if (convertView != null && (convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            return convertView;
        }
        if (this.summaryLength > 0) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_card_item"), null);
        } else if (this.imagePosition == 2) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_card_none_desc_item"), null);
        } else {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_card_none_desc_left_image_item"), null);
        }
        TopicListFragmentAdapterHolder holder = new TopicListFragmentAdapterHolder();
        initTopicAdapterHolder(convertView, holder);
        convertView.setTag(holder);
        return convertView;
    }

    private View getPortalView(View convertView) {
        if (convertView != null && (convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            return convertView;
        }
        if (this.summaryLength > 0) {
            if (this.imagePosition == 1) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("portal_topic_list_card_left_image_item"), null);
            } else {
                convertView = this.inflater.inflate(this.resource.getLayoutId("portal_topic_list_card_item"), null);
            }
        } else if (this.imagePosition == 1) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("portal_topic_list_card_no_desc_left_image_item"), null);
        } else {
            convertView = this.inflater.inflate(this.resource.getLayoutId("portal_topic_list_card_no_desc_item"), null);
        }
        TopicListFragmentAdapterHolder holder = new TopicListFragmentAdapterHolder();
        initPortalAdapterHolder(convertView, holder);
        convertView.setTag(holder);
        return convertView;
    }

    private void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_title")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_board_name")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_time")));
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_thumbnail_img")));
        holder.setHeadImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_top_icon_img")));
        holder.setHighlightImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_essence_icon_img")));
        holder.setDescTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_content")));
        holder.setLocationBox((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_location_box")));
        holder.setLocationText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_location_text")));
        holder.setReplyBoxView((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_conmment_times_box")));
        holder.setReadBoxView((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_read_times_box")));
        holder.setTopImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_top_icon_img")));
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_conmment_times")));
        holder.setReadTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_read_times")));
    }

    private void initPortalAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_title_text")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_portal_user")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_portal_time")));
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_thumbnail_img")));
        holder.setDescTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_subject_text")));
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_reply_num")));
        holder.setReadTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_comment_num")));
    }
}
