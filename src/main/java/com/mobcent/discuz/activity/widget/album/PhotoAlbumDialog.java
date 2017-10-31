package com.mobcent.discuz.activity.widget.album;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.discuz.android.model.PictureAlbumModel;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumDialog extends Dialog {
    private final String TAG = "PhotoAlbumDialog";
    private PhotoAlbumAdapter adapter;
    private List<PictureAlbumModel> albumList;
    private Context context;
    private ListView listView;
    private PhotoAlbumListener listener;
    private MCResource resource;

    private class PhotoAlbumAdapter extends BaseAdapter {
        private List<PictureAlbumModel> albumList;
        private Context context;
        private LayoutInflater inflater;
        private MCResource resource;

        private class PhotoAlbumAdapterHolder {
            private ImageView imageView;
            private TextView textView;

            private PhotoAlbumAdapterHolder() {
            }

            public ImageView getImageView() {
                return this.imageView;
            }

            public void setImageView(ImageView imageView) {
                this.imageView = imageView;
            }

            public TextView getTextView() {
                return this.textView;
            }

            public void setTextView(TextView textView) {
                this.textView = textView;
            }
        }

        public PhotoAlbumAdapter(Context context, List<PictureAlbumModel> albumList) {
            this.context = context.getApplicationContext();
            this.inflater = LayoutInflater.from(context.getApplicationContext());
            this.albumList = albumList;
            this.resource = MCResource.getInstance(context.getApplicationContext());
        }

        public int getCount() {
            return this.albumList.size();
        }

        public Object getItem(int position) {
            return this.albumList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoAlbumAdapterHolder holder;
            PictureAlbumModel pictureAlbumModel = (PictureAlbumModel) this.albumList.get(position);
            if (convertView == null) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("photo_album_dialog_item"), null);
                holder = new PhotoAlbumAdapterHolder();
                initViews(convertView, holder);
                convertView.setTag(holder);
            } else {
                holder = (PhotoAlbumAdapterHolder) convertView.getTag();
            }
            holder.getImageView().setTag(pictureAlbumModel.getFirstPicPath());
            loadImage(holder.getImageView());
            String folderName = "";
            int indexPosition = pictureAlbumModel.getFolderPath().lastIndexOf("/");
            if (indexPosition < 0) {
                folderName = pictureAlbumModel.getFolderPath();
            } else {
                folderName = pictureAlbumModel.getFolderPath().substring(indexPosition + 1, pictureAlbumModel.getFolderPath().length());
            }
            holder.getTextView().setText(folderName + "(" + pictureAlbumModel.getSize() + ")");
            return convertView;
        }

        private void initViews(View convertView, PhotoAlbumAdapterHolder holder) {
            holder.setImageView((ImageView) convertView.findViewById(this.resource.getViewId("album_first_img")));
            holder.setTextView((TextView) convertView.findViewById(this.resource.getViewId("album_name_text")));
        }

        private void loadImage(final ImageView imageView) {
            ImageLoader.getInstance().loadImage((String) imageView.getTag(), new ImageSize(200, 200), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                }
            });
        }
    }

    public interface PhotoAlbumListener {
        void refreshPhoto(PictureAlbumModel pictureAlbumModel);
    }

    public PhotoAlbumDialog(Context context) {
        super(context);
        init(context);
    }

    public PhotoAlbumDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public PhotoAlbumDialog(Context context, int theme, PhotoAlbumListener listener) {
        super(context, theme);
        init(context);
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context.getApplicationContext();
        this.resource = MCResource.getInstance(context.getApplicationContext());
        setContentView(this.resource.getLayoutId("photo_album_dialog"));
        setCanceledOnTouchOutside(true);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = MCPhoneUtil.getDisplayWidth(context.getApplicationContext());
        getWindow().setAttributes(lp);
        getWindow().setWindowAnimations(this.resource.getStyleId("mc_forum_photo_album_dialog_anim_style"));
        initData();
        initViews();
        initActions();
    }

    private void initData() {
        this.albumList = new ArrayList();
    }

    private void initViews() {
        this.listView = (ListView) findViewById(this.resource.getViewId("photo_album_listview"));
    }

    private void initActions() {
        this.adapter = new PhotoAlbumAdapter(this.context, this.albumList);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PictureAlbumModel pictureAlbumModel = (PictureAlbumModel) PhotoAlbumDialog.this.listView.getAdapter().getItem(position);
                if (PhotoAlbumDialog.this.listener != null) {
                    PhotoAlbumDialog.this.listener.refreshPhoto(pictureAlbumModel);
                }
                PhotoAlbumDialog.this.dismiss();
            }
        });
    }
}
