package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import java.util.List;

public interface UserService {
    boolean addLocalForumMentionFriend(long j, UserInfoModel userInfoModel);

    BaseResultModel<UserInfoModel> changeUserPwd(String str, String str2, String str3, long j);

    boolean deletedLocalForumMentionFriend(long j, UserInfoModel userInfoModel);

    List<UserInfoModel> getAllUserInfo();

    String getFriendOptionRequestUrl(long j, String str);

    UserInfoModel getLastUserInfo();

    BaseResultModel<List<UserInfoModel>> getMentionFriend(long j, boolean z);

    BaseResultModel<List<UserInfoModel>> getMentionFriendByNet(long j);

    BaseResultModel<PermissionModel> getPermissionModel(String str);

    String getPermissionStr(boolean z, long j);

    BaseResultModel<List<UserInfoModel>> getSurroudUserByLocal(long j);

    BaseResultModel<List<UserInfoModel>> getSurroudUserByNet(long j, double d, double d2, int i, int i2, int i3);

    BaseResultModel<UserInfoModel> getUser();

    BaseResultModel<UserInfoModel> getUserInfo(long j);

    BaseResultModel<List<UserInfoModel>> getUserList(long j, int i, int i2, String str, boolean z, String str2, double d, double d2);

    BaseResultModel<UserInfoModel> getUserPlatforminfo(String str, String str2, String str3);

    boolean isLogin();

    BaseResult locationSetting(int i);

    BaseResultModel<UserInfoModel> loginUser(String str, String str2, String str3);

    BaseResultModel<Object> logoutUser(String str);

    BaseResultModel<Object> manageUser(long j, String str);

    BaseResultModel<UserInfoModel> parseOpenplatformUserInfo(String str);

    BaseResultModel<UserInfoModel> parseOpenplatformUserInfoModel(String str, long j, String str2);

    BaseResultModel<UserInfoModel> registUser(String str, String str2, String str3);

    BaseResultModel<UserInfoModel> saveUserPlatforminfo(UserInfoModel userInfoModel, String str, String str2);

    BaseResultModel<UserInfoModel> saveWebRegisteUser(String str, UserInfoModel userInfoModel, String str2);

    BaseResultModel<UserInfoModel> switchUser(String str);

    BaseResultModel<Object> updateUser(String str, int i, String str2, boolean z);

    BaseResultModel<UploadPictureModel> uploadIcon(String str, long j);
}
