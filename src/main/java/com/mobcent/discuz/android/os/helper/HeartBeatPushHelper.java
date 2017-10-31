package com.mobcent.discuz.android.os.helper;

import android.content.Context;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartPushInfoModel;
import com.mobcent.discuz.android.model.HeartPushModel;
import com.mobcent.discuz.android.os.helper.HeartNotificationHelper.NotifyInfoModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.List;

public class HeartBeatPushHelper {
    public static void dealPush(Context context, BaseResultModel<HeartPushModel> result) {
        HeartPushModel heartModel = (HeartPushModel) result.getData();
        if (result.getRs() != 0 && heartModel != null && !MCListUtils.isEmpty(heartModel.getPushList())) {
            List<HeartPushInfoModel> pushInfos = ((HeartPushModel) result.getData()).getPushList();
            if (getLastPushMsgId(context) != ((HeartPushInfoModel) pushInfos.get(0)).getPushMsgId()) {
                setLastPushMsgId(context, ((HeartPushInfoModel) pushInfos.get(0)).getPushMsgId());
                int len = pushInfos.size();
                for (int i = 0; i < len; i++) {
                    HeartPushInfoModel pushInfo = (HeartPushInfoModel) pushInfos.get(i);
                    NotifyInfoModel notifyInfoModel = new NotifyInfoModel();
                    notifyInfoModel.data = pushInfo;
                    notifyInfoModel.title = pushInfo.getTitle();
                    notifyInfoModel.content = pushInfo.getDesc();
                    notifyInfoModel.id = 4;
                    HeartNotificationHelper.getInstance(context).notifyNotication(notifyInfoModel);
                }
            }
        }
    }

    public static void setLastPushMsgId(Context context, long lastPushMsgId) {
        SharedPreferencesDB.getInstance(context).setLastPushId(lastPushMsgId);
    }

    public static long getLastPushMsgId(Context context) {
        return SharedPreferencesDB.getInstance(context).getLastPushId();
    }
}
