package com.mobcent.discuz.android.model;

import java.util.List;

public class FriendModel extends BaseModel {
    private static final long serialVersionUID = -7645391066776097781L;
    private List<OtherPanelModel> actions;
    private String author;
    private String authorAvatar;
    private long authorId;
    private String dateLine;
    private long fromId;
    private String fromIdType;
    private String iconUrl;
    private String note;
    private String type;

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDateLine() {
        return this.dateLine;
    }

    public void setDateLine(String dateLine) {
        this.dateLine = dateLine;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getFromId() {
        return this.fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public String getFromIdType() {
        return this.fromIdType;
    }

    public void setFromIdType(String fromIdType) {
        this.fromIdType = fromIdType;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorAvatar() {
        return this.authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public List<OtherPanelModel> getActions() {
        return this.actions;
    }

    public void setActions(List<OtherPanelModel> actions) {
        this.actions = actions;
    }
}
