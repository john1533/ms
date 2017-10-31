package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.article.fragment.activity.ArticleDetailActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class ProtalPicList1Adapter extends PagerAdapter {
    private List<TopicModel> datas;
    private String style;

    public ProtalPicList1Adapter(List<TopicModel> topicList, String style) {
        this.datas = topicList;
        this.style = style;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ScaleType.CENTER_CROP);
        container.addView(imageView, new LayoutParams(-1, -1));
        String url = MCAsyncTaskLoaderImage.formatUrl(((TopicModel) this.datas.get(position)).getPicPath(), FinalConstant.RESOLUTION_BIG);
        if (new SettingSharePreference(container.getContext()).isPicAvailable()) {
            ImageLoader.getInstance().displayImage(url, imageView);
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent;
                if (((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getSourceType().equals("topic")) {
                    intent = new Intent(container.getContext(), TopicDetailActivity.class);
                    intent.putExtra("boardId", ((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getBoardId());
                    intent.putExtra("boardName", ((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getBoardName());
                    intent.putExtra("topicId", ((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getTopicId());
                    intent.putExtra("style", ProtalPicList1Adapter.this.style);
                    container.getContext().startActivity(intent);
                } else if (((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getSourceType().equals(FinalConstant.PORTAL_WEBLINK)) {
                    String url = ((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getPicToUrl();
                    if (!MCStringUtil.isEmpty(url)) {
                        if (!url.contains("http://")) {
                            url = "http://" + url;
                        }
                        Intent it = new Intent(container.getContext(), WebViewFragmentActivity.class);
                        it.putExtra("webViewUrl", url);
                        container.getContext().startActivity(it);
                    }
                } else if (((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getSourceType().equals("news")) {
                    intent = new Intent(container.getContext(), ArticleDetailActivity.class);
                    intent.putExtra("aid", ((TopicModel) ProtalPicList1Adapter.this.datas.get(position)).getTopicId());
                    container.getContext().startActivity(intent);
                }
            }
        });
        return imageView;
    }

    public int getCount() {
        return this.datas.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public List<TopicModel> getDatas() {
        return this.datas;
    }

    public void setDatas(List<TopicModel> datas) {
        this.datas = datas;
    }
}
