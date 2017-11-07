package com.mobcent.lowest.module.ad.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.cache.AdPositionsCache;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.db.DownSqliteHelper;
import com.mobcent.lowest.module.ad.delegate.AdManagerListener;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.model.RequestParamsModel;
import com.mobcent.lowest.module.ad.service.impl.AdServiceImpl;
import com.mobcent.lowest.module.ad.task.ActiveDoTask;
import com.mobcent.lowest.module.ad.task.DownDoTask;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class AdManager {
    private static AdManager adHelper;
    public String TAG = "AdManager";
    private Map<String, Map<Integer, AdContainerModel>> activityAdCache = new HashMap();
    private Map<String, Map<String, List<AdContainerModel>>> activityAdContainerCache = new HashMap();
    private AdDatasCache adDatasCache;
    private AdObserver adObservers = new AdObserver();
    private AdPositionsCache adPositionsCache;
    private Set<Long> exposureSet = new HashSet();
    private AsyncTask<Context, Void, AdPositionsCache> haveAdTask = null;
    private boolean haveNoNet = false;
    private AsyncTask<Context, Void, AdDatasCache> requestAdTask = null;
    private long requestAdTimeMillis;
    private RequestParamsModel requestParams;

    final class AdObserver extends Observable {
        AdObserver() {
        }

        public void getAdOk() {
            setChanged();
            notifyObservers();
            deleteObservers();
        }
    }

    class GetAdTask extends AsyncTask<Context, Void, AdDatasCache> {
        GetAdTask() {
        }

        protected AdDatasCache doInBackground(Context... params) {
            return new AdServiceImpl(params[0]).getAd();
        }

        protected void onPostExecute(AdDatasCache result) {
            if (result != null) {
                AdManager.this.setAdDatasCache(null);
                AdManager.this.getExposureSet().clear();
                AdManager.this.setAdDatasCache(result);
                return;
            }
            AdManager.this.requestAdTimeMillis = System.currentTimeMillis();
        }
    }

    class HaveAdTask extends AsyncTask<Context, Void, AdPositionsCache> {
        HaveAdTask() {
        }

        protected AdPositionsCache doInBackground(Context... params) {
            return new AdServiceImpl(params[0]).haveAd();
        }

        protected void onPostExecute(AdPositionsCache result) {
            if (result == null) {
                AdManager.this.haveNoNet = false;
            } else if (result.isConectFailed()) {
                AdManager.this.haveAdTask = null;
                AdManager.this.haveNoNet = true;
            } else {
                AdManager.this.setAdPositionsCache(result);
                AdManager.this.haveNoNet = false;
            }
        }
    }

    public static AdManager getInstance() {
        if (adHelper == null) {
            adHelper = new AdManager();
        }
        return adHelper;
    }

    public boolean isHaveNoNet() {
        return this.haveNoNet;
    }

    public void setHaveNoNet(boolean haveNoNet) {
        this.haveNoNet = haveNoNet;
    }

    public Set<Long> getExposureSet() {
        return this.exposureSet;
    }

    public AsyncTask<Context, Void, AdPositionsCache> getHaveAdTask() {
        return this.haveAdTask;
    }

    public void setHaveAdTask(AsyncTask<Context, Void, AdPositionsCache> haveAdTask) {
        this.haveAdTask = haveAdTask;
    }

    public synchronized AsyncTask<Context, Void, AdDatasCache> getRequestAdTask() {
        return this.requestAdTask;
    }

    public void setRequestAdTask(AsyncTask<Context, Void, AdDatasCache> requestAdTask) {
        this.requestAdTask = requestAdTask;
    }

    public RequestParamsModel getRequestParams() {
        return this.requestParams;
    }

    public AdPositionsCache getAdPositionsCache() {
        return this.adPositionsCache;
    }

    public void setAdPositionsCache(AdPositionsCache adPositionsCache) {
        this.adPositionsCache = adPositionsCache;
    }

    public AdDatasCache getAdDatasCache() {
        return this.adDatasCache;
    }

    public void setAdDatasCache(AdDatasCache adDatasCache) {
        this.adDatasCache = adDatasCache;
        if (adDatasCache != null) {
            this.requestAdTimeMillis = System.currentTimeMillis();
            this.adObservers.getAdOk();
        }
    }

    public Map<String, Map<Integer, AdContainerModel>> getActivityAdCache() {
        return this.activityAdCache;
    }

    public void setActivityAdCache(Map<String, Map<Integer, AdContainerModel>> activityAdCache) {
        this.activityAdCache = activityAdCache;
    }

    public Map<String, Map<String, List<AdContainerModel>>> getActivityAdContainerCache() {
        return this.activityAdContainerCache;
    }

    public void setActivityAdContainerCache(Map<String, Map<String, List<AdContainerModel>>> activityAdContainerCache) {
        this.activityAdContainerCache = activityAdContainerCache;
    }

    public void recyclAdByTag(String tag) {
        if (this.activityAdCache.get(tag) != null) {
            this.activityAdCache.remove(tag);
        }
    }

    public void recyclAdContainerByTag(String tag) {
        if (this.activityAdContainerCache.get(tag) != null) {
            this.activityAdContainerCache.remove(tag);
        }
    }

    public void init(Context context) {
        this.requestParams = new RequestParamsModel();
        this.requestParams.setIm(MCPhoneUtil.getIMEI(context.getApplicationContext()));
        this.requestParams.setPt(new StringBuilder(AdConstant.ANDROID_OS).append(MCPhoneUtil.getSDKVersion()).toString());
        this.requestParams.setMc(new StringBuilder(AdConstant.MAC).append(MCPhoneUtil.getLocalMacAddress(context.getApplicationContext())).toString());
        this.requestParams.setSs(MCPhoneUtil.getResolution(context.getApplicationContext()));
        this.requestParams.setUa(MCPhoneUtil.getPhoneType());
        this.requestParams.setZo(MCPhoneUtil.getPhoneLanguage());
        this.requestParams.setPn(context.getPackageName());
        this.requestParams.setAk(LowestManager.getInstance().getConfig().getForumKey());
        this.requestParams.setCh(LowestManager.getInstance().getConfig().getChannelNum());
        this.requestParams.setUid(LowestManager.getInstance().getConfig().getUserId());
//        try {
            Log.v(TAG,"init:"+new WebView(context).getSettings().getUserAgentString());
//            this.requestParams.setUserAgent(new WebView(context).getSettings().getUserAgentString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        requestHaveAd(context.getApplicationContext());
    }

    public void requestHaveAd(Context context) {
        if (this.adPositionsCache == null && this.haveAdTask == null) {
            this.haveAdTask = new HaveAdTask().execute(new Context[]{context});
        }
    }

    public void requestAdDatas(Context context, boolean isForce) {
        if (this.requestAdTask == null || this.adDatasCache == null || (isForce && this.requestAdTask.getStatus() != Status.RUNNING)) {
            this.requestAdTask = new GetAdTask().execute(new Context[]{context});
        }
    }

    private void sendActiveDo(Context context) {
        for (AdDownDbModel dbModel : DownSqliteHelper.getInstance(context).queryUnActiveDoList()) {
            new ActiveDoTask(context, dbModel).execute(new Void[0]);
        }
    }

    private void sendDownDo(Context context) {
        for (AdDownDbModel dbModel : DownSqliteHelper.getInstance(context).queryUnDownDoList()) {
            new DownDoTask(context, dbModel).execute(new Void[0]);
        }
    }

    public void getHaveAdPositions(final int[] positions, final AdManagerListener listener) {
        if (this.haveAdTask.getStatus() != Status.FINISHED) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    AdManager.this.getHaveAdPositions(positions, listener);
                }
            }, 1000);
        } else if (this.adPositionsCache != null && listener != null) {
            listener.setPositions(this.adPositionsCache.isHaveAd(positions));
        }
    }

    public boolean isOverDue() {
        if (this.haveAdTask == null || this.haveAdTask.getStatus() != Status.FINISHED || System.currentTimeMillis() - this.requestAdTimeMillis <= ((long) ((this.adPositionsCache.getCycle() * 1000) * 60))) {
            return false;
        }
        return true;
    }

    public void registerObserver(Observer observer) {
        this.adObservers.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        try {
            this.adObservers.deleteObserver(observer);
        } catch (Exception e) {
        }
    }
}
