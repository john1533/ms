package com.mobcent.lowest.android.ui.module.plaza.activity.adapter;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.mobcent.lowest.android.ui.module.plaza.fragment.PlazaFragment;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlazaViewPagerAdapter extends FragmentStatePagerAdapter {
    public String TAG = "PlazaViewPagerAdapter";
    private Map<Integer, PlazaFragment> fragmentMap = new HashMap();
    private int girdCutLength = 12;
    private Handler handler = new Handler();
    private int pageCount = 0;
    private List<PlazaAppModel> plazaAppModels;

    public PlazaViewPagerAdapter(FragmentManager fm, List<PlazaAppModel> plazaAppModels) {
        super(fm);
        this.plazaAppModels = plazaAppModels;
    }

    public Fragment getItem(int position) {
        if (this.fragmentMap.get(Integer.valueOf(position)) == null) {
            PlazaFragment fragment = new PlazaFragment();
            fragment.setHandler(this.handler);
            fragment.setPlazaAppModels(getPositionData(position));
            this.fragmentMap.put(Integer.valueOf(position), fragment);
        }
        return (Fragment) this.fragmentMap.get(Integer.valueOf(position));
    }

    public int getCount() {
        return getDealCount();
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        this.fragmentMap.remove(Integer.valueOf(position));
    }

    private List<PlazaAppModel> getPositionData(int position) {
        List<PlazaAppModel> plazaList = new ArrayList();
        int endLen = position == this.pageCount + -1 ? this.plazaAppModels.size() : (position + 1) * this.girdCutLength;
        for (int i = position * this.girdCutLength; i < endLen; i++) {
            plazaList.add(this.plazaAppModels.get(i));
        }
        return plazaList;
    }

    private int getDealCount() {
        if (this.plazaAppModels == null || this.plazaAppModels.size() == 0) {
            return 0;
        }
        int appCount = this.plazaAppModels.size();
        if (appCount % this.girdCutLength == 0) {
            this.pageCount = appCount / this.girdCutLength;
        } else {
            this.pageCount = (appCount / this.girdCutLength) + 1;
        }
        return this.pageCount;
    }

    public void setPlazaAppModels(List<PlazaAppModel> plazaAppModels) {
        this.plazaAppModels = plazaAppModels;
    }
}
