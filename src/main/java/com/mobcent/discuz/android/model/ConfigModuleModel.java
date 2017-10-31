package com.mobcent.discuz.android.model;

import java.util.List;

public class ConfigModuleModel extends BaseModel {
    private static final long serialVersionUID = 6525208891698044986L;
    private List<ConfigComponentModel> componentList;
    private String icon;
    private long id;
    private boolean isComponent;
    private List<ConfigComponentModel> leftTopbarList;
    private String padding;
    private int position;
    private List<ConfigComponentModel> rightTopbarList;
    private String style;
    private String title;
    private String type;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPadding() {
        return this.padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public List<ConfigComponentModel> getLeftTopbarList() {
        return this.leftTopbarList;
    }

    public void setLeftTopbarList(List<ConfigComponentModel> leftTopbarList) {
        this.leftTopbarList = leftTopbarList;
    }

    public List<ConfigComponentModel> getRightTopbarList() {
        return this.rightTopbarList;
    }

    public void setRightTopbarList(List<ConfigComponentModel> rightTopbarList) {
        this.rightTopbarList = rightTopbarList;
    }

    public List<ConfigComponentModel> getComponentList() {
        return this.componentList;
    }

    public void setComponentList(List<ConfigComponentModel> componentList) {
        this.componentList = componentList;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isComponent() {
        return this.isComponent;
    }

    public void setComponent(boolean isComponent) {
        this.isComponent = isComponent;
    }

    public ConfigModuleModel cloneModule() {
        ConfigModuleModel configModuleModel = new ConfigModuleModel();
        configModuleModel.setId(getId());
        configModuleModel.setType(getType());
        configModuleModel.setIcon(getIcon());
        configModuleModel.setTitle(getTitle());
        configModuleModel.setStyle(getStyle());
        configModuleModel.setComponentList(getComponentList());
        configModuleModel.setLeftTopbarList(getLeftTopbarList());
        configModuleModel.setRightTopbarList(getRightTopbarList());
        return configModuleModel;
    }
}
