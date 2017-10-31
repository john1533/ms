package com.mobcent.discuz.module.weather;

import android.content.Intent;
import android.view.View;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.lowest.android.ui.module.weather.helper.WeatherWidgetInflate;

public class WeatherModuleFragment extends BaseFragment {
    private WeatherWidgetInflate weatherInflate;

    protected String getRootLayoutName() {
        if (this.weatherInflate == null) {
            this.weatherInflate = new WeatherWidgetInflate(getActivity());
        }
        return this.weatherInflate.getLayoutName();
    }

    protected void initViews(View rootView) {
        this.weatherInflate.setUpView(rootView, 1);
        this.weatherInflate.hideTopBar(true);
    }

    protected void initActions(View rootView) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        this.weatherInflate.onActivityResult(requestCode, resultCode, intent);
    }
}
