package com.mobcent.lowest.android.ui.module.place.dialog;

import android.app.AlertDialog;
import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;

public class BasePlaceFiledDialog extends AlertDialog {
    protected String allStr;
    protected String defaultStr;
    protected String rangeStr;
    protected MCResource resource;

    public BasePlaceFiledDialog(Context context) {
        this(context, 0);
    }

    public BasePlaceFiledDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected void init(Context context) {
        this.resource = MCResource.getInstance(context);
        this.allStr = this.resource.getString("mc_place_around_all");
        this.rangeStr = this.resource.getString("mc_place_around_range");
        this.defaultStr = this.resource.getString("mc_place_around_default");
    }
}
