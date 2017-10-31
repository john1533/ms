package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.FeedBackRestfulApiRequester;
import com.mobcent.discuz.android.service.FeedBackService;

public class FeedBackServiceImpl implements FeedBackService {
    public int feedBack(Context context, String mainInfo, String stackTraceInfo, int projectType, String projectVersion) {
        FeedBackRestfulApiRequester.feedBack(context, mainInfo, stackTraceInfo, projectType, projectVersion);
        return 1;
    }
}
