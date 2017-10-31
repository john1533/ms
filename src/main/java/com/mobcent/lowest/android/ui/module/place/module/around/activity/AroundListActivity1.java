package com.mobcent.lowest.android.ui.module.place.module.around.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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

public class AroundListActivity1 extends BasePlaceFragmentActivity implements PlaceConstant {
    private int DEFAULT_RADIUS = 5000;
    public String TAG = "AroundListActivity";
    private Map<String, Integer> aa = new HashMap();
    private String allStr;
    private Button backBtn;
    private Button categoryBtn;
    private String defaultStr;
    private Button distanceBtn;
    private LinearLayout filedChoseBox;
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
            AroundListActivity1.this.poiInfoList.clear();
            if (AroundListActivity1.this.page != 0) {
                AroundListActivity1.this.pullRefreshList.updateHeaderLabel(1, AroundListActivity1.this.labelReleaseUpPage);
                AroundListActivity1.this.pullRefreshList.updateHeaderLabel(2, AroundListActivity1.this.labelUpPage);
            } else {
                AroundListActivity1.this.pullRefreshList.updateHeaderLabel(1, AroundListActivity1.this.labelReleaseUpdate);
                AroundListActivity1.this.pullRefreshList.updateHeaderLabel(2, AroundListActivity1.this.labelPullToRefresh);
            }
            if (result.getStatus() == 0) {
                if (AroundListActivity1.this.isRefresh) {
                    AroundListActivity1.this.pullRefreshList.onRefreshComplete(true);
                    if (result.getPoiInfoList() != null) {
                        AroundListActivity1.this.poiInfoList.addAll(result.getPoiInfoList());
                        if (AroundListActivity1.this.poiInfoList.size() >= result.getTotal()) {
                            AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(3);
                        } else {
                            AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(0, AroundListActivity1.this.labelNextPage);
                        }
                    } else {
                        AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(2);
                    }
                } else if (result.getPoiInfoList() != null) {
                    AroundListActivity1.this.poiInfoList.addAll(result.getPoiInfoList());
                    if (AroundListActivity1.this.poiInfoList.size() >= result.getTotal()) {
                        AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(3);
                    } else {
                        AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(0, AroundListActivity1.this.labelNextPage);
                    }
                } else {
                    AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(3);
                }
            } else if (AroundListActivity1.this.isRefresh) {
                AroundListActivity1.this.pullRefreshList.onRefreshComplete(true);
                AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(4);
            } else {
                AroundListActivity1.this.pullRefreshList.onBottomRefreshComplete(0);
            }
            AroundListActivity1.this.listAdapter.notifyDataSetChanged();
            AroundListActivity1.this.listAdapter.notifyDataSetInvalidated();
            AroundListActivity1.this.pullRefreshList.setSelection(0);
        }
    };
    private Button mapBtn;
    private AroundListFiledNavAdapter navAdapter = null;
    private List<PlaceFiledNavModel> navDataList = null;
    private ListView navListView;
    private int navPosition = 0;
    private int page = 0;
    private List<PlacePoiInfoModel> poiInfoList = null;
    private PullToRefreshListView pullRefreshList;
    private PlaceQueryModel queryModel;
    private int radius = this.DEFAULT_RADIUS;
    private String rangeStr;
    private boolean searchByRadius = true;
    private AroundListFiledSortAdapter sortAdapter = null;
    private Button sortBtn;
    private List<PlaceFiledNavModel> sortDataList = null;
    private ListView sortListView;
    private AroundListFiledSubAdapter subAdapter = null;
    private List<PlaceFiledSubModel> subDataList = null;
    private ListView subListView;
    private int subPosition = 0;
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
        this.navDataList = new ArrayList();
        this.subDataList = new ArrayList();
        this.sortDataList = new ArrayList();
        this.listAdapter = new AroundListAdapter(this, this.poiInfoList);
        this.navAdapter = new AroundListFiledNavAdapter(this, this.navDataList);
        this.subAdapter = new AroundListFiledSubAdapter(this, this.subDataList);
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
        this.pullRefreshList = (PullToRefreshListView) findViewById("pull_refresh_list");
        this.pullRefreshList.setAdapter(this.listAdapter);
        this.navListView = (ListView) findViewById("first_category_list");
        this.navListView.setAdapter(this.navAdapter);
        this.subListView = (ListView) findViewById("second_category_list");
        this.subListView.setAdapter(this.subAdapter);
        this.sortListView = (ListView) findViewById("rule_list");
        this.sortListView.setAdapter(this.sortAdapter);
        this.filedChoseBox = (LinearLayout) findViewById("around_category_layout");
    }

    protected void initActions() {
        this.pullRefreshList.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                AroundListActivity1.this.isRefresh = true;
                if (AroundListActivity1.this.page > 1) {
                    AroundListActivity1.this.page = AroundListActivity1.this.page - 1;
                } else {
                    AroundListActivity1.this.page = 0;
                }
                AroundListActivity1.this.mSearch.setPageNum(AroundListActivity1.this.page);
                AroundListActivity1.this.searchPoiDataByRadius(AroundListActivity1.this.searchByRadius);
            }
        });
        this.pullRefreshList.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                AroundListActivity1.this.isRefresh = false;
                AroundListActivity1.this.page = AroundListActivity1.this.page + 1;
                AroundListActivity1.this.mSearch.setPageNum(AroundListActivity1.this.page);
                AroundListActivity1.this.searchPoiDataByRadius(AroundListActivity1.this.searchByRadius);
            }
        });
        this.navListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity1.this.navPosition = position;
                AroundListActivity1.this.subDataList.clear();
                PlaceFiledNavModel navModel = (PlaceFiledNavModel) AroundListActivity1.this.navDataList.get(position);
                AroundListActivity1.this.setNavSelected(AroundListActivity1.this.navDataList, position);
                AroundListActivity1.this.navAdapter.notifyDataSetChanged();
                if (navModel.getType() == 4) {
                    AroundListActivity1.this.subAdapter.notifyDataSetChanged();
                    PlaceLocationHelper.getInastance().querySubAreaByAreaCode(AroundListActivity1.this.getApplicationContext(), navModel.getAreaCode(), new QueryAreaDelegate() {
                        public void onResult(AreaModel areaModel) {
                            if (!(areaModel == null || areaModel.getSubAreaList().isEmpty())) {
                                AroundListActivity1.this.subDataList.addAll(AroundListActivity1.this.dealSubAreaModel(areaModel));
                            }
                            AroundListActivity1.this.subAdapter.notifyDataSetChanged();
                        }
                    });
                    return;
                }
                AroundListActivity1.this.subDataList.addAll(((PlaceFiledNavModel) AroundListActivity1.this.navDataList.get(position)).getSubList());
                AroundListActivity1.this.subAdapter.notifyDataSetChanged();
            }
        });
        this.subListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity1.this.filedChoseBox.setVisibility(8);
                PlaceFiledSubModel subModel = (PlaceFiledSubModel) AroundListActivity1.this.subDataList.get(position);
                switch (subModel.getType()) {
                    case 1:
                        if (subModel.getKey().equals(StyleConstant.STYLE_DEFAULT)) {
                            AroundListActivity1.this.radius = AroundListActivity1.this.DEFAULT_RADIUS;
                        } else {
                            AroundListActivity1.this.radius = Integer.parseInt(((PlaceFiledSubModel) AroundListActivity1.this.subDataList.get(position)).getKey());
                        }
                        AroundListActivity1.this.tagRegion = null;
                        AroundListActivity1.this.searchByRadius = true;
                        AroundListActivity1.this.pullRefreshList.onRefresh(true);
                        return;
                    case 3:
                        if (!subModel.getName().equals(AroundListActivity1.this.allStr)) {
                            AroundListActivity1.this.tagKeywords = subModel.getName();
                        } else if (AroundListActivity1.this.navPosition == 0) {
                            AroundListActivity1.this.tagKeywords = null;
                        } else {
                            AroundListActivity1.this.tagKeywords = ((PlaceFiledNavModel) AroundListActivity1.this.navDataList.get(AroundListActivity1.this.navPosition)).getName();
                        }
                        AroundListActivity1.this.pullRefreshList.onRefresh(true);
                        return;
                    case 4:
                        AroundListActivity1.this.tagRegion = subModel.getName();
                        AroundListActivity1.this.searchByRadius = false;
                        AroundListActivity1.this.pullRefreshList.onRefresh(true);
                        return;
                    default:
                        return;
                }
            }
        });
        this.sortListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AroundListActivity1.this.setNavSelected(AroundListActivity1.this.sortDataList, position);
                AroundListActivity1.this.sortAdapter.notifyDataSetChanged();
                PlaceFiledNavModel sortModel = (PlaceFiledNavModel) AroundListActivity1.this.sortDataList.get(position);
                AroundListActivity1.this.filterModel.setSortName(sortModel.getKey());
                AroundListActivity1.this.filterModel.setSortRule(sortModel.getRule());
                AroundListActivity1.this.sortListView.setVisibility(8);
                AroundListActivity1.this.pullRefreshList.onRefresh(true);
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
            onDistanceBtnClickDo();
        } else if (v.equals(this.categoryBtn)) {
            onCategoryBtnClickDo();
        } else if (v.equals(this.sortBtn)) {
            onSortBtnClickDo();
        } else if (v.equals(this.mapBtn)) {
            onMapBtnClickDo();
        }
    }

    private void onMapBtnClickDo() {
        Intent intent = new Intent(this, AroundPoiMapMessageActivity.class);
        intent.putExtra(RouteConstant.POI_MODEL_LIST, (ArrayList) this.poiInfoList);
        startActivity(intent);
    }

    private void onDistanceBtnClickDo() {
        if (this.filedType == 1 && this.filedChoseBox.getVisibility() == 0) {
            this.filedChoseBox.setVisibility(8);
            return;
        }
        if (this.filedType == 2) {
            this.sortListView.setVisibility(8);
        }
        this.navPosition = 0;
        this.filedType = 1;
        PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
            public void onPlaceFiledResult(final PlaceTypeModel placeTypeModel) {
                PlaceLocationHelper.getInastance().queryAreaByAreaCode(AroundListActivity1.this.getApplicationContext(), AroundListActivity1.this.location.getAreaCode(), new QueryAreaDelegate() {
                    public void onResult(AreaModel areaModel) {
                        List<PlaceFiledNavModel> distanceFiledNavList = AroundListActivity1.this.dealFiledData(placeTypeModel, 1);
                        if (distanceFiledNavList == null) {
                            AroundListActivity1.this.warn("mc_place_no_distance");
                            return;
                        }
                        AroundListActivity1.this.filedChoseBox.setVisibility(0);
                        AroundListActivity1.this.navDataList.clear();
                        AroundListActivity1.this.navDataList.addAll(distanceFiledNavList);
                        AroundListActivity1.this.navDataList.addAll(AroundListActivity1.this.dealNavAreaModel(areaModel));
                        AroundListActivity1.this.setNavSelected(AroundListActivity1.this.navDataList, 0);
                        AroundListActivity1.this.navAdapter.notifyDataSetChanged();
                        AroundListActivity1.this.subDataList.clear();
                        AroundListActivity1.this.subDataList.addAll(((PlaceFiledNavModel) AroundListActivity1.this.navDataList.get(0)).getSubList());
                        AroundListActivity1.this.subAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void onCategoryBtnClickDo() {
        if (this.filedType == 3 && this.filedChoseBox.getVisibility() == 0) {
            this.filedChoseBox.setVisibility(8);
            return;
        }
        if (this.filedType == 2) {
            this.sortListView.setVisibility(8);
        }
        this.filedType = 3;
        this.navPosition = 0;
        PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
            public void onPlaceFiledResult(PlaceTypeModel placeTypeModel) {
                List<PlaceFiledNavModel> keyWordsFiledNavList = AroundListActivity1.this.dealFiledData(placeTypeModel, 3);
                if (keyWordsFiledNavList == null) {
                    AroundListActivity1.this.warn("mc_place_no_category");
                    return;
                }
                AroundListActivity1.this.filedChoseBox.setVisibility(0);
                AroundListActivity1.this.navDataList.clear();
                AroundListActivity1.this.navDataList.addAll(keyWordsFiledNavList);
                AroundListActivity1.this.setNavSelected(AroundListActivity1.this.navDataList, 0);
                AroundListActivity1.this.navAdapter.notifyDataSetChanged();
                AroundListActivity1.this.subDataList.clear();
                AroundListActivity1.this.subDataList.addAll(((PlaceFiledNavModel) AroundListActivity1.this.navDataList.get(0)).getSubList());
                AroundListActivity1.this.subAdapter.notifyDataSetChanged();
            }
        });
    }

    private void onSortBtnClickDo() {
        this.filedType = 2;
        if (this.sortDataList.isEmpty()) {
            PlaceFiledHelper.getInstance().queryFiledType(this, this.queryModel.getQueryType(), new PlaceFiledDelegate() {
                public void onPlaceFiledResult(PlaceTypeModel placeTypeModel) {
                    List<PlaceFiledNavModel> sortList = AroundListActivity1.this.dealFiledData(placeTypeModel, 2);
                    if (sortList == null) {
                        AroundListActivity1.this.warn("mc_place_no_sort");
                        return;
                    }
                    AroundListActivity1.this.sortDataList.clear();
                    AroundListActivity1.this.sortDataList.addAll(sortList);
                    AroundListActivity1.this.setNavSelected(AroundListActivity1.this.sortDataList, 0);
                    AroundListActivity1.this.sortAdapter.notifyDataSetChanged();
                    if (AroundListActivity1.this.filedChoseBox.getVisibility() == 0) {
                        AroundListActivity1.this.filedChoseBox.setVisibility(8);
                        if (AroundListActivity1.this.sortListView.getVisibility() == 0) {
                            AroundListActivity1.this.sortListView.setVisibility(8);
                        } else {
                            AroundListActivity1.this.sortListView.setVisibility(0);
                        }
                    } else if (AroundListActivity1.this.sortListView.getVisibility() == 0) {
                        AroundListActivity1.this.sortListView.setVisibility(8);
                    } else {
                        AroundListActivity1.this.sortListView.setVisibility(0);
                    }
                }
            });
        } else if (this.filedChoseBox.getVisibility() == 0) {
            this.filedChoseBox.setVisibility(8);
            if (this.sortListView.getVisibility() == 0) {
                this.sortListView.setVisibility(8);
            } else {
                this.sortListView.setVisibility(0);
            }
        } else if (this.sortListView.getVisibility() == 0) {
            this.sortListView.setVisibility(8);
        } else {
            this.sortListView.setVisibility(0);
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
                    System.out.println("num == null");
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

    private void setSubSelected(List<PlaceFiledSubModel> subList, int position) {
        int len = subList.size();
        for (int i = 0; i < len; i++) {
            if (i == position) {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(true);
            } else {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(false);
            }
        }
    }

    public void onBackPressed() {
        if (this.filedChoseBox.getVisibility() == 8 && this.sortListView.getVisibility() == 8) {
            super.onBackPressed();
            return;
        }
        this.filedChoseBox.setVisibility(8);
        this.sortListView.setVisibility(8);
    }
}
