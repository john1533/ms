package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.ReportRestfulApiRequester;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.ReportService;
import com.mobcent.discuz.android.service.impl.helper.UserServiceImplHelper;

public class ReportServiceImpl implements ReportService {
    private Context context;

    public ReportServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel report(long id, String reason, String idType) {
        return UserServiceImplHelper.getManageUser(this.context, ReportRestfulApiRequester.report(this.context, id, reason, idType));
    }
}
