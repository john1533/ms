package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.db.constant.HeartBeatConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartBeatModel;
import com.mobcent.discuz.android.model.HeartInfoModel;
import com.mobcent.discuz.android.model.HeartMsgModel;
import com.mobcent.discuz.android.model.HeartPushInfoModel;
import com.mobcent.discuz.android.model.HeartPushModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HeartBeatServiceImplHelper implements HeartBeatConstant {
    public static BaseResultModel<HeartBeatModel> parseHeartBeatModel(Context context, String jsonStr) {
        BaseResultModel<HeartBeatModel> result = new BaseResultModel();
        BaseJsonHelper.formJsonRs(context, jsonStr, result);
        if (result.getRs() != 0) {
            try {
                HeartBeatModel heartModel = new HeartBeatModel();
                JSONObject bodyObject = new JSONObject(jsonStr).optJSONObject("body");
                JSONObject externObject = bodyObject.optJSONObject("externInfo");
                heartModel.setHeartTime(externObject.optLong(HeartBeatConstant.HEARTPERIOD, 10000));
                heartModel.setPmPeriod(externObject.optLong(HeartBeatConstant.PMPERIOD, 5000));
                JSONObject replyObject = bodyObject.optJSONObject(HeartBeatConstant.REPLYINFO);
                HeartInfoModel replyInfoModel = new HeartInfoModel();
                replyInfoModel.setCount(replyObject.optInt("count"));
                replyInfoModel.setTime(Long.valueOf(replyObject.optString("time")).longValue());
                heartModel.setReplyInfo(replyInfoModel);
                JSONObject atMeObject = bodyObject.optJSONObject(HeartBeatConstant.ATMEINFO);
                HeartInfoModel atInfoModel = new HeartInfoModel();
                atInfoModel.setCount(atMeObject.optInt("count"));
                atInfoModel.setTime(Long.valueOf(atMeObject.optString("time")).longValue());
                heartModel.setAtMeInfo(atInfoModel);
                JSONObject friendObject = bodyObject.optJSONObject(HeartBeatConstant.FRIENDINFO);
                HeartInfoModel friendInfoModel = new HeartInfoModel();
                friendInfoModel.setCount(friendObject.optInt("count"));
                friendInfoModel.setTime(Long.valueOf(friendObject.optString("time")).longValue());
                heartModel.setFriendInfo(friendInfoModel);
                JSONArray pmArray = bodyObject.optJSONArray("pmInfos");
                List<HeartMsgModel> pmInfos = new ArrayList();
                if (pmArray != null) {
                    int len = pmArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject pmObject = pmArray.optJSONObject(i);
                        HeartMsgModel msgModel = new HeartMsgModel();
                        msgModel.setFromUid(pmObject.optLong("fromUid"));
                        msgModel.setPlid(pmObject.optInt("plid"));
                        msgModel.setPmid(pmObject.optInt("pmid"));
                        msgModel.setTime(Long.valueOf(pmObject.optString("time")).longValue());
                        pmInfos.add(msgModel);
                    }
                }
                heartModel.setPmInfos(pmInfos);
                result.setData(heartModel);
            } catch (Exception e) {
                MCLogUtil.e("HeartBeatServiceImplHelper", e.toString());
            }
        }
        return result;
    }

    public static BaseResultModel<HeartPushModel> parseHeartPushModel(String json) {
        BaseResultModel<HeartPushModel> result = new BaseResultModel();
        try {
            JSONObject rootObj = new JSONObject(json);
            int rs = rootObj.optInt("rs");
            result.setRs(rs);
            if (rs != 0) {
                HeartPushModel pushModel = new HeartPushModel();
                pushModel.setHbTime(rootObj.optInt(HeartBeatConstant.HB_TIME));
                JSONArray pushArray = rootObj.optJSONArray("push");
                if (pushArray != null && pushArray.length() > 0) {
                    List<HeartPushInfoModel> pushList = new ArrayList();
                    int len = pushArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject pushObj = pushArray.optJSONObject(i);
                        HeartPushInfoModel pushInfoModel = new HeartPushInfoModel();
                        pushInfoModel.setPushMsgId(pushObj.optLong(HeartBeatConstant.PUSH_MSG_ID));
                        pushInfoModel.setType(pushObj.optInt("type"));
                        pushInfoModel.setTitle(pushObj.optString("title"));
                        pushInfoModel.setDesc(pushObj.optString("desc"));
                        pushInfoModel.setTopicId(pushObj.optLong("topic_id"));
                        pushInfoModel.setDetailType(pushObj.optInt(HeartBeatConstant.DETAIL_TYPE));
                        pushList.add(pushInfoModel);
                    }
                    pushModel.setPushList(pushList);
                }
                result.setData(pushModel);
            }
        } catch (Exception e) {
            result.setRs(0);
        }
        return result;
    }
}
