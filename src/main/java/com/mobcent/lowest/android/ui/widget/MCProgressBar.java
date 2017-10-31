package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.mobcent.lowest.base.utils.MCResource;

public class MCProgressBar extends ImageView {
    private static final String TAG = "MCProgressBar";
    private Drawable defaultDrawable = null;
    private int height = 0;
    private RotateAnimation mRotateAnimation;
    private Paint paint = null;
    private MCResource resource;
    private int width = 0;

    public MCProgressBar(Context context) {
        super(context);
        init();
    }

    public MCProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MCProgressBar(Context context, boolean isCreate) {
        super(context);
        init();
        this.width = (int) getResources().getDimension(this.resource.getDimenId("mc_forum_default_progress_bar_width"));
        this.height = (int) getResources().getDimension(this.resource.getDimenId("mc_forum_default_progress_bar_height"));
    }

    private void init() {
        this.resource = MCResource.getInstance(getContext());
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        setFocusable(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0);
        this.defaultDrawable = getBackground();
        if (this.defaultDrawable == null) {
            this.defaultDrawable = getResources().getDrawable(this.resource.getDrawableId("mc_forum_loading1"));
        }
        if (this.width == 0) {
            this.width = getWidth();
        }
        if (this.height == 0) {
            this.height = getHeight();
        }
        this.defaultDrawable.setBounds(0, 0, this.width, this.height);
        this.defaultDrawable.draw(canvas);
    }

    public void show() {
        setVisibility(0);
        this.mRotateAnimation = new RotateAnimation(0.0f, 3.6E8f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setDuration(1400000000);
        this.mRotateAnimation.setInterpolator(new LinearInterpolator());
        this.mRotateAnimation.setFillAfter(true);
        startAnimation(this.mRotateAnimation);
    }

    public void stop() {
        clearAnimation();
    }

    public void hide() {
        clearAnimation();
        setVisibility(8);
    }
}
