package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.manager.PlaceManager;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.BusDetailActivity;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.holder.BusListAdapterHolder;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import com.mobcent.lowest.base.utils.MCResource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusResultAdapter extends BaseRouteAdapter {
    private Context context;
    private MCResource resource;
    private MKTransitRouteResult result;
    private RouteSearchMessageModel searchMsgModel;
    private long sysCurMil;

    public BusResultAdapter(Context context, MCResource resource, MKTransitRouteResult result, RouteSearchMessageModel searchMsgModel, long sysCurMil) {
        super(context);
        this.result = result;
        this.context = context;
        this.resource = resource;
        this.searchMsgModel = searchMsgModel;
        this.sysCurMil = sysCurMil;
    }

    public MKTransitRouteResult getBusResult() {
        return this.result;
    }

    public int getCount() {
        return this.result.getNumPlan();
    }

    public Object getItem(int position) {
        return this.result.getPlan(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BusListAdapterHolder holder;
        MKTransitRoutePlan routePlanModel = (MKTransitRoutePlan) getItem(position);
        if (convertView == null) {
            convertView = View.inflate(this.context, this.resource.getLayoutId("mc_place_route_bus_list_item"), null);
            holder = new BusListAdapterHolder();
            initBusListAdapterHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BusListAdapterHolder) convertView.getTag();
        }
        updataAdapterHodler(routePlanModel, holder, position);
        clickRouteItem(holder, position);
        return convertView;
    }

    private void initBusListAdapterHolder(View convertView, BusListAdapterHolder holder) {
        Button setp = (Button) convertView.findViewById(this.resource.getViewId("route_suggest_location_step"));
        TextView content = (TextView) convertView.findViewById(this.resource.getViewId("route_suggest_location_name"));
        RelativeLayout itemView = (RelativeLayout) convertView.findViewById(this.resource.getViewId("route_suggest_location_view"));
        holder.setOtherMsg((TextView) convertView.findViewById(this.resource.getViewId("route_suggest_location_city")));
        holder.setRouteContent(content);
        holder.setRouteNum(setp);
        holder.setItemView(itemView);
    }

    private void updataAdapterHodler(MKTransitRoutePlan routePlanModel, BusListAdapterHolder holder, int position) {
        holder.getOtherMsg().setText(getTime(routePlanModel.getTime(), routePlanModel.getDistance()));
        holder.getRouteContent().setText(routePlanModel.getContent().replaceAll("_", "→"));
        holder.getRouteNum().setText((position + 1) + "");
    }

    private void clickRouteItem(BusListAdapterHolder holder, final int position) {
        holder.getItemView().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RouteModel routeModel = new RouteModel();
                routeModel.setTransitRouteModel(BusResultAdapter.this.result);
                PlaceManager.getInstance().setRouteModel(routeModel);
                Intent locationIntent = new Intent(BusResultAdapter.this.context, BusDetailActivity.class);
                BusResultAdapter.this.searchMsgModel.setSearchNum(position);
                locationIntent.putExtra(RouteConstant.SEARCH_MSG_MODEL, BusResultAdapter.this.searchMsgModel);
                locationIntent.addFlags(268435456);
                BusResultAdapter.this.context.startActivity(locationIntent);
            }
        });
    }

    private String getTime(int second, int meter) {
        long ft = this.sysCurMil + ((long) (second * LocationClientOption.MIN_SCAN_SPAN));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d = new Date(ft);
        String yue = this.resource.getString("mc_traffic_route_about_time");
        String arrive = this.resource.getString("mc_place_route_arrive");
        int h = second / 3600;
        float m = (((float) second) % 3600.0f) / 60.0f;
        DecimalFormat df1 = new DecimalFormat("###");
        DecimalFormat df2 = new DecimalFormat("###.0");
        if (h == 0) {
            return yue + sdf.format(d) + arrive + "  " + (this.resource.getString("mc_traffic_route_about_time") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) meter) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km"));
        }
        return yue + sdf.format(d) + arrive + "  " + (this.resource.getString("mc_traffic_route_about_time") + h + this.resource.getString("mc_traffic_route_about_hour") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) meter) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km"));
    }
}
