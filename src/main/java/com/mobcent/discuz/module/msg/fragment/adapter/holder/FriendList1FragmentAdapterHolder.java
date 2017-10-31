package com.mobcent.discuz.module.msg.fragment.adapter.holder;

import android.widget.Button;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class FriendList1FragmentAdapterHolder {
    private Button applyBtn;
    private MCHeadIcon iconImg;
    private TextView nameText;
    private TextView noteText;

    public MCHeadIcon getIconImg() {
        return this.iconImg;
    }

    public void setIconImg(MCHeadIcon iconImg) {
        this.iconImg = iconImg;
    }

    public TextView getNoteText() {
        return this.noteText;
    }

    public void setNoteText(TextView noteText) {
        this.noteText = noteText;
    }

    public TextView getNameText() {
        return this.nameText;
    }

    public void setNameText(TextView nameText) {
        this.nameText = nameText;
    }

    public Button getApplyBtn() {
        return this.applyBtn;
    }

    public void setApplyBtn(Button applyBtn) {
        this.applyBtn = applyBtn;
    }
}
