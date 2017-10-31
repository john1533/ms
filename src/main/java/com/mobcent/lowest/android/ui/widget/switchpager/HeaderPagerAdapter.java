package com.mobcent.lowest.android.ui.widget.switchpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class HeaderPagerAdapter extends PagerAdapter {
    private List<SwitchModel> datas;
    private HeaderPagerClickListener headerListener;

    public interface HeaderPagerClickListener {
        void onHeaderClick(SwitchModel switchModel);
    }

    public HeaderPagerAdapter(List<SwitchModel> topicList, HeaderPagerClickListener headerListener) {
        this.datas = topicList;
        this.headerListener = headerListener;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ScaleType.CENTER_CROP);
        container.addView(imageView, new LayoutParams(-1, -1));
        ImageLoader.getInstance().displayImage(((SwitchModel) this.datas.get(position)).getImgUrl(), imageView);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (HeaderPagerAdapter.this.headerListener != null) {
                    HeaderPagerAdapter.this.headerListener.onHeaderClick((SwitchModel) HeaderPagerAdapter.this.datas.get(position));
                }
            }
        });
        return imageView;
    }

    public int getCount() {
        return this.datas.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
