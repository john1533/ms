package com.mobcent.lowest.android.ui.widget.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import com.mobcent.lowest.android.ui.module.ad.widget.AdProgress;
import com.mobcent.lowest.android.ui.module.ad.widget.BasePicView;
import com.mobcent.lowest.android.ui.widget.gif.GifCache.GifCallback;
import com.mobcent.lowest.base.model.BitmapModel;
import com.mobcent.lowest.base.utils.MCBitmapUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GifView extends BasePicView implements GifAction {
    private String TAG;
    private GifImageType animationType;
    private Context context;
    private Bitmap currentImage;
    private boolean doOverScreen;
    private DrawThread drawThread;
    private GestureDetector gestureScanner;
    private GifDecoder gifDecoder;
    private GifViewListener gifViewListener;
    private String imageUrl;
    private InputStream is;
    private boolean isLikeScreen;
    private boolean isRun;
    private int maxLen;
    private boolean noWidth;
    private boolean pause;
    private int proportion;
    private Rect rect;
    private Handler redrawHandler;
    private int showHeight;
    private int showWidth;
    private int showWidthReal;

    private class DrawThread extends Thread {
        private DrawThread() {
        }

        public void run() {
            if (GifView.this.gifDecoder != null) {
                while (GifView.this.isRun) {
                    if (GifView.this.pause) {
                        SystemClock.sleep(10);
                    } else {
                        GifFrame frame = GifView.this.gifDecoder.next();
                        GifView.this.currentImage = frame.image;
                        long sp = (long) frame.delay;
                        if (GifView.this.redrawHandler != null) {
                            GifView.this.redrawHandler.sendMessage(GifView.this.redrawHandler.obtainMessage());
                            SystemClock.sleep(sp);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    public enum GifImageType {
        WAIT_FINISH(0),
        SYNC_DECODER(1),
        COVER(2);
        
        final int nativeInt;

        private GifImageType(int i) {
            this.nativeInt = i;
        }
    }

    public interface GifViewListener {
        void done(boolean z);

        void onSingleTap(MotionEvent motionEvent);
    }

    private class MySimpleGesture extends SimpleOnGestureListener {
        private MySimpleGesture() {
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (GifView.this.gifViewListener != null) {
                GifView.this.gifViewListener.onSingleTap(e);
            }
            return true;
        }
    }

    public GifView(Context context) {
        super(context);
        this.TAG = "GifView";
        this.gifDecoder = null;
        this.currentImage = null;
        this.isRun = true;
        this.pause = false;
        this.showWidth = -1;
        this.showHeight = -1;
        this.rect = null;
        this.drawThread = null;
        this.animationType = GifImageType.SYNC_DECODER;
        this.noWidth = false;
        this.isLikeScreen = false;
        this.doOverScreen = false;
        this.proportion = 1;
        this.maxLen = -1;
        this.showWidthReal = -1;
        this.redrawHandler = new Handler() {
            public void handleMessage(Message msg) {
                GifView.this.invalidate();
            }
        };
        this.context = context;
        initData();
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        initData();
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "GifView";
        this.gifDecoder = null;
        this.currentImage = null;
        this.isRun = true;
        this.pause = false;
        this.showWidth = -1;
        this.showHeight = -1;
        this.rect = null;
        this.drawThread = null;
        this.animationType = GifImageType.SYNC_DECODER;
        this.noWidth = false;
        this.isLikeScreen = false;
        this.doOverScreen = false;
        this.proportion = 1;
        this.maxLen = -1;
        this.showWidthReal = -1;
//        this.redrawHandler = /* anonymous class already generated */;
        this.context = context;
        initData();
    }

    public void setGifViewListener(GifViewListener gifViewListener) {
        this.gifViewListener = gifViewListener;
    }

    private void initData() {
        this.gestureScanner = new GestureDetector(new MySimpleGesture());
    }

    private void setGifDecoderImage(byte[] gif) {
        if (this.gifDecoder != null) {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.isRun = true;
        this.pause = false;
        this.gifDecoder = new GifDecoder(gif, (GifAction) this);
        this.gifDecoder.start();
    }

    private void setGifDecoderImage(InputStream is) {
        this.is = is;
        if (this.gifDecoder != null) {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.isRun = true;
        this.pause = false;
        this.gifDecoder = new GifDecoder(is, (GifAction) this);
        this.gifDecoder.start();
    }

    public void setGifImage(byte[] gif) {
        setGifDecoderImage(gif);
    }

    public void setGifImage(InputStream is) {
        setGifDecoderImage(is);
    }

    public void setGifImage(String imageUrl) {
        this.imageUrl = imageUrl;
        try {
            setGifDecoderImage((InputStream) new URL(imageUrl).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void setGifImage(int resId) {
        setGifDecoderImage(getResources().openRawResource(resId));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.gifDecoder != null) {
            if (this.currentImage == null) {
                this.currentImage = this.gifDecoder.getImage();
            }
            if (this.currentImage != null) {
                int saveCount = canvas.getSaveCount();
                canvas.save();
                canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
                if (this.showWidth == -1) {
                    canvas.drawBitmap(this.currentImage, 0.0f, 0.0f, null);
                } else {
                    if (this.isLikeScreen) {
                        setLikeScreenDimension(this.currentImage);
                    }
                    canvas.drawBitmap(this.currentImage, null, this.rect, null);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w;
        int h;
        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();
        if (this.gifDecoder == null) {
            w = 1;
            h = 1;
        } else {
            w = this.gifDecoder.width;
            h = this.gifDecoder.height;
        }
        h += ptop + pbottom;
        w = Math.max(w + (pleft + pright), getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());
        int widthSize = resolveSize(w, widthMeasureSpec);
        if (widthSize <= 1) {
            this.noWidth = true;
        } else {
            this.noWidth = false;
        }
        setMeasuredDimension(widthSize, resolveSize(h, heightMeasureSpec));
    }

    public void showCover() {
        if (this.gifDecoder != null) {
            this.pause = true;
            this.currentImage = this.gifDecoder.getImage();
            invalidate();
        }
    }

    public void showAnimation() {
        if (this.pause) {
            this.pause = false;
        }
    }

    public void setGifImageType(GifImageType type) {
        if (this.gifDecoder == null) {
            this.animationType = type;
        }
    }

    public void setShowDimension(int width, int height) {
        if (width > 0 && height > 0) {
            this.showWidth = width;
            this.showHeight = height;
            this.rect = new Rect();
            this.rect.left = 0;
            this.rect.top = 0;
            this.rect.right = width;
            this.rect.bottom = height;
        }
    }

    public void setIsLikeScreen(boolean isLikeScreen) {
        this.isLikeScreen = isLikeScreen;
        this.showWidth = 0;
    }

    public void setIsLikeScreen(boolean isLikeScreen, boolean isOverScreen) {
        this.isLikeScreen = isLikeScreen;
        this.showWidth = 0;
        this.doOverScreen = isOverScreen;
    }

    public void setIsLikeScreen(boolean isLikeScreen, boolean isOverScreen, int proportion) {
        this.isLikeScreen = isLikeScreen;
        this.showWidth = 0;
        this.doOverScreen = isOverScreen;
        this.proportion = proportion;
    }

    public void setIsLikeScreen(boolean isLikeScreen, boolean isOverScreen, int proportion, int maxLen) {
        this.isLikeScreen = isLikeScreen;
        this.showWidth = 0;
        this.doOverScreen = isOverScreen;
        this.proportion = proportion;
        this.maxLen = maxLen;
    }

    private void setLikeScreenDimension(Bitmap currentBitmap) {
        BitmapModel bitmapModel = MCBitmapUtil.getBitmapWidHeightByScreen(this.context, currentBitmap, this.doOverScreen);
        this.rect = new Rect();
        this.rect.left = 0;
        this.rect.top = 0;
        if (this.maxLen > 0 && bitmapModel.getHeight() / this.proportion > this.maxLen) {
            bitmapModel.setWidth((this.maxLen * bitmapModel.getWidth()) / bitmapModel.getHeight());
            bitmapModel.setHeight(this.maxLen);
        }
        this.rect.right = bitmapModel.getWidth() / this.proportion;
        this.rect.bottom = bitmapModel.getHeight() / this.proportion;
    }

    public void parseOk(boolean parseStatus, int frameIndex) {
        if (this.gifDecoder == null) {
            return;
        }
        if (this.gifDecoder.getFrameCount() == 0) {
            parseFail(true, true);
            return;
        }
        if (this.noWidth) {
            this.noWidth = false;
            parseFail(false, true);
        } else {
            parseFail(false, false);
        }
        if (parseStatus && this.gifDecoder != null) {
            switch (this.animationType) {
                case WAIT_FINISH:
                    if (frameIndex != -1) {
                        return;
                    }
                    if (this.gifDecoder.getFrameCount() > 1) {
                        new DrawThread().start();
                        return;
                    } else {
                        reDraw();
                        return;
                    }
                case COVER:
                    if (frameIndex == 1) {
                        this.currentImage = this.gifDecoder.getImage();
                        reDraw();
                        return;
                    } else if (frameIndex != -1) {
                        return;
                    } else {
                        if (this.gifDecoder.getFrameCount() <= 1) {
                            reDraw();
                            return;
                        } else if (this.drawThread == null) {
                            this.drawThread = new DrawThread();
                            this.drawThread.start();
                            return;
                        } else {
                            return;
                        }
                    }
                case SYNC_DECODER:
                    if (frameIndex == 1) {
                        this.currentImage = this.gifDecoder.getImage();
                        reDraw();
                        return;
                    } else if (frameIndex == -1) {
                        reDraw();
                        return;
                    } else if (this.drawThread == null) {
                        this.drawThread = new DrawThread();
                        this.drawThread.start();
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private void reDraw() {
        if (this.redrawHandler != null) {
            this.redrawHandler.sendMessage(this.redrawHandler.obtainMessage());
        }
    }

    public void free() {
        this.isRun = false;
        this.pause = true;
        try {
            if (this.gifDecoder != null) {
                this.gifDecoder.interrupt();
                this.gifDecoder.free();
                this.gifDecoder = null;
            }
            if (this.drawThread != null) {
                this.drawThread.interrupt();
                this.drawThread = null;
            }
            if (this.currentImage != null && !this.currentImage.isRecycled()) {
                this.currentImage.recycle();
                this.currentImage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseFail(boolean isDel, boolean isFail) {
        if (isDel) {
            GifCache.removeInputStream(this.is);
        }
        if (this.gifViewListener != null) {
            this.gifViewListener.done(isFail);
        }
    }

    public GifDecoder getGifDecoder() {
        return this.gifDecoder;
    }

    public void setGifDecoder(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
    }

    protected void loadPic(String url, final AdProgress progress) {
        if (progress != null) {
            progress.show();
        }
        new GifCache(getContext(), url, new GifCallback() {
            public void onGifLoaded(InputStream is, String url) {
                if (progress != null) {
                    progress.hide();
                }
                if (is != null) {
                    GifView.this.setGifViewListener(new GifViewListener() {
                        public void onSingleTap(MotionEvent e) {
                        }

                        public void done(final boolean isFail) {
                            GifView.this.handler.post(new Runnable() {
                                public void run() {
                                    if (!isFail && GifView.this.getGifDecoder() != null) {
                                        LayoutParams lps = GifView.this.getLayoutParams();
                                        if (GifView.this.showWidthReal != -1) {
                                            GifView.this.setShowDimension(GifView.this.showWidthReal, (int) (((float) GifView.this.showWidthReal) * AdConstant.RADIO_IMG_AD));
                                        } else if (lps != null) {
                                            if (lps.width == -1) {
                                                lps.width = MCPhoneUtil.getDisplayWidth(GifView.this.getContext());
                                            }
                                            GifView.this.setShowDimension(lps.width, lps.height);
                                        }
                                    }
                                }
                            });
                        }
                    });
                    GifView.this.setVisibility(0);
                    GifView.this.setGifImageType(GifImageType.SYNC_DECODER);
                    GifView.this.setGifImage(is);
                }
            }
        }).execute(new Void[0]);
    }

    protected void recyclePic() {
        free();
    }

    protected void setShowWidthReal(int showWidth) {
        this.showWidthReal = showWidth;
    }
}
