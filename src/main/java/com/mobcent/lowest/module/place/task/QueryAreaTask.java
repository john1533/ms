package com.mobcent.lowest.module.place.task;

import android.content.Context;
import com.mobcent.lowest.module.place.delegate.QueryAreaDelegate;
import com.mobcent.lowest.module.place.model.AreaModel;

public class QueryAreaTask extends BasePlaceTask<Void, Void, AreaModel> {
    private String areaCode;
    private QueryAreaDelegate delegate;
    private boolean queryArea = true;

    public QueryAreaTask(Context context, String areaCode) {
        super(context);
        this.areaCode = areaCode;
    }

    protected AreaModel doInBackground(Void... arg0) {
        if (this.queryArea) {
            return this.aroundService.queryAreaByCityCode(this.areaCode);
        }
        return this.aroundService.querySubAreaByCityCode(this.areaCode);
    }

    protected void onPostExecute(AreaModel result) {
        super.onPostExecute(result);
        if (this.delegate != null) {
            this.delegate.onResult(result);
        }
    }

    public QueryAreaDelegate getDelegate() {
        return this.delegate;
    }

    public void setDelegate(QueryAreaDelegate delegate) {
        this.delegate = delegate;
    }

    public boolean isQueryArea() {
        return this.queryArea;
    }

    public void setQueryArea(boolean queryArea) {
        this.queryArea = queryArea;
    }
}
