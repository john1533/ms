package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.HeartBeatRestfulApiRequester;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartBeatModel;
import com.mobcent.discuz.android.model.HeartPushModel;
import com.mobcent.discuz.android.service.HeartBeatService;
import com.mobcent.discuz.android.service.impl.helper.HeartBeatServiceImplHelper;

public class HeartBeatServiceImpl implements HeartBeatService {
    private Context context;

    public HeartBeatServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<HeartBeatModel> getHeartBeatModel() {
        return HeartBeatServiceImplHelper.parseHeartBeatModel(this.context, HeartBeatRestfulApiRequester.getHeartModel(this.context));
    }

    public BaseResultModel<HeartPushModel> getHeartPushModel(long userId) {
        return HeartBeatServiceImplHelper.parseHeartPushModel(HeartBeatRestfulApiRequester.getIMSDKHeartBeat(this.context, userId));
    }
}
