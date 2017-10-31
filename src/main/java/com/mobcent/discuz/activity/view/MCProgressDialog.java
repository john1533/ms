package com.mobcent.discuz.activity.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class MCProgressDialog extends AlertDialog {
    private Context context;
    private MCProgressBar progress;
    private MCResource resource;

    public MCProgressDialog(Context context) {
        this(context, MCResource.getInstance(context.getApplicationContext()).getStyleId("mc_forum_dialog_top_menu"));
    }

    public MCProgressDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.progress = new MCProgressBar(this.context);
        this.progress.setBackgroundResource(this.resource.getDrawableId("mc_forum_loading1"));
        setContentView(this.progress, new LayoutParams(MCPhoneUtil.dip2px(this.context, 40.0f), MCPhoneUtil.dip2px(this.context, 40.0f)));
    }

    public void show() {
        super.show();
        if (this.progress != null) {
            this.progress.show();
        }
    }

    public void dismiss() {
        if (this.progress != null) {
            this.progress.hide();
        }
        super.dismiss();
    }
}
