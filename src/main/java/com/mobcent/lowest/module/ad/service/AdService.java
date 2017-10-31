package com.mobcent.lowest.module.ad.service;

import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.cache.AdPositionsCache;
import com.mobcent.lowest.module.ad.model.AdModel;

public interface AdService {
    boolean activeAdDo(long j, int i, String str, String str2);

    boolean downAdDo(long j, int i, String str, String str2);

    void exposureAd(String str);

    AdDatasCache getAd();

    String getLinkUrl(long j, long j2, String str);

    AdModel getSplashAd();

    AdPositionsCache haveAd();
}
