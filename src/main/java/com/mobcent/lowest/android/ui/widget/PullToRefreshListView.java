package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class PullToRefreshListView extends ListView implements android.widget.AbsListView.OnScrollListener {
    public static final int BOTTOM_CACHE_FINISH = 5;
    public static final int BOTTOM_GONE = 6;
    public static final int BOTTOM_LOAD_FAIL = 4;
    public static final int BOTTOM_LOAD_FINISH = 3;
    public static final int BOTTOM_LOAD_MORE = 0;
    public static final int BOTTOM_NO_DATA = 2;
    public static final int BOTTOM_REFRESH = 1;
    private static final int DONE = 3;
    private static final int FRICTION = 2;
    private static final int LOADING = 4;
    public static final int MODE_BOTH = 3;
    public static final int MODE_PULL_DOWN_TO_REFRESH = 1;
    public static final int MODE_PULL_UP_TO_REFRESH = 2;
    private static final int PULL_To_REFRESH = 1;
    private static final int REFRESHING = 2;
    private static final int RELEASE_To_REFRESH = 0;
    public static boolean autoRefresh = false;
    private boolean autoLoadMore = true;
    private OnBottomRefreshListener bottomRefreshListener;
    private int bottomState;
    private String buttonFailLabel;
    private String buttonIsEndLabel;
    private String buttonNodataLabel;
    private String cacheEndLabel;
    private SmoothScrollRunnable currentSmoothScrollRunnable;
    private int firstItemIndex;
    private PullToRefreshLayout footerLayout;
    private final Handler handler = new Handler();
    private int headerHeight;
    private PullToRefreshLayout headerLayout;
    private boolean isBack;
    private boolean isBottom;
    private boolean isHand;
    private boolean isHasContentView = false;
    private boolean isInterceptTouch = false;
    boolean isRecordStart = true;
    private boolean isRecored;
    private boolean isRefreshable;
    private boolean isRemoveHeader = false;
    private boolean isShowHeader = true;
    private ImageView mTopImageView;
    private MCResource mcResource;
    private int mode = 1;
    private OnVerticalScrollListener onVerticalScrollListener;
    private String pullLabel;
    private String pullLabelbottom;
    private OnRefreshListener refreshListener;
    private String refreshingLabel;
    private String refreshingLabelbottom;
    private String releaseLabel;
    private OnScrollListener scrollListener;
    private int slideSpace;
    private int springBack;
    int start = 0;
    private int startY;
    private int state;

    public interface OnBottomRefreshListener {
        void onRefresh();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnScrollListener {
        void onScroll(AbsListView absListView, int i, int i2, int i3);

        void onScrollDirection(boolean z, int i);

        void onScrollStateChanged(AbsListView absListView, int i);
    }

    public interface OnVerticalScrollListener {
        void onVerticalScroll();
    }

    final class SmoothScrollRunnable implements Runnable {
        static final int ANIMATION_DURATION_MS = 500;
        static final int ANIMATION_FPS = 16;
        private boolean continueRunning = true;
        private int currentY = -1;
        private final Handler handler;
        private final Interpolator interpolator;
        private final int scrollFromY;
        private final int scrollToY;
        private long startTime = -1;

        public SmoothScrollRunnable(Handler handler, int fromY, int toY) {
            this.handler = handler;
            this.scrollFromY = fromY;
            this.scrollToY = toY;
            this.interpolator = new AccelerateDecelerateInterpolator();
        }

        public void run() {
            if (this.startTime == -1) {
                this.startTime = System.currentTimeMillis();
            } else {
                this.currentY = this.scrollFromY - Math.round(((float) (this.scrollFromY - this.scrollToY)) * this.interpolator.getInterpolation(((float) Math.max(Math.min(((System.currentTimeMillis() - this.startTime) * 1000) / 500, 1000), 0)) / 1000.0f));
                PullToRefreshListView.this.headerLayout.setPadding(0, this.currentY, 0, 0);
                PullToRefreshListView.this.springBack = this.currentY;
            }
            if (this.continueRunning && this.scrollToY != this.currentY) {
                this.handler.postDelayed(this, 16);
            }
        }

        public void stop() {
            this.continueRunning = false;
            this.handler.removeCallbacks(this);
        }
    }

    class clickToRefresh implements OnClickListener {
        clickToRefresh() {
        }

        public void onClick(View v) {
            if (PullToRefreshListView.this.bottomState != 3 && PullToRefreshListView.this.bottomState != 5) {
                PullToRefreshListView.this.bottomState = 1;
                PullToRefreshListView.this.footerLayout.bottomRefreshing();
                PullToRefreshListView.this.onBottomRefresh();
            } else if (PullToRefreshListView.this.bottomState == 5) {
                PullToRefreshListView.this.setSelection(0);
                PullToRefreshListView.this.handler.postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshListView.this.onHandRefresh();
                    }
                }, 300);
            }
        }
    }

    protected final void smoothScrollTo(int fromY, int toY) {
        if (this.currentSmoothScrollRunnable != null) {
            this.currentSmoothScrollRunnable.stop();
        }
        if (fromY != toY) {
            this.currentSmoothScrollRunnable = new SmoothScrollRunnable(this.handler, fromY, toY);
            this.handler.post(this.currentSmoothScrollRunnable);
        }
    }

    public String getButtonNodataLabel() {
        return this.buttonNodataLabel;
    }

    public void setButtonNodataLabel(String buttonNodataLabel) {
        this.buttonNodataLabel = buttonNodataLabel;
    }

    public String getButtonIsEndLabel() {
        return this.buttonIsEndLabel;
    }

    public void setButtonIsEndLabel(String buttonIsEndLabel) {
        this.buttonIsEndLabel = buttonIsEndLabel;
    }

    public String getButtonFailLabel() {
        return this.buttonFailLabel;
    }

    public void setButtonFailLabel(String buttonFailLabel) {
        this.buttonFailLabel = buttonFailLabel;
    }

    public void setInterceptTouch(boolean isInterceptTouch) {
        this.isInterceptTouch = isInterceptTouch;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    public boolean isHand() {
        return this.isHand;
    }

    public void setHand(boolean isHand) {
        this.isHand = isHand;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    public PullToRefreshListView(Context context) {
        super(context);
        init(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mcResource = MCResource.getInstance(context);
        this.slideSpace = MCPhoneUtil.getRawSize(context, 1, 20.0f);
        this.mode = 3;
        this.pullLabel = context.getString(this.mcResource.getStringId("mc_forum_drop_dowm"));
        this.refreshingLabel = context.getString(this.mcResource.getStringId("mc_forum_doing_update"));
        this.releaseLabel = context.getString(this.mcResource.getStringId("mc_forum_release_update"));
        this.pullLabelbottom = context.getString(this.mcResource.getStringId("mc_forum_more"));
        this.refreshingLabelbottom = context.getString(this.mcResource.getStringId("mc_forum_doing_update"));
        this.buttonNodataLabel = context.getString(this.mcResource.getStringId("mc_forum_no_content"));
        this.buttonIsEndLabel = context.getString(this.mcResource.getStringId("mc_forum_loaded"));
        this.buttonFailLabel = context.getString(this.mcResource.getStringId("mc_forum_load_fail"));
        this.cacheEndLabel = this.mcResource.getString("mc_forum_cache_load_more");
        if (this.mode == 1 || this.mode == 3) {
            this.headerLayout = new PullToRefreshLayout(context, 1, this.releaseLabel, this.pullLabel, this.refreshingLabel);
            addHeaderView(this.headerLayout, null, false);
            measureView(this.headerLayout);
            this.headerHeight = this.headerLayout.getMeasuredHeight();
        }
        if (this.mode == 2 || this.mode == 3) {
            this.footerLayout = new PullToRefreshLayout(context, 2, "", "", this.refreshingLabelbottom);
            addFooterView(this.footerLayout, null, false);
            measureView(this.footerLayout);
            this.headerHeight = this.footerLayout.getMeasuredHeight();
            this.footerLayout.resetBottom();
            this.footerLayout.setOnClickListener(new clickToRefresh());
        }
        setOnScrollListener(this);
        switch (this.mode) {
            case 2:
                this.footerLayout.setPadding(0, 0, 0, 0);
                this.isRefreshable = false;
                break;
            case 3:
                this.footerLayout.setPadding(0, 0, 0, 0);
                this.headerLayout.setPadding(0, -this.headerHeight, 0, 0);
                this.isRefreshable = true;
                break;
            default:
                this.headerLayout.setPadding(0, -this.headerHeight, 0, 0);
                this.isRefreshable = true;
                break;
        }
        this.springBack = -this.headerHeight;
        this.state = 3;
        this.bottomState = 3;
    }

    private void measureView(View child) {
        int childHeightSpec;
        LayoutParams p = (LayoutParams)child.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void addHeaderView(View v) {
        super.addHeaderView(v);
        this.isHasContentView = true;
    }

    public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        if (firstVisiableItem != totalItemCount - visibleItemCount || totalItemCount <= visibleItemCount) {
            this.isBottom = false;
        } else {
            this.isBottom = true;
        }
        this.firstItemIndex = firstVisiableItem;
        if (this.isBottom && this.autoLoadMore && this.bottomState == 0) {
            this.bottomState = 1;
            this.footerLayout.bottomRefreshing();
            onBottomRefresh();
        }
        if (this.scrollListener != null) {
            this.scrollListener.onScroll(arg0, firstVisiableItem, visibleItemCount, totalItemCount);
        }
    }

    public void removePullToRefreshLayout() {
        this.mode = 2;
        this.isRemoveHeader = true;
        this.isHasContentView = true;
        removeHeaderView(this.headerLayout);
    }

    public void onScrollStateChanged(AbsListView arg0, int scrollState) {
        if (this.scrollListener != null) {
            this.scrollListener.onScrollStateChanged(arg0, scrollState);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.isInterceptTouch) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isRefreshable) {
            switch (event.getAction()) {
                case 0:
                    if (this.firstItemIndex == 0 && !this.isRecored) {
                        this.isRecored = true;
                        this.startY = (int) event.getY();
                        break;
                    }
                case 1:
                    this.isRecordStart = true;
                    if (!(this.state == 2 || this.state == 4 || this.state == 3)) {
                        if (this.state == 1) {
                            this.state = 3;
                            this.isShowHeader = true;
                            changeHeaderViewByState();
                        } else if (this.state == 0 && !this.isHasContentView) {
                            this.state = 2;
                            changeHeaderViewByState();
                            onHandRefresh();
                        }
                    }
                    this.isRecored = false;
                    this.isBack = false;
                    break;
                case 2:
                    if (this.onVerticalScrollListener != null) {
                        this.onVerticalScrollListener.onVerticalScroll();
                    }
                    int tempY = (int) event.getY();
                    if (this.state != 2) {
                        this.springBack = ((tempY - this.startY) / 2) - this.headerHeight;
                    }
                    if (!this.isRecored && this.firstItemIndex == 0) {
                        this.isRecored = true;
                        this.startY = tempY;
                    }
                    if (this.bottomState != 1 && this.state != 2 && this.isRecored && this.state != 4) {
                        switch (this.state) {
                            case 0:
                                if (!this.isRemoveHeader) {
                                    setSelection(0);
                                }
                                if ((tempY - this.startY) / 2 < this.headerHeight && tempY - this.startY > 0) {
                                    this.state = 1;
                                    changeHeaderViewByState();
                                } else if (tempY - this.startY <= 0) {
                                    this.state = 3;
                                    changeHeaderViewByState();
                                }
                                this.headerLayout.setPadding(0, ((tempY - this.startY) / 2) - this.headerHeight, 0, 0);
                                break;
                            case 1:
                                if (!this.isRemoveHeader) {
                                    setSelection(0);
                                }
                                if ((tempY - this.startY) / 2 >= this.headerHeight) {
                                    this.state = 0;
                                    this.isBack = true;
                                    changeHeaderViewByState();
                                } else if (tempY - this.startY <= 0) {
                                    this.state = 3;
                                    changeHeaderViewByState();
                                }
                                this.headerLayout.setPadding(0, (this.headerHeight * -1) + ((tempY - this.startY) / 2), 0, 0);
                                break;
                            case 3:
                                if (tempY - this.startY > 0) {
                                    this.state = 1;
                                    changeHeaderViewByState();
                                    break;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    if (this.isRecordStart) {
                        this.start = (int) event.getY();
                        this.isRecordStart = false;
                    }
                    if (this.scrollListener != null) {
                        if (tempY - this.start <= this.slideSpace) {
                            if (tempY - this.start < (-this.slideSpace)) {
                                this.scrollListener.onScrollDirection(true, this.start - tempY);
                                this.isRecordStart = true;
                                break;
                            }
                        }
                        this.scrollListener.onScrollDirection(false, tempY - this.start);
                        this.isRecordStart = true;
                        break;
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeHeaderViewByState() {
        switch (this.state) {
            case 0:
                this.headerLayout.releaseToRefresh();
                return;
            case 1:
                this.headerLayout.pullToRefresh(this.isBack);
                if (this.isBack) {
                    this.isBack = false;
                    return;
                }
                return;
            case 2:
                smoothScrollTo(this.springBack, 0);
                this.headerLayout.refreshing();
                return;
            case 3:
                if (this.isShowHeader) {
                    smoothScrollTo(this.springBack, this.headerHeight * -1);
                }
                this.headerLayout.reset(null);
                return;
            default:
                return;
        }
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void onRefreshComplete() {
        onRefreshComplete(true);
    }

    public void hideBottom() {
        this.footerLayout.hideBottom();
    }

    public void showBottom() {
        this.footerLayout.showBottom();
    }

    public void removeFooterLayout() {
        this.autoLoadMore = false;
        removeFooterView(this.footerLayout);
    }

    public void onRefreshComplete(boolean isToTop) {
        showBottom();
        if (isToTop) {
            goToTop();
        }
        this.state = 3;
        changeHeaderViewByState();
    }

    public void onHandRefresh() {
        setHand(true);
        onBaseRefresh(true);
        setHand(false);
    }

    public void onRefresh() {
        setHand(false);
        onBaseRefresh(true);
    }

    public void onRefresh(boolean isShowHeader) {
        setHand(false);
        onBaseRefresh(isShowHeader);
    }

    public void onBaseRefresh(boolean isShowHeader) {
        if (this.mode != 2) {
            hideBottom();
        }
        this.isShowHeader = isShowHeader;
        showHeaderBar();
        if (this.refreshListener != null) {
            this.refreshListener.onRefresh();
        }
    }

    public void onRefreshWithOutListener() {
        if (this.mode != 2) {
            hideBottom();
        }
        this.isShowHeader = true;
        showHeaderBar();
    }

    public void showHeaderBar() {
        if (this.headerLayout != null) {
            if (this.isShowHeader && this.state != 2) {
                this.state = 2;
            }
            changeHeaderViewByState();
        }
    }

    public void goToTop() {
        setSelection(0);
    }

    public void updateRefreshTime(String time) {
        if (this.headerLayout != null) {
            this.headerLayout.reset(time);
        }
    }

    public void setOnBottomRefreshListener(OnBottomRefreshListener bottomRefreshListener) {
        this.bottomRefreshListener = bottomRefreshListener;
    }

    public void onBottomRefreshExt() {
        this.bottomState = 1;
        this.footerLayout.bottomRefreshing();
        onBottomRefresh();
    }

    private void onBottomRefresh() {
        if (this.bottomRefreshListener != null) {
            this.bottomRefreshListener.onRefresh();
        }
    }

    public void onBottomRefreshComplete(int status) {
        onBottomRefreshComplete(status, null);
    }

    public void onBottomRefreshComplete(int status, String text) {
        this.footerLayout.setVisibility(0);
        this.footerLayout.resetBottom();
        switch (status) {
            case 0:
                this.bottomState = 0;
                return;
            case 2:
                this.bottomState = 2;
                if (text == null || "".equals(text)) {
                    this.footerLayout.changeBottomRefreshLabel(this.buttonNodataLabel);
                    return;
                } else {
                    this.footerLayout.changeBottomRefreshLabel(text);
                    return;
                }
            case 3:
                this.bottomState = 3;
                if (text == null || "".equals(text)) {
                    this.footerLayout.changeBottomRefreshLabel(this.buttonIsEndLabel);
                    return;
                } else {
                    this.footerLayout.changeBottomRefreshLabel(text);
                    return;
                }
            case 4:
                this.bottomState = 4;
                if (text == null || "".equals(text)) {
                    this.footerLayout.changeBottomRefreshLabel(this.buttonFailLabel);
                    return;
                } else {
                    this.footerLayout.changeBottomRefreshLabel(text);
                    return;
                }
            case 5:
                this.bottomState = 5;
                if (text == null || "".equals(text)) {
                    this.footerLayout.changeBottomRefreshLabel(this.cacheEndLabel);
                    return;
                } else {
                    this.footerLayout.changeBottomRefreshLabel(text);
                    return;
                }
            case 6:
                this.bottomState = 5;
                this.footerLayout.setVisibility(8);
                removeFooterLayout();
                return;
            default:
                this.bottomState = 0;
                return;
        }
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setVerticalScrollListener(OnVerticalScrollListener onVerticalScrollListener) {
        this.onVerticalScrollListener = onVerticalScrollListener;
    }

    public void setBackToTopView(ImageView mTopImageView) {
        this.mTopImageView = mTopImageView;
        mTopImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PullToRefreshListView.this.setSelection(0);
            }
        });
    }

    public void updateHeaderLabel(int type, String label) {
        this.headerLayout.changeHeaderLabel(type, label);
    }
}
