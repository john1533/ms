package com.mobcent.lowest.android.ui.widget.scaleview;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.List;

public class ImagePreviewFragmentAdapter extends FragmentStatePagerAdapter {
    private List<RichImageModel> imageUrlList;

    public void setImageUrlList(List<RichImageModel> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public ImagePreviewFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImagePreviewFragment getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        ImagePreviewFragment imageViewerFragment = ImagePreviewFragment.newInstance();
        imageViewerFragment.setImageModel((RichImageModel) this.imageUrlList.get(position));
        return imageViewerFragment;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public int getCount() {
        return this.imageUrlList.size();
    }
}
