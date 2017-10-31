package com.mobcent.discuz.module.article.fragment.adapter.holder;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArticleDetailAdapterHolder {
    private TextView commentCountTextView;
    private LinearLayout conteLayout;
    private TextView hitCountTextView;
    private Button pageBackBtn;
    private LinearLayout pageBox;
    private Button pageNextBtn;
    private Button pageSizeBtn;
    private TextView timeTextView;
    private TextView titleTextView;

    public TextView getTitleTextView() {
        return this.titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public TextView getTimeTextView() {
        return this.timeTextView;
    }

    public void setTimeTextView(TextView timeTextView) {
        this.timeTextView = timeTextView;
    }

    public LinearLayout getConteLayout() {
        return this.conteLayout;
    }

    public void setConteLayout(LinearLayout conteLayout) {
        this.conteLayout = conteLayout;
    }

    public Button getPageNextBtn() {
        return this.pageNextBtn;
    }

    public void setPageNextBtn(Button pageNextBtn) {
        this.pageNextBtn = pageNextBtn;
    }

    public Button getPageBackBtn() {
        return this.pageBackBtn;
    }

    public void setPageBackBtn(Button pageBackBtn) {
        this.pageBackBtn = pageBackBtn;
    }

    public Button getPageSizeBtn() {
        return this.pageSizeBtn;
    }

    public void setPageSizeBtn(Button pageSizeBtn) {
        this.pageSizeBtn = pageSizeBtn;
    }

    public LinearLayout getPageBox() {
        return this.pageBox;
    }

    public void setPageBox(LinearLayout pageBox) {
        this.pageBox = pageBox;
    }

    public TextView getHitCountTextView() {
        return this.hitCountTextView;
    }

    public void setHitCountTextView(TextView hitCountTextView) {
        this.hitCountTextView = hitCountTextView;
    }

    public TextView getCommentCountTextView() {
        return this.commentCountTextView;
    }

    public void setCommentCountTextView(TextView commentCountTextView) {
        this.commentCountTextView = commentCountTextView;
    }
}
