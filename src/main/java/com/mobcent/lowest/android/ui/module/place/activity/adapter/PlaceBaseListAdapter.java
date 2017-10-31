package com.mobcent.lowest.android.ui.module.place.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.List;

public abstract class PlaceBaseListAdapter<DataModel, Holder> extends BaseAdapter {
    protected Context context;
    protected List<DataModel> dataList;
    protected LayoutInflater inflater;
    protected boolean isItemClickAble = false;
    protected MCResource resource;

    protected abstract String getConvertViewName();

    protected abstract void initDatas(Holder holder, DataModel dataModel, int i);

    protected abstract void initViews(View view, Holder holder);

    protected abstract Holder instanceHolder();

    public PlaceBaseListAdapter(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
        this.isItemClickAble = true;
    }

    public PlaceBaseListAdapter(Context context, List<DataModel> dataList) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    public int getCount() {
        return this.dataList.size();
    }

    public DataModel getItem(int position) {
        return this.dataList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        final DataModel model = getItem(position);
        if (convertView == null) {
            convertView = getConvertView();
            holder = instanceHolder();
            initViews(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        initDatas(holder, model, position);
        if (this.isItemClickAble) {
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PlaceBaseListAdapter.this.onItemClick(v, model);
                }
            });
        }
        return convertView;
    }

    protected void onItemClick(View v, DataModel dataModel) {
    }

    protected View getConvertView() {
        return inflatView(getConvertViewName());
    }

    public View inflatView(String name) {
        return this.inflater.inflate(this.resource.getLayoutId(name), null);
    }

    protected View findViewById(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    public List<DataModel> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<DataModel> dataList) {
        this.dataList = dataList;
    }

    protected void warnById(String name) {
        warnStr(this.resource.getString(name));
    }

    protected void warnStr(String msg) {
        Toast.makeText(this.context, msg, LocationClientOption.MIN_SCAN_SPAN).show();
    }
}
