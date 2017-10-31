package com.mobcent.lowest.android.ui.widget.web;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.place.api.constant.BasePlaceApiConstant;
import java.io.File;

public class MCWebChromeClient extends WebChromeClient {
    public String TAG = "MCWebChromeClient";
    private Activity activity;
    private Builder dialog;
    private String mCameraFilePath;
    private View mTempView;
    private CustomViewCallback mTempViewCallback;
    private ValueCallback<Uri> mUploadMsg;
    private ProgressBar progressBar;
    private ProgressBar videoPro;
    private WebView web;

    public MCWebChromeClient(Activity activity, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.activity = activity;
    }

    public void onProgressChanged(WebView view, int progress) {
        if (this.progressBar != null) {
            if (progress == 100) {
                this.progressBar.setVisibility(8);
            } else {
                this.progressBar.setVisibility(0);
                this.progressBar.setProgress(progress);
            }
        }
        if (this.web == null) {
            this.web = view;
        }
    }

    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (this.mTempView != null) {
            callback.onCustomViewHidden();
            return;
        }
        try {
            ((ViewGroup) this.web.getParent()).removeView(this.web);
            ((ViewGroup) this.web.getParent()).addView(view);
            this.mTempView = view;
            this.mTempViewCallback = callback;
            this.activity.setRequestedOrientation(0);
        } catch (Exception e) {
        }
    }

    public View getVideoLoadingProgressView() {
        if (this.videoPro != null) {
            this.videoPro = new ProgressBar(this.activity);
        }
        return this.videoPro;
    }

    public void onHideCustomView() {
        if (this.mTempView != null) {
            ((ViewGroup) this.web.getParent()).removeView(this.mTempView);
            this.mTempView = null;
            ((ViewGroup) this.web.getParent()).addView(this.web);
            this.mTempViewCallback.onCustomViewHidden();
            this.activity.setRequestedOrientation(1);
        }
    }

    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        getLocationDialog(this.activity, origin, callback).show();
    }

    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.mUploadMsg = uploadMsg;
        this.activity.startActivityForResult(createDefaultOpenableIntent(), 1);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent("android.intent.action.GET_CONTENT");
        i.addCategory("android.intent.category.OPENABLE");
        i.setType("image/*");
        Intent chooser = createChooserIntent(createCameraIntent(), createSoundRecorderIntent());
        chooser.putExtra("android.intent.extra.INTENT", i);
        return chooser;
    }

    private Intent createSoundRecorderIntent() {
        return new Intent("android.provider.MediaStore.RECORD_SOUND");
    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent("android.intent.action.CHOOSER");
        chooser.putExtra("android.intent.extra.INITIAL_INTENTS", intents);
        chooser.putExtra("android.intent.extra.TITLE", "File Chooser");
        return chooser;
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        File cameraDataDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "browser-photos");
        cameraDataDir.mkdirs();
        this.mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        cameraIntent.putExtra(BasePlaceApiConstant.REQ_OUTPUT, Uri.fromFile(new File(this.mCameraFilePath)));
        return cameraIntent;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && this.mUploadMsg != null) {
            File cameraFile = new File(this.mCameraFilePath);
            if (cameraFile.exists()) {
                this.mUploadMsg.onReceiveValue(Uri.fromFile(cameraFile));
                this.mUploadMsg = null;
            } else if (data != null) {
                Uri result = (data == null || resultCode != -1) ? null : data.getData();
                Uri fileUri = getFileUri(result);
                if (fileUri != null) {
                    this.mUploadMsg.onReceiveValue(fileUri);
                    this.mUploadMsg = null;
                    return;
                }
                Toast.makeText(this.activity.getApplicationContext(), "choose img error", LocationClientOption.MIN_SCAN_SPAN).show();
            }
        }
    }

    protected Uri getFileUri(Uri uri) {
        Cursor cursor = this.activity.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            return Uri.fromFile(file);
        }
        return null;
    }

    private Builder getLocationDialog(Context context, final String origin, final Callback callback) {
        if (this.dialog == null) {
            this.dialog = new Builder(context).setTitle(origin + getString(this.activity, "mc_lowest_request_location"));
            this.dialog.setPositiveButton(getString(this.activity, "mc_lowest_request_location_accept"), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    callback.invoke(origin, true, true);
                }
            });
            this.dialog.setNegativeButton(getString(this.activity, "mc_lowest_request_location_refuse"), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    callback.invoke(origin, false, false);
                }
            });
            this.dialog.create();
        }
        return this.dialog;
    }

    private String getString(Context context, String name) {
        return MCResource.getInstance(context.getApplicationContext()).getString(name);
    }
}
