package com.mobcent.discuz.android.model;

import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.List;

public class UserInfoModel extends BaseModel {
    private static final long serialVersionUID = -5139521112098754278L;
    private String autoToken;
    private int blackStatus;
    private String creditInfo;
    private List<UserProfileModel> creditList;
    private List<UserProfileModel> creditShowList;
    private int credits;
    private int currUser;
    private String dateline;
    private String distance;
    private String email;
    private int essenceNum;
    private String extraUserInfo;
    private int followNum;
    private int gender;
    private int goldNum;
    private String icon;
    private int isFollow;
    private boolean isFriend;
    private String lastLoginTime;
    private int level;
    private String levelName;
    private String location;
    private String nickname;
    private String openId;
    private long pageFrom;
    private String permission;
    private int photoNum;
    private long platformId;
    private String platformUserId;
    private String pwd;
    private String regEmail;
    private int regSource;
    private boolean register;
    private int relatePostsNum;
    private List<UserInfoModel> repeatList;
    private int replyPostsNum;
    private int roleNum;
    private int score;
    private String secret;
    private String signature;
    private int status;
    private String token;
    private int topicNum;
    private int userFavoriteNum;
    private int userFriendsNum;
    private long userId;
    private List<UserProfileModel> userProfileList;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getExtraUserInfo() {
        return this.extraUserInfo;
    }

    public void setExtraUserInfo(String extraUserInfo) {
        this.extraUserInfo = extraUserInfo;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getPageFrom() {
        return this.pageFrom;
    }

    public void setPageFrom(long pageFrom) {
        this.pageFrom = pageFrom;
    }

    public int getRelatePostsNum() {
        return this.relatePostsNum;
    }

    public void setRelatePostsNum(int relatePostsNum) {
        this.relatePostsNum = relatePostsNum;
    }

    public int getBlackStatus() {
        return this.blackStatus;
    }

    public void setBlackStatus(int blackStatus) {
        this.blackStatus = blackStatus;
    }

    public int getUserFriendsNum() {
        return this.userFriendsNum;
    }

    public void setUserFriendsNum(int userFriendsNum) {
        this.userFriendsNum = userFriendsNum;
    }

    public int getUserFavoriteNum() {
        return this.userFavoriteNum;
    }

    public void setUserFavoriteNum(int userFavoriteNum) {
        this.userFavoriteNum = userFavoriteNum;
    }

    public int getIsFollow() {
        return this.isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getGoldNum() {
        return this.goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    public int getTopicNum() {
        return this.topicNum;
    }

    public void setTopicNum(int topicNum) {
        this.topicNum = topicNum;
    }

    public int getReplyPostsNum() {
        return this.replyPostsNum;
    }

    public void setReplyPostsNum(int replyPostsNum) {
        this.replyPostsNum = replyPostsNum;
    }

    public int getEssenceNum() {
        return this.essenceNum;
    }

    public void setEssenceNum(int essenceNum) {
        this.essenceNum = essenceNum;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrUser() {
        return this.currUser;
    }

    public void setCurrUser(int currUser) {
        this.currUser = currUser;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRoleNum() {
        return this.roleNum;
    }

    public void setRoleNum(int roleNum) {
        this.roleNum = roleNum;
    }

    public boolean isRegister() {
        return this.register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public String getPlatformUserId() {
        return this.platformUserId;
    }

    public void setPlatformUserId(String platformUserId) {
        this.platformUserId = platformUserId;
    }

    public boolean equals(Object obj) {
        UserInfoModel userInfoModel = (UserInfoModel) obj;
        if (userInfoModel == null || this == null) {
            return false;
        }
        if (getNickname() != null && userInfoModel.getNickname() != null && !getNickname().equals(userInfoModel.getNickname())) {
            return true;
        }
        if (userInfoModel.getIcon() != null && getIcon() != null && !getIcon().equals(userInfoModel.getIcon())) {
            return true;
        }
        if ((userInfoModel.getIcon() == null || getIcon() != null) && getGender() == userInfoModel.getGender()) {
            return false;
        }
        return true;
    }

    public long getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public int getFollowNum() {
        return this.followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public String getRegEmail() {
        return this.regEmail;
    }

    public void setRegEmail(String regEmail) {
        this.regEmail = regEmail;
    }

    public int getRegSource() {
        return this.regSource;
    }

    public void setRegSource(int regSource) {
        this.regSource = regSource;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAutoToken() {
        return this.autoToken;
    }

    public void setAutoToken(String autoToken) {
        this.autoToken = autoToken;
    }

    public int getPhotoNum() {
        return this.photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    public String getLevelName() {
        return this.levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public List<UserInfoModel> getRepeatList() {
        return this.repeatList;
    }

    public void setRepeatList(List<UserInfoModel> repeatList) {
        this.repeatList = repeatList;
    }

    public List<UserProfileModel> getUserProfileList() {
        return this.userProfileList;
    }

    public void setUserProfileList(List<UserProfileModel> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<UserProfileModel> getCreditList() {
        return this.creditList;
    }

    public void setCreditList(List<UserProfileModel> creditList) {
        this.creditList = creditList;
    }

    public List<UserProfileModel> getCreditShowList() {
        return this.creditShowList;
    }

    public void setCreditShowList(List<UserProfileModel> creditShowList) {
        this.creditShowList = creditShowList;
    }

    public String getCreditInfo() {
        StringBuffer sb = new StringBuffer();
        if (!MCListUtils.isEmpty(this.creditShowList)) {
            for (UserProfileModel credit : this.creditShowList) {
                if (credit != null) {
                    sb.append(credit.getTitle() + ":" + credit.getData() + " ");
                }
            }
        }
        return sb.toString();
    }

    public void setCreditInfo(String creditInfo) {
        this.creditInfo = creditInfo;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isFriend() {
        return this.isFriend;
    }

    public void setFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getDateline() {
        return this.dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
