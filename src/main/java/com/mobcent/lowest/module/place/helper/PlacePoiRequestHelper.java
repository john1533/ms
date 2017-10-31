package com.mobcent.lowest.module.place.helper;

import android.content.Context;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.place.delegate.PlacePoiSearchDelegate;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import com.mobcent.lowest.module.place.task.PlacePoiSearchTask;

public class PlacePoiRequestHelper {
    private Context context;
    private PlaceApiFilterModel filter;
    private int pageNum = 0;
    private int pageSize = 10;
    private PlacePoiSearchDelegate poiSearchDelegate;
    private int scope = 2;
    private PlacePoiSearchTask task = null;

    public PlacePoiRequestHelper(Context context) {
        this.context = context;
    }

    public void searchPoiData(String query, String tag, String regoin) {
        if (!(this.task == null || this.task.isCancelled())) {
            this.task.cancel(true);
        }
        this.task = new PlacePoiSearchTask(this.context, query, tag, this.scope, regoin, this.pageSize, this.pageNum, this.filter);
        this.task.setPoiSearchDelegate(this.poiSearchDelegate);
        this.task.execute(new Void[0]);
    }

    public void searchPoiDataByRadius(String query, String tag, String regoin, double lat, double lng, int radius) {
        if (!(this.task == null || this.task.isCancelled())) {
            this.task.cancel(true);
        }
        String location = null;
        if (!(lat == 0.0d || lng == 0.0d)) {
            location = new StringBuilder(String.valueOf(lat)).append(AdApiConstant.RES_SPLIT_COMMA).append(lng).toString();
        }
        this.task = new PlacePoiSearchTask(this.context, query, tag, this.scope, regoin, location, radius, this.pageSize, this.pageNum, this.filter);
        this.task.setPoiSearchDelegate(this.poiSearchDelegate);
        this.task.execute(new Void[0]);
    }

    public void registerPoiSearchDelegate(PlacePoiSearchDelegate poiSearchDelegate) {
        this.poiSearchDelegate = poiSearchDelegate;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getScope() {
        return this.scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public PlaceApiFilterModel getFilter() {
        return this.filter;
    }

    public void setFilter(PlaceApiFilterModel filter) {
        this.filter = filter;
    }
}
