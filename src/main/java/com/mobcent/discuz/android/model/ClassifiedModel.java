package com.mobcent.discuz.android.model;

public class ClassifiedModel extends BaseModel {
    private static final long serialVersionUID = -1828130675242036823L;
    private String classifiedAid;
    private String classifiedName;
    private RulesModle classifiedRules;
    private String classifiedTitle;
    private int classifiedTopId;
    private int classifiedType;
    private String classifiedValue;
    private String required;
    private String unchangeable;

    public String getClassifiedTitle() {
        return this.classifiedTitle;
    }

    public void setClassifiedTitle(String classifiedTitle) {
        this.classifiedTitle = classifiedTitle;
    }

    public String getClassifiedName() {
        return this.classifiedName;
    }

    public void setClassifiedName(String classifiedName) {
        this.classifiedName = classifiedName;
    }

    public int getClassifiedType() {
        return this.classifiedType;
    }

    public void setClassifiedType(int classifiedType) {
        this.classifiedType = classifiedType;
    }

    public RulesModle getClassifiedRules() {
        return this.classifiedRules;
    }

    public void setClassifiedRules(RulesModle classifiedRules) {
        this.classifiedRules = classifiedRules;
    }

    public String getRequired() {
        return this.required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getUnchangeable() {
        return this.unchangeable;
    }

    public void setUnchangeable(String unchangeable) {
        this.unchangeable = unchangeable;
    }

    public String getClassifiedValue() {
        return this.classifiedValue;
    }

    public void setClassifiedValue(String classifiedValue) {
        this.classifiedValue = classifiedValue;
    }

    public int getClassifiedTopId() {
        return this.classifiedTopId;
    }

    public void setClassifiedTopId(int classifiedTopId) {
        this.classifiedTopId = classifiedTopId;
    }

    public String getClassifiedAid() {
        return this.classifiedAid;
    }

    public void setClassifiedAid(String classifiedAid) {
        this.classifiedAid = classifiedAid;
    }
}
