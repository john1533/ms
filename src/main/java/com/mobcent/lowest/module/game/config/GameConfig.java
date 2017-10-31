package com.mobcent.lowest.module.game.config;

import android.view.View;
import com.mobcent.lowest.module.game.model.WebGameModel;

public class GameConfig {
    private static GameConfig instance;
    private GameDelegate gameConfigDelegate;
    private boolean isLogin;

    public interface GameDelegate {
        void clickScreenShotImg(View view, String[] strArr);

        boolean isLogin(View view);

        void shareWebGame(View view, WebGameModel webGameModel);
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public GameDelegate getGameConfigDelegate() {
        return this.gameConfigDelegate;
    }

    public void setGameConfigDelegate(GameDelegate gameConfigDelegate) {
        this.gameConfigDelegate = gameConfigDelegate;
    }
}
