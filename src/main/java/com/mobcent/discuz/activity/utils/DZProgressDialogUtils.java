package com.mobcent.discuz.activity.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.KeyEvent;
import com.mobcent.lowest.base.utils.MCResource;

public class DZProgressDialogUtils {
    private static ProgressDialog myDialog;

    public static void showProgressDialog(Context context, String str, final AsyncTask obj) {
        myDialog = new ProgressDialog(context);
        myDialog.setProgressStyle(0);
        myDialog.setTitle(context.getResources().getString(MCResource.getInstance(context).getStringId("mc_forum_dialog_tip")));
        myDialog.setMessage(context.getResources().getString(MCResource.getInstance(context).getStringId(str)));
        myDialog.setIndeterminate(false);
        myDialog.getWindow().setType(2003);
        myDialog.setCancelable(false);
        myDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    DZProgressDialogUtils.hideProgressDialog();
                    if (!(obj == null || obj.getStatus() == Status.FINISHED)) {
                        obj.cancel(true);
                    }
                }
                return true;
            }
        });
        myDialog.show();
    }

    public static void hideProgressDialog() {
        if (myDialog != null) {
            myDialog.cancel();
        }
    }
}
