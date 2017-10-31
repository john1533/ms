package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PayStateModel;

public interface PayStateService {
    BaseResultModel<PayStateModel> queryPayByLocal(String str);

    BaseResultModel<PayStateModel> queryPayByNet(String str);
}
