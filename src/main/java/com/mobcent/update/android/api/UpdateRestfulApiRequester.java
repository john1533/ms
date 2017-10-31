package com.mobcent.update.android.api;

import android.content.Context;
import com.mobcent.update.android.util.MCUpdateResource;
import java.util.HashMap;

public class UpdateRestfulApiRequester extends BaseRestfulApiRequester {
    public static String getUpdateInfo(Context context) {
        String baseURL = context.getResources().getString(MCUpdateResource.getInstance(context).getStringId("mc_update_domain_url"));
        return BaseRestfulApiRequester.doPostRequest(new StringBuilder(String.valueOf(baseURL)).append("push/update.do").toString(), new HashMap(), context);
    }
}
