package com.mobcent.discuz.module.board.fragment.adapter;

import android.content.Context;
import com.mobcent.discuz.android.model.BoardParent;
import java.util.List;

public class BoardListAdapter2 extends BoardListAdapter1 {
    public BoardListAdapter2(Context context, List<BoardParent> datas) {
        super(context, datas);
    }

    protected String getLayoutName() {
        return "board_list_fragment_item2";
    }

    protected String getSingleLayoutName() {
        return "board_list_fragment_item2_single";
    }

    protected boolean isCard() {
        return true;
    }
}
