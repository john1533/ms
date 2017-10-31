package com.mobcent.lowest.base.delegate;

import android.content.Context;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.mobcent.lowest.module.plaza.model.SearchModel;

public interface PlazaDelegate {
    void onAboutClick(Context context);

    void onAppItemClick(Context context, PlazaAppModel plazaAppModel);

    void onPersonalClick(Context context);

    boolean onPlazaBackPressed(Context context);

    void onSearchItemClick(Context context, SearchModel searchModel);

    void onSetClick(Context context);
}
