package com.mobcent.discuz.android.model;

public class BaseResultModel<T> extends BaseResult {
    private static final long serialVersionUID = 1;
    private T data;

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
