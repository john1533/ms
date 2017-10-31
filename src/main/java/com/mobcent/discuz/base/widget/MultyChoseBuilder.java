package com.mobcent.discuz.base.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;

public class MultyChoseBuilder extends Builder {
    private boolean[] checkedItems;
    private ClassifiedModel classifiedModel;
    private TextView contentText;
    private String[] items;
    private MCResource resource;

    public MultyChoseBuilder(Context context, String[] items, boolean[] checkedItems, TextView contentText, ClassifiedModel classifiedModel) {
        super(context);
        this.items = items;
        this.checkedItems = checkedItems;
        this.contentText = contentText;
        this.classifiedModel = classifiedModel;
        this.resource = MCResource.getInstance(context);
    }

    public AlertDialog create() {
        setMultiChoiceItems(this.items, this.checkedItems, new OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                MultyChoseBuilder.this.checkedItems[which] = isChecked;
            }
        });
        setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (MultyChoseBuilder.this.classifiedModel != null && !MultyChoseBuilder.this.classifiedModel.equals("")) {
                    MultyChoseBuilder.this.setText();
                }
            }
        });
        setCancelable(true);
        return super.create();
    }

    private void setText() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbItem = new StringBuilder();
        for (int i = 0; i < this.checkedItems.length; i++) {
            if (this.checkedItems[i]) {
                sb.append(this.items[i] + AdApiConstant.RES_SPLIT_COMMA);
                sbItem.append(i + AdApiConstant.RES_SPLIT_COMMA);
            }
        }
        if (sb.length() > 1) {
            String text = sb.subSequence(0, sb.length() - 1).toString();
            this.classifiedModel.setClassifiedValue(sbItem.subSequence(0, sbItem.length() - 1).toString());
            this.contentText.setText(text);
        } else if (this.classifiedModel != null) {
            this.classifiedModel.setClassifiedValue("");
            this.contentText.setText("");
        }
    }
}
