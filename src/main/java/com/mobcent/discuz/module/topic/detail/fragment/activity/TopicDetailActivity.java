package com.mobcent.discuz.module.topic.detail.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.Window;

import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.module.topic.detail.fragment.TopicDetail1Fragment;
import com.mobcent.discuz.module.topic.detail.fragment.TopicDetail2Fragment;
import com.mobcent.discuz.module.topic.detail.fragment.TopicDetail3Fragment;
import com.mobcent.discuz.module.topic.detail.fragment.TopicDetailRequestFragment;

import java.lang.reflect.Method;

public class TopicDetailActivity extends PopComponentActivity implements IntentConstant {
    private long boardId;
    private String boardName;
    private TopicDetailRequestFragment fragment;
    private boolean isRequestEdit = false;
    private String style;
    private TopicDraftModel topicDraftModel;
    private long topicId;

    protected void initDatas() {
        super.initDatas();
        if (this.componentModel == null || this.componentModel.getTopicId() == 0) {
            this.style = getIntent().getStringExtra("style");
            this.boardId = getIntent().getLongExtra("boardId", 0);
            this.boardName = getIntent().getStringExtra("boardName");
            this.topicId = getIntent().getLongExtra("topicId", 0);
            this.isRequestEdit = getIntent().getBooleanExtra(IntentConstant.INTENT_TOPIC_DETAIL_REQUEST_EDIT, false);
        } else {
            this.style = this.componentModel.getStyle();
            this.boardId = this.componentModel.getForumId();
            this.boardName = this.componentModel.getTitle();
            this.topicId = this.componentModel.getTopicId();
        }
        this.topicDraftModel = (TopicDraftModel) getIntent().getSerializableExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL);
    }

    protected Fragment initContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("boardName", this.boardName);
        bundle.putLong("boardId", this.boardId);
        bundle.putLong("topicId", this.topicId);
        bundle.putBoolean(IntentConstant.INTENT_TOPIC_DETAIL_REQUEST_EDIT, this.isRequestEdit);
        bundle.putSerializable(IntentConstant.INTENT_TOPIC_DRAFMODEL, this.topicDraftModel);
//        if (StyleConstant.STYLE_NO_TITLE.equals(this.style)) {
//            this.fragment = new TopicDetail3Fragment();
//        } else if ("card".equals(this.style)) {
//            this.fragment = new TopicDetail2Fragment();
//        } else {
//            this.fragment = new TopicDetail1Fragment();
//        }
        this.fragment = new TopicDetail1Fragment();
        this.fragment.setArguments(bundle);
        return this.fragment;
    }

    public boolean isSlideAble() {
        if (this.fragment instanceof SlideDelegate) {
            return this.fragment.isSlideFullScreen();
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}
