package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

import java.util.List;

public class MCTabBarScrollView extends LinearLayout {
    private String TAG = "MCTabBarScrollView";
    private Drawable arrowImage;
    private int arrowImageHeight;
    private int arrowImageWidth;
//    private LinearLayout arrowItmBox;
    private Button arrow;
    private LinearLayout arrowLinearLayout;
    private int arrowMarginTop = 0;
    private float arrowRatio = CustomConstant.RATIO_ONE_LOW;
    private float btnWidth;
    private ClickSubNavListener clickSubNavListener;
    private Context context;
    private int currentNavPosition = 0;
    private boolean isContainArrow = false;
    private int preNavPosition = 0;
//    private FrameLayout rootView;
    private Drawable tabBoxBackground;
    private int tabBoxBackgroundHeight;
    private int tabBoxBackgroundWidth;
    private LinearLayout tabBoxLayout;
    private int visiableCount = 4;
    private int tabCount;

    private HorizontalScrollView horizontalScrollView;

    public interface ClickSubNavListener {
        void initTextView(TextView textView);

        void onClickSubNav(View view, int i, TextView textView);
    }

    public void setTabBoxView(Drawable background, int height, int width) {
        this.tabBoxBackgroundHeight = height + 1;
        this.tabBoxBackground = background;
        this.tabBoxBackgroundWidth = width-dip2px(30);
    }

    public void setArrowView(Drawable arrow, int height, int width) {
        this.arrowImage = arrow;
        this.arrowImageHeight = height;
        this.arrowImageWidth = width;
    }

    public int getArrowMarginTop() {
        return this.arrowMarginTop;
    }

    public void setArrowMarginTop(int arrowMarginTop) {
        this.arrowMarginTop = arrowMarginTop;
    }

    public boolean isContainArrow() {
        return this.isContainArrow;
    }

    public void setContainArrow(boolean isContainArrow) {
        this.isContainArrow = isContainArrow;
    }

    public MCTabBarScrollView(Context context) {
        super(context);
    }

    public MCTabBarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCTabBarScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(final Context context, List<String> tabList, int visiableCount, ClickSubNavListener clickSubNavListener) {
        this.visiableCount = visiableCount;
        this.context = context;
        this.clickSubNavListener = clickSubNavListener;

//        this.rootView = new FrameLayout(context);
        LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        this.rootView.setLayoutParams(lp2);

        this.tabBoxLayout = new LinearLayout(context);
        this.tabBoxLayout.setBackground(this.tabBoxBackground);
        this.tabBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.tabBoxLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, this.tabBoxBackgroundHeight));
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.addView(this.tabBoxLayout);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);

        FrameLayout.LayoutParams linearLp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        linearLp.gravity = Gravity.LEFT;
//        this.rootView.addView(horizontalScrollView, linearLp);



        linearLp = new FrameLayout.LayoutParams(dip2px(30),LayoutParams.MATCH_PARENT);
        linearLp.gravity = Gravity.RIGHT;
//        this.rootView.addView(toggleTextView, linearLp);

        setFadingEdgeLength(0);
        addView(this.horizontalScrollView);

        this.arrowLinearLayout = new LinearLayout(context);
        lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.arrowLinearLayout.setLayoutParams(lp2);
        createNavTagView(tabList);
        addView(this.arrowLinearLayout,lp2);

        requestLayout();
        postInvalidate();


    }

    protected void createNavTagView(List<String> tabList) {
        int count = tabCount = tabList.size();
        for (int i = 0; i < count; i++) {
            CharSequence cs = (CharSequence) tabList.get(i);
            final TextView tabView = getTabView(cs);
            tabView.setText((CharSequence) tabList.get(i));
            tabView.setTag(Integer.valueOf(i));

            this.tabBoxLayout.addView(tabView);
            tabView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MCTabBarScrollView.this.selectCurrentTab(((Integer) tabView.getTag()).intValue());
                }
            });
        }
//        this.arrowLinearLayout.getLayoutParams().width = ((int) this.btnWidth) * count;
//        this.arrowItmBox = new LinearLayout(this.context);
//        this.arrowItmBox.setLayoutParams(new RelativeLayout.LayoutParams((int) this.btnWidth, this.arrowImageHeight));
//        this.arrowItmBox.setGravity(1);
        arrow = new Button(this.context);
        LinearLayout.LayoutParams arrowLp = new LinearLayout.LayoutParams(this.arrowImageWidth == 0 ? (int) (this.btnWidth * this.arrowRatio) : (int) (((float) this.arrowImageWidth) * this.arrowRatio), this.arrowImageHeight);
        arrowLp.leftMargin = (int) ((this.btnWidth * (CustomConstant.RATIO_ONE_HEIGHT - this.arrowRatio)) / 2.0f);
//        arrowLp.leftMargin = dip2px(15);

        arrow.setBackground(this.arrowImage);
        arrow.setLayoutParams(arrowLp);
//        this.arrowItmBox.addView(arrow);
//        this.arrowLinearLayout.addView(this.arrowItmBox);
        this.arrowLinearLayout.addView(arrow);


    }

    public void computeScroll() {
        super.computeScroll();
    }

//    public void fling(int velocityX) {
//        super.fling(velocityX);
//    }

    private TextView getTabView(CharSequence cs) {
        TextView tabView = new TextView(this.context);
        tabView.setIncludeFontPadding(false);
        tabView.setSingleLine(true);
//        tabView.setBackgroundResource(17170445);
        tabView.setPadding(dip2px(2), 0, dip2px(2), 0);
        tabView.setEllipsize(TruncateAt.END);

        this.btnWidth = ((float) this.tabBoxBackgroundWidth) / ((float) this.visiableCount);
        tabView.setLayoutParams(new ViewGroup.LayoutParams((int) this.btnWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        tabView.setGravity(Gravity.CENTER);

        MCLogUtil.v("RecordUtils", "getTabView" );

        this.clickSubNavListener.initTextView(tabView);
        return tabView;
    }

    public Animation getAnimation(View view) {
        TranslateAnimation ta = new TranslateAnimation((float) this.preNavPosition, (float) this.currentNavPosition, 0.0f, 0.0f);
        ta.setDuration(300);
        ta.setFillAfter(true);
        ta.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                MCTabBarScrollView.this.preNavPosition = MCTabBarScrollView.this.currentNavPosition;
            }
        });
        return ta;
    }

    public void selectCurrentTab(int position) {
        if (this.tabBoxLayout != null && this.tabBoxLayout.getChildCount() != 0 && position <= this.tabBoxLayout.getChildCount() - 1) {
            TextView currentTab = (TextView) this.tabBoxLayout.getChildAt(position);

            int w = (int) (this.btnWidth * ((float) position));
//            Log.v("RecordUtils", "w:" + w);
//            Log.v("RecordUtils","horizontalScrollView:"+horizontalScrollView.getWidth());
//            Log.v("RecordUtils","tabBoxLayout:"+tabBoxLayout.getWidth());
            if(horizontalScrollView.getWidth()>=tabBoxLayout.getWidth()){
                this.currentNavPosition = w;
            }else if(w+horizontalScrollView.getWidth()>=tabBoxLayout.getWidth()){
                this.currentNavPosition = horizontalScrollView.getWidth()-(int)((tabCount - position) * this.btnWidth);
            }else{
                this.currentNavPosition = 0;
            }

//            this.currentNavPosition = (int) (this.btnWidth * ((float) position));
//            this.arrowItmBox.startAnimation(getAnimation(currentTab));
            this.arrow.startAnimation(getAnimation(currentTab));
            horizontalScrollView.smoothScrollTo((int)(position*btnWidth),0);
//            final int to = (int) ((((float) (this.currentNavPosition + this.currentNavPosition)) + this.btnWidth) / 2.0f);
            for (int i = 0; i < this.tabBoxLayout.getChildCount(); i++) {
                this.clickSubNavListener.initTextView((TextView) this.tabBoxLayout.getChildAt(i));
            }
            postInvalidate();

            this.clickSubNavListener.onClickSubNav(currentTab, ((Integer) currentTab.getTag()).intValue(), currentTab);
        }
    }

//    public void selectCurrentTabNoAnimation(int position) {
//        if (this.tabBoxLayout != null && this.tabBoxLayout.getChildCount() != 0 && position <= this.tabBoxLayout.getChildCount() - 1) {
//            TextView currentTab = (TextView) this.tabBoxLayout.getChildAt(position);
//            this.currentNavPosition = (int) (this.btnWidth * ((float) position));
//            Animation animation = getAnimation(currentTab);
//            animation.setDuration(0);
//            this.arrow.startAnimation(getAnimation(currentTab));
//            for (int i = 0; i < this.tabBoxLayout.getChildCount(); i++) {
//                this.clickSubNavListener.initTextView((TextView) this.tabBoxLayout.getChildAt(i));
//            }
//            this.clickSubNavListener.onClickSubNav(currentTab, ((Integer) currentTab.getTag()).intValue(), currentTab);
//        }
//    }

//    public void addNavTag(View view) {
//        this.tabBoxLayout.addView(view);
//    }

    public void addNavTag(int position, View view) {
        this.tabBoxLayout.addView(view, position);
    }

    public void removeNavTag(int position) {
        this.tabBoxLayout.removeViewAt(position);
    }

    public void clearNavTag() {
        this.tabBoxLayout.removeAllViews();
    }

    public void setArrowWidthRatio(float arrowRatio) {
        this.arrowRatio = arrowRatio;
    }

    private int dip2px(int dip) {
        return MCPhoneUtil.dip2px(getContext(), (float) dip);
    }
}
