package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class TopicListBigPicFragmentAdapter extends BaseTopicListFragmentAdapter {
    public TopicListBigPicFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        super(context, topicList, componentModel);
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel data = topicList.get(position);
        if (data instanceof AnnoModel) {
            return getAnnoConvertView(convertView, data, true);
        }
        TopicListFragmentAdapterHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_bigpic_item"), null);
            holder = new TopicListFragmentAdapterHolder();
            initBigPicView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        initCommonView(holder, data);
        praiseClick(holder.getPraiseBoxView(), holder.getPraiseButton(), holder.getPraiseTextView(), data);
        holder.getNameTextView().setText(data.getUserName());
        setGenderText(holder.getGenderImageView(), data.getGender());
        if (MCStringUtil.isEmpty(data.getSubject()) || this.summaryLength <= 0) {
            holder.getDescTextView().setVisibility(8);
        } else {
            holder.getDescTextView().setVisibility(0);
            holder.getDescTextView().setText(MCStringUtil.subString(data.getSubject(), this.summaryLength));
        }
        if (this.titleLength == 0) {
            holder.getTitleTextView().setVisibility(8);
        } else {
            holder.getTitleTextView().setVisibility(0);
//            holder.getTitleTextView().setText(MCStringUtil.subString(data.getTitle(), this.titleLength));

            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
                holder.getTitleTextView().setText(MCStringUtil.subString(data.getTitle(), this.titleLength));
            }else{//繁体
                holder.getTitleTextView().setText(ChineseHelper.convertToTraditionalChinese(MCStringUtil.subString(data.getTitle(), this.titleLength)));
            }

        }
        if (MCStringUtil.isEmpty(data.getPicPath())) {
            ((ImageView) holder.getThumbnailView()).setImageBitmap(null);
            holder.getThumbnailView().setVisibility(8);
        } else {
            holder.getThumbnailView().setVisibility(0);
            displayThumbnailImage(false, MCAsyncTaskLoaderImage.formatUrl(data.getPicPath(), FinalConstant.RESOLUTION_SMALL), (ImageView) holder.getThumbnailView());
        }
        if (data.getLastReplyDate() >= 0) {
            holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, data.getLastReplyDate(), "yyyy-MM-dd HH:mm"));
        } else {
            holder.getTimeTextView().setText("");
        }
        setTextViewData(holder.getReplyTextView(), (long) ((int) data.getReplies()));
        setTextViewData(holder.getPraiseTextView(), (long) data.getRecommendAdd());
        if (data.getSourceType().equals("news")) {
            holder.getPraiseBoxView().setVisibility(8);
        } else {
            holder.getPraiseBoxView().setVisibility(0);
        }
        setOnClickListener(convertView, data);
        return convertView;
    }

    protected void initCommonView(TopicListFragmentAdapterHolder holder, TopicModel data) {
        if (MCStringUtil.isEmpty(data.getIcon())) {
            holder.getHeadImageView().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            displayThumbnailImage(true, data.getIcon(), holder.getHeadImageView());
        }
        LayoutParams lParams = new LayoutParams(-1, (int) (((float) MCPhoneUtil.getDisplayWidth(this.context.getApplicationContext())) * 0.659375f));
        lParams.bottomMargin = MCPhoneUtil.dip2px(this.context, 10.0f);
        holder.getThumbnailView().setLayoutParams(lParams);
        ((ImageView) holder.getThumbnailView()).setScaleType(ScaleType.CENTER_CROP);
    }

    private void initBigPicView(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setHeadImageView((ImageView) findViewByName(convertView, "user_icon_img"));
        holder.setNameTextView((TextView) findViewByName(convertView, "user_name_text"));
        holder.setTimeTextView((TextView) findViewByName(convertView, "topic_time_text"));
        holder.setThumbnailView((ImageView) findViewByName(convertView, "topic_content_img"));
        holder.setDescTextView((TextView) findViewByName(convertView, "topic_content_text"));
        holder.setPraiseBoxView((LinearLayout) findViewByName(convertView, "topic_praise_box"));
        holder.setPraiseButton((ImageButton) findViewByName(convertView, "topic_praise_btn"));
        holder.setPraiseTextView((TextView) findViewByName(convertView, "topic_praise_text"));
        holder.setReplyBoxView((LinearLayout) findViewByName(convertView, "topic_reply_box"));
        holder.setReplyButton((ImageButton) findViewByName(convertView, "topic_reply_btn"));
        holder.setReplyTextView((TextView) findViewByName(convertView, "topic_reply_text"));
        holder.setShareBoxView((LinearLayout) findViewByName(convertView, "topic_share_box"));
        holder.setShareButton((ImageButton) findViewByName(convertView, "topic_share_btn"));
        holder.setGenderImageView((ImageView) findViewByName(convertView, "user_gender_text"));
        holder.setTitleTextView((TextView) findViewByName(convertView, "topic_title_text"));
    }

    protected void praiseClick(View layout, final View btnView, final TextView textView, final TopicModel data) {
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LoginHelper.doInterceptor(TopicListBigPicFragmentAdapter.this.context, null, null)) {
                    btnView.startAnimation(AnimationUtils.loadAnimation(TopicListBigPicFragmentAdapter.this.context.getApplicationContext(), TopicListBigPicFragmentAdapter.this.resource.getAnimId("dianzan_anim")));
                    new SupportAsyncTask(TopicListBigPicFragmentAdapter.this.context, data.getTopicId(), 0, "topic", new BaseRequestCallback<BaseResultModel<Object>>() {
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
                            TopicListBigPicFragmentAdapter.this.setTextViewData(textView, (long) recommendAdd);
                        }
                    }).execute(new Void[0]);
                }
            }
        });
    }
}
