package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BaseRouteAdapter extends BaseAdapter implements RouteConstant {
    protected Context context;
    protected LayoutInflater inflater;
    protected MCResource resource;

    public BaseRouteAdapter(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }
}
