package com.mobcent.discuz.activity.widget.album;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class PhotoPreviewFragment extends BaseFragment {
    private List<Drawable> drawables;
    private ImageView imageView;
    private PictureModel pictureModel;
    private TextView textView;

    public PhotoPreviewFragment(PictureModel pictureModel) {
        this.pictureModel = pictureModel;
    }

    public PictureModel getPictureModel() {
        return this.pictureModel;
    }

    protected String getRootLayoutName() {
        return "photo_preview_fragment";
    }

    protected void initViews(View rootView) {
        this.imageView = (ImageView) findViewByName(rootView, "photo_preview_image");
        this.textView = (TextView) findViewByName(rootView, "photo_preview_text");
    }

    protected void initActions(View rootView) {
        this.imageView.setTag(this.pictureModel.getAbsolutePath());
        loadImage(this.imageView);
        this.textView.setText(this.pictureModel.getAbsolutePath());
    }

    public void loadImage(ImageView imageView) {
        ImageLoader.getInstance().displayImage((String) imageView.getTag(), imageView);
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public TextView getTextView() {
        return this.textView;
    }
}
