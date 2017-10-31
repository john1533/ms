package com.mobcent.lowest.module.plaza.config;

import com.mobcent.lowest.base.delegate.PlazaDelegate;

public class PlazaConfig {
    public static String BASE_SEARCH_REQUEST_URL = null;
    private static PlazaConfig plazaConfig;
    private PlazaDelegate plazaDelegate;

    public static PlazaConfig getInstance() {
        if (plazaConfig == null) {
            plazaConfig = new PlazaConfig();
        }
        return plazaConfig;
    }

    public PlazaDelegate getPlazaDelegate() {
        return this.plazaDelegate;
    }

    public void setPlazaDelegate(PlazaDelegate plazaDelegate) {
        this.plazaDelegate = plazaDelegate;
    }
}
