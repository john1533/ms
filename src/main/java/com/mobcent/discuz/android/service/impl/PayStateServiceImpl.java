package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.PayStateRestfulApiRequester;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.service.PayStateService;
import com.mobcent.discuz.android.service.impl.helper.PayStateServiceHelper;

public class PayStateServiceImpl extends BaseServiceImpl implements PayStateService {
    public PayStateServiceImpl(Context context) {
        super(context);
    }

    public BaseResultModel<PayStateModel> queryPayByNet(String forumKey) {
        BaseResultModel<PayStateModel> result = PayStateServiceHelper.controll(PayStateRestfulApiRequester.controll(this.context, forumKey));
        if (result.getRs() == 1 && result.getData() != null) {
            SharedPreferencesDB.getInstance(this.context).setPayState(result.getData());
        }
        return result;
    }

    public BaseResultModel<PayStateModel> queryPayByLocal(String forumKey) {
        BaseResultModel<PayStateModel> result = new BaseResultModel();
        if (SharedPreferencesDB.getInstance(this.context).isPayStateExist()) {
            result.setData(SharedPreferencesDB.getInstance(this.context).getPayState());
            result.setRs(1);
        } else {
            result.setRs(0);
        }
        return result;
    }
}
