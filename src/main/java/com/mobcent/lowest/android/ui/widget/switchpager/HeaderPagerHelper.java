package com.mobcent.lowest.android.ui.widget.switchpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.widget.switchpager.HeaderPagerAdapter.HeaderPagerClickListener;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class HeaderPagerHelper {
    private Context context;
    private int currentPosition = 0;
    private List<SwitchModel> dataList = new ArrayList();
    private RadioGroup group = null;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                HeaderPagerHelper.this.startAutoScroll();
                HeaderPagerHelper.this.switchPager();
            }
        }
    };
    private LayoutInflater inflater = null;
    private ViewPager pager = null;
    private HeaderPagerAdapter pagerAdapter = null;
    private OnPageChangeListener pagerListener = new OnPageChangeListener() {
        public void onPageSelected(int position) {
            HeaderPagerHelper.this.currentPosition = position;
            HeaderPagerHelper.this.changeTitleText(HeaderPagerHelper.this.currentPosition);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    };
    private MCResource resource = null;
    private View rootView = null;
    private TextView titleText = null;

    public HeaderPagerHelper(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public View initView(Context context, List<SwitchModel> dataList, HeaderPagerClickListener headerPagerListener) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.rootView = this.inflater.inflate(this.resource.getLayoutId("mc_lowest_header_switch"), null);
        this.pager = (ViewPager) this.rootView.findViewById(this.resource.getViewId("pager"));
        this.group = (RadioGroup) this.rootView.findViewById(this.resource.getViewId("page_radio"));
        this.titleText = (TextView) this.rootView.findViewById(this.resource.getViewId("title_text"));
        int width = MCPhoneUtil.getDisplayWidth(context);
        int height = (int) (((float) width) * PlazaConstant.PLAZA_SWITCH_RADIO);
        LayoutParams lp = this.pager.getLayoutParams();
        lp.height = height;
        lp.width = width;
        this.pager.setLayoutParams(lp);
        this.pagerAdapter = new HeaderPagerAdapter(this.dataList, headerPagerListener);
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setOnPageChangeListener(this.pagerListener);
        resetRadioAndTitle();
        return this.rootView;
    }

    public void notifyDataSetChanged() {
        if (this.pagerAdapter != null) {
            this.pagerAdapter.notifyDataSetChanged();
        }
    }

    private void resetRadioAndTitle() {
        if (this.group != null) {
            this.group.removeAllViews();
            int count = this.dataList.size();
            for (int i = 0; i < count; i++) {
                RadioButton r = new RadioButton(this.context);
                r.setId(i);
                r.setButtonDrawable(17170445);
                r.setBackgroundResource(this.resource.getDrawableId("mc_forum_picture_rotate"));
                this.group.addView(r, new RadioGroup.LayoutParams(MCPhoneUtil.dip2px(this.context, 9.0f), MCPhoneUtil.dip2px(this.context, 6.0f)));
                if (i == 0) {
                    r.setChecked(true);
                    r.requestFocus();
                }
            }
            this.currentPosition = 0;
            this.pager.setCurrentItem(this.currentPosition);
            changeTitleText(this.currentPosition);
        }
    }

    public void changeTitleText(int position) {
        if (this.group != null && this.dataList.size() > position) {
            this.group.check(position);
            ((RadioButton) this.group.getChildAt(position)).setChecked(true);
            this.titleText.setText(((SwitchModel) this.dataList.get(position)).getDesc());
        }
    }

    public void startAutoScroll() {
        this.handler.sendEmptyMessageDelayed(1, 5000);
    }

    public void stopAutoScroll() {
        this.handler.removeMessages(1);
    }

    public void switchPager() {
        if (!this.dataList.isEmpty() && this.dataList.size() != 1) {
            if (this.currentPosition < this.dataList.size() - 1) {
                this.pager.setCurrentItem(this.currentPosition + 1);
            } else if (this.currentPosition == this.dataList.size() - 1) {
                this.pager.setCurrentItem(0);
            }
        }
    }
}
