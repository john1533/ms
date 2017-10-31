package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;

public class ScaleHeaderScrollView extends ScrollView {
    private static final int BACK_SCALE = 0;
    private final int MODE_DRAG = 1;
    private Bitmap bmp;
    private Matrix currentMatrix = new Matrix();
    private Matrix defaultMatrix = new Matrix();
    private int displayWidth;
    private ImageView imageView;
    private float imgHeight;
    private float imgWidth;
    private boolean isBacking = false;
    private boolean isHaveHead = false;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    float scale = ((ScaleHeaderScrollView.this.scaleY / 2.0f) + ScaleHeaderScrollView.this.imgHeight) / ScaleHeaderScrollView.this.imgHeight;
                    if (ScaleHeaderScrollView.this.scaleY <= 0.0f) {
                        ScaleHeaderScrollView.this.scaleY = 0.0f;
                        ScaleHeaderScrollView.this.imageView.setLayoutParams(new LayoutParams((int) ScaleHeaderScrollView.this.imgWidth, (int) ScaleHeaderScrollView.this.imgHeight));
                        ScaleHeaderScrollView.this.matrix.set(ScaleHeaderScrollView.this.defaultMatrix);
                        ScaleHeaderScrollView.this.imageView.setImageMatrix(ScaleHeaderScrollView.this.matrix);
                        ScaleHeaderScrollView.this.isBacking = false;
                        break;
                    }
                    Log.e("---", "log=1");
                    ScaleHeaderScrollView.this.isBacking = true;
                    ScaleHeaderScrollView.this.matrix.set(ScaleHeaderScrollView.this.currentMatrix);
                    ScaleHeaderScrollView.this.imageView.setLayoutParams(new LayoutParams((int) (ScaleHeaderScrollView.this.imgWidth * scale), (int) (ScaleHeaderScrollView.this.imgHeight * scale)));
                    ScaleHeaderScrollView.this.matrix.postScale(scale, scale, ScaleHeaderScrollView.this.imgWidth / 2.0f, 0.0f);
                    ScaleHeaderScrollView.this.imageView.setImageMatrix(ScaleHeaderScrollView.this.matrix);
                    ScaleHeaderScrollView.this.scaleY = (ScaleHeaderScrollView.this.scaleY / 2.0f) - CustomConstant.RATIO_ONE_HEIGHT;
                    ScaleHeaderScrollView.this.mHandler.sendEmptyMessageDelayed(0, 20);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Matrix matrix = new Matrix();
    private int mode = 0;
    private float scaleY = 0.0f;
    private PointF startPoint = new PointF();

    public ScaleHeaderScrollView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ScaleHeaderScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public ScaleHeaderScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    public void addHeaderView(View headerView) {
        super.addView(headerView, 0);
    }

    public void scaleHeaderImage(ImageView mImageView) {
        this.imageView = mImageView;
        this.bmp = ((BitmapDrawable) this.imageView.getDrawable()).getBitmap();
        float scale = ((float) this.displayWidth) / ((float) this.bmp.getWidth());
        this.matrix.postScale(scale, scale, 0.0f, 0.0f);
        this.imageView.setImageMatrix(this.matrix);
        this.defaultMatrix.set(this.matrix);
        this.imgHeight = ((float) this.bmp.getHeight()) * scale;
        this.imgWidth = ((float) this.bmp.getWidth()) * scale;
        this.imageView.setLayoutParams(new LayoutParams((int) this.imgWidth, (int) this.imgHeight));
        this.isHaveHead = true;
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        this.displayWidth = dm.widthPixels;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!this.isHaveHead) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                if (!this.isBacking) {
                    int[] location = new int[2];
                    this.imageView.getLocationInWindow(location);
                    if (location[1] >= 0) {
                        this.mode = 1;
                        this.currentMatrix.set(this.imageView.getImageMatrix());
                        this.startPoint.set(event.getX(), event.getY());
                        break;
                    }
                }
                return super.onInterceptTouchEvent(event);
//                break;
            case 1:
                this.mHandler.sendEmptyMessage(0);
                break;
            case 2:
                if (this.mode == 1) {
                    float dy = event.getY() - this.startPoint.y;
                    if (((double) ((dy / 2.0f) + this.imgHeight)) <= 1.5d * ((double) this.imgHeight)) {
                        this.matrix.set(this.currentMatrix);
                        float scale = ((dy / 2.0f) + this.imgHeight) / this.imgHeight;
                        if (dy > 0.0f) {
                            this.scaleY = dy;
                            this.imageView.setLayoutParams(new LayoutParams((int) (this.imgWidth * scale), (int) (this.imgHeight * scale)));
                            this.matrix.postScale(scale, scale, this.imgWidth / 2.0f, 0.0f);
                            this.imageView.setImageMatrix(this.matrix);
                            break;
                        }
                    }
                }
                return super.onInterceptTouchEvent(event);
//                break;
            case 6:
                break;
        }
        this.mode = 0;
        return super.onInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isHaveHead) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                if (!this.isBacking) {
                    int[] location = new int[2];
                    this.imageView.getLocationInWindow(location);
                    if (location[1] >= 0) {
                        this.mode = 1;
                        this.currentMatrix.set(this.imageView.getImageMatrix());
                        this.startPoint.set(event.getX(), event.getY());
                        break;
                    }
                }
                return super.onTouchEvent(event);
//                break;
            case 1:
                this.mHandler.sendEmptyMessage(0);
                break;
            case 2:
                if (this.mode == 1) {
                    float dy = event.getY() - this.startPoint.y;
                    if (((double) ((dy / 2.0f) + this.imgHeight)) <= 1.5d * ((double) this.imgHeight)) {
                        this.matrix.set(this.currentMatrix);
                        float scale = ((dy / 2.0f) + this.imgHeight) / this.imgHeight;
                        if (dy > 0.0f) {
                            this.scaleY = dy;
                            this.imageView.setLayoutParams(new LayoutParams((int) (this.imgWidth * scale), (int) (this.imgHeight * scale)));
                            this.matrix.postScale(scale, scale, this.imgWidth / 2.0f, 0.0f);
                            this.imageView.setImageMatrix(this.matrix);
                            break;
                        }
                    }
                }
                return super.onTouchEvent(event);
//                break;
            case 6:
                break;
        }
        this.mode = 0;
        return super.onTouchEvent(event);
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
