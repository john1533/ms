package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.mobcent.discuz.activity.view.MomentsView;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class TopicListWechatFragmentAdapter extends BaseTopicListFragmentAdapter {
    protected int gridViewWidth;

    public TopicListWechatFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        super(context, topicList, componentModel);
        this.gridViewWidth = (((MCPhoneUtil.getDisplayWidth(context) - MCPhoneUtil.dip2px(context, 10.0f)) - MCPhoneUtil.dip2px(context, 12.0f)) - MCPhoneUtil.dip2px(context, 30.0f)) - MCPhoneUtil.dip2px(context, 42.0f);
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel data = topicList.get(position);
        if (data instanceof AnnoModel) {
            return getAnnoConvertView(convertView, data, true);
        }
        TopicListFragmentAdapterHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_wechat_item"), null);
            holder = new TopicListFragmentAdapterHolder();
            initMomentsView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        holder.getPraiseButton().setVisibility(0);
        holder.getReplyButton().setVisibility(0);
        holder.getNameTextView().setText(data.getUserName());
        setGenderText(holder.getGenderImageView(), data.getGender());
        if (MCStringUtil.isEmpty(data.getLocation())) {
            holder.getLocationText().setVisibility(8);
        } else {
            holder.getLocationText().setText(data.getLocation());
        }
        if (MCStringUtil.isEmpty(data.getSubject()) || this.summaryLength <= 0) {
            holder.getDescTextView().setVisibility(8);
        } else {
            holder.getDescTextView().setVisibility(0);
            holder.getDescTextView().setText(MCStringUtil.subString(data.getSubject(), this.summaryLength));
        }
        if (this.titleLength <= 0) {
            holder.getTitleTextView().setVisibility(8);
        } else {
            holder.getTitleTextView().setVisibility(0);
//            holder.getTitleTextView().setText(MCStringUtil.subString(data.getTitle(), this.titleLength));
            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
                holder.getTitleTextView().setText((MCStringUtil.subString(data.getTitle(), this.titleLength)));
            }else{//繁体
                holder.getTitleTextView().setText(ChineseHelper.convertToTraditionalChinese((MCStringUtil.subString(data.getTitle(), this.titleLength))));
            }

        }
        if (data.getLastReplyDate() >= 0) {
            holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, data.getLastReplyDate(), "yyyy-MM-dd HH:mm"));
        } else {
            holder.getTimeTextView().setText("");
        }
        if (MCListUtils.isEmpty(data.getImageList())) {
            holder.getMomentsView().removeAllViews();
            holder.getMomentsView().setVisibility(8);
            holder.getMomentsView().setLayoutParams(holder.getMomentsView().getResetLayoutParams(0));
        } else {
            holder.getMomentsView().setVisibility(0);
            holder.getMomentsView().updateViews(data.getImageList());
        }
        initCommonView(holder, data);
        if (MCStringUtil.isEmpty(data.getSourceType()) || !data.getSourceType().equals("news")) {
            holder.getPraiseButton().setVisibility(0);
            holder.getPraiseTextView().setVisibility(0);
            praiseClick(holder.getPraiseButton(), holder.getPraiseTextView(), data);
        } else {
            holder.getPraiseButton().setVisibility(8);
            holder.getPraiseTextView().setVisibility(8);
        }
        holder.getPraiseTextView().setText(data.getRecommendAdd() + "");
        holder.getReplyTextView().setText(data.getReplies() + "");
        MCTouchUtil.createTouchDelegate(holder.getPraiseButton(), 10);
        MCTouchUtil.createTouchDelegate(holder.getReplyButton(), 10);
        setOnClickListener(convertView, data);
        return convertView;
    }

    private void initMomentsView(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setHeadImageView((ImageView) findViewByName(convertView, "user_icon_img"));
        holder.setNameTextView((TextView) findViewByName(convertView, "user_name_text"));
        holder.setDescTextView((TextView) findViewByName(convertView, "topic_content_text"));
        holder.setMomentsView((MomentsView) findViewByName(convertView, "topic_moments_view"));
        holder.getMomentsView().init(this.gridViewWidth, MCPhoneUtil.dip2px(this.context, 5.0f), MCPhoneUtil.dip2px(this.context, 5.0f));
        holder.setTimeTextView((TextView) findViewByName(convertView, "topic_time_text"));
        holder.setLocationText((TextView) findViewByName(convertView, "topic_location_text"));
        holder.setReplyButton((ImageButton) findViewByName(convertView, "reply_reply_btn"));
        holder.setPraiseTextView((TextView) findViewByName(convertView, "topic_praise_text"));
        holder.setPraiseButton((ImageButton) findViewByName(convertView, "topic_praise_btn"));
        holder.setGenderImageView((ImageView) findViewByName(convertView, "user_gender_text"));
        holder.setReplyTextView((TextView) findViewByName(convertView, "topic_reply_text"));
        holder.setTitleTextView((TextView) findViewByName(convertView, "topic_title_text"));
    }

    protected void initCommonView(TopicListFragmentAdapterHolder holder, TopicModel data) {
        holder.getHeadImageView().setImageBitmap(null);
        displayThumbnailImage(true, data.getIcon(), holder.getHeadImageView());
    }

    protected void praiseClick(final View btnView, final TextView textView, final TopicModel data) {
        btnView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LoginHelper.doInterceptor(TopicListWechatFragmentAdapter.this.context, null, null)) {
                    btnView.startAnimation(AnimationUtils.loadAnimation(TopicListWechatFragmentAdapter.this.context.getApplicationContext(), TopicListWechatFragmentAdapter.this.resource.getAnimId("dianzan_anim")));
                    new SupportAsyncTask(TopicListWechatFragmentAdapter.this.context, data.getTopicId(), 0, "topic", new BaseRequestCallback<BaseResultModel<Object>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<Object> baseResultModel) {
                            int recommendAdd = 0;
                            if (data != null) {
                                recommendAdd = data.getRecommendAdd() + 1;
                                data.setRecommendAdd(recommendAdd);
                                data.setIsHasRecommendAdd(1);
                            }
                            textView.setVisibility(0);
                            TopicListWechatFragmentAdapter.this.setTextViewData(textView, (long) recommendAdd);
                        }
                    }).execute(new Void[0]);
                }
            }
        });
    }
}
