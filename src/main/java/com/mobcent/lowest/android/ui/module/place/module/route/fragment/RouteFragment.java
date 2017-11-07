package com.mobcent.lowest.android.ui.module.place.module.route.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.activity.RouteSolutionDetailActivity;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.fragment.BasePlaceFragment;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.RouteSugDialogListAdapter;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.RouteSugDialogListAdapter.ClickSugItemListener;
import com.mobcent.lowest.android.ui.module.place.module.route.model.GeoPointModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import com.mobcent.lowest.android.ui.module.place.module.route.model.SearchConditionModel;
import com.mobcent.lowest.android.ui.module.place.utils.RouteLoadingUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import java.util.ArrayList;

public class RouteFragment extends BasePlaceFragment implements RouteConstant {
    private String TAG = "RouteFragment";
//    private MapView bDMapView;
    private LinearLayout bus;
    private ImageView busIcon;
    private TextView busText;
    private LinearLayout car;
    private ImageView carIcon;
    private TextView carText;
    private Button changeBtn;
    private int currentSearchType;
    private int currentSugLocation = 1;
    private AlertDialog dlg;
//    private ArrayList<MKPoiInfo> enPois;
    private ArrayAdapter<String> endAdapter = null;
    private boolean endChange = false;
    private String endCity;
    private boolean endIsOk;
    private SearchConditionModel endModel = null;
    private AutoCompleteTextView endNameET;
    private LinearLayout endPointLayout;
//    private LocationData locData = null;
//    private BMapManager mBMapManager = null;
//    private MKSearch mSearch = null;
    private int myLatitude;
//    private MyLocationOverlay myLocationOverlay = null;
    private int myLongitude;
    private PlacePoiInfoModel poiInfo;
//    private ArrayList<MKPoiInfo> stPois;
    private ArrayAdapter<String> startAdapter = null;
    private boolean startChange = false;
    private String startCity;
    private boolean startIsOk;
    private SearchConditionModel startModel = null;
    private AutoCompleteTextView startNameET;
    private LinearLayout startPointLayout;
    private LinearLayout walk;
    private ImageView walkIcon;
    private TextView walkText;


    protected void initData() {
        super.initData();
        this.poiInfo = (PlacePoiInfoModel) getActivity().getIntent().getSerializableExtra(RouteConstant.POI_MODEL_LIST);
        this.endModel = new SearchConditionModel();
        this.endModel.setHint(this.resource.getString("mc_place_route_input_dis_location"));
        this.startModel = new SearchConditionModel();
        this.startModel.setHint(this.resource.getString("mc_place_route_input_start_location"));
        if (this.poiInfo != null) {
            this.endModel.setLatitudeE6(this.poiInfo.locationModel.getLatE6());
            this.endModel.setLongitudeE6(this.poiInfo.locationModel.getLngE6());
            this.endModel.setPointName(this.poiInfo.name);
            this.endModel.setPointLocation(this.poiInfo.address);
        }
//        this.mBMapManager = PlaceManager.getInstance().getBMapManager(getActivity().getApplicationContext());
//        this.mSearch = new MKSearch();
//        this.mSearch.init(this.mBMapManager, new MKSearchListener() {
//            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
//                if (res != null && res.getAllSuggestions() != null) {
//                    Iterator i$;
//                    MKSuggestionInfo info;
//                    if (RouteFragment.this.currentSugLocation == 1) {
//                        RouteFragment.this.startAdapter.clear();
//                        i$ = res.getAllSuggestions().iterator();
//                        while (i$.hasNext()) {
//                            info = (MKSuggestionInfo) i$.next();
//                            if (info.key != null) {
//                                RouteFragment.this.startAdapter.add(info.key);
//                            }
//                        }
//                        RouteFragment.this.startAdapter.notifyDataSetChanged();
//                        return;
//                    }
//                    RouteFragment.this.endAdapter.clear();
//                    i$ = res.getAllSuggestions().iterator();
//                    while (i$.hasNext()) {
//                        info = (MKSuggestionInfo) i$.next();
//                        if (info.key != null) {
//                            RouteFragment.this.endAdapter.add(info.key);
//                        }
//                    }
//                    RouteFragment.this.endAdapter.notifyDataSetChanged();
//                }
//            }
//
//            public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
//                MCLogUtil.e(RouteFragment.this.TAG, "onGetTransitRouteResult get result" + res);
//                if (error == 4) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.stPois = res.getAddrResult().mStartPoiList;
//                    RouteFragment.this.enPois = res.getAddrResult().mEndPoiList;
//                    ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//                    ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
//                    if (res.getAddrResult() == null || res.getAddrResult().mEndPoiList == null || res.getAddrResult().mStartPoiList == null) {
//                        RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                    } else {
//                        RouteFragment.this.handleSugResult();
//                    }
//                } else if (error != 0 || res == null) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                } else {
//                    GeoPoint sp = res.getStart().pt;
//                    RouteFragment.this.startModel.setLatitudeE6(sp.getLatitudeE6());
//                    RouteFragment.this.startModel.setLongitudeE6(sp.getLongitudeE6());
//                    GeoPoint np = res.getEnd().pt;
//                    RouteFragment.this.endModel.setLatitudeE6(np.getLatitudeE6());
//                    RouteFragment.this.endModel.setLongitudeE6(np.getLongitudeE6());
//                    RouteFragment.this.startSearch();
//                }
//            }
//
//            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
//                MCLogUtil.e(RouteFragment.this.TAG, "onGetDrivingRouteResult get result " + res);
//                if (error == 4) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.stPois = res.getAddrResult().mStartPoiList;
//                    RouteFragment.this.enPois = res.getAddrResult().mEndPoiList;
//                    ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//                    ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
//                    if (res.getAddrResult() == null || res.getAddrResult().mEndPoiList == null || res.getAddrResult().mStartPoiList == null) {
//                        RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                    } else {
//                        RouteFragment.this.handleSugResult();
//                    }
//                } else if (error != 0 || res == null) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                } else {
//                    GeoPoint sp = res.getStart().pt;
//                    RouteFragment.this.startModel.setLatitudeE6(sp.getLatitudeE6());
//                    RouteFragment.this.startModel.setLongitudeE6(sp.getLongitudeE6());
//                    GeoPoint np = res.getEnd().pt;
//                    RouteFragment.this.endModel.setLatitudeE6(np.getLatitudeE6());
//                    RouteFragment.this.endModel.setLongitudeE6(np.getLongitudeE6());
//                    RouteFragment.this.startSearch();
//                }
//            }
//
//            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
//                MCLogUtil.e(RouteFragment.this.TAG, "onGetWalkingRouteResult get result" + res);
//                if (error == 4) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.stPois = res.getAddrResult().mStartPoiList;
//                    RouteFragment.this.enPois = res.getAddrResult().mEndPoiList;
//                    ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//                    ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
//                    if (res.getAddrResult() == null || res.getAddrResult().mEndPoiList == null || res.getAddrResult().mStartPoiList == null) {
//                        RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                    } else {
//                        RouteFragment.this.handleSugResult();
//                    }
//                } else if (error != 0 || res == null) {
//                    RouteLoadingUtil.getInstance().hide();
//                    RouteFragment.this.warn("mc_place_route_can_not_find_result");
//                } else {
//                    GeoPoint sp = res.getStart().pt;
//                    RouteFragment.this.startModel.setLatitudeE6(sp.getLatitudeE6());
//                    RouteFragment.this.startModel.setLongitudeE6(sp.getLongitudeE6());
//                    GeoPoint np = res.getEnd().pt;
//                    RouteFragment.this.endModel.setLatitudeE6(np.getLatitudeE6());
//                    RouteFragment.this.endModel.setLongitudeE6(np.getLongitudeE6());
//                    RouteFragment.this.startSearch();
//                }
//            }
//
//            public void onGetAddrResult(MKAddrInfo res, int error) {
//            }
//
//            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
//            }
//
//            public void onGetPoiDetailSearchResult(int type, int error) {
//            }
//
//            public void onGetPoiResult(MKPoiResult res, int type, int error) {
//            }
//
//            public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
//            }
//        });



//        this.locData = new LocationData();
    }

    private void handleSugResult() {

    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.resource.getLayoutId("mc_place_route_planning"), null);
        this.changeBtn = (Button) view.findViewById(this.resource.getViewId("route_plan_switch_btn"));
        this.bus = (LinearLayout) view.findViewById(this.resource.getViewId("route_plan_by_bus"));
        this.car = (LinearLayout) view.findViewById(this.resource.getViewId("route_plan_by_car"));
        this.walk = (LinearLayout) view.findViewById(this.resource.getViewId("route_plan_by_walk"));
        this.busIcon = (ImageView) view.findViewById(this.resource.getViewId("place_route_bus_icon"));
        this.carIcon = (ImageView) view.findViewById(this.resource.getViewId("place_route_car_icon"));
        this.walkIcon = (ImageView) view.findViewById(this.resource.getViewId("place_route_walk_icon"));
        this.busText = (TextView) view.findViewById(this.resource.getViewId("mc_place_route_bus_text"));
        this.carText = (TextView) view.findViewById(this.resource.getViewId("mc_place_route_car_text"));
        this.walkText = (TextView) view.findViewById(this.resource.getViewId("mc_place_route_walk_text"));
        this.changeBtn.setOnClickListener(this.clickListener);
        this.bus.setOnClickListener(this.clickListener);
        this.car.setOnClickListener(this.clickListener);
        this.walk.setOnClickListener(this.clickListener);
        this.startNameET = (AutoCompleteTextView) view.findViewById(this.resource.getViewId("route_plan_start_name"));
        this.endNameET = (AutoCompleteTextView) view.findViewById(this.resource.getViewId("route_plan_end_name"));
        this.startAdapter = new ArrayAdapter(this.context, 17367050);
        this.endAdapter = new ArrayAdapter(this.context, 17367050);
        this.startNameET.setAdapter(this.startAdapter);
        this.endNameET.setAdapter(this.endAdapter);
//        PlaceLocationHelper.getInastance().getCurrentLocation(this.context, false, new LocationDelegate() {
//            public void onReceiveLocation(BDLocation location) {
//                RouteFragment.this.startCity = location.getCity();
//                RouteFragment.this.endCity = RouteFragment.this.startCity;
//                RouteFragment.this.myLatitude = (int) (location.getLatitude() * 1000000.0d);
//                RouteFragment.this.myLongitude = (int) (location.getLongitude() * 1000000.0d);
//                RouteFragment.this.startModel.setLatitudeE6((int) (location.getLatitude() * 1000000.0d));
//                RouteFragment.this.startModel.setLongitudeE6((int) (location.getLongitude() * 1000000.0d));
//                RouteFragment.this.startModel.setHint(RouteFragment.this.resource.getString("mc_place_route_input_start_location"));
//                RouteFragment.this.startModel.setPointName(RouteFragment.this.resource.getString("mc_traffic_route_my_location"));
//                RouteFragment.this.startModel.setPointLocation(RouteFragment.this.resource.getString("mc_traffic_route_my_location"));
//                RouteFragment.this.startChange = true;
//                RouteFragment.this.startNameET.setText(RouteFragment.this.startModel.getPointName());
//                if (RouteFragment.this.startModel.getPointName().equals(RouteFragment.this.resource.getString("mc_traffic_route_my_location"))) {
//                    RouteFragment.this.startNameET.setTextColor(-16776961);
//                } else {
//                    RouteFragment.this.startNameET.setTextColor(-7829368);
//                }
//                RouteFragment.this.findMyLoc(location);
//            }
//        });
        if (this.endModel != null) {
            this.endNameET.setText(this.endModel.getPointName());
        }
        return view;
    }


    protected void initActions() {
        this.bus.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    RouteFragment.this.busIcon.setPressed(true);
                    RouteFragment.this.busText.setPressed(true);
                } else if (event.getAction() == 1) {
                    RouteFragment.this.busIcon.setPressed(false);
                    RouteFragment.this.busText.setPressed(false);
                }
                return false;
            }
        });
        this.car.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    RouteFragment.this.carIcon.setPressed(true);
                    RouteFragment.this.carText.setPressed(true);
                } else if (event.getAction() == 1) {
                    RouteFragment.this.carIcon.setPressed(false);
                    RouteFragment.this.carText.setPressed(false);
                }
                return false;
            }
        });
        this.walk.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    RouteFragment.this.walkIcon.setPressed(true);
                    RouteFragment.this.walkText.setPressed(true);
                } else if (event.getAction() == 1) {
                    RouteFragment.this.walkIcon.setPressed(false);
                    RouteFragment.this.walkText.setPressed(false);
                }
                return false;
            }
        });
        this.startNameET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String startContent = cs.toString();
                if (RouteFragment.this.resource.getString("mc_traffic_route_my_location").equals(startContent)) {
                    RouteFragment.this.startNameET.setTextColor(-16776961);
                    RouteFragment.this.startModel.setLatitudeE6(RouteFragment.this.myLatitude);
                    RouteFragment.this.startModel.setLongitudeE6(RouteFragment.this.myLongitude);
                } else {
                    RouteFragment.this.startNameET.setTextColor(-7829368);
                }
                if (!RouteFragment.this.startChange) {
                    RouteFragment.this.startIsOk = false;
                    RouteFragment.this.currentSugLocation = 1;
                    RouteFragment.this.startModel.setLatitudeE6(0);
                    RouteFragment.this.startModel.setLongitudeE6(0);
                    RouteFragment.this.startModel.setPointName(startContent);
                    RouteFragment.this.startModel.setPointLocation(startContent);
                }
//                RouteFragment.this.mSearch.suggestionSearch(startContent, RouteFragment.this.startCity);
                RouteFragment.this.startChange = false;
            }
        });
        this.endNameET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String endContent = cs.toString();
                if (RouteFragment.this.resource.getString("mc_traffic_route_my_location").equals(endContent)) {
                    RouteFragment.this.endNameET.setTextColor(-16776961);
                    RouteFragment.this.endModel.setLatitudeE6(RouteFragment.this.myLatitude);
                    RouteFragment.this.endModel.setLongitudeE6(RouteFragment.this.myLongitude);
                } else {
                    RouteFragment.this.endNameET.setTextColor(-7829368);
                }
                if (!RouteFragment.this.endChange) {
                    RouteFragment.this.endIsOk = false;
                    RouteFragment.this.currentSugLocation = 2;
                    RouteFragment.this.endModel.setLatitudeE6(0);
                    RouteFragment.this.endModel.setLongitudeE6(0);
                    RouteFragment.this.endModel.setPointName(endContent);
                    RouteFragment.this.endModel.setPointLocation(endContent);
                }
//                RouteFragment.this.mSearch.suggestionSearch(endContent, RouteFragment.this.endCity);
                RouteFragment.this.endChange = false;
            }
        });
        this.endNameET.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 66 || event.getAction() == 1) {
                    return false;
                }
                ((InputMethodManager) RouteFragment.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(RouteFragment.this.getActivity().getCurrentFocus().getWindowToken(), 2);
                if (!RouteFragment.this.getStEnPoint()) {
                    return true;
                }
                RouteFragment.this.bus.performClick();
                return true;
            }
        });
        this.startNameET.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 66 || event.getAction() == 1) {
                    return false;
                }
                ((InputMethodManager) RouteFragment.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(RouteFragment.this.getActivity().getCurrentFocus().getWindowToken(), 2);
                if (!RouteFragment.this.getStEnPoint()) {
                    return true;
                }
                RouteFragment.this.bus.performClick();
                return true;
            }
        });
    }


    private void startSearch() {
        switch (this.currentSearchType) {
            case 1:
                startSearch(1);
                return;
            case 2:
                startSearch(2);
                return;
            case 3:
                startSearch(3);
                return;
            default:
                return;
        }
    }

    protected void onViewClick(View v) {
        if (this.changeBtn.equals(v)) {
            this.startChange = true;
            this.endChange = true;
            SearchConditionModel sm = this.startModel;
            this.startModel = this.endModel;
            this.endModel = sm;
            this.startModel.setHint(this.resource.getString("mc_place_route_input_start_location"));
            this.endModel.setHint(this.resource.getString("mc_place_route_input_dis_location"));
            fillTextView(this.endNameET, this.endModel);
            fillTextView(this.startNameET, this.startModel);
        } else if (!getStEnPoint()) {
        } else {
            if (this.bus.equals(v)) {
                onBusClick();
            } else if (this.car.equals(v)) {

            } else if (this.walk.equals(v)) {

            }
        }
    }

    private void onBusClick() {
        RouteLoadingUtil.getInstance().show(this.context);
        resetMyLocation();
        this.currentSearchType = 1;

        MCLogUtil.e(this.TAG, "startModel" + this.startModel + "  endModel = " + this.endModel);
    }

    private void resetMyLocation() {
        if (this.startModel.equals(this.resource.getString("mc_traffic_route_my_location"))) {
            this.startModel.setLatitudeE6(this.myLatitude);
            this.startModel.setLongitudeE6(this.myLongitude);
        }
        if (this.endModel.equals(this.resource.getString("mc_traffic_route_my_location"))) {
            this.endModel.setLatitudeE6(this.myLatitude);
            this.endModel.setLongitudeE6(this.myLongitude);
        }
    }

    private void fillTextView(AutoCompleteTextView actv, SearchConditionModel model) {
        if (MCStringUtil.isEmpty(model.getPointName()) && model.getLatitudeE6() == 0) {
            actv.setText("");
            actv.setHint(model.getHint());
        }
        if (!MCStringUtil.isEmpty(model.getPointName())) {
            if (model.getPointName().equals(this.resource.getString("mc_traffic_route_my_location"))) {
                actv.setTextColor(-16776961);
                actv.setText(model.getPointName());
                return;
            }
            actv.setText(model.getPointName());
            actv.setTextColor(-7829368);
        }
    }

    private void startSearch(int searchType) {
        RouteLoadingUtil.getInstance().hide();
        RouteSearchMessageModel searchMsgModel = new RouteSearchMessageModel();
        searchMsgModel.setStartName(this.startModel.getPointName());
        searchMsgModel.setStartLocation(this.startModel.getPointLocation());
        searchMsgModel.setDistanceName(this.endModel.getPointName());
        searchMsgModel.setDistanceLocation(this.endModel.getPointLocation());
        searchMsgModel.setStartCity(this.startCity);
        searchMsgModel.setDistanceCity(this.endCity);
        searchMsgModel.setSearchType(searchType);
        if (this.startModel.getLatitudeE6() != 0) {
            GeoPointModel geoModel = new GeoPointModel();
            geoModel.setLatitudeE6(this.startModel.getLatitudeE6());
            geoModel.setLongitudeE6(this.startModel.getLongitudeE6());
            searchMsgModel.setStartGeoPointModel(geoModel);
        }
        if (this.endModel.getLatitudeE6() != 0) {
            GeoPointModel endGeoModel = new GeoPointModel();
            endGeoModel.setLatitudeE6(this.endModel.getLatitudeE6());
            endGeoModel.setLongitudeE6(this.endModel.getLongitudeE6());
            searchMsgModel.setEndPointModel(endGeoModel);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(RouteConstant.SEARCH_MSG_MODEL, searchMsgModel);
        Intent locationIntent = new Intent(this.context, RouteSolutionDetailActivity.class);
        locationIntent.putExtra(RouteConstant.SEARCHLOCATION, bundle);
        MCLogUtil.e(this.TAG, "searchMsgModel = " + searchMsgModel);
        startActivity(locationIntent);
    }



    private boolean getStEnPoint() {
        if (MCStringUtil.isEmpty(this.startNameET.getText() + "")) {
            warn("mc_traffic_route_search_no_start_location");
            return false;
        } else if (!MCStringUtil.isEmpty(this.endNameET.getText() + "")) {
            return true;
        } else {
            warn("mc_traffic_route_search_no_end_location");
            return false;
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
//        this.bDMapView.onResume();
        super.onResume();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
//        this.bDMapView.destroy();
//        this.mSearch.destory();
        super.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        this.bDMapView.onSaveInstanceState(outState);
    }
}
