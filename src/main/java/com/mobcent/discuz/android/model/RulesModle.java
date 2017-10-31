package com.mobcent.discuz.android.model;

import java.io.Serializable;
import java.util.List;

public class RulesModle implements Serializable {
    private static final long serialVersionUID = -3817734983751527503L;
    private List<ChoicesModle> choicesModleList;
    private String colsize;
    private String defaultvalue;
    private String inputsize;
    private int isDate;
    private String maxheight;
    private String maxlength;
    private String maxnum;
    private String maxwidth;
    private String minnum;
    private String profile;
    private String rowsize;
    private int typeNum;

    public int getIsDate() {
        return this.isDate;
    }

    public void setIsDate(int isDate) {
        this.isDate = isDate;
    }

    public int getTypeNum() {
        return this.typeNum;
    }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public List<ChoicesModle> getChoicesModleList() {
        return this.choicesModleList;
    }

    public void setChoicesModleList(List<ChoicesModle> choicesModleList) {
        this.choicesModleList = choicesModleList;
    }

    public String getMaxwidth() {
        return this.maxwidth;
    }

    public void setMaxwidth(String maxwidth) {
        this.maxwidth = maxwidth;
    }

    public String getMaxlength() {
        return this.maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getInputsize() {
        return this.inputsize;
    }

    public void setInputsize(String inputsize) {
        this.inputsize = inputsize;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDefaultvalue() {
        return this.defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getColsize() {
        return this.colsize;
    }

    public void setColsize(String colsize) {
        this.colsize = colsize;
    }

    public String getRowsize() {
        return this.rowsize;
    }

    public void setRowsize(String rowsize) {
        this.rowsize = rowsize;
    }

    public String getMaxheight() {
        return this.maxheight;
    }

    public void setMaxheight(String maxheight) {
        this.maxheight = maxheight;
    }

    public String getMaxnum() {
        return this.maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getMinnum() {
        return this.minnum;
    }

    public void setMinnum(String minnum) {
        this.minnum = minnum;
    }
}
