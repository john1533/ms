package com.mobcent.lowest.android.ui.module.place.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil;

public class PlaceMapViewActivity extends BasePlaceFragmentActivity {
    private final String TAG = "PlaceMapViewActivity";
    private Bundle bundle;
//    private CurrentLocationCallback callback;
    private Context context;
    private Button findMe;
    private Button finishBtn;
    private boolean isShowTraffic = false;
    private E_BUTTON_TYPE mCurBtnType = E_BUTTON_TYPE.LOC;
    private int maxZoomLevel;
    private int minZoomLevel;
    private Button multiBtn;
//    private MyLocationListenner myListener = new MyLocationListenner();
//    private locationOverlay myLocationOverlay = null;
    private TextView navTopbar;
    private Button nextStep;
    private int nodeIndex = -2;
    private TextView popupText = null;
    private Button preStep;
    private PlaceLocationUtil realLocationUtil;
    private RouteSearchMessageModel searchMsgModel;
    private Button traffic;
    private Button zoomIn;
    private Button zoomOut;

    private enum E_BUTTON_TYPE {
        LOC,
        COMPASS,
        FOLLOW
    }







    protected void initData() {
        super.initData();
        this.context = getApplication();
        this.bundle = getIntent().getBundleExtra(RouteConstant.MAP_VIEW_BUNDLE);
        this.searchMsgModel = (RouteSearchMessageModel) this.bundle.getSerializable(RouteConstant.SEARCH_MSG_MODEL);
        this.realLocationUtil = PlaceLocationUtil.getInstance();
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
        modifyLocationOverlayIcon(null);
        modifyLocationOverlayIcon(null);
        createPaopao();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                int searchType = PlaceMapViewActivity.this.searchMsgModel.getSearchType();
                if (searchType == 1) {
                } else if (searchType == 2) {
                } else if (searchType == 3) {
                }
            }
        }, 500);
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

        }
    }

    private void findMyLocation() {

    }

    private void zoomOutMap() {
//        if (this.mMapView.getZoomLevel() <= ((float) this.minZoomLevel)) {
//            warn("mc_bd_map_zoom_minimum");
//        } else {
//            this.mMapController.zoomOut();
//        }
    }

    private void zoomInMap() {
//        if (this.mMapView.getZoomLevel() >= ((float) this.maxZoomLevel)) {
//            warn("mc_bd_map_zoom_maximum");
//        } else {
//            this.mMapController.zoomIn();
//        }
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
//        this.pop = new PopupOverlay(this.mMapView, new PopupClickListener() {
//            public void onClickedPopup(int index) {
//            }
//        });
    }

    public void nodeClick(View v) {
        View popView = View.inflate(this.context, this.resource.getLayoutId("mc_place_route_map_view_pop_text"), null);
        TextView popText = (TextView) popView.findViewById(this.resource.getViewId("route_map_view_pop_text"));
        int searchType = this.searchMsgModel.getSearchType();
//        if (searchType == 2 || searchType == 3) {
//            if (this.nodeIndex >= -1 && this.route != null && this.nodeIndex < this.route.getNumSteps()) {
//                if (this.preStep.equals(v) && this.nodeIndex > 0) {
//                    this.nodeIndex--;
//                    handlePop(popView, popText, this.route.getStep(this.nodeIndex).getPoint(), this.route.getStep(this.nodeIndex).getContent());
//                }
//                if (this.nextStep.equals(v) && this.nodeIndex < this.route.getNumSteps() - 1) {
//                    this.nodeIndex++;
//                    handlePop(popView, popText, this.route.getStep(this.nodeIndex).getPoint(), this.route.getStep(this.nodeIndex).getContent());
//                }
//            } else {
//                return;
//            }
//        }
//        if (searchType == 1 && this.nodeIndex >= -1 && this.transitOverlay != null && this.nodeIndex < this.transitOverlay.getAllItem().size()) {
//            if (this.preStep.equals(v) && this.nodeIndex > 1) {
//                this.nodeIndex--;
//                handlePop(popView, popText, this.transitOverlay.getItem(this.nodeIndex).getPoint(), this.transitOverlay.getItem(this.nodeIndex).getTitle());
//            }
//            if (this.nextStep.equals(v) && this.nodeIndex < this.transitOverlay.getAllItem().size() - 2) {
//                this.nodeIndex++;
//                handlePop(popView, popText, this.transitOverlay.getItem(this.nodeIndex).getPoint(), this.transitOverlay.getItem(this.nodeIndex).getTitle());
//            }
//        }
    }



    public void modifyLocationOverlayIcon(Drawable marker) {
//        this.myLocationOverlay.setMarker(marker);
//        this.mMapView.refresh();
    }

    private void controlTrafficView() {
        if (this.isShowTraffic) {
            this.isShowTraffic = false;
            this.traffic.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        } else {
            this.traffic.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1_traffic"));
            this.isShowTraffic = true;
        }
//        this.mMapView.setTraffic(this.isShowTraffic);
    }

    protected void onPause() {
//        this.mMapView.onPause();
        super.onPause();
    }

    protected void onResume() {
//        this.mMapView.onResume();
        if (this.realLocationUtil != null) {
//            this.realLocationUtil.requestLocation(this.context, this.callback);
        }
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
//        this.mMapView.destroy();
//        this.mSearch.destory();
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        this.mMapView.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        this.mMapView.onRestoreInstanceState(savedInstanceState);
    }
}
