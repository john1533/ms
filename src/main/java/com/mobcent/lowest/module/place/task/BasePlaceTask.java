package com.mobcent.lowest.module.place.task;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.module.place.service.PlaceAroundService;
import com.mobcent.lowest.module.place.service.impl.PlaceAroundServiceImpl;

public abstract class BasePlaceTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected PlaceAroundService aroundService;
    protected Context context;

    public BasePlaceTask(Context context) {
        this.context = context;
        this.aroundService = new PlaceAroundServiceImpl(context);
    }
}
