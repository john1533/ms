package com.mobcent.discuz.android.model;

import java.util.List;

public class ActivityModel extends BaseModel {
    private static final long serialVersionUID = 1238353873224586076L;
    private ActivityModel actionInfo;
    private ActivityModel applyList;
    private ActivityModel applyListVerified;
    private List<TopicContentModel> content;
    private String description;
    private String image;
    private List<ActivityOptionModel> options;
    private String summary;
    private String title;
    private String type;

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<TopicContentModel> getContent() {
        return this.content;
    }

    public void setContent(List<TopicContentModel> content) {
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ActivityOptionModel> getOptions() {
        return this.options;
    }

    public void setOptions(List<ActivityOptionModel> options) {
        this.options = options;
    }

    public ActivityModel getActionInfo() {
        return this.actionInfo;
    }

    public void setActionInfo(ActivityModel actionInfo) {
        this.actionInfo = actionInfo;
    }

    public ActivityModel getApplyList() {
        return this.applyList;
    }

    public void setApplyList(ActivityModel applyList) {
        this.applyList = applyList;
    }

    public ActivityModel getApplyListVerified() {
        return this.applyListVerified;
    }

    public void setApplyListVerified(ActivityModel applyListVerified) {
        this.applyListVerified = applyListVerified;
    }
}
