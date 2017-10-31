package com.mobcent.lowest.android.ui.module.place.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCResource;

public class RouteLoadingUtil {
    private static RouteLoadingUtil loadingUtil;
    private Dialog dialog;
    private MCResource resource;

    private RouteLoadingUtil() {
    }

    public static RouteLoadingUtil getInstance() {
        if (loadingUtil == null) {
            loadingUtil = new RouteLoadingUtil();
        }
        return loadingUtil;
    }

    public void show(Context context) {
        this.dialog = creatDialog(context, "");
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    public void show(Context context, String msg, boolean isCancel, boolean isRight) {
        this.dialog = creatDialog(context, msg);
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    public void hide() {
        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
        }
    }

    private Dialog creatDialog(Context context, String msg) {
        this.resource = MCResource.getInstance(context);
        View v = LayoutInflater.from(context).inflate(this.resource.getLayoutId("mc_place_route_loading"), null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(this.resource.getViewId("mc_place_route_loading_view"));
        TextView tipTextView = (TextView) v.findViewById(this.resource.getViewId("mc_place_route_loading_text"));
        ((ImageView) v.findViewById(this.resource.getViewId("mc_place_route_loading_img"))).startAnimation(AnimationUtils.loadAnimation(context, this.resource.getAnimId("mc_place_route_loading_animation")));
        Dialog loadingDialog = new Dialog(context, this.resource.getStyleId("mc_place_route_loading_dialog"));
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LayoutParams(-1, -1));
        return loadingDialog;
    }
}
