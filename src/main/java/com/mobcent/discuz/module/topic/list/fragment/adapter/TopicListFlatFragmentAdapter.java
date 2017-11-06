package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

public class TopicListFlatFragmentAdapter extends BaseTopicListFragmentAdapter {
    public TopicListFlatFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        super(context, topicList, componentModel);
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel topicModel = topicList.get(position);
        if (topicModel instanceof AnnoModel) {
            return getAnnoConvertView(convertView, topicModel, true);
        }
        TopicListFragmentAdapterHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            if (this.summaryLength > 0) {
                if (this.imagePosition == 2) {
                    convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_flat_item"), null);
                } else {
                    convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_flat_left_image_item"), null);
                }
            } else if (this.imagePosition == 2) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_flat_none_desc_item"), null);
            } else {
                convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_flat_none_desc_left_image_item"), null);
            }
            holder = new TopicListFragmentAdapterHolder();
            initTopicAdapterHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        String signStr = "";
        String titleStr = "";
        holder.getNameTextView().setText(topicModel.getUserName());
        if (topicModel.getLastReplyDate() >= 0) {
            holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, topicModel.getLastReplyDate(), "yyyy-MM-dd HH:mm"));
        } else {
            holder.getTimeTextView().setText("");
        }
        holder.getReadTextView().setText(topicModel.getHits() + "");
        holder.getReplyTextView().setText(topicModel.getReplies() + "");
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
        if (this.summaryLength > 0) {
            holder.getDescTextView().setVisibility(0);
            holder.getDescTextView().setText(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength));

            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
                holder.getDescTextView().setText(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength));
            }else{//繁体
                holder.getDescTextView().setText(ChineseHelper.convertToTraditionalChinese(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength)));
            }

            String Str = "";
            double distance = topicModel.getDistance();
            if (distance > 0.0d) {
                holder.getLocationBox().setVisibility(0);
                holder.getLocationText().setVisibility(0);
                if (distance > 1000.0d) {
                    Str = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str2"), (((double) Math.round(distance / 10.0d)) / 100.0d) + "", this.context.getApplicationContext());
                } else {
                    Str = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str1"), (((double) Math.round(100.0d * distance)) / 100.0d) + "", this.context.getApplicationContext());
                }
                holder.getLocationText().setText(Str);
            } else {
                holder.getLocationText().setVisibility(8);
            }
        }
        if (this.titleLength > 0) {
            titleStr = MCStringUtil.subString(signStr + topicModel.getTitle(), this.titleLength);
            int signEndPosition = signStr.length();
            Log.v("RecordUtils","locale:"+ LowestManager.getInstance().getConfig().getCtr());
//            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
//                holder.getTitleTextView().setText(titleStr);
//            }else{//繁体
//                Log.v("RecordUtils","title:"+ ChineseHelper.convertToTraditionalChinese(titleStr));
//                holder.getTitleTextView().setText(ChineseHelper.convertToTraditionalChinese(titleStr));
//            }

            MCColorUtil.setTextViewPart(this.context, holder.getTitleTextView(), titleStr, 0, signEndPosition, "mc_forum_text6_normal_color");
        } else {
            holder.getTitleTextView().setText("");
        }
        if (topicModel.getPicPath() == null || topicModel.getPicPath().trim().equals("") || this.imagePosition == 0) {
            holder.getThumbnailView().setVisibility(8);
        } else {
            holder.getThumbnailView().setVisibility(0);
            displayThumbnailImage(false, topicModel.getPicPath(), (ImageView) holder.getThumbnailView());
        }
        holder.getTitleTextView().setTextColor(resource.getColorFromAttr(context.getTheme(),"topic_list_item_title_color"));

        if(position == 0){
            holder.getDivider().setVisibility(View.GONE);
//            holder.getDivider().setBackground(this.resource.getDrawableFromAttr(context.getTheme(),"topic_list_item_desc_color"));
//            topic_list_item_desc_color
        }else {
            holder.getDivider().setBackground(this.resource.getDrawableFromAttr(context.getTheme(),"topic_list_item_desc_color"));
        }
        setOnClickListener(convertView, topicModel);
        return convertView;
    }

    private void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_thumbnail_img")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_last_update_time_text")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_nickname_text")));


        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_title_text")));
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_reply_comments_text")));
        holder.setReplyButton((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_comments_img")));
        holder.setReadTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_reply_hit_text")));
        holder.setReadButton((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_hits_img")));
        holder.setTopImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_ding_img")));
        holder.setHighlightImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_jing_img")));
        holder.setDescTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_content")));
        holder.setLocationBox((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_location_box")));
        holder.setLocationText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_location_text")));
        holder.setDivider(convertView.findViewById(this.resource.getViewId("divider")));

    }
}
