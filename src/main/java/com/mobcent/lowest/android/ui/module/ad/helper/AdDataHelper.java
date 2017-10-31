package com.mobcent.lowest.android.ui.module.ad.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import com.mobcent.lowest.android.ui.module.ad.delegate.AdGetDateDelegate;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdStateModel;
import java.util.List;
import java.util.Observer;

public class AdDataHelper implements AdConstant {
    public static String TAG = "AdDataHelper";

    public static synchronized void createAdData(Context context, int[] adPositions, Observer observer, AdGetDateDelegate _callback) {
        synchronized (AdDataHelper.class) {
            AdManager adManager = AdManager.getInstance();
            if (adManager.getAdPositionsCache() != null) {
                List<AdStateModel> adStateModels = adManager.getAdPositionsCache().getAdStates(adPositions);
                if (!(adStateModels == null || adStateModels.isEmpty())) {
                    if (adManager.getAdDatasCache() == null) {
                        postDelayRequestAd(observer);
                        AsyncTask<Context, Void, AdDatasCache> adTask = adManager.getRequestAdTask();
                        if (adTask == null) {
                            adManager.requestAdDatas(context.getApplicationContext(), false);
                        } else if (adTask.getStatus() == Status.FINISHED) {
                            MCLogUtil.e(TAG, "get ad data failed");
                        }
                    } else if (adManager.isOverDue()) {
                        postDelayRequestAd(observer);
                        adManager.requestAdDatas(context.getApplicationContext(), true);
                    } else if (adManager.getAdDatasCache().getAdDataMap().isEmpty()) {
                        MCLogUtil.e(TAG, "ad set is empty");
                    } else {
                        List<AdContainerModel> adContainerModels = AdManager.getInstance().getAdDatasCache().createAdContainerModels(adStateModels);
                        if (_callback != null) {
                            _callback.onAdDateEndCallBack(adContainerModels);
                        }
                    }
                }
            } else if (((adManager.getHaveAdTask() != null && adManager.getHaveAdTask().getStatus() == Status.RUNNING) || adManager.isHaveNoNet()) && adManager.isHaveNoNet()) {
                adManager.requestHaveAd(context.getApplicationContext());
            }
        }
    }

    private static void postDelayRequestAd(Observer observer) {
        AdManager.getInstance().removeObserver(observer);
        AdManager.getInstance().registerObserver(observer);
    }
}
