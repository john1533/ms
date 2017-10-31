package com.mobcent.lowest.android.ui.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

public class MCTouchUtil {
    public static void createTouchDelegate(View view, int expandTop, int expandBottom, int expandLeft, int expandRight) {
        final View parent = (View) view.getParent();
        final View view2 = view;
        final int i = expandTop;
        final int i2 = expandBottom;
        final int i3 = expandLeft;
        final int i4 = expandRight;
        parent.post(new Runnable() {
            public void run() {
                Rect r = new Rect();
                view2.getHitRect(r);
                r.top -= i;
                r.bottom += i2;
                r.left -= i3;
                r.right += i4;
                parent.setTouchDelegate(new TouchDelegate(r, view2));
            }
        });
    }

    public static void createTouchDelegate(final View view, final int expandTouchWidth) {
        final View parent = (View) view.getParent();
        parent.post(new Runnable() {
            public void run() {
                Rect r = new Rect();
                view.getHitRect(r);
                r.top -= expandTouchWidth;
                r.bottom += expandTouchWidth;
                r.left -= expandTouchWidth;
                r.right += expandTouchWidth;
                parent.setTouchDelegate(new TouchDelegate(r, view));
            }
        });
    }
}
