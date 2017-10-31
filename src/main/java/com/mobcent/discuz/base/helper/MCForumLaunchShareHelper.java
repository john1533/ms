package com.mobcent.discuz.base.helper;

import android.content.Context;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.helper.MCShareHelper;
import java.io.File;

public class MCForumLaunchShareHelper {
    public static final String LOCAL_POSITION_DIR = (File.separator + MCLibIOUtil.MOBCENT + File.separator + "share" + File.separator);

    public static void share(Context context, MCShareModel shareModel) {
        shareModel.setDownloadUrl(getShareDownURL(shareModel.getDownloadUrl(), context));
        shareModel.setAppKey(getShareAppKey(context));
        MCShareHelper.share(context, shareModel);
    }

    public static void shareToWeChat(Context context, MCShareModel shareModel) {
        shareModel.setDownloadUrl(getShareDownURL(shareModel.getDownloadUrl(), context));
        shareModel.setAppKey(getShareAppKey(context));
        MCShareHelper.shareToWeChat(context, shareModel);
    }

    public static void shareToMoments(Context context, MCShareModel shareModel) {
        shareModel.setDownloadUrl(getShareDownURL(shareModel.getDownloadUrl(), context));
        shareModel.setAppKey(getShareAppKey(context));
        MCShareHelper.shareToMoments(context, shareModel);
    }

    private static String getImageSharePath(Context context) {
        return MCLibIOUtil.getBaseLocalLocation(context) + LOCAL_POSITION_DIR + "mcshareimage.jpg";
    }

    public static String getDefaultShareUrl(Context context) {
        int shareUrlResId = MCResource.getInstance(context).getStringId("mc_forum_share_url");
        if (shareUrlResId <= 0) {
            return "";
        }
        return context.getResources().getString(shareUrlResId);
    }

    private static String getShareDownURL(String shareURL, Context context) {
        String shareUrl = shareURL;
        if (!MCStringUtil.isEmpty(shareURL)) {
            return shareUrl;
        }
        try {
            return context.getResources().getString(MCResource.getInstance(context).getStringId("mc_discuz_base_request_url")) + context.getResources().getString(MCResource.getInstance(context).getStringId("mc_forum_share_back_url"));
        } catch (Exception e) {
            return "";
        }
    }

    private static String getShareAppKey(Context context) {
        String appkey = "";
        try {
            return SharedPreferencesDB.getInstance(context).getForumKey();
        } catch (Exception e) {
            return MCResource.getInstance(context).getString("mc_forum_share_key");
        }
    }
}
