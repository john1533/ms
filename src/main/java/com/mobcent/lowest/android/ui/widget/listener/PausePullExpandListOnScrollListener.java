package com.mobcent.lowest.android.ui.widget.listener;

import android.widget.AbsListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshExpandableListView.OnScrollListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PausePullExpandListOnScrollListener implements OnScrollListener {
    private final OnScrollListener externalListener;
    private ImageLoader imageLoader;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;

    public PausePullExpandListOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    public PausePullExpandListOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case 0:
                this.imageLoader.resume();
                break;
            case 1:
                if (this.pauseOnScroll) {
                    this.imageLoader.pause();
                    break;
                }
                break;
            case 2:
                if (this.pauseOnFling) {
                    this.imageLoader.pause();
                    break;
                }
                break;
        }
        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void onScrollDirection(boolean isUp, int distance) {
    }
}
