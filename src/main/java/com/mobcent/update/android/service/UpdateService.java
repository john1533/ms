package com.mobcent.update.android.service;

import android.content.Context;
import com.mobcent.update.android.model.UpdateModel;

public interface UpdateService {
    UpdateModel getUpdateInfo(Context context);
}
