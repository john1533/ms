package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.HashMap;

public class SignInApiRequester extends BaseDiscuzApiRequester {
    public static String signIn(Context context) {
        return BaseDiscuzApiRequester.doPostRequest(context, new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_request_plug_sign")).toString(), new HashMap());
    }
}
