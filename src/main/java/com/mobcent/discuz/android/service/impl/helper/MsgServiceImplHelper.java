package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.MsgConstant;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.FriendModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.model.OtherPanelModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MsgServiceImplHelper implements MsgConstant {
    public static String getMsgUserListJson(int page, int pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", page);
            jsonObject.put("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<List<MsgUserListModel>> getSessionList(Context context, String jsonStr, Map<Long, MsgDBModel> cacheMap) {
        BaseResultModel<List<MsgUserListModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject bodyObject = new JSONObject(jsonStr).optJSONObject("body");
                JSONArray listArray = bodyObject.optJSONArray("list");
                List<MsgUserListModel> list = new ArrayList();
                if (listArray != null) {
                    for (int i = 0; i < listArray.length(); i++) {
                        MsgUserListModel msgUserModel = new MsgUserListModel();
                        JSONObject userObject = listArray.getJSONObject(i);
                        msgUserModel.setPlid(userObject.optLong("plid"));
                        msgUserModel.setPmid(userObject.optLong("pmid"));
                        msgUserModel.setLastUserId(userObject.optLong(MsgConstant.LAST_USER_ID));
                        msgUserModel.setLastUserName(userObject.optString(MsgConstant.LAST_USER_NAME));
                        msgUserModel.setLastSummary(userObject.optString(MsgConstant.LAST_SUMMARY));
                        msgUserModel.setLastDateLine(userObject.optString(MsgConstant.LAST_DATE_LINE));
                        msgUserModel.setToUserId(userObject.optLong(MsgConstant.TO_USER_ID));
                        msgUserModel.setToUserAvatar(userObject.optString(MsgConstant.TO_USER_AVATAR));
                        msgUserModel.setToUserName(userObject.optString(MsgConstant.TO_USER_NAME));
                        msgUserModel.setToUserIsBlack(userObject.optInt(MsgConstant.TO_USER_ISBLACK));
                        if (!(cacheMap == null || cacheMap.get(Long.valueOf(msgUserModel.getToUserId())) == null)) {
                            msgUserModel.setUnReadCount(((MsgDBModel) cacheMap.get(Long.valueOf(msgUserModel.getToUserId()))).getUnReadCount());
                        }
                        list.add(msgUserModel);
                    }
                    baseResultModel.setData(list);
                }
                if (list.size() > 0) {
                    baseResultModel.setHasNext(bodyObject.optInt(BaseApiConstant.HAS_NEXT));
                    baseResultModel.setTotalNum(bodyObject.optInt("count"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<MsgUserListModel>> getSessionListByLocal(Context context, Map<Long, MsgDBModel> cacheMap) {
        BaseResultModel<List<MsgUserListModel>> baseResultModel = new BaseResultModel();
        if (cacheMap == null || cacheMap.isEmpty()) {
            return null;
        }
        List<MsgUserListModel> list = new ArrayList();
        for (Long key : cacheMap.keySet()) {
            MsgDBModel msgDBModel = (MsgDBModel) cacheMap.get(key);
            MsgUserListModel msgUserListModel = new MsgUserListModel();
            msgUserListModel.setPlid(msgDBModel.getPlid());
            msgUserListModel.setPmid(msgDBModel.getPmid());
            if (msgDBModel.getSource() == 0) {
                SharedPreferencesDB sDb = SharedPreferencesDB.getInstance(context.getApplicationContext());
                msgUserListModel.setLastUserId(sDb.getUserId());
                msgUserListModel.setLastUserName(sDb.getNickName());
            } else {
                msgUserListModel.setLastUserId(msgDBModel.getFromUid());
                msgUserListModel.setLastUserName(msgDBModel.getName());
            }
            msgUserListModel.setLastSummary(msgDBModel.getContent());
            msgUserListModel.setLastDateLine(String.valueOf(msgDBModel.getTime()));
            msgUserListModel.setToUserId(msgDBModel.getFromUid());
            msgUserListModel.setToUserAvatar(msgDBModel.getIcon());
            msgUserListModel.setToUserName(msgDBModel.getName());
            msgUserListModel.setLastSummaryType(msgDBModel.getType());
            list.add(msgUserListModel);
        }
        baseResultModel.setData(list);
        return baseResultModel;
    }

    public static BaseResultModel<List<CommentAtModel>> getCommentAtList(Context context, String jsonStr) {
        BaseResultModel<List<CommentAtModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray listArray = jsonObject.optJSONArray("list");
                List<CommentAtModel> list = new ArrayList();
                if (listArray != null) {
                    for (int i = 0; i < listArray.length(); i++) {
                        CommentAtModel commentAtModel = new CommentAtModel();
                        JSONObject commentAtObject = listArray.getJSONObject(i);
                        commentAtModel.setBoardName(commentAtObject.getString("board_name"));
                        commentAtModel.setBoardId(commentAtObject.getLong("board_id"));
                        commentAtModel.setTopicId(commentAtObject.getLong("topic_id"));
                        commentAtModel.setTopicSubject(commentAtObject.getString(MsgConstant.TOPIC_SUBJECT));
                        commentAtModel.setTopicContent(commentAtObject.getString(MsgConstant.TOPIC_CONTENT));
                        commentAtModel.setTopicUrl(commentAtObject.getString(MsgConstant.TOPIC_URL));
                        commentAtModel.setReplyContent(commentAtObject.getString("reply_content"));
                        commentAtModel.setReplyUrl(commentAtObject.getString(MsgConstant.REPLY_URL));
                        commentAtModel.setReplyRemindId(commentAtObject.getLong(MsgConstant.REPLY_REMIND_ID));
                        commentAtModel.setReplyUserName(commentAtObject.getString(MsgConstant.REPLY_NICK_NAME));
                        commentAtModel.setUserId(commentAtObject.getLong("user_id"));
                        commentAtModel.setIconUrl(commentAtObject.getString("icon"));
                        commentAtModel.setIsRead(commentAtObject.getInt(MsgConstant.IS_READ));
                        commentAtModel.setTime(commentAtObject.getString(MsgConstant.REPLIED_DATE));
                        list.add(commentAtModel);
                    }
                    baseResultModel.setData(list);
                    if (list.size() > 0) {
                        baseResultModel.setHasNext(jsonObject.optInt(BaseApiConstant.HAS_NEXT));
                        baseResultModel.setTotalNum(jsonObject.optInt("count"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<FriendModel>> getFriendList(Context context, String jsonStr) {
        BaseResultModel<List<FriendModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                List<FriendModel> friendList = new ArrayList();
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray dataArray = jsonObject.optJSONObject("body").getJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject object = dataArray.optJSONObject(i);
                        FriendModel friendModel = new FriendModel();
                        friendModel.setDateLine(object.optString("dateline"));
                        friendModel.setType(object.optString("type"));
                        friendModel.setNote(object.optString(MsgConstant.NOTE));
                        friendModel.setFromId(object.optLong(MsgConstant.FROM_ID));
                        friendModel.setFromIdType(object.optString(MsgConstant.FROM_ID_TYPE));
                        friendModel.setAuthor(object.optString("author"));
                        friendModel.setAuthorId(object.optLong("authorId"));
                        friendModel.setAuthorAvatar(object.optString(MsgConstant.AUTHOR_AVATAR));
                        List<OtherPanelModel> actions = new ArrayList();
                        JSONArray actionsArray = object.optJSONArray(MsgConstant.ACTIONS);
                        if (actionsArray != null) {
                            for (int j = 0; j < actionsArray.length(); j++) {
                                JSONObject actionObject = actionsArray.optJSONObject(j);
                                OtherPanelModel model = new OtherPanelModel();
                                model.setTitle(actionObject.optString("title"));
                                model.setType(actionObject.optString("type"));
                                model.setAction(actionObject.optString("redirect"));
                                actions.add(model);
                            }
                            friendModel.setActions(actions);
                        }
                        friendList.add(friendModel);
                    }
                    baseResultModel.setData(friendList);
                    baseResultModel.setHasNext(jsonObject.optInt("has_next"));
                    baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static String createPmJson(List<MsgDBModel> tempList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject bodyObject = new JSONObject();
            JSONArray pmInfosArray = new JSONArray();
            for (int i = 0; i < tempList.size(); i++) {
                JSONObject object = new JSONObject();
                MsgDBModel model = (MsgDBModel) tempList.get(i);
                object.put("fromUid", model.getFromUid());
                object.put(MsgConstant.START_TIME, String.valueOf(model.getStartTime()));
                object.put(MsgConstant.STOP_TIME, String.valueOf(model.getStopTime()));
                object.put(MsgConstant.CACHE_COUNT, model.getCacheCount());
                object.put(MsgConstant.PM_LIMIT, model.getPmLimit());
                object.put("plid", model.getPlid());
                object.put("pmid", model.getPmid());
                pmInfosArray.put(object);
            }
            bodyObject.put("pmInfos", pmInfosArray);
            JSONObject externInfoObject = new JSONObject();
            externInfoObject.put(MsgConstant.ONLY_FROM_UID, 0);
            bodyObject.put("externInfo", externInfoObject);
            jsonObject.put("body", bodyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<MsgModel> getPmList(Context context, String jsonStr) {
        BaseResultModel<MsgModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                MsgModel msgModel = new MsgModel();
                JSONObject bodyObject = new JSONObject(jsonStr).optJSONObject("body");
                JSONObject userInfoObject = bodyObject.optJSONObject(MsgConstant.USER_INFO);
                UserInfoModel userInfoModel = new UserInfoModel();
                userInfoModel.setUserId(userInfoObject.optLong("uid"));
                userInfoModel.setNickname(userInfoObject.optString("name"));
                userInfoModel.setIcon(userInfoObject.optString("avatar"));
                msgModel.setUserInfoModel(userInfoModel);
                JSONArray pmListArray = bodyObject.optJSONArray(MsgConstant.PM_LIST);
                if (pmListArray != null) {
                    List<MsgUserListModel> pmList = new ArrayList();
                    for (int i = 0; i < pmListArray.length(); i++) {
                        MsgUserListModel msgUserListModel = new MsgUserListModel();
                        JSONObject pmObject = pmListArray.optJSONObject(i);
                        msgUserListModel.setToUserId(pmObject.optLong("fromUid"));
                        msgUserListModel.setToUserName(pmObject.optString("name"));
                        msgUserListModel.setToUserAvatar(pmObject.optString("avatar"));
                        msgUserListModel.setPlid((long) pmObject.optInt("plid"));
                        msgUserListModel.setHasPrev(pmObject.optInt(MsgConstant.HAS_PREV));
                        JSONArray msgAarry = pmObject.optJSONArray(MsgConstant.MSG_LIST);
                        if (msgAarry != null) {
                            List<MsgContentModel> msgList = new ArrayList();
                            for (int j = 0; j < msgAarry.length(); j++) {
                                MsgContentModel msgContentModel = new MsgContentModel();
                                JSONObject msgObject = msgAarry.optJSONObject(j);
                                msgContentModel.setPmid(msgObject.optLong("mid"));
                                msgContentModel.setSender((long) msgObject.optInt(MsgConstant.SENDER));
                                msgContentModel.setType(msgObject.optString("type"));
                                msgContentModel.setContent(msgObject.optString("content"));
                                msgContentModel.setTime(Long.valueOf(msgObject.optLong("time")).longValue());
                                msgList.add(msgContentModel);
                            }
                            msgUserListModel.setMsgList(msgList);
                        }
                        pmList.add(msgUserListModel);
                    }
                    msgModel.setPmList(pmList);
                }
                baseResultModel.setData(msgModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static String getSendMsgJson(MsgContentModel msgContentModel, long userId, long fromUid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "send");
            jsonObject.put(MsgConstant.TO_UId, fromUid);
            jsonObject.put("plid", msgContentModel.getPlid());
            jsonObject.put("pmid", msgContentModel.getPmid());
            JSONObject msgObject = new JSONObject();
            msgObject.put("type", msgContentModel.getType());
            if ("text".equals(msgContentModel.getType())) {
                try {
                    msgObject.put("content", URLEncoder.encode(msgContentModel.getContent(), "utf-8").replaceAll("\\+", "%20"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                msgObject.put("content", msgContentModel.getContent());
            }
            jsonObject.put("msg", msgObject);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String json = jsonObject.toString();
        MCLogUtil.e("MsgServiceImplHelper", "json=" + json);
        return json;
    }

    public static BaseResultModel<MsgContentModel> sendMsg(Context context, MsgContentModel msgContentModel, String jsonStr) {
        BaseResultModel<MsgContentModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() == 0) {
                msgContentModel.setStatus(2);
                baseResultModel.setData(msgContentModel);
            } else {
                JSONObject bodyObject = new JSONObject(jsonStr).optJSONObject("body");
                msgContentModel.setPlid(bodyObject.optLong("plid", 0));
                msgContentModel.setPmid(bodyObject.optLong("pmid", 0));
                msgContentModel.setTime(bodyObject.optLong(MsgConstant.SEND_TIME, 0));
                msgContentModel.setStatus(0);
                baseResultModel.setData(msgContentModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }
}
