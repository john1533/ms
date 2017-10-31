package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.mobcent.lowest.base.model.FlowTag;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PullToRefreshWaterFall extends PullToRefreshBase<ScrollView> {
    public static final int DRAW_ADD = 1;
    public static final int DRAW_REFRESH = 0;
    private final int FLING_DOWN = 1;
    private final int FLING_UP = 2;
    private int columnCount = 3;
    private List<LinearLayout> columnLayoutList;
    private int columnSpace = 5;
    private int columnWidth;
    private Context context;
    private int currentFlingState = 1;
    private int currentPosition;
    private final OnRefreshListener defaultOnRefreshListener = new OnRefreshListener() {
        public void onRefresh() {
            PullToRefreshWaterFall.this.onRefreshComplete();
        }
    };
    private int expandHeight = 480;
    private List<FlowTag> flowTagList;
    private boolean isFlingFinished;
    private boolean isHasBorder = true;
    private boolean isLoadOrRecycle;
    private Handler mHandler;
    private Timer mTimer;
    private int marginTop;
    private int oldY = 0;
    private OnLoadItemListener onLoadItemListener;
    private int preYForScrollViewStateCompare;
    private int rowSpace = 5;

    public interface OnLoadItemListener {
        void loadImage(String str);

        void loadLayout(LinearLayout linearLayout, FlowTag flowTag);

        void onItemClick(int i, FlowTag flowTag);

        void recycleImage(String str);
    }

    class ViewRecycler {
        SparseArray<View> recycleView = new SparseArray();

        ViewRecycler() {
        }

        private View getRecycleView(int position) {
            int size = this.recycleView.size();
            if (size <= 0) {
                return null;
            }
            for (int i = 0; i < size; i++) {
                int fromPosition = this.recycleView.keyAt(i);
                View view = (View) this.recycleView.get(fromPosition);
                if (fromPosition == position) {
                    this.recycleView.remove(fromPosition);
                    return view;
                }
            }
            int index = size - 1;
            View r = (View) this.recycleView.valueAt(index);
            this.recycleView.remove(this.recycleView.keyAt(index));
            return r;
        }

        private void addRecycleView(View scrap, int position) {
            this.recycleView.put(position, scrap);
        }

        private void clearRcycleView() {
            this.recycleView.clear();
        }
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

    public void setColumnLayoutList(List<LinearLayout> columnLayoutList) {
        this.columnLayoutList = columnLayoutList;
    }

    public void setExpandHeight(int expandHeight) {
        this.expandHeight = expandHeight;
    }

    public boolean isHasBorder() {
        return this.isHasBorder;
    }

    public void setHasBorder(boolean isHasBorder) {
        this.isHasBorder = isHasBorder;
    }

    public PullToRefreshWaterFall(Context context) {
        super(context);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshWaterFall(Context context, int mode) {
        super(context, mode);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshWaterFall(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    private void initData() {
        this.flowTagList = new ArrayList();
        this.mHandler = new Handler();
        this.expandHeight = MCPhoneUtil.getRawSize(this.context, 1, (float) this.expandHeight);
    }

    public void initView(Context context, View header) {
        this.context = context;
        initData();
        int columnSpacePx = MCPhoneUtil.getRawSize(context, 1, (float) this.columnSpace);
        if (this.isHasBorder) {
            this.columnWidth = (getDisplay(context).getWidth() - ((this.columnCount + 1) * columnSpacePx)) / this.columnCount;
        } else {
            this.columnWidth = (getDisplay(context).getWidth() - ((this.columnCount - 1) * columnSpacePx)) / this.columnCount;
        }
        this.marginTop = MCPhoneUtil.getRawSize(context, 1, (float) this.rowSpace);
        LinearLayout rootLayout = new LinearLayout(context);
        LayoutParams rootLp = new LayoutParams(-1, -2);
        rootLayout.setOrientation(0);
        rootLayout.setLayoutParams(rootLp);
        this.columnLayoutList = new ArrayList();
        for (int i = 0; i < this.columnCount; i++) {
            LinearLayout columnLayout = new LinearLayout(context);
            LayoutParams columnLp = new LayoutParams(this.columnWidth, -2);
            columnLp.setMargins(columnSpacePx, 0, 0, 0);
            columnLayout.setOrientation(1);
            columnLayout.setLayoutParams(columnLp);
            rootLayout.addView(columnLayout);
            columnLayout.setTag(Integer.valueOf(this.marginTop));
            this.columnLayoutList.add(columnLayout);
        }
        if (header != null) {
            this.expandHeight += header.getMeasuredHeight();
        }
        addContentView(rootLayout, header);
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                int nowY = ((ScrollView) PullToRefreshWaterFall.this.refreshableView).getScrollY();
                if (PullToRefreshWaterFall.this.oldY == nowY) {
                    PullToRefreshWaterFall.this.isFlingFinished = true;
                } else {
                    PullToRefreshWaterFall.this.isFlingFinished = false;
                    PullToRefreshWaterFall.this.oldY = nowY;
                }
                if (PullToRefreshWaterFall.this.isFlingFinished) {
                    if (PullToRefreshWaterFall.this.isLoadOrRecycle) {
                        MCLogUtil.i("nowY", "加载");
                        PullToRefreshWaterFall.this.preYForScrollViewStateCompare = nowY;
                        for (int i = 0; i < PullToRefreshWaterFall.this.flowTagList.size(); i++) {
                            PullToRefreshWaterFall.this.checkVisible((FlowTag) PullToRefreshWaterFall.this.flowTagList.get(i));
                        }
                    }
                    PullToRefreshWaterFall.this.isLoadOrRecycle = false;
                    return;
                }
                PullToRefreshWaterFall.this.isLoadOrRecycle = true;
            }
        }, 0, 200);
    }

    public void initView(Context context) {
        initView(context, null);
    }

    public void onDrawWaterFall(List modelList, int type) {
        int i;
        List<FlowTag> flowTagListTemp = new ArrayList();
        for (i = 0; i < modelList.size(); i++) {
            flowTagListTemp.add((FlowTag) modelList.get(i));
        }
        switch (type) {
            case 0:
                this.preYForScrollViewStateCompare = 0;
                for (i = 0; i < this.columnLayoutList.size(); i++) {
                    ((LinearLayout) this.columnLayoutList.get(i)).setTag(Integer.valueOf(0));
                    ((LinearLayout) this.columnLayoutList.get(i)).removeAllViews();
                }
                for (int j = 0; j < this.flowTagList.size(); j++) {
                    this.onLoadItemListener.recycleImage(((FlowTag) this.flowTagList.get(j)).getThumbnailUrl());
                }
                this.flowTagList.clear();
                loadLayout(flowTagListTemp, type);
                this.flowTagList.addAll(flowTagListTemp);
                return;
            case 1:
                loadLayout(flowTagListTemp, type);
                this.flowTagList.addAll(flowTagListTemp);
                return;
            default:
                return;
        }
    }

    private void loadLayout(List<FlowTag> flowTagListTemp, int type) {
        for (int i = 0; i < flowTagListTemp.size(); i++) {
            int imageHeight;
            this.currentPosition = i;
            final int position = this.flowTagList.size() + i;
            final FlowTag flowTag = (FlowTag) flowTagListTemp.get(i);
            float ratio = flowTag.getRatio();
            if (ratio == 0.0f) {
                imageHeight = this.columnWidth * 1;
            } else if (ratio > 2.0f) {
                imageHeight = this.columnWidth * 2;
            } else {
                imageHeight = (int) (((float) this.columnWidth) * ratio);
            }
            LayoutParams lp = new LayoutParams(-1, imageHeight);
            lp.topMargin = this.marginTop;
            LinearLayout linearLayout = new LinearLayout(this.context);
            linearLayout.setBackgroundColor(-1);
            linearLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PullToRefreshWaterFall.this.onLoadItemListener.onItemClick(position, flowTag);
                }
            });
            this.onLoadItemListener.loadLayout(linearLayout, flowTag);
            getShortColumnLayout(flowTag, imageHeight).addView(linearLayout, lp);
        }
    }

    private LinearLayout getShortColumnLayout(FlowTag flowTag, int imageHeight) {
        int shortPosition = 0;
        int shortHeight = 0;
        for (int i = 0; i < this.columnLayoutList.size(); i++) {
            int height = ((Integer) ((LinearLayout) this.columnLayoutList.get(i)).getTag()).intValue();
            if (i == 0) {
                shortHeight = height;
            } else if (shortHeight > height) {
                shortHeight = height;
                shortPosition = i;
            }
        }
        flowTag.setMarginTop((((Integer) ((LinearLayout) this.columnLayoutList.get(shortPosition)).getTag()).intValue() + imageHeight) + this.marginTop);
        ((LinearLayout) this.columnLayoutList.get(shortPosition)).setTag(Integer.valueOf((((Integer) ((LinearLayout) this.columnLayoutList.get(shortPosition)).getTag()).intValue() + imageHeight) + this.marginTop));
        checkVisible(flowTag);
        return (LinearLayout) this.columnLayoutList.get(shortPosition);
    }

    private void checkVisible(final FlowTag flowTag) {
        if (flowTag.getMarginTop() <= this.preYForScrollViewStateCompare - this.expandHeight || flowTag.getMarginTop() >= (this.preYForScrollViewStateCompare + getDisplay(this.context).getHeight()) + this.expandHeight) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PullToRefreshWaterFall.this.onLoadItemListener.recycleImage(flowTag.getThumbnailUrl());
                }
            });
        } else {
            this.onLoadItemListener.loadImage(flowTag.getThumbnailUrl());
        }
    }

    protected void gotoTop() {
        this.preYForScrollViewStateCompare = 0;
    }

    public void onDestroyView() {
        for (int i = 0; i < this.flowTagList.size(); i++) {
            if (this.onLoadItemListener != null) {
                this.onLoadItemListener.recycleImage(((FlowTag) this.flowTagList.get(i)).getThumbnailUrl());
            }
        }
        if (this.mTimer != null) {
            MCLogUtil.i("onDestroyView", "close timer");
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    protected boolean onScroll(MotionEvent event) {
        return false;
    }

    private Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        return display;
    }

    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        return new ScrollView(context, attrs);
    }

    protected boolean isReadyForPullDown() {
        return ((ScrollView) this.refreshableView).getScrollY() == 0;
    }
}
