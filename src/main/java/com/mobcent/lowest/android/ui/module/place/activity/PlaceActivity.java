package com.mobcent.lowest.android.ui.module.place.activity;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.module.around.fragment.PlaceHomeAroundFragment;
import com.mobcent.lowest.android.ui.module.place.module.route.fragment.RouteFragment;

public class PlaceActivity extends BasePlaceFragmentActivity {
    private RelativeLayout aroundBox;
    private Fragment aroundFragment;
    private ImageView aroundIcon;
    private TextView aroundText;
    private Button backBtn;
    private RelativeLayout containerBox;
    private RelativeLayout routeBox;
    private Fragment routeFragment;
    private ImageView routeIcon;
    private TextView routeText;

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_activity"));
        this.containerBox = (RelativeLayout) findViewById("container_layout");
        this.aroundBox = (RelativeLayout) findViewById("around_lift_btn");
        this.routeBox = (RelativeLayout) findViewById("route_plane_btn");
        this.backBtn = (Button) findViewById("back_btn");
        this.aroundText = (TextView) findViewById("around_text");
        this.routeText = (TextView) findViewById("route_text");
        this.aroundIcon = (ImageView) findViewById("around_icon");
        this.routeIcon = (ImageView) findViewById("route_icon");
        this.aroundFragment = new PlaceHomeAroundFragment();
        this.routeFragment = new RouteFragment();
        addFragment(this.containerBox.getId(), this.routeFragment);
        addFragment(this.containerBox.getId(), this.aroundFragment);
    }

    protected void initActions() {
        this.aroundBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    PlaceActivity.this.aroundText.setSelected(true);
                    PlaceActivity.this.aroundIcon.setSelected(true);
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    PlaceActivity.this.aroundText.setSelected(false);
                    PlaceActivity.this.aroundIcon.setSelected(false);
                }
                return false;
            }
        });
        this.routeBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    PlaceActivity.this.routeText.setSelected(true);
                    PlaceActivity.this.routeIcon.setSelected(true);
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    PlaceActivity.this.routeText.setSelected(false);
                    PlaceActivity.this.routeIcon.setSelected(false);
                }
                return false;
            }
        });
        this.aroundBox.setOnClickListener(this.clickListener);
        this.routeBox.setOnClickListener(this.clickListener);
        this.backBtn.setOnClickListener(this.clickListener);
        this.aroundBox.performClick();
        selectAroundBox();
    }

    protected void onViewClick(View v) {
        if (this.aroundBox.equals(v)) {
            aroundBoxClickDo();
            selectAroundBox();
        } else if (this.routeBox.equals(v)) {
            routeBoxClickDo();
            selectRouteBox();
        } else if (this.backBtn.equals(v)) {
            onBackPressed();
        }
    }

    private void aroundBoxClickDo() {
        if (this.aroundFragment.isHidden()) {
            showFragment(this.aroundFragment);
        }
        if (!this.routeFragment.isHidden()) {
            hideFragment(this.routeFragment);
        }
    }

    private void routeBoxClickDo() {
        if (this.routeFragment.isHidden()) {
            showFragment(this.routeFragment);
        }
        if (!this.aroundFragment.isHidden()) {
            hideFragment(this.aroundFragment);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void selectAroundBox() {
        this.aroundBox.setSelected(true);
        this.aroundText.setSelected(true);
        this.aroundIcon.setSelected(true);
        this.routeBox.setSelected(false);
        this.routeText.setSelected(false);
        this.routeIcon.setSelected(false);
    }

    private void selectRouteBox() {
        this.aroundBox.setSelected(false);
        this.aroundText.setSelected(false);
        this.aroundIcon.setSelected(false);
        this.routeBox.setSelected(true);
        this.routeText.setSelected(true);
        this.routeIcon.setSelected(true);
    }
}
