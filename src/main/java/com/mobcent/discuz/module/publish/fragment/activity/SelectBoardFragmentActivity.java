package com.mobcent.discuz.module.publish.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.ConfigFastPostModel;
import com.mobcent.discuz.module.publish.fragment.SelectBoardFragment;
import java.io.Serializable;
import java.util.List;

public class SelectBoardFragmentActivity extends PopComponentActivity implements IntentConstant {
    private long boardId;
    private List<ConfigFastPostModel> boardList;

    protected void initDatas() {
        super.initDatas();
        this.boardList = (List) getIntent().getSerializableExtra(IntentConstant.INTENT_BOARDLIST);
        this.boardId = getIntent().getLongExtra("boardId", 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new SelectBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_BOARDLIST, (Serializable) this.boardList);
        bundle.putLong("boardId", this.boardId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
