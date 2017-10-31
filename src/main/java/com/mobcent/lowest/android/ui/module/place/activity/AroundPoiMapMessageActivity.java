package com.mobcent.lowest.android.ui.module.place.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.manager.PlaceManager;
import com.mobcent.lowest.android.ui.module.place.module.route.model.MyPoiOverlay;
import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil;
import com.mobcent.lowest.android.ui.module.place.utils.PlaceLocationUtil.RouteLocationCallBack;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import java.io.File;
import java.util.ArrayList;

public class AroundPoiMapMessageActivity extends BasePlaceFragmentActivity {
    private String TAG = "AroundPoiMapMessageActivity";
    private BMapManager bManager = null;
    private CurrentLocationCallback callback;
    private Button findMeBtn;
    private Button finishBtn;
    private boolean isShowTraffic = false;
    private LocationData locData = null;
    private MapController mMapController = null;
    private MapView mMapView;
    private MKSearch mSearch = null;
    MKMapViewListener mapViewListener = new MKMapViewListener() {
        public void onMapMoveFinish() {
        }

        public void onClickMapPoi(MapPoi arg0) {
        }

        public void onGetCurrentMap(Bitmap bitmap) {
            String absPath = AroundPoiMapMessageActivity.this.getFilePathByContentResolver(AroundPoiMapMessageActivity.this.context, Uri.parse(Media.insertImage(AroundPoiMapMessageActivity.this.getContentResolver(), bitmap, "", "")));
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(new File(absPath)));
            AroundPoiMapMessageActivity.this.context.sendBroadcast(intent);
            AroundPoiMapMessageActivity.this.screenShotBtn.setBackgroundDrawable(AroundPoiMapMessageActivity.this.resource.getDrawable("mc_forum_map_button2"));
            AroundPoiMapMessageActivity.this.warnStr(AroundPoiMapMessageActivity.this.resource.getString("mc_place_route_screet_shot_suc") + absPath);
        }

        public void onMapAnimationFinish() {
        }

        public void onMapLoadFinish() {
            AroundPoiMapMessageActivity.this.mMapView.setScaleControlPosition(80, AroundPoiMapMessageActivity.this.mMapView.getHeight() - 40);
        }
    };
    private int maxZoomLevel;
    private int minZoomLevel;
    private locationOverlay myLocationOverlay = null;
    private RelativeLayout poiDetaiLayout;
    private ArrayList<PlacePoiInfoModel> poiInfoList;
    private ArrayList<MKPoiInfo> poiList;
    private PlaceLocationUtil realLocationUtil;
    private Button screenShotBtn;
    private Button trafficBtn;
    private Button zoomInBtn;
    private Button zoomOutBtn;

    class CurrentLocationCallback implements RouteLocationCallBack {
        CurrentLocationCallback() {
        }

        public void onGetMyPosition(BDLocation location) {
            AroundPoiMapMessageActivity.this.locData.latitude = location.getLatitude();
            AroundPoiMapMessageActivity.this.locData.longitude = location.getLongitude();
            AroundPoiMapMessageActivity.this.locData.accuracy = location.getRadius();
            AroundPoiMapMessageActivity.this.locData.direction = location.getDerect();
            AroundPoiMapMessageActivity.this.myLocationOverlay.setData(AroundPoiMapMessageActivity.this.locData);
        }

        public void onGetPoi(BDLocation poiLocation) {
        }
    }

    public class locationOverlay extends MyLocationOverlay {
        public locationOverlay(MapView mapView) {
            super(mapView);
        }

        protected boolean dispatchTap() {
            return true;
        }
    }

    protected void initData() {
        super.initData();
        this.mSearch = new MKSearch();
        this.poiInfoList = (ArrayList) getIntent().getSerializableExtra(RouteConstant.POI_MODEL_LIST);
        this.poiList = convertData(this.poiInfoList);
        this.bManager = PlaceManager.getInstance().getBMapManager(this.context);
        this.locData = new LocationData();
        this.callback = new CurrentLocationCallback();
        this.realLocationUtil = PlaceLocationUtil.getInstance();
        this.realLocationUtil.requestLocation(this.context, this.callback);
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_around_map_view"));
        this.poiDetaiLayout = (RelativeLayout) findViewById("place_around_poi_detail_layout");
        this.finishBtn = (Button) findViewById("place_around_map_view_finish");
        this.mMapView = (MapView) findViewById("place_around_poi_baidu_mapview");
        this.findMeBtn = (Button) findViewById("place_around_poi_find_me_btn");
        this.zoomInBtn = (Button) findViewById("place_around_poi_zoom_in");
        this.zoomOutBtn = (Button) findViewById("place_around_poi_zoom_out");
        this.trafficBtn = (Button) findViewById("place_around_poi_traffic");
        this.screenShotBtn = (Button) findViewById("place_around_poi_screen_shot");
        this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        this.screenShotBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button2"));
        this.maxZoomLevel = this.mMapView.getMaxZoomLevel();
        this.minZoomLevel = this.mMapView.getMinZoomLevel();
        this.poiDetaiLayout.setVisibility(View.GONE);
        this.myLocationOverlay = new locationOverlay(this.mMapView);
        this.myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
        this.myLocationOverlay.setData(this.locData);
        this.mMapController = this.mMapView.getController();
        this.mMapController.enableClick(true);
        this.mMapController.setZoom(18.0f);
    }

    protected void initActions() {
        this.finishBtn.setOnClickListener(this.clickListener);
        this.findMeBtn.setOnClickListener(this.clickListener);
        this.zoomInBtn.setOnClickListener(this.clickListener);
        this.zoomOutBtn.setOnClickListener(this.clickListener);
        this.trafficBtn.setOnClickListener(this.clickListener);
        this.screenShotBtn.setOnClickListener(this.clickListener);
        onGetPoiData(this.poiList);
    }

    private ArrayList<MKPoiInfo> convertData(ArrayList<PlacePoiInfoModel> poiInfoList) {
        ArrayList<MKPoiInfo> poiList = new ArrayList();
        int num = poiInfoList.size();
        for (int i = 0; i < num; i++) {
            MKPoiInfo mKPoiInfo = new MKPoiInfo();
            PlacePoiInfoModel poiInfoModel = (PlacePoiInfoModel) poiInfoList.get(i);
            mKPoiInfo.address = poiInfoModel.address;
            mKPoiInfo.city = poiInfoModel.locationModel.getCity();
            mKPoiInfo.name = poiInfoModel.name;
            mKPoiInfo.phoneNum = poiInfoModel.telephone;
            mKPoiInfo.pt = new GeoPoint(poiInfoModel.locationModel.getLatE6(), poiInfoModel.locationModel.getLngE6());
            mKPoiInfo.uid = poiInfoModel.uid;
            poiList.add(mKPoiInfo);
        }
        return poiList;
    }

    private void onGetPoiData(ArrayList<MKPoiInfo> poiList) {
        MyPoiOverlay poiOverlay = new MyPoiOverlay(this, this.mMapView, this.poiDetaiLayout, this.poiInfoList);
        poiOverlay.setData(poiList);
        this.mMapView.regMapViewListener(this.bManager, this.mapViewListener);
        this.mMapView.getOverlays().clear();
        this.mMapView.getOverlays().add(poiOverlay);
        this.mMapView.getOverlays().add(this.myLocationOverlay);
        this.mMapView.showScaleControl(true);
        this.mMapView.refresh();
        int num = poiList.size();
        for (int i = 0; i < num; i++) {
            MKPoiInfo info = (MKPoiInfo) poiList.get(i);
            if (info.pt != null) {
                this.mMapController.animateTo(info.pt);
                return;
            }
        }
    }

    private String getFilePathByContentResolver(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (c == null) {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
        try {
            if (c.getCount() == 1 && c.moveToFirst()) {
                filePath = c.getString(c.getColumnIndexOrThrow("_data"));
            }
            c.close();
            return filePath;
        } catch (Throwable th) {
            c.close();
        }
        return filePath;
    }

    protected void onViewClick(View v) {
        if (this.finishBtn.equals(v)) {
            finish();
        } else if (this.findMeBtn.equals(v)) {
            PlaceLocationHelper.getInastance().getCurrentLocation(this.context, false, new LocationDelegate() {
                public void onReceiveLocation(BDLocation location) {
                    AroundPoiMapMessageActivity.this.locData.latitude = location.getLatitude();
                    AroundPoiMapMessageActivity.this.locData.longitude = location.getLongitude();
                    AroundPoiMapMessageActivity.this.locData.accuracy = location.getRadius();
                    AroundPoiMapMessageActivity.this.locData.direction = location.getDerect();
                    AroundPoiMapMessageActivity.this.myLocationOverlay.setData(AroundPoiMapMessageActivity.this.locData);
                    AroundPoiMapMessageActivity.this.mMapView.refresh();
                    AroundPoiMapMessageActivity.this.mMapController.animateTo(new GeoPoint((int) (AroundPoiMapMessageActivity.this.locData.latitude * 1000000.0d), (int) (AroundPoiMapMessageActivity.this.locData.longitude * 1000000.0d)));
                }
            });
        } else if (this.zoomInBtn.equals(v)) {
            zoomInMap();
        } else if (this.zoomOutBtn.equals(v)) {
            zoomOutMap();
        } else if (this.trafficBtn.equals(v)) {
            controlTrafficView();
        } else if (this.screenShotBtn.equals(v)) {
            this.mMapView.getCurrentMap();
            this.screenShotBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button2_d"));
            warn("mc_place_route_screet_shot");
        }
    }

    private void zoomInMap() {
        if (this.mMapView.getZoomLevel() >= ((float) this.maxZoomLevel)) {
            warn("mc_bd_map_zoom_maximum");
        } else {
            this.mMapController.zoomIn();
        }
    }

    private void zoomOutMap() {
        if (this.mMapView.getZoomLevel() <= ((float) this.minZoomLevel)) {
            warn("mc_bd_map_zoom_minimum");
        } else {
            this.mMapController.zoomOut();
        }
    }

    private void controlTrafficView() {
        if (this.isShowTraffic) {
            this.isShowTraffic = false;
            this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1"));
        } else {
            this.trafficBtn.setBackgroundDrawable(this.resource.getDrawable("mc_forum_map_button1_traffic"));
            this.isShowTraffic = true;
        }
        this.mMapView.setTraffic(this.isShowTraffic);
    }

    public void onBackPressed() {
        if (this.poiDetaiLayout.getVisibility() == View.VISIBLE) {
            this.poiDetaiLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
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
