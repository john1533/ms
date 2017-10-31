package com.mobcent.lowest.android.ui.module.game.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.widget.InnerHorizontalScrollView;
import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.config.GameConfig;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GameDetailFragment extends BaseGameFragment implements BaseRestfulApiConstant, GameApiConstant {
    private final int FULL_STAT = 5;
    private final String TAG = "GameDetailFragment";
    private Bundle bundle;
    private TextView desc;
    private InnerHorizontalScrollView innerScrollView;
    private TextView name;
    private ScrollView parentScrollView;
    private LinearLayout picContainer;
    private Button startGame;
    private ImageView thumbnail;
    private WebGameModel webGameModel;

    protected void initData() {
        this.bundle = getArguments();
        this.webGameModel = (WebGameModel) this.bundle.get(GameConstance.INTENT_TO_DETAIL_FRAGMENT_MODEL);
    }

    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String str;
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_game_detail_fragment"), container, false);
        this.thumbnail = (ImageView) view.findViewById(this.mcResource.getViewId("mc_game_detail_thumbnail"));
        this.name = (TextView) view.findViewById(this.mcResource.getViewId("mc_game_detail_game_name"));
        this.startGame = (Button) view.findViewById(this.mcResource.getViewId("mc_game_detail_into_game"));
        LinearLayout starLayout = (LinearLayout) view.findViewById(this.mcResource.getViewId("mc_game_detail_game_rank"));
        TextView playersTv = (TextView) view.findViewById(this.mcResource.getViewId("mc_game_detail_game_player"));
        this.innerScrollView = (InnerHorizontalScrollView) view.findViewById(this.mcResource.getViewId("mc_game_detail_pic"));
        this.parentScrollView = (ScrollView) view.findViewById(this.mcResource.getViewId("mc_game_detail_buttom_content"));
        this.innerScrollView.setParentScrollView(this.parentScrollView);
        this.picContainer = (LinearLayout) view.findViewById(this.mcResource.getViewId("mc_geme_detai_pic"));
        this.desc = (TextView) view.findViewById(this.mcResource.getViewId("mc_game_detail_game_description"));
        if (this.webGameModel.getGameType() == 1) {
            str = this.mcResource.getString("mc_game_center_into_game");
        } else {
            str = this.mcResource.getString("mc_forum_down_load_game");
        }
        addStar(starLayout, this.webGameModel.getGameStars());
        playersTv.setText(this.mcResource.getString("mc_game_player_num_pre") + this.webGameModel.getGamePlayer() + this.mcResource.getString("mc_game_player_num_suffix"));
        this.startGame.setText(str);
        this.name.setText(this.webGameModel.getGameName());
        this.desc.setText(this.webGameModel.getGameDesc());
        addPicView(this.webGameModel.getGameScreenshots(), this.picContainer);
        ImageLoader.getInstance().displayImage(this.webGameModel.getGameIcon(), this.thumbnail);
        return view;
    }

    protected void initWidgetActions() {
        this.startGame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameDetailFragment.this.intoGame();
            }
        });
    }

    private void intoGame() {
        String gameUrl = getGameUrl();
        if (this.webGameModel.getGameType() == 1) {
            Intent openGamePageIntent = new Intent(this.activity, PlazaWebViewActivity.class);
            MCLogUtil.e("intoGame.setOnClickListener", "gameUrl = " + gameUrl);
            openGamePageIntent.putExtra(PlazaConstant.WEB_VIEW_URL, gameUrl);
            openGamePageIntent.putExtra(PlazaConstant.WEB_VIEW_TOP, false);
            startActivity(openGamePageIntent);
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        try {
            intent.setData(Uri.parse(gameUrl));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, this.mcResource.getString("mc_game_no_down_load_url"),Toast.LENGTH_SHORT).show();
        }
    }

    private String getGameUrl() {
        String gameId = this.webGameModel.getGameId() + "";
        String tempURL = (MCResource.getInstance(this.context).getString("mc_forum_base_request_domain_url") + "game/playGame") + "?";
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
        MCLogUtil.i("GameDetailFragment", "game/playGame url = " + tempURL);
        return tempURL;
    }

    private void addStar(LinearLayout startLayout, int starNum) {
        startLayout.removeAllViews();
        for (int i = 0; i < starNum; i++) {
            ImageView iv = new ImageView(this.activity);
            iv.setImageDrawable(this.mcResource.getDrawable("mc_forum_game_star1"));
            int width = MCPhoneUtil.dip2px(this.activity, 15.0f);
            startLayout.addView(iv, width, width);
        }
        for (int j = 0; j < 5 - starNum; j++) {
            ImageView iv = new ImageView(this.activity);
            iv.setImageDrawable(this.mcResource.getDrawable("mc_forum_game_star2"));
            int width = MCPhoneUtil.dip2px(this.activity, 15.0f);
            startLayout.addView(iv, width, width);
        }
    }

    private void addPicView(final String[] urlArr, LinearLayout linearLayout) {
        for (int i = 0; i < urlArr.length; i++) {
            String myUrl = urlArr[i];
            final ImageView iv = new ImageView(this.activity);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GameDetailFragment.this.setScreenShotClickListener(iv, urlArr);
                }
            });
            iv.setTag(Integer.valueOf(i));
            iv.setPadding(0, 0, MCPhoneUtil.dip2px(this.activity, 20.0f), 0);
            iv.setScaleType(ScaleType.FIT_XY);
            int screenWidth = MCPhoneUtil.getDisplayWidth(this.activity);
            int screenHeight = MCPhoneUtil.getDisplayHeight(this.activity);
            int height = MCPhoneUtil.dip2px(this.activity, 200.0f);
            int width = (MCPhoneUtil.dip2px(this.activity, 260.0f) * screenWidth) / screenHeight;
            if (i + 1 == urlArr.length) {
                width -= MCPhoneUtil.dip2px(this.activity, 20.0f);
                iv.setPadding(0, 0, 0, 0);
            }
            linearLayout.addView(iv, new LayoutParams(width, height));
            ImageLoader.getInstance().displayImage(myUrl, iv);
        }
    }

    private void setScreenShotClickListener(ImageView iv, String[] urlArr) {
        if (GameConfig.getInstance().getGameConfigDelegate() != null) {
            GameConfig.getInstance().getGameConfigDelegate().clickScreenShotImg(iv, urlArr);
        }
    }

    public void loadDataByNet() {
    }
}
