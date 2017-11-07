package com.mobcent.lowest.android.ui.widget.scaleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.Scroller;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;

public class ScaleView extends ImageView {
    public String TAG;
    private float beforeDis;
    private int bound;
    private float currentHeight;
    private Matrix currentMatrix;
    private float currentScale;
    private float currentWidth;
    private GestureDetector detector;
    private int mScrollX;
    private int mScrollY;
    private int maxVelocity;
    private float maxWidth;
    private float minWidth;
    private MODE mode;
    private ScaleViewListener onScaleViewListener;
    private float scaleViewHeight;
    private float scaleViewWidth;
    private Scroller scroller;
    private float startX;
    private float startY;
    private VelocityTracker vTracker;
    private ScaleViewPager viewPager;

    enum MODE {
        NONE,
        DRAG,
        ZOOM,
        SCROLL
    }

    class MyScaleViewListener extends SimpleOnGestureListener {
        MyScaleViewListener() {
        }

        public boolean onDoubleTap(MotionEvent e) {
            ScaleView.this.onDoubleTapDo(e);
            return super.onDoubleTap(e);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (ScaleView.this.onScaleViewListener != null) {
                ScaleView.this.onScaleViewListener.onSingleTapConfirmed();
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public interface ScaleViewListener {
        void onSingleTapConfirmed();
    }

    public ScaleViewListener getOnScaleViewListener() {
        return this.onScaleViewListener;
    }

    public void setOnScaleViewListener(ScaleViewListener onScaleViewListener) {
        this.onScaleViewListener = onScaleViewListener;
    }

    public ScaleViewPager getViewPager() {
        return this.viewPager;
    }

    public void setViewPager(ScaleViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public float getScaleViewWidth() {
        return this.scaleViewWidth;
    }

    public void setScaleViewWidth(float scaleViewWidth) {
        this.scaleViewWidth = scaleViewWidth;
        this.minWidth = scaleViewWidth / 2.0f;
        this.maxWidth = 3.0f * scaleViewWidth;
    }

    public float getScaleViewHeight() {
        return this.scaleViewHeight;
    }

    public void setScaleViewHeight(float scaleViewHeight) {
        this.scaleViewHeight = scaleViewHeight;
    }

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "ScaleView";
        this.bound = 2;
        this.mode = MODE.NONE;
        init(context);
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm != null) {
            this.currentMatrix = new Matrix();
            float bmWidth = (float) bm.getWidth();
            float bmHeight = (float) bm.getHeight();
            this.currentWidth = this.scaleViewWidth;
            this.currentScale = this.scaleViewWidth / bmWidth;
            this.currentHeight = this.currentScale * bmHeight;
            this.currentMatrix.postScale(this.currentScale, this.currentScale);
            if (this.currentHeight < this.scaleViewHeight) {
                this.currentMatrix.postTranslate(0.0f, (this.scaleViewHeight - this.currentHeight) / 2.0f);
            }
            setImageMatrix(this.currentMatrix);
        }
    }

    private void init(Context context) {
        this.maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.scroller = new Scroller(context);
        this.currentMatrix = new Matrix();
        this.detector = new GestureDetector(new MyScaleViewListener());
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.detector.onTouchEvent(event);
        if (this.currentMatrix == null) {
            return super.onTouchEvent(event);
        }
        if (this.vTracker == null) {
            this.vTracker = VelocityTracker.obtain();
        }
        this.vTracker.addMovement(event);
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                onTouchDown(event);
                break;
            case 1:
                onTouchUp(event);
                break;
            case 2:
                onTouchMove(event);
                break;
            case 3:
                recycleTracker();
                break;
            case 5:
                onTouchPointerDown(event);
                break;
            case 6:
                onTouchPointerUp(event);
                break;
        }
        return true;
    }

    private void onTouchDown(MotionEvent event) {
        this.mode = MODE.DRAG;
        this.startX = event.getX();
        this.startY = event.getY();
        this.scroller.abortAnimation();
    }

    private void onTouchPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            this.mode = MODE.ZOOM;
            this.beforeDis = getTwoPointDis(event);
        }
        this.scroller.abortAnimation();
    }

    private void onTouchMove(MotionEvent event) {
        if (this.mode == MODE.DRAG) {
            judgeViewScrollEnable(event, onDragDo(event));
        } else if (this.mode == MODE.ZOOM) {
            onZoomDo(event);
        }
    }

    private void onTouchPointerUp(MotionEvent event) {
        this.mode = MODE.NONE;
        recycleTracker();
    }

    private void onTouchUp(MotionEvent event) {
        this.mode = MODE.NONE;
        if (this.currentWidth < this.scaleViewWidth) {
            resetView();
        }
        VelocityTracker v = this.vTracker;
        v.computeCurrentVelocity(1000, (float) (this.maxVelocity * 2));
        float yV = v.getYVelocity();
        float xV = v.getXVelocity();
        if (this.currentHeight > this.scaleViewHeight || this.currentWidth > this.scaleViewWidth) {
            this.mScrollX = (int) getMatrixLeft();
            this.mScrollY = (int) getMatrixTop();
            this.scroller.fling(this.mScrollX, this.mScrollY, (int) xV, (int) yV, (int) (this.scaleViewWidth - this.currentWidth), 0, (int) (this.scaleViewHeight - this.currentHeight), 0);
            invalidate();
        }
        recycleTracker();
    }

    public void computeScroll() {
        if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
            int disX = this.scroller.getCurrX() - this.mScrollX;
            int disY = this.scroller.getCurrY() - this.mScrollY;
            this.mScrollX = this.scroller.getCurrX();
            this.mScrollY = this.scroller.getCurrY();
            move(disX, disY);
            invalidate();
        }
    }

    private void move(int dx, int dy) {
        if (getMatrixLeft() + ((float) dx) > 0.0f || getMatrixRight() + ((float) dx) < this.scaleViewWidth) {
            dx = 0;
        }
        if (getMatrixTop() + ((float) dy) > 0.0f || getMatrixBottom() + ((float) dx) < this.scaleViewHeight) {
            dy = 0;
        }
        this.currentMatrix.postTranslate((float) dx, (float) dy);
        setImageMatrix(this.currentMatrix);
    }

    private void recycleTracker() {
        if (this.vTracker != null) {
            this.vTracker.recycle();
            this.vTracker = null;
        }
    }

    public void resetView() {
        float dy;
        float scale = this.scaleViewWidth / this.currentWidth;
        this.currentMatrix.postScale(scale, scale, (getMatrixRight() + getMatrixLeft()) / 2.0f, (getMatrixBottom() + getMatrixTop()) / 2.0f);
        setImageMatrix(this.currentMatrix);
        this.currentWidth = this.scaleViewWidth;
        this.currentHeight *= scale;
        if (this.currentHeight > this.scaleViewHeight) {
            dy = -getMatrixTop();
        } else {
            dy = ((this.scaleViewHeight - this.currentHeight) / 2.0f) - getMatrixTop();
        }
        this.currentMatrix.postTranslate(-getMatrixLeft(), dy);
        setImageMatrix(this.currentMatrix);
    }

    private float onDragDo(MotionEvent event) {
        float disX = event.getX() - this.startX;
        float disY = event.getY() - this.startY;
        float currentRight = getMatrixRight() + disX;
        float currentTop = getMatrixTop() + disY;
        float currentBottom = getMatrixBottom() + disY;
        if (getMatrixLeft() + disX > 0.0f || currentRight < this.scaleViewWidth) {
            disX = 0.0f;
        }
        if (this.currentHeight < this.scaleViewHeight || currentTop > 0.0f || currentBottom < this.scaleViewHeight) {
            disY = 0.0f;
        }
        this.currentMatrix.postTranslate(disX, disY);
        setImageMatrix(this.currentMatrix);
        float diffX = event.getX() - this.startX;
        this.startX = event.getX();
        this.startY = event.getY();
        return diffX;
    }

    private void onZoomDo(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            float currentDis = getTwoPointDis(event);
            float scale = currentDis / this.beforeDis;
            if (this.currentWidth * scale >= this.minWidth && this.currentWidth * scale <= this.maxWidth) {
                PointF p = getMidPoint(event);
                this.currentMatrix.postScale(scale, scale, p.x, p.y);
                setImageMatrix(this.currentMatrix);
                this.currentWidth *= scale;
                this.currentHeight *= scale;
                this.beforeDis = currentDis;
                center(scale > CustomConstant.RATIO_ONE_HEIGHT);
            }
        }
    }

    private void center(boolean big) {
        float dx = 0.0f;
        float dy = 0.0f;
        if ((getMatrixLeft() > 0.0f || getMatrixRight() < this.scaleViewWidth) && this.currentWidth > this.scaleViewWidth) {
            dx = getMatrixLeft() > 0.0f ? 0.0f - getMatrixLeft() : this.scaleViewWidth - getMatrixRight();
        }
        if (this.currentHeight > this.scaleViewHeight && (getMatrixTop() > 0.0f || getMatrixBottom() < this.scaleViewHeight)) {
            dy = getMatrixTop() > 0.0f ? 0.0f - getMatrixTop() : this.scaleViewHeight - getMatrixBottom();
        }
        this.currentMatrix.postTranslate(dx, dy);
        setImageMatrix(this.currentMatrix);
    }

    private float getTwoPointDis(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt((x * x) + (y * y));
    }

    private PointF getMidPoint(MotionEvent event) {
        PointF p = new PointF();
        p.x = this.scaleViewWidth / 2.0f;
        p.y = this.scaleViewHeight / 2.0f;
        return p;
    }

    private float getMatrixLeft() {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        return values[2];
    }

    private float getMatrixTop() {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        return values[5];
    }

    private float getMatrixRight() {
        return getMatrixLeft() + this.currentWidth;
    }

    private float getMatrixBottom() {
        return getMatrixTop() + this.currentHeight;
    }

    private void judgeViewScrollEnable(MotionEvent event, float diffX) {
        if (this.viewPager == null) {
            return;
        }
        if (this.currentWidth <= this.scaleViewWidth) {
            setParentInterruptTouchEvent(true);
        } else if (diffX > 0.0f) {
            if (getMatrixLeft() >= ((float) (-this.bound))) {
                setParentInterruptTouchEvent(true);
            } else if (getMatrixRight() <= this.scaleViewWidth + ((float) this.bound)) {
                setParentInterruptTouchEvent(true);
            } else {
                setParentInterruptTouchEvent(false);
            }
        } else if (diffX >= 0.0f) {
            setParentInterruptTouchEvent(false);
        } else if (getMatrixRight() <= this.scaleViewWidth + ((float) this.bound)) {
            setParentInterruptTouchEvent(true);
        } else if (getMatrixLeft() >= ((float) (-this.bound))) {
            setParentInterruptTouchEvent(true);
        } else {
            setParentInterruptTouchEvent(false);
        }
    }

    private void setParentInterruptTouchEvent(boolean interrupt) {
        getParent().requestDisallowInterceptTouchEvent(!interrupt);
    }

    private void onDoubleTapDo(MotionEvent event) {
        if (this.currentWidth > this.scaleViewWidth) {
            resetView();
            return;
        }
        PointF p = getMidPoint(event);
        this.currentMatrix.postScale(2.0f, 2.0f, p.x, p.y);
        setImageMatrix(this.currentMatrix);
        this.currentWidth *= 2.0f;
        this.currentHeight *= 2.0f;
    }
}
