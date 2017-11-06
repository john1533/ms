package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.module.article.fragment.activity.ArticleDetailActivity;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.AnnounceDetailActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.AnnoListFragmentAdapterHolder;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCColorUtil;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public abstract class BaseTopicListFragmentAdapter extends BaseAdapter implements FinalConstant {
    protected ConfigComponentModel componentModel;
    protected Context context;
    protected int imagePosition;
    public LayoutInflater inflater;
    protected MCResource resource;
    protected String style;
    protected int summaryLength;
    protected int titleLength;
    protected List<TopicModel> topicList;

//    protected abstract View getTopicConvertView(View view, TopicModel topicModel);

    protected abstract View getTopicConvertView(View view, int position);

    public BaseTopicListFragmentAdapter(Context context, List<TopicModel> topicList, ConfigComponentModel componentModel) {
        this.context = context;
        this.topicList = topicList;
        this.componentModel = componentModel;
        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.style = componentModel.getStyle();
        this.summaryLength = componentModel.getListSummaryLength();
        this.titleLength = componentModel.getListTitleLength();
        this.imagePosition = componentModel.getListImagePosition();
    }

    public int getCount() {
        return this.topicList.size();
    }

    public Object getItem(int position) {
        return this.topicList.get(position);
    }

    public long getItemId(int position) {
        return ((TopicModel) this.topicList.get(position)).getTopicId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        return getTopicConvertView(convertView, (TopicModel) this.topicList.get(position));
        return getTopicConvertView(convertView, position);
    }

    protected void setOnClickListener(View convertView, final TopicModel topicModel) {
        TopicListFragmentAdapterHolder holder = null;
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                if (topicModel == null || MCStringUtil.isEmpty(topicModel.getSourceType()) || !topicModel.getSourceType().equals("news")) {
                    intent = new Intent(BaseTopicListFragmentAdapter.this.context.getApplicationContext(), TopicDetailActivity.class);
                    intent.putExtra("boardId", topicModel.getBoardId());
                    intent.putExtra("boardName", topicModel.getBoardName());
                    intent.putExtra("topicId", topicModel.getTopicId());
                    if (BaseTopicListFragmentAdapter.this.titleLength == 0) {
                        intent.putExtra("style", StyleConstant.STYLE_NO_TITLE);
                    } else {
                        intent.putExtra("style", BaseTopicListFragmentAdapter.this.componentModel.getSubDetailViewStyle());
                    }
                    BaseTopicListFragmentAdapter.this.context.startActivity(intent);
                } else if (MCStringUtil.isEmpty(topicModel.getRedirectUrl())) {
                    intent = new Intent(BaseTopicListFragmentAdapter.this.context.getApplicationContext(), ArticleDetailActivity.class);
                    intent.putExtra("aid", topicModel.getTopicId());
                    BaseTopicListFragmentAdapter.this.context.startActivity(intent);
                } else {
                    String url = topicModel.getRedirectUrl();
                    if (!MCStringUtil.isEmpty(url)) {
                        if (!url.contains("http://")) {
                            url = "http://" + url;
                        }
                        intent = new Intent(BaseTopicListFragmentAdapter.this.context.getApplicationContext(), WebViewFragmentActivity.class);
                        intent.putExtra("webViewUrl", url);
                        BaseTopicListFragmentAdapter.this.context.startActivity(intent);
                    }
                }
            }
        });
        if (convertView != null) {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        if (holder != null) {
            userIconClick(holder.getHeadImageView(), topicModel);
            replyClick(holder.getReplyButton(), topicModel);
            replyClick(holder.getReplyBoxView(), topicModel);
            shareClick(holder.getShareButton(), topicModel);
            shareClick(holder.getShareBoxView(), topicModel);
        }
    }

    protected void displayThumbnailImage(final boolean isIcon, String imgUrl, final ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            imageView.setTag(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL));
            ImageLoader.getInstance().displayImage(imageView.getTag().toString(), imageView, DZImageLoadUtils.getHeadIconOptions(), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage == null || loadedImage.isRecycled() || !imageView.getTag().equals(imageUri)) {
                        if (!isIcon) {
                            imageView.setBackgroundResource(BaseTopicListFragmentAdapter.this.resource.getDrawableId("mc_forum_add_new_img"));
                        }
                    } else if (!isIcon) {
                        imageView.setImageBitmap(loadedImage);
                    }
                }
            });
        }
    }

    protected View getAnnoConvertView(View convertView, final TopicModel topicModel, boolean isHideLine) {
        AnnoListFragmentAdapterHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof AnnoListFragmentAdapterHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("anno_list_item"), null);
            holder = new AnnoListFragmentAdapterHolder();
            initAnnoView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (AnnoListFragmentAdapterHolder) convertView.getTag();
        }
        AnnoModel annoModel = (AnnoModel) topicModel;
        String signStr = this.resource.getString("mc_forum_announce_mark");
        holder.getNameTextView().setText(annoModel.getAuthor());
        if (annoModel.getAnnoStartDate() >= 0) {
            holder.getTimeTextView().setText(MCDateUtil.getFormatTimeByWord(this.resource, annoModel.getAnnoStartDate(), "yyyy-MM-dd HH:mm"));
        } else {
            holder.getTimeTextView().setText("");
        }
        String titleStr = signStr + topicModel.getTitle();
        int signEndPosition = signStr.length();
//        holder.getTitleTextView().setText(titleStr);
        if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
            holder.getTitleTextView().setText(MCStringUtil.subString(topicModel.getTitle(), this.titleLength));
        }else{//繁体
            holder.getTitleTextView().setText(ChineseHelper.convertToTraditionalChinese(MCStringUtil.subString(topicModel.getTitle(), this.titleLength)));
        }
        MCColorUtil.setTextViewPart(this.context, holder.getTitleTextView(), titleStr, 0, signEndPosition, "mc_forum_text6_normal_color");
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BaseTopicListFragmentAdapter.this.context.getApplicationContext(), AnnounceDetailActivity.class);
                intent.putExtra(IntentConstant.INTENT_ANNO_ID, ((AnnoModel) topicModel).getAnnoId());
                BaseTopicListFragmentAdapter.this.context.startActivity(intent);
            }
        });
        if (!isHideLine) {
            holder.getLineView().setVisibility(4);
        }
        return convertView;
    }

    private void initAnnoView(View convertView, AnnoListFragmentAdapterHolder holder) {
        holder.setTimeTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_last_update_time_text")));
        holder.setNameTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_nickname_text")));

        holder.setTitleTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_topic_title_text")));

        holder.setLineView((ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_line")));
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    protected void userIconClick(View view, final TopicModel data) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    HashMap<String, Serializable> param = new HashMap();
                    param.put("userId", Long.valueOf(data.getUserId()));
                    if (LoginHelper.doInterceptor(BaseTopicListFragmentAdapter.this.context, UserHomeActivity.class, param)) {
                        Intent intent = new Intent(BaseTopicListFragmentAdapter.this.context, UserHomeActivity.class);
                        intent.putExtra("userId", data.getUserId());
                        BaseTopicListFragmentAdapter.this.context.startActivity(intent);
                    }
                }
            });
        }
    }

    protected void replyClick(View view, final TopicModel data) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (LoginHelper.doInterceptor(BaseTopicListFragmentAdapter.this.context, null, null)) {
                        Intent intent = new Intent(BaseTopicListFragmentAdapter.this.context.getApplicationContext(), TopicDetailActivity.class);
                        intent.putExtra("boardId", data.getBoardId());
                        intent.putExtra("boardName", data.getBoardName());
                        intent.putExtra("topicId", data.getTopicId());
                        intent.putExtra(IntentConstant.INTENT_TOPIC_DETAIL_REQUEST_EDIT, true);
                        BaseTopicListFragmentAdapter.this.context.startActivity(intent);
                    }
                }
            });
        }
    }

    protected void shareClick(View view, final TopicModel data) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MCShareModel shareModel = new MCShareModel();
                    shareModel.setTitle("");
                    shareModel.setContent(data.getSubject());
                    shareModel.setType(3);
                    shareModel.setPicUrl(data.getPicPath());
                    shareModel.setLinkUrl(data.getForumTopicUrl());
                    shareModel.setSkipUrl(data.getForumTopicUrl());
                    shareModel.setDownloadUrl(BaseTopicListFragmentAdapter.this.resource.getString("mc_share_download_url"));
                    HashMap<String, String> params = new HashMap();
                    params.put("topicId", String.valueOf(data.getTopicId()));
                    params.put(MCShareConstant.PARAM_SHARE_FROM, MCShareConstant.FROM_PHP);
                    params.put("baseUrl", BaseTopicListFragmentAdapter.this.resource.getString("mc_discuz_base_request_url"));
                    params.put("style", String.valueOf(1));
                    params.put(MCShareConstant.PARAM_CONTENT_TYPE, "topic");
                    params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
                    shareModel.setParams(params);
                    MCForumLaunchShareHelper.share(BaseTopicListFragmentAdapter.this.context, shareModel);
                }
            });
        }
    }

    protected void setTextViewData(TextView textView, long num) {
        if (num > 99) {
            textView.setText("99+");
        } else {
            textView.setText(num + "");
        }
    }

    protected void setGenderText(ImageView imageView, int gender) {
        if (gender == 1) {
            imageView.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon29_n"));
        } else if (gender == 2) {
            imageView.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon28_n"));
        } else if (gender == 0) {
            imageView.setBackgroundDrawable(null);
        }
    }
}
