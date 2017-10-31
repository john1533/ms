package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.list.fragment.adapter.ProtalPicList1Adapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class MCHeaderPagerHelper {
    public String TAG = "MCHeaderPagerHelper";
    private final int TIME_DELAYED = 7000;
    private Context context;
    private int currentPosition = 0;
    private List<TopicModel> dataList = new ArrayList();
    private RadioGroup group = null;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MCHeaderPagerHelper.this.startAutoScroll();
                MCHeaderPagerHelper.this.switchPager();
            }
        }
    };
    private LayoutInflater inflater = null;
    private MCHeaderViewPager pager = null;
    private ProtalPicList1Adapter pagerAdapter = null;
    private OnPageChangeListener pagerListener = new OnPageChangeListener() {
        public void onPageSelected(int position) {
            MCLogUtil.e(MCHeaderPagerHelper.this.TAG, "HeaderView===========parent" + MCHeaderPagerHelper.this.parentPosition + "======" + position);
            MCHeaderPagerHelper.this.currentPosition = position;
            MCHeaderPagerHelper.this.changeTitleText(MCHeaderPagerHelper.this.currentPosition);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    };
    public int parentPosition = 0;
    private MCResource resource = null;
    private View rootView = null;
    private TextView titleText = null;

    public MCHeaderPagerHelper(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public View initView(Context context, String style) {
        int width;
        this.rootView = this.inflater.inflate(this.resource.getLayoutId("portal_recomm_item"), null);
        this.pager = (MCHeaderViewPager) this.rootView.findViewById(this.resource.getViewId("vPager"));
        this.group = (RadioGroup) this.rootView.findViewById(this.resource.getViewId("page_radio"));
        this.titleText = (TextView) this.rootView.findViewById(this.resource.getViewId("title_text"));
        if ("card".equals(style)) {
            width = MCPhoneUtil.getDisplayWidth(context) - MCPhoneUtil.getRawSize(context, 1, 16.0f);
            this.pager.setBackgroundResource(this.resource.getDrawableId("mc_forum_card_bg2"));
        } else {
            width = MCPhoneUtil.getDisplayWidth(context);
            this.rootView.setPadding(0, 0, 0, 0);
        }
        int height = (int) (((double) width) * 0.56d);
        LayoutParams lp = this.pager.getLayoutParams();
        lp.height = height;
        lp.width = width;
        this.pager.setLayoutParams(lp);
        this.pagerAdapter = new ProtalPicList1Adapter(this.dataList, style);
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setOnPageChangeListener(this.pagerListener);
        return this.rootView;
    }

    public void attachListView(PullToRefreshListView pullToRefreshListView) {
        if (this.pager != null) {
            this.pager.setPullToRefreshListView(pullToRefreshListView);
        }
    }

    public void notifyDataSetChanged() {
        if (this.pagerAdapter != null) {
            this.pagerAdapter.notifyDataSetChanged();
        }
    }

    public void setHeaderNoData() {
        if (this.pagerAdapter != null) {
            this.pagerAdapter.setDatas(new ArrayList());
        }
        notifyDataSetChanged();
    }

    public void setData(List<TopicModel> dataList, String style) {
        if (dataList == null || dataList.isEmpty()) {
            setHeaderNoData();
            return;
        }
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
        if (this.rootView != null && this.rootView.getVisibility() == 8) {
            this.rootView.setVisibility(0);
        }
        resetRadioAndTitle();
    }

    public void resetRadioAndTitle() {
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
            this.titleText.setText(((TopicModel) this.dataList.get(position)).getTitle());
        }
    }

    public void startAutoScroll() {
        stopAutoScroll();
        this.handler.sendEmptyMessageDelayed(1, 7000);
    }

    public void stopAutoScroll() {
        this.handler.removeMessages(1);
    }

    public void switchPager() {
        if (this.pager == null || this.dataList.isEmpty() || this.dataList.size() == 1) {
            stopAutoScroll();
            return;
        }
        changeTitleText(this.currentPosition);
        if (this.currentPosition < this.dataList.size() - 1) {
            this.pager.setCurrentItem(this.currentPosition + 1);
        } else if (this.currentPosition == this.dataList.size() - 1) {
            this.pager.setCurrentItem(0);
        }
    }
}
