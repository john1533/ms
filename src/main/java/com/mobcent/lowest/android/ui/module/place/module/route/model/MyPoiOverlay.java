package com.mobcent.lowest.android.ui.module.place.module.route.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.AroundHotwordsActivity;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.RouteActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import com.mobcent.lowest.module.place.model.PlacePoiLocationModel;
import java.util.ArrayList;

public class MyPoiOverlay extends PoiOverlay {
    private Activity activity;
    private TextView average;
    private ImageView callIcon;
    private LinearLayout callLayout;
    private TextView callText;
    private int currentShow;
    private ImageView detailIcon;
    private ImageButton disIcon;
    private LinearLayout disLayout;
    private TextView disText;
    private Button finishBtn;
    private PlacePoiInfoModel info = null;
    private ImageView lineTwo;
    private MapView mMapView;
    private RelativeLayout msgOneLayout;
    private RelativeLayout msgTwoLayout;
    private RelativeLayout poiDetaiLayout;
    private TextView poiDetailText;
    private ArrayList<PlacePoiInfoModel> poiInfoList;
    private TextView poiName;
    private int preShow;
    private LinearLayout rankLayout;
    private TextView rankText;
    private RatingBar ratingBar;
    private MCResource resource;
    private ImageButton searchAround;
    private ImageButton searchAroundBtn;
    private LinearLayout searchLayout;
    private TextView searchText;
    private String telNum = null;

    public MyPoiOverlay(final Activity activity, MapView mapView, RelativeLayout layout, ArrayList<PlacePoiInfoModel> poiInfoList) {
        super(activity, mapView);
        this.activity = activity;
        this.poiInfoList = poiInfoList;
        this.poiDetaiLayout = layout;
        this.resource = MCResource.getInstance(activity);
        this.poiName = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_name"));
        this.poiDetailText = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_text"));
        this.ratingBar = (RatingBar) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_rating_bar"));
        this.rankText = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_rank_text"));
        this.average = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_average_expense_text"));
        this.searchAround = (ImageButton) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_search_around"));
        this.msgOneLayout = (RelativeLayout) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_msg_one"));
        this.msgTwoLayout = (RelativeLayout) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_msg_two"));
        this.disLayout = (LinearLayout) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_go_dis_layout"));
        this.searchLayout = (LinearLayout) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_search_around_layout"));
        this.disIcon = (ImageButton) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_go_dis_icon"));
        this.disText = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_go_dis_text"));
        this.callLayout = (LinearLayout) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_call_layout"));
        this.callIcon = (ImageView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_call_icon"));
        this.callText = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_call_text"));
        this.lineTwo = (ImageView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_layout_line_two"));
        this.searchText = (TextView) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_search_around_text"));
        this.searchAroundBtn = (ImageButton) this.poiDetaiLayout.findViewById(this.resource.getViewId("place_poi_detail_search_around"));
        this.msgOneLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PlazaWebViewActivity.class);
                intent.putExtra(PlazaConstant.WEB_VIEW_URL, MyPoiOverlay.this.info.detailInfoModel.detailUrl);
                intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
                activity.startActivity(intent);
            }
        });
        this.msgTwoLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PlazaWebViewActivity.class);
                intent.putExtra(PlazaConstant.WEB_VIEW_URL, MyPoiOverlay.this.info.detailInfoModel.detailUrl);
                intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
                activity.startActivity(intent);
            }
        });
        this.callLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("tel:" + MyPoiOverlay.this.info.telephone)));
            }
        });
        this.disLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, RouteActivity.class);
                intent.putExtra(RouteConstant.POI_MODEL_LIST, MyPoiOverlay.this.info);
                activity.startActivity(intent);
            }
        });
        this.searchLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, AroundHotwordsActivity.class));
            }
        });
        this.callLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    MyPoiOverlay.this.callIcon.setPressed(true);
                    MyPoiOverlay.this.callText.setPressed(true);
                } else if (event.getAction() == 1) {
                    MyPoiOverlay.this.callIcon.setPressed(false);
                    MyPoiOverlay.this.callText.setPressed(false);
                }
                return false;
            }
        });
        this.disLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    MyPoiOverlay.this.disIcon.setPressed(true);
                    MyPoiOverlay.this.disText.setPressed(true);
                } else if (event.getAction() == 1) {
                    MyPoiOverlay.this.disIcon.setPressed(false);
                    MyPoiOverlay.this.disText.setPressed(false);
                }
                return false;
            }
        });
        this.searchLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    MyPoiOverlay.this.searchAround.setPressed(true);
                    MyPoiOverlay.this.searchText.setPressed(true);
                } else if (event.getAction() == 1) {
                    MyPoiOverlay.this.searchAround.setPressed(false);
                    MyPoiOverlay.this.searchText.setPressed(false);
                }
                return false;
            }
        });
        this.searchAroundBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, AroundHotwordsActivity.class);
                PlacePoiLocationModel locationModel = new PlacePoiLocationModel();
                locationModel.setAddress(MyPoiOverlay.this.info.address);
                locationModel.setAreaCode(MyPoiOverlay.this.info.locationModel.getAreaCode());
                locationModel.setCity(MyPoiOverlay.this.info.locationModel.getCity());
                locationModel.setLat(MyPoiOverlay.this.info.locationModel.getLat());
                locationModel.setLng(MyPoiOverlay.this.info.locationModel.getLng());
                intent.putExtra("location", locationModel);
                activity.startActivity(intent);
            }
        });
    }

    protected boolean onTap(int i) {
        this.currentShow = i;
        if (this.preShow == 0) {
            this.preShow = i;
        }
        int visibili = this.poiDetaiLayout.getVisibility();
        if (this.preShow == i && visibili == 0) {
            this.poiDetaiLayout.setVisibility(8);
        } else {
            this.poiDetaiLayout.setVisibility(0);
            this.info = (PlacePoiInfoModel) this.poiInfoList.get(i);
            this.telNum = this.info.telephone;
            if (MCStringUtil.isEmpty(this.telNum)) {
                this.lineTwo.setVisibility(8);
                this.callLayout.setVisibility(8);
            } else {
                this.lineTwo.setVisibility(0);
                this.callLayout.setVisibility(0);
            }
            this.poiName.setText((i + 1) + "." + this.info.name);
            this.rankText.setText(this.info.detailInfoModel.overallRating);
            this.average.setText(this.info.detailInfoModel.price);
            if (!MCStringUtil.isEmpty(this.info.detailInfoModel.overallRating)) {
                this.ratingBar.setRating(((float) Math.round(Float.valueOf(this.info.detailInfoModel.overallRating).floatValue() * 10.0f)) / 10.0f);
            }
        }
        this.preShow = i;
        return true;
    }
}
