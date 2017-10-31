package com.mobcent.discuz.module.custom.widget.delegate;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;

public class CustomViewClickListener implements OnClickListener {
    private ConfigComponentModel component;
    private Context context;

    public CustomViewClickListener(Context context, ConfigComponentModel component) {
        this.context = context;
        this.component = component;
    }

    public void onClick(View v) {
        ActivityDispatchHelper.dispatchActivity(this.context, this.component);
    }
}
