package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.MsgRestfulApiRequester;
import com.mobcent.discuz.android.constant.MsgConstant;
import com.mobcent.discuz.android.db.MsgDBUtil;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.FriendModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.helper.BaseJsonHelper;
import com.mobcent.discuz.android.service.impl.helper.MsgServiceImplHelper;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class MsgServiceImpl implements MsgService, MsgConstant {
    private final String TAG = "MsgServiceImpl";
    private Context context;

    public MsgServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<List<CommentAtModel>> getCommentAtList(String type, int page, int pageSize, boolean isLocal) {
        String jsonStr = "";
        final long userId = SharedPreferencesDB.getInstance(this.context.getApplicationContext()).getUserId();
        if (isLocal) {
            jsonStr = MsgDBUtil.getInstance(this.context.getApplicationContext()).getCommentAtJson(userId, type);
        }
        if (!isLocal || MCStringUtil.isEmpty(jsonStr)) {
            isLocal = false;
            jsonStr = MsgRestfulApiRequester.getCommentAtList(this.context, type, page, pageSize);
        }
        BaseResultModel<List<CommentAtModel>> baseResultModel = MsgServiceImplHelper.getCommentAtList(this.context, jsonStr);
        if (baseResultModel.getRs() == 1) {
            if ("at".equals(type)) {
                ObserverHelper.getInstance().getActivityObservable().updateMsgNum(this.context.getApplicationContext(), 1, 0, userId, 0);
            } else if ("post".equals(type)) {
                ObserverHelper.getInstance().getActivityObservable().updateMsgNum(this.context.getApplicationContext(), 2, 0, userId, 0);
            }
            final String saveStr = jsonStr;
            if (!isLocal && page == 1) {
                final String str = type;
                new Thread() {
                    public void run() {
                        MsgDBUtil.getInstance(MsgServiceImpl.this.context.getApplicationContext()).saveCommentAtJson(userId, str, saveStr);
                    }
                }.start();
            }
        }
        return baseResultModel;
    }

    public BaseResultModel<List<MsgUserListModel>> getSessionList(int page, int pageSize, boolean isLocal) {
        MsgDBUtil msgDBUtil = MsgDBUtil.getInstance(this.context.getApplicationContext());
        long userId = SharedPreferencesDB.getInstance(this.context.getApplicationContext()).getUserId();
        String jsonStr = "";
        if (isLocal) {
            jsonStr = msgDBUtil.getSessionJson(userId);
        }
        if (!isLocal || MCStringUtil.isEmpty(jsonStr)) {
            isLocal = false;
            jsonStr = MsgRestfulApiRequester.getMsgUserList(this.context, MsgServiceImplHelper.getMsgUserListJson(page, pageSize));
        }
        BaseResultModel<List<MsgUserListModel>> baseResultModel = MsgServiceImplHelper.getSessionList(this.context, jsonStr, msgDBUtil.getCacheSessionList(userId));
        if (baseResultModel.getRs() == 1 && !isLocal) {
            msgDBUtil.saveSessionJson(userId, jsonStr);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<FriendModel>> getFriendList(String type, int page, int pageSize) {
        long userId = SharedPreferencesDB.getInstance(this.context.getApplicationContext()).getUserId();
        BaseResultModel<List<FriendModel>> baseResultModel = MsgServiceImplHelper.getFriendList(this.context, MsgRestfulApiRequester.getCommentAtList(this.context, type, page, pageSize));
        if (baseResultModel.getRs() == 1 && "friend".equals(type)) {
            ObserverHelper.getInstance().getActivityObservable().updateMsgNum(this.context.getApplicationContext(), 4, 0, userId, 0);
        }
        return baseResultModel;
    }

    public BaseResultModel<MsgModel> getPmList(List<MsgDBModel> tempList) {
        return MsgServiceImplHelper.getPmList(this.context, MsgRestfulApiRequester.getPmList(this.context, MsgServiceImplHelper.createPmJson(tempList)));
    }

    public List<MsgDBModel> getChatListFromDB(long userId, long fromUid, long plid, long pmid, long time, int length, int type) {
        List<MsgDBModel> chatList = new ArrayList();
        MsgDBUtil msgDBUtil = MsgDBUtil.getInstance(this.context.getApplicationContext());
        if (!msgDBUtil.createTable(userId)) {
            return chatList;
        }
        chatList = msgDBUtil.getChatList(userId, fromUid, time, length, type);
        if (!MCListUtils.isEmpty((List) chatList)) {
            ObserverHelper.getInstance().getActivityObservable().updateMsgNum(this.context, 3, 0, userId, fromUid);
        }
        if (type == 2) {
            return chatList;
        }
        if (chatList == null || chatList.isEmpty()) {
            BaseResultModel<MsgModel> baseResultModel = MsgServiceImplHelper.getPmList(this.context, MsgRestfulApiRequester.getPmList(this.context, getHistoryMessageJsonStr(fromUid, userId, plid, pmid, time, length)));
            if (!(baseResultModel == null || baseResultModel.getRs() != 1 || baseResultModel.getData() == null || ((MsgModel) baseResultModel.getData()).getUserInfoModel() == null || ((MsgModel) baseResultModel.getData()).getPmList() == null || ((MsgModel) baseResultModel.getData()).getPmList().isEmpty() || ((MsgModel) baseResultModel.getData()).getPmList().get(0) == null || !msgDBUtil.saveMsg(((MsgModel) baseResultModel.getData()).getUserInfoModel(), ((MsgModel) baseResultModel.getData()).getPmList()))) {
                chatList = msgDBUtil.getChatList(userId, fromUid, time, length, type);
            }
        }
        return chatList;
    }

    private String getHistoryMessageJsonStr(long fromUid, long userId, long plid, long pmid, long time, int pmlimit) {
        List<MsgDBModel> createInfos = new ArrayList();
        MsgDBModel heartInfoModel = new MsgDBModel();
        heartInfoModel.setFromUid(fromUid);
        heartInfoModel.setStartTime(0);
        heartInfoModel.setStopTime(time);
        heartInfoModel.setPlid(plid);
        heartInfoModel.setPmid(pmid);
        List<Long> fromUidList = new ArrayList();
        fromUidList.add(Long.valueOf(fromUid));
        Map<Long, MsgDBModel> timeAndCountMap = MsgDBUtil.getInstance(this.context.getApplicationContext()).getLastTimeAndCacheCount(userId, fromUidList);
        int cacheCount = 0;
        if (!(timeAndCountMap == null || timeAndCountMap.get(Long.valueOf(fromUid)) == null)) {
            cacheCount = ((MsgDBModel) timeAndCountMap.get(Long.valueOf(fromUid))).getCacheCount();
        }
        heartInfoModel.setCacheCount(cacheCount);
        heartInfoModel.setPmLimit(pmlimit);
        createInfos.add(heartInfoModel);
        return MsgServiceImplHelper.createPmJson(createInfos);
    }

    public boolean modifyMsgStatus(long userId, long fromUid) {
        return MsgDBUtil.getInstance(this.context.getApplicationContext()).modifyMsgStatus(userId, fromUid);
    }

    public BaseResultModel<Object> sendMsg(MsgContentModel msgContentModel, long userId, long fromUid, String fromName, boolean isAgain) {
        boolean saveSucc;
        BaseResultModel<Object> baseResultModel = new BaseResultModel();
        baseResultModel.setRs(0);
        boolean uploadSucc = true;
        long tempTime = msgContentModel.getTime();
        MsgDBUtil msgDBUtil = MsgDBUtil.getInstance(this.context.getApplicationContext());
        if (isAgain) {
            saveSucc = true;
        } else {
            saveSucc = msgDBUtil.saveSendMsg(msgContentModel, userId, fromUid, fromName, SharedPreferencesDB.getInstance(this.context.getApplicationContext()).getIcon());
        }
        if (!saveSucc) {
            baseResultModel.setErrorInfo(MCResource.getInstance(this.context.getApplicationContext()).getString("mc_forum_save_message_fail"));
            uploadSucc = false;
        }
        if (uploadSucc) {
            if (!isAgain) {
                ObserverHelper.getInstance().getActivityObservable().showChatRoomData(fromUid);
            }
            if (!msgContentModel.getContent().contains("http://")) {
                PostsService postsService = new PostsServiceImpl(this.context.getApplicationContext());
                BaseResultModel<List<UploadPictureModel>> resultModel;
                if ("audio".equals(msgContentModel.getType())) {
                    resultModel = postsService.upload(new String[]{msgContentModel.getContent()}, "pm", "audio", 0, 0, 0);
                    if (resultModel == null || resultModel.getRs() != 1 || resultModel.getData() == null || ((List) resultModel.getData()).isEmpty() || ((List) resultModel.getData()).get(0) == null) {
                        baseResultModel.setErrorInfo(resultModel.getErrorInfo());
                        uploadSucc = false;
                    } else {
                        msgContentModel.setContent(((UploadPictureModel) ((List) resultModel.getData()).get(0)).getPicPath());
                    }
                } else if ("image".equals(msgContentModel.getType())) {
                    try {
                        String[] files = new String[1];
                        files[0] = msgContentModel.getContent().replace(" ", "");
                        resultModel = postsService.upload(files, "pm", "image", 0, 0, 0);
                        if (resultModel == null || resultModel.getRs() != 1 || resultModel.getData() == null || ((List) resultModel.getData()).isEmpty() || ((List) resultModel.getData()).get(0) == null) {
                            baseResultModel.setErrorInfo(resultModel.getErrorInfo());
                            uploadSucc = false;
                        } else {
                            msgContentModel.setContent(((UploadPictureModel) ((List) resultModel.getData()).get(0)).getPicPath());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        uploadSucc = false;
                    }
                }
            }
        }
        if (uploadSucc) {
            BaseResultModel<MsgContentModel> msgContentResultModel = MsgServiceImplHelper.sendMsg(this.context, msgContentModel, MsgRestfulApiRequester.sendMsg(this.context, MsgServiceImplHelper.getSendMsgJson(msgContentModel, userId, fromUid)));
            if (msgContentResultModel.getRs() != 1) {
                baseResultModel.setErrorInfo(msgContentResultModel.getErrorInfo());
                uploadSucc = false;
            } else {
                baseResultModel.setRs(1);
                ObserverHelper.getInstance().getActivityObservable().onRefreshMessageList();
            }
        }
        if (!uploadSucc) {
            msgContentModel.setStatus(2);
        }
        msgDBUtil.modifyMsgAfterSend(tempTime, msgContentModel, userId);
        ObserverHelper.getInstance().getActivityObservable().modifyChatRoomUI(tempTime, msgContentModel);
        return baseResultModel;
    }

    public void deleteUserMsg(long userId, long plid, long toUserId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "delplid");
            jsonObject.put("plid", plid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = MsgRestfulApiRequester.sendMsg(this.context, jsonObject.toString());
        BaseResultModel<MsgContentModel> baseResultModel = new BaseResultModel();
        BaseJsonHelper.formJsonRs(this.context, jsonStr, baseResultModel);
        if (baseResultModel.getRs() == 1) {
            MsgDBUtil.getInstance(this.context.getApplicationContext()).deleteUserMsg(userId, toUserId);
        }
    }
}
