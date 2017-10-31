package com.mobcent.lowest.android.ui.module.place.module.around.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.lowest.android.ui.module.place.activity.AroundPoiMapMessageActivity;
import com.mobcent.lowest.android.ui.module.place.activity.BasePlaceFragmentActivity;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListFiledNavAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListFiledSortAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListFiledSubAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledNavModel;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledSubModel;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.constant.PlaceConstant;
import com.mobcent.lowest.module.place.delegate.PlaceFiledDelegate;
import com.mobcent.lowest.module.place.delegate.PlacePoiSearchDelegate;
import com.mobcent.lowest.module.place.delegate.QueryAreaDelegate;
import com.mobcent.lowest.module.place.helper.PlaceFiledHelper;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;
import com.mobcent.lowest.module.place.helper.PlacePoiRequestHelper;
import com.mobcent.lowest.module.place.model.AreaModel;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import com.mobcent.lowest.module.place.model.PlaceKeyNameModel;
import com.mobcent.lowest.module.place.model.PlaceLocationIntentModel;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import com.mobcent.lowest.module.place.model.PlacePoiLocationModel;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.model.PlaceQueryModel;
import com.mobcent.lowest.module.place.model.PlaceTypeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AroundListActivity extends BasePlaceFragmentActivity implements PlaceConstant {
    private int DEFAULT_RADIUS = 5000;
    public String TAG = "AroundListActivity";
    private Map<String, Integer> aa = new HashMap();
    private String allStr;
    private Map<String, List<PlaceFiledSubModel>> areaMap = null;
    private Button backBtn;
    private LinearLayout categoryBox;
    private Button categoryBtn;
    private ImageView categoryIcon;
    private String defaultStr;
    private Button distanceBtn;
    private ImageView distanceIcon;
    private int filedType = 1;
    private PlaceApiFilterModel filterModel;
    private boolean isRefresh = true;
    private boolean isSearch = false;
    private String labelNextPage = null;
    private String labelPullToRefresh;
    private String labelReleaseUpPage;
    private String labelReleaseUpdate;
    private String labelUpPage = null;
    private AroundListAdapter listAdapter;
    private PlacePoiLocationModel location;
    private PlacePoiRequestHelper mSearch;
    private PlacePoiSearchDelegate mSearchDelegate = new PlacePoiSearchDelegate() {
        public void onPlacePoiResult(PlacePoiResult result) {
            AroundListActivity.this.poiInfoList.clear();
            if (AroundListActivity.this.page != 0) {
                AroundListActivity.this.pullRefreshList.updateHeaderLabel(1, AroundListActivity.this.labelReleaseUpPage);
                AroundListActivity.this.pullRefreshList.updateHeaderLabel(2, AroundListActivity.this.labelUpPage);
            } else {
                AroundListActivity.this.pullRefreshList.updateHeaderLabel(1, AroundListActivity.this.labelReleaseUpdate);
                AroundListActivity.this.pullRefreshList.updateHeaderLabel(2, AroundListActivity.this.labelPullToRefresh);
            }
            if (result.getStatus() == 0) {
                if (AroundListActivity.this.isRefresh) {
                    AroundListActivity.this.pullRefreshList.onRefreshComplete(true);
                    if (result.getPoiInfoList() != null) {
                        AroundListActivity.this.poiInfoList.addAll(result.getPoiInfoList());
                        if (AroundListActivity.this.poiInfoList.size() >= result.getTotal()) {
                            AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(3);
                        } else {
                            AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(0, AroundListActivity.this.labelNextPage);
                        }
                    } else {
                        AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(2);
                    }
                } else if (result.getPoiInfoList() != null) {
                    AroundListActivity.this.poiInfoList.addAll(result.getPoiInfoList());
                    if (AroundListActivity.this.poiInfoList.size() >= result.getTotal()) {
                        AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(3);
                    } else {
                        AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(0, AroundListActivity.this.labelNextPage);
                    }
                } else {
                    AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(3);
                }
            } else if (AroundListActivity.this.isRefresh) {
                AroundListActivity.this.pullRefreshList.onRefreshComplete(true);
                AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(4);
            } else {
                AroundListActivity.this.pullRefreshList.onBottomRefreshComplete(0);
            }
            AroundListActivity.this.listAdapter.notifyDataSetChanged();
            AroundListActivity.this.listAdapter.notifyDataSetInvalidated();
            AroundListActivity.this.pullRefreshList.setSelection(0);
        }
    };
    private Button mapBtn;
    private AroundListFiledNavAdapter navCategoryAdapter;
    private List<PlaceFiledNavModel> navCategoryDataList;
    private ListView navCategoryListView;
    private int navCategoryPosition;
    private AroundListFiledNavAdapter navRangeAdapter;
    private List<PlaceFiledNavModel> navRangeDataList;
    private ListView navRangeListView;
    private int navRangePosition = 0;
    private int page = 0;
    private List<PlacePoiInfoModel> poiInfoList = null;
    private PullToRefreshListView pullRefreshList;
    private PlaceQueryModel queryModel;
    private int radius = this.DEFAULT_RADIUS;
    private LinearLayout rangeBox;
    private String rangeStr;
    private boolean searchByRadius = true;
    private AroundListFiledSortAdapter sortAdapter = null;
    private Button sortBtn;
    private List<PlaceFiledNavModel> sortDataList = null;
    private ImageView sortIcon;
    private ListView sortListView;
    private AroundListFiledSubAdapter subCategoryAdapter;
    private List<PlaceFiledSubModel> subCategoryDataList;
    private ListView subCategoryListView;
    private int subPosition = 0;
    private AroundListFiledSubAdapter subRangeAdapter;
    private List<PlaceFiledSubModel> subRangeDataList;
    private ListView subRangeListView;
    private String tagKeywords = null;
    private String tagRegion = null;
    private TextView titleText;

    protected void initData() {
        super.initData();
        this.allStr = this.resource.getString("mc_place_around_all");
        this.rangeStr = this.resource.getString("mc_place_around_range");
        this.defaultStr = this.resource.getString("mc_place_around_default");
        this.labelNextPage = this.resource.getString("mc_place_around_next_page");
        this.labelUpPage = this.resource.getString("mc_place_around_up_page");
        this.labelPullToRefresh = this.resource.getString("mc_forum_drop_dowm");
        this.labelReleaseUpdate = this.resource.getString("mc_forum_release_update");
        this.labelReleaseUpPage = this.resource.getString("mc_place_around_release_up_page");
        this.areaMap = new HashMap();
        PlaceLocationIntentModel locationModel = (PlaceLocationIntentModel) getIntent().getSerializableExtra("location");
        if (locationModel == null || locationModel.getLocationModel() == null) {
            this.location = new PlacePoiLocationModel();
            this.queryModel = locationModel.getQueryModel();
        } else {
            this.location = locationModel.getLocationModel();
            this.queryModel = locationModel.getQueryModel();
            this.isSearch = locationModel.isSearch();
        }
        this.filterModel = new PlaceApiFilterModel();
        this.filterModel.setIndustryType(this.queryModel.getQueryType());
        this.mSearch = new PlacePoiRequestHelper(this);
        this.mSearch.registerPoiSearchDelegate(this.mSearchDelegate);
        this.poiInfoList = new ArrayList();
        this.navRangeDataList = new ArrayList();
        this.subRangeDataList = new ArrayList();
        this.navCategoryDataList = new ArrayList();
        this.subCategoryDataList = new ArrayList();
        this.sortDataList = new ArrayList();
        this.listAdapter = new AroundListAdapter(this, this.poiInfoList);
        this.navRangeAdapter = new AroundListFiledNavAdapter(this, this.navRangeDataList);
        this.subRangeAdapter = new AroundListFiledSubAdapter(this, this.subRangeDataList);
        this.navCategoryAdapter = new AroundListFiledNavAdapter(this, this.navCategoryDataList);
        this.subCategoryAdapter = new AroundListFiledSubAdapter(this, this.subCategoryDataList);
        this.sortAdapter = new AroundListFiledSortAdapter(this, this.sortDataList);
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_around_list_activity"));
        this.titleText = (TextView) findViewById("title_text");
        this.titleText.setText(this.queryModel.getQuery());
        this.backBtn = (Button) findViewById("back_btn");
        this.mapBtn = (Button) findViewById("map_btn");
        this.distanceBtn = (Button) findViewById("distance_btn");
        this.categoryBtn = (Button) findViewById("category_btn");
        this.sortBtn = (Button) findViewById("sort_btn");
        this.distanceIcon = (ImageView) findViewById("distance_small_icon");
        this.categoryIcon = (ImageView) findViewById("distance_category_icon");
        this.sortIcon = (ImageView) findViewById("distance_sort_icon");
        this.pullRefreshList = (PullToRefreshListView) findViewById("pull_refresh_list");
        this.pullRefreshList.setAdapter(this.listAdapter);
        this.navRangeListView = (ListView) findViewById("range_nav_list");
        this.navRangeListView.setAdapter(this.navRangeAdapter);
        this.subRangeListView = (ListView) findViewById("range_sub_list");
        this.subRangeListView.setAdapter(this.subRangeAdapter);
        this.sortListView = (ListView) findViewById("rule_list");
        this.navCategoryListView = (ListView) findViewById("category_nav_list");
        this.navCategoryListView.setAdapter(this.navCategoryAdapter);
        this.subCategoryListView = (ListView) findViewById("category_sub_list");
        this.subCategoryListView.setAdapter(this.subCategoryAdapter);
        this.sortListView = (ListView) findViewById("rule_list");
        this.sortListView.setAdapter(this.sortAdapter);
        this.rangeBox = (LinearLayout) findViewById("range_layout");
        this.categoryBox = (LinearLayout) findViewById("category_layout");
    }

    protected void initActions() {
        this.pullRefreshList.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                AroundListActivity.this.isRefresh = true;
                if (AroundListActivity.this.page > 1) {
                    AroundListActivity.this.page = AroundListActivity.this.page - 1;
                } else {
                    AroundListActivity.this.page = 0;
                }
                AroundListActivity.this.mSearch.setPageNum(AroundListActivity.this.page);
                AroundListActivity.this.searchPoiDataByRadius(AroundListActivity.this.searchByRadius);
            }
        });
        this.pullRefreshList.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                AroundListActivity.this.isRefresh = false;
                AroundListActivity.this.page = AroundListActivity.this.page + 1;
                AroundListActivity.this.mSearch.setPageNum(AroundListActivity.this.page);
                AroundListActivity.this.searchPoiDataByRadius(AroundListActivity.this.searchByRadius);
            }
        });
        this.navRangeListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity.this.navRangePosition = position;
                AroundListActivity.this.subRangeDataList.clear();
                AroundListActivity.this.setNavSelected(AroundListActivity.this.navRangeDataList, position);
                AroundListActivity.this.navRangeAdapter.notifyDataSetChanged();
                final PlaceFiledNavModel navModel = (PlaceFiledNavModel) AroundListActivity.this.navRangeDataList.get(position);
                if (navModel.getType() == 4) {
                    AroundListActivity.this.subRangeAdapter.notifyDataSetChanged();
                    List<PlaceFiledSubModel> areaListTemp = (List) AroundListActivity.this.areaMap.get(navModel.getAreaCode());
                    if (areaListTemp == null || areaListTemp.isEmpty()) {
                        PlaceLocationHelper.getInastance().querySubAreaByAreaCode(AroundListActivity.this.getApplicationContext(), navModel.getAreaCode(), new QueryAreaDelegate() {
                            public void onResult(AreaModel areaModel) {
                                List<PlaceFiledSubModel> subAreaList = new ArrayList();
                                PlaceFiledSubModel subModel;
                                if (areaModel == null || areaModel.getSubAreaList().isEmpty()) {
                                    subModel = new PlaceFiledSubModel();
                                    subModel.setName(AroundListActivity.this.allStr);
                                    subModel.setType(4);
                                    subModel.setAll(true);
                                    subAreaList.add(subModel);
                                } else {
                                    subAreaList = AroundListActivity.this.dealSubAreaModel(areaModel);
                                    subModel = new PlaceFiledSubModel();
                                    subModel.setName(AroundListActivity.this.allStr);
                                    subModel.setType(4);
                                    subModel.setAll(true);
                                    subAreaList.add(0, subModel);
                                }
                                AroundListActivity.this.areaMap.put(navModel.getAreaCode(), subAreaList);
                                AroundListActivity.this.subRangeDataList.addAll(subAreaList);
                                AroundListActivity.this.subRangeAdapter.notifyDataSetChanged();
                            }
                        });
                        return;
                    }
                    AroundListActivity.this.subRangeDataList.addAll(areaListTemp);
                    AroundListActivity.this.subRangeAdapter.notifyDataSetChanged();
                    return;
                }
                AroundListActivity.this.subRangeDataList.addAll(((PlaceFiledNavModel) AroundListActivity.this.navRangeDataList.get(position)).getSubList());
                AroundListActivity.this.subRangeAdapter.notifyDataSetChanged();
            }
        });
        this.subRangeListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity.this.rangeBox.setVisibility(8);
                AroundListActivity.this.clearAllTab();
                PlaceFiledSubModel subModel = (PlaceFiledSubModel) AroundListActivity.this.subRangeDataList.get(position);
                boolean isArea = false;
                switch (subModel.getType()) {
                    case 1:
                        if (subModel.getKey().equals(StyleConstant.STYLE_DEFAULT)) {
                            AroundListActivity.this.radius = AroundListActivity.this.DEFAULT_RADIUS;
                        } else {
                            AroundListActivity.this.radius = Integer.parseInt(((PlaceFiledSubModel) AroundListActivity.this.subRangeDataList.get(position)).getKey());
                        }
                        AroundListActivity.this.tagRegion = null;
                        AroundListActivity.this.searchByRadius = true;
                        AroundListActivity.this.pullRefreshList.onRefresh(true);
                        break;
                    case 4:
                        isArea = true;
                        if (subModel.isAll()) {
                            AroundListActivity.this.tagRegion = ((PlaceFiledNavModel) AroundListActivity.this.navRangeDataList.get(AroundListActivity.this.navRangePosition)).getName();
                        } else {
                            AroundListActivity.this.tagRegion = subModel.getName();
                        }
                        AroundListActivity.this.searchByRadius = false;
                        AroundListActivity.this.pullRefreshList.onRefresh(true);
                        break;
                }
                AroundListActivity.this.setRangeSubSelected(AroundListActivity.this.navRangeDataList, AroundListActivity.this.areaMap, isArea, subModel);
            }
        });
        this.navCategoryListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity.this.navCategoryPosition = position;
                PlaceFiledNavModel placeFiledNavModel = (PlaceFiledNavModel) AroundListActivity.this.navCategoryDataList.get(position);
                AroundListActivity.this.subCategoryDataList.clear();
                AroundListActivity.this.setNavSelected(AroundListActivity.this.navCategoryDataList, position);
                AroundListActivity.this.navCategoryAdapter.notifyDataSetChanged();
                AroundListActivity.this.subCategoryDataList.addAll(placeFiledNavModel.getSubList());
                AroundListActivity.this.subCategoryAdapter.notifyDataSetChanged();
            }
        });
        this.subCategoryListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                AroundListActivity.this.categoryBox.setVisibility(8);
                AroundListActivity.this.clearAllTab();
                PlaceFiledSubModel subModel = (PlaceFiledSubModel) AroundListActivity.this.subCategoryDataList.get(position);
                AroundListActivity.this.setCategorySubSelected(AroundListActivity.this.navCategoryDataList, AroundListActivity.this.navCategoryPosition, position);
                AroundListActivity.this.setSubListSelected(AroundListActivity.this.subCategoryDataList, position);
                AroundListActivity.this.subCategoryAdapter.notifyDataSetChanged();
                if (AroundListActivity.this.navCategoryPosition == 0 && position == 0) {
                    AroundListActivity.this.tagKeywords = null;
                } else if (position == 0) {
                    AroundListActivity.this.tagKeywords = ((PlaceFiledNavModel) AroundListActivity.this.navCategoryDataList.get(AroundListActivity.this.navCategoryPosition)).getName();
                } else {
                    AroundListActivity.this.tagKeywords = subModel.getName();
                }
                AroundListActivity.this.pullRefreshList.onRefresh(true);
            }
        });
        this.sortListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity.this.setNavSelected(AroundListActivity.this.sortDataList, position);
                AroundListActivity.this.sortAdapter.notifyDataSetChanged();
                PlaceFiledNavModel sortModel = (PlaceFiledNavModel) AroundListActivity.this.sortDataList.get(position);
                AroundListActivity.this.filterModel.setSortName(sortModel.getKey());
                AroundListActivity.this.filterModel.setSortRule(sortModel.getRule());
                AroundListActivity.this.sortListView.setVisibility(8);
                AroundListActivity.this.clearAllTab();
                AroundListActivity.this.pullRefreshList.onRefresh(true);
            }
        });
        this.backBtn.setOnClickListener(this.clickListener);
        this.distanceBtn.setOnClickListener(this.clickListener);
        this.categoryBtn.setOnClickListener(this.clickListener);
        this.sortBtn.setOnClickListener(this.clickListener);
        this.mapBtn.setOnClickListener(this.clickListener);
        this.pullRefreshList.onRefresh(true);
    }

    protected void onViewClick(View v) {
        if (this.isSearch && ((v.equals(this.categoryBtn) || v.equals(this.sortBtn)) && MCStringUtil.isEmpty(this.queryModel.getQueryType()))) {
            if (!this.poiInfoList.isEmpty()) {
                this.queryModel.setQueryType(getQueryTypeByData(this.poiInfoList));
            } else {
                return;
            }
        }
        if (v.equals(this.backBtn)) {
            onBackPressed();
        } else if (v.equals(this.distanceBtn)) {
            onDistanceBtnClickDo(v);
        } else if (v.equals(this.categoryBtn)) {
            onCategoryBtnClickDo(v);
        } else if (v.equals(this.sortBtn)) {
            onSortBtnClickDo(v);
        } else if (v.equals(this.mapBtn)) {
            onMapBtnClickDo(v);
        }
    }

    private void onMapBtnClickDo(View v) {
        Intent intent = new Intent(this, AroundPoiMapMessageActivity.class);
        intent.putExtra(RouteConstant.POI_MODEL_LIST, (ArrayList) this.poiInfoList);
        startActivity(intent);
    }

    private void onDistanceBtnClickDo(View v) {
        if (this.sortListView.getVisibility() == 0 || this.categoryBox.getVisibility() == 0) {
            this.sortListView.setVisibility(8);
            this.categoryBox.setVisibility(8);
        }
        if (this.rangeBox.getVisibility() == 0) {
            this.rangeBox.setVisibility(8);
            clearAllTab();
            return;
        }
        this.rangeBox.setVisibility(0);
        onTabSelected(v);
        this.filedType = 1;
        if (this.navRangeDataList.isEmpty()) {
            PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
                public void onPlaceFiledResult(final PlaceTypeModel placeTypeModel) {
                    PlaceLocationHelper.getInastance().queryAreaByAreaCode(AroundListActivity.this.getApplicationContext(), AroundListActivity.this.location.getAreaCode(), new QueryAreaDelegate() {
                        public void onResult(AreaModel areaModel) {
                            List<PlaceFiledNavModel> distanceFiledNavList = AroundListActivity.this.dealFiledData(placeTypeModel, 1);
                            if (distanceFiledNavList == null) {
                                AroundListActivity.this.warn("mc_place_no_distance");
                                return;
                            }
                            AroundListActivity.this.navRangeDataList.clear();
                            AroundListActivity.this.navRangeDataList.addAll(distanceFiledNavList);
                            AroundListActivity.this.navRangeDataList.addAll(AroundListActivity.this.dealNavAreaModel(areaModel));
                            AroundListActivity.this.setNavSelected(AroundListActivity.this.navRangeDataList, 0);
                            AroundListActivity.this.navRangeAdapter.notifyDataSetChanged();
                            AroundListActivity.this.subRangeDataList.clear();
                            ((PlaceFiledSubModel) ((PlaceFiledNavModel) AroundListActivity.this.navRangeDataList.get(0)).getSubList().get(0)).setSelected(true);
                            AroundListActivity.this.subRangeDataList.addAll(((PlaceFiledNavModel) AroundListActivity.this.navRangeDataList.get(0)).getSubList());
                            AroundListActivity.this.subRangeAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }

    private void onCategoryBtnClickDo(final View v) {
        if (this.rangeBox.getVisibility() == 0 || this.sortListView.getVisibility() == 0) {
            this.rangeBox.setVisibility(8);
            this.sortListView.setVisibility(8);
        }
        if (this.categoryBox.getVisibility() == 0) {
            this.categoryBox.setVisibility(8);
            clearAllTab();
            return;
        }
        this.filedType = 3;
        if (this.navCategoryDataList.isEmpty()) {
            PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
                public void onPlaceFiledResult(PlaceTypeModel placeTypeModel) {
                    List<PlaceFiledNavModel> keyWordsFiledNavList = AroundListActivity.this.dealFiledData(placeTypeModel, 3);
                    if (keyWordsFiledNavList == null) {
                        AroundListActivity.this.warn("mc_place_no_category");
                        return;
                    }
                    AroundListActivity.this.categoryBox.setVisibility(0);
                    AroundListActivity.this.onTabSelected(v);
                    AroundListActivity.this.navCategoryDataList.clear();
                    AroundListActivity.this.navCategoryDataList.addAll(keyWordsFiledNavList);
                    AroundListActivity.this.setNavSelected(AroundListActivity.this.navCategoryDataList, 0);
                    AroundListActivity.this.navCategoryAdapter.notifyDataSetChanged();
                    AroundListActivity.this.subCategoryDataList.clear();
                    AroundListActivity.this.subCategoryDataList.addAll(((PlaceFiledNavModel) AroundListActivity.this.navCategoryDataList.get(0)).getSubList());
                    AroundListActivity.this.setSubListSelected(AroundListActivity.this.subCategoryDataList, 0);
                    AroundListActivity.this.subCategoryAdapter.notifyDataSetChanged();
                }
            });
            return;
        }
        this.categoryBox.setVisibility(0);
        onTabSelected(v);
    }

    private void onSortBtnClickDo(View v) {
        this.filedType = 2;
        if (this.sortDataList.isEmpty()) {
            PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
                public void onPlaceFiledResult(PlaceTypeModel placeTypeModel) {
                    List<PlaceFiledNavModel> sortList = AroundListActivity.this.dealFiledData(placeTypeModel, 2);
                    if (sortList == null) {
                        AroundListActivity.this.warn("mc_place_no_sort");
                        return;
                    }
                    AroundListActivity.this.sortDataList.clear();
                    AroundListActivity.this.sortDataList.addAll(sortList);
                    AroundListActivity.this.setNavSelected(AroundListActivity.this.sortDataList, 0);
                    AroundListActivity.this.sortAdapter.notifyDataSetChanged();
                    if (AroundListActivity.this.rangeBox.getVisibility() == 0) {
                        AroundListActivity.this.rangeBox.setVisibility(8);
                        if (AroundListActivity.this.sortListView.getVisibility() == 0) {
                            AroundListActivity.this.sortListView.setVisibility(8);
                        } else {
                            AroundListActivity.this.sortListView.setVisibility(0);
                        }
                    } else if (AroundListActivity.this.sortListView.getVisibility() == 0) {
                        AroundListActivity.this.sortListView.setVisibility(8);
                    } else {
                        AroundListActivity.this.sortListView.setVisibility(0);
                    }
                }
            });
        } else if (this.rangeBox.getVisibility() == 0 || this.categoryBox.getVisibility() == 0) {
            this.rangeBox.setVisibility(8);
            this.categoryBox.setVisibility(8);
            if (this.sortListView.getVisibility() == 0) {
                this.sortListView.setVisibility(8);
                clearAllTab();
                return;
            }
            onTabSelected(v);
            this.sortListView.setVisibility(0);
        } else if (this.sortListView.getVisibility() == 0) {
            this.sortListView.setVisibility(8);
            clearAllTab();
        } else {
            this.sortListView.setVisibility(0);
            onTabSelected(v);
        }
    }

    private List<PlaceFiledNavModel> dealFiledData(PlaceTypeModel placeTypeModel, int type) {
        List<PlaceFiledNavModel> navDataList = new ArrayList();
        List<PlaceKeyNameModel> dataList;
        PlaceFiledNavModel placeFiledNavModel;
        int len;
        int i;
        PlaceKeyNameModel keyModel;
        if (type == 1) {
            dataList = placeTypeModel.getDistanceList();
            if (dataList == null || dataList.isEmpty()) {
                return null;
            }
            placeFiledNavModel = new PlaceFiledNavModel();
            placeFiledNavModel.setName(this.rangeStr);
            placeFiledNavModel.setType(1);
            len = dataList.size();
            for (i = 0; i < len; i++) {
                PlaceFiledSubModel subModel = new PlaceFiledSubModel();
                keyModel = (PlaceKeyNameModel) dataList.get(i);
                subModel.setKey(keyModel.getKey());
                subModel.setName(keyModel.getName());
                subModel.setType(1);
                placeFiledNavModel.getSubList().add(subModel);
            }
            navDataList.add(placeFiledNavModel);
            return navDataList;
        } else if (type == 3) {
            dataList = placeTypeModel.getKeywordsList();
            if (dataList == null || dataList.isEmpty()) {
                return null;
            }
            len = dataList.size();
            for (i = 0; i < len; i++) {
                keyModel = (PlaceKeyNameModel) dataList.get(i);
                placeFiledNavModel = new PlaceFiledNavModel();
                placeFiledNavModel.setName(keyModel.getName());
                placeFiledNavModel.setType(3);
                PlaceFiledSubModel subAll = new PlaceFiledSubModel();
                subAll.setName(this.allStr);
                subAll.setType(3);
                List<PlaceFiledSubModel> filedSubList = new ArrayList();
                if (keyModel.getSub() == null || keyModel.getSub().isEmpty()) {
                    filedSubList.add(subAll);
                } else {
                    List<String> subStr = keyModel.getSub();
                    int subLen = subStr.size();
                    for (int j = 0; j < subLen; j++) {
                        PlaceFiledSubModel subItem = new PlaceFiledSubModel();
                        subItem.setName((String) subStr.get(j));
                        subItem.setType(3);
                        filedSubList.add(subItem);
                    }
                }
                placeFiledNavModel.setSubList(filedSubList);
                navDataList.add(placeFiledNavModel);
            }
            return navDataList;
        } else {
            dataList = placeTypeModel.getSortList();
            if (dataList == null || dataList.isEmpty()) {
                return null;
            }
            len = dataList.size();
            for (i = 0; i < len; i++) {
                keyModel = (PlaceKeyNameModel) dataList.get(i);
                placeFiledNavModel = new PlaceFiledNavModel();
                placeFiledNavModel.setName(keyModel.getName());
                placeFiledNavModel.setRule(keyModel.getRule());
                placeFiledNavModel.setKey(keyModel.getKey());
                placeFiledNavModel.setType(2);
                navDataList.add(placeFiledNavModel);
            }
            return navDataList;
        }
    }

    private List<PlaceFiledNavModel> dealNavAreaModel(AreaModel areaModel) {
        List<PlaceFiledNavModel> areaFiledList = new ArrayList();
        if (!(areaModel == null || areaModel.getSubAreaList() == null)) {
            int len = areaModel.getSubAreaList().size();
            for (int i = 0; i < len; i++) {
                AreaModel areaTemp = (AreaModel) areaModel.getSubAreaList().get(i);
                PlaceFiledNavModel areaFiledModel = new PlaceFiledNavModel();
                areaFiledModel.setType(4);
                areaFiledModel.setAreaCode(areaTemp.getAreaCode() + "");
                areaFiledModel.setName(areaTemp.getAreaName());
                areaFiledList.add(areaFiledModel);
            }
        }
        return areaFiledList;
    }

    private List<PlaceFiledSubModel> dealSubAreaModel(AreaModel areaModel) {
        List<PlaceFiledSubModel> areaFiledList = new ArrayList();
        if (!(areaModel == null || areaModel.getSubAreaList() == null)) {
            int len = areaModel.getSubAreaList().size();
            for (int i = 0; i < len; i++) {
                AreaModel areaTemp = (AreaModel) areaModel.getSubAreaList().get(i);
                PlaceFiledSubModel areaFiledModel = new PlaceFiledSubModel();
                areaFiledModel.setType(4);
                areaFiledModel.setName(areaTemp.getAreaName());
                areaFiledList.add(areaFiledModel);
            }
        }
        return areaFiledList;
    }

    private String getTagString() {
        String tagString = (this.tagRegion == null ? "" : this.tagRegion) + " " + (this.tagKeywords == null ? "" : this.tagKeywords);
        if (MCStringUtil.isEmpty(tagString.trim())) {
            return null;
        }
        return tagString;
    }

    private void searchPoiDataByRadius(boolean searchByRadius) {
        this.mSearch.setFilter(this.filterModel);
        if (searchByRadius) {
            this.mSearch.searchPoiDataByRadius(this.queryModel.getQuery(), getTagString(), this.location.getCity(), this.location.getLat(), this.location.getLng(), this.radius);
        } else {
            this.mSearch.searchPoiData(this.queryModel.getQuery(), getTagString(), this.location.getCity());
        }
    }

    private String getQueryTypeByData(List<PlacePoiInfoModel> poiInfoList) {
        this.aa.clear();
        String queryType = null;
        int currentMaxTypeNum = 0;
        int len = poiInfoList.size();
        for (int i = 0; i < len; i++) {
            PlacePoiInfoModel poiInfo = (PlacePoiInfoModel) poiInfoList.get(i);
            if (!(poiInfo.detailInfoModel == null || poiInfo.detailInfoModel.type == null)) {
                String typeTemp = poiInfo.detailInfoModel.type;
                Integer num = (Integer) this.aa.get(typeTemp);
                if (num == null) {
                    this.aa.put(typeTemp, Integer.valueOf(1));
                    if (currentMaxTypeNum == 0) {
                        queryType = typeTemp;
                        currentMaxTypeNum = 1;
                    }
                } else {
                    this.aa.put(typeTemp, Integer.valueOf(num.intValue() + 1));
                    if (((Integer) this.aa.get(typeTemp)).intValue() > currentMaxTypeNum) {
                        queryType = typeTemp;
                        currentMaxTypeNum = ((Integer) this.aa.get(typeTemp)).intValue();
                    }
                }
            }
        }
        return queryType;
    }

    private void setNavSelected(List<PlaceFiledNavModel> navList, int position) {
        int len = navList.size();
        for (int i = 0; i < len; i++) {
            if (i == position) {
                ((PlaceFiledNavModel) navList.get(i)).setSelected(true);
            } else {
                ((PlaceFiledNavModel) navList.get(i)).setSelected(false);
            }
        }
    }

    private void setSubListSelected(List<PlaceFiledSubModel> subList, int position) {
        int len = subList.size();
        for (int i = 0; i < len; i++) {
            if (i == position) {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(true);
            } else {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(false);
            }
        }
    }

    private void setCategorySubSelected(List<PlaceFiledNavModel> navList, int navCategoryPosition, int position) {
        int len = navList.size();
        for (int i = 0; i < len; i++) {
            PlaceFiledNavModel navModel = (PlaceFiledNavModel) navList.get(i);
            if (!(navModel.getSubList() == null || navModel.getSubList().isEmpty())) {
                int j = 0;
                int l = navModel.getSubList().size();
                while (j < l) {
                    if (i == navCategoryPosition && j == position) {
                        ((PlaceFiledSubModel) navModel.getSubList().get(j)).setSelected(true);
                    } else {
                        ((PlaceFiledSubModel) navModel.getSubList().get(j)).setSelected(false);
                    }
                    j++;
                }
            }
        }
    }

    private void setRangeSubSelected(List<PlaceFiledNavModel> navRangeList, Map<String, List<PlaceFiledSubModel>> areaMap, boolean isArea, PlaceFiledSubModel subModel) {
        List<PlaceFiledSubModel> rangeList = ((PlaceFiledNavModel) navRangeList.get(0)).getSubList();
        int len = rangeList.size();
        for (int i = 0; i < len; i++) {
            PlaceFiledSubModel subModelTemp = (PlaceFiledSubModel) rangeList.get(i);
            if (isArea) {
                subModelTemp.setSelected(false);
            } else if (subModelTemp.getName().equals(subModel.getName())) {
                subModelTemp.setSelected(true);
            } else {
                subModelTemp.setSelected(false);
            }
        }
        for (String key : areaMap.keySet()) {
            List<PlaceFiledSubModel> areaList = (List) areaMap.get(key);
            String currentAreaCode = ((PlaceFiledNavModel) navRangeList.get(this.navRangePosition)).getAreaCode();
            if (!(areaList == null || areaList.isEmpty())) {
                for (PlaceFiledSubModel placeSubModel : areaList) {
                    if (!isArea) {
                        placeSubModel.setSelected(false);
                    } else if (key.equals(currentAreaCode) && placeSubModel.getName().equals(subModel.getName())) {
                        placeSubModel.setSelected(true);
                    } else {
                        placeSubModel.setSelected(false);
                    }
                }
            }
        }
        this.subRangeAdapter.notifyDataSetChanged();
    }

    private void onTabSelected(View v) {
        if (v.equals(this.distanceBtn)) {
            clearAllTab();
            this.distanceBtn.setSelected(true);
            this.distanceIcon.setSelected(true);
        } else if (v.equals(this.categoryBtn)) {
            clearAllTab();
            this.categoryBtn.setSelected(true);
            this.categoryIcon.setSelected(true);
        } else if (v.equals(this.sortBtn)) {
            clearAllTab();
            this.sortBtn.setSelected(true);
            this.sortIcon.setSelected(true);
        } else {
            clearAllTab();
        }
    }

    private void clearAllTab() {
        this.distanceBtn.setSelected(false);
        this.distanceIcon.setSelected(false);
        this.categoryBtn.setSelected(false);
        this.categoryIcon.setSelected(false);
        this.sortBtn.setSelected(false);
        this.sortIcon.setSelected(false);
    }

    public void onBackPressed() {
        if (this.rangeBox.getVisibility() == 8 && this.sortListView.getVisibility() == 8 && this.categoryBox.getVisibility() == 8) {
            super.onBackPressed();
            return;
        }
        this.rangeBox.setVisibility(8);
        this.sortListView.setVisibility(8);
        this.categoryBox.setVisibility(8);
    }
}
