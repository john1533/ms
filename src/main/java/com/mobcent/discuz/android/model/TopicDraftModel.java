package com.mobcent.discuz.android.model;

import android.text.TextUtils;
import com.mobcent.discuz.android.db.TopicDraftDBUtil;
import com.mobcent.discuz.android.db.constant.TopicDraftDBConstant;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TopicDraftModel implements Serializable, TopicDraftDBConstant {
    private static final long serialVersionUID = 3735826464935778778L;
    private long boardId;
    private String boardName;
    private long commonId;
    private String content;
    private String draftId;
    private int id;
    private PermissionModel isCheckedSettingModel;
    private String json;
    private String location;
    private String[] picPath;
    private long saveTime;
    private String title;
    private int type;
    private long typeId;
    private List<ClassifyTypeModel> typeList;
    private String typeName;
    private String voicePath;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCommonId() {
        return this.commonId;
    }

    public void setCommonId(long commonId) {
        this.commonId = commonId;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public PermissionModel getIsCheckedSettingModel() {
        return this.isCheckedSettingModel;
    }

    public void setIsCheckedSettingModel(PermissionModel isCheckedSettingModel) {
        this.isCheckedSettingModel = isCheckedSettingModel;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDraftId() {
        return TopicDraftDBUtil.getDraftId(this);
    }

    public long getSaveTime() {
        return this.saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getJson() {
        return parseJson();
    }

    public void setJson(String json) {
        parseModel(json);
        this.json = json;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVoicePath() {
        return this.voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String[] getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String[] picPath) {
        this.picPath = picPath;
    }

    public List<ClassifyTypeModel> getTypeList() {
        return this.typeList;
    }

    public void setTypeList(List<ClassifyTypeModel> typeList) {
        this.typeList = typeList;
    }

    private String parseJson() {
        JSONObject rootObj = new JSONObject();
        try {
            rootObj.put("type", this.type);
            rootObj.put("typeId", this.typeId);
            rootObj.put(TopicDraftDBConstant.COLUMN_TYPE_NAME, this.typeName);
            rootObj.put(TopicDraftDBConstant.COLUMN_COMMON_ID, this.commonId);
            rootObj.put("boardId", this.boardId);
            rootObj.put("boardName", this.boardName);
            rootObj.put(TopicDraftDBConstant.COLUMN_VOICE_PATH, this.voicePath);
            rootObj.put("title", this.title);
            rootObj.put("content", this.content);
            if (this.isCheckedSettingModel != null) {
                JSONObject settingObj = new JSONObject();
                settingObj.put(TopicDraftDBConstant.CHECK_COLOMN_ISHIDDEN, this.isCheckedSettingModel.getIsHidden());
                settingObj.put(TopicDraftDBConstant.CHECK_COLOMN_ISANONYMOUS, this.isCheckedSettingModel.getIsAnonymous());
                settingObj.put(TopicDraftDBConstant.CHECK_COLOMN_ISONLYAUTHOR, this.isCheckedSettingModel.getIsOnlyAuthor());
                rootObj.put(TopicDraftDBConstant.CHECK_SETTING_MODEL, settingObj);
            }
            JSONArray jsonArray = null;
            if (!(this.picPath == null || this.picPath.length == 0)) {
                jsonArray = new JSONArray();
                for (Object put : this.picPath) {
                    jsonArray.put(put);
                }
            }
            if (jsonArray != null) {
                rootObj.put("pic_path", jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootObj.toString();
    }

    private void parseModel(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                getClass().getFields()[0].getGenericType();
                JSONObject rootObj = new JSONObject(json);
                this.commonId = rootObj.optLong(TopicDraftDBConstant.COLUMN_COMMON_ID);
                this.typeId = rootObj.optLong("typeId");
                this.typeName = rootObj.optString(TopicDraftDBConstant.COLUMN_TYPE_NAME);
                this.boardId = rootObj.optLong("boardId");
                this.boardName = rootObj.optString("boardName");
                this.voicePath = rootObj.optString(TopicDraftDBConstant.COLUMN_VOICE_PATH);
                this.title = rootObj.optString("title");
                this.content = rootObj.optString("content");
                JSONObject settingObj = rootObj.optJSONObject(TopicDraftDBConstant.CHECK_SETTING_MODEL);
                if (settingObj != null) {
                    this.isCheckedSettingModel = new PermissionModel();
                    this.isCheckedSettingModel.setIsHidden(settingObj.optInt(TopicDraftDBConstant.CHECK_COLOMN_ISHIDDEN));
                    this.isCheckedSettingModel.setIsAnonymous(settingObj.optInt(TopicDraftDBConstant.CHECK_COLOMN_ISANONYMOUS));
                    this.isCheckedSettingModel.setIsOnlyAuthor(settingObj.optInt(TopicDraftDBConstant.CHECK_COLOMN_ISONLYAUTHOR));
                }
                JSONArray jsonArray = rootObj.optJSONArray("pic_path");
                if (jsonArray != null && jsonArray.length() != 0) {
                    int len = jsonArray.length();
                    this.picPath = new String[len];
                    for (int i = 0; i < len; i++) {
                        this.picPath[i] = jsonArray.optString(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        return "TopicDraftModel [id=" + this.id + ", commonId=" + this.commonId + ", boardId=" + this.boardId + ", boardName=" + this.boardName + ", title=" + this.title + ", content=" + this.content + ", isCheckedSettingModel=" + this.isCheckedSettingModel + ", draftId=" + this.draftId + ", saveTime=" + this.saveTime + ", json=" + this.json + ", type=" + this.type + ", voicePath=" + this.voicePath + ", picPath=" + Arrays.toString(this.picPath) + ", typeName=" + this.typeName + ", typeId=" + this.typeId + "]";
    }
}
