package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;

public interface ConfigService {
    BaseResultModel<ConfigModel> getConfigModel(boolean z);

    BaseResultModel<ConfigModel> getConfigModelAssets(String str);

    BaseResultModel<ConfigModel> getConfigModelByLocal();

    BaseResultModel<ConfigModuleModel> queryConfigModel(long j, boolean z);
}
