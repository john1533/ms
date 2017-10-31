package com.mobcent.discuz.android.model;

public class AnnoModel extends TopicModel {
    private static final long serialVersionUID = 7205015692078660032L;
    private long annoEndDate;
    private long annoId;
    private long annoStartDate;
    private String annoStartTime;
    private String annoUrl;
    private String author;
    private String content;
    private long forumId;
    private String icon;
    private String type;

    public long getAnnoStartDate() {
        return this.annoStartDate;
    }

    public long getAnnoId() {
        return this.annoId;
    }

    public void setAnnoId(long annoId) {
        this.annoId = annoId;
    }

    public void setAnnoStartDate(long annoStartDate) {
        this.annoStartDate = annoStartDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getForumId() {
        return this.forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getAnnoEndDate() {
        return this.annoEndDate;
    }

    public void setAnnoEndDate(long annoEndDate) {
        this.annoEndDate = annoEndDate;
    }

    public String getAnnoUrl() {
        return this.annoUrl;
    }

    public void setAnnoUrl(String annoUrl) {
        this.annoUrl = annoUrl;
    }

    public String getAnnoStartTime() {
        return this.annoStartTime;
    }

    public void setAnnoStartTime(String annoStartTime) {
        this.annoStartTime = annoStartTime;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
