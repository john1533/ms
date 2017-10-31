package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.SettingRestfulApiRequester;
import com.mobcent.discuz.android.api.UploadFileRestfulApiRequester;
import com.mobcent.discuz.android.api.UserRestfulApiRequester;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.db.MentionFriendDBUtil;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.db.UserDBUtil;
import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.helper.BaseJsonHelper;
import com.mobcent.discuz.android.service.impl.helper.PostsServiceImplHelper;
import com.mobcent.discuz.android.service.impl.helper.UserServiceImplHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserServiceImpl extends BaseServiceImpl implements UserService, UserConstant {
    public String TAG = "UserServiceImpl";

    public UserServiceImpl(Context context) {
        super(context);
    }

    public BaseResultModel<List<UserInfoModel>> getMentionFriendByNet(long userId) {
        String jsonStr = UserRestfulApiRequester.getMentionFriend(this.context);
        BaseResultModel<List<UserInfoModel>> mentionFriendList = UserServiceImplHelper.parseMentionFriendListJson(jsonStr);
        long lastUpdateTime = Calendar.getInstance().getTimeInMillis();
        long user = userId;
        if (mentionFriendList.getRs() == 1) {
            MentionFriendDBUtil.getInstance(this.context).updateMentionFriendJsonString(jsonStr, 0, user, lastUpdateTime);
        }
        return mentionFriendList;
    }

    public BaseResultModel<List<UserInfoModel>> getMentionFriend(long userId, boolean isLocal) {
        if (!isLocal) {
            return getMentionFriendByNet(userId);
        }
        String jsonStr;
        try {
            jsonStr = MentionFriendDBUtil.getInstance(this.context).getMentionFriendJsonString(userId);
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = null;
        }
        if (MCStringUtil.isEmpty(jsonStr)) {
            return getMentionFriendByNet(userId);
        }
        return UserServiceImplHelper.parseMentionFriendListJson(jsonStr);
    }

    public boolean addLocalForumMentionFriend(long userId, UserInfoModel userInfo) {
        try {
            JSONArray array = new JSONObject(MentionFriendDBUtil.getInstance(this.context).getMentionFriendJsonString(userId)).getJSONArray("list");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", userInfo.getNickname());
            jsonObject.put("uid", userInfo.getUserId());
            jsonObject.put("role_num", 2);
            array.put(jsonObject);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("list", array);
            MentionFriendDBUtil.getInstance(this.context).updateMentionFriendJsonString(jsonObject2.toString(), 0, userId, Calendar.getInstance().getTimeInMillis());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deletedLocalForumMentionFriend(long userId, UserInfoModel userInfo) {
        try {
            JSONArray array = new JSONObject(MentionFriendDBUtil.getInstance(this.context).getMentionFriendJsonString(userId)).getJSONArray("list");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                if (Long.valueOf(jsonObject.optLong("uid")).longValue() != userInfo.getUserId()) {
                    jsonArray.put(jsonObject);
                }
            }
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("list", jsonArray);
            MentionFriendDBUtil.getInstance(this.context).updateMentionFriendJsonString(jsonObject2.toString(), 0, userId, Calendar.getInstance().getTimeInMillis());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BaseResultModel<UserInfoModel> getUserInfo(long userId) {
        BaseResultModel<UserInfoModel> baseResultUserInfoModel = UserServiceImplHelper.parseGetUserInfoJson(this.context, UserRestfulApiRequester.getUserInfo(this.context, userId));
        if (baseResultUserInfoModel.getRs() == 1 && baseResultUserInfoModel.getData() != null) {
            ((UserInfoModel) baseResultUserInfoModel.getData()).setUserId(userId);
            UserManageHelper.getInstance(this.context.getApplicationContext()).notifyUserInfo((UserInfoModel) baseResultUserInfoModel.getData());
        }
        return baseResultUserInfoModel;
    }

    public boolean isLogin() {
        String accessToken = SharedPreferencesDB.getInstance(this.context).getAccessToken();
        String accessSecret = SharedPreferencesDB.getInstance(this.context).getAccessSecret();
        if (MCStringUtil.isEmpty(accessToken) || MCStringUtil.isEmpty(accessSecret)) {
            return false;
        }
        return true;
    }

    public String getPermissionStr(boolean isLocal, long userId) {
        if (isLocal && userId != 0) {
            UserInfoModel userInfoModel = UserDBUtil.getInstance(this.context).getCurrUser(userId);
            if (!(userInfoModel == null || MCStringUtil.isEmpty(userInfoModel.getPermission()))) {
                return userInfoModel.getPermission();
            }
        }
        return UserServiceImplHelper.getPostInfo(this.context, SettingRestfulApiRequester.getSettingContent(this.context, false));
    }

    public BaseResultModel<PermissionModel> getPermissionModel(String jsonStr) {
        return UserServiceImplHelper.getPermissionContent(this.context, jsonStr);
    }

    public UserInfoModel getLastUserInfo() {
        return UserDBUtil.getInstance(this.context).getLastUserInfo();
    }

    public List<UserInfoModel> getAllUserInfo() {
        return UserDBUtil.getInstance(this.context).getAllUserInfo();
    }

    public BaseResultModel<UserInfoModel> getUser() {
        return UserServiceImplHelper.parseGetUserInfoJson(this.context, UserRestfulApiRequester.getUser(this.context));
    }

    public BaseResultModel<Object> updateUser(String icon, int gender, String type, boolean isModifyIcon) {
        String avatar = icon;
        if (isModifyIcon) {
            UserManageHelper.getInstance(this.context.getApplicationContext()).deleteIconBitmap(SharedPreferencesDB.getInstance(this.context.getApplicationContext()).getIcon());
        } else {
            avatar = "";
        }
        BaseResultModel<Object> baseResultModel = UserServiceImplHelper.getManageUser(this.context, UserRestfulApiRequester.getUpdateUserInfo(this.context, type, String.valueOf(gender), avatar, "", ""));
        if (baseResultModel.getRs() == 1) {
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setIcon(icon);
            userInfoModel.setGender(gender);
            userInfoModel.setUserId(SharedPreferencesDB.getInstance(this.context).getUserId());
            UserManageHelper.getInstance(this.context.getApplicationContext()).updateUserInfo(userInfoModel);
        }
        return baseResultModel;
    }

    public BaseResultModel<UploadPictureModel> uploadIcon(String uploadFile, long userId) {
        String jsonStr = UploadFileRestfulApiRequester.uploadIcon(this.context, uploadFile);
        BaseResultModel<UploadPictureModel> baseResultModel = PostsServiceImplHelper.parseUploadImageJson(this.context, jsonStr);
        if (baseResultModel != null) {
            return baseResultModel;
        }
        baseResultModel = new BaseResultModel();
        baseResultModel.setErrorCode("#" + BaseJsonHelper.formJsonRs(jsonStr, this.context));
        return baseResultModel;
    }

    public BaseResultModel<UserInfoModel> loginUser(String userName, String password, String type) {
        BaseResultModel<UserInfoModel> baseResultModel = UserServiceImplHelper.getLoginUser(this.context, UserRestfulApiRequester.loginUser(this.context, userName, password, type));
        if (baseResultModel.getRs() == 1) {
            UserInfoModel loginUser = (UserInfoModel) baseResultModel.getData();
            if (userName.equals(UserConstant.SYSTEM_USER_PASSWORD)) {
                loginUser.setPwd(UserConstant.SYSTEM_USER_PASSWORD);
                loginUser.setNickname(loginUser.getNickname());
            } else {
                loginUser.setPwd(password);
                loginUser.setNickname(userName);
            }
            if (!UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo(loginUser, true)) {
                baseResultModel.setRs(0);
                baseResultModel.setErrorInfo("");
                UserManageHelper.getInstance(this.context.getApplicationContext()).logout();
            }
        }
        return baseResultModel;
    }

    public BaseResultModel<UserInfoModel> switchUser(String userName) {
        BaseResultModel<UserInfoModel> baseResultUserInfoModel = UserServiceImplHelper.parseLoginUserJson(this.context, UserRestfulApiRequester.switchUser(this.context, userName));
        if (baseResultUserInfoModel.getRs() == 1) {
            if (!UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo((UserInfoModel) baseResultUserInfoModel.getData(), true)) {
                baseResultUserInfoModel.setRs(0);
                baseResultUserInfoModel.setErrorInfo("");
                UserManageHelper.getInstance(this.context.getApplicationContext()).logout();
            }
        }
        return baseResultUserInfoModel;
    }

    public BaseResultModel<Object> logoutUser(String logInOrOut) {
        BaseResultModel<Object> baseResultModel = UserServiceImplHelper.getManageUser(this.context, UserRestfulApiRequester.loginUser(this.context, "", "", "logout"));
        if (baseResultModel.getRs() == 1) {
            UserManageHelper.getInstance(this.context).logout();
        }
        return baseResultModel;
    }

    public BaseResultModel<Object> manageUser(long userId, String type) {
        return UserServiceImplHelper.getManageUser(this.context, UserRestfulApiRequester.getManageUser(this.context, userId, type));
    }

    public BaseResultModel<UserInfoModel> changeUserPwd(String type, String oldPassword, String newPassword, long userId) {
        BaseResultModel<UserInfoModel> baseUserInfoModel = UserServiceImplHelper.parseChangePwdJson(this.context, UserRestfulApiRequester.getUpdateUserInfo(this.context, type, "", "", oldPassword, newPassword));
        if (baseUserInfoModel.getRs() == 1) {
            UserInfoModel userInfoModel = (UserInfoModel) baseUserInfoModel.getData();
            userInfoModel.setUserId(userId);
            userInfoModel.setPwd(newPassword);
            UserManageHelper.getInstance(this.context.getApplicationContext()).changePwd(userInfoModel);
        }
        return baseUserInfoModel;
    }

    public BaseResultModel<UserInfoModel> registUser(String userName, String password, String email) {
        BaseResultModel<UserInfoModel> baseResultModel = UserServiceImplHelper.getRegistUser(this.context, UserRestfulApiRequester.registUser(this.context, userName, password, email));
        if (baseResultModel.getRs() == 1) {
            UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
            userInfoModel.setNickname(userName);
            userInfoModel.setEmail(email);
            userInfoModel.setPwd(password);
            UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo(userInfoModel, true);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<UserInfoModel>> getUserList(long userId, int page, int pageSize, String userType, boolean isLocal, String orderBy, double longitude, double latitude) {
        String jsonStr = "";
        if (isLocal) {
            jsonStr = UserDBUtil.getInstance(this.context.getApplicationContext()).getUserJson(userId, userType);
        }
        if (!isLocal || MCStringUtil.isEmpty(jsonStr)) {
            isLocal = false;
            jsonStr = UserRestfulApiRequester.getUserList(this.context, userId, page, pageSize, userType, orderBy, longitude, latitude);
        }
        BaseResultModel<List<UserInfoModel>> baseResultModel = UserServiceImplHelper.getUserList(this.context, jsonStr);
        if (baseResultModel.getRs() == 1 && !isLocal && page == 1) {
            UserDBUtil.getInstance(this.context.getApplicationContext()).saveUserJson(userId, userType, jsonStr);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<UserInfoModel>> getSurroudUserByLocal(long userId) {
        String jsonStr = UserDBUtil.getInstance(this.context.getApplicationContext()).getUserJson(userId, "all");
        BaseResultModel<List<UserInfoModel>> baseResultModel = new BaseResultModel();
        if (MCStringUtil.isEmpty(jsonStr)) {
            return new BaseResultModel();
        }
        return UserServiceImplHelper.getSurroudUserByNet(this.context, jsonStr);
    }

    public BaseResultModel<List<UserInfoModel>> getSurroudUserByNet(long userId, double longitude, double latitude, int radius, int page, int pageSize) {
        String jsonStr = UserRestfulApiRequester.getSurroudUserByNet(this.context, longitude, latitude, radius, page, pageSize);
        BaseResultModel<List<UserInfoModel>> baseResultModel = UserServiceImplHelper.getSurroudUserByNet(this.context, jsonStr);
        if (baseResultModel.getRs() == 1 && page == 1) {
            UserDBUtil.getInstance(this.context.getApplicationContext()).saveUserJson(userId, "all", jsonStr);
        }
        return baseResultModel;
    }

    public BaseResult locationSetting(int location) {
        return BaseJsonHelper.formJsonRs(UserRestfulApiRequester.setUserLocationSetting(this.context, UserServiceImplHelper.createLocationSettingJson(location)), this.context);
    }

    public BaseResultModel<UserInfoModel> parseOpenplatformUserInfo(String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        baseResultModel = UserServiceImplHelper.parseOpenplatformUserInfo(this.context, jsonStr);
        UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
        BaseResultModel<Object> model = BaseJsonHelper.formJsonRs(jsonStr, this.context);
        if (model.getRs() == 1) {
            UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo(userInfoModel, false);
        } else {
            baseResultModel.setErrorInfo(model.getErrorInfo());
            baseResultModel.setRs(baseResultModel.getRs());
        }
        return baseResultModel;
    }

    public BaseResultModel<UserInfoModel> parseOpenplatformUserInfoModel(String openId, long paltformId, String oauthToken) {
        return parseOpenplatformUserInfo(UserRestfulApiRequester.getPlatFormLoginUserInfoModel(this.context, openId, paltformId, oauthToken));
    }

    public BaseResultModel<UserInfoModel> saveWebRegisteUser(String nickname, UserInfoModel model, String webId) {
        BaseResultModel<UserInfoModel> baseResultModel = UserServiceImplHelper.getRegistUser(this.context, UserRestfulApiRequester.saveWebRegisteUser(this.context, model, webId));
        if (baseResultModel.getRs() == 1) {
            UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
            userInfoModel.setNickname(model.getNickname());
            userInfoModel.setEmail(model.getEmail());
            userInfoModel.setPwd(model.getPwd());
            UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo(userInfoModel, true);
        }
        return baseResultModel;
    }

    public String getFriendOptionRequestUrl(long userId, String act) {
        String token = this.db.getAccessToken();
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(MCResource.getInstance(this.context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(this.context).getString("mc_forum_request_friend_option")).toString())).append("&").append("accessToken").append("=").append(token).append("&").append("accessSecret").append("=").append(this.db.getAccessSecret()).append("&").append("uid").append("=").append(userId).append("&act=").append(act).toString();
    }

    public BaseResultModel<UserInfoModel> getUserPlatforminfo(String openid, String oauthToken, String platformId) {
        return parseOpenplatformUserInfo(UserRestfulApiRequester.getUserPlatforminfo(this.context, openid, oauthToken, platformId));
    }

    public BaseResultModel<UserInfoModel> saveUserPlatforminfo(UserInfoModel userInfoModel, String action, String platformId) {
        BaseResultModel<UserInfoModel> baseResultModel = UserServiceImplHelper.getRegistBoundUser(this.context, UserRestfulApiRequester.saveUserPlatforminfo(this.context, userInfoModel, action, platformId));
        if (baseResultModel.getRs() == 1) {
            UserInfoModel userInfoModel2 = (UserInfoModel) baseResultModel.getData();
            userInfoModel2.setNickname(userInfoModel.getNickname());
            userInfoModel2.setEmail(userInfoModel.getEmail());
            userInfoModel2.setPwd(userInfoModel.getPwd());
            if (!UserManageHelper.getInstance(this.context.getApplicationContext()).addUserInfo(userInfoModel2, true)) {
                baseResultModel.setRs(0);
                baseResultModel.setErrorInfo("");
                UserManageHelper.getInstance(this.context.getApplicationContext()).logout();
            }
        }
        return baseResultModel;
    }
}
