package com.mobcent.discuz.android.model;

import com.mobcent.lowest.base.model.BaseFallWallModel;
import java.util.List;

public class TopicModel extends BaseFallWallModel {
    private static final long serialVersionUID = 288413788407396615L;
    private int aid;
    private long boardId;
    private String boardName;
    private double distance;
    private int essence;
    private String forumTopicUrl;
    private int gender;
    private long hits;
    private int hot;
    private String icon;
    private List<String> imageList;
    private int isHasRecommendAdd;
    private long lastRelyDate;
    private String location;
    private String picPath;
    private String picToUrl;
    private List<TopicModel> portalRecommList;
    private float ratio;
    private int recommendAdd;
    private String redirectUrl;
    private long replies;
    private String sourceType;
    private int status;
    private String subject;
    private String title;
    private int top;
    private long topicId;
    private long userId;
    private String userName;
    private int vote;

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLastReplyDate() {
        return this.lastRelyDate;
    }

    public void setLastReplyDate(long lastRelyDate) {
        this.lastRelyDate = lastRelyDate;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getHits() {
        return this.hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getReplies() {
        return this.replies;
    }

    public void setReplies(long replies) {
        this.replies = replies;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String summary) {
        this.subject = summary;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPicToUrl() {
        return this.picToUrl;
    }

    public void setPicToUrl(String picToUrl) {
        this.picToUrl = picToUrl;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public List<TopicModel> getPortalRecommList() {
        return this.portalRecommList;
    }

    public void setPortalRecommList(List<TopicModel> portalRecommList) {
        this.portalRecommList = portalRecommList;
    }

    public int getVote() {
        return this.vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getHot() {
        return this.hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getEssence() {
        return this.essence;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public int getAid() {
        return this.aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getRecommendAdd() {
        return this.recommendAdd;
    }

    public void setRecommendAdd(int recommendAdd) {
        this.recommendAdd = recommendAdd;
    }

    public int getIsHasRecommendAdd() {
        return this.isHasRecommendAdd;
    }

    public void setIsHasRecommendAdd(int isHasRecommendAdd) {
        this.isHasRecommendAdd = isHasRecommendAdd;
    }

    public List<String> getImageList() {
        return this.imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getForumTopicUrl() {
        return this.forumTopicUrl;
    }

    public void setForumTopicUrl(String forumTopicUrl) {
        this.forumTopicUrl = forumTopicUrl;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
