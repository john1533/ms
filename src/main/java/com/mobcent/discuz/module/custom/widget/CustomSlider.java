package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomViewClickListener;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.List;

public class CustomSlider extends CustomBaseRelativeLayout {
    public String TAG = "CustomSlider";
    private HeaderPagerAdapter adapter;
    private int currentPosition;
    private List<ConfigComponentModel> datas;
    private RadioGroup group;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CustomSlider.this.startAutoScroll();
                CustomSlider.this.switchPager();
            }
        }
    };
    private LayoutParams lps;
    private ViewPager pager;
    private OnPageChangeListener pagerListener = new OnPageChangeListener() {
        public void onPageSelected(int position) {
            CustomSlider.this.currentPosition = position;
            CustomSlider.this.changeTitleText(CustomSlider.this.currentPosition);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    };
    private MCResource resource = MCResource.getInstance(getContext());
    private CustomBaseText titleText;

    public class HeaderPagerAdapter extends PagerAdapter {
        private List<ConfigComponentModel> datas;

        public HeaderPagerAdapter(List<ConfigComponentModel> datas) {
            this.datas = datas;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            CustomBaseImg imageView = new CustomBaseImg(container.getContext());
            imageView.setScaleType(ScaleType.CENTER_CROP);
            container.addView(imageView, new LayoutParams(-1, -1));
            imageView.loadImg(((ConfigComponentModel) this.datas.get(position)).getIcon());
            imageView.setOnClickListener(new CustomViewClickListener(CustomSlider.this.getContext(), (ConfigComponentModel) this.datas.get(position)));
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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

    public CustomSlider(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel data) {
        if (!MCListUtils.isEmpty(data.getComponentList())) {
            computeSelf();
            this.datas = data.getComponentList();
            this.pager = new ViewPager(getContext());
            this.group = new RadioGroup(getContext());
            this.group.setOrientation(0);
            this.group.setGravity(16);
            this.titleText = CustomHelper.createOverTitle(getContext());
            this.titleText.setBackgroundDrawable(null);
            this.lps = new LayoutParams(-1, -1);
            addView(this.pager, this.lps);
            LinearLayout bottomBox = new LinearLayout(getContext());
            bottomBox.setOrientation(0);
            this.lps = new LayoutParams(-1, MCPhoneUtil.dip2px(getContext(), 20.0f));
            this.lps.addRule(12, -1);
            bottomBox.setBackgroundResource(this.resource.getDrawableId("mc_forum_card_bg1"));
            addView(bottomBox, this.lps);
            bottomBox.addView(this.titleText, new LinearLayout.LayoutParams(-1, -1, CustomConstant.RATIO_ONE_HEIGHT));
            bottomBox.addView(this.group, new LinearLayout.LayoutParams(-2, -1, 0.0f));
            this.adapter = new HeaderPagerAdapter(this.datas);
            this.pager.setAdapter(this.adapter);
            this.pager.setOnPageChangeListener(this.pagerListener);
            setUpGroup(data.getComponentList());
            startAutoScroll();
        }
    }

    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    protected void computeSelf() {
        int height;
        setPadding(0, this.marginTopBottom, 0, this.marginTopBottom);
        if (CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(this.style)) {
            height = (int) (((float) this.screenWidth) * 0.5f);
        } else if (CustomConstant.STYLE_LAYOUT_STYLE_IMAGE.equals(this.style)) {
            height = (int) (((float) this.screenWidth) * CustomConstant.RATIO_SLIDER_IMAGE);
        } else {
            height = (int) (((float) this.screenWidth) * CustomConstant.RATIO_SLIDER);
        }
        setLayoutParams(new LinearLayout.LayoutParams(-1, height));
    }

    private void setUpGroup(List<ConfigComponentModel> componentList) {
        if (this.group != null) {
            this.group.removeAllViews();
            int count = componentList.size();
            for (int i = 0; i < count; i++) {
                RadioButton r = new RadioButton(getContext());
                r.setId(i);
                r.setButtonDrawable(17170445);
                r.setBackgroundResource(this.resource.getDrawableId("mc_forum_picture_rotate"));
                this.group.addView(r, new RadioGroup.LayoutParams(MCPhoneUtil.dip2px(getContext(), 9.0f), MCPhoneUtil.dip2px(getContext(), 6.0f)));
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
        if (this.group != null && this.datas.size() > position) {
            this.group.check(position);
            ((RadioButton) this.group.getChildAt(position)).setChecked(true);
            this.titleText.setText(((ConfigComponentModel) this.datas.get(position)).getDesc());
        }
    }

    public void startAutoScroll() {
        stopAutoScroll();
        this.handler.sendEmptyMessageDelayed(1, 5000);
    }

    public void stopAutoScroll() {
        this.handler.removeMessages(1);
    }

    public void switchPager() {
        if (!this.datas.isEmpty() && this.datas.size() != 1) {
            if (this.pager.getCurrentItem() < this.datas.size() - 1) {
                this.pager.setCurrentItem(this.pager.getCurrentItem() + 1);
            } else if (this.currentPosition == this.datas.size() - 1) {
                this.pager.setCurrentItem(0);
            }
        }
    }
}
