package com.mobcent.update.android.service.impl;

import android.content.Context;
import com.mobcent.update.android.api.UpdateRestfulApiRequester;
import com.mobcent.update.android.db.UpdateStatusDBUtil;
import com.mobcent.update.android.model.UpdateModel;
import com.mobcent.update.android.service.UpdateService;
import com.mobcent.update.android.service.impl.helper.UpdateServiceImplHelper;

public class UpdateServiceImpl implements UpdateService {
    private UpdateStatusDBUtil dbUtil;
    private Context mContext;

    public UpdateServiceImpl(Context context) {
        this.mContext = context;
        this.dbUtil = new UpdateStatusDBUtil(context);
    }

    public UpdateModel getUpdateInfo(Context context) {
        UpdateModel updateModel = UpdateServiceImplHelper.getUpdateInfo(UpdateRestfulApiRequester.getUpdateInfo(this.mContext));
        return updateModel != null ? updateModel : null;
    }
}
