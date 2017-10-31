package com.mobcent.discuz.android.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigModel extends BaseModel {
    private static final long serialVersionUID = 3075571940419268570L;
    private boolean isPayed = false;
    private boolean isShowMessageList = false;
    private Map<Long, ConfigModuleModel> moduleMap;//key:module id;value:ConfigModuleModel
    private List<ConfigNavModel> navList;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ConfigNavModel> getNavList() {
        return this.navList;
    }

    public void setNavList(List<ConfigNavModel> navList) {
        this.navList = navList;
    }

    public Map<Long, ConfigModuleModel> getModuleMap() {
        if (this.moduleMap == null) {
            this.moduleMap = new HashMap();
        }
        return this.moduleMap;
    }

    public void setModuleMap(Map<Long, ConfigModuleModel> moduleMap) {
        this.moduleMap = moduleMap;
    }

    public boolean isPayed() {
        return this.isPayed;
    }

    public void setPayed(boolean isPayed) {
        this.isPayed = isPayed;
    }

    public boolean isShowMessageList() {
        return this.isShowMessageList;
    }

    public void setShowMessageList(boolean isShowMessageList) {
        this.isShowMessageList = isShowMessageList;
    }
}
