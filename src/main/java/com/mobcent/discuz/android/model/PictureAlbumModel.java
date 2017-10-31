package com.mobcent.discuz.android.model;

public class PictureAlbumModel extends BaseModel {
    private static final long serialVersionUID = 4330427937701906320L;
    private String firstPicPath;
    private String folderPath;
    private int size;

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFirstPicPath() {
        return this.firstPicPath;
    }

    public void setFirstPicPath(String firstPicPath) {
        this.firstPicPath = firstPicPath;
    }
}
