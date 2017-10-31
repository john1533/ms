package com.mobcent.lowest.android.ui.module.place.module.route.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.mobcent.lowest.android.ui.module.place.activity.BasePlaceFragmentActivity;
import com.mobcent.lowest.android.ui.module.place.module.route.fragment.RouteFragment;

public class RouteActivity extends BasePlaceFragmentActivity {
    private RouteFragment contentFragment;
    private Button finishBtn;
    private LinearLayout routeContentLayout;

    protected void initData() {
        super.initData();
        this.contentFragment = new RouteFragment();
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_route"));
        this.finishBtn = (Button) findViewById("route_finish_btn");
        addFragment(this.resource.getViewId("route_content"), this.contentFragment);
    }

    protected void initActions() {
        this.finishBtn.setOnClickListener(this.clickListener);
    }

    protected void onViewClick(View v) {
        if (this.finishBtn.equals(v)) {
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
