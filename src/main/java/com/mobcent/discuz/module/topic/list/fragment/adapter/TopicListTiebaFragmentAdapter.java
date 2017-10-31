package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class TopicListTiebaFragmentAdapter extends BaseTopicListFragmentAdapter {
    private ArrayList<RichImageModel> richImageModelList;

    public TopicListTiebaFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        super(context, topicList, componentModel);
    }

    protected View getTopicConvertView(View convertView, int position) {
        TopicModel topicModel = topicList.get(position);
        if (topicModel instanceof AnnoModel) {
            return getAnnoConvertView(convertView, topicModel, true);
        }
        TopicListFragmentAdapterHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof TopicListFragmentAdapterHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("topic_list_tieba_item"), null);
            holder = new TopicListFragmentAdapterHolder();
            initTopicAdapterHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        holder.getNameTextView().setText(topicModel.getUserName());
        holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, topicModel.getLastReplyDate(), "yyyy-MM-dd HH:mm"));
        ImageLoader.getInstance().displayImage(topicModel.getIcon(), holder.getHeadImageView(), DZImageLoadUtils.getHeadIconOptions());
        if (this.summaryLength > 0) {
            holder.getDescTextView().setVisibility(0);
            holder.getDescTextView().setText(MCStringUtil.subString(topicModel.getSubject(), this.summaryLength));
        } else {
            holder.getDescTextView().setVisibility(8);
            holder.getDescTextView().setText("");
        }
        if (this.titleLength > 0) {
            holder.getTitleTextView().setVisibility(0);
            holder.getTitleTextView().setText(MCStringUtil.subString(topicModel.getTitle(), this.titleLength));
        } else {
            holder.getTitleTextView().setVisibility(8);
            holder.getTitleTextView().setText("");
        }
        if (topicModel.getImageList() == null || topicModel.getImageList().size() <= 0) {
            holder.getThumbnailView().setVisibility(8);
        } else {
            holder.getThumbnailView().setVisibility(0);
            initImageBox((RelativeLayout) holder.getThumbnailView(), topicModel);
        }
        if (topicModel.getSourceType() == null || !topicModel.getSourceType().equals("news")) {
            holder.getPraiseTextView().setVisibility(0);
            holder.getPraiseButton().setVisibility(0);
        } else {
            holder.getPraiseTextView().setVisibility(8);
            holder.getPraiseButton().setVisibility(8);
        }
        MCTouchUtil.createTouchDelegate(holder.getReplyButton(), MCPhoneUtil.dip2px(this.context, 20.0f));
        MCTouchUtil.createTouchDelegate(holder.getPraiseButton(), MCPhoneUtil.dip2px(this.context, 20.0f));
        setTextViewData(holder.getPraiseTextView(), (long) topicModel.getRecommendAdd());
        setTextViewData(holder.getReplyTextView(), topicModel.getReplies());
        setGenderText(holder.getGenderImageView(), topicModel.getGender());
        initActions(holder, topicModel);
        setOnClickListener(convertView, topicModel);
        return convertView;
    }

    private void initActions(final TopicListFragmentAdapterHolder holder, final TopicModel data) {
        holder.getPraiseButton().setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (LoginHelper.doInterceptor(TopicListTiebaFragmentAdapter.this.context, null, null)) {
                    holder.getPraiseButton().startAnimation(AnimationUtils.loadAnimation(TopicListTiebaFragmentAdapter.this.context.getApplicationContext(), TopicListTiebaFragmentAdapter.this.resource.getAnimId("dianzan_anim")));
                    new SupportAsyncTask(TopicListTiebaFragmentAdapter.this.context, data.getTopicId(), 0, "topic", new BaseRequestCallback<BaseResultModel<Object>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<Object> result) {
                            if (result.getRs() == 1) {
                                int recommendAdd = 0;
                                if (data != null) {
                                    recommendAdd = data.getRecommendAdd() + 1;
                                    data.setRecommendAdd(recommendAdd);
                                    data.setIsHasRecommendAdd(1);
                                }
                                holder.getPraiseTextView().setText(recommendAdd + "");
                            }
                        }
                    }).execute(new Void[0]);
                }
            }
        });
    }

    private void initImageBox(RelativeLayout imageBox, final TopicModel topicModel) {
        int sizeI = 3;
        imageBox.removeAllViews();
        if (topicModel.getImageList() != null && topicModel.getImageList().size() > 0) {
            int margen = MCPhoneUtil.dip2px(this.context, 5.0f);
            int imageWidth = ((MCPhoneUtil.getDisplayWidth(this.context) - (MCPhoneUtil.dip2px(this.context, 10.0f) * 2)) - (margen * 2)) / 3;
            int i = 0;
            if (topicModel.getImageList().size() <= 3) {
                sizeI = topicModel.getImageList().size();
            }
            while (i < sizeI) {
                final String sourceUrl = (String) topicModel.getImageList().get(i);
                String url = MCAsyncTaskLoaderImage.formatUrl(sourceUrl, FinalConstant.RESOLUTION_SMALL);
                LayoutParams lp = new LayoutParams(imageWidth, imageWidth);
                lp.leftMargin = (margen + imageWidth) * i;
                ImageView imageView = new ImageView(this.context);
                imageView.setBackgroundColor(Color.parseColor("#e0e0e0"));
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(url, imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        TopicListTiebaFragmentAdapter.this.richImageModelList = new ArrayList();
                        for (int i = 0; i < topicModel.getImageList().size(); i++) {
                            RichImageModel model = new RichImageModel();
                            model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl((String) topicModel.getImageList().get(i), FinalConstant.RESOLUTION_BIG));
                            TopicListTiebaFragmentAdapter.this.richImageModelList.add(model);
                        }
                        ImagePreviewHelper.getInstance().startImagePreview((Activity) TopicListTiebaFragmentAdapter.this.context, TopicListTiebaFragmentAdapter.this.richImageModelList, MCAsyncTaskLoaderImage.formatUrl(sourceUrl, FinalConstant.RESOLUTION_BIG), new ImageViewerListener() {
                            public void onViewerPageSelect(int position) {
                                super.onViewerPageSelect(position);
                            }

                            public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                                MCShareModel shareModel = new MCShareModel();
                                shareModel.setPicUrl(MCAsyncTaskLoaderImage.formatUrl(sourceUrl, FinalConstant.RESOLUTION_BIG));
                                shareModel.setImageFilePath("");
                                shareModel.setDownloadUrl(MCResource.getInstance(context.getApplicationContext()).getString("mc_share_download_url"));
                                shareModel.setType(1);
                                MCForumLaunchShareHelper.share(context, shareModel);
                            }
                        });
                    }
                });
                imageBox.addView(imageView);
                i++;
            }
        }
    }

    public void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setLayoutView((RelativeLayout) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_layout")));
        holder.setHeadImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_headview")));
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_titleview")));
        holder.setDescTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_descview")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_timeview")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_nameview")));
        holder.setPraiseTextView((TextView) convertView.findViewById(this.resource.getViewId("topic_praise_text")));
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("topic_reply_text")));
        holder.setThumbnailView((RelativeLayout) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_imagebox")));
        holder.setPraiseButton((ImageButton) convertView.findViewById(this.resource.getViewId("topic_praise_btn")));
        holder.setReplyButton((ImageButton) convertView.findViewById(this.resource.getViewId("topic_reply_btn")));
        holder.setGenderImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_item_genderview")));
    }
}
