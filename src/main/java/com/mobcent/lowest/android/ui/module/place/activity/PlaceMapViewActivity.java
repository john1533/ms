package com.mobcent.lowest.android.ui.module.place.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
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
import com.mobcent.lowest.android.ui.module.place.module.route.model.GeoPointModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil;
import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil.RouteLocationCallBack;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;

public class PlaceMapViewActivity extends BasePlaceFragmentActivity {
    private final String TAG = "PlaceMapViewActivity";
    private BMapManager bManager = null;
    private Bundle bundle;
    private CurrentLocationCallback callback;
    private Context context;
    private Button findMe;
    private Button finishBtn;
    private boolean isShowTraffic = false;
    private LocationData locData = null;
    private E_BUTTON_TYPE mCurBtnType = E_BUTTON_TYPE.LOC;
    private LocationClient mLocClient;
    private MapController mMapController = null;
    private MapView mMapView;
    private MKSearch mSearch = null;
    private int maxZoomLevel;
    private int minZoomLevel;
    private Button multiBtn;
    private MyLocationListenner myListener = new MyLocationListenner();
    private locationOverlay myLocationOverlay = null;
    private TextView navTopbar;
    private Button nextStep;
    private int nodeIndex = -2;
    private NaviPara para = null;
    private PopupOverlay pop = null;
    private TextView popupText = null;
    private Button preStep;
    private PlaceLocationUtil realLocationUtil;
    private MKRoute route = null;
    private RouteOverlay routeOverlay = null;
    private RouteSearchMessageModel searchMsgModel;
    private Button traffic;
    private TransitOverlay transitOverlay = null;
    private Button zoomIn;
    private Button zoomOut;

    private enum E_BUTTON_TYPE {
        LOC,
        COMPASS,
        FOLLOW
    }

    class CurrentLocationCallback implements RouteLocationCallBack {
        CurrentLocationCallback() {
        }

        public void onGetMyPosition(BDLocation location) {
            PlaceMapViewActivity.this.setLocation(location);
        }

        public void onGetPoi(BDLocation poiLocation) {
        }
    }

    public class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                PlaceMapViewActivity.this.setLocation(location);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public class locationOverlay extends MyLocationOverlay {
        public locationOverlay(MapView mapView) {
            super(mapView);
        }

        protected boolean dispatchTap() {
            View popView = View.inflate(PlaceMapViewActivity.this.context, PlaceMapViewActivity.this.resource.getLayoutId("mc_place_route_map_view_pop_text"), null);
            PlaceMapViewActivity.this.handlePop(popView, (TextView) popView.findViewById(PlaceMapViewActivity.this.resource.getViewId("route_map_view_pop_text")), new GeoPoint((int) (PlaceMapViewActivity.this.locData.latitude * 1000000.0d), (int) (PlaceMapViewActivity.this.locData.longitude * 1000000.0d)), "我的位置");
            return true;
        }
    }

    private void setLocation(BDLocation location) {
        this.locData.latitude = location.getLatitude();
        this.locData.longitude = location.getLongitude();
        this.locData.accuracy = location.getRadius();
        this.locData.direction = location.getDerect();
        this.myLocationOverlay.setData(this.locData);
        this.mMapView.refresh();
        this.mMapController.animateTo(new GeoPoint((int) (this.locData.latitude * 1000000.0d), (int) (this.locData.longitude * 1000000.0d)));
        this.mMapView.refresh();
    }

    protected void initData() {
        super.initData();
        this.context = getApplication();
        this.bundle = getIntent().getBundleExtra(RouteConstant.MAP_VIEW_BUNDLE);
        this.searchMsgModel = (RouteSearchMessageModel) this.bundle.getSerializable(RouteConstant.SEARCH_MSG_MODEL);
        this.bManager = PlaceManager.getInstance().getBMapManager(this.context);
        this.mSearch = new MKSearch();
        this.locData = new LocationData();
        this.callback = new CurrentLocationCallback();
        this.realLocationUtil = PlaceLocationUtil.getInstance();
        this.realLocationUtil.requestLocation(this.context, this.callback);
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_map_view"));
        this.finishBtn = (Button) findViewById("nav_detail_finish_btn");
        this.multiBtn = (Button) findViewById("nav_detail_multi_btn");
        this.navTopbar = (TextView) findViewById("nav_detail_topbar_title");
        this.findMe = (Button) findViewById("map_view_find_me");
        this.preStep = (Button) findViewById("map_view_pre_step");
        this.nextStep = (Button) findViewById("map_view_next_step");
        this.zoomIn = (Button) findViewById("map_view_zoom_in");
        this.zoomOut = (Button) findViewById("map_view_zoom_out");
        this.traffic = (Button) findViewById("map_view_traffic");
        this.traffic.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        this.navTopbar.setText(getTitleText(this.searchMsgModel.getSearchType()));
        this.multiBtn.setText(this.resource.getStringId("mc_place_route_map_nav"));
        this.mMapView = (MapView) findViewById("place_baidu_mapview");
        this.mMapView.setBuiltInZoomControls(false);
        this.mMapView.getController().setZoom(12.0f);
        this.mMapView.getController().enableClick(true);
        this.myLocationOverlay = new locationOverlay(this.mMapView);
        modifyLocationOverlayIcon(null);
        this.myLocationOverlay.setData(this.locData);
        this.myLocationOverlay.enableCompass();
        this.myLocationOverlay.setLocationMode(LocationMode.NORMAL);
        this.mMapView.getOverlays().add(this.myLocationOverlay);
        this.mMapView.showScaleControl(true);
        this.mMapView.refresh();
        this.maxZoomLevel = this.mMapView.getMaxZoomLevel();
        this.minZoomLevel = this.mMapView.getMinZoomLevel();
        this.mMapView.regMapTouchListner(new MKMapTouchListener() {
            public void onMapClick(GeoPoint point) {
                if (PlaceMapViewActivity.this.pop != null) {
                    PlaceMapViewActivity.this.pop.hidePop();
                }
            }

            public void onMapDoubleClick(GeoPoint point) {
            }

            public void onMapLongClick(GeoPoint point) {
            }
        });
        this.mMapController = this.mMapView.getController();
        modifyLocationOverlayIcon(null);
        createPaopao();
        this.mSearch.init(this.bManager, new MKSearchListener() {
            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
                if (error != 4) {
                    if (error != 0 || res == null) {
                        PlaceMapViewActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    PlaceMapViewActivity.this.route = res.getPlan(0).getRoute(0);
                    PlaceMapViewActivity.this.routeOverlay = new RouteOverlay(PlaceMapViewActivity.this, PlaceMapViewActivity.this.mMapView);
                    PlaceMapViewActivity.this.routeOverlay.setData(PlaceMapViewActivity.this.route);
                    PlaceMapViewActivity.this.mMapView.getOverlays().clear();
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.routeOverlay);
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.myLocationOverlay);
                    PlaceMapViewActivity.this.mMapView.refresh();
                    PlaceMapViewActivity.this.mMapView.getController().zoomToSpan(PlaceMapViewActivity.this.routeOverlay.getLatSpanE6(), PlaceMapViewActivity.this.routeOverlay.getLonSpanE6());
                    PlaceMapViewActivity.this.mMapView.getController().animateTo(res.getStart().pt);
                    PlaceMapViewActivity.this.nodeIndex = -1;
                }
            }

            public void onGetTransitRouteResult(MKTransitRouteResult result, int error) {
                if (error != 4) {
                    if (error != 0 || result == null) {
                        PlaceMapViewActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    PlaceMapViewActivity.this.transitOverlay = new TransitOverlay(PlaceMapViewActivity.this, PlaceMapViewActivity.this.mMapView);
                    PlaceMapViewActivity.this.transitOverlay.setData(result.getPlan(PlaceMapViewActivity.this.searchMsgModel.getSearchNum()));
                    PlaceMapViewActivity.this.mMapView.getOverlays().clear();
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.transitOverlay);
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.myLocationOverlay);
                    PlaceMapViewActivity.this.mMapView.refresh();
                    PlaceMapViewActivity.this.mMapView.getController().zoomToSpan(PlaceMapViewActivity.this.transitOverlay.getLatSpanE6(), PlaceMapViewActivity.this.transitOverlay.getLonSpanE6());
                    PlaceMapViewActivity.this.mMapView.getController().animateTo(result.getStart().pt);
                    PlaceMapViewActivity.this.nodeIndex = 0;
                }
            }

            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
                if (error != 4) {
                    if (error != 0 || res == null) {
                        PlaceMapViewActivity.this.warn("mc_place_route_can_not_find_result");
                        return;
                    }
                    PlaceMapViewActivity.this.route = res.getPlan(0).getRoute(0);
                    PlaceMapViewActivity.this.routeOverlay = new RouteOverlay(PlaceMapViewActivity.this, PlaceMapViewActivity.this.mMapView);
                    PlaceMapViewActivity.this.routeOverlay.setData(PlaceMapViewActivity.this.route);
                    PlaceMapViewActivity.this.mMapView.getOverlays().clear();
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.routeOverlay);
                    PlaceMapViewActivity.this.mMapView.getOverlays().add(PlaceMapViewActivity.this.myLocationOverlay);
                    PlaceMapViewActivity.this.mMapView.refresh();
                    PlaceMapViewActivity.this.mMapView.getController().zoomToSpan(PlaceMapViewActivity.this.routeOverlay.getLatSpanE6(), PlaceMapViewActivity.this.routeOverlay.getLonSpanE6());
                    PlaceMapViewActivity.this.mMapView.getController().animateTo(res.getStart().pt);
                    PlaceMapViewActivity.this.nodeIndex = -1;
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int searchType = PlaceMapViewActivity.this.searchMsgModel.getSearchType();
                if (searchType == 1) {
                    PlaceMapViewActivity.this.mSearch.setTransitPolicy(PlaceMapViewActivity.this.searchMsgModel.getSearchPolicy());
                    PlaceMapViewActivity.this.mSearch.transitSearch(PlaceMapViewActivity.this.searchMsgModel.getStartCity(), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getStartLocation(), PlaceMapViewActivity.this.getStartGeoPoint(PlaceMapViewActivity.this.searchMsgModel)), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getDistanceLocation(), PlaceMapViewActivity.this.getEndGeoPoint(PlaceMapViewActivity.this.searchMsgModel)));
                } else if (searchType == 2) {
                    PlaceMapViewActivity.this.mSearch.setDrivingPolicy(PlaceMapViewActivity.this.searchMsgModel.getSearchPolicy());
                    PlaceMapViewActivity.this.mSearch.drivingSearch(PlaceMapViewActivity.this.searchMsgModel.getStartCity(), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getStartLocation(), PlaceMapViewActivity.this.getStartGeoPoint(PlaceMapViewActivity.this.searchMsgModel)), PlaceMapViewActivity.this.searchMsgModel.getDistanceCity(), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getDistanceLocation(), PlaceMapViewActivity.this.getEndGeoPoint(PlaceMapViewActivity.this.searchMsgModel)));
                } else if (searchType == 3) {
                    PlaceMapViewActivity.this.mSearch.walkingSearch(PlaceMapViewActivity.this.searchMsgModel.getStartCity(), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getStartLocation(), PlaceMapViewActivity.this.getStartGeoPoint(PlaceMapViewActivity.this.searchMsgModel)), PlaceMapViewActivity.this.searchMsgModel.getDistanceCity(), PlaceMapViewActivity.this.getNode(PlaceMapViewActivity.this.searchMsgModel.getDistanceLocation(), PlaceMapViewActivity.this.getEndGeoPoint(PlaceMapViewActivity.this.searchMsgModel)));
                }
            }
        }, 500);
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

    protected void initActions() {
        this.finishBtn.setOnClickListener(this.clickListener);
        this.findMe.setOnClickListener(this.clickListener);
        this.traffic.setOnClickListener(this.clickListener);
        this.zoomIn.setOnClickListener(this.clickListener);
        this.zoomOut.setOnClickListener(this.clickListener);
        this.preStep.setOnClickListener(this.clickListener);
        this.nextStep.setOnClickListener(this.clickListener);
        this.multiBtn.setOnClickListener(this.clickListener);
    }

    protected void onViewClick(View v) {
        if (this.finishBtn.equals(v)) {
            finish();
        }
        if (this.findMe.equals(v)) {
            switch (this.mCurBtnType) {
                case LOC:
                    this.myLocationOverlay.setLocationMode(LocationMode.NORMAL);
                    this.findMe.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button6_n"));
                    this.mCurBtnType = E_BUTTON_TYPE.COMPASS;
                    findMyLocation();
                    return;
                case COMPASS:
                    this.myLocationOverlay.setLocationMode(LocationMode.COMPASS);
                    this.findMe.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button5_h"));
                    this.mCurBtnType = E_BUTTON_TYPE.FOLLOW;
                    return;
                case FOLLOW:
                    this.myLocationOverlay.enableCompass();
                    this.myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
                    this.findMe.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button5_n"));
                    this.mCurBtnType = E_BUTTON_TYPE.LOC;
                    return;
                default:
                    return;
            }
        } else if (this.traffic.equals(v)) {
            controlTrafficView();
        } else if (this.zoomIn.equals(v)) {
            zoomInMap();
        } else if (this.zoomOut.equals(v)) {
            zoomOutMap();
        } else if (this.preStep.equals(v)) {
            nodeClick(v);
        } else if (this.nextStep.equals(v)) {
            nodeClick(v);
        } else if (this.multiBtn.equals(v)) {
            try {
                this.para = new NaviPara();
                GeoPointModel sPt = this.searchMsgModel.getStartGeoPointModel();
                GeoPointModel nPt = this.searchMsgModel.getEndPointModel();
                if (sPt != null) {
                    this.para.startPoint = new GeoPoint(sPt.getLatitudeE6(), sPt.getLongitudeE6());
                }
                this.para.startName = this.searchMsgModel.getStartName();
                if (nPt != null) {
                    this.para.endPoint = new GeoPoint(nPt.getLatitudeE6(), nPt.getLongitudeE6());
                }
                this.para.endName = this.searchMsgModel.getDistanceName();
                BaiduMapNavigation.openBaiduMapNavi(this.para, this);
            } catch (BaiduMapAppNotSupportNaviException e) {
                e.printStackTrace();
                Builder builder = new Builder(this);
                builder.setMessage(this.resource.getString("mc_place_baidu_map_setup_msg"));
                builder.setTitle(this.resource.getString("mc_place_baidu_map_prompt"));
                builder.setPositiveButton(this.resource.getString("mc_place_baidu_map_confirm"), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BaiduMapNavigation.GetLatestBaiduMapApp(PlaceMapViewActivity.this);
                    }
                });
                builder.setNegativeButton(this.resource.getString("mc_place_baidu_map_web_nav"), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BaiduMapNavigation.openWebBaiduMapNavi(PlaceMapViewActivity.this.para, PlaceMapViewActivity.this);
                    }
                });
                builder.create().show();
            }
        }
    }

    private void findMyLocation() {
        PlaceLocationHelper.getInastance().getCurrentLocation(this.context, true, new LocationDelegate() {
            public void onReceiveLocation(BDLocation location) {
                PlaceMapViewActivity.this.setLocation(location);
            }
        });
    }

    private void zoomOutMap() {
        if (this.mMapView.getZoomLevel() <= ((float) this.minZoomLevel)) {
            warn("mc_bd_map_zoom_minimum");
        } else {
            this.mMapController.zoomOut();
        }
    }

    private void zoomInMap() {
        if (this.mMapView.getZoomLevel() >= ((float) this.maxZoomLevel)) {
            warn("mc_bd_map_zoom_maximum");
        } else {
            this.mMapController.zoomIn();
        }
    }

    private String getTitleText(int searchType) {
        switch (searchType) {
            case 1:
                return this.resource.getString("mc_place_route_nav_bus_solution");
            case 2:
                return this.resource.getString("mc_place_route_nav_drive_solution");
            case 3:
                return this.resource.getString("mc_place_route_nav_walk_solution");
            case 4:
                return this.resource.getString("mc_place_route_nav_around");
            default:
                return null;
        }
    }

    public void createPaopao() {
        this.pop = new PopupOverlay(this.mMapView, new PopupClickListener() {
            public void onClickedPopup(int index) {
            }
        });
    }

    public void nodeClick(View v) {
        View popView = View.inflate(this.context, this.resource.getLayoutId("mc_place_route_map_view_pop_text"), null);
        TextView popText = (TextView) popView.findViewById(this.resource.getViewId("route_map_view_pop_text"));
        int searchType = this.searchMsgModel.getSearchType();
        if (searchType == 2 || searchType == 3) {
            if (this.nodeIndex >= -1 && this.route != null && this.nodeIndex < this.route.getNumSteps()) {
                if (this.preStep.equals(v) && this.nodeIndex > 0) {
                    this.nodeIndex--;
                    handlePop(popView, popText, this.route.getStep(this.nodeIndex).getPoint(), this.route.getStep(this.nodeIndex).getContent());
                }
                if (this.nextStep.equals(v) && this.nodeIndex < this.route.getNumSteps() - 1) {
                    this.nodeIndex++;
                    handlePop(popView, popText, this.route.getStep(this.nodeIndex).getPoint(), this.route.getStep(this.nodeIndex).getContent());
                }
            } else {
                return;
            }
        }
        if (searchType == 1 && this.nodeIndex >= -1 && this.transitOverlay != null && this.nodeIndex < this.transitOverlay.getAllItem().size()) {
            if (this.preStep.equals(v) && this.nodeIndex > 1) {
                this.nodeIndex--;
                handlePop(popView, popText, this.transitOverlay.getItem(this.nodeIndex).getPoint(), this.transitOverlay.getItem(this.nodeIndex).getTitle());
            }
            if (this.nextStep.equals(v) && this.nodeIndex < this.transitOverlay.getAllItem().size() - 2) {
                this.nodeIndex++;
                handlePop(popView, popText, this.transitOverlay.getItem(this.nodeIndex).getPoint(), this.transitOverlay.getItem(this.nodeIndex).getTitle());
            }
        }
    }

    private void handlePop(View popView, TextView popText, GeoPoint geoPoint, String tip) {
        this.pop.hidePop();
        this.mMapController.animateTo(geoPoint);
        popText.setText(tip);
        showPop(popView, geoPoint);
    }

    private void showPop(final View popView, final GeoPoint geoPoint) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                PlaceMapViewActivity.this.pop.showPopup(popView, geoPoint, 5);
            }
        }, 300);
    }

    public void modifyLocationOverlayIcon(Drawable marker) {
        this.myLocationOverlay.setMarker(marker);
        this.mMapView.refresh();
    }

    private void controlTrafficView() {
        if (this.isShowTraffic) {
            this.isShowTraffic = false;
            this.traffic.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        } else {
            this.traffic.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1_traffic"));
            this.isShowTraffic = true;
        }
        this.mMapView.setTraffic(this.isShowTraffic);
    }

    protected void onPause() {
        this.mMapView.onPause();
        super.onPause();
    }

    protected void onResume() {
        this.mMapView.onResume();
        if (this.realLocationUtil != null) {
            this.realLocationUtil.requestLocation(this.context, this.callback);
        }
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
        this.realLocationUtil.stopLocation();
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
