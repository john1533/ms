package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.holder.RouteSugDialogListAdapterHolder;
import java.util.ArrayList;

public class RouteSugDialogListAdapter extends BaseRouteAdapter {
    private int stOrEn;
    private ClickSugItemListener sugClickListener;

    public RouteSugDialogListAdapter(Context context) {
        super(context);
    }

    public interface ClickSugItemListener {
//        void onClickItemLayout(MKPoiInfo mKPoiInfo, int i);
    }

//    public RouteSugDialogListAdapter(Context context, ArrayList<MKPoiInfo> infoList, ClickSugItemListener sugClickListener, int stOrEn) {
//        super(context);
//        this.infoList = infoList;
//        this.sugClickListener = sugClickListener;
//        this.stOrEn = stOrEn;
//    }

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
//        return this.infoList.get(position);
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RouteSugDialogListAdapterHolder holder;
//        MKPoiInfo poiInfo = (MKPoiInfo) getItem(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_place_route_location_dialog_list_item"), null);
            holder = new RouteSugDialogListAdapterHolder();
            initHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (RouteSugDialogListAdapterHolder) convertView.getTag();
        }
//        updataHolder(holder, poiInfo);
//        clickItem(holder, poiInfo, this.stOrEn);
        return convertView;
    }

    private void initHolder(View convertView, RouteSugDialogListAdapterHolder holder) {
        TextView sugName = (TextView) convertView.findViewById(this.resource.getViewId("route_dialog_sug_list_item_name"));
        RelativeLayout itemLayout = (RelativeLayout) convertView.findViewById(this.resource.getViewId("route_dialog_sug_list_item"));
        holder.setCity((TextView) convertView.findViewById(this.resource.getViewId("route_dialog_sug_list_item_city")));
        holder.setName(sugName);
        holder.setItemLayout(itemLayout);
    }




}
