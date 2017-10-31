package com.mobcent.discuz.module.article.fragment.adapter.holder;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class ArticleCommentListAdapterHolder {
    private MCHeadIcon icomImg;
    private LinearLayout replyBox;
    private LinearLayout replyContentBox;
    private TextView timeText;
    private TextView userNameText;

    public MCHeadIcon getIcomImg() {
        return this.icomImg;
    }

    public void setIcomImg(MCHeadIcon icomImg) {
        this.icomImg = icomImg;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public TextView getUserNameText() {
        return this.userNameText;
    }

    public void setUserNameText(TextView userNameText) {
        this.userNameText = userNameText;
    }

    public LinearLayout getReplyContentBox() {
        return this.replyContentBox;
    }

    public void setReplyContentBox(LinearLayout replyContentBox) {
        this.replyContentBox = replyContentBox;
    }

    public LinearLayout getReplyBox() {
        return this.replyBox;
    }

    public void setReplyBox(LinearLayout replyBox) {
        this.replyBox = replyBox;
    }
}
