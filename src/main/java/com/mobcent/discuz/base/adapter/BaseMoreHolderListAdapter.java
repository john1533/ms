package com.mobcent.discuz.base.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public abstract class BaseMoreHolderListAdapter extends BaseAdapter {
    public String TAG = "BaseMoreHolderListAdapter";
    protected Context context;
    protected List<TopicModel> datas;
    protected Handler handler = new Handler();
    protected LayoutInflater inflater;
    protected MCResource resource;

    protected abstract View getConvertView(View view, TopicModel topicModel, ViewGroup viewGroup);

    protected abstract void initViewDatas(View view, TopicModel topicModel, int i);

    public BaseMoreHolderListAdapter(Context context, List<TopicModel> datas) {
        this.context = context;
        this.datas = datas;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public TopicModel getItem(int position) {
        return (TopicModel) this.datas.get(position);
    }

    public int getCount() {
        return this.datas.size();
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, getItem(position), parent);
        initViewDatas(convertView, getItem(position), position);
        return convertView;
    }

    public List<TopicModel> getDatas() {
        return this.datas;
    }

    public void setDatas(List<TopicModel> datas) {
        this.datas = datas;
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    protected synchronized void loadImage(ImageView img, String url, int width) {
        if (!TextUtils.isEmpty(url)) {
            img.setTag(url);
            ImageLoader.getInstance().displayImage(url, img);
        }
    }
}
