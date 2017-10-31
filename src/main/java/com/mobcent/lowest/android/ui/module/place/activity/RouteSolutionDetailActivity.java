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
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
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
    private BMapManager mBMapManager = null;
    private MapView mMapView;
    private MKSearch mSearch = null;
    private LinearLayout navLayout;
    private Button openMapBtn;
    private LinearLayout otherMsg;
    private NaviPara para = null;
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
    private MKTransitRouteResult transitRes = null;
    private Button walkModel;

    protected void initData() {
        super.initData();
        context = getApplicationContext();
        this.mBMapManager = PlaceManager.getInstance().getBMapManager(context);
        this.searchMsgModel = (RouteSearchMessageModel) getIntent().getBundleExtra(RouteConstant.SEARCHLOCATION).getSerializable(RouteConstant.SEARCH_MSG_MODEL);
        this.startCity = this.searchMsgModel.getStartCity();
        this.disCity = this.searchMsgModel.getDistanceCity();
        this.routeList = new ArrayList();
        this.mMapView = new MapView(this);
        this.adapter = new RouteCommentDetailAdapter(this, this.routeList, this.searchMsgModel);
        this.mSearch = new MKSearch();
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
        this.mSearch.init(this.mBMapManager, new MKSearchListener() {
            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
                RouteLoadingUtil.getInstance().hide();
                if (RouteSolutionDetailActivity.this.busAdapter != null) {
                    RouteSolutionDetailActivity.this.busAdapter.notifyDataSetChanged();
                }
                RouteSolutionDetailActivity.this.routeList.clear();
                RouteSolutionDetailActivity.this.adapter.notifyDataSetChanged();
                if (error != 4) {
                    if (error != 0 || res == null) {
                        RouteSolutionDetailActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    RouteSolutionDetailActivity.this.handlWalkAndCarResult(res.getPlan(0).getRoute(0), res.getTaxiPrice());
                }
            }

            public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
                if (error != 4) {
                    if (error != 0 || res == null) {
                        RouteSolutionDetailActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    View view = View.inflate(RouteSolutionDetailActivity.context, RouteSolutionDetailActivity.this.resource.getLayoutId("mc_place_route_time_picker"), null);
                    LinearLayout dataLayout = (LinearLayout) view.findViewById(RouteSolutionDetailActivity.this.resource.getViewId("place_route_bus_data_picker_layout"));
                    final ImageView iv = (ImageView) view.findViewById(RouteSolutionDetailActivity.this.resource.getViewId("route_time_picker_icon"));
                    final TextView timeText = (TextView) view.findViewById(RouteSolutionDetailActivity.this.resource.getViewId("route_time_picker_text"));
                    if (MCStringUtil.isEmpty(RouteSolutionDetailActivity.this.setDate)) {
                        timeText.setText(RouteSolutionDetailActivity.this.resource.getString("mc_place_today") + RouteSolutionDetailActivity.this.getNowTime() + RouteSolutionDetailActivity.this.resource.getString("mc_place_go"));
                        RouteSolutionDetailActivity.this.systemCurMil = System.currentTimeMillis();
                    } else {
                        timeText.setText(RouteSolutionDetailActivity.this.setDate);
                    }
                    RouteSolutionDetailActivity.this.otherMsg.removeAllViews();
                    RouteSolutionDetailActivity.this.otherMsg.addView(view);
                    RouteSolutionDetailActivity.this.otherMsg.setVisibility(0);
                    dataLayout.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            RouteSolutionDetailActivity.this.showTimePicker(timeText);
                        }
                    });
                    dataLayout.setOnTouchListener(new OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == 0 || event.getAction() == 2) {
                                iv.setPressed(true);
                            } else {
                                iv.setPressed(false);
                            }
                            return false;
                        }
                    });
                    RouteSolutionDetailActivity.this.transitRes = res;
                    RouteSolutionDetailActivity.this.busAdapter = new BusResultAdapter(RouteSolutionDetailActivity.context, RouteSolutionDetailActivity.this.resource, RouteSolutionDetailActivity.this.transitRes, RouteSolutionDetailActivity.this.searchMsgModel, RouteSolutionDetailActivity.this.systemCurMil);
                    RouteSolutionDetailActivity.this.trafficList.setAdapter(RouteSolutionDetailActivity.this.busAdapter);
                    RouteLoadingUtil.getInstance().hide();
                }
            }

            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
                RouteSolutionDetailActivity.this.routeList.clear();
                if (error != 4) {
                    if (error != 0 || res == null) {
                        RouteSolutionDetailActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    RouteSolutionDetailActivity.this.handlWalkAndCarResult(res.getPlan(0).getRoute(0), res.getTaxiPrice());
                    RouteLoadingUtil.getInstance().hide();
                }
            }

            public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
            }

            public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
            }

            public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
            }

            public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
            }

            public void onGetPoiDetailSearchResult(int arg0, int arg1) {
            }

            public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
            }
        });
        this.startLocation.setText(this.searchMsgModel.getStartName());
        this.endLocation.setText(this.searchMsgModel.getDistanceName());
        loadAssignView(this.searchMsgModel.getSearchType());
    }

    private String getNowTime() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        return hour + ":" + time.minute;
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
                try {
                    RouteSolutionDetailActivity.this.para = new NaviPara();
                    GeoPointModel sPt = RouteSolutionDetailActivity.this.searchMsgModel.getStartGeoPointModel();
                    GeoPointModel nPt = RouteSolutionDetailActivity.this.searchMsgModel.getEndPointModel();
                    if (sPt != null) {
                        RouteSolutionDetailActivity.this.para.startPoint = new GeoPoint(sPt.getLatitudeE6(), sPt.getLongitudeE6());
                    }
                    RouteSolutionDetailActivity.this.para.startName = RouteSolutionDetailActivity.this.searchMsgModel.getStartName();
                    if (nPt != null) {
                        MCLogUtil.e("RouteSolutionDetailActivity", "进来了");
                        RouteSolutionDetailActivity.this.para.endPoint = new GeoPoint(nPt.getLatitudeE6(), nPt.getLongitudeE6());
                    }
                    RouteSolutionDetailActivity.this.para.endName = RouteSolutionDetailActivity.this.searchMsgModel.getDistanceName();
                    MCLogUtil.e("RouteSolutionDetailActivity", "开始 = " + RouteSolutionDetailActivity.this.para.startName + "  座標：" + RouteSolutionDetailActivity.this.para.startPoint.getLatitudeE6() + " " + RouteSolutionDetailActivity.this.para.startPoint.getLongitudeE6());
                    MCLogUtil.e("RouteSolutionDetailActivity", "结束 = " + RouteSolutionDetailActivity.this.para.endName + "  座標：" + RouteSolutionDetailActivity.this.para.endPoint.getLatitudeE6() + " " + RouteSolutionDetailActivity.this.para.endPoint.getLongitudeE6());
                    BaiduMapNavigation.openBaiduMapNavi(RouteSolutionDetailActivity.this.para, RouteSolutionDetailActivity.this);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                    Builder builder = new Builder(RouteSolutionDetailActivity.this);
                    builder.setMessage(RouteSolutionDetailActivity.this.resource.getString("mc_place_baidu_map_setup_msg"));
                    builder.setTitle(RouteSolutionDetailActivity.this.resource.getString("mc_place_baidu_map_prompt"));
                    builder.setPositiveButton(RouteSolutionDetailActivity.this.resource.getString("mc_place_baidu_map_confirm"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BaiduMapNavigation.GetLatestBaiduMapApp(RouteSolutionDetailActivity.this);
                        }
                    });
                    builder.setNegativeButton(RouteSolutionDetailActivity.this.resource.getString("mc_place_baidu_map_web_nav"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BaiduMapNavigation.openWebBaiduMapNavi(RouteSolutionDetailActivity.this.para, RouteSolutionDetailActivity.this);
                        }
                    });
                    builder.create().show();
                }
            }
        });
        this.shareLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    private void handlWalkAndCarResult(MKRoute walkRoute, int taxiMoney) {
        int distance = walkRoute.getDistance();
        int time = walkRoute.getTime();
        TextView tv = new TextView(this);
        tv.setTextColor(this.resource.getColor("mc_forum_text4_desc_normal_color"));
        String aboutMsg = getTime(time, distance);
        if (taxiMoney != 0) {
            aboutMsg = taxiMoney + this.resource.getString("mc_place_rmb_unit") + "/" + aboutMsg;
        }
        tv.setText(aboutMsg);
        this.otherMsg.removeAllViews();
        this.otherMsg.addView(tv);
        this.otherMsg.setVisibility(0);
        int sNum = walkRoute.getNumSteps();
        for (int i = 0; i < sNum; i++) {
            this.routeList.add(new String[]{walkRoute.getRouteType() + "", walkRoute.getStep(i).getContent()});
        }
        this.routeList.add(0, new String[]{"", this.searchMsgModel.getStartName()});
        this.routeList.add(new String[]{"", this.searchMsgModel.getDistanceName()});
        this.trafficList.setAdapter(this.adapter);
        this.adapter.updataData(this.routeList, this.searchMsgModel);
        this.adapter.notifyDataSetChanged();
        this.openMapBtn.setClickable(true);
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
                if (view.getText().equals(busTagList.get(0))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(3);
                    RouteSolutionDetailActivity.this.mSearch.setTransitPolicy(3);
                    RouteSolutionDetailActivity.this.mSearch.transitSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                } else if (view.getText().equals(busTagList.get(1))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(4);
                    RouteSolutionDetailActivity.this.mSearch.setTransitPolicy(4);
                    RouteSolutionDetailActivity.this.mSearch.transitSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                } else if (view.getText().equals(busTagList.get(2))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(5);
                    RouteSolutionDetailActivity.this.mSearch.setTransitPolicy(5);
                    RouteSolutionDetailActivity.this.mSearch.transitSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                }
            }

            public void initTextView(TextView view) {
            }
        });
        this.searchMsgModel.setSearchPolicy(3);
        this.mSearch.setTransitPolicy(3);
        this.mSearch.transitSearch(this.startCity, getNode(this.searchMsgModel.getStartLocation(), getStartGeoPoint(this.searchMsgModel)), getNode(this.searchMsgModel.getDistanceLocation(), getEndGeoPoint(this.searchMsgModel)));
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
        this.mSearch.walkingSearch(this.startCity, getNode(this.searchMsgModel.getStartLocation(), getStartGeoPoint(this.searchMsgModel)), this.disCity, getNode(this.searchMsgModel.getDistanceLocation(), getEndGeoPoint(this.searchMsgModel)));
        RouteLoadingUtil.getInstance().show(this);
    }

    private void loadDriveView() {
        if (this.busAdapter != null) {
            MKTransitRouteResult busResult = this.busAdapter.getBusResult();
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
            public void onClickSubNav(View v, int position, TextView view) {
                if (view.getText().equals(busTagList.get(0))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(0);
                    RouteSolutionDetailActivity.this.mSearch.setDrivingPolicy(0);
                    RouteSolutionDetailActivity.this.mSearch.drivingSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.disCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                } else if (view.getText().equals(busTagList.get(1))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(-1);
                    RouteSolutionDetailActivity.this.mSearch.setDrivingPolicy(-1);
                    RouteSolutionDetailActivity.this.mSearch.drivingSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.disCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                } else if (view.getText().equals(busTagList.get(2))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(1);
                    RouteSolutionDetailActivity.this.mSearch.setDrivingPolicy(1);
                    RouteSolutionDetailActivity.this.mSearch.drivingSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.disCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                } else if (view.getText().equals(busTagList.get(3))) {
                    RouteSolutionDetailActivity.this.searchMsgModel.setSearchPolicy(2);
                    RouteSolutionDetailActivity.this.mSearch.setDrivingPolicy(2);
                    RouteSolutionDetailActivity.this.mSearch.drivingSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.disCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
                    RouteLoadingUtil.getInstance().show(RouteSolutionDetailActivity.this);
                }
            }

            public void initTextView(TextView view) {
            }
        });
        this.searchMsgModel.setSearchPolicy(0);
        this.mSearch.setDrivingPolicy(0);
        this.mSearch.drivingSearch(this.startCity, getNode(this.searchMsgModel.getStartLocation(), getStartGeoPoint(this.searchMsgModel)), this.disCity, getNode(this.searchMsgModel.getDistanceLocation(), getEndGeoPoint(this.searchMsgModel)));
    }

    private GeoPoint getStartGeoPoint(RouteSearchMessageModel searchMsgModel) {
        if (searchMsgModel.getStartGeoPointModel() != null) {
            return new GeoPoint(searchMsgModel.getStartGeoPointModel().getLatitudeE6(), searchMsgModel.getStartGeoPointModel().getLongitudeE6());
        }
        return null;
    }

    private GeoPoint getEndGeoPoint(RouteSearchMessageModel searchMsgModel) {
        if (searchMsgModel.getEndPointModel() != null) {
            return new GeoPoint(searchMsgModel.getEndPointModel().getLatitudeE6(), searchMsgModel.getEndPointModel().getLongitudeE6());
        }
        return null;
    }

    private MKPlanNode getNode(String startPoint, GeoPoint pt) {
        MKPlanNode stNode = new MKPlanNode();
        stNode.name = startPoint;
        if (pt != null) {
            stNode.pt = pt;
        }
        return stNode;
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
                RouteSolutionDetailActivity.this.mSearch.setTransitPolicy(RouteSolutionDetailActivity.this.searchMsgModel.getSearchPolicy());
                RouteSolutionDetailActivity.this.mSearch.transitSearch(RouteSolutionDetailActivity.this.startCity, RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getStartLocation(), RouteSolutionDetailActivity.this.getStartGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)), RouteSolutionDetailActivity.this.getNode(RouteSolutionDetailActivity.this.searchMsgModel.getDistanceLocation(), RouteSolutionDetailActivity.this.getEndGeoPoint(RouteSolutionDetailActivity.this.searchMsgModel)));
            }
        };
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.tpd = new TimePickerDialog(this, tpdl, calendar.get(11), calendar.get(12), true);
        this.tpd.show();
    }

    protected void onPause() {
        this.mMapView.onPause();
        super.onPause();
    }

    protected void onResume() {
        this.mMapView.onResume();
        super.onResume();
    }

    protected void onDestroy() {
        this.mMapView.destroy();
        this.mSearch.destory();
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mMapView.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMapView.onRestoreInstanceState(savedInstanceState);
    }
}
