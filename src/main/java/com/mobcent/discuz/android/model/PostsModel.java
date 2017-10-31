package com.mobcent.discuz.android.model;

import java.util.List;
import java.util.Map;

public class PostsModel extends BaseModel {
    private static final long serialVersionUID = 467820137076940333L;
    private ActivityModel activityInfo;
    private String boardName;
    private List<TopicContentModel> content;
    private long createDate;
    private int essence;
    private Map<String, OtherPanelModel> extraPanel;
    private String forumTopicUrl;
    private int hits;
    private int hot;
    private String icon;
    private int isFavor;
    private boolean isFollow;
    private boolean isHasActivityInfo;
    private int level;
    private String location;
    private Map<String, OtherPanelModel> managePanel;
    private String mobileSign;
    private PollModel pollInfo;
    private long postsId;
    private Map<String, List<String>> rateList;
    private String rateShowAllUrl;
    private int replies;
    private long sortId;
    private String title;
    private int top;
    private long topicId;
    private String type;
    private long userId;
    private String userNickName;
    private String userTitle;

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSortId() {
        return this.sortId;
    }

    public void setSortId(long sortId) {
        this.sortId = sortId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return this.userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getReplies() {
        return this.replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getHits() {
        return this.hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getEssence() {
        return this.essence;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public int getHot() {
        return this.hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getIsFavor() {
        return this.isFavor;
    }

    public void setIsFavor(int isFavor) {
        this.isFavor = isFavor;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserTitle() {
        return this.userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public List<TopicContentModel> getContent() {
        return this.content;
    }

    public void setContent(List<TopicContentModel> content) {
        this.content = content;
    }

    public PollModel getPollInfo() {
        return this.pollInfo;
    }

    public void setPollInfo(PollModel pollInfo) {
        this.pollInfo = pollInfo;
    }

    public boolean isHasActivityInfo() {
        return this.isHasActivityInfo;
    }

    public void setHasActivityInfo(boolean isHasActivityInfo) {
        this.isHasActivityInfo = isHasActivityInfo;
    }

    public ActivityModel getActivityInfo() {
        return this.activityInfo;
    }

    public void setActivityInfo(ActivityModel activityInfo) {
        this.activityInfo = activityInfo;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, OtherPanelModel> getManagePanel() {
        return this.managePanel;
    }

    public void setManagePanel(Map<String, OtherPanelModel> managePanel) {
        this.managePanel = managePanel;
    }

    public Map<String, OtherPanelModel> getExtraPanel() {
        return this.extraPanel;
    }

    public void setExtraPanel(Map<String, OtherPanelModel> extraPanel) {
        this.extraPanel = extraPanel;
    }

    public String getMobileSign() {
        return this.mobileSign;
    }

    public void setMobileSign(String mobileSign) {
        this.mobileSign = mobileSign;
    }

    public long getPostsId() {
        return this.postsId;
    }

    public void setPostsId(long postsId) {
        this.postsId = postsId;
    }

    public Map<String, List<String>> getRateList() {
        return this.rateList;
    }

    public void setRateList(Map<String, List<String>> rateList) {
        this.rateList = rateList;
    }

    public String getRateShowAllUrl() {
        return this.rateShowAllUrl;
    }

    public void setRateShowAllUrl(String rateShowAllUrl) {
        this.rateShowAllUrl = rateShowAllUrl;
    }

    public boolean isFollow() {
        return this.isFollow;
    }

    public void setFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public String getForumTopicUrl() {
        return this.forumTopicUrl;
    }

    public void setForumTopicUrl(String forumTopicUrl) {
        this.forumTopicUrl = forumTopicUrl;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
