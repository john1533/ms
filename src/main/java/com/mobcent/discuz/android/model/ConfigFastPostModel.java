package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class ConfigFastPostModel implements Serializable {
    private static final long serialVersionUID = 1;
    private long fid;
    private String name;

    public long getFid() {
        return this.fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
