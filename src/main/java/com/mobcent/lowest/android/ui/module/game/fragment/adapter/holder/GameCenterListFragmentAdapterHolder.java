package com.mobcent.lowest.android.ui.module.game.fragment.adapter.holder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameCenterListFragmentAdapterHolder {
    private TextView gameDesc;
    private RelativeLayout gameItemLayout;
    private TextView gameName;
    private Button gameUrl;
    private TextView players;
    private LinearLayout startLayout;
    private ImageView thumbnail;

    public ImageView getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }

    public TextView getGameName() {
        return this.gameName;
    }

    public RelativeLayout getGameItemLayout() {
        return this.gameItemLayout;
    }

    public void setGameItemLayout(RelativeLayout gameItemLayout) {
        this.gameItemLayout = gameItemLayout;
    }

    public void setGameName(TextView gameName) {
        this.gameName = gameName;
    }

    public Button getGameUrl() {
        return this.gameUrl;
    }

    public void setGameUrl(Button gameUrl) {
        this.gameUrl = gameUrl;
    }

    public TextView getGameDesc() {
        return this.gameDesc;
    }

    public void setGameDesc(TextView gameDesc) {
        this.gameDesc = gameDesc;
    }

    public TextView getPlayers() {
        return this.players;
    }

    public void setPlayers(TextView players) {
        this.players = players;
    }

    public LinearLayout getStartLayout() {
        return this.startLayout;
    }

    public void setStartLayout(LinearLayout startLayout) {
        this.startLayout = startLayout;
    }
}
