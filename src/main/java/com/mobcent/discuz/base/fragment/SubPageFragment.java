package com.mobcent.discuz.base.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.delegate.SubChangeListener;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.topic.list.fragment.BaseTopicListFragment;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView.ClickSubNavListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

import java.util.ArrayList;
import java.util.List;

public class SubPageFragment extends BaseModuleFragment {
    public String TAG = "SubPageFragment";
    private List<ConfigComponentModel> childModelList;
    private int currentPosition = 0;
    private int maxTab = 4;
    private ViewPager pager;
    private SubPageFragmentAdapter pagerAdapter;
    private List<String> subList = new ArrayList();
    private MCTabBarScrollView subWidget;
    private TopSettingModel mTopSettingModel;

    public class SubPageFragmentAdapter extends FragmentPagerAdapter {
        public SubPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            Fragment fragment = FragmentDispatchHelper.disPatchFragment((ConfigComponentModel) SubPageFragment.this.childModelList.get(position));
            Bundle bundle = fragment.getArguments();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            if (fragment instanceof BaseTopicListFragment) {
                ((BaseTopicListFragment) fragment).setViewPager(SubPageFragment.this.pager);
            }
            return fragment;
        }

        public int getCount() {
            return SubPageFragment.this.subList.size();
        }
    }

    protected String getRootLayoutName() {
        return "base_subnav_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if (this.childModelList == null) {
            this.childModelList = new ArrayList();
        }
        if (this.moduleModel != null) {
            List componentList = this.moduleModel.getComponentList();
            if (this.childModelList.isEmpty() && !MCListUtils.isEmpty(componentList)) {
                this.childModelList.addAll(componentList);
            }
            if (this.subList.isEmpty() && !this.childModelList.isEmpty()) {
                this.subList.clear();
                int len = this.childModelList.size();
                for (int i = 0; i < len; i++) {
                    this.subList.add(((ConfigComponentModel) this.childModelList.get(i)).getTitle());
                }
            }
        }
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        this.subWidget = (MCTabBarScrollView) findViewByName(rootView, "subnav_widget");
        this.pager = (ViewPager) findViewByName(rootView, "pager_layout");
        this.pager.setOffscreenPageLimit(3);
        setTabs();
        if (this.pagerAdapter == null) {
            this.pagerAdapter = new SubPageFragmentAdapter(getChildFragmentManager());
        }
        this.pager.setAdapter(this.pagerAdapter);
        if (!MCListUtils.isEmpty(this.subList)) {
            this.subWidget.selectCurrentTab(this.currentPosition);
        }
    }

    public void onResume() {
        super.onResume();
        dealChildFragmentState(true);
    }

    public void onPause() {
        super.onPause();
        dealChildFragmentState(false);
    }

    private void dealChildFragmentState(boolean isResume) {
        if (this.pagerAdapter != null && !MCListUtils.isEmpty(getChildFragmentManager().getFragments())) {
            for (Fragment f : getChildFragmentManager().getFragments()) {
                if (f != null && f.isVisible()) {
                    if (isResume) {
                        f.onResume();
                    } else {
                        f.onPause();
                    }
                }
            }
        }
    }

    private void selectedFragment(int currentPosition) {
        if (this.pagerAdapter != null && !MCListUtils.isEmpty(getChildFragmentManager().getFragments())) {
            for (Fragment f : getChildFragmentManager().getFragments()) {
                if (f != null && f.isVisible() && (f instanceof SubChangeListener)) {
                    ((SubChangeListener) f).onSelected(f.getArguments().getInt("position") == currentPosition);
                }
            }
        }
    }

    protected void initActions(View rootView) {
        this.pager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                SubPageFragment.this.currentPosition = position;
                SubPageFragment.this.subWidget.selectCurrentTab(SubPageFragment.this.currentPosition);
                SubPageFragment.this.selectedFragment(SubPageFragment.this.currentPosition);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setTabs() {
        if (this.activity != null && this.subList != null) {
            boolean isCard;
            if (this.moduleModel.getStyle().equals("card")) {
                isCard = true;
            } else {
                isCard = false;
            }
            MCLogUtil.v("RecordUtils", "isCard:" + isCard);


            Drawable backgroud = resource.getDrawableFromAttr(activity.getTheme(),android.R.attr.windowBackground);


            Drawable arrowBackground = resource.getDrawableFromAttr(activity.getTheme(),"colorPrimary");

            if (isCard) {
                this.subWidget.setTabBoxView(backgroud, dip2px(34), MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
                this.subWidget.setArrowView(arrowBackground, dip2px(10), dip2px(16));
                this.subWidget.setContainArrow(false);
                this.subWidget.setArrowMarginTop(3);
                LayoutParams lps = (LayoutParams) this.pager.getLayoutParams();
                lps.setMargins(lps.leftMargin, -dip2px(7), lps.rightMargin, lps.bottomMargin);
                this.pager.setLayoutParams(lps);
            } else {
                this.subWidget.setTabBoxView(backgroud, dip2px(34), MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
                this.subWidget.setArrowView(arrowBackground, dip2px(3), 0);
                this.subWidget.setContainArrow(true);
                this.subWidget.setArrowWidthRatio(CustomConstant.RATIO_ITEM);
            }

            this.subWidget.init(this.activity, this.subList, this.subList.size() > this.maxTab ? this.maxTab : this.subList.size(), new ClickSubNavListener() {
                public void onClickSubNav(View v, int position, TextView view) {
                    SubPageFragment.this.currentPosition = position;
                    SubPageFragment.this.pager.setCurrentItem(position, true);
                }

                public void initTextView(TextView view) {
                    if (view != null && view.getTag() != null) {
                        if (((Integer) view.getTag()).intValue() == SubPageFragment.this.currentPosition) {
                            view.setTextColor(resource.getColorFromAttr(activity.getTheme(), "colorPrimary"));
                            view.setTextSize(0, SubPageFragment.this.getResources().getDimension(SubPageFragment.this.resource.getDimenId("mc_forum_text_size_15")));
                            return;
                        }
                        view.setTextColor(resource.getColorFromAttr(activity.getTheme(),"nav_title_color"));
                        view.setTextSize(0, SubPageFragment.this.getResources().getDimension(SubPageFragment.this.resource.getDimenId("mc_forum_text_size_14")));
                    }
                }
            });
        }
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

    public void dealTopBar() {
        if (this.moduleModel != null) {
            int i;
            ConfigComponentModel moduleModel;
            TopBtnModel topBtnModel;
            TopSettingModel topSettingModel = createTopSettingModel();
            topSettingModel.title = this.moduleModel.getTitle();
            List<TopBtnModel> leftList = new ArrayList();
            List<TopBtnModel> rightList = new ArrayList();
            List<ConfigComponentModel> leftModels = this.moduleModel.getLeftTopbarList();
            if (!(leftModels == null || leftModels.isEmpty())) {
                for (i = 0; i < leftModels.size(); i++) {
                    moduleModel = (ConfigComponentModel) leftModels.get(i);
                    topBtnModel = new TopBtnModel();
                    topBtnModel.tag = moduleModel;
                    topBtnModel.icon = moduleModel.getIcon();
                    leftList.add(topBtnModel);
                }
            }
            List<ConfigComponentModel> rightModels = this.moduleModel.getRightTopbarList();
            if (!(rightModels == null || rightModels.isEmpty())) {
                for (i = 0; i < rightModels.size(); i++) {
                    moduleModel = (ConfigComponentModel) rightModels.get(i);
                    topBtnModel = new TopBtnModel();
                    topBtnModel.tag = moduleModel;
                    topBtnModel.icon = moduleModel.getIcon();
                    rightList.add(topBtnModel);
                }
            }
            topSettingModel.leftModels = leftList;
            topSettingModel.rightModels = rightList;
            dealTopBar(topSettingModel);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//
//            final String [] testStrings = getResources().getStringArray(resource.getArrayId("mc_place_home_around_names"));
//
////            MenuInflater inflater = activity.getMenuInflater();
//            inflater.inflate(resource.getMenuId("menu"), menu);
//
//
//            SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
//            MenuItem menuItem = menu.findItem(resource.getViewId("searchView"));
//
//            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
//            int completeTextId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);
//
//            AutoCompleteTextView completeText = (AutoCompleteTextView) searchView
//                    .findViewById(android.support.v7.appcompat.R.id.search_src_text) ;
//            completeText.setAdapter(new ArrayAdapter<>(activity,android.R.layout.simple_list_item_1,testStrings));
//            completeText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    searchView.setQuery(testStrings[position], true);
//
//                }
//            });
//
//            completeText.setThreshold(0);
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Toast.makeText(activity, query, Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

}
