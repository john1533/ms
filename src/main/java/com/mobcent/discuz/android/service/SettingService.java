package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.SettingModel;

public interface SettingService {
    BaseResultModel<SettingModel> getSettingByLocal();

    BaseResultModel<SettingModel> getSettingContent(boolean z);
}
