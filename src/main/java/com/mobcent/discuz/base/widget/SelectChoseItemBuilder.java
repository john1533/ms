package com.mobcent.discuz.base.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;

public class SelectChoseItemBuilder extends Builder {
    private ClassifiedModel classifiedModel;
    private String[] items;
    private MCResource resource;
    private int selected = -1;
    private TextView tv;

    public SelectChoseItemBuilder(Context context, String[] items, TextView tv, ClassifiedModel classifiedModel) {
        super(context);
        this.tv = tv;
        this.items = items;
        this.resource = MCResource.getInstance(context);
        this.classifiedModel = classifiedModel;
    }

    public AlertDialog create() {
        final StringBuilder sb = new StringBuilder();
        setSingleChoiceItems(this.items, -1, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SelectChoseItemBuilder.this.selected = which;
                sb.append(which + AdApiConstant.RES_SPLIT_COMMA);
            }
        });
        setIcon(17301543);
        setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (SelectChoseItemBuilder.this.classifiedModel != null && !SelectChoseItemBuilder.this.classifiedModel.equals("") && SelectChoseItemBuilder.this.selected > -1) {
                    SelectChoseItemBuilder.this.classifiedModel.setClassifiedValue(SelectChoseItemBuilder.this.selected + "");
                    MCLogUtil.i("setPositiveButton", SelectChoseItemBuilder.this.selected + "");
                    SelectChoseItemBuilder.this.tv.setText(SelectChoseItemBuilder.this.items[SelectChoseItemBuilder.this.selected]);
                }
            }
        });
        setNegativeButton(this.resource.getStringId("mc_forum_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (SelectChoseItemBuilder.this.classifiedModel != null && !SelectChoseItemBuilder.this.classifiedModel.equals("")) {
                    SelectChoseItemBuilder.this.classifiedModel.setClassifiedValue("");
                    MCLogUtil.i("setPositiveButton", SelectChoseItemBuilder.this.selected + "");
                    SelectChoseItemBuilder.this.tv.setText("");
                }
            }
        });
        setCancelable(true);
        return super.create();
    }
}
