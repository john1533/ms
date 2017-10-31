package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PhotoList1FragementAdapter extends BasePhotoListFragmentAdapter {
    public PhotoList1FragementAdapter(Context context, ConfigComponentModel componentModel) {
        super(context, componentModel);
    }

    public View createView(BaseFallWallModel tag, final TopicModel model) {
        final TopicListFragmentAdapterHolder holder = new TopicListFragmentAdapterHolder();
        View convertView = this.inflater.inflate(this.resource.getLayoutId("portal_list_photo1_item"), null);
        initTopicAdapterHolder(convertView, holder);
        convertView.setTag(holder);
        setTextViewData(holder.getReplyTextView(), model.getReplies());
        holder.getTitleTextView().setText(model.getTitle());
        holder.getTimeTextView().setText(MCDateUtil.getFormatTimeExceptHourAndSecond(model.getLastReplyDate()));
        holder.getThumbnailView().setTag(MCAsyncTaskLoaderImage.formatUrl(model.getPicPath(), FinalConstant.RESOLUTION_SMALL));
        holder.getPraiseButton().setBackgroundResource(this.resource.getDrawableId("mc_forum_ico50_n"));
        MCTouchUtil.createTouchDelegate(holder.getPraiseButton(), 15);
        holder.getPraiseButton().setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (LoginHelper.doInterceptor(PhotoList1FragementAdapter.this.context, null, null)) {
                    holder.getPraiseButton().startAnimation(AnimationUtils.loadAnimation(PhotoList1FragementAdapter.this.context.getApplicationContext(), PhotoList1FragementAdapter.this.resource.getAnimId("dianzan_anim")));
                    new SupportAsyncTask(PhotoList1FragementAdapter.this.context.getApplicationContext(), model.getTopicId(), 0, "topic", new BaseRequestCallback<BaseResultModel<Object>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<Object> result) {
                            if (result.getRs() != 0) {
                                return;
                            }
                            if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                                MCToastUtils.toastByResName(PhotoList1FragementAdapter.this.context.getApplicationContext(), "mc_forum_posts_praise_fail");
                            } else {
                                MCToastUtils.toast(PhotoList1FragementAdapter.this.context.getApplicationContext(), result.getErrorInfo(), 0);
                            }
                        }
                    }).execute(new Void[0]);
                }
            }
        });
        setOnClickListener(convertView, model);
        return convertView;
    }

    public void setData(BaseFallWallModel flowTag, View view, boolean isVisibile, TopicModel model) {
        if (isVisibile && view.getTag() != null && (view.getTag() instanceof TopicListFragmentAdapterHolder)) {
            TopicListFragmentAdapterHolder holder = (TopicListFragmentAdapterHolder) view.getTag();
            String imgUrl = MCAsyncTaskLoaderImage.formatUrl(model.getPicPath(), FinalConstant.RESOLUTION_SMALL);
            if (((ImageView) holder.getThumbnailView()).getDrawable() == null) {
                displayThumbnailImage((ImageView) holder.getThumbnailView(), imgUrl, false);
            }
            if (holder.getHeadImageView().getDrawable() == null) {
                displayThumbnailImage(holder.getHeadImageView(), MCAsyncTaskLoaderImage.formatUrl(model.getIcon(), FinalConstant.RESOLUTION_SMALL), true);
            }
        }
    }

    private void displayThumbnailImage(ImageView imageView, String url, boolean isIcon) {
        if (!TextUtils.isEmpty(url)) {
            imageView.setTag(MCAsyncTaskLoaderImage.formatUrl(url, FinalConstant.RESOLUTION_SMALL));
            DisplayImageOptions options = new Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(imageView.getWidth() / 2)).build();
            ImageLoader instance = ImageLoader.getInstance();
            String obj = imageView.getTag().toString();
            if (!isIcon) {
                options = null;
            }
            instance.displayImage(obj, imageView, options);
        }
    }

    private void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_photo_img")));
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_title")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_time_text")));
        holder.setReplyTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_comment_text")));
        holder.setHeadImageView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_icon_img")));
        holder.setPraiseButton((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_support_img")));
    }
}
