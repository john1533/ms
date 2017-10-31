package com.mobcent.discuz.module.article.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.ArticleModel;
import com.mobcent.discuz.android.model.ContentModel;
import com.mobcent.discuz.android.model.SoundModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.module.article.fragment.adapter.holder.ArticleDetailAdapterHolder;
import com.mobcent.discuz.module.article.fragment.adapter.holder.PostsAudioAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailAdapter extends BaseAdapter implements FinalConstant {
    public static final int AUDIO_CLICK_NO = 0;
    public static final int AUDIO_CLICK_YES = 1;
    private ArticleModel articleModel;
    private MCAsyncTaskLoaderImage asyncTaskLoaderImage;
    private PostsAudioAdapterHolder audioAdapterHolder;
    private Context context;
    private LayoutInflater inflater;
    private Handler mHandler = new Handler();
    private OnPageClickLisener onPageClickLisener;
    private long page;
    private MCResource resource;
    private ArrayList<RichImageModel> richImageModelList = new ArrayList();
    protected String tag;

    public interface OnPageClickLisener {
        void onClick(boolean z);
    }

    public PostsAudioAdapterHolder getAudioAdapterHolder() {
        return this.audioAdapterHolder;
    }

    public void setAudioAdapterHolder(PostsAudioAdapterHolder audioAdapterHolder) {
        this.audioAdapterHolder = audioAdapterHolder;
    }

    public OnPageClickLisener getOnPageClickLisener() {
        return this.onPageClickLisener;
    }

    public void setOnPageClickLisener(OnPageClickLisener onPageClickLisener) {
        this.onPageClickLisener = onPageClickLisener;
    }

    public ArticleDetailAdapter(Context context) {
        this.context = context;
        this.asyncTaskLoaderImage = MCAsyncTaskLoaderImage.getInstance(context);
        this.tag = context.toString();
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
        this.articleModel = new ArticleModel();
    }

    public ArticleModel getArticleModel() {
        return this.articleModel;
    }

    public void setArticleModel(ArticleModel articleModel) {
        this.articleModel = articleModel;
    }

    public long getPage() {
        return this.page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return this.articleModel;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConverView(convertView);
        ArticleDetailAdapterHolder holder = (ArticleDetailAdapterHolder) convertView.getTag();
        if (this.articleModel != null) {
            holder.getTitleTextView().setText(this.articleModel.getTitle());
            holder.getTimeTextView().setText(this.articleModel.getDateline());
            holder.getCommentCountTextView().setText(this.articleModel.getCommentNum() + "");
            holder.getHitCountTextView().setText(this.articleModel.getViewNum() + "");
            this.richImageModelList.clear();
            updatePostsDetailView(this.articleModel.getContentList(), holder.getConteLayout(), holder);
            if (this.articleModel.getPageCount() > 1) {
                holder.getPageBox().setVisibility(0);
            }
            if (((long) this.articleModel.getPageCount()) == this.page) {
                holder.getPageNextBtn().setClickable(false);
                holder.getPageNextBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_detail1_nextpage_button5"));
            } else {
                holder.getPageNextBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_detail1_next_button"));
                holder.getPageNextBtn().setClickable(true);
            }
            if (this.page == 1) {
                holder.getPageBackBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_detail1_nextpage_button1"));
                holder.getPageBackBtn().setClickable(false);
            } else {
                holder.getPageBackBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_detail1_back_button"));
                holder.getPageBackBtn().setClickable(true);
            }
            holder.getPageSizeBtn().setText(this.page + "/" + this.articleModel.getPageCount());
            holder.getPageBackBtn().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ArticleDetailAdapter.this.onPageClickLisener.onClick(false);
                }
            });
            holder.getPageNextBtn().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ArticleDetailAdapter.this.onPageClickLisener.onClick(true);
                }
            });
        }
        return convertView;
    }

    private LinearLayout updatePostsDetailView(List<ContentModel> topicContentList, LinearLayout contentLayout, ArticleDetailAdapterHolder holder) {
        contentLayout.setMinimumHeight(MCPhoneUtil.getDisplayHeight(this.context) - MCPhoneUtil.getRawSize(this.context, 1, 270.0f));
        if (topicContentList != null && topicContentList.size() > 0) {
            contentLayout.setVisibility(0);
            contentLayout.removeAllViews();
            int j = topicContentList.size();
            for (int i = 0; i < j; i++) {
                final ContentModel topicContent = (ContentModel) topicContentList.get(i);
                View view = null;
                if (topicContent.getType().equals("text")) {
                    view = this.inflater.inflate(this.resource.getLayoutId("article_text_item"), null);
                    TextView topicInfoText = (TextView) view.findViewById(this.resource.getViewId("mc_forum_topic_info_text"));
                    topicInfoText.setTextSize(18.0f);
                    topicInfoText.setText(topicContent.getContent());
                    DZFaceUtil.setStrToFace(topicInfoText, topicContent.getContent(), this.context);
                    Linkify.addLinks(topicInfoText, 1);
                } else if (topicContent.getType().equals("image")) {
                    view = this.inflater.inflate(this.resource.getLayoutId("article_img_item"), null);
                    ImageView topicInfoImg = (ImageView) view.findViewById(this.resource.getViewId("mc_forum_topic_info_img"));
                    topicInfoImg.setImageResource(this.resource.getDrawableId("mc_forum_add_new_img"));
                    String imageUrl = topicContent.getContent();
                    if (MCStringUtil.isEmpty(topicContent.getContent())) {
                        topicInfoImg.setImageResource(this.resource.getDrawableId("mc_forum_portal_img"));
                    } else {
                        updateTopicContentImage(imageUrl, topicInfoImg);
                        RichImageModel model = new RichImageModel();
                        model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl(imageUrl, FinalConstant.RESOLUTION_BIG));
                        model.setImageDesc(getShareContent(this.articleModel));
                        this.richImageModelList.add(model);
                    }
                    topicInfoImg.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ImagePreviewHelper.getInstance().startImagePreview((Activity) ArticleDetailAdapter.this.context, ArticleDetailAdapter.this.richImageModelList, MCAsyncTaskLoaderImage.formatUrl(topicContent.getSource(), FinalConstant.RESOLUTION_BIG), new ImageViewerListener() {
                                public void onViewerPageSelect(int position) {
                                    super.onViewerPageSelect(position);
                                }

                                public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                                    MCShareModel shareModel = new MCShareModel();
                                    shareModel.setPicUrl(imgModel.getImageUrl());
                                    shareModel.setImageFilePath(localPath);
                                    shareModel.setDownloadUrl(ArticleDetailAdapter.this.resource.getString("mc_share_download_url"));
                                    shareModel.setType(1);
                                    MCForumLaunchShareHelper.share(context, shareModel);
                                }
                            });
                        }
                    });
                } else if (topicContent.getType().equals("audio")) {
                    view = getSoundView(topicContent.getSoundModel());
                } else if (topicContent.getType().equals("video")) {
                    view = this.inflater.inflate(this.resource.getLayoutId("article_detail_video_item"), null);
                    view.setBackgroundResource(17170445);
                    ((ImageView) view.findViewById(this.resource.getViewId("mc_forum_topic_info_video"))).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (!topicContent.getContent().equals("") && topicContent.getContent().contains("http://")) {
                                Intent intent = new Intent(ArticleDetailAdapter.this.context.getApplicationContext(), WebViewFragmentActivity.class);
                                intent.putExtra("webViewUrl", topicContent.getContent());
                                intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TYPE, "video");
                                ArticleDetailAdapter.this.context.startActivity(intent);
                            }
                        }
                    });
                }
                contentLayout.addView(view);
            }
            contentLayout.setVisibility(0);
        }
        return contentLayout;
    }

    private String getShareContent(ArticleModel topic) {
        try {
            String shareContent = "" + topic.getSummary();
            if (shareContent.length() > 70) {
                shareContent = shareContent.substring(0, 70);
            }
            return shareContent;
        } catch (Exception e) {
            return "";
        }
    }

    protected View getSoundView(final SoundModel soundModel) {
        View view = this.inflater.inflate(this.resource.getLayoutId("article_detail_audio_item"), null);
        view.setLayoutParams(new LayoutParams(-2, -2));
        view.setBackgroundResource(17170445);
        PostsAudioAdapterHolder audioAdapterHolder = new PostsAudioAdapterHolder();
        audioAdapterHolder.setAudioLayout((LinearLayout) view.findViewById(this.resource.getViewId("mc_forum_audio_layout")));
        audioAdapterHolder.setTimeText((TextView) view.findViewById(this.resource.getViewId("mc_forum_topic_time_text")));
        audioAdapterHolder.setPlayStautsImg((ImageView) view.findViewById(this.resource.getViewId("mc_forum_topic_play_stauts_img")));
        audioAdapterHolder.setPlayingImg((ImageView) view.findViewById(this.resource.getViewId("mc_forum_topic_playing_img")));
        audioAdapterHolder.setDownProgressBar((MCProgressBar) view.findViewById(this.resource.getViewId("mc_forum_down_progress_bar")));
        audioAdapterHolder.getPlayStautsImg().setImageResource(this.resource.getDrawableId("mc_forum_voice_chat_play"));
        audioAdapterHolder.getPlayStautsImg().setTag(soundModel.getSoundPath());
        setAudioAdapterHolder(audioAdapterHolder);
        audioAdapterHolder.getAudioLayout().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCAudioUtils.getInstance(ArticleDetailAdapter.this.context).playAudioOnLine(soundModel.getSoundPath());
            }
        });
        return view;
    }

    private View getConverView(View convertView) {
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("article_detail_item"), null);
            ArticleDetailAdapterHolder holder = new ArticleDetailAdapterHolder();
            initActicleDetailAdapterHolder(convertView, holder);
            convertView.setTag(holder);
            return convertView;
        }
        try {
            ArticleDetailAdapterHolder holder = (ArticleDetailAdapterHolder) convertView.getTag();
            return convertView;
        } catch (ClassCastException e) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("article_detail_item"), null);
            ArticleDetailAdapterHolder holder = new ArticleDetailAdapterHolder();
            initActicleDetailAdapterHolder(convertView, holder);
            convertView.setTag(holder);
            return convertView;
        }
    }

    private void initActicleDetailAdapterHolder(View convertView, ArticleDetailAdapterHolder holder) {
        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_info_detail_title_text")));
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_info_detail_source_time_text")));
        holder.setConteLayout((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_article_content_layout")));
        holder.setPageBackBtn((Button) convertView.findViewById(this.resource.getViewId("mc_forum_page_back")));
        holder.setPageNextBtn((Button) convertView.findViewById(this.resource.getViewId("mc_forum_page_next")));
        holder.setPageSizeBtn((Button) convertView.findViewById(this.resource.getViewId("mc_forum_page_size")));
        holder.setPageBox((LinearLayout) convertView.findViewById(this.resource.getViewId("mc_forum_page_box")));
        holder.setHitCountTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_access_count_text")));
        holder.setCommentCountTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_froum_reply_count_text")));
    }

    private void updateTopicContentImage(String imgUrl, final ImageView imageView) {
        if (new SettingSharePreference(this.context).isPicAvailable()) {
            ImageViewAware imgAware = new ImageViewAware(imageView);
            ImageLoader.getInstance().loadImage(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_BIG), new ImageSize(imgAware.getWidth(), imgAware.getHeight()), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap image) {
                    super.onLoadingComplete(imageUri, view, image);
                    if (image != null) {
                        int imageWidth = MCPhoneUtil.getRawSize(ArticleDetailAdapter.this.context.getApplicationContext(), 1, (float) image.getWidth());
                        int imageHeight = MCPhoneUtil.getRawSize(ArticleDetailAdapter.this.context.getApplicationContext(), 1, (float) image.getHeight());
                        float ratio = ((float) imageWidth) / ((float) imageHeight);
                        int displayWidth = MCPhoneUtil.getDisplayWidth(ArticleDetailAdapter.this.context.getApplicationContext());
                        int margins = MCPhoneUtil.getRawSize(ArticleDetailAdapter.this.context.getApplicationContext(), 1, 20.0f);
                        if (displayWidth - margins < imageWidth) {
                            imageWidth = displayWidth - margins;
                            imageHeight = (int) (((float) imageWidth) / ratio);
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                        int y = MCPhoneUtil.getRawSize(ArticleDetailAdapter.this.context.getApplicationContext(), 1, 5.0f);
                        layoutParams.setMargins(y, y, y, y);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(image);
                    }
                }
            });
        }
    }
}
