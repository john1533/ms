package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class MCEditText extends EditText {
    private MCEditText mthis = this;

    public MCEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MCEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCEditText(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == 0) {
            if (this.mthis.getLineCount() > 7) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (e.getAction() == 2) {
            if (this.mthis.getLineCount() > 7) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (e.getAction() == 1) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.onTouchEvent(e);
    }
}
