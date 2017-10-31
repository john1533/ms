package com.mobcent.lowest.android.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class SubNavScrollView extends HorizontalScrollView {
    private Activity activity;
    private List<Button> btnList;
    private ClickSubNavListener clickSubNavListener;
    private MCResource mcResource;
    private String[] subArr;
    private LinearLayout subNavMenu;
    private int visiableCount = 3;

    public interface ClickSubNavListener {
        void onClickSubNav(View view, int i);
    }

    public SubNavScrollView(Context context) {
        super(context);
    }

    public SubNavScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubNavScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initView(Activity activity, String[] subArr, int visiableCount, ClickSubNavListener clickSubNavListener) {
        this.btnList = new ArrayList();
        this.mcResource = MCResource.getInstance(activity);
        this.activity = activity;
        this.visiableCount = visiableCount;
        this.subArr = subArr;
        this.clickSubNavListener = clickSubNavListener;
        setBackgroundDrawable(this.mcResource.getDrawable("mc_forum_tab3_style_bg"));
        createNavView();
    }

    private void createNavView() {
        this.subNavMenu = new LinearLayout(this.activity);
        int count = this.subArr.length;
        for (int i = 0; i < count; i++) {
            RelativeLayout rl = new RelativeLayout(this.activity);
            LayoutParams rllp = new LayoutParams((int) (((float) MCPhoneUtil.getDisplayWidth(this.activity)) / ((float) this.visiableCount)), 40);
            rllp.gravity = 17;
            rl.setLayoutParams(rllp);
            Button navBtn = getButton(i);
            this.btnList.add(navBtn);
            ((Button) this.btnList.get(0)).setSelected(true);
            ((Button) this.btnList.get(0)).setTextColor(this.mcResource.getColor("mc_forum_list10_button_press"));
            rl.addView(navBtn);
            this.subNavMenu.addView(rl);
        }
        addView(this.subNavMenu);
    }

    public void setFadingEdgeLength(int length) {
        super.setFadingEdgeLength(0);
    }

    private Button getButton(int i) {
        Button navBtn = new Button(this.activity);
        float btnW = (((float) MCPhoneUtil.getDisplayWidth(this.activity)) / ((float) this.visiableCount)) / 2.0f;
        RelativeLayout.LayoutParams btnLp = new RelativeLayout.LayoutParams((int) btnW, -1);
        btnLp.setMargins(((int) btnW) / 2, 0, ((int) btnW) / 2, 0);
        navBtn.setLayoutParams(btnLp);
        navBtn.setText(this.subArr[i]);
        navBtn.setTag(Integer.valueOf(i));
        navBtn.setTextSize(16.0f);
        navBtn.setTextColor(this.mcResource.getColor("mc_forum_list10_list_text_color"));
        navBtn.setBackgroundDrawable(this.mcResource.getDrawable("mc_forum_tab3_style"));
        navBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubNavScrollView.this.onNavClick(((Integer) v.getTag()).intValue());
                SubNavScrollView.this.clickSubNavListener.onClickSubNav(v, ((Integer) v.getTag()).intValue());
            }
        });
        return navBtn;
    }

    public void onNavClick(int position) {
        for (int i = 0; i < this.btnList.size(); i++) {
            Button btn = (Button) this.btnList.get(i);
            btn.setPressed(false);
            btn.setSelected(false);
            btn.setFocusable(false);
            btn.setTextColor(this.mcResource.getColor("mc_forum_list10_list_text_color"));
        }
        ((Button) this.btnList.get(position)).setSelected(true);
        ((Button) this.btnList.get(position)).setTextColor(this.mcResource.getColor("mc_forum_list10_button_press"));
    }
}
