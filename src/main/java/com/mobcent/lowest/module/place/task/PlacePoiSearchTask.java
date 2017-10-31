package com.mobcent.lowest.module.place.task;

import android.content.Context;
import com.mobcent.lowest.module.place.delegate.PlacePoiSearchDelegate;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.service.PlaceAroundService;
import com.mobcent.lowest.module.place.service.impl.PlaceAroundServiceImpl;

public class PlacePoiSearchTask extends BasePlaceTask<Void, Void, PlacePoiResult> {
    private PlaceApiFilterModel filter;
    private String location = null;
    private int pageNum;
    private int pageSize;
    private PlaceAroundService placeAroundService;
    private PlacePoiSearchDelegate poiSearchDelegate;
    private String query;
    private int radius = 0;
    private String regoin;
    private int scope;
    private String tag;

    public PlacePoiSearchTask(Context context, String query, String tag, int scope, String regoin, int pageSize, int pageNum, PlaceApiFilterModel filter) {
        super(context);
        this.placeAroundService = new PlaceAroundServiceImpl(context);
        this.query = query;
        this.tag = tag;
        this.scope = scope;
        this.regoin = regoin;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.filter = filter;
    }

    public PlacePoiSearchTask(Context context, String query, String tag, int scope, String regoin, String location, int radius, int pageSize, int pageNum, PlaceApiFilterModel filter) {
        super(context);
        this.placeAroundService = new PlaceAroundServiceImpl(context);
        this.query = query;
        this.tag = tag;
        this.scope = scope;
        this.regoin = regoin;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.filter = filter;
        this.radius = radius;
        this.location = location;
    }

    protected PlacePoiResult doInBackground(Void... arg0) {
        return this.placeAroundService.queryPoiList(this.query, this.tag, this.scope, this.regoin, this.location, this.radius, this.pageSize, this.pageNum, this.filter);
    }

    protected void onPostExecute(PlacePoiResult result) {
        if (this.poiSearchDelegate != null) {
            this.poiSearchDelegate.onPlacePoiResult(result);
        }
    }

    public PlacePoiSearchDelegate getPoiSearchDelegate() {
        return this.poiSearchDelegate;
    }

    public void setPoiSearchDelegate(PlacePoiSearchDelegate poiSearchDelegate) {
        this.poiSearchDelegate = poiSearchDelegate;
    }
}
