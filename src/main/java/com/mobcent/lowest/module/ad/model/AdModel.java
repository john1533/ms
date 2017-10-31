package com.mobcent.lowest.module.ad.model;

import java.util.ArrayList;
import java.util.List;

public class AdModel extends AdBaseModel implements Cloneable {
    private static final long serialVersionUID = -870214963810034428L;
    private String adu;
    private long aid;
    private int at;
    private int dt;
    private String du;
    private String dx;
    private long gid;
    private int gt;
    private boolean isConsumed;
    private boolean isOther;
    private List<AdOtherModel> others = new ArrayList();
    private int po;
    private String pu;
    private String tel;
    private String tx;

    public long getAid() {
        return this.aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public int getAt() {
        return this.at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public int getDt() {
        return this.dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public String getDu() {
        return this.du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public long getGid() {
        return this.gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public int getGt() {
        return this.gt;
    }

    public void setGt(int gt) {
        this.gt = gt;
    }

    public String getPu() {
        return this.pu;
    }

    public void setPu(String pu) {
        this.pu = pu;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTx() {
        return this.tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public boolean isConsumed() {
        return this.isConsumed;
    }

    public void setConsumed(boolean isConsumed) {
        this.isConsumed = isConsumed;
    }

    public int getPo() {
        return this.po;
    }

    public void setPo(int po) {
        this.po = po;
    }

    public boolean isOther() {
        return this.isOther;
    }

    public void setOther(boolean isOther) {
        this.isOther = isOther;
    }

    public List<AdOtherModel> getOthers() {
        return this.others;
    }

    public void setOthers(List<AdOtherModel> others) {
        this.others = others;
    }

    public String getDx() {
        return this.dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getAdu() {
        return this.adu;
    }

    public void setAdu(String au) {
        this.adu = au;
    }

    public AdModel clone() {
        try {
            return (AdModel) super.clone();
        } catch (Exception e) {
            return null;
        }
    }
}
