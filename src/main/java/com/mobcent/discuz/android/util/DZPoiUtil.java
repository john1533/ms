package com.mobcent.discuz.android.util;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.impl.helper.LocationServiceImplHelper;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DZPoiUtil {
    private static DZPoiUtil poiUtil;
    private final long TIMEOUT;
    private Vector<PoiDelegate> delegates;
    private long lastRequestTime;
    private List<String> poi;
    private int times;

    public interface PoiDelegate {
        void onReceivePoi(List<String> list);
    }

    public static synchronized DZPoiUtil getInstance(Context context) {
        DZPoiUtil dZPoiUtil;
        synchronized (DZPoiUtil.class) {
            if (poiUtil == null) {
                poiUtil = new DZPoiUtil(context.getApplicationContext());
            }
            dZPoiUtil = poiUtil;
        }
        return dZPoiUtil;
    }

    private DZPoiUtil(Context context) {
        this.poi = null;
        this.delegates = null;
        this.times = 0;
        this.lastRequestTime = 0;
        this.TIMEOUT = 60;

    }

    private void notifyDelegate(List<String> poi) {
        if (this.delegates != null) {
            Iterator it = this.delegates.iterator();
            while (it.hasNext()) {
                PoiDelegate delegate = (PoiDelegate) it.next();
                if (delegate != null) {
                    delegate.onReceivePoi(poi);
                }
            }
            this.delegates.clear();
        }
    }

    public synchronized void requestPoi(PoiDelegate delegate) {
        MCLogUtil.i("PoiUtil", "requestPoi");
        if (this.poi == null || isLocationInfoTimeOut()) {
            if (!this.delegates.contains(delegate)) {
                this.delegates.add(delegate);
            }

            this.times = 0;
            this.lastRequestTime = System.currentTimeMillis();
        } else {
            delegate.onReceivePoi(this.poi);
        }
    }

    private boolean isLocationInfoTimeOut() {
        if (System.currentTimeMillis() - this.lastRequestTime > 60) {
            return true;
        }
        return false;
    }

    public synchronized void stopLocation() {
    }
}
