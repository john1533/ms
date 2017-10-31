package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class TopicListNeteaseNewsFragmentAdapter extends BaseTopicListFragmentAdapter {
    public TopicListNeteaseNewsFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        super(context, topicList, componentModel);
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel topicModel = topicList.get(position);
        if (topicModel instanceof AnnoModel) {
            return getAnnoConvertView(convertView, topicModel, true);
        }
        convertView = getTopicView(convertView);
        TopicListFragmentAdapterHolder holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        if (this.titleLength > 0) {
            holder.getTitleTextView().setText(MCStringUtil.subString(topicModel.getTitle(), this.titleLength));
        }
        if (topicModel.getPicPath() == null || topicModel.getPicPath().trim().equals("") || topicModel.getPicPath().equals("null") || this.imagePosition == 0) {
            holder.getThumbnailView().setVisibility(8);
        } else {
            holder.getThumbnailView().setVisibility(0);
            holder.getThumbnailView().setTag(MCAsyncTaskLoaderImage.formatUrl(topicModel.getPicPath(), FinalConstant.RESOLUTION_SMALL));
            displayThumbnailImage(false, topicModel.getPicPath(), (ImageView) holder.getThumbnailView());
        }
        if (this.summaryLength > 0) {
            holder.getDescTextView().setText(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength));
        } else {
            holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, topicModel.getLastReplyDate(), "yyyy-MM-dd"));
            holder.getNameTextView().setText(topicModel.getUserName());
        }
        holder.getReplyTextView().setText(topicModel.getReplies() + this.resource.getString("mc_forum_reply"));
        setOnClickListener(convertView, topicModel);
        return convertView;
    }

    private View getTopicView(View convertView) {
        if (convertView != null && (convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            return convertView;
        }
        if (this.summaryLength > 0) {
            if (this.imagePosition == 1) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_neteasenews_left_image_item"), null);
            } else {
                convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_neteasenews_item"), null);
            }
        } else if (this.imagePosition == 1) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_neteasenews_none_desc_left_image_item"), null);
        } else {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_neteasenews_none_desc_item"), null);
        }
        TopicListFragmentAdapterHolder holder = new TopicListFragmentAdapterHolder();
        initTopicAdapterHolder(convertView, holder);
        convertView.setTag(holder);
        return convertView;
    }

    public void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_thumbnail_img")));
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_title_text")));
        if (this.summaryLength > 0) {
            holder.setDescTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_subject_text")));
        } else {
            holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_portal_time")));
            holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_portal_user")));
        }
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_reply_num")));
    }
}
