package com.mobcent.lowest.android.ui.module.game.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.game.cache.GameDataCache;
import com.mobcent.lowest.android.ui.module.game.fragment.BaseGameFragment;
import com.mobcent.lowest.android.ui.module.game.fragment.GameCenterListFragment;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView.ClickSubNavListener;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCenterActivity extends BaseGameFragmentActivity {
    protected Button finishBtn;
    protected ViewPager gameListViewPager;
    protected List subList;
    protected int subNavVisiableCount = 2;
    protected MCTabBarScrollView tabBarView;
    protected TextView titleText;
    protected RelativeLayout topbar;

    private class GameCenterPagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, BaseGameFragment> fragmentMap = new HashMap();

        public GameCenterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (this.fragmentMap.get(Integer.valueOf(position)) != null) {
                return (Fragment) this.fragmentMap.get(Integer.valueOf(position));
            }
            BaseGameFragment fragment = new GameCenterListFragment();
            this.fragmentMap.put(Integer.valueOf(position), fragment);
            Bundle bundle = new Bundle();
            String tagName = GameCenterActivity.this.getTagName(position);
            MCLogUtil.e("GameCenterPagerAdapter", "position = " + position + "  tagName = " + tagName);
            bundle.putString("tag_name", tagName);
            fragment.setArguments(bundle);
            return fragment;
        }

        public int getCount() {
            return GameCenterActivity.this.subList.size();
        }
    }

    protected void initData() {
        this.subList = Arrays.asList(getResources().getStringArray(this.mcResource.getArrayId("mc_game_center_channel")));
    }

    protected void initViews() {
        setContentView(this.mcResource.getLayoutId("mc_game_activity"));
        this.topbar = (RelativeLayout) findViewById(this.mcResource.getViewId("mc_game_topbar"));
        this.titleText = (TextView) findViewById(this.mcResource.getViewId("mc_game_topbar_title_text"));
        this.titleText.setText(this.mcResource.getString("mc_game_center_topbar_title"));
        this.finishBtn = (Button) findViewById(this.mcResource.getViewId("mc_game_topbar_left_btn"));
        this.tabBarView = (MCTabBarScrollView) findViewById(this.mcResource.getViewId("mc_game_subnav"));
        this.tabBarView.setTabBoxView(this.mcResource.getDrawable("mc_forum_peripheral_tab_bg"), MCPhoneUtil.dip2px(this.context, 34.0f), MCPhoneUtil.getDisplayWidth(this));
        this.tabBarView.setArrowView(this.mcResource.getDrawable("mc_forum_tab_style1_glide"), MCPhoneUtil.dip2px(this.context, 3.0f), 0);
        this.tabBarView.setArrowWidthRatio(0.8f);
        this.tabBarView.setContainArrow(true);
        this.tabBarView.init(this.context, this.subList, this.subNavVisiableCount, new ClickSubNavListener() {
            public void onClickSubNav(View v, int position, TextView view) {
                if (view.getText().equals(GameCenterActivity.this.subList.get(0))) {
                    if (GameCenterActivity.this.gameListViewPager != null) {
                        GameCenterActivity.this.gameListViewPager.setCurrentItem(position, true);
                    }
                } else if (view.getText().equals(GameCenterActivity.this.subList.get(1)) && GameCenterActivity.this.gameListViewPager != null) {
                    GameCenterActivity.this.gameListViewPager.setCurrentItem(position, true);
                }
            }

            public void initTextView(TextView view) {
                if (view != null && view.getTag() != null) {
                    if (((Integer) view.getTag()).intValue() == GameCenterActivity.this.currentPosition) {
                        view.setTextColor(GameCenterActivity.this.mcResource.getColor("mc_forum_tabbar_press_color"));
                    } else {
                        view.setTextColor(GameCenterActivity.this.mcResource.getColor("mc_forum_tabbar_normal_color"));
                    }
                }
            }
        });
        this.gameListViewPager = (ViewPager) findViewById(this.mcResource.getViewId("mc_game_content"));
        this.gameCenterPagerAdapter = new GameCenterPagerAdapter(getSupportFragmentManager());
        this.gameListViewPager.setAdapter(this.gameCenterPagerAdapter);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                GameCenterActivity.this.gameListViewPager.setCurrentItem(GameCenterActivity.this.currentPosition);
                GameCenterActivity.this.loadCurrentFragmentData();
            }
        }, 1000);
    }

    protected void initWidgetActions() {
        this.finishBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameCenterActivity.this.finish();
            }
        });
        this.gameListViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                GameCenterActivity.this.tabBarView.selectCurrentTab(position);
                GameCenterActivity.this.currentPosition = position;
                GameCenterActivity.this.loadCurrentFragmentData();
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private String getTagName(int position) {
        if (position == 0) {
            return GameApiConstant.RECOMMEND_TAG;
        }
        if (position == 1) {
            return GameApiConstant.LATEST_TAG;
        }
        if (position == 2) {
            return GameApiConstant.MY_TAG;
        }
        return null;
    }

    protected void onDestroy() {
        GameDataCache.getInstance().clearAllList();
        super.onDestroy();
    }
}
