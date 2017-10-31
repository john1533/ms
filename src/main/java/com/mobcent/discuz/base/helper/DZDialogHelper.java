package com.mobcent.discuz.base.helper;

import android.app.Dialog;
import android.view.WindowManager.LayoutParams;

public class DZDialogHelper {
    public static Dialog setDialogSystemAlert(Dialog dialog) {
        LayoutParams lps = dialog.getWindow().getAttributes();
        lps.type = 2003;
        dialog.getWindow().setAttributes(lps);
        try {
            dialog.show();
        } catch (Exception e) {
        }
        return dialog;
    }
}
