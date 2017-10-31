package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartBeatModel;
import com.mobcent.discuz.android.model.HeartPushModel;

public interface HeartBeatService {
    BaseResultModel<HeartBeatModel> getHeartBeatModel();

    BaseResultModel<HeartPushModel> getHeartPushModel(long j);
}
