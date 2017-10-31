package com.mobcent.discuz.android.model;

public class UploadPictureModel extends BaseModel {
    private static final long serialVersionUID = -4249716058300622481L;
    private long aid;
    private String changePath;
    private String folderPath;
    private String name;
    private String picPath;
    private String savePath;

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getAid() {
        return this.aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getChangePath() {
        return this.changePath;
    }

    public void setChangePath(String changePath) {
        this.changePath = changePath;
    }
}
