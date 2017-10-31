package com.mobcent.lowest.android.ui.module.place.module.route.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.mobcent.lowest.android.ui.module.place.activity.BasePlaceFragmentActivity;
import com.mobcent.lowest.android.ui.module.place.activity.PlaceMapViewActivity;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.manager.PlaceManager;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.RouteCommentDetailAdapter;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BusDetailActivity extends BasePlaceFragmentActivity implements RouteConstant {
    private final String TAG = "BusDetailActivity";
    private RouteCommentDetailAdapter commentDetailAdapter;
    private Context context;
    private LinearLayout detailContent;
    private Button finishBtn;
    private Button openMapBtn;
    private ArrayList<String[]> routeList;
    private RouteModel routeModel;
    private MKTransitRoutePlan routePlan;
    private RouteSearchMessageModel searchMsgModel;
    private int solutionNum;
    private ListView stepList;
    private TextView topbarTitle;
    private MKTransitRouteResult transitResult;

    protected void initData() {
        super.initData();
        this.searchMsgModel = (RouteSearchMessageModel) getIntent().getSerializableExtra(RouteConstant.SEARCH_MSG_MODEL);
        this.routeModel = PlaceManager.getInstance().getRouteModel();
        this.transitResult = this.routeModel.getTransitRouteModel();
        this.routePlan = this.transitResult.getPlan(this.searchMsgModel.getSearchNum());
        this.context = getApplicationContext();
        this.routeList = new ArrayList();
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_route_bus_detail"));
        this.finishBtn = (Button) findViewById(this.resource.getViewId("nav_detail_finish_btn"));
        this.openMapBtn = (Button) findViewById(this.resource.getViewId("nav_detail_multi_btn"));
        this.topbarTitle = (TextView) findViewById(this.resource.getViewId("nav_detail_topbar_title"));
        this.detailContent = (LinearLayout) findViewById(this.resource.getViewId("route_bus_detail_content"));
        this.stepList = (ListView) findViewById(this.resource.getViewId("traffic_step_detail_item"));
        this.topbarTitle.setText(this.resource.getStringId("mc_route_bus_detail_topbar_tital"));
        View contentView = View.inflate(this, this.resource.getLayoutId("mc_place_route_bus_list_item"), null);
        TextView content = (TextView) contentView.findViewById(this.resource.getViewId("route_suggest_location_name"));
        TextView otherMsg = (TextView) contentView.findViewById(this.resource.getViewId("route_suggest_location_city"));
        ((Button) contentView.findViewById(this.resource.getViewId("route_suggest_location_step"))).setText((this.searchMsgModel.getSearchNum() + 1) + "");
        content.setText(this.routePlan.getContent().replaceAll("_", "→"));
        otherMsg.setText(getTime(this.routePlan.getTime()));
        this.detailContent.addView(contentView);
        this.routeModel.setTransitRouteModel(this.transitResult);
        this.searchMsgModel.setSearchType(1);
        MKTransitRoutePlan routePlan = this.transitResult.getPlan(this.searchMsgModel.getSearchNum());
        String[] strArr;
        int tNum = routePlan.getNumRoute();
        for (int i = 0; i < tNum; i++) {
            if (routePlan.getRoute(i) != null) {
                strArr = new String[2];
                MKRoute route = routePlan.getRoute(i);
                strArr[0] = route.getRouteType() + "";
                strArr[1] = route.getTip();
                this.routeList.add(strArr);
            }
            if (routePlan.getLine(i) != null) {
                strArr = new String[2];
                MKLine line = routePlan.getLine(i);
                strArr[0] = line.getType() + "";
                strArr[1] = line.getTip();
                this.routeList.add(strArr);
            }
        }
        this.routeList.add(0, new String[]{"", this.searchMsgModel.getStartName()});
        this.routeList.add(this.routeList.size(), new String[]{"", this.searchMsgModel.getDistanceName()});
        this.commentDetailAdapter = new RouteCommentDetailAdapter(this, this.routeList, this.searchMsgModel);
        this.stepList.setAdapter(this.commentDetailAdapter);
    }

    protected void initActions() {
        this.openMapBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent mapViewIntent = new Intent(BusDetailActivity.this.context, PlaceMapViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouteConstant.SEARCH_MSG_MODEL, BusDetailActivity.this.searchMsgModel);
                mapViewIntent.putExtra(RouteConstant.MAP_VIEW_BUNDLE, bundle);
                mapViewIntent.setFlags(268435456);
                BusDetailActivity.this.context.startActivity(mapViewIntent);
            }
        });
        this.finishBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BusDetailActivity.this.finish();
            }
        });
    }

    private String getTime(int second) {
        int h = second / 3600;
        float m = (((float) second) % 3600.0f) / 60.0f;
        DecimalFormat df1 = new DecimalFormat("###");
        DecimalFormat df2 = new DecimalFormat("###.0");
        if (h == 0) {
            return this.resource.getString("mc_traffic_route_about_time") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) this.routePlan.getDistance()) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km");
        }
        return this.resource.getString("mc_traffic_route_about_time") + h + this.resource.getString("mc_traffic_route_about_hour") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) this.routePlan.getDistance()) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km");
    }
}
