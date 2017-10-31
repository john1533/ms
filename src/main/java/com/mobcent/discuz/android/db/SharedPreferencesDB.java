package com.mobcent.discuz.android.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.android.db.constant.SharedPreferencesDBConstant;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.lowest.base.utils.MCStringUtil;

public class SharedPreferencesDB implements SharedPreferencesDBConstant {
    private static SharedPreferencesDB sharedPreferencesDB = null;
    private Context context;
    private SharedPreferences prefs = null;

    private SharedPreferencesDB(Context cxt) {
        this.context = cxt;
        try {
            this.prefs = cxt.createPackageContext(cxt.getPackageName(), 2).getSharedPreferences(PREFS_FILE, 3);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SharedPreferencesDB getInstance(Context context) {
//        SharedPreferencesDB sharedPreferencesDB;
        synchronized (SharedPreferencesDB.class) {
            if (sharedPreferencesDB == null) {
                sharedPreferencesDB = new SharedPreferencesDB(context.getApplicationContext());
            }
            sharedPreferencesDB = sharedPreferencesDB;
        }
        return sharedPreferencesDB;
    }

    public String getForumKey() {
        String forumKey = this.prefs.getString("mc_forum_key", null);
        if (forumKey != null && !MCStringUtil.isEmpty(forumKey)) {
            return forumKey;
        }
        try {
            forumKey = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128).metaData.getString("mc_forum_key");
            this.prefs.edit().putString("mc_forum_key", forumKey).commit();
            return forumKey;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public long getUserId() {
        return this.prefs.getLong("userId", 0);
    }

    public void setAtMeModel(long time, int count, long userId) {
        Editor editor = this.prefs.edit();
        editor.putLong(new StringBuilder(ATME_TIME).append(userId).toString(), time);
        editor.putInt(new StringBuilder(ATME_COUNT).append(userId).toString(), count);
        editor.commit();
    }

    public void setLastPushId(long lastPushMsgId) {
        Editor editor = this.prefs.edit();
        editor.putLong(PUSH_MSG_ID, lastPushMsgId);
        editor.commit();
    }

    public void setFriendModel(long time, int count, long userId) {
        Editor editor = this.prefs.edit();
        editor.putLong(new StringBuilder(FRIEND_TIME).append(userId).toString(), time);
        editor.putInt(new StringBuilder(FRIEND_COUNT).append(userId).toString(), count);
        editor.commit();
    }

    public long getFriendTime(long userId) {
        return this.prefs.getLong(new StringBuilder(FRIEND_TIME).append(userId).toString(), 0);
    }

    public int getFriendCount(long userId) {
        return this.prefs.getInt(new StringBuilder(FRIEND_COUNT).append(userId).toString(), 0);
    }

    public long getLastPushId() {
        return this.prefs.getLong(PUSH_MSG_ID, 0);
    }

    public long getAtMeTime(long userId) {
        return this.prefs.getLong(new StringBuilder(ATME_TIME).append(userId).toString(), 0);
    }

    public int getAtMeCount(long userId) {
        return this.prefs.getInt(new StringBuilder(ATME_COUNT).append(userId).toString(), 0);
    }

    public void setReplyModel(long time, int count, long userId) {
        Editor editor = this.prefs.edit();
        editor.putLong(new StringBuilder(REPLY_TIME).append(userId).toString(), time);
        editor.putInt(new StringBuilder(REPLY_COUNT).append(userId).toString(), count);
        editor.commit();
    }

    public long getReplyTime(long userId) {
        return this.prefs.getLong(new StringBuilder(REPLY_TIME).append(userId).toString(), 0);
    }

    public int getReplyCount(long userId) {
        return this.prefs.getInt(new StringBuilder(REPLY_COUNT).append(userId).toString(), 0);
    }

    public boolean isLight(){
        return this.prefs.getBoolean(LIGHT, true);
    }

    public void setLight(boolean isLight) {
        Editor editor = this.prefs.edit();
        editor.putBoolean(LIGHT, isLight);
        editor.commit();
    }

    public void setUserId(long userId) {
        Editor editor = this.prefs.edit();
        editor.putLong("userId", userId);
        editor.commit();
    }

    public void updateTokenAndSecret(String accessToken, String accessSecret) {
        Editor editor = this.prefs.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("accessSecret", accessSecret);
        editor.commit();
    }

    public String getForumType() {
        try {
            return this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128).metaData.getString(MC_MOBCENT_TYPE);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public void setIcon(String icon) {
        Editor editor = this.prefs.edit();
        editor.putString(ICON_URL, icon);
        editor.commit();
    }

    public String getIcon() {
        return this.prefs.getString(ICON_URL, "");
    }

    public void setNickName(String nickname) {
        Editor editor = this.prefs.edit();
        editor.putString("nickname", nickname);
        editor.commit();
    }

    public String getNickName() {
        return this.prefs.getString("nickname", "");
    }

    public void setRefresh(boolean isfresh) {
        Editor editor = this.prefs.edit();
        editor.putBoolean(ISFRESH_TOPIC, isfresh);
        editor.commit();
    }

    public boolean isRefresh() {
        return this.prefs.getBoolean(ISFRESH_TOPIC, false);
    }

    public String getdiscusVersion() {
        try {
            return new StringBuilder(String.valueOf(this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128).metaData.getFloat(SharedPreferencesDBConstant.MC_DISCUZ_VERSION))).toString();
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public String getAccessToken() {
        return this.prefs.getString("accessToken", null);
    }

    public String getAccessSecret() {
        return this.prefs.getString("accessSecret", null);
    }

    public void setSettingModel(SettingModel model) {
        Editor editor = this.prefs.edit();
        editor.putInt(QQ_CONNECT, model.getQqConnect());
        editor.putInt(WX_CONNECT, model.getWxConnect());
        editor.putInt(PLUG_CHECK, model.getPlugCheck());
        editor.putLong(SEVER_TIME_INTERVAL, Long.valueOf(model.getServerTime()).longValue() - System.currentTimeMillis());
        editor.commit();
    }

    public void saveSettingStr(String jsonStr) {
        Editor editor = this.prefs.edit();
        editor.putString(SETTING_STR, jsonStr);
        editor.commit();
    }

    public String getSettingStr() {
        return this.prefs.getString(SETTING_STR, "");
    }

    public int getQQConnect() {
        return this.prefs.getInt(QQ_CONNECT, 0);
    }

    public int getWXConnect() {
        return this.prefs.getInt(WX_CONNECT, 0);
    }

    public int getPlugCheck() {
        return this.prefs.getInt(PLUG_CHECK, 0);
    }

    public long getServerTimeInterval() {
        return this.prefs.getLong(SEVER_TIME_INTERVAL, 0);
    }

    public void removeUserInfo() {
        Editor editor = this.prefs.edit();
        if (this.prefs.getLong("userId", 0) != 0) {
            editor.remove("userId");
        }
        if (this.prefs.getString("accessToken", null) != null) {
            editor.remove("accessToken");
        }
        if (this.prefs.getString("accessSecret", null) != null) {
            editor.remove("accessSecret");
        }
        if (!"".equals(this.prefs.getString("nickname", ""))) {
            editor.remove("nickname");
        }
        if (!"".equals(this.prefs.getString(ICON_URL, ""))) {
            editor.remove(ICON_URL);
        }
        editor.commit();
    }

    public void setConfig(String json) {
        Editor editor = this.prefs.edit();
        editor.putString(CONFIG, json);
        editor.commit();
    }

    public String getConfig() {
        return this.prefs.getString(CONFIG, null);
    }

    public void setPayState(PayStateModel payStateModel) {
        if (payStateModel != null) {
            Editor editor = this.prefs.edit();
            editor.putBoolean(IS_PAY_STATE_EXIST, true);
            editor.putString("loadingpage_image", payStateModel.getLoadingPageImage());
            editor.putBoolean("adv", payStateModel.getAdv());
            editor.putBoolean("client_manager", payStateModel.getClientManager());
            editor.putBoolean("loadingpage", payStateModel.getLoadingPage());
            editor.putBoolean("msg_push", payStateModel.getMsgPush());
            editor.putBoolean("powerby", payStateModel.getPowerBy());
            editor.putBoolean("share_key", payStateModel.getShareKey());
            editor.putBoolean("square", payStateModel.getSquare());
            editor.putBoolean("watermark", payStateModel.getWaterMark());
            editor.putString("watermark_image", payStateModel.getWaterMarkImage());
            editor.putString("img_url", payStateModel.getImgUrl());
            editor.putString("weixin_appkey", payStateModel.getWeiXinAppKey());
            editor.putString("weixin_appsecret", payStateModel.getWeixinAppSecret());
            editor.putBoolean(IS_USER_DEFINED, payStateModel.isUserDefined());
            editor.commit();
        }
    }

    public PayStateModel getPayState() {
        PayStateModel payStateModel = new PayStateModel();
        payStateModel.setAdv(this.prefs.getBoolean("adv", false));
        payStateModel.setClientManager(this.prefs.getBoolean("client_manager", false));
        payStateModel.setImgUrl(this.prefs.getString("img_url", ""));
        payStateModel.setLoadingPage(this.prefs.getBoolean("loadingpage", false));
        payStateModel.setLoadingPageImage(this.prefs.getString("loadingpage_image", ""));
        payStateModel.setMsgPush(this.prefs.getBoolean("msg_push", false));
        payStateModel.setPowerBy(this.prefs.getBoolean("powerby", false));
        payStateModel.setShareKey(this.prefs.getBoolean("share_key", false));
        payStateModel.setSquare(this.prefs.getBoolean("square", false));
        payStateModel.setWaterMark(this.prefs.getBoolean("watermark", false));
        payStateModel.setWaterMarkImage(this.prefs.getString("watermark_image", ""));
        payStateModel.setWeiXinAppKey(this.prefs.getString("weixin_appkey", ""));
        payStateModel.setWeixinAppSecret(this.prefs.getString("weixin_appsecret", ""));
        payStateModel.setUserDefined(this.prefs.getBoolean(SharedPreferencesDBConstant.IS_USER_DEFINED, false));
        return payStateModel;
    }

    public boolean isPayStateExist() {
        return this.prefs.getBoolean(SharedPreferencesDBConstant.IS_PAY_STATE_EXIST, false);
    }

    public void setShortCutFlag(boolean isSetted) {
        Editor editor = this.prefs.edit();
        editor.putBoolean(SharedPreferencesDBConstant.ADD_SHORT_CUT_FLAG, isSetted);
        editor.commit();
    }

    public boolean getShortCutFlag() {
        return this.prefs.getBoolean(SharedPreferencesDBConstant.ADD_SHORT_CUT_FLAG, false);
    }

    public void saveJsonByKey(String key, String json) {
        Editor editor = this.prefs.edit();
        editor.putString(key, json);
        editor.commit();
    }

    public String queryJsonByKey(String key) {
        return this.prefs.getString(key, "");
    }

    public void saveLocation(BDLocation location) {
        Editor editor = this.prefs.edit();
        editor.putFloat(LONGITUDE, (float) location.getLongitude());
        editor.putFloat(LATITUDE, (float) location.getLatitude());
        editor.putString(SharedPreferencesDBConstant.LOCATION_STR, location.getAddrStr());
        editor.commit();
    }

    public BDLocation getLocation() {
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLongitude((double) this.prefs.getFloat(LONGITUDE, 0.0f));
        bdLocation.setLatitude((double) this.prefs.getFloat(LATITUDE, 0.0f));
        bdLocation.setAddrStr(this.prefs.getString(SharedPreferencesDBConstant.LOCATION_STR, ""));
        return bdLocation;
    }

    public void saveInt(String key, int value) {
        Editor editor = this.prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }

    public void saveLong(String key, long value) {
        Editor editor = this.prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return this.prefs.getLong(key, 0);
    }

    public void saveString(String key, String value) {
        Editor editor = this.prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return this.prefs.getString(key, null);
    }
}
