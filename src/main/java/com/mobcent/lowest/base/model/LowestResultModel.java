package com.mobcent.lowest.base.model;

import java.io.Serializable;

public class LowestResultModel<T> implements Serializable {
    private static final long serialVersionUID = -506755261828753853L;
    private T data;
    private boolean isUpdate = false;
    private int rs;
    private long ut;

    public long getUt() {
        return this.ut;
    }

    public void setUt(long ut) {
        this.ut = ut;
    }

    public int getRs() {
        return this.rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public boolean isUpdate() {
        return this.isUpdate;
    }

    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
