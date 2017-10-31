package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class MCSetVisibleView extends CheckBox {
    private Context context;
    private boolean isRapidPublish = false;
    private MCResource resource;

    public MCSetVisibleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public MCSetVisibleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public MCSetVisibleView(Context context) {
        super(context);
        initData(context);
    }

    public MCSetVisibleView(Context context, boolean isRapidPublish) {
        super(context);
        initData(context);
        this.isRapidPublish = isRapidPublish;
    }

    private void initData(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
    }

    public CheckBox create(String content, int id, boolean isChecked) {
        CheckBox checkBox = new CheckBox(this.context);
        LayoutParams layoutParams = new LayoutParams(-2, MCPhoneUtil.getRawSize(this.context, 1, 41.0f));
        if (this.isRapidPublish) {
            if (isChecked) {
                checkBox.setButtonDrawable(this.resource.getDrawableId("mc_forum_publish_icons6_h"));
            } else {
                checkBox.setButtonDrawable(this.resource.getDrawableId("mc_forum_publish_icons6_n"));
            }
            layoutParams.leftMargin = MCPhoneUtil.getRawSize(this.context, 1, 50.0f);
        } else if (isChecked) {
            checkBox.setButtonDrawable(this.resource.getDrawableId("mc_forum_detail_select_h"));
        } else {
            checkBox.setButtonDrawable(this.resource.getDrawableId("mc_forum_detail_select_n"));
        }
        layoutParams.gravity = 0;
        checkBox.setLayoutParams(layoutParams);
        checkBox.setTextColor(this.resource.getColorId("mc_forum_text4_normal_color"));
        checkBox.setTextSize(15.0f);
        checkBox.setId(id);
        checkBox.setText("\t" + content);
        return checkBox;
    }
}
