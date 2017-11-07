package com.mobcent.lowest.android.ui.module.place.activity;

import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.manager.PlaceManager;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.BusResultAdapter;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.RouteCommentDetailAdapter;
import com.mobcent.lowest.android.ui.module.place.module.route.model.GeoPointModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import com.mobcent.lowest.android.ui.module.place.utils.RouteLoadingUtil;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView.ClickSubNavListener;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class RouteSolutionDetailActivity extends BasePlaceFragmentActivity implements RouteConstant {
    private static Context context;
    public static boolean m_bKeyRight = true;
    private final String TAG = "RouteSolutionDetailActivity";
    private RouteCommentDetailAdapter adapter;
    private BusResultAdapter busAdapter;
    private Button busFaster;
    private Button busModel;
    private Button busTransfer;
    private Button busWalk;
    private Button carModel;
    private String currentTrafficMode;
    private String disCity;
    private TextView endLocation;
    private Button finishBtn;
    private int layoutResouceId;
    private LinearLayout navLayout;
    private Button openMapBtn;
    private LinearLayout otherMsg;
    private ArrayList<String[]> routeList;
    private RouteSearchMessageModel searchMsgModel;
    private String setDate = null;
    private ImageView shareIcon;
    private LinearLayout shareLayout;
    private TextView shareText;
    private String startCity;
    private TextView startLocation;
    private LinearLayout strategyTag;
    private long systemCurMil = 0;
    private TimePickerDialog tpd = null;
    private ListView trafficList;
    private LinearLayout trafficPolicy;
    private Button walkModel;

    protected void initData() {
        super.initData();
        context = getApplicationContext();
//        this.mBMapManager = PlaceManager.getInstance().getBMapManager(context);
        this.searchMsgModel = (RouteSearchMessageModel) getIntent().getBundleExtra(RouteConstant.SEARCHLOCATION).getSerializable(RouteConstant.SEARCH_MSG_MODEL);
        this.startCity = this.searchMsgModel.getStartCity();
        this.disCity = this.searchMsgModel.getDistanceCity();
        this.routeList = new ArrayList();
        this.adapter = new RouteCommentDetailAdapter(this, this.routeList, this.searchMsgModel);
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_traffic_list"));
        this.strategyTag = (LinearLayout) findViewById("traffic_policy");
        this.trafficPolicy = (LinearLayout) findViewById("traffic_policy");
        this.startLocation = (TextView) findViewById("route_plan_traffic_list_start_text");
        this.endLocation = (TextView) findViewById("route_plan_traffic_list_end_text");
        this.otherMsg = (LinearLayout) findViewById("route_plan_traffic_other_msg");
        this.trafficList = (ListView) findViewById("route_plan_traffic_result_list");
        this.busModel = (Button) findViewById("route_plan_top_by_bus");
        this.carModel = (Button) findViewById("route_plan_top_by_car");
        this.walkModel = (Button) findViewById("route_plan_top_by_walk");
        this.openMapBtn = (Button) findViewById("route_plan_open_map");
        this.finishBtn = (Button) findViewById("route_plan_go_route_plan");
        this.navLayout = (LinearLayout) findViewById("route_traffic_nav_layout");
        this.shareLayout = (LinearLayout) findViewById("route_traffic_share_layout");
        this.shareIcon = (ImageView) findViewById("place_traffic_list_share_icon");
        this.shareText = (TextView) findViewById("place_route_traffic_share_text");
    }


    protected void initActions() {
        this.shareLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    RouteSolutionDetailActivity.this.shareIcon.setPressed(true);
                    RouteSolutionDetailActivity.this.shareText.setPressed(true);
                } else if (event.getAction() == 1) {
                    RouteSolutionDetailActivity.this.shareIcon.setPressed(false);
                    RouteSolutionDetailActivity.this.shareText.setPressed(false);
                }
                return false;
            }
        });
        this.busModel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RouteSolutionDetailActivity.this.loadBusView();
            }
        });
        this.carModel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RouteSolutionDetailActivity.this.loadDriveView();
            }
        });
        this.walkModel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RouteSolutionDetailActivity.this.loadWalkView();
            }
        });
        this.openMapBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent mapViewIntent = new Intent(RouteSolutionDetailActivity.context, PlaceMapViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RouteConstant.SEARCH_MSG_MODEL, RouteSolutionDetailActivity.this.searchMsgModel);
                mapViewIntent.putExtra(RouteConstant.MAP_VIEW_BUNDLE, bundle);
                RouteSolutionDetailActivity.this.startActivity(mapViewIntent);
            }
        });
        this.finishBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RouteSolutionDetailActivity.this.finish();
            }
        });
        this.navLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

            }
        });
        this.shareLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
    }



    private void setTrafficIcon(int searchType) {
        switch (searchType) {
            case 1:
                this.carModel.setSelected(false);
                this.busModel.setSelected(true);
                this.walkModel.setSelected(false);
                this.routeList.clear();
                this.adapter.notifyDataSetChanged();
                return;
            case 2:
                this.carModel.setSelected(true);
                this.busModel.setSelected(false);
                this.walkModel.setSelected(false);
                this.routeList.clear();
                this.adapter.notifyDataSetChanged();
                return;
            case 3:
                this.carModel.setSelected(false);
                this.busModel.setSelected(false);
                this.walkModel.setSelected(true);
                this.routeList.clear();
                this.adapter.notifyDataSetChanged();
                return;
            default:
                return;
        }
    }

    private void loadAssignView(int mode) {
        if (mode == 1) {
            loadBusView();
        } else if (mode == 2) {
            loadDriveView();
        } else if (mode == 3) {
            loadWalkView();
        }
    }

    private void loadBusView() {
        RouteLoadingUtil.getInstance().show(this);
        this.navLayout.setVisibility(8);
        this.openMapBtn.setVisibility(8);
        this.searchMsgModel.setSearchType(1);
        setTrafficIcon(this.searchMsgModel.getSearchType());
        this.trafficPolicy.setVisibility(0);
        this.trafficPolicy.removeAllViews();
        this.trafficPolicy.addView(View.inflate(this, this.resource.getLayoutId("mc_place_route_bus_pattern"), null));
        final ArrayList<String> busTagList = new ArrayList();
        busTagList.add(this.resource.getString("mc_traffic_feel_faster"));
        busTagList.add(this.resource.getString("mc_traffic_feel_transfer"));
        busTagList.add(this.resource.getString("mc_traffic_feel_walk"));
        MCTabBarScrollView mcTabBarScrollView = (MCTabBarScrollView) findViewById(this.resource.getViewId("subnav_scrollview"));
        mcTabBarScrollView.setTabBoxView(this.resource.getDrawable("mc_forum_peripheral_tab_bg"), MCPhoneUtil.dip2px(context, 34.0f), MCPhoneUtil.getDisplayWidth(this));
        mcTabBarScrollView.setArrowView(this.resource.getDrawable("mc_forum_tab_style1_glide"), MCPhoneUtil.dip2px(context, 3.0f), MCPhoneUtil.getDisplayWidth(this));
        mcTabBarScrollView.setContainArrow(true);
        mcTabBarScrollView.init(context, busTagList, 3, new ClickSubNavListener() {
            public void onClickSubNav(View v, int position, TextView view) {

            }

            public void initTextView(TextView view) {
            }
        });
        this.searchMsgModel.setSearchPolicy(3);
    }

    private void loadWalkView() {
        this.otherMsg.removeAllViews();
        this.navLayout.setVisibility(0);
        this.openMapBtn.setClickable(false);
        this.openMapBtn.setVisibility(0);
        this.searchMsgModel.setSearchType(3);
        setTrafficIcon(this.searchMsgModel.getSearchType());
        this.trafficPolicy.removeAllViews();
        this.trafficPolicy.setVisibility(4);
        MCLogUtil.e("RouteSolutionDetailActivity", "loadWalkView  searchMsgModel == " + this.searchMsgModel);
        RouteLoadingUtil.getInstance().show(this);
    }

    private void loadDriveView() {
        if (this.busAdapter != null) {
            this.busAdapter.notifyDataSetChanged();
        }
        this.otherMsg.removeAllViews();
        this.navLayout.setVisibility(0);
        this.openMapBtn.setClickable(false);
        this.openMapBtn.setVisibility(0);
        this.searchMsgModel.setSearchType(2);
        setTrafficIcon(this.searchMsgModel.getSearchType());
        this.trafficPolicy.setVisibility(0);
        this.trafficPolicy.removeAllViews();
        this.trafficPolicy.addView(View.inflate(this, this.resource.getLayoutId("mc_place_route_car_pattern"), null));
        final ArrayList<String> busTagList = new ArrayList();
        busTagList.add(this.resource.getString("mc_traffic_route_least"));
        busTagList.add(this.resource.getString("mc_traffic_route_unimpeded"));
        busTagList.add(this.resource.getString("mc_traffic_route_short_road"));
        busTagList.add(this.resource.getString("mc_traffic_route_general_road"));
        MCTabBarScrollView mcTabBarScrollView = (MCTabBarScrollView) findViewById(this.resource.getViewId("subnav_scrollview"));
        mcTabBarScrollView.setTabBoxView(this.resource.getDrawable("mc_forum_peripheral_tab_bg"), MCPhoneUtil.dip2px(context, 34.0f), MCPhoneUtil.getDisplayWidth(this));
        mcTabBarScrollView.setArrowView(this.resource.getDrawable("mc_forum_tab_style1_glide"), MCPhoneUtil.dip2px(context, 3.0f), MCPhoneUtil.getDisplayWidth(this));
        mcTabBarScrollView.setContainArrow(true);
        mcTabBarScrollView.init(context, busTagList, 4, new ClickSubNavListener() {
            public void onClickSubNav(View v, int position, TextView view) {};

            public void initTextView(TextView view) {
            }
        });
        this.searchMsgModel.setSearchPolicy(0);
    }



    private String getTime(int second, int meter) {
        int h = second / 3600;
        float m = (((float) second) % 3600.0f) / 60.0f;
        DecimalFormat df1 = new DecimalFormat("###");
        DecimalFormat df2 = new DecimalFormat("###.0");
        if (h == 0) {
            return this.resource.getString("mc_traffic_route_about_time") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) meter) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km");
        }
        return this.resource.getString("mc_traffic_route_about_time") + h + this.resource.getString("mc_traffic_route_about_hour") + df1.format((double) m) + this.resource.getString("mc_traffic_route_minute") + "/" + df2.format((double) (((float) meter) / 1000.0f)) + this.resource.getString("mc_traffic_route_distance_km");
    }

    private void showTimePicker(final TextView tv) {
        OnTimeSetListener tpdl = new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strMinute = minute + "";
                if (minute <= 9) {
                    strMinute = "0" + minute;
                }
                RouteSolutionDetailActivity.this.setDate = RouteSolutionDetailActivity.this.resource.getString("mc_place_today") + hourOfDay + ":" + strMinute + RouteSolutionDetailActivity.this.resource.getString("mc_place_go");
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.set(1, 2, 5, hourOfDay, minute);
                RouteSolutionDetailActivity.this.systemCurMil = calendar.getTimeInMillis();
                tv.setText(RouteSolutionDetailActivity.this.setDate);
                RouteSolutionDetailActivity.this.tpd.dismiss();
                RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
            }
        };
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.tpd = new TimePickerDialog(this, tpdl, calendar.get(11), calendar.get(12), true);
        this.tpd.show();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
