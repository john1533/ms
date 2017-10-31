package com.mobcent.discuz.module.board.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.module.board.fragment.adapter.BoardChildListAdapter;
import java.util.ArrayList;
import java.util.List;

public class BoardChildListFragment extends BaseBoardListFragment {
    private BoardChildListAdapter adapter;
    private List<BoardChild> childList = new ArrayList();
    private String style = StyleConstant.STYLE_DEFAULT;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.style = getArguments().getString("style");
        if (TextUtils.isEmpty(this.style)) {
            this.style = StyleConstant.STYLE_DEFAULT;
        }
    }

    protected void onBoardDataLoaded(BaseResultModel<BoardModel> result) {
        if (((BoardModel) result.getData()).getParentList() != null) {
            this.parentList.clear();
            this.parentList.addAll(((BoardModel) result.getData()).getParentList());
        }
        boolean isTodayNumVisible = false;
        if (this.settingModel != null) {
            isTodayNumVisible = this.settingModel.getIsTodayPostCount() != 0;
        }
        this.adapter.setTodayNumVisibile(isTodayNumVisible);
        this.childList.clear();
        this.childList.addAll(((BoardParent) ((BoardModel) result.getData()).getParentList().get(0)).getChildList());
        this.adapter.setColumn(((BoardParent) ((BoardModel) result.getData()).getParentList().get(0)).getBoardCategoryType());
        this.adapter.setDatas(this.childList);
        this.adapter.notifyDataSetChanged();
    }

    protected BaseAdapter getListAdapter() {
        this.adapter = new BoardChildListAdapter(this.activity, this.childList, this.style);
        this.adapter.setComponentModel(this.moduleModel);
        return this.adapter;
    }
}
