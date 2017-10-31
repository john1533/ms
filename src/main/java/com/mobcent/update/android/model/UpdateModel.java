package com.mobcent.update.android.model;

import java.io.Serializable;

public class UpdateModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String appName;
    private String desc;
    private String icon;
    private int id;
    private String link;
    private int showDetail;
    private float size;
    private int status;
    private long time;
    private String type;
    private String ver_name;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowDetail() {
        return this.showDetail;
    }

    public void setShowDetail(int showDetail) {
        this.showDetail = showDetail;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVer_name() {
        return this.ver_name;
    }

    public void setVer_name(String ver_name) {
        this.ver_name = ver_name;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
