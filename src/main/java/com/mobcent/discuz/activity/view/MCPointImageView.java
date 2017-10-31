package com.mobcent.discuz.activity.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;

public class MCPointImageView extends ImageView {
    static final float SCALE_RATE = 1.25f;
    float _dy;
    private float baseValue;
    private GestureDetector gestureScanner;
    private Bitmap image;
    private int imageHeight;
    private int imageWidth;
    private Matrix mBaseMatrix;
    private MCPointImageViewSingleTapListener mCPointImageViewSingleTapListener;
    private final Matrix mDisplayMatrix;
    public Handler mHandler;
    private final float[] mMatrixValues;
    private float mMaxZoom;
    private float mMinZoom;
    private Matrix mSuppMatrix;
    private int mThisHeight;
    private int mThisWidth;
    private float originalScale;
    private float scaleRate;
    private int screenHeight;
    private int screenWidth;
    private MCImageViewPager viewPager;

    public interface MCPointImageViewSingleTapListener {
        void onSingleTap(MotionEvent motionEvent);
    }

    private class MySimpleGesture extends SimpleOnGestureListener {
        private MySimpleGesture() {
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (MCPointImageView.this.mCPointImageViewSingleTapListener != null) {
                MCPointImageView.this.mCPointImageViewSingleTapListener.onSingleTap(e);
            }
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (MCPointImageView.this.getScale() > MCPointImageView.this.getScaleRate()) {
                MCPointImageView.this.changeViewPagerState(false);
                MCPointImageView.this.zoomTo(MCPointImageView.this.getScaleRate(), (float) (MCPointImageView.this.screenWidth / 2), (float) (MCPointImageView.this.screenHeight / 2), 200.0f);
            } else {
                MCPointImageView.this.changeViewPagerState(true);
                MCPointImageView.this.zoomTo(CustomConstant.RATIO_ONE_HEIGHT, (float) (MCPointImageView.this.screenWidth / 2), (float) (MCPointImageView.this.screenHeight / 2), 200.0f);
            }
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int count = 1;
            if (MCPointImageView.this.viewPager != null) {
                count = MCPointImageView.this.viewPager.getAdapter().getCount();
            }
            float[] v = new float[9];
            MCPointImageView.this.getImageMatrix().getValues(v);
            float width = MCPointImageView.this.getScale() * ((float) MCPointImageView.this.getImageWidth());
            float height = MCPointImageView.this.getScale() * ((float) MCPointImageView.this.getImageHeight());
            if (((int) width) > MCPointImageView.this.screenWidth || ((int) height) > MCPointImageView.this.screenHeight) {
                float left = v[2];
                float right = left + width;
                Rect r = new Rect();
                MCPointImageView.this.getGlobalVisibleRect(r);
                if (distanceX > 0.0f) {
                    if (r.left > 0 && count > 1) {
                        MCPointImageView.this.changeViewPagerState(true);
                    } else if (right > ((float) MCPointImageView.this.screenWidth) || count <= 1) {
                        MCPointImageView.this.changeViewPagerState(false);
                        MCPointImageView.this.postTranslate(-distanceX, -distanceY);
                    } else {
                        MCPointImageView.this.changeViewPagerState(true);
                    }
                } else if (distanceX < 0.0f) {
                    if (r.right < MCPointImageView.this.screenWidth && count > 1) {
                        MCPointImageView.this.changeViewPagerState(true);
                    } else if (left != 0.0f || count <= 1) {
                        MCPointImageView.this.changeViewPagerState(false);
                        MCPointImageView.this.postTranslate(-distanceX, -distanceY);
                    } else {
                        MCPointImageView.this.changeViewPagerState(true);
                    }
                }
            } else {
                MCPointImageView.this.changeViewPagerState(true);
            }
            return false;
        }
    }

    public MCPointImageView(Context context) {
        this(context, null);
    }

    public MCPointImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayMatrix = new Matrix();
        this.mMatrixValues = new float[9];
        this.image = null;
        this.mThisWidth = -1;
        this.mThisHeight = -1;
        this.mMaxZoom = 2.0f;
        this.mHandler = new Handler();
        this._dy = 0.0f;
        init(context);
    }

    public void setViewPager(MCImageViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setPointImageViewSingleTapListener(MCPointImageViewSingleTapListener mCPointImageViewSingleTapListener) {
        this.mCPointImageViewSingleTapListener = mCPointImageViewSingleTapListener;
    }

    private void arithScaleRate() {
        this.scaleRate = Math.min(((float) this.screenWidth) / ((float) this.imageWidth), ((float) this.screenHeight) / ((float) this.imageHeight));
    }

    public float getScaleRate() {
        return this.scaleRate;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void onDraw(Canvas canvas) {
        center(true, true);
        super.onDraw(canvas);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        event.startTracking();
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !event.isTracking() || event.isCanceled() || getScale() <= CustomConstant.RATIO_ONE_HEIGHT) {
            return super.onKeyUp(keyCode, event);
        }
        zoomTo(CustomConstant.RATIO_ONE_HEIGHT);
        return true;
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (bitmap != null) {
            this.image = bitmap;
            this.imageHeight = this.image.getHeight();
            this.imageWidth = this.image.getWidth();
            arithScaleRate();
            zoomTo(this.scaleRate, ((float) this.screenWidth) / 2.0f, ((float) this.screenHeight) / 2.0f);
            layoutToCenter();
        }
    }

    public void center(boolean horizontal, boolean vertical) {
        if (this.image != null) {
            Matrix m = getImageViewMatrix();
            RectF rect = new RectF(0.0f, 0.0f, (float) this.image.getWidth(), (float) this.image.getHeight());
            m.mapRect(rect);
            float height = rect.height();
            float width = rect.width();
            float deltaX = 0.0f;
            float deltaY = 0.0f;
            if (vertical) {
                int viewHeight = getHeight();
                if (height < ((float) viewHeight)) {
                    deltaY = ((((float) viewHeight) - height) / 2.0f) - rect.top;
                } else if (rect.top > 0.0f) {
                    deltaY = -rect.top;
                } else if (rect.bottom < ((float) viewHeight)) {
                    deltaY = ((float) getHeight()) - rect.bottom;
                }
            }
            if (horizontal) {
                int viewWidth = getWidth();
                if (width < ((float) viewWidth)) {
                    deltaX = ((((float) viewWidth) - width) / 2.0f) - rect.left;
                } else if (rect.left > 0.0f) {
                    deltaX = -rect.left;
                } else if (rect.right < ((float) viewWidth)) {
                    deltaX = ((float) viewWidth) - rect.right;
                }
            }
            postTranslate(deltaX, deltaY);
            setImageMatrix(getImageViewMatrix());
        }
    }

    private void init(Context context) {
        this.gestureScanner = new GestureDetector(new MySimpleGesture());
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.screenWidth = dm.widthPixels;
        this.screenHeight = dm.heightPixels;
        setScaleType(ScaleType.MATRIX);
    }

    public void layoutToCenter() {
        float fill_width = ((float) this.screenWidth) - (((float) this.imageWidth) * getScale());
        float fill_height = ((float) this.screenHeight) - (((float) this.imageHeight) * getScale());
        float tran_width = 0.0f;
        float tran_height = 0.0f;
        if (fill_width > 0.0f) {
            tran_width = fill_width / 2.0f;
        }
        if (fill_height > 0.0f) {
            tran_height = fill_height / 2.0f;
        }
        postTranslate(tran_width, tran_height);
        setImageMatrix(getImageViewMatrix());
    }

    public float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        this.mMinZoom = (((float) this.screenWidth) / 2.0f) / ((float) this.imageWidth);
        return this.mMatrixValues[whichValue];
    }

    public float getScale(Matrix matrix) {
        return getValue(matrix, 0);
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    public Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    public float maxZoom() {
        if (this.image == null) {
            return CustomConstant.RATIO_ONE_HEIGHT;
        }
        return Math.max(((float) this.image.getWidth()) / ((float) this.mThisWidth), ((float) this.image.getHeight()) / ((float) this.mThisHeight)) * 4.0f;
    }

    public void zoomTo(float scale, float centerX, float centerY) {
        if (scale > this.mMaxZoom) {
            scale = this.mMaxZoom;
        } else if (scale < this.mMinZoom) {
            scale = this.mMinZoom;
        }
        float deltaScale = scale / getScale();
        this.mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    public void zoomTo(float scale, float centerX, float centerY, float durationMs) {
        final float incrementPerMs = (scale - getScale()) / durationMs;
        final float oldScale = getScale();
        final long startTime = System.currentTimeMillis();
        final float f = durationMs;
        final float f2 = centerX;
        final float f3 = centerY;
        this.mHandler.post(new Runnable() {
            public void run() {
                float currentMs = Math.min(f, (float) (System.currentTimeMillis() - startTime));
                MCPointImageView.this.zoomTo(oldScale + (incrementPerMs * currentMs), f2, f3);
                if (currentMs < f) {
                    MCPointImageView.this.mHandler.post(this);
                }
            }
        });
    }

    public void zoomTo(float scale) {
        zoomTo(scale, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
    }

    public void zoomToPoint(float scale, float pointX, float pointY) {
        float cx = ((float) getWidth()) / 2.0f;
        float cy = ((float) getHeight()) / 2.0f;
        panBy(cx - pointX, cy - pointY);
        zoomTo(scale, cx, cy);
    }

    public void zoomIn() {
        zoomIn(SCALE_RATE);
    }

    public void zoomOut() {
        zoomOut(SCALE_RATE);
    }

    public void zoomIn(float rate) {
        if (getScale() < this.mMaxZoom && getScale() > this.mMinZoom && this.image != null) {
            this.mSuppMatrix.postScale(rate, rate, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
            setImageMatrix(getImageViewMatrix());
        }
    }

    public void zoomOut(float rate) {
        if (this.image != null) {
            float cx = ((float) getWidth()) / 2.0f;
            float cy = ((float) getHeight()) / 2.0f;
            Matrix tmp = new Matrix(this.mSuppMatrix);
            tmp.postScale(CustomConstant.RATIO_ONE_HEIGHT / rate, CustomConstant.RATIO_ONE_HEIGHT / rate, cx, cy);
            if (getScale(tmp) < CustomConstant.RATIO_ONE_HEIGHT) {
                this.mSuppMatrix.setScale(CustomConstant.RATIO_ONE_HEIGHT, CustomConstant.RATIO_ONE_HEIGHT, cx, cy);
            } else {
                this.mSuppMatrix.postScale(CustomConstant.RATIO_ONE_HEIGHT / rate, CustomConstant.RATIO_ONE_HEIGHT / rate, cx, cy);
            }
            setImageMatrix(getImageViewMatrix());
            center(true, true);
        }
    }

    public void postTranslate(float dx, float dy) {
        this.mSuppMatrix.postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }

    public void postTranslateDur(float dy, float durationMs) {
        this._dy = 0.0f;
        final float incrementPerMs = dy / durationMs;
        final long startTime = System.currentTimeMillis();
        final float f = durationMs;
        this.mHandler.post(new Runnable() {
            public void run() {
                float currentMs = Math.min(f, (float) (System.currentTimeMillis() - startTime));
                MCPointImageView.this.postTranslate(0.0f, (incrementPerMs * currentMs) - MCPointImageView.this._dy);
                MCPointImageView.this._dy = incrementPerMs * currentMs;
                if (currentMs < f) {
                    MCPointImageView.this.mHandler.post(this);
                }
            }
        });
    }

    public void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.gestureScanner.onTouchEvent(event);
        switch (event.getAction()) {
            case 0:
                this.baseValue = 0.0f;
                this.originalScale = getScale();
                break;
            case 1:
                if (getScale() < getScaleRate()) {
                    zoomTo(getScaleRate(), (float) (this.screenWidth / 2), (float) (this.screenHeight / 2), 200.0f);
                    break;
                }
                break;
            case 2:
                if (event.getPointerCount() == 2) {
                    changeViewPagerState(false);
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);
                    float value = (float) Math.sqrt((double) ((x * x) + (y * y)));
                    if (this.baseValue != 0.0f) {
                        zoomTo(this.originalScale * (value / this.baseValue), event.getX(1) + x, event.getY(1) + y);
                        break;
                    }
                    this.baseValue = value;
                    break;
                }
                break;
        }
        return true;
    }

    public void changeViewPagerState(boolean isFlag) {
        if (this.viewPager != null) {
            this.viewPager.setScroll(isFlag);
        }
    }
}
