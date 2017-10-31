package com.mobcent.discuz.android.model;

public class PictureModel extends BaseModel {
    private static final long serialVersionUID = 1549854405233131345L;
    private String absolutePath;
    private int position;

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
