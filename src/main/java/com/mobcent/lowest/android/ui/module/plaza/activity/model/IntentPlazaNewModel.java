package com.mobcent.lowest.android.ui.module.plaza.activity.model;

import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.io.Serializable;
import java.util.List;

public class IntentPlazaNewModel implements Serializable {
    private static final long serialVersionUID = 8578200976962382769L;
    private int[] dbIds = null;
    private List<PlazaAppModel> nativeAppList = null;
    private List<PlazaAppModel> switchAppList = null;

    public List<PlazaAppModel> getSwitchAppList() {
        return this.switchAppList;
    }

    public void setSwitchAppList(List<PlazaAppModel> switchAppList) {
        this.switchAppList = switchAppList;
    }

    public List<PlazaAppModel> getNativeAppList() {
        return this.nativeAppList;
    }

    public void setNativeAppList(List<PlazaAppModel> nativeAppList) {
        this.nativeAppList = nativeAppList;
    }

    public int[] getDbIds() {
        return this.dbIds;
    }

    public void setDbIds(int[] dbIds) {
        this.dbIds = dbIds;
    }
}
