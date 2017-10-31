package com.mobcent.discuz.activity.widget.cropImage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Handler;
import android.widget.Toast;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.concurrent.CountDownLatch;

public class CropImage {
    private Bitmap mBitmap;
    private Context mContext;
    public HighlightView mCrop;
    private Handler mHandler = new Handler();
    private CropImageView mImageView;
    Runnable mRunFaceDetection = new Runnable() {
        Face[] mFaces = new Face[3];
        Matrix mImageMatrix;
        int mNumFaces;
        float mScale = CustomConstant.RATIO_ONE_HEIGHT;

        private void handleFace(Face f) {
            PointF midPoint = new PointF();
            int r = ((int) (f.eyesDistance() * this.mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= this.mScale;
            midPoint.y *= this.mScale;
            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            Rect imageRect = new Rect(0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight());
            RectF faceRect = new RectF((float) midX, (float) midY, (float) midX, (float) midY);
            faceRect.inset((float) (-r), (float) (-r));
            if (faceRect.left < 0.0f) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }
            if (faceRect.top < 0.0f) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }
            if (faceRect.right > ((float) imageRect.right)) {
                faceRect.inset(faceRect.right - ((float) imageRect.right), faceRect.right - ((float) imageRect.right));
            }
            if (faceRect.bottom > ((float) imageRect.bottom)) {
                faceRect.inset(faceRect.bottom - ((float) imageRect.bottom), faceRect.bottom - ((float) imageRect.bottom));
            }
            hv.setup(this.mImageMatrix, imageRect, faceRect, false, true);
            CropImage.this.mImageView.add(hv);
        }

        private void makeDefault() {
            HighlightView hv = new HighlightView(CropImage.this.mImageView);
            if (CropImage.this.mBitmap != null) {
                int width = CropImage.this.mBitmap.getWidth();
                int height = CropImage.this.mBitmap.getHeight();
                int cropWidth = (Math.min(width, height) * 4) / 5;
                int cropHeight = cropWidth;
                int x = (width - cropWidth) / 2;
                int y = (height - cropHeight) / 2;
                hv.setup(this.mImageMatrix, new Rect(0, 0, width, height), new RectF((float) x, (float) y, (float) (x + cropWidth), (float) (y + cropHeight)), false, true);
                CropImage.this.mImageView.add(hv);
            }
        }

        private Bitmap prepareBitmap() {
            if (CropImage.this.mBitmap == null) {
                return null;
            }
            if (CropImage.this.mBitmap.getWidth() > 256) {
                this.mScale = 256.0f / ((float) CropImage.this.mBitmap.getWidth());
            }
            Matrix matrix = new Matrix();
            matrix.setScale(this.mScale, this.mScale);
            return Bitmap.createBitmap(CropImage.this.mBitmap, 0, 0, CropImage.this.mBitmap.getWidth(), CropImage.this.mBitmap.getHeight(), matrix, true);
        }

        public void run() {
            this.mImageMatrix = CropImage.this.mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();
            this.mScale = CustomConstant.RATIO_ONE_HEIGHT / this.mScale;
            if (faceBitmap != null) {
                this.mNumFaces = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), this.mFaces.length).findFaces(faceBitmap, this.mFaces);
            }
            if (!(faceBitmap == null || faceBitmap == CropImage.this.mBitmap)) {
                faceBitmap.recycle();
            }
            CropImage.this.mHandler.post(new Runnable() {
                public void run() {
                    boolean z;
                    CropImage cropImage = CropImage.this;
//                    if (AnonymousClass2.this.mNumFaces > 1) {
//                        z = true;
//                    } else {
//                        z = false;
//                    }
//                    cropImage.mWaitingToPick = z;
//                    if (AnonymousClass2.this.mNumFaces > 0) {
//                        for (int i = 0; i < AnonymousClass2.this.mNumFaces; i++) {
//                            AnonymousClass2.this.handleFace(AnonymousClass2.this.mFaces[i]);
//                        }
//                    } else {
//                        AnonymousClass2.this.makeDefault();
//                    }
//                    CropImage.this.mImageView.invalidate();
//                    if (CropImage.this.mImageView.mHighlightViews.size() == 1) {
//                        CropImage.this.mCrop = (HighlightView) CropImage.this.mImageView.mHighlightViews.get(0);
//                        CropImage.this.mCrop.setFocus(true);
//                    }
//                    if (AnonymousClass2.this.mNumFaces > 1) {
//                        Toast.makeText(CropImage.this.mContext, CropImage.this.mcResource.getStringId("mc_forum_multipic_crop_help"), 0).show();
//                    }
                }
            });
        }
    };
    public boolean mSaving;
    public boolean mWaitingToPick;
    private MCResource mcResource;

    class BackgroundJob implements Runnable {
        private Handler mHandler;
        private Runnable mJob;
        private ProgressDialog mProgress;

        public BackgroundJob(ProgressDialog progress, Runnable job, Handler handler) {
            this.mProgress = progress;
            this.mJob = job;
            this.mHandler = handler;
        }

        public void run() {
            try {
                this.mJob.run();
            } finally {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (BackgroundJob.this.mProgress != null && BackgroundJob.this.mProgress.isShowing()) {
                            BackgroundJob.this.mProgress.dismiss();
                            BackgroundJob.this.mProgress = null;
                        }
                    }
                });
            }
        }
    }

    public CropImage(Context context, CropImageView imageView) {
        this.mContext = context;
        this.mImageView = imageView;
        this.mImageView.setCropImage(this);
        this.mcResource = MCResource.getInstance(this.mContext);
    }

    public void crop(Bitmap bm) {
        this.mBitmap = bm;
        startFaceDetection();
    }

    private void startFaceDetection() {
        if (!((Activity) this.mContext).isFinishing()) {
            showProgressDialog(this.mContext.getResources().getString(this.mcResource.getStringId("mc_forum_please_wait")), new Runnable() {
                public void run() {
                    final CountDownLatch latch = new CountDownLatch(1);
                    final Bitmap b = CropImage.this.mBitmap;
                    CropImage.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (!(b == CropImage.this.mBitmap || b == null)) {
                                CropImage.this.mImageView.setImageBitmapResetBase(b, true);
                                CropImage.this.mBitmap.recycle();
                                CropImage.this.mBitmap = b;
                            }
                            if (CropImage.this.mImageView.getScale() == CustomConstant.RATIO_ONE_HEIGHT) {
                                CropImage.this.mImageView.center(true, true);
                            }
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                        CropImage.this.mRunFaceDetection.run();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, this.mHandler);
        }
    }

    public Bitmap cropAndSave(Bitmap bm) {
        Bitmap bmp = onSaveClicked(bm);
        this.mImageView.mHighlightViews.clear();
        return bmp;
    }

    public void cropCancel() {
        this.mImageView.mHighlightViews.clear();
        this.mImageView.invalidate();
    }

    private Bitmap onSaveClicked(Bitmap bm) {
        if (this.mSaving || this.mCrop == null) {
            return bm;
        }
        this.mSaving = true;
        Rect r = this.mCrop.getCropRect();
        int width = r.width();
        int height = r.height();
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Config.RGB_565);
        new Canvas(croppedImage).drawBitmap(bm, r, new Rect(0, 0, width, height), null);
        return croppedImage;
    }

    public void saveToLocal(Bitmap bm, String path) {
        MCImageUtil.compressBitmap(path, bm, 100, 3, this.mContext);
    }

    private void showProgressDialog(String msg, Runnable job, Handler handler) {
        new Thread(new BackgroundJob(ProgressDialog.show(this.mContext, null, msg), job, handler)).start();
    }
}
