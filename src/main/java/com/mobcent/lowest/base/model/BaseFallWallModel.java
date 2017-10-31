package com.mobcent.lowest.base.model;

import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import java.io.Serializable;

public class BaseFallWallModel implements Serializable {
    private static final long serialVersionUID = 3246462057176802969L;
    private int marginLeft;
    private int marginTop;
    private int position;
    private float ratio = CustomConstant.RATIO_ONE_HEIGHT;

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
