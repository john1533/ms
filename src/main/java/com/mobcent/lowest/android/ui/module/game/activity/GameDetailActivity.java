package com.mobcent.lowest.android.ui.module.game.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.android.ui.module.game.fragment.BaseGameFragment;
import com.mobcent.lowest.android.ui.module.game.fragment.GameCommentFragmet;
import com.mobcent.lowest.android.ui.module.game.fragment.GameDetailFragment;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView;
import com.mobcent.lowest.android.ui.widget.MCTabBarScrollView.ClickSubNavListener;
import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.config.GameConfig;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.mobcent.lowest.module.game.service.GameService;
import com.mobcent.lowest.module.game.service.impl.GameServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GameDetailActivity extends BaseGameFragmentActivity implements BaseRestfulApiConstant, GameApiConstant {
    private final String TAG = "GameDetailActivity";
    private Button backBtn;
    private Bundle bundle;
    private LinearLayout buttomLayout;
    private Button cancel;
    private Button comment;
    private TextView commentContent;
    private AlertDialog dlg;
    private GameDetailPagerAdapter gameDetailPagerAdapter;
    private ViewPager gameDetailViewPager;
    private GameService gameService;
    private Button intoGame;
    private Button share;
    protected final char splitChar = FinalConstant.SPLIT_CHAR;
    private List subList;
    private int subNavVisiableCount = 2;
    private Button submit;
    private MCTabBarScrollView tabBarView;
    protected final char tagImg = FinalConstant.TAG_IMG;
    private TextView titleText;
    private RelativeLayout topbar;
    private WebGameModel webGameModel;

    private class ReplyTextAsyncTask extends AsyncTask<Void, Void, Integer> {
        private String text = null;

        public ReplyTextAsyncTask(String text) {
            this.text = text;
        }

        protected Integer doInBackground(Void... params) {
            return Integer.valueOf(GameDetailActivity.this.gameService.commentGame("", GameDetailActivity.this.gameService.createCommentJson(this.text.trim(), "ß", "á"), GameDetailActivity.this.webGameModel.getGameId(), false, 0, 0, "", 0, 0));
        }

        protected void onPostExecute(Integer res) {
            if (res.intValue() == 1) {
                GameDetailActivity.this.showToast(GameDetailActivity.this.mcResource.getString("mc_game_detail_comment_succ"));
            } else {
                GameDetailActivity.this.showToast(GameDetailActivity.this.mcResource.getString("mc_game_detail_comment_fail"));
            }
        }
    }

    private class GameDetailPagerAdapter extends FragmentStatePagerAdapter {
        private Map<Integer, BaseGameFragment> fragmentMap = new HashMap();

        public GameDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (this.fragmentMap.get(Integer.valueOf(position)) != null) {
                return (Fragment) this.fragmentMap.get(Integer.valueOf(position));
            }
            BaseGameFragment baseGameFragment = null;
            Bundle bundle = new Bundle();
            if (position == 0) {
                baseGameFragment = new GameDetailFragment();
            }
            if (position == 1) {
                baseGameFragment = new GameCommentFragmet();
            }
            bundle.putSerializable(GameConstance.INTENT_TO_DETAIL_FRAGMENT_MODEL, GameDetailActivity.this.webGameModel);
            this.fragmentMap.put(Integer.valueOf(position), baseGameFragment);
            baseGameFragment.setArguments(bundle);
            return baseGameFragment;
        }

        public int getCount() {
            return GameDetailActivity.this.subList.size();
        }
    }

    protected void initData() {
        this.bundle = getIntent().getExtras();
        this.webGameModel = (WebGameModel) this.bundle.get(GameConstance.INTENT_TO_DETAIL_MODEL);
        this.subList = Arrays.asList(getResources().getStringArray(this.mcResource.getArrayId("mc_game_detail_channel")));
        this.subNavVisiableCount = this.subList.size();
        this.gameService = new GameServiceImpl(this.context);
    }

    protected void initViews() {
        String str;
        setContentView(this.mcResource.getLayoutId("mc_game_activity"));
        this.topbar = (RelativeLayout) findViewById(this.mcResource.getViewId("mc_game_topbar"));
        this.tabBarView = (MCTabBarScrollView) findViewById(this.mcResource.getViewId("mc_game_subnav"));
        this.tabBarView.setTabBoxView(this.mcResource.getDrawable("mc_forum_peripheral_tab_bg"), MCPhoneUtil.dip2px(this.context, 34.0f), MCPhoneUtil.getDisplayWidth(this));
        this.tabBarView.setArrowView(this.mcResource.getDrawable("mc_forum_tab_style1_glide"), MCPhoneUtil.dip2px(this.context, 3.0f), 0);
        this.tabBarView.setArrowWidthRatio(0.8f);
        this.tabBarView.setContainArrow(true);
        this.tabBarView.init(this.context, this.subList, this.subNavVisiableCount, new ClickSubNavListener() {
            public void onClickSubNav(View v, int position, TextView view) {
                if (view.getText().equals(GameDetailActivity.this.subList.get(0))) {
                    if (GameDetailActivity.this.gameDetailViewPager != null) {
                        GameDetailActivity.this.gameDetailViewPager.setCurrentItem(position, true);
                    }
                } else if (view.getText().equals(GameDetailActivity.this.subList.get(1)) && GameDetailActivity.this.gameDetailViewPager != null) {
                    GameDetailActivity.this.gameDetailViewPager.setCurrentItem(position, true);
                }
            }

            public void initTextView(TextView view) {
            }
        });
        this.titleText = (TextView) findViewById(this.mcResource.getViewId("mc_game_topbar_title_text"));
        this.titleText.setText(this.mcResource.getString("mc_game_detail_topbar_title"));
        this.backBtn = (Button) findViewById(this.mcResource.getViewId("mc_game_topbar_left_btn"));
        this.buttomLayout = (LinearLayout) findViewById(this.mcResource.getViewId("mc_game_detail_buttom_bar"));
        this.buttomLayout.setVisibility(0);
        this.comment = (Button) findViewById(this.mcResource.getViewId("mc_game_detail_buttom_comment"));
        this.intoGame = (Button) findViewById(this.mcResource.getViewId("mc_game_detail_buttom_into_game"));
        this.share = (Button) findViewById(this.mcResource.getViewId("mc_game_detail_buttom_share"));
        if (this.webGameModel.getGameType() == 1) {
            str = this.mcResource.getString("mc_game_center_into_game");
        } else {
            str = this.mcResource.getString("mc_forum_down_load_game");
        }
        this.intoGame.setText(str);
        this.gameDetailViewPager = (ViewPager) findViewById(this.mcResource.getViewId("mc_game_content"));
        this.gameDetailPagerAdapter = new GameDetailPagerAdapter(getSupportFragmentManager());
        this.gameDetailViewPager.setAdapter(this.gameDetailPagerAdapter);
    }

    protected void initWidgetActions() {
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameDetailActivity.this.finish();
            }
        });
        this.gameDetailViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                GameDetailActivity.this.tabBarView.selectCurrentTab(position);
                GameDetailActivity.this.currentPosition = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        this.comment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GameConfig.getInstance().getGameConfigDelegate().isLogin(v)) {
                    GameDetailActivity.this.showCommentDiaLog();
                } else {
                    GameDetailActivity.this.showToast(GameDetailActivity.this.mcResource.getString("mc_game_detail_login_prompt"));
                }
            }
        });
        this.intoGame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameDetailActivity.this.intoGame();
            }
        });
        this.share.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GameConfig.getInstance().getGameConfigDelegate() != null) {
                    GameConfig.getInstance().getGameConfigDelegate().shareWebGame(v, GameDetailActivity.this.webGameModel);
                }
            }
        });
    }

    private void intoGame() {
        String playGameUrl = getGameUrl();
        if (this.webGameModel.getGameType() == 1) {
            Intent openGamePageIntent = new Intent(this, PlazaWebViewActivity.class);
            openGamePageIntent.putExtra(PlazaConstant.WEB_VIEW_URL, playGameUrl);
            openGamePageIntent.putExtra(PlazaConstant.WEB_VIEW_TOP, false);
            startActivity(openGamePageIntent);
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        try {
            intent.setData(Uri.parse(playGameUrl));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, this.mcResource.getString("mc_game_no_down_load_url"), 0).show();
        }
    }

    private String getGameUrl() {
        String gameId = this.webGameModel.getGameId() + "";
        String tempURL = (MCResource.getInstance(this).getString("mc_forum_base_request_domain_url") + "game/playGame") + "?";
        String accessToken = LowestManager.getInstance().getConfig().getAccessToken();
        String accessSecret = LowestManager.getInstance().getConfig().getAccessSecret();
        String sdkVersion = LowestManager.getInstance().getConfig().getSDKVersion();
        long userId = LowestManager.getInstance().getConfig().getUserId();
        String forumKey = LowestManager.getInstance().getConfig().getForumKey();
        String forumId = LowestManager.getInstance().getConfig().getForumId();
        HashMap<String, String> params = new HashMap();
        params.put("forumId", forumId);
        params.put("forumKey", forumKey);
        if (!MCStringUtil.isEmpty(accessToken)) {
            params.put("accessToken", accessToken);
        }
        if (!MCStringUtil.isEmpty(accessSecret)) {
            params.put("accessSecret", accessSecret);
        }
        if (userId != 0) {
            params.put("userId", userId + "");
        }
        params.put("imei", MCPhoneUtil.getIMEI(this.context));
        params.put("imsi", MCPhoneUtil.getIMSI(this.context));
        String packageName = this.context.getPackageName();
        String appName = this.context.getApplicationInfo().loadLabel(this.context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put("sdkType", "");
        params.put("sdkVersion", sdkVersion);
        params.put("platType", "1");
        params.put("topicId", gameId + "");
        params.put("source", "1");
        params.put("platType", "1");
        List<NameValuePair> nameValuePairs = new ArrayList();
        if (!(params == null || params.isEmpty())) {
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, (String) params.get(key)));
                tempURL = tempURL + key + "=" + ((String) params.get(key)) + "&";
            }
        }
        MCLogUtil.i("GameDetailActivity", "game/playGame url = " + tempURL);
        return tempURL;
    }

    private void showCommentDiaLog() {
        this.dlg = new Builder(this).create();
        this.dlg.setView(this.inflater.inflate(this.mcResource.getLayoutId("mc_game_comment_dialog"), null));
        this.dlg.show();
        Window window = this.dlg.getWindow();
        int screentWidth = MCPhoneUtil.getDisplayWidth(this);
        int width = (screentWidth / 10) * 8;
        window.setLayout(width, (width / 3) * 4);
        this.dlg.setContentView(this.mcResource.getLayoutId("mc_game_comment_dialog"));
        final EditText commentContent = (EditText) window.findViewById(this.mcResource.getViewId("mc_game_detail_comment_content"));
        Button submit = (Button) window.findViewById(this.mcResource.getViewId("mc_game_detail_comment_submit"));
        Button cancel = (Button) window.findViewById(this.mcResource.getViewId("mc_game_detail_comment_cancel"));
        submit.getLayoutParams().width = screentWidth / 4;
        cancel.getLayoutParams().width = screentWidth / 4;
        submit.setSelected(true);
        submit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String comment = commentContent.getText().toString().trim();
                if (MCStringUtil.isEmpty(comment)) {
                    GameDetailActivity.this.showToast(GameDetailActivity.this.mcResource.getString("mc_game_detail_comment_empty"));
                    return;
                }
                ReplyTextAsyncTask replyTask = new ReplyTextAsyncTask(comment);
                GameDetailActivity.this.addAsyncTask(replyTask);
                replyTask.execute(new Void[0]);
                GameDetailActivity.this.dlg.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameDetailActivity.this.dlg.dismiss();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        clearAsyncTask();
    }
}
