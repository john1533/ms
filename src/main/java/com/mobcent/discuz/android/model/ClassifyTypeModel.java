package com.mobcent.discuz.android.model;

public class ClassifyTypeModel extends BaseModel {
    private static final long serialVersionUID = -1232534948720114488L;
    private int id;
    private String name;
    private boolean select;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return this.select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
