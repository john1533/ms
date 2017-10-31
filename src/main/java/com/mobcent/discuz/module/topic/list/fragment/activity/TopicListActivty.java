package com.mobcent.discuz.module.topic.list.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.BasePopActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.topic.list.fragment.TopicSubPageFragment;

public class TopicListActivty extends BasePopActivity implements IntentConstant {
    private int boardChild;
    private int boardContent;
    private long boardId;
    private String boardName;
    private ConfigComponentModel componentModel;
    private String style;

    protected void initDatas() {
        super.initDatas();
        this.style = getIntent().getStringExtra("style");
        this.boardId = getIntent().getLongExtra("boardId", 0);
        this.boardChild = getIntent().getIntExtra(IntentConstant.INTENT_BOARD_CHILD, 0);
        this.boardContent = getIntent().getIntExtra(IntentConstant.INTENT_BOARD_CONTENT, 1);
        this.boardName = getIntent().getStringExtra("boardName");
        this.componentModel = (ConfigComponentModel) getIntent().getSerializableExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new TopicSubPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("style", this.style);
        bundle.putString("boardName", this.boardName);
        bundle.putLong("boardId", this.boardId);
        bundle.putInt(IntentConstant.INTENT_BOARD_CHILD, this.boardChild);
        bundle.putInt(IntentConstant.INTENT_BOARD_CONTENT, this.boardContent);
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
