package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class HeartInfoModel implements Serializable {
    private static final long serialVersionUID = 8666188485301503149L;
    private int count;
    private long time;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
