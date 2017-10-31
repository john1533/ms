package com.mobcent.discuz.base.helper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.service.DraftService;
import com.mobcent.discuz.android.service.impl.DraftServiceImpl;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.module.draft.activity.DraftActivity;
import com.mobcent.lowest.base.utils.MCResource;

public class DraftHelper {

    public interface DraftDelegate {
        void onDraftAlertBack(TopicDraftModel topicDraftModel);
    }

    public static boolean isNeedAlertDialog(final Context activity, final int type, final DraftDelegate delegate) {
        final DraftService draftService = new DraftServiceImpl(activity);
        boolean needAlert = draftService.isHasDraftToAlert(type);
        if (needAlert) {
            Context context;
            final Context topActivity = DiscuzApplication._instance.getTopActivity();
            if (topActivity == null) {
                context = activity;
            } else {
                context = topActivity;
            }
            AlertDialog dialog = new Builder(context).setMessage(MCResource.getInstance(activity).getString("mc_forum_draft_reload_alert")).setPositiveButton(MCResource.getInstance(activity).getString("mc_forum_draft_look"), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    draftService.setLastIgnoreAlert(type);
                    if (topActivity != null) {
                        ActivityDispatchHelper.startActivity(topActivity, new Intent(topActivity, DraftActivity.class));
                        return;
                    }
                    ActivityDispatchHelper.startActivity(activity, new Intent(activity, DraftActivity.class));
                }
            }).setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    draftService.setLastIgnoreAlert(type);
                    delegate.onDraftAlertBack(null);
                }
            }).create();
            if (topActivity != null) {
                try {
                    dialog.show();
                } catch (Exception e) {
                    DZDialogHelper.setDialogSystemAlert(dialog);
                }
            } else {
                DZDialogHelper.setDialogSystemAlert(dialog);
            }
        }
        return needAlert;
    }
}
