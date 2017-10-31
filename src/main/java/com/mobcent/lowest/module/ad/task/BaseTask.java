package com.mobcent.lowest.module.ad.task;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.service.AdService;
import com.mobcent.lowest.module.ad.service.impl.AdServiceImpl;

public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected AdService adService;
    protected Context context;
    protected MCResource resource;

    public BaseTask(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.adService = new AdServiceImpl(context);
    }
}
