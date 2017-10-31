package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.mobcent.lowest.base.utils.MCPhoneUtil;

public class MCTouchSlidHelper {
    private TouchSlideDelegate delegate;
    private float disX = 0.0f;
    private float disY = 0.0f;
    private boolean isSliding = false;
    private int slideMax;
    private float startX;
    private float startY;
    private float touchSlop;

    public interface TouchSlideDelegate {
        boolean isSlideAble();

        boolean isSlideFullScreen();

        void slideExit();
    }

    public TouchSlideDelegate getDelegate() {
        return this.delegate;
    }

    public void setDelegate(TouchSlideDelegate delegate) {
        this.delegate = delegate;
    }

    public MCTouchSlidHelper(Context context) {
        this.slideMax = MCPhoneUtil.getDisplayWidth(context.getApplicationContext()) / 3;
        this.touchSlop = (float) ViewConfiguration.get(context.getApplicationContext()).getScaledTouchSlop();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getDelegate() == null || !getDelegate().isSlideAble()) {
            return false;
        }
        switch (ev.getAction()) {
            case 0:
                resetSlidParams();
                this.startX = ev.getX();
                this.startY = ev.getY();
                break;
            case 1:
                if (this.isSliding) {
                    if (Math.abs(this.disX) <= Math.abs(this.disY) || this.disX <= ((float) this.slideMax)) {
                        return false;
                    }
                    getDelegate().slideExit();
                    resetSlidParams();
                    return true;
                }
                break;
            case 2:
                this.disX = ev.getX() - this.startX;
                this.disY = ev.getY() - this.startY;
                if (Math.abs(this.disX) > Math.abs(this.disY) && this.disX > this.touchSlop) {
                    if (getDelegate().isSlideFullScreen()) {
                        this.isSliding = true;
                        return true;
                    } else if (this.startX <= this.touchSlop) {
                        this.isSliding = true;
                        return true;
                    }
                }
                break;
            case 3:
                resetSlidParams();
                break;
        }
        return false;
    }

    private void resetSlidParams() {
        this.disX = 0.0f;
        this.disY = 0.0f;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.isSliding = false;
    }
}
