package com.mobcent.discuz.activity.widget.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import com.mobcent.discuz.activity.widget.cropImage.CropImageActivity;
import com.mobcent.discuz.android.model.PictureAlbumModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.constant.BasePlaceApiConstant;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PhotoManageHelper {
    public static String ALL = null;
    public static final String CALL_BACK_RIGHT_NOW = "callBackRightNow";
    public static final String CAMERA_PATH = "cameraPath";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PREVIEW_APPOINT = 2;
    public static final String PREVIEW_NORMAL_ALBUM_PATH = null;
    public static final int PREVIEW_NORMAL_POSITION = -1;
    public static final int SELECT_ICON = 1;
    public static final int SELECT_PHOTO = 2;
    public static final String SELECT_TYPE = "selectType";
    public static final int TYPE_ONE_FOR_ONE = -1;
    private final String TAG = "PhotoManageHelper";
    private List<PictureAlbumModel> albumList = new ArrayList();
    private Map<String, List<PictureModel>> allMap = new LinkedHashMap();
    private String baseIconPath;
    private String baseImagePath;
    private String cameraPath;
    public ClickListener clickListener = null;
    private FinishListener finishListener;
    private PhotoManageHelper helper;
    private Map<String, PictureModel> innerSelectMap = new LinkedHashMap();
    private boolean isCallBackRightNow = false;
    private boolean isFromSelector = false;
    private int maxSelectNum = 10;
    private Map<String, PictureModel> outerSelectMap = new LinkedHashMap();
    private PictureAsyncTask pictureAsyncTask;
    public int selectType = 2;
    private int type = -1;

    public interface ClickListener {
        boolean changeStatus(PictureModel pictureModel);
    }

    public interface FinishListener {
        void cameraFinish(int i, Map<String, PictureModel> map, String str, Bitmap bitmap);

        void photoFinish(int i, Map<String, PictureModel> map);
    }

    private class PictureAsyncTask extends AsyncTask<Void, Void, List<String>> {
        private Activity context;

        public PictureAsyncTask(Activity context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PhotoManageHelper.this.albumList.clear();
            PhotoManageHelper.this.allMap.clear();
        }

        protected List<String> doInBackground(Void... arg0) {
            Cursor cursor = this.context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data"}, null, null, "datetaken desc");
            if (cursor == null) {
                return null;
            }
            List<String> pathList = new ArrayList();
            if (cursor.moveToFirst()) {
                do {
                    pathList.add(Uri.fromFile(new File(cursor.getString(cursor.getColumnIndexOrThrow("_data")))).toString());
                } while (cursor.moveToNext());
            }
            cursor.close();
            return pathList;
        }

        protected void onPostExecute(List<String> pathList) {
            super.onPostExecute(pathList);
            if (pathList != null && !pathList.isEmpty()) {
                for (int i = 0; i < pathList.size(); i++) {
                    String str = (String) pathList.get(i);
                    if (str.length() == 0) {
                        break;
                    }
                    PictureModel pictureModel = new PictureModel();
                    pictureModel.setAbsolutePath(str);
                    String folderPath = "";
                    int position = str.lastIndexOf("/");
                    if (position < 0) {
                        folderPath = str;
                    } else {
                        folderPath = str.substring(0, position);
                    }
                    List<PictureModel> allList = (List) PhotoManageHelper.this.allMap.get(PhotoManageHelper.ALL);
                    if (allList == null) {
                        allList = new ArrayList();
                        PhotoManageHelper.this.allMap.put(PhotoManageHelper.ALL, allList);
                    }
                    allList.add(pictureModel);
                    List<PictureModel> pictureList = (List) PhotoManageHelper.this.allMap.get(folderPath);
                    if (pictureList == null) {
                        pictureList = new ArrayList();
                        PhotoManageHelper.this.allMap.put(folderPath, pictureList);
                    }
                    pictureList.add(pictureModel);
                }
                for (String key : PhotoManageHelper.this.allMap.keySet()) {
                    PictureAlbumModel pictureAlbumModel = new PictureAlbumModel();
                    pictureAlbumModel.setFolderPath(key);
                    List<PictureModel> list = (List) PhotoManageHelper.this.allMap.get(key);
                    if (list == null || list.isEmpty()) {
                        break;
                    }
                    pictureAlbumModel.setSize(list.size());
                    pictureAlbumModel.setFirstPicPath(((PictureModel) list.get(0)).getAbsolutePath());
                    PhotoManageHelper.this.albumList.add(pictureAlbumModel);
                }
                PhotoSelectorActivity.setManageHelper(PhotoManageHelper.this.helper);
                Intent intent = new Intent(this.context.getApplicationContext(), PhotoSelectorActivity.class);
                intent.putExtra("selectType", PhotoManageHelper.this.selectType);
                intent.putExtra(PhotoManageHelper.CALL_BACK_RIGHT_NOW, PhotoManageHelper.this.isCallBackRightNow);
                this.context.startActivity(intent);
            }
        }
    }

    public PhotoManageHelper(Context context) {
        ALL = MCResource.getInstance(context.getApplicationContext()).getString("mc_forum_photo_album_all");
        this.baseImagePath = MCLibIOUtil.getImageCachePath(context.getApplicationContext());
        this.baseIconPath = MCLibIOUtil.getIconPath(context.getApplicationContext());
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        init();
    }

    private void init() {
        this.innerSelectMap = new LinkedHashMap();
        this.outerSelectMap = new LinkedHashMap();
        this.helper = this;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public int getMaxSelectNum() {
        return this.maxSelectNum;
    }

    public List<PictureAlbumModel> getAlbumList() {
        return this.albumList;
    }

    public Map<String, List<PictureModel>> getAllMap() {
        return this.allMap;
    }

    public Map<String, PictureModel> getInnerSelectMap() {
        return this.innerSelectMap;
    }

    public Map<String, PictureModel> getOuterSelectMap() {
        return this.outerSelectMap;
    }

    public String getHash(String url) {
        return MCStringUtil.getMD5Str(url);
    }

    public void registListener(FinishListener listener) {
        this.finishListener = listener;
    }

    public void openPhotoSelector(Activity context, int selectType) {
        this.type = -1;
        openPhotoSelector(context, selectType, this.type, this.outerSelectMap);
    }

    public void openPhotoSelector(Activity context, int selectType, int type, Map<String, PictureModel> outerMap) {
        if (outerMap == null) {
            outerMap = new LinkedHashMap();
        }
        this.type = type;
        this.selectType = selectType;
        this.outerSelectMap = outerMap;
        this.innerSelectMap.clear();
        this.innerSelectMap.putAll(this.outerSelectMap);
        this.pictureAsyncTask = new PictureAsyncTask(context);
        this.pictureAsyncTask.execute(new Void[0]);
    }

    public void closePhotoSelector() {
        this.outerSelectMap.clear();
        this.outerSelectMap.putAll(this.innerSelectMap);
        this.innerSelectMap.clear();
        if (this.finishListener != null) {
            this.finishListener.photoFinish(this.type, this.outerSelectMap);
        }
    }

    public void setFromSelector(boolean isFromSelector) {
        this.isFromSelector = isFromSelector;
    }

    public void openPhotoPreview(Activity context, int previewType, int position, String albumPath) {
        this.type = -1;
        openPhotoPreview(context, previewType, position, albumPath, this.type, this.outerSelectMap);
    }

    public void openPhotoPreview(Activity context, int previewType, int position, String albumPath, int type, Map<String, PictureModel> outerMap) {
        if (outerMap == null) {
            outerMap = new LinkedHashMap();
        }
        this.type = type;
        this.outerSelectMap = outerMap;
        if (MCStringUtil.isEmpty(albumPath)) {
            if (this.isFromSelector) {
                this.isFromSelector = !this.isFromSelector;
            } else {
                this.innerSelectMap.clear();
                this.innerSelectMap.putAll(this.outerSelectMap);
            }
        }
        PhotoPreviewActivity.setManageHelper(this.helper);
        Intent intent = new Intent(context.getApplicationContext(), PhotoPreviewActivity.class);
        intent.putExtra(PhotoPreviewActivity.PREVIEW_TYPE, previewType);
        intent.putExtra("position", position);
        intent.putExtra(PhotoPreviewActivity.ALBUM_PATH, albumPath);
        intent.putExtra(CALL_BACK_RIGHT_NOW, this.isCallBackRightNow);
        context.startActivity(intent);
    }

    public void closePhotoPreview() {
        this.outerSelectMap.clear();
        this.outerSelectMap.putAll(this.innerSelectMap);
        this.innerSelectMap.clear();
        if (this.finishListener != null) {
            this.finishListener.photoFinish(this.type, this.outerSelectMap);
        }
        PhotoSelectorActivity.finishActivity();
    }

    public void onDestroy() {
        if (this.pictureAsyncTask != null) {
            this.pictureAsyncTask.cancel(true);
        }
        this.innerSelectMap.clear();
        this.outerSelectMap.clear();
        this.innerSelectMap = new LinkedHashMap();
        this.outerSelectMap = new LinkedHashMap();
    }

    public void setCallBackRightNow(boolean isCallBackRightNow) {
        this.isCallBackRightNow = isCallBackRightNow;
    }

    public ClickListener getClickListener() {
        return this.clickListener;
    }

    public void registListener(ClickListener listener) {
        this.clickListener = listener;
    }

    public void removeListener(ClickListener listener) {
        this.clickListener = null;
    }

    @SuppressLint({"NewApi"})
    public void openPhotoGraph(Activity context, int selectType) {
        this.type = -1;
        openPhotoGraph(context, selectType, this.type, this.outerSelectMap);
    }

    public void openPhotoGraph(Activity context, int selectType, int type, Map<String, PictureModel> outerMap) {
        if (outerMap == null) {
            outerMap = new LinkedHashMap();
        }
        this.type = type;
        this.selectType = selectType;
        this.outerSelectMap = outerMap;
        if (selectType == 1) {
            this.cameraPath = this.baseIconPath + System.currentTimeMillis() + ".jpg";
        } else {
            this.cameraPath = this.baseImagePath + System.currentTimeMillis() + ".jpg";
        }
        MCLogUtil.e("PhotoManageHelper", "cameraPath=" + this.cameraPath);
        File file = new File(this.cameraPath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(BasePlaceApiConstant.REQ_OUTPUT, Uri.fromFile(file));
        context.startActivityForResult(intent, 100);
    }

    public void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        MCLogUtil.e("PhotoManageHelper", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    if (this.cameraPath != null) {
                        this.cameraPath = Uri.fromFile(new File(this.cameraPath)).toString();
                    }
                    if (this.selectType == 1) {
                        CropImageActivity.setManageHelper(this.helper);
                        Intent intent = new Intent(context.getApplicationContext(), CropImageActivity.class);
                        intent.putExtra("inputPath", this.cameraPath);
                        context.startActivity(intent);
                        return;
                    }
                    PictureModel pictureModel = new PictureModel();
                    pictureModel.setAbsolutePath(this.cameraPath);
                    this.outerSelectMap.put(this.cameraPath, pictureModel);
                    if (this.finishListener != null) {
                        this.finishListener.photoFinish(this.type, this.outerSelectMap);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void closeCrop(String outputPath, Bitmap bitmap) {
        PictureModel pictureModel = new PictureModel();
        pictureModel.setAbsolutePath(outputPath);
        Map<String, PictureModel> outerMap = new LinkedHashMap();
        outerMap.put(outputPath, pictureModel);
        if (this.finishListener != null) {
            this.finishListener.cameraFinish(this.type, outerMap, outputPath, bitmap);
        }
    }
}
