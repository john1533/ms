package com.mobcent.discuz.base.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public abstract class BaseSimpleAdapter<Data> extends BaseAdapter {
    protected Context context;
    protected List<Data> datas;
    protected Handler handler = new Handler();
    protected LayoutInflater inflater;
    protected MCResource resource;

    public BaseSimpleAdapter(Context context, List<Data> datas) {
        this.context = context;
        this.datas = datas;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
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

    protected void loadImage(ImageView img, String url) {
        ImageLoader.getInstance().displayImage(url, img);
    }
}
