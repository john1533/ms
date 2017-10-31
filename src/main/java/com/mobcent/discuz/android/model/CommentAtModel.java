package com.mobcent.discuz.android.model;

public class CommentAtModel extends BaseModel {
    private static final long serialVersionUID = 6795339275984765490L;
    private long boardId;
    private String boardName;
    private String iconUrl;
    private int isRead;
    private String replyContent;
    private long replyRemindId;
    private String replyUrl;
    private String replyUserName;
    private String time;
    private String topicContent;
    private long topicId;
    private String topicSubject;
    private String topicUrl;
    private long userId;

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTopicContent() {
        return this.topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getTopicUrl() {
        return this.topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getTopicSubject() {
        return this.topicSubject;
    }

    public void setTopicSubject(String topicSubject) {
        this.topicSubject = topicSubject;
    }

    public String getReplyContent() {
        return this.replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyUrl() {
        return this.replyUrl;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public long getReplyRemindId() {
        return this.replyRemindId;
    }

    public void setReplyRemindId(long replyRemindId) {
        this.replyRemindId = replyRemindId;
    }

    public String getReplyUserName() {
        return this.replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
