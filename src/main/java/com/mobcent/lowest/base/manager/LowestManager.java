package com.mobcent.lowest.base.manager;

import android.content.Context;
import com.mobcent.lowest.base.config.LowestConfig;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.game.config.GameConfig;
import com.mobcent.lowest.module.plaza.config.PlazaConfig;

public class LowestManager {
    private static LowestManager manager;
    private LowestConfig config;

    public static synchronized LowestManager getInstance() {
        LowestManager lowestManager;
        synchronized (LowestManager.class) {
            if (manager == null) {
                manager = new LowestManager();
            }
            lowestManager = manager;
        }
        return lowestManager;
    }

    public void init(Context context, LowestConfig lowestConfigImpl) {
        if (lowestConfigImpl == null) {
            throw new NullPointerException("lowestConfigImpl or plazaDelegate can not be null");
        }
        this.config = lowestConfigImpl;
        AdManager.getInstance().init(context.getApplicationContext());
        PlazaConfig.getInstance().setPlazaDelegate(lowestConfigImpl);
        GameConfig.getInstance().setGameConfigDelegate(lowestConfigImpl);
    }

    public LowestConfig getConfig() {
        if (this.config != null) {
            return this.config;
        }
        throw new NullPointerException("LowestManager config is not init");
    }
}
