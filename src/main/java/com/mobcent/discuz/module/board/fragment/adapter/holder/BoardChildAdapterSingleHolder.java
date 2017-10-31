package com.mobcent.discuz.module.board.fragment.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BoardChildAdapterSingleHolder {
    private TextView boardName;
    private TextView boardTime;
    private ImageView img;
    private View midLine;
    private View rootBox;
    private TextView totalText;

    public ImageView getImg() {
        return this.img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public TextView getBoardName() {
        return this.boardName;
    }

    public void setBoardName(TextView boardName) {
        this.boardName = boardName;
    }

    public TextView getBoardTime() {
        return this.boardTime;
    }

    public void setBoardTime(TextView boardTime) {
        this.boardTime = boardTime;
    }

    public TextView getTotalText() {
        return this.totalText;
    }

    public void setTotalText(TextView totalText) {
        this.totalText = totalText;
    }

    public View getMidLine() {
        return this.midLine;
    }

    public void setMidLine(View midLine) {
        this.midLine = midLine;
    }

    public View getRootBox() {
        return this.rootBox;
    }

    public void setRootBox(View rootBox) {
        this.rootBox = rootBox;
    }
}
