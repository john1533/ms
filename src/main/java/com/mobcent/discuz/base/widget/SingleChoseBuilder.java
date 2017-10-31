package com.mobcent.discuz.base.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;

public class SingleChoseBuilder extends Builder {
    private ClassifiedModel classifiedModel;
    private String[] items;
    private MCResource resource;
    private int selected = -1;
    private TextView tv;

    public SingleChoseBuilder(Context context, String[] items, TextView tv, String radioChoice, ClassifiedModel classifiedModel) {
        super(context);
        this.tv = tv;
        this.items = items;
        this.classifiedModel = classifiedModel;
        this.resource = MCResource.getInstance(context);
    }

    public AlertDialog create() {
        final StringBuilder sb = new StringBuilder();
        setSingleChoiceItems(this.items, -1, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SingleChoseBuilder.this.selected = which;
                sb.append(which + AdApiConstant.RES_SPLIT_COMMA);
            }
        });
        setIcon(17301543);
        setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (SingleChoseBuilder.this.classifiedModel != null && !SingleChoseBuilder.this.classifiedModel.equals("") && SingleChoseBuilder.this.selected > -1) {
                    SingleChoseBuilder.this.classifiedModel.setClassifiedValue(SingleChoseBuilder.this.selected + "");
                    SingleChoseBuilder.this.tv.setText(SingleChoseBuilder.this.items[SingleChoseBuilder.this.selected]);
                }
            }
        });
        setNegativeButton(this.resource.getStringId("mc_forum_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (SingleChoseBuilder.this.classifiedModel != null && !SingleChoseBuilder.this.classifiedModel.equals("")) {
                    SingleChoseBuilder.this.classifiedModel.setClassifiedValue("");
                    SingleChoseBuilder.this.tv.setText("");
                }
            }
        });
        setCancelable(true);
        return super.create();
    }
}
