package com.mobcent.discuz.module.board.fragment;

import android.widget.BaseAdapter;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.module.board.fragment.adapter.BoardListAdapter2;

public class BoardListFragment2 extends BaseBoardListFragment {
    private BoardListAdapter2 adapter;

    protected String getRootLayoutName() {
        return "board_list_fragment2";
    }

    protected void onBoardDataLoaded(BaseResultModel<BoardModel> result) {
        if (((BoardModel) result.getData()).getParentList() != null) {
            this.parentList.clear();
            this.parentList.addAll(((BoardModel) result.getData()).getParentList());
        }
        boolean isTodayTotalVisible = false;
        if (this.settingModel != null) {
            isTodayTotalVisible = this.settingModel.getIsTodayPostCount() != 0;
        }
        this.adapter.setTodayNumVisibile(isTodayTotalVisible);
        this.adapter.setDatas(this.parentList);
        this.adapter.notifyDataSetChanged();
    }

    protected BaseAdapter getListAdapter() {
        this.adapter = new BoardListAdapter2(this.activity, this.parentList);
        this.adapter.setComponentModel(this.moduleModel);
        if (this.fid != -1) {
            this.adapter.setChildBoard(true);
        }
        return this.adapter;
    }
}
