package com.mobcent.lowest.android.ui.module.place.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import java.util.ArrayList;

public class AroundPoiMapMessageActivity extends BasePlaceFragmentActivity {
    private String TAG = "AroundPoiMapMessageActivity";
//    private CurrentLocationCallback callback;
    private Button findMeBtn;
    private Button finishBtn;
    private boolean isShowTraffic = false;


    private int maxZoomLevel;
    private int minZoomLevel;
//    private locationOverlay myLocationOverlay = null;
    private RelativeLayout poiDetaiLayout;
    private ArrayList<PlacePoiInfoModel> poiInfoList;
    private PlaceLocationUtil realLocationUtil;
    private Button screenShotBtn;
    private Button trafficBtn;
    private Button zoomInBtn;
    private Button zoomOutBtn;





    protected void initData() {
//        super.initData();
//        this.mSearch = new MKSearch();
//        this.poiInfoList = (ArrayList) getIntent().getSerializableExtra(RouteConstant.POI_MODEL_LIST);
//        this.poiList = convertData(this.poiInfoList);
////        this.bManager = PlaceManager.getInstance().getBMapManager(this.context);
//        this.locData = new LocationData();
//        this.callback = new CurrentLocationCallback();
//        this.realLocationUtil = PlaceLocationUtil.getInstance();
//        this.realLocationUtil.requestLocation(this.context, this.callback);
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_around_map_view"));
        this.poiDetaiLayout = (RelativeLayout) findViewById("place_around_poi_detail_layout");
        this.finishBtn = (Button) findViewById("place_around_map_view_finish");
        this.findMeBtn = (Button) findViewById("place_around_poi_find_me_btn");
        this.zoomInBtn = (Button) findViewById("place_around_poi_zoom_in");
        this.zoomOutBtn = (Button) findViewById("place_around_poi_zoom_out");
        this.trafficBtn = (Button) findViewById("place_around_poi_traffic");
        this.screenShotBtn = (Button) findViewById("place_around_poi_screen_shot");
        this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        this.screenShotBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button2"));
//        this.maxZoomLevel = this.mMapView.getMaxZoomLevel();
//        this.minZoomLevel = this.mMapView.getMinZoomLevel();
        this.poiDetaiLayout.setVisibility(View.GONE);
//        this.myLocationOverlay = new locationOverlay(this.mMapView);
//        this.myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
//        this.myLocationOverlay.setData(this.locData);
//        this.mMapController = this.mMapView.getController();
//        this.mMapController.enableClick(true);
//        this.mMapController.setZoom(18.0f);
    }

    protected void initActions() {
        this.finishBtn.setOnClickListener(this.clickListener);
        this.findMeBtn.setOnClickListener(this.clickListener);
        this.zoomInBtn.setOnClickListener(this.clickListener);
        this.zoomOutBtn.setOnClickListener(this.clickListener);
        this.trafficBtn.setOnClickListener(this.clickListener);
        this.screenShotBtn.setOnClickListener(this.clickListener);
//        onGetPoiData(this.poiList);
    }






    protected void onViewClick(View v) {
        if (this.finishBtn.equals(v)) {
            finish();
        } else if (this.findMeBtn.equals(v)) {

        } else if (this.zoomInBtn.equals(v)) {
            zoomInMap();
        } else if (this.zoomOutBtn.equals(v)) {
            zoomOutMap();
        } else if (this.trafficBtn.equals(v)) {
            controlTrafficView();
        } else if (this.screenShotBtn.equals(v)) {
//            this.mMapView.getCurrentMap();
            this.screenShotBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button2_d"));
            warn("mc_place_route_screet_shot");
        }
    }

    private void zoomInMap() {
//        if (this.mMapView.getZoomLevel() >= ((float) this.maxZoomLevel)) {
//            warn("mc_bd_map_zoom_maximum");
//        } else {
//            this.mMapController.zoomIn();
//        }
    }

    private void zoomOutMap() {
//        if (this.mMapView.getZoomLevel() <= ((float) this.minZoomLevel)) {
//            warn("mc_bd_map_zoom_minimum");
//        } else {
//            this.mMapController.zoomOut();
//        }
    }

    private void controlTrafficView() {
        if (this.isShowTraffic) {
            this.isShowTraffic = false;
            this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        } else {
            this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1_traffic"));
            this.isShowTraffic = true;
        }
//        this.mMapView.setTraffic(this.isShowTraffic);
    }

    public void onBackPressed() {
        if (this.poiDetaiLayout.getVisibility() == View.VISIBLE) {
            this.poiDetaiLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    protected void onPause() {
//        this.mMapView.onPause();
        super.onPause();
    }

    protected void onResume() {
//        this.mMapView.onResume();
        super.onResume();
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
