package com.mobcent.discuz.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.module.board.fragment.BaseBoardListFragment;
import com.mobcent.discuz.module.board.fragment.BoardChildListFragment;

public class BoardListActivity extends PopComponentActivity {
    private long fid = -1;
    private String style = StyleConstant.STYLE_DEFAULT;

    protected void initDatas() {
        super.initDatas();
        this.fid = getIntent().getLongExtra("fid", -1);
        this.style = getIntent().getStringExtra("style");
        if (TextUtils.isEmpty(this.style)) {
            this.style = StyleConstant.STYLE_DEFAULT;
        }
    }

    protected Fragment initContentFragment() {
        BaseBoardListFragment boardFragment = new BoardChildListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("fid", this.fid);
        bundle.putString("style", this.style);
        boardFragment.setArguments(bundle);
        return boardFragment;
    }
}
