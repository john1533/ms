package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.mobcent.lowest.android.ui.widget.MCScrollView.OnScrollListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullToRefreshWaterFallNew extends PullToRefreshBase<ScrollView> {
    public static final int DRAW_ADD = 1;
    public static final int DRAW_REFRESH = 0;
    public String TAG = "PullToRefreshWaterFallNew";
    private boolean autoLoadMore = true;
    private Map<Integer, List<View>> cachePageViews = null;
    private List<View> cacheViews = null;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag(PullToRefreshWaterFallNew.this.tagKey) instanceof BaseFallWallModel) {
                PullToRefreshWaterFallNew.this.getOnLoadItemListener().onItemClick((BaseFallWallModel) v.getTag(PullToRefreshWaterFallNew.this.tagKey), v);
            }
        }
    };
    private int columnCount = 3;
    private int[] columnHeights = null;
    private int columnSpace = 5;
    private int columnWidth;
    private final OnRefreshListener defaultOnRefreshListener = new OnRefreshListener() {
        public void onRefresh() {
            PullToRefreshWaterFallNew.this.onRefreshComplete();
        }
    };
    private int expandHeight = 480;
    private List<BaseFallWallModel> flowTagList;
    private boolean isFirstDraw = true;
    private boolean isHasBorder = true;
    private int lastPage;
    private Handler mHandler = new Handler();
    private Handler mHandler2 = null;
    private OnLoadItemListener onLoadItemListener;
    private View poView = null;
    private int positionCounter = 0;
    private RelativeLayout rootLayout;
    private int rowSpace = 5;
    private int screenHeight;
    private int tagKey = 0;
    private ViewStateChangeThread thread = null;
    private List<View> visibileViews = null;

    public static abstract class OnLoadItemListener {
        @Deprecated
        public View createItemView() {
            return null;
        }

        public View createItemView(BaseFallWallModel tag) {
            return null;
        }

        public void setItemData(BaseFallWallModel flowTag, View view, boolean isVisibile) {
        }

        public void onItemRecycle(BaseFallWallModel flowTag, View view) {
        }

        public void onItemClick(BaseFallWallModel flowTag, View view) {
        }
    }

    class ViewStateChangeThread extends Thread {
        private boolean isBusy;
        private boolean isForce;

        public ViewStateChangeThread(boolean isForce) {
            this.isForce = isForce;
        }

        public synchronized boolean isBusy() {
            return this.isBusy;
        }

        public synchronized void setBusy(boolean isBusy) {
            this.isBusy = isBusy;
        }

        public void run() {
            setBusy(true);
            MCLogUtil.e(PullToRefreshWaterFallNew.this.TAG, "start======" + getId());
            int currentPage = PullToRefreshWaterFallNew.this.getPageByTop(((ScrollView) PullToRefreshWaterFallNew.this.getRefreshableView()).getScrollY());
            if (PullToRefreshWaterFallNew.this.isFirstDraw || this.isForce) {
                PullToRefreshWaterFallNew.this.isFirstDraw = false;
            } else if (PullToRefreshWaterFallNew.this.lastPage == currentPage) {
                setBusy(false);
                return;
            }
            PullToRefreshWaterFallNew.this.lastPage = currentPage;
            int delayMillis = 0;
            for (int page = currentPage - 1; page <= currentPage + 1; page++) {
                synchronized (PullToRefreshWaterFallNew.this.cachePageViews) {
                    List<View> pageList = (List) PullToRefreshWaterFallNew.this.cachePageViews.get(Integer.valueOf(page));
                    if (!MCListUtils.isEmpty((List) pageList)) {
                        for (final View view : pageList) {
                            PullToRefreshWaterFallNew.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    PullToRefreshWaterFallNew.this.onLoadItemListener.setItemData((BaseFallWallModel) view.getTag(PullToRefreshWaterFallNew.this.tagKey), view, true);
                                }
                            }, (long) delayMillis);
                            delayMillis += 50;
                        }
                    }
                }
            }
            setBusy(false);
        }
    }

    public OnLoadItemListener getOnLoadItemListener() {
        if (this.onLoadItemListener != null) {
            return this.onLoadItemListener;
        }
        throw new NullPointerException("you must register setOnLoadItemListener");
    }

    public void setOnLoadItemListener(OnLoadItemListener onLoadItemListener) {
        this.onLoadItemListener = onLoadItemListener;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getColumnSpace() {
        return this.columnSpace;
    }

    public void setColumnSpace(int columnSpace) {
        this.columnSpace = columnSpace;
    }

    public boolean isAutoLoadMore() {
        return this.autoLoadMore;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    public int getColumnWidth() {
        return this.columnWidth;
    }

    public void setExpandHeight(int expandHeight) {
        this.expandHeight = expandHeight;
    }

    public PullToRefreshWaterFallNew(Context context) {
        super(context);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshWaterFallNew(Context context, int mode) {
        super(context, mode);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshWaterFallNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    private void initData() {
        this.screenHeight = MCPhoneUtil.getDisplayHeight(getContext());
        this.flowTagList = new ArrayList();
        this.visibileViews = new ArrayList();
        this.cacheViews = new ArrayList();
        this.cachePageViews = new HashMap();
        this.mHandler2 = new Handler() {
            public void dispatchMessage(Message msg) {
                MCLogUtil.e(PullToRefreshWaterFallNew.this.TAG, "dispatchMessage==========" + msg.what);
                if (msg.what == 2) {
                    PullToRefreshWaterFallNew.this.changeVisibile(((Boolean) msg.obj).booleanValue());
                }
            }
        };
        this.expandHeight = MCPhoneUtil.dip2px(getContext(), (float) this.expandHeight);
        this.columnSpace = MCPhoneUtil.dip2px(getContext(), (float) this.columnSpace);
        this.rowSpace = this.columnSpace;
        if (this.isHasBorder) {
            this.columnWidth = (MCPhoneUtil.getDisplayWidth(getContext()) - (this.columnSpace * (this.columnCount + 1))) / this.columnCount;
        } else {
            this.columnWidth = (MCPhoneUtil.getDisplayWidth(getContext()) - (this.columnSpace * (this.columnCount - 1))) / this.columnCount;
        }
        this.columnHeights = new int[this.columnCount];
        this.tagKey = MCResource.getInstance(getContext()).getViewId("position");
    }

    public void initView(Context context, View header) {
        initData();
        if (header != null) {
            this.expandHeight += header.getMeasuredHeight();
        }
        this.rootLayout = new RelativeLayout(getContext());
        this.rootLayout.setLayoutParams(new LayoutParams(-1, -1));
        addContentView(this.rootLayout, header);
    }

    public void initView(Context context) {
        initView(context, null);
    }

    public void onDrawWaterFall(List<? extends BaseFallWallModel> modelList, int type) {
        List<BaseFallWallModel> flowTagListTemp = new ArrayList();
        for (int i = 0; i < modelList.size(); i++) {
            flowTagListTemp.add(modelList.get(i));
        }
        if (this.thread != null && this.thread.isBusy()) {
            this.thread.interrupt();
        }
        if (type == 0) {
            this.cachePageViews.clear();
            this.positionCounter = 0;
            this.visibileViews.clear();
            this.cacheViews.clear();
            this.rootLayout.removeAllViews();
            this.flowTagList.clear();
            this.poView = new View(getContext());
            this.rootLayout.addView(this.poView, new RelativeLayout.LayoutParams(10, 10));
        } else if (type == 1) {
        }
        dealWallData(flowTagListTemp, type);
        if (this.poView != null) {
            RelativeLayout.LayoutParams poLps = (RelativeLayout.LayoutParams) this.poView.getLayoutParams();
            poLps.topMargin = getHeightest();
            this.poView.setLayoutParams(poLps);
        }
        this.flowTagList.addAll(flowTagListTemp);
    }

    private void sendVisibileChangeMsg(boolean isForce) {
        ImageLoader.getInstance().pause();
        this.mHandler2.removeMessages(2);
        Message msg = new Message();
        msg.what = 2;
        msg.obj = Boolean.valueOf(isForce);
        this.mHandler2.sendMessageDelayed(msg, 100);
    }

    private void changeVisibile(boolean isForce) {
        MCLogUtil.e(this.TAG, "==============changeVisibile");
        ImageLoader.getInstance().resume();
        if (this.thread == null || !this.thread.isBusy()) {
            if (this.thread != null) {
                this.thread.interrupt();
            }
            this.thread = new ViewStateChangeThread(isForce);
            this.thread.start();
        }
    }

    private View isInVisibleViewList(int position) {
        for (View v : this.visibileViews) {
            if ((v.getTag(this.tagKey) instanceof BaseFallWallModel) && ((BaseFallWallModel) v.getTag(this.tagKey)).getPosition() == position) {
                return v;
            }
        }
        return null;
    }

    private boolean isInVisibileBound(int marginTop) {
        int scrollY = ((ScrollView) getRefreshableView()).getScrollY();
        int bottomBound = (MCPhoneUtil.getDisplayHeight(getContext()) + scrollY) + this.expandHeight;
        if (marginTop < scrollY - this.expandHeight || marginTop > bottomBound) {
            return false;
        }
        return true;
    }

    private View getRecycledView(BaseFallWallModel tag) {
        if (this.cacheViews.size() != 0) {
            return (View) this.cacheViews.remove(0);
        }
        View v = getOnLoadItemListener().createItemView(tag);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(this.columnWidth, (int) (tag.getRatio() * ((float) this.columnWidth)));
        lps.leftMargin = tag.getMarginLeft();
        lps.topMargin = tag.getMarginTop();
        v.setLayoutParams(lps);
        v.setTag(this.tagKey, tag);
        int page = getPageByTop(tag.getMarginTop());
        synchronized (this.cachePageViews) {
            if (this.cachePageViews.get(Integer.valueOf(page)) == null) {
                this.cachePageViews.put(Integer.valueOf(page), new ArrayList());
            }
            ((List) this.cachePageViews.get(Integer.valueOf(page))).add(v);
        }
        v.setOnClickListener(this.clickListener);
        return v;
    }

    private int getPageByTop(int marginTop) {
        return marginTop / this.screenHeight;
    }

    private void dealWallData(List<BaseFallWallModel> flowTagListTemp, int type) {
        int i;
        if (type == 0) {
            for (i = 0; i < this.columnHeights.length; i++) {
                this.columnHeights[i] = 0;
            }
        }
        int len = flowTagListTemp.size();
        for (i = 0; i < len; i++) {
            int marginLeft;
            int index = i;
            BaseFallWallModel flowTag = (BaseFallWallModel) flowTagListTemp.get(i);
            int position = getShortIndex();
            int height = (int) (((float) this.columnWidth) * flowTag.getRatio());
            if (this.isHasBorder) {
                marginLeft = ((position + 1) * this.columnSpace) + (this.columnWidth * position);
            } else {
                marginLeft = (this.columnSpace * position) + (this.columnWidth * position);
            }
            int marginTop = this.columnHeights[position] + this.rowSpace;
            this.columnHeights[position] = marginTop + height;
            flowTag.setMarginTop(marginTop);
            flowTag.setMarginLeft(marginLeft);
            flowTag.setPosition(this.positionCounter);
            this.rootLayout.addView(getRecycledView(flowTag));
            if (index == len - 1) {
                sendVisibileChangeMsg(true);
            }
            this.positionCounter++;
        }
    }

    private int getShortIndex() {
        int count = this.columnHeights.length;
        if (count <= 1) {
            return 0;
        }
        int position = 0;
        int tempHeigth = this.columnHeights[0];
        for (int i = 0; i < count; i++) {
            int currentHeigth = this.columnHeights[i];
            if (currentHeigth < tempHeigth) {
                tempHeigth = currentHeigth;
                position = i;
            }
        }
        return position;
    }

    private int getHeightest() {
        int tempHeigth = this.columnHeights[0];
        for (int currentHeigth : this.columnHeights) {
            if (currentHeigth > tempHeigth) {
                tempHeigth = currentHeigth;
            }
        }
        return tempHeigth;
    }

    protected void gotoTop() {
        ((ScrollView) getRefreshableView()).scrollTo(0, 0);
    }

    public void onDestroyView() {
        int i;
        View child;
        if (this.rootLayout != null) {
            int childCount = this.rootLayout.getChildCount();
            for (i = 0; i < childCount; i++) {
                child = this.rootLayout.getChildAt(i);
                if (child.getTag(this.tagKey) instanceof BaseFallWallModel) {
                    getOnLoadItemListener().onItemRecycle((BaseFallWallModel) child.getTag(this.tagKey), child);
                }
            }
            this.rootLayout.removeAllViews();
        }
        int vL = this.visibileViews.size();
        for (i = 0; i < vL; i++) {
            child = (View) this.visibileViews.get(i);
            if (child.getTag(this.tagKey) instanceof BaseFallWallModel) {
                getOnLoadItemListener().onItemRecycle((BaseFallWallModel) child.getTag(this.tagKey), child);
            }
        }
        this.visibileViews.clear();
        int cL = this.cacheViews.size();
        for (i = 0; i < cL; i++) {
            child = (View) this.cacheViews.get(i);
            if (child.getTag(this.tagKey) instanceof BaseFallWallModel) {
                getOnLoadItemListener().onItemRecycle((BaseFallWallModel) child.getTag(this.tagKey), child);
            }
        }
        this.cacheViews.clear();
        for (Integer page : this.cachePageViews.keySet()) {
            List<View> pageList = (List) this.cachePageViews.get(page);
            if (!MCListUtils.isEmpty((List) pageList)) {
                for (View v : pageList) {
                    if (v.getTag(this.tagKey) instanceof BaseFallWallModel) {
                        getOnLoadItemListener().onItemRecycle((BaseFallWallModel) v.getTag(this.tagKey), v);
                    }
                }
            }
        }
        this.cachePageViews.clear();
    }

    protected boolean onScroll(MotionEvent event) {
        return false;
    }

    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        final MCScrollView scrollView = new MCScrollView(context, attrs);
        scrollView.setOnScrollListener(new OnScrollListener() {
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                PullToRefreshWaterFallNew.this.sendVisibileChangeMsg(false);
                if (scrollView.getChildCount() > 0 && scrollView.getChildAt(0) != null) {
                    if (scrollView.getScrollY() >= scrollView.getChildAt(0).getHeight() - scrollView.getHeight() && PullToRefreshWaterFallNew.this.autoLoadMore && PullToRefreshWaterFallNew.this.getBottomState() == 0) {
                        PullToRefreshWaterFallNew.this.setBottomState(1);
                        PullToRefreshWaterFallNew.this.footerLayout.bottomRefreshing();
                        PullToRefreshWaterFallNew.this.getOnBottomRefreshListener().onRefresh();
                    }
                }
            }
        });
        return scrollView;
    }

    protected boolean isReadyForPullDown() {
        return ((ScrollView) this.refreshableView).getScrollY() == 0;
    }

    protected void log(String msg) {
        MCLogUtil.e(this.TAG, msg);
    }

    public void setHasBorder(boolean isHasBorder) {
        this.isHasBorder = isHasBorder;
    }
}
