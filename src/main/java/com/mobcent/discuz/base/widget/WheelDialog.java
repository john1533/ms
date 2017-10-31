package com.mobcent.discuz.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import com.mobcent.discuz.activity.view.MCWheelView;
import com.mobcent.discuz.activity.view.MCWheelView.OnWheelScrollListener;
import com.mobcent.discuz.base.adapter.WheelListAdapter;
import com.mobcent.lowest.base.utils.MCResource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WheelDialog extends Dialog {
    private boolean addYear;
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(MCWheelView wheel, int oldValue, int newValue) {
            if (!WheelDialog.this.wheelScrolled) {
                WheelDialog.this.updateStatus();
            }
        }
    };
    private Context context;
    private WheelDelegate delegate;
    private View mainView;
    private Button negativeButton;
    private Button positiveButton;
    private MCResource resource;
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(MCWheelView wheel) {
            WheelDialog.this.wheelScrolled = true;
        }

        public void onScrollingFinished(MCWheelView wheel) {
            WheelDialog.this.wheelScrolled = false;
            WheelDialog.this.updateStatus();
        }
    };
    private boolean wheelScrolled = false;
    private int yearNum;

    public interface OnWheelChangedListener {
        void onChanged(MCWheelView mCWheelView, int i, int i2);
    }

    public interface WheelDelegate {
        void negativeClickListener(View view);

        void positiveClickListener(View view);
    }

    public boolean isAddYear() {
        return this.addYear;
    }

    public void setAddYear(boolean addYear) {
        this.addYear = addYear;
    }

    public int getYearNum() {
        return this.yearNum;
    }

    public void setYearNum(int yearNum) {
        this.yearNum = yearNum;
    }

    public WheelDialog(Context context) {
        super(context);
        this.context = context;
    }

    public WheelDialog(Context context, View mainView, WheelDelegate delegate) {
        super(context);
        this.context = context;
        this.mainView = mainView;
        this.delegate = delegate;
    }

    public WheelDialog(Context context, int theme, View mainView) {
        super(context, theme);
        this.context = context;
        this.mainView = mainView;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resource = MCResource.getInstance(this.context);
        requestWindowFeature(1);
        setContentView(this.resource.getLayoutId("wheel_dialog_layout"));
        initWheel(this.resource.getViewId("mc_forum_wheel1"));
        initWheel(this.resource.getViewId("mc_forum_wheel2"));
        initWheel(this.resource.getViewId("mc_forum_wheel3"));
        this.positiveButton = (Button) findViewById(this.resource.getViewId("positiveButton"));
        this.negativeButton = (Button) findViewById(this.resource.getViewId("negativeButton"));
        this.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog.this.updateStatus();
                if (WheelDialog.this.delegate != null) {
                    WheelDialog.this.delegate.positiveClickListener(WheelDialog.this.mainView);
                }
                WheelDialog.this.dismiss();
            }
        });
        this.negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WheelDialog.this.mainView.setTag("");
                if (WheelDialog.this.delegate != null) {
                    WheelDialog.this.delegate.negativeClickListener(WheelDialog.this.mainView);
                }
                WheelDialog.this.dismiss();
            }
        });
    }

    private void initWheel(int id) {
        MCWheelView wheel = getWheel(id);
        List<String> list;
        int i;
        if (id == this.resource.getViewId("mc_forum_wheel1")) {
            list = new ArrayList();
            int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            if (this.addYear) {
                for (i = 1970; i <= getYearNum() + year; i++) {
                    list.add(i + this.resource.getString("mc_forum_anno_time_year"));
                }
            } else {
                for (i = 1970; i <= getYearNum() + year; i++) {
                    list.add(i + this.resource.getString("mc_forum_anno_time_year"));
                }
            }
            wheel.setAdapter(new WheelListAdapter(list));
            wheel.setCurrentItem(year - 1970);
        } else if (id == this.resource.getViewId("mc_forum_wheel2")) {
            list = new ArrayList();
            for (i = 1; i <= 12; i++) {
                list.add(i + this.resource.getString("mc_forum_anno_time_month"));
            }
            wheel.setAdapter(new WheelListAdapter(list));
            wheel.setCurrentItem(Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1);
        } else if (id == this.resource.getViewId("mc_forum_wheel3")) {
            list = new ArrayList();
            for (i = 1; i <= 31; i++) {
                list.add(i + this.resource.getString("mc_forum_anno_time_day"));
            }
            wheel.setAdapter(new WheelListAdapter(list));
            wheel.setCurrentItem(Integer.parseInt(new SimpleDateFormat("dd").format(new Date())) - 1);
        }
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private MCWheelView getWheel(int id) {
        return (MCWheelView) findViewById(id);
    }

    private void updateStatus() {
        this.mainView.setTag(getAllCode());
    }

    private String getAllCode() {
        return getCurrentItemContent(getWheel(this.resource.getViewId("mc_forum_wheel1"))) + getCurrentItemContent(getWheel(this.resource.getViewId("mc_forum_wheel2"))) + getCurrentItemContent(getWheel(this.resource.getViewId("mc_forum_wheel3")));
    }

    private String getCurrentItemContent(MCWheelView wheelView) {
        return formatDate(wheelView.getAdapter().getItem(wheelView.getCurrentItem()) + "");
    }

    private String formatDate(String date) {
        if (date.indexOf(this.resource.getString("mc_forum_anno_time_year")) > -1) {
            return date.substring(0, date.indexOf(this.resource.getString("mc_forum_anno_time_year"))) + "-";
        }
        if (date.indexOf(this.resource.getString("mc_forum_anno_time_month")) > -1) {
            return date.substring(0, date.indexOf(this.resource.getString("mc_forum_anno_time_month"))) + "-";
        }
        if (date.indexOf(this.resource.getString("mc_forum_anno_time_day")) > -1) {
            return date.substring(0, date.indexOf(this.resource.getString("mc_forum_anno_time_day")));
        }
        return date;
    }

    public void show() {
        super.show();
        if (this.mainView != null) {
            this.mainView.setSelected(true);
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.mainView != null) {
            this.mainView.setSelected(false);
        }
    }
}
