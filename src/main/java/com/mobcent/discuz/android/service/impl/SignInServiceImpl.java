package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.SignInApiRequester;
import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.discuz.android.service.SignInService;
import com.mobcent.discuz.android.service.impl.helper.SignInServiceImplHelper;

public class SignInServiceImpl extends BaseServiceImpl implements SignInService {
    public SignInServiceImpl(Context context) {
        super(context);
    }

    public BaseResult signIn() {
        return SignInServiceImplHelper.parseResult(this.context, SignInApiRequester.signIn(this.context));
    }
}
