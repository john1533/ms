package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ClassifyTopModel;
import com.mobcent.discuz.android.model.ClassifyTypeModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.model.UserProfileModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.tencent.connect.common.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserServiceImplHelper implements UserConstant {
    public static BaseResultModel<List<UserInfoModel>> parseMentionFriendListJson(String jsonStr) {
        BaseResultModel<List<UserInfoModel>> baseResultModel = new BaseResultModel();
        List<UserInfoModel> userList = new ArrayList();
        boolean isAddAdmin = false;
        boolean isAddFriend = false;
        boolean isFollow = false;
        String adminIds = "";
        try {
            JSONArray jsonArray = new JSONObject(jsonStr).optJSONArray("list");
            if (jsonArray != null) {
                int j = jsonArray.length();
                for (int i = 0; i < j; i++) {
                    UserInfoModel userInfo;
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    UserInfoModel userInfoModel = new UserInfoModel();
                    userInfoModel.setNickname(jsonObj.optString("name"));
                    userInfoModel.setRoleNum(jsonObj.optInt("role_num"));
                    userInfoModel.setUserId((long) jsonObj.optInt("uid"));
                    if (userInfoModel.getRoleNum() == 2 && !isAddFriend) {
                        userInfo = new UserInfoModel();
                        userInfo.setUserId(-1);
                        userInfo.setRoleNum(2);
                        userList.add(userInfo);
                        isAddFriend = true;
                    }
                    if (userInfoModel.getRoleNum() == 8 && !isAddAdmin) {
                        userInfo = new UserInfoModel();
                        userInfo.setUserId(-1);
                        userInfo.setRoleNum(8);
                        userList.add(userInfo);
                        isAddAdmin = true;
                    }
                    if (userInfoModel.getRoleNum() == 6 && !isFollow) {
                        userInfo = new UserInfoModel();
                        userInfo.setUserId(-1);
                        userInfo.setRoleNum(6);
                        userList.add(userInfo);
                        isFollow = true;
                    }
                    if (userInfoModel.getRoleNum() == 8) {
                        if (adminIds.contains(new StringBuilder(String.valueOf(userInfoModel.getUserId())).toString())) {
                            userInfoModel.setRoleNum(2);
                        } else {
                            adminIds = new StringBuilder(String.valueOf(adminIds)).append(userInfoModel.getUserId()).append(AdApiConstant.RES_SPLIT_COMMA).toString();
                        }
                    }
                    userList.add(userInfoModel);
                }
                baseResultModel.setData(userList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<UserInfoModel> parseGetUserInfoJson(Context context, String jsonStr) {
        UserInfoModel userInfoModel = new UserInfoModel();
        BaseResultModel<UserInfoModel> result = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, result);
            if (result.getRs() != 0) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                userInfoModel.setIcon(jsonObject.optString("icon"));
                userInfoModel.setNickname(jsonObject.optString("name"));
                if (jsonObject.optInt("gender") == 1) {
                    userInfoModel.setGender(1);
                } else if (jsonObject.optInt("gender") == 2) {
                    userInfoModel.setGender(2);
                } else {
                    userInfoModel.setGender(0);
                }
                userInfoModel.setIsFollow(jsonObject.optInt(UserConstant.IS_FOLLOW));
                userInfoModel.setFriend(jsonObject.optInt(UserConstant.IS_FRIEND_) == 1);
                userInfoModel.setStatus(jsonObject.optInt("status"));
                userInfoModel.setCredits(jsonObject.optInt(UserConstant.CREDITS));
                userInfoModel.setGoldNum(jsonObject.optInt(UserConstant.GOLD_NUM));
                userInfoModel.setScore(jsonObject.optInt(UserConstant.SCORE));
                userInfoModel.setLevel(jsonObject.optInt("level"));
                userInfoModel.setTopicNum(jsonObject.optInt(UserConstant.TOPIC_NUM));
                userInfoModel.setReplyPostsNum(jsonObject.optInt(UserConstant.REPLY_POSTS_NUM));
                userInfoModel.setEssenceNum(jsonObject.optInt(UserConstant.ESSENCE_NUM));
                userInfoModel.setUserFavoriteNum(jsonObject.optInt(UserConstant.USER_FAVOR_NUM));
                userInfoModel.setRelatePostsNum(jsonObject.optInt(UserConstant.USER_RELATE_POSTS_NUM));
                userInfoModel.setUserFriendsNum(jsonObject.optInt(UserConstant.USER_FRIEND_NUM));
                userInfoModel.setBlackStatus(jsonObject.optInt(UserConstant.IS_BLACK_USER));
                userInfoModel.setLevel(jsonObject.optInt("level"));
                userInfoModel.setFollowNum(jsonObject.optInt(UserConstant.USER_FOLLOW_NUM));
                userInfoModel.setPhotoNum(jsonObject.optInt(UserConstant.USER_PHOTO_NUM));
                userInfoModel.setLevelName(jsonObject.optString("userTitle"));
                userInfoModel.setEmail(jsonObject.optString("email"));
                JSONObject jsonObject2 = jsonObject.optJSONObject("body");
                JSONArray array = jsonObject2.optJSONArray(UserConstant.REPEAT_LIST);
                List<UserInfoModel> infoModels = new ArrayList();
                for (int i = 0; i < array.length(); i++) {
                    UserInfoModel infoModel = new UserInfoModel();
                    infoModel.setNickname(array.getJSONObject(i).optString("userName"));
                    infoModels.add(infoModel);
                }
                userInfoModel.setRepeatList(infoModels);
                userInfoModel.setUserProfileList(getUserProfileModels(jsonObject2.optJSONArray(UserConstant.PROFILE_LIST)));
                userInfoModel.setCreditList(getUserCreditList(jsonObject2.optJSONArray(UserConstant.CREDIT_LIST)));
                userInfoModel.setCreditShowList(getUserCreditList(jsonObject2.optJSONArray(UserConstant.CREDIT_SHOW_LIST)));
                result.setData(userInfoModel);
            }
        } catch (Exception e) {
            result.setRs(0);
        }
        return result;
    }

    private static List<UserProfileModel> getUserProfileModels(JSONArray profileArray) {
        List<UserProfileModel> userProfileModels = new ArrayList();
        if (profileArray != null) {
            for (int i = 0; i < profileArray.length(); i++) {
                UserProfileModel model = new UserProfileModel();
                JSONObject object = profileArray.optJSONObject(i);
                model.setType(object.optString("type"));
                model.setTitle(object.optString("title"));
                model.setData(object.optString("data"));
                userProfileModels.add(model);
            }
        }
        return userProfileModels;
    }

    private static List<UserProfileModel> getUserCreditList(JSONArray profileArray) {
        List<UserProfileModel> userProfileModels = new ArrayList();
        if (profileArray != null) {
            for (int i = 0; i < profileArray.length(); i++) {
                UserProfileModel model = new UserProfileModel();
                JSONObject object = profileArray.optJSONObject(i);
                model.setType(object.optString("type"));
                model.setTitle(object.optString("title"));
                model.setData(new StringBuilder(String.valueOf(object.optInt("data"))).toString());
                userProfileModels.add(model);
            }
        }
        return userProfileModels;
    }

    public static BaseResultModel<Object> getManageUser(Context context, String jsonStr) {
        BaseResultModel<Object> result = new BaseResultModel();
        BaseJsonHelper.formJsonRs(context, jsonStr, result);
        return result;
    }

    public static String getPostInfo(Context context, String jsonStr) {
        String postInfoStr = "";
        try {
            if (BaseJsonHelper.formJsonRs(jsonStr, context).getRs() == 0) {
                return postInfoStr;
            }
            postInfoStr = new JSONObject(jsonStr).optJSONObject("body").optString("postInfo");
            return postInfoStr;
        } catch (Exception e) {
            e.printStackTrace();
            postInfoStr = null;
        }
        return postInfoStr;
    }

    public static BaseResultModel<PermissionModel> getPermissionContent(Context context, String jsonStr) {
        BaseResultModel<PermissionModel> baseResultModel = new BaseResultModel();
        PermissionModel model = new PermissionModel();
        try {
            baseResultModel.setRs(1);
            JSONArray postInfoArray = new JSONArray(jsonStr);
            if (postInfoArray != null && postInfoArray.length() > 0) {
                Map<Long, PermissionModel> postInfo = new HashMap();
                for (int i = 0; i < postInfoArray.length(); i++) {
                    JSONObject object = postInfoArray.optJSONObject(i);
                    PermissionModel permissionModel = new PermissionModel();
                    permissionModel.setFid((long) object.optInt("fid"));
                    JSONObject topicObject = object.optJSONObject("topic");
                    if (topicObject != null) {
                        PermissionModel topicPermissionModel = new PermissionModel();
                        topicPermissionModel.setIsAnonymous(topicObject.optInt("isAnonymous"));
                        topicPermissionModel.setIsHidden(topicObject.optInt("isHidden"));
                        topicPermissionModel.setIsOnlyAuthor(topicObject.optInt("isOnlyAuthor"));
                        topicPermissionModel.setAllowPostAttachment(topicObject.optInt(UserConstant.ALLOW_POST_ATTACHMENT));
                        topicPermissionModel.setAllowPostImage(topicObject.optInt("allowPostImage"));
                        topicPermissionModel.setClassifyTypeList(getClassifyTypeList(topicObject.optJSONArray(UserConstant.CLASSIFICATIONTYPE_LIST)));
                        topicPermissionModel.setNewTopicPanel(getClassifyTopList(topicObject.optJSONArray(UserConstant.NEW_TOPIC_PANEL)));
                        permissionModel.setTopicPermissionModel(topicPermissionModel);
                    }
                    JSONObject postObject = object.optJSONObject("post");
                    if (postObject != null) {
                        PermissionModel postPermissionModel = new PermissionModel();
                        postPermissionModel.setIsAnonymous(postObject.optInt("isAnonymous"));
                        postPermissionModel.setIsHidden(postObject.optInt("isHidden"));
                        postPermissionModel.setIsOnlyAuthor(postObject.optInt("isOnlyAuthor"));
                        postPermissionModel.setAllowPostAttachment(postObject.optInt(UserConstant.ALLOW_POST_ATTACHMENT));
                        postPermissionModel.setAllowPostImage(postObject.optInt("allowPostImage"));
                        permissionModel.setPostPermissionModel(postPermissionModel);
                    }
                    postInfo.put(Long.valueOf(permissionModel.getFid()), permissionModel);
                }
                model.setPostInfo(postInfo);
                baseResultModel.setData(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    private static List<ClassifyTopModel> getClassifyTopList(JSONArray topArray) {
        Exception e;
        if (topArray == null) {
            return null;
        }
        try {
            if (topArray.length() <= 0) {
                return null;
            }
            List<ClassifyTopModel> tops = new ArrayList();
            int i = 0;
            while (i < topArray.length()) {
                try {
                    JSONObject object = topArray.optJSONObject(i);
                    ClassifyTopModel model = new ClassifyTopModel();
                    model.setType(object.optString("type"));
                    model.setActionId(object.optInt(UserConstant.ACTION_ID));
                    model.setTitle(object.optString("title"));
                    model.setAction(object.optString("action"));
                    tops.add(model);
                    i++;
                } catch (Exception e2) {
                    e = e2;
                    List<ClassifyTopModel> list = tops;
                }
            }
            return tops;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    private static List<ClassifyTypeModel> getClassifyTypeList(JSONArray typeArray) {
        Exception e;
        if (typeArray == null) {
            return null;
        }
        try {
            if (typeArray.length() <= 0) {
                return null;
            }
            List<ClassifyTypeModel> types = new ArrayList();
            int i = 0;
            while (i < typeArray.length()) {
                try {
                    JSONObject object = typeArray.optJSONObject(i);
                    ClassifyTypeModel model = new ClassifyTypeModel();
                    model.setId(object.optInt(UserConstant.CLASSIFICATIONTYPE_ID));
                    model.setName(object.optString(UserConstant.CLASSIFICATIONTYPE_NAME));
                    types.add(model);
                    i++;
                } catch (Exception e2) {
                    e = e2;
                    List<ClassifyTypeModel> list = types;
                }
            }
            return types;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public static BaseResultModel<UserInfoModel> getLoginUser(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                UserInfoModel userInfoModel = new UserInfoModel();
                userInfoModel.setSecret(jsonObj.optString("secret"));
                userInfoModel.setToken(jsonObj.optString("token"));
                userInfoModel.setUserId(jsonObj.optLong("uid"));
                userInfoModel.setIcon(jsonObj.optString("avatar"));
                userInfoModel.setNickname(jsonObj.optString("userName"));
                baseResultModel.setData(userInfoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<UserInfoModel> parseLoginUserJson(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        UserInfoModel userInfoModel = new UserInfoModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                userInfoModel.setSecret(jsonObj.optString("secret"));
                userInfoModel.setToken(jsonObj.optString("token"));
                userInfoModel.setUserId(jsonObj.optLong("uid"));
                userInfoModel.setIcon(jsonObj.optString("avatar"));
                userInfoModel.setNickname(jsonObj.optString("userName"));
                baseResultModel.setData(userInfoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<UserInfoModel> parseChangePwdJson(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        UserInfoModel userInfoModel = new UserInfoModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                userInfoModel.setSecret(jsonObj.optString("secret"));
                userInfoModel.setToken(jsonObj.optString("token"));
                baseResultModel.setData(userInfoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<UserInfoModel> getRegistUser(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                UserInfoModel userInfoModel = new UserInfoModel();
                userInfoModel.setSecret(jsonObj.optString("secret"));
                userInfoModel.setToken(jsonObj.optString("token"));
                userInfoModel.setUserId(jsonObj.optLong("uid"));
                userInfoModel.setIcon(jsonObj.optString("avatar"));
                userInfoModel.setNickname(jsonObj.optString("userName"));
                baseResultModel.setData(userInfoModel);
            }
        } catch (Exception e) {
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<UserInfoModel>> getUserList(Context context, String jsonStr) {
        BaseResultModel<List<UserInfoModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray listArray = jsonObject.optJSONArray("list");
                List<UserInfoModel> list = new ArrayList();
                if (listArray != null) {
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject jsonObj = listArray.optJSONObject(i);
                        UserInfoModel userInfoModel = new UserInfoModel();
                        int gender = jsonObj.optInt("gender");
                        if (gender == 1) {
                            userInfoModel.setGender(1);
                        } else if (gender == 2) {
                            userInfoModel.setGender(2);
                        } else {
                            userInfoModel.setGender(0);
                        }
                        userInfoModel.setIcon(jsonObj.optString("icon"));
                        userInfoModel.setNickname(jsonObj.optString("name"));
                        userInfoModel.setStatus(jsonObj.optInt("status"));
                        userInfoModel.setLevel(jsonObj.optInt("level"));
                        userInfoModel.setUserId(jsonObj.optLong("uid"));
                        userInfoModel.setBlackStatus(jsonObj.optInt(UserConstant.IS_BLACK_USER));
                        userInfoModel.setCredits(jsonObj.optInt(UserConstant.CREDITS));
                        userInfoModel.setIsFollow(jsonObj.optInt(UserConstant.IS_FRIEND));
                        userInfoModel.setLevel(jsonObj.optInt("level"));
                        userInfoModel.setLocation(jsonObj.optString("location"));
                        userInfoModel.setDistance(jsonObj.optString("distance"));
                        userInfoModel.setLastLoginTime(jsonObj.optString(UserConstant.LAST_LOGIN_TIME));
                        userInfoModel.setDateline(jsonObj.optString("dateline"));
                        userInfoModel.setSignature(jsonObj.optString(UserConstant.SIGNATURE));
                        list.add(userInfoModel);
                    }
                    baseResultModel.setData(list);
                }
                if (list.size() > 0) {
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

    public static BaseResultModel<List<UserInfoModel>> getSurroudUserByNet(Context context, String jsonStr) {
        BaseResultModel<List<UserInfoModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                List<UserInfoModel> list = new ArrayList();
                JSONArray jsonArray = jsonObject.optJSONArray("pois");
                if (jsonArray != null) {
                    int j = jsonArray.length();
                    for (int i = 0; i < j; i++) {
                        JSONObject jsonObj = jsonArray.optJSONObject(i);
                        UserInfoModel userInfoModel = new UserInfoModel();
                        userInfoModel.setLocation(jsonObj.optString("location"));
                        userInfoModel.setDistance(jsonObj.optString("distance"));
                        userInfoModel.setGender(jsonObj.optInt("gender"));
                        userInfoModel.setIcon(jsonObj.optString("icon"));
                        userInfoModel.setBlackStatus(jsonObj.optInt(UserConstant.IS_BLACK_USER));
                        userInfoModel.setIsFollow(jsonObj.optInt(UserConstant.IS_FRIEND));
                        userInfoModel.setNickname(jsonObj.optString("nickname"));
                        userInfoModel.setUserId(jsonObj.optLong("uid"));
                        userInfoModel.setStatus(jsonObj.optInt("status"));
                        list.add(userInfoModel);
                    }
                }
                baseResultModel.setData(list);
                if (list.size() > 0) {
                    baseResultModel.setPage(jsonObject.optInt("page"));
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

    public static String createLocationSettingJson(int location) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject bodyObject = new JSONObject();
            JSONObject hidden = new JSONObject();
            hidden.put(BaseApiConstant.HIDDEN, location);
            bodyObject.put(BaseApiConstant.SETTING_INTO, hidden);
            JSONObject externInfoObject = new JSONObject();
            jsonObject.put("body", bodyObject);
            jsonObject.put("externInfo", externInfoObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<UserInfoModel> parseOpenplatformUserInfo(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        UserInfoModel userInfoModel = new UserInfoModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            JSONObject object = new JSONObject(jsonStr).optJSONObject("body");
            MCLogUtil.e(Constants.PARAM_PLATFORM, "REGISTER==" + object.optInt(UserConstant.REGISTER));
            if (object.optInt(UserConstant.REGISTER) == 1) {
                userInfoModel.setRegister(true);
            } else {
                userInfoModel.setRegister(false);
            }
            if (userInfoModel.isRegister()) {
                userInfoModel.setOpenId(object.optString(UserConstant.OPENID));
                userInfoModel.setToken(object.optString(UserConstant.OAUTH_TOKEN));
                userInfoModel.setPlatformId(object.optLong("platformId"));
            } else {
                userInfoModel.setSecret(object.optString("secret"));
                userInfoModel.setToken(object.optString("token"));
                userInfoModel.setUserId(object.optLong("uid"));
                userInfoModel.setIcon(object.optString("avatar"));
                userInfoModel.setNickname(object.optString("userName"));
            }
            baseResultModel.setData(userInfoModel);
        } catch (Exception e) {
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<UserInfoModel> getRegistBoundUser(Context context, String jsonStr) {
        BaseResultModel<UserInfoModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject object = new JSONObject(jsonStr).optJSONObject("body");
                UserInfoModel userInfoModel = new UserInfoModel();
                userInfoModel.setSecret(object.optString("secret"));
                userInfoModel.setToken(object.optString("token"));
                userInfoModel.setUserId(object.optLong("uid"));
                userInfoModel.setIcon(object.optString("avatar"));
                userInfoModel.setNickname(object.optString("userName"));
                baseResultModel.setData(userInfoModel);
            }
        } catch (Exception e) {
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }
}
