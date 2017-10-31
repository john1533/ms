package com.mobcent.discuz.android.model;

public class BoardChild extends BaseModel {
    private static final long serialVersionUID = -6179520966646266556L;
    private int boardChild;
    private int boardContent;
    private long boardId;
    private String boardImg;
    private String boardName;
    private String description;
    private String forumRedirect;
    private boolean isHaveAnnoModel;
    private long lastPostsDate;
    private int postsTotalNum;
    private int todayPostsNum;
    private int topicTotalNum;

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public int getBoardChild() {
        return this.boardChild;
    }

    public void setBoardChild(int boardChild) {
        this.boardChild = boardChild;
    }

    public String getBoardImg() {
        return this.boardImg;
    }

    public void setBoardImg(String boardImg) {
        this.boardImg = boardImg;
    }

    public int getBoardContent() {
        return this.boardContent;
    }

    public void setBoardContent(int boardContent) {
        this.boardContent = boardContent;
    }

    public int getTodayPostsNum() {
        return this.todayPostsNum;
    }

    public void setTodayPostsNum(int todayPostsNum) {
        this.todayPostsNum = todayPostsNum;
    }

    public int getTopicTotalNum() {
        return this.topicTotalNum;
    }

    public void setTopicTotalNum(int topicTotalNum) {
        this.topicTotalNum = topicTotalNum;
    }

    public int getPostsTotalNum() {
        return this.postsTotalNum;
    }

    public void setPostsTotalNum(int postsTotalNum) {
        this.postsTotalNum = postsTotalNum;
    }

    public long getLastPostsDate() {
        return this.lastPostsDate;
    }

    public void setLastPostsDate(long lastPostsDate) {
        this.lastPostsDate = lastPostsDate;
    }

    public String getForumRedirect() {
        return this.forumRedirect;
    }

    public void setForumRedirect(String forumRedirect) {
        this.forumRedirect = forumRedirect;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHaveAnnoModel() {
        return this.isHaveAnnoModel;
    }

    public void setHaveAnnoModel(boolean isHaveAnnoModel) {
        this.isHaveAnnoModel = isHaveAnnoModel;
    }
}
