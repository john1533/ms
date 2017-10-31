package com.mobcent.lowest.module.ad.delegate;

import com.mobcent.lowest.module.ad.model.AdContainerModel;

public interface AdViewDelegate {
    void freeMemery();

    void setAdContainerModel(AdContainerModel adContainerModel);
}
