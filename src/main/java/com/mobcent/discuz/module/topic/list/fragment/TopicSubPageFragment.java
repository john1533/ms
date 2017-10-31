package com.mobcent.discuz.module.topic.list.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.view.MCPopupListView;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupClickListener;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.model.ClassifyTopModel;
import com.mobcent.discuz.android.model.ClassifyTypeModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.DraftHelper;
import com.mobcent.discuz.base.helper.DraftHelper.DraftDelegate;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.board.fragment.BoardChildListFragment;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.publish.fragment.activity.ClassifyTopicActivity;
import com.mobcent.discuz.module.publish.fragment.activity.PublishPollTopicActivity;
import com.mobcent.discuz.module.publish.fragment.activity.PublishTopicActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView.ClickSubNavListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicSubPageFragment extends BaseFragment implements SlideDelegate {
    private int PUBLISH = 1;
    private int SEARCH = 2;
    public String TAG = "TopicSubPageFragment";
    private int boardChild = 0;
    private int boardContent = 1;
    private long boardId;
    private String boardName;
    private List<ClassifyTopModel> classifyTopList;
    private List<ClassifyTypeModel> classifyTypeList;
    private int currentPosition = 0;
    private Fragment fragment1 = null;
    private boolean isInitView = false;
    private int maxTab = 5;
    private ViewPager pager;
    private SubPageFragmentAdapter pagerAdapter;
    private MCPopupListView popupListView;
    private List<Integer> positionList;
    private MCPopupListView publishView;
    private String style;
    private MCTabBarScrollView subWidget;
    private List<String> tabs = new ArrayList();

    public class SubPageFragmentAdapter extends FragmentStatePagerAdapter {
        public SubPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            Fragment fragment;
            if (position == 0) {
                if (TopicSubPageFragment.this.boardContent == 0 && TopicSubPageFragment.this.boardChild == 1) {
                    fragment = new BoardChildListFragment();
                    bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, TopicSubPageFragment.this.createComponentModel(""));
                    fragment.setArguments(bundle);
                    return fragment;
                }
                fragment = FragmentDispatchHelper.disPatchFragment(TopicSubPageFragment.this.createComponentModel("all"));
                TopicSubPageFragment.this.fragment1 = fragment;
                return fragment;
            } else if (position == 1) {
                return FragmentDispatchHelper.disPatchFragment(TopicSubPageFragment.this.createComponentModel("new"));
            } else {
                if (position == 2) {
                    return FragmentDispatchHelper.disPatchFragment(TopicSubPageFragment.this.createComponentModel("essence"));
                }
                if (position == 3) {
                    return FragmentDispatchHelper.disPatchFragment(TopicSubPageFragment.this.createComponentModel("top"));
                }
                if (position != 4) {
                    return null;
                }
                fragment = new BoardChildListFragment();
                bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, TopicSubPageFragment.this.createComponentModel(""));
                fragment.setArguments(bundle);
                return fragment;
            }
        }

        public int getCount() {
            return TopicSubPageFragment.this.tabs.size();
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.boardContent = getArguments().getInt(IntentConstant.INTENT_BOARD_CONTENT);
        this.boardChild = getArguments().getInt(IntentConstant.INTENT_BOARD_CHILD);
        this.style = getArguments().getString("style");
        this.boardId = getArguments().getLong("boardId");
        this.boardName = getArguments().getString("boardName");
        if (MCStringUtil.isEmpty(this.style)) {
            this.style = "flat";
        }
        if (!(this.permissionModel == null || this.permissionModel.getPostInfo() == null || this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId)) == null || ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel() == null)) {
            this.classifyTypeList = ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel().getClassifyTypeList();
            this.classifyTopList = ((PermissionModel) this.permissionModel.getPostInfo().get(Long.valueOf(this.boardId))).getTopicPermissionModel().getNewTopicPanel();
        }
        this.positionList = new ArrayList();
    }

    protected String getRootLayoutName() {
        return "topic_subnav_fragment";
    }

    protected void initViews(View rootView) {
        this.isInitView = true;
        this.subWidget = (MCTabBarScrollView) findViewByName(rootView, "subnav_widget");
        this.pager = (ViewPager) findViewByName(rootView, "pager_layout");
        this.pager.setOffscreenPageLimit(4);
        this.popupListView = (MCPopupListView) findViewByName(rootView, "popup_listview");
        this.publishView = (MCPopupListView) findViewByName(rootView, "publish_popup_listview");
        if (this.boardContent == 1 && this.boardChild == 0) {
            this.tabs.add(this.resource.getString("mc_forum_topic_all"));
        } else if (this.boardContent == 1 && this.boardChild == 1) {
            this.tabs.add(this.resource.getString("mc_forum_topic_all"));
        } else if (this.boardContent == 0 && this.boardChild == 1) {
            this.tabs.add(this.resource.getString("mc_forum_topic_all"));
        }
        this.tabs.add(this.resource.getString("mc_forum_topic_new"));
        this.tabs.add(this.resource.getString("mc_forum_topic_essence"));
        this.tabs.add(this.resource.getString("mc_forum_topic_top"));
        if (this.boardContent == 1 && this.boardChild == 1) {
            this.tabs.add(this.resource.getString("mc_forum_seed_board"));
        }
        componentDealTopbar();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (TopicSubPageFragment.this.pagerAdapter == null) {
                    TopicSubPageFragment.this.pagerAdapter = new SubPageFragmentAdapter(TopicSubPageFragment.this.getChildFragmentManager());
                }
                TopicSubPageFragment.this.pager.setAdapter(TopicSubPageFragment.this.pagerAdapter);
            }
        }, -100);
    }

    protected void initActions(View rootView) {
        if (this.style.equals("flat")) {
            this.subWidget.setTabBoxView(this.resource.getDrawable("mc_forum_tab_style1_bg"), dip2px(34), MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
            this.subWidget.setArrowView(this.resource.getDrawable("mc_forum_tab_style1_glide"), dip2px(3), 0);
            this.subWidget.setContainArrow(true);
            this.subWidget.setArrowWidthRatio(CustomConstant.RATIO_ITEM);
        } else {
            this.subWidget.setTabBoxView(this.resource.getDrawable("mc_forum_tab_style2_bg"), dip2px(34), MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
            this.subWidget.setArrowView(this.resource.getDrawable("mc_forum_tab_style2_arrow1"), dip2px(10), dip2px(16));
            this.subWidget.setContainArrow(false);
            this.subWidget.setArrowMarginTop(3);
            LayoutParams lps = (LayoutParams) this.pager.getLayoutParams();
            lps.setMargins(lps.leftMargin, -dip2px(10), lps.rightMargin, lps.bottomMargin);
            this.pager.setLayoutParams(lps);
        }
        this.subWidget.init(getActivity(), this.tabs, this.tabs.size() > this.maxTab ? this.maxTab : this.tabs.size(), new ClickSubNavListener() {
            public void initTextView(TextView view) {
                if (view != null && view.getTag() != null) {
                    if (((Integer) view.getTag()).intValue() == TopicSubPageFragment.this.currentPosition) {
                        view.setTextColor(TopicSubPageFragment.this.getResources().getColorStateList(TopicSubPageFragment.this.resource.getColorId("mc_forum_tabbar_press_color")));
                        view.setTextSize(0, TopicSubPageFragment.this.getResources().getDimension(TopicSubPageFragment.this.resource.getDimenId("mc_forum_text_size_15")));
                        return;
                    }
                    view.setTextColor(TopicSubPageFragment.this.getResources().getColorStateList(TopicSubPageFragment.this.resource.getColorId("mc_forum_tabbar_normal_color")));
                    view.setTextSize(0, TopicSubPageFragment.this.getResources().getDimension(TopicSubPageFragment.this.resource.getDimenId("mc_forum_text_size_14")));
                }
            }

            public void onClickSubNav(View v, int position, TextView view) {
                TopicSubPageFragment.this.currentPosition = position;
                TopicSubPageFragment.this.pager.setCurrentItem(position, true);
            }
        });
        this.pager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                TopicSubPageFragment.this.currentPosition = position;
                if (TopicSubPageFragment.this.isInitView) {
//                    TopicSubPageFragment.this.subWidget.selectCurrentTabNoAnimation(TopicSubPageFragment.this.currentPosition);
                    TopicSubPageFragment.this.subWidget.selectCurrentTab(TopicSubPageFragment.this.currentPosition);
                    TopicSubPageFragment.this.isInitView = false;
                    return;
                }
                TopicSubPageFragment.this.subWidget.selectCurrentTab(TopicSubPageFragment.this.currentPosition);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.subWidget.selectCurrentTab(this.currentPosition);
        this.popupListView.setResource("mc_forum_pop_upmenu_bg1", 40);
        LayoutParams layoutParams = new LayoutParams(MCPhoneUtil.getRawSize(this.activity, 1, 180.0f), -2);
        layoutParams.addRule(14);
        this.popupListView.setPopupListViewLayoutParams(layoutParams);
        this.popupListView.init(initClassifyView(), new PopupClickListener() {
            public void initTextView(TextView textView) {
                textView.setTextColor(TopicSubPageFragment.this.getResources().getColorStateList(TopicSubPageFragment.this.resource.getColorId("mc_forum_bubble_color")));
                textView.setTextSize(14.0f);
            }

            public void click(TextView textView, ImageView imageView, PopupModel popupModel, int position) {
                try {
                    TopicSubPageFragment.this.popupListView.setPopupList(TopicSubPageFragment.this.initClassifyView());
                    TopicSubPageFragment.this.popupListView.setVisibility(8);
                    TopicSubPageFragment.this.pager.setCurrentItem(0);
                    TopicSubPageFragment.this.subWidget.selectCurrentTab(0);
                    ((BaseTopicListFragment) TopicSubPageFragment.this.fragment1).onRefreshListView(popupModel);
                } catch (Exception e) {
                }
            }

            public void hideView() {
//                if (TopicSubPageFragment.this.getTopBarHelper() != null && TopicSubPageFragment.this.getTopBarHelper().getTopBox() != null) {
//                    TopicSubPageFragment.this.getTopBarHelper().getTopBox().rotateTitleDrawable(true);
//                }
            }
        });
        initPublishView();
    }

    private void initPublishView() {
        this.publishView.setResource("mc_forum_personal_publish_bg", 40);
        LayoutParams layoutParams1 = new LayoutParams(MCPhoneUtil.getRawSize(this.activity, 1, 100.0f), -2);
        layoutParams1.addRule(11);
        layoutParams1.rightMargin = MCPhoneUtil.getRawSize(this.activity, 1, 8.0f);
        this.publishView.setPopupListViewLayoutParams(layoutParams1);
        this.publishView.init(classifyTopList(), new PopupClickListener() {
            public void initTextView(TextView textView) {
                textView.setTextColor(TopicSubPageFragment.this.getResources().getColorStateList(TopicSubPageFragment.this.resource.getColorId("mc_forum_bubble_color")));
                textView.setTextSize(14.0f);
            }

            public void click(TextView textView, ImageView imageView, PopupModel popupModel, int position) {
                Intent intent;
                if (popupModel.getAction().equals(PostsConstant.TOPIC_TYPE_NORMAL)) {
                    if (!DraftHelper.isNeedAlertDialog(TopicSubPageFragment.this.activity, 1, new DraftDelegate() {
                        public void onDraftAlertBack(TopicDraftModel model) {
                            TopicSubPageFragment.this.onPublishTopicClick(model);
                        }
                    })) {
                        TopicSubPageFragment.this.onPublishTopicClick(null);
                    }
                } else if (popupModel.getAction().equals("vote")) {
                    intent = new Intent(TopicSubPageFragment.this.activity.getApplicationContext(), PublishPollTopicActivity.class);
                    intent.putExtra("boardId", TopicSubPageFragment.this.boardId);
                    intent.putExtra("boardName", TopicSubPageFragment.this.boardName);
                    intent.putExtra(IntentConstant.INTENT_CLASSIFICATIONTYPE_LIST, (Serializable) TopicSubPageFragment.this.classifyTypeList);
                    TopicSubPageFragment.this.getActivity().startActivity(intent);
                } else {
                    intent = new Intent(TopicSubPageFragment.this.activity.getApplicationContext(), ClassifyTopicActivity.class);
                    intent.putExtra("boardId", TopicSubPageFragment.this.boardId);
                    intent.putExtra("boardName", TopicSubPageFragment.this.boardName);
                    intent.putExtra(IntentConstant.CLASSIFICATIONTOP_ID, popupModel.getId());
                    intent.putExtra(IntentConstant.CLASSIFICATIONTOP_NAME, popupModel.getName());
                    intent.putExtra(IntentConstant.INTENT_CLASSIFICATIONTYPE_LIST, (Serializable) TopicSubPageFragment.this.classifyTypeList);
                    TopicSubPageFragment.this.getActivity().startActivity(intent);
                }
                TopicSubPageFragment.this.publishView.setVisibility(8);
            }

            public void hideView() {
            }
        });
    }

    private void onPublishTopicClick(TopicDraftModel model) {
        Intent intent = new Intent(this.activity.getApplicationContext(), PublishTopicActivity.class);
        intent.putExtra("boardId", this.boardId);
        intent.putExtra("boardName", this.boardName);
        intent.putExtra(IntentConstant.INTENT_CLASSIFICATIONTYPE_LIST, (Serializable) this.classifyTypeList);
        intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, model);
        getActivity().startActivity(intent);
    }

    private List<PopupModel> classifyTopList() {
        List<PopupModel> list = new ArrayList();
        if (this.classifyTopList != null) {
            for (int i = 0; i < this.classifyTopList.size(); i++) {
                PopupModel model = new PopupModel();
                model.setName(((ClassifyTopModel) this.classifyTopList.get(i)).getTitle());
                model.setId(((ClassifyTopModel) this.classifyTopList.get(i)).getActionId());
                model.setAction(((ClassifyTopModel) this.classifyTopList.get(i)).getType());
                list.add(model);
            }
        }
        return list;
    }

    private List<PopupModel> initClassifyView() {
        int i;
        List<PopupModel> list = new ArrayList();
        PopupModel model1 = new PopupModel();
        model1.setName(this.resource.getString("mc_forum_topic_all"));
        model1.setType(-1);
        list.add(model1);
        if (this.classifyTypeList != null) {
            for (i = 0; i < this.classifyTypeList.size(); i++) {
                PopupModel model = new PopupModel();
                model.setName(((ClassifyTypeModel) this.classifyTypeList.get(i)).getName());
                model.setId(((ClassifyTypeModel) this.classifyTypeList.get(i)).getId());
                model.setType(-1);
                list.add(model);
            }
        }
        if (this.classifyTopList != null) {
            for (i = 0; i < this.classifyTopList.size(); i++) {
                if (((ClassifyTopModel) this.classifyTopList.get(i)).getType().equals("sort")) {
                    PopupModel model = new PopupModel();
                    model.setName(((ClassifyTopModel) this.classifyTopList.get(i)).getTitle());
                    model.setId(((ClassifyTopModel) this.classifyTopList.get(i)).getActionId());
                    model.setType(-2);
                    list.add(model);
                }
            }
        }
        return list;
    }

    protected void loadCurrentFragmentData() {
        BaseFragment fragment = (BaseFragment) this.pagerAdapter.getItem(this.currentPosition);
        if (!this.positionList.contains(Integer.valueOf(this.currentPosition))) {
            this.positionList.add(Integer.valueOf(this.currentPosition));
            fragment.loadDataByNet();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.sharedPreferencesDB.isRefresh()) {
            this.pager.setAdapter(this.pagerAdapter);
            this.sharedPreferencesDB.setRefresh(false);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.sharedPreferencesDB.setRefresh(false);
    }

    private ConfigComponentModel createComponentModel(String topicType) {
        ConfigComponentModel model = new ConfigComponentModel();
        model.setForumId(this.boardId);
        model.setOrderby(topicType);
        model.setType(ConfigConstant.COMPONENT_TOPICLIST_SIMPLE);
        if (this.moduleModel != null) {
            model.setStyle(this.moduleModel.getSubListStyle());
            model.setSubListStyle(this.moduleModel.getSubListStyle());
            model.setSubDetailViewStyle(this.moduleModel.getSubDetailViewStyle());
        }
        model.setListImagePosition(2);
        model.setListTitleLength(20);
        model.setListSummaryLength(40);
        return model;
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = this.boardName;
        topSettingModel.isTitleClickAble = true;
        TopBtnModel topBtnModel1 = new TopBtnModel();
        topBtnModel1.icon = "mc_forum_top_bar_button10";
        topBtnModel1.action = this.SEARCH;
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button17";
        topBtnModel.action = this.PUBLISH;
        rightModels.add(topBtnModel);
        rightModels.add(topBtnModel1);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                TopBtnModel t = (TopBtnModel) v.getTag();
                if (t.action == TopicSubPageFragment.this.PUBLISH) {
                    TopicSubPageFragment.this.initPublishView();
                    if (TopicSubPageFragment.this.classifyTopList().size() == 0) {
                        MCToastUtils.toastByResName(TopicSubPageFragment.this.activity, "mc_forum_publish_permission", 1);
                    } else if (!LoginHelper.doInterceptor(TopicSubPageFragment.this.activity, null, null)) {
                    } else {
                        if (TopicSubPageFragment.this.publishView.getVisibility() == 8) {
                            TopicSubPageFragment.this.publishView.setVisibility(0);
                        } else {
                            TopicSubPageFragment.this.publishView.setVisibility(8);
                        }
                    }
                } else if (t.action == TopicSubPageFragment.this.SEARCH) {
                    Intent intent = new Intent(TopicSubPageFragment.this.activity.getApplicationContext(), UserTopicListActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, TopicSubPageFragment.this.createComponentModel(""));
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "search");
                    TopicSubPageFragment.this.startActivity(intent);
                } else if (t.action == -2) {
                    if (TopicSubPageFragment.this.publishView != null) {
                        TopicSubPageFragment.this.publishView.setVisibility(8);
                    }
                    if (TopicSubPageFragment.this.popupListView.getVisibility() == 8) {
                        TopicSubPageFragment.this.popupListView.setVisibility(0);
//                        if (TopicSubPageFragment.this.getTopBarHelper() != null && TopicSubPageFragment.this.getTopBarHelper().getTopBox() != null) {
//                            TopicSubPageFragment.this.getTopBarHelper().getTopBox().rotateTitleDrawable(false);
//                            return;
//                        }
                        return;
                    }
                    TopicSubPageFragment.this.popupListView.setVisibility(8);
//                    if (TopicSubPageFragment.this.getTopBarHelper() != null && TopicSubPageFragment.this.getTopBarHelper().getTopBox() != null) {
//                        TopicSubPageFragment.this.getTopBarHelper().getTopBox().rotateTitleDrawable(true);
//                    }
                }
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.isInitView = false;
    }

    public boolean isChildInteruptBackPress() {
        if (this.popupListView != null && !this.popupListView.onKeyDown()) {
            return true;
        }
        if (this.publishView == null || this.publishView.onKeyDown()) {
            return super.isChildInteruptBackPress();
        }
        return true;
    }

    public boolean isSlideFullScreen() {
        if (this.pager == null) {
            return true;
        }
        if (this.pager.getCurrentItem() != 0) {
            return false;
        }
        List fragments = getChildFragmentManager().getFragments();
        if (!MCListUtils.isEmpty(fragments)) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragments.get(0);
            } catch (Exception e) {
            }
            if (fragment != null && (fragment instanceof SlideDelegate)) {
                return ((SlideDelegate) fragment).isSlideFullScreen();
            }
        }
        return true;
    }
}
