package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.SupportAsyncTask;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PhotoList2FragementAdapter extends BasePhotoListFragmentAdapter {
    public String TAG = "PhotoList2FragementAdapter";

    public PhotoList2FragementAdapter(Context context, ConfigComponentModel componentModel) {
        super(context, componentModel);
    }

    public View createView(BaseFallWallModel tag, TopicModel model) {
        TopicListFragmentAdapterHolder holder = new TopicListFragmentAdapterHolder();
        View convertView = this.inflater.inflate(this.resource.getLayoutId("portal_list_photo2_item"), null);
        initTopicAdapterHolder(convertView, holder);
        convertView.setTag(holder);
        holder.getNameTextView().setText(model.getUserName());
        holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, model.getLastReplyDate(), "yyyy-MM-dd"));
        if (model.getRecommendAdd() > 0) {
            setTextViewData(holder.getPraiseTextView(), (long) model.getRecommendAdd());
        }
        holder.getHeadImageView().setImageDrawable(MCHeadIcon.getHeadIconDrawable(this.context));
        praiseClick(holder.getPraiseBoxView(), holder.getPraiseButton(), holder.getPraiseTextView(), model);
        setOnClickListener(convertView, model);
        return convertView;
    }

    public void setData(BaseFallWallModel flowTag, View view, boolean isVisibile, TopicModel model) {
        if (isVisibile && view.getTag() != null && (view.getTag() instanceof TopicListFragmentAdapterHolder)) {
            TopicListFragmentAdapterHolder holder = (TopicListFragmentAdapterHolder) view.getTag();
            String imgUrl = MCAsyncTaskLoaderImage.formatUrl(model.getPicPath(), FinalConstant.RESOLUTION_SMALL);
            if (((ImageView) holder.getThumbnailView()).getDrawable() == null) {
                updateImage(false, imgUrl, (ImageView) holder.getThumbnailView());
            }
            updateImage(true, MCAsyncTaskLoaderImage.formatUrl(model.getIcon(), FinalConstant.RESOLUTION_SMALL), holder.getHeadImageView());
        }
    }

    protected void praiseClick(View layout, final View btnView, final TextView textView, final TopicModel data) {
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LoginHelper.doInterceptor(PhotoList2FragementAdapter.this.context.getApplicationContext(), null, null)) {
                    btnView.startAnimation(AnimationUtils.loadAnimation(PhotoList2FragementAdapter.this.context.getApplicationContext(), PhotoList2FragementAdapter.this.resource.getAnimId("dianzan_anim")));
                    new SupportAsyncTask(PhotoList2FragementAdapter.this.context, data.getTopicId(), 0, "topic", new BaseRequestCallback<BaseResultModel<Object>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<Object> result) {
                            int recommendAdd = 0;
                            if (result.getRs() == 1) {
                                if (data != null) {
                                    recommendAdd = data.getRecommendAdd() + 1;
                                }
                                PhotoList2FragementAdapter.this.setTextViewData(textView, (long) recommendAdd);
                            }
                        }
                    }).execute(new Void[0]);
                }
            }
        });
    }

    private void displayThumbnailImage(ImageView imageView, String url, boolean isIcon) {
        if (!TextUtils.isEmpty(url)) {
            imageView.setTag(MCAsyncTaskLoaderImage.formatUrl(url, FinalConstant.RESOLUTION_SMALL));
            ImageLoader.getInstance().displayImage(imageView.getTag().toString(), imageView, isIcon ? DZImageLoadUtils.getHeadIconOptions() : null);
        }
    }

    private void updateImage(final boolean isIcon, String imgUrl, final ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            imageView.setTag(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL));
            ImageLoader.getInstance().displayImage(imageView.getTag().toString(), imageView, DZImageLoadUtils.getHeadIconOptions(), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage == null || loadedImage.isRecycled() || !imageView.getTag().equals(imageUri)) {
                        if (!isIcon) {
                            imageView.setBackgroundResource(PhotoList2FragementAdapter.this.resource.getDrawableId("mc_forum_add_new_img"));
                        }
                    } else if (!isIcon) {
                        imageView.setImageBitmap(loadedImage);
                    }
                }
            });
        }
    }

    private void initTopicAdapterHolder(View convertView, TopicListFragmentAdapterHolder holder) {
        holder.setHeadImageView((ImageView) convertView.findViewById(this.resource.getViewId("user_icon_img")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("user_name_text")));
        holder.setThumbnailView((ImageView) convertView.findViewById(this.resource.getViewId("pic_img")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("time_text")));
        holder.setPraiseButton((ImageView) convertView.findViewById(this.resource.getViewId("topic_praise_btn")));
        holder.setPraiseTextView((TextView) convertView.findViewById(this.resource.getViewId("topic_praise_text")));
        holder.setPraiseBoxView((LinearLayout) convertView.findViewById(this.resource.getViewId("topic_praise_box")));
    }
}
