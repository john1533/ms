package com.mobcent.discuz.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.List;

public abstract class BaseListAdatper<Data, Holder> extends BaseAdapter {
    public String TAG = "BaseListAdatper";
    protected Context context;
    protected List<Data> datas;
    protected Handler handler = new Handler();
    protected LayoutInflater inflater;
    protected MCResource resource;

    protected abstract String getLayoutName();

    protected abstract void initViewDatas(Holder holder, Data data, int i);

    protected abstract void initViews(View view, Holder holder);

    protected abstract Holder instanceHolder();

    public BaseListAdatper(Context context, List<Data> datas) {
        this.context = context;
        this.datas = datas;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public Data getItem(int position) {
        return this.datas.get(position);
    }

    public int getCount() {
        return this.datas.size();
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getLayoutName()), null);
            holder = instanceHolder();
            initViews(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        initViewDatas(holder, getItem(position), position);
        return convertView;
    }

    public List<Data> getDatas() {
        return this.datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    protected synchronized void loadImage(final View img, String url) {
        if (!TextUtils.isEmpty(url)) {
            img.setTag(url);
            ImageLoader.getInstance().loadImage(url, new ImageSize(img.getWidth(), img.getHeight()), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (img.getTag() != null && img.getTag().equals(imageUri)) {
                        if (img instanceof ImageView) {
                            ((ImageView) img).setImageBitmap(loadedImage);
                        } else if (img instanceof MCHeadIcon) {
                            ((MCHeadIcon) img).setImageBitmap(loadedImage);
                        }
                    }
                }
            });
        }
    }

    protected void loadImageByTag(ImageView img) {
        loadImage(img, (String) img.getTag());
    }
}
