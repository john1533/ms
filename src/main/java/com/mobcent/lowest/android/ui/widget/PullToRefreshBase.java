package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout {
    public static final int BOTTOM_LOAD_FAIL = 4;
    public static final int BOTTOM_LOAD_FINISH = 3;
    public static final int BOTTOM_LOAD_MORE = 0;
    public static final int BOTTOM_NO_DATA = 2;
    public static final int BOTTOM_REFRESH = 1;
    static final float FRICTION = 2.0f;
    static final int MANUAL_REFRESHING = 3;
    public static final int MODE_BOTH = 3;
    public static final int MODE_PULL_DOWN_TO_REFRESH = 1;
    public static final int MODE_PULL_UP_TO_REFRESH = 2;
    static final int PULL_TO_REFRESH = 0;
    static final int REFRESHING = 2;
    static final int RELEASE_TO_REFRESH = 1;
    private int bottomState;
    private String buttonFailLabel;
    private String buttonIsEndLabel;
    private String buttonNodataLabel;
    private Context context;
    private int currentMode;
    private SmoothScrollRunnable currentSmoothScrollRunnable;
    private boolean disableScrollingWhileRefreshing = true;
    protected PullToRefreshLayout footerLayout;
    private final Handler handler = new Handler();
    private int headerHeight;
    private PullToRefreshLayout headerLayout;
    private float initialMotionY;
    private boolean isBeingDragged = false;
    private boolean isHand;
    private boolean isPullToRefreshEnabled = true;
    private float lastMotionX;
    private float lastMotionY;
    private ImageView mTopImageView;
    private MCResource mcResource;
    private int mode = 1;
    private OnBottomRefreshListener onBottomRefreshListener;
    private OnRefreshListener onRefreshListener;
    private String pullLabel;
    private String pullLabelbottom;
    T refreshableView;
    private String refreshingLabel;
    private String refreshingLabelbottom;
    private String releaseLabel;
    private int state = 0;
    private int touchSlop;

    public interface OnBottomRefreshListener {
        void onRefresh();
    }

    public interface OnLastItemVisibleListener {
        void onLastItemVisible();
    }

    public interface OnRefreshListener {
        void onRefresh();
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
                PullToRefreshBase.this.setHeaderScroll(this.currentY);
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
            if (PullToRefreshBase.this.bottomState != 3) {
                PullToRefreshBase.this.bottomState = 1;
                PullToRefreshBase.this.footerLayout.bottomRefreshing();
                PullToRefreshBase.this.onBottomRefresh();
            }
        }
    }

    protected abstract T createRefreshableView(Context context, AttributeSet attributeSet);

    protected abstract void gotoTop();

    protected abstract boolean isReadyForPullDown();

    protected abstract boolean onScroll(MotionEvent motionEvent);

    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    public PullToRefreshBase(Context context, int mode) {
        super(context);
        this.mode = mode;
        init(context, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setBottomState(int bottomState) {
        this.bottomState = bottomState;
    }

    public int getBottomState() {
        return this.bottomState;
    }

    public final T getRefreshableView() {
        return this.refreshableView;
    }

    public final boolean isPullToRefreshEnabled() {
        return this.isPullToRefreshEnabled;
    }

    public final boolean isDisableScrollingWhileRefreshing() {
        return this.disableScrollingWhileRefreshing;
    }

    public final boolean isRefreshing() {
        return this.state == 2 || this.state == 3;
    }

    public final void setDisableScrollingWhileRefreshing(boolean disableScrollingWhileRefreshing) {
        this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
    }

    public OnBottomRefreshListener getOnBottomRefreshListener() {
        return this.onBottomRefreshListener;
    }

    public void setOnBottomRefreshListener(OnBottomRefreshListener onBottomRefreshListener) {
        this.onBottomRefreshListener = onBottomRefreshListener;
    }

    public final void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    public final void setPullToRefreshEnabled(boolean enable) {
        this.isPullToRefreshEnabled = enable;
    }

    public final void setRefreshing() {
        setRefreshing(true);
    }

    public final void setRefreshing(boolean doScroll) {
        if (!isRefreshing()) {
            setRefreshingInternal(doScroll);
            this.state = 3;
        }
    }

    public final boolean hasPullFromTop() {
        return this.currentMode != 2;
    }

    public boolean isHand() {
        return this.isHand;
    }

    public void setHand(boolean isHand) {
        this.isHand = isHand;
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.mcResource = MCResource.getInstance(context);
        this.mode = 3;
        setOrientation(1);
        this.touchSlop = ViewConfiguration.getTouchSlop();
        this.refreshableView = createRefreshableView(context, attrs);
        addRefreshableView(context, this.refreshableView);
        this.pullLabel = context.getString(this.mcResource.getStringId("mc_forum_drop_dowm"));
        this.refreshingLabel = context.getString(this.mcResource.getStringId("mc_forum_doing_update"));
        this.releaseLabel = context.getString(this.mcResource.getStringId("mc_forum_release_update"));
        this.pullLabelbottom = context.getString(this.mcResource.getStringId("mc_forum_more"));
        this.refreshingLabelbottom = context.getString(this.mcResource.getStringId("mc_forum_doing_update"));
        this.buttonNodataLabel = context.getString(this.mcResource.getStringId("mc_forum_no_content"));
        this.buttonIsEndLabel = context.getString(this.mcResource.getStringId("mc_forum_loaded"));
        this.buttonFailLabel = context.getString(this.mcResource.getStringId("mc_forum_load_fail"));
        if (this.mode == 1 || this.mode == 3) {
            this.headerLayout = new PullToRefreshLayout(context, 1, this.releaseLabel, this.pullLabel, this.refreshingLabel);
            addView(this.headerLayout, 0, new LayoutParams(-1, -2));
            measureView(this.headerLayout);
            this.headerHeight = this.headerLayout.getMeasuredHeight();
        }
        if (this.mode == 2 || this.mode == 3) {
            this.footerLayout = new PullToRefreshLayout(context, 2, "", this.pullLabelbottom, this.refreshingLabelbottom);
            measureView(this.footerLayout);
            this.headerHeight = this.footerLayout.getMeasuredHeight();
            this.footerLayout.setOnClickListener(new clickToRefresh());
        }
        switch (this.mode) {
            case 2:
                setPadding(0, 0, 0, 0);
                break;
            case 3:
                setPadding(0, -this.headerHeight, 0, 0);
                break;
            default:
                setPadding(0, -this.headerHeight, 0, 0);
                break;
        }
        if (this.mode != 3) {
            this.currentMode = this.mode;
        }
        this.bottomState = 0;
    }

    public void onTheme() {
        this.headerLayout.onTheme();
        this.footerLayout.onTheme();
    }

    public void measureView(View child) {
        int childHeightSpec;
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void addContentView(View content) {
        addContentView(content, null);
    }

    public void addContentView(View content, View header) {
        ScrollView mScrollView = (ScrollView) getRefreshableView();
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(1);
        if (header != null) {
            linearLayout.addView(header);
        }
        linearLayout.addView(content);
        if (this.footerLayout != null) {
            linearLayout.addView(this.footerLayout, new LayoutParams(-1, -2));
        }
        mScrollView.addView(linearLayout, new LayoutParams(-1, -1));
        this.footerLayout.resetBottom();
    }

    public final boolean onTouchEvent(MotionEvent event) {
        if (!this.isPullToRefreshEnabled) {
            return false;
        }
        if (isRefreshing() && this.disableScrollingWhileRefreshing) {
            return true;
        }
        if (event.getAction() == 0 && event.getEdgeFlags() != 0) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                if (!isReadyForPull()) {
                    return false;
                }
                float y = event.getY();
                this.initialMotionY = y;
                this.lastMotionY = y;
                return true;
            case 1:
            case 3:
                if (!this.isBeingDragged) {
                    return false;
                }
                this.isBeingDragged = false;
                if (this.state != 1 || this.onRefreshListener == null) {
                    smoothScrollTo(0);
                } else {
                    setRefreshingInternal(true);
                    setHand(true);
                    this.onRefreshListener.onRefresh();
                    setHand(false);
                }
                return true;
            case 2:
                if (!this.isBeingDragged) {
                    return false;
                }
                this.lastMotionY = event.getY();
                pullEvent();
                return true;
            default:
                return false;
        }
    }

    public final boolean onInterceptTouchEvent(MotionEvent event) {
        onScroll(event);
        if (!this.isPullToRefreshEnabled) {
            return false;
        }
        if (isRefreshing() && this.disableScrollingWhileRefreshing) {
            return false;
        }
        int action = event.getAction();
        if (action == 3 || action == 1) {
            this.isBeingDragged = false;
            return false;
        } else if (action != 0 && this.isBeingDragged) {
            return true;
        } else {
            switch (action) {
                case 0:
                    if (isReadyForPull()) {
                        float y = event.getY();
                        this.initialMotionY = y;
                        this.lastMotionY = y;
                        this.lastMotionX = event.getX();
                        this.isBeingDragged = false;
                        break;
                    }
                    break;
                case 2:
                    if (isReadyForPull()) {
                        float y2 = event.getY();
                        float dy = y2 - this.lastMotionY;
                        float yDiff = Math.abs(dy);
                        float xDiff = Math.abs(event.getX() - this.lastMotionX);
                        if (yDiff > ((float) this.touchSlop) && yDiff > xDiff && ((this.mode == 1 || this.mode == 3) && dy >= 1.0E-4f && isReadyForPullDown())) {
                            this.lastMotionY = y2;
                            this.isBeingDragged = true;
                            if (this.mode == 3) {
                                this.currentMode = 1;
                                break;
                            }
                        }
                    }
                    break;
            }
            return this.isBeingDragged;
        }
    }

    protected void addRefreshableView(Context context, T refreshableView) {
        addView(refreshableView, new LayoutParams(-1, 0, CustomConstant.RATIO_ONE_HEIGHT));
    }

    protected void resetHeader() {
        this.state = 0;
        this.isBeingDragged = false;
        if (this.headerLayout != null) {
            this.headerLayout.reset(null);
        }
        smoothScrollTo(0);
    }

    protected void setRefreshingInternal(boolean doScroll) {
        this.state = 2;
        if (this.headerLayout != null) {
            this.headerLayout.refreshing();
        }
        if (doScroll) {
            smoothScrollTo(-this.headerHeight);
        }
    }

    protected final void setHeaderScroll(int y) {
        scrollTo(0, y);
    }

    protected final void smoothScrollTo(int y) {
        if (this.currentSmoothScrollRunnable != null) {
            this.currentSmoothScrollRunnable.stop();
        }
        if (getScrollY() != y) {
            this.currentSmoothScrollRunnable = new SmoothScrollRunnable(this.handler, getScrollY(), y);
            this.handler.post(this.currentSmoothScrollRunnable);
        }
    }

    private boolean pullEvent() {
        int newHeight;
        int oldHeight = getScrollY();
        switch (this.currentMode) {
            case 2:
                newHeight = Math.round(Math.max(this.initialMotionY - this.lastMotionY, 0.0f) / FRICTION);
                break;
            default:
                newHeight = Math.round(Math.min(this.initialMotionY - this.lastMotionY, 0.0f) / FRICTION);
                break;
        }
        setHeaderScroll(newHeight);
        if (newHeight != 0) {
            if (this.state == 0 && this.headerHeight < Math.abs(newHeight)) {
                this.state = 1;
                switch (this.currentMode) {
                    case 1:
                        this.headerLayout.releaseToRefresh();
                        return true;
                    default:
                        return true;
                }
            } else if (this.state == 1 && this.headerHeight >= Math.abs(newHeight)) {
                this.state = 0;
                switch (this.currentMode) {
                    case 1:
                        this.headerLayout.pullToRefresh(true);
                        return true;
                    default:
                        return true;
                }
            }
        }
        if (oldHeight == newHeight) {
            return false;
        }
        return true;
    }

    private boolean isReadyForPull() {
        switch (this.mode) {
            case 1:
                return isReadyForPullDown();
            case 3:
                return isReadyForPullDown();
            default:
                return false;
        }
    }

    public void onRefresh() {
        this.footerLayout.hideBottom();
        setRefreshingInternal(true);
        if (this.onRefreshListener != null) {
            setHand(false);
            this.onRefreshListener.onRefresh();
        }
    }

    public final void onRefreshComplete() {
        this.footerLayout.showBottom();
        if (this.state != 0) {
            resetHeader();
        }
    }

    public void onRefreshWithOutListener() {
        this.footerLayout.hideBottom();
        setRefreshingInternal(true);
        if (this.onRefreshListener != null) {
            setHand(false);
        }
    }

    public void onBottomRefreshExt() {
        this.bottomState = 1;
        this.footerLayout.bottomRefreshing();
        onBottomRefresh();
    }

    private void onBottomRefresh() {
        if (this.onBottomRefreshListener != null) {
            this.onBottomRefreshListener.onRefresh();
        }
    }

    public void onBottomRefreshComplete(int status) {
        onBottomRefreshComplete(status, null);
    }

    public void onBottomRefreshComplete(int status, String text) {
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
            default:
                this.bottomState = 0;
                return;
        }
    }

    public void setBackToTopView(ImageView mTopImageView) {
        this.mTopImageView = mTopImageView;
        mTopImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PullToRefreshBase.this.refreshableView.scrollTo(0, 0);
                PullToRefreshBase.this.gotoTop();
            }
        });
    }

    public void scrolToTop() {
        this.refreshableView.scrollTo(0, 0);
    }

    public void setLongClickable(boolean longClickable) {
        getRefreshableView().setLongClickable(longClickable);
    }
}
