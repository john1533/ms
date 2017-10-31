package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.mobcent.discuz.android.constant.FeedBackConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.HashMap;

public class FeedBackRestfulApiRequester extends BaseDiscuzApiRequester implements FeedBackConstant {
    private static final String BASE_URL = "http://www.mobcent.com/";

    public static String feedBack(Context context, String mainInfo, String stackTraceInfo, int projectType, String projectVersion) {
        String urlString = "http://www.mobcent.com/count_query/mobcent/feedback.do";
        HashMap<String, String> params = new HashMap();
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(context);
        params.put(FeedBackConstant.MAIN_INFO, mainInfo);
        params.put(FeedBackConstant.STACK_TRACE_INFO, stackTraceInfo);
        params.put(FeedBackConstant.PHONE_TYPE, MCPhoneUtil.getPhoneType());
        params.put(FeedBackConstant.PHONE_OS_VERSION, MCPhoneUtil.getSDKVersionName());
        params.put("platType", "1");
        params.put("appKey", shareDB.getForumKey());
        params.put("appName", MCAppUtil.getAppName(context));
        params.put(FeedBackConstant.APP_VERSION, MCAppUtil.getVersionName(context));
        String serviceType = MCPhoneUtil.getServiceName(context);
        if (serviceType.equals("wifi")) {
            params.put(FeedBackConstant.NETWORK_TYPE, "1");
            params.put(FeedBackConstant.SERVICE_TYPE, "");
        } else if (serviceType.equals("mobile")) {
            params.put(FeedBackConstant.NETWORK_TYPE, getNetWorkType(context));
            params.put(FeedBackConstant.SERVICE_TYPE, "1");
        } else if (serviceType.equals("unicom")) {
            params.put(FeedBackConstant.NETWORK_TYPE, getNetWorkType(context));
            params.put(FeedBackConstant.SERVICE_TYPE, "2");
        } else if (serviceType.equals("telecom")) {
            params.put(FeedBackConstant.NETWORK_TYPE, getNetWorkType(context));
            params.put(FeedBackConstant.SERVICE_TYPE, "3");
        }
        params.put("userId", new StringBuilder(String.valueOf(shareDB.getUserId())).toString());
        params.put(FeedBackConstant.PROJECT_TYPE, new StringBuilder(String.valueOf(projectType)).toString());
        params.put(FeedBackConstant.PROJECT_VERSION, projectVersion);
        return DZHttpClientUtil.doPostRequest(urlString, params, context);
    }

    public static String getNetWorkType(Context context) {
        String netWorkType = MCPhoneUtil.getNetWorkName(context);
        if (netWorkType.equals("cmwap")) {
            return "2";
        }
        if (netWorkType.equals("cmnet")) {
            return "2";
        }
        if (netWorkType.equals("ctwap")) {
            return "2";
        }
        if (netWorkType.equals("ctnet")) {
            return "2";
        }
        if (netWorkType.equals("uniwap")) {
            return "2";
        }
        if (netWorkType.equals("uninet")) {
            return "2";
        }
        if (netWorkType.equals("3gwap")) {
            return "3";
        }
        if (netWorkType.equals("3gnet")) {
            return "3";
        }
        return "1";
    }
}
