package com.mobcent.discuz.android.util;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;

public class DZErrorInfoUtils {
    public static String dataPaseError(Context context) {
        return MCResource.getInstance(context).getString("mc_forum_parse_data_error");
    }
}
