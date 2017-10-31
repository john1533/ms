package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.Calendar;

public class MCStringBundleUtil {
    public static String resolveString(int stringSource, String[] args, Context context) {
        String string = context.getResources().getString(stringSource);
        for (int i = 0; i < args.length; i++) {
            string = string.replace("{" + i + "}", args[i]);
        }
        return string;
    }

    public static String resolveString(int stringSource, String arg, Context context) {
        return context.getResources().getString(stringSource).replace("{0}", arg);
    }

    public static String timeToString(Context context, long time) {
        MCResource forumResource = MCResource.getInstance(context);
        String timeString = "";
        if (Calendar.getInstance().getTimeInMillis() - time <= 60000) {
            return context.getResources().getString(forumResource.getStringId("mc_forum_just_now"));
        }
        if (time > 0) {
            return MCDateUtil.getFormatTime(time);
        }
        return timeString;
    }

    public static String getPathName(String url) {
        String pathName = "";
        try {
            if (url.contains("?") && url.contains("/")) {
                return url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
            }
            if (url.contains("/")) {
                return url.substring(url.lastIndexOf("/") + 1);
            }
            return pathName;
        } catch (Exception e) {
            return "";
        }
    }
}
