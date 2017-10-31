package com.mobcent.discuz.module.person.activity.helper;

import android.content.Context;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlatformLoginHelper {
    public static final Long PLATFORM_QQ = Long.valueOf(20);
    private static PlatformLoginHelper helper;
    private static Map<Long, String> infos = new LinkedHashMap();

    private PlatformLoginHelper(Context context) {
        infos.clear();
        if (SharedPreferencesDB.getInstance(context.getApplicationContext()).getQQConnect() == 1) {
            infos.put(PLATFORM_QQ, "mc_forum_shareto_icon7");
        }
    }

    public static synchronized PlatformLoginHelper getInstance(Context context) {
        PlatformLoginHelper platformLoginHelper;
        synchronized (PlatformLoginHelper.class) {
            if (helper == null) {
                helper = new PlatformLoginHelper(context.getApplicationContext());
            }
            platformLoginHelper = helper;
        }
        return platformLoginHelper;
    }

    public Map<Long, String> getInfos() {
        return infos;
    }
}
