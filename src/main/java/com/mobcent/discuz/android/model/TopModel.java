package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class TopModel implements Serializable {
    private static final long serialVersionUID = -959723294358712238L;
    private long id;
    private String title;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
