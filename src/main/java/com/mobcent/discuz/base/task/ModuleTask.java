package com.mobcent.discuz.base.task;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.service.ConfigService;
import com.mobcent.discuz.android.service.impl.ConfigServiceImpl;

public class ModuleTask extends BaseTask<BaseResultModel<ConfigModuleModel>> {
    private ConfigService configService = new ConfigServiceImpl(this.context);
    private boolean isLocal;
    private long moduleId;

    public ModuleTask(Context context, boolean isLocal, long moduleId, BaseRequestCallback<BaseResultModel<ConfigModuleModel>> _callback) {
        super(context, _callback);
        this.isLocal = isLocal;
        this.moduleId = moduleId;
    }

    protected BaseResultModel<ConfigModuleModel> doInBackground(Void... params) {
        return this.configService.queryConfigModel(this.moduleId, this.isLocal);
    }
}
