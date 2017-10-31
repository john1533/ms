package com.mobcent.discuz.android.model;

public class ContentModel extends BaseModel {
    private static final long serialVersionUID = -3838257548208977466L;
    private String content;
    private int idType;
    private SoundModel soundModel;
    private String source;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public SoundModel getSoundModel() {
        return this.soundModel;
    }

    public void setSoundModel(SoundModel soundModel) {
        this.soundModel = soundModel;
    }

    public int getIdType() {
        return this.idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }
}
