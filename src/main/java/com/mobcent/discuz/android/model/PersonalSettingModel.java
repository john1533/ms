package com.mobcent.discuz.android.model;

public class PersonalSettingModel {
    private boolean isAtNotify;
    private boolean isLocationAvailable;
    private boolean isLocationOpen;
    private boolean isPicAvailable;
    private boolean isReplyNotify;
    private boolean isSoundOpen;

    public boolean isReplyNotify() {
        return this.isReplyNotify;
    }

    public void setReplyNotify(boolean isReplyNotify) {
        this.isReplyNotify = isReplyNotify;
    }

    public boolean isAtNotify() {
        return this.isAtNotify;
    }

    public void setAtNotify(boolean isAtNotify) {
        this.isAtNotify = isAtNotify;
    }

    public boolean isSoundOpen() {
        return this.isSoundOpen;
    }

    public void setSoundOpen(boolean isSoundOpen) {
        this.isSoundOpen = isSoundOpen;
    }

    public boolean isLocationOpen() {
        return this.isLocationOpen;
    }

    public void setLocationOpen(boolean isLocationOpen) {
        this.isLocationOpen = isLocationOpen;
    }

    public boolean isLocationAvailable() {
        return this.isLocationAvailable;
    }

    public void setLocationAvailable(boolean isLocationAvailable) {
        this.isLocationAvailable = isLocationAvailable;
    }

    public boolean isPicAvailable() {
        return this.isPicAvailable;
    }

    public void setPicAvailable(boolean isPicAvailable) {
        this.isPicAvailable = isPicAvailable;
    }
}
