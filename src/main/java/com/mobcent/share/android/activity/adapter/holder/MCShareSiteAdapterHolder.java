package com.mobcent.share.android.activity.adapter.holder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MCShareSiteAdapterHolder {
    private ImageView itemImg;
    private TextView itemName;
    private TextView itemNickName;
    private Button setBtn;

    public ImageView getItemImg() {
        return this.itemImg;
    }

    public void setItemImg(ImageView itemImg) {
        this.itemImg = itemImg;
    }

    public TextView getItemName() {
        return this.itemName;
    }

    public void setItemName(TextView itemName) {
        this.itemName = itemName;
    }

    public TextView getItemNickName() {
        return this.itemNickName;
    }

    public void setItemNickName(TextView itemNickName) {
        this.itemNickName = itemNickName;
    }

    public Button getSetBtn() {
        return this.setBtn;
    }

    public void setSetBtn(Button setBtn) {
        this.setBtn = setBtn;
    }
}
