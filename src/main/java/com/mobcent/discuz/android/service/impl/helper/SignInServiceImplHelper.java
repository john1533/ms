package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.BaseResult;

public class SignInServiceImplHelper implements BaseApiConstant {
    public static BaseResult parseResult(Context context, String jsonStr) {
        return BaseJsonHelper.formJsonRs(jsonStr, context);
    }
}
