package com.mobcent.discuz.activity.widget.cropImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;

public class CropImageActivity extends BaseFragmentActivity {
    public static final int IMG_CROP_ACTION = 1;
    public static final int IMG_ROTATE_ACTION = 2;
    public static final String INPUT_PATH = "inputPath";
    private static PhotoManageHelper photoManageHelper;
    private int action = 1;
    private String baseIconPath;
    private Button cancelBtn;
    private CropImage cropImage;
    private CropImageView cropImageView;
    private String inputPath;
    private Bitmap loadBitmap;
    private Button saveBtn;
    private RelativeLayout saveLayout;
    private String savePath;

    protected String getLayoutName() {
        return "crop_image_activity";
    }

    public static void setManageHelper(PhotoManageHelper helper) {
        photoManageHelper = helper;
    }

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.inputPath = intent.getStringExtra("inputPath");
            if (this.inputPath != null) {
                this.inputPath = MCLibIOUtil.getRealFilePath(getApplicationContext(), Uri.parse(this.inputPath));
            }
        }
        this.baseIconPath = MCLibIOUtil.getIconPath(getApplicationContext());
    }

    protected boolean isContainTopBar() {
        return false;
    }

    protected void initViews() {
        this.cropImageView = (CropImageView) findViewByName("mc_forum_crop_view");
        this.saveLayout = (RelativeLayout) findViewByName("mc_forum_save_layout");
        this.saveBtn = (Button) findViewByName("mc_forum_save_btn");
        this.cancelBtn = (Button) findViewByName("mc_forum_cancel_btn");
        showView(this.action);
    }

    private void showView(int action) {
        if (action == 1) {
            this.cropImageView.setVisibility(0);
            this.saveLayout.setVisibility(0);
            this.loadBitmap = MCImageUtil.getBitmapFromMedia(getApplicationContext(), this.inputPath, MCPhoneUtil.getDisplayWidth(getApplicationContext()), MCPhoneUtil.getDisplayHeight(getApplicationContext()));
            this.cropImageView.setImageBitmap(this.loadBitmap);
            this.cropImageView.setImageBitmapResetBase(this.loadBitmap, true);
            this.cropImage = new CropImage(this, this.cropImageView);
            this.cropImage.crop(this.loadBitmap);
        } else if (action != 2) {
        }
    }

    protected void initActions() {
        this.saveBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = CropImageActivity.this.loadBitmap;
                if (CropImageActivity.this.loadBitmap != null) {
                    if (CropImageActivity.this.inputPath.contains(MCAppUtil.getPackageName(CropImageActivity.this.getApplicationContext()))) {
                        CropImageActivity.this.savePath = CropImageActivity.this.inputPath;
                    } else {
                        CropImageActivity.this.savePath = CropImageActivity.this.baseIconPath + System.currentTimeMillis() + ".jpg";
                    }
                    bitmap = CropImageActivity.this.cropImage.cropAndSave(CropImageActivity.this.loadBitmap);
                    CropImageActivity.this.cropImage.saveToLocal(bitmap, CropImageActivity.this.savePath);
                }
                CropImageActivity.photoManageHelper.closeCrop(CropImageActivity.this.savePath, bitmap);
                CropImageActivity.this.finish();
            }
        });
        this.cancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CropImageActivity.this.finish();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        photoManageHelper = null;
    }
}
