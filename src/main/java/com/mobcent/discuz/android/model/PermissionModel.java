package com.mobcent.discuz.android.model;

import java.util.List;
import java.util.Map;

public class PermissionModel extends BaseModel {
    private static final long serialVersionUID = -2845452024346877065L;
    private int allowPostAttachment;
    private int allowPostImage;
    private List<ClassifyTypeModel> classifyTypeList;
    private long fid;
    private int isAnonymous;
    private int isHidden;
    private int isOnlyAuthor;
    private int isOnlyTopicType;
    private List<ClassifyTopModel> newTopicPanel;
    private Map<Long, PermissionModel> postInfo;
    private PermissionModel postPermissionModel;
    private PermissionModel topicPermissionModel;

    public Map<Long, PermissionModel> getPostInfo() {
        return this.postInfo;
    }

    public void setPostInfo(Map<Long, PermissionModel> postInfo) {
        this.postInfo = postInfo;
    }

    public PermissionModel getTopicPermissionModel() {
        return this.topicPermissionModel;
    }

    public void setTopicPermissionModel(PermissionModel topicPermissionModel) {
        this.topicPermissionModel = topicPermissionModel;
    }

    public PermissionModel getPostPermissionModel() {
        return this.postPermissionModel;
    }

    public void setPostPermissionModel(PermissionModel postPermissionModel) {
        this.postPermissionModel = postPermissionModel;
    }

    public long getFid() {
        return this.fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public int getIsHidden() {
        return this.isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public int getIsAnonymous() {
        return this.isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public int getIsOnlyAuthor() {
        return this.isOnlyAuthor;
    }

    public void setIsOnlyAuthor(int isOnlyAuthor) {
        this.isOnlyAuthor = isOnlyAuthor;
    }

    public int getAllowPostAttachment() {
        return this.allowPostAttachment;
    }

    public void setAllowPostAttachment(int allowPostAttachment) {
        this.allowPostAttachment = allowPostAttachment;
    }

    public int getAllowPostImage() {
        return this.allowPostImage;
    }

    public void setAllowPostImage(int allowPostImage) {
        this.allowPostImage = allowPostImage;
    }

    public int getIsOnlyTopicType() {
        return this.isOnlyTopicType;
    }

    public void setIsOnlyTopicType(int isOnlyTopicType) {
        this.isOnlyTopicType = isOnlyTopicType;
    }

    public List<ClassifyTopModel> getNewTopicPanel() {
        return this.newTopicPanel;
    }

    public void setNewTopicPanel(List<ClassifyTopModel> newTopicPanel) {
        this.newTopicPanel = newTopicPanel;
    }

    public List<ClassifyTypeModel> getClassifyTypeList() {
        return this.classifyTypeList;
    }

    public void setClassifyTypeList(List<ClassifyTypeModel> classifyTypeList) {
        this.classifyTypeList = classifyTypeList;
    }
}
