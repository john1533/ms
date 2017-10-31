package com.mobcent.discuz.base.model;

import android.view.Menu;

import java.util.List;

public class TopSettingModel {
    public boolean isTitleClickAble = false;
    public boolean isVisibile = true;
    public List<TopBtnModel> leftModels;
    public List<TopBtnModel> rightModels;
    public int style = 0;
    public String leftTitle;
    public String title;

    public OptionsMenuListener optionsMenuListener;

    public static interface OptionsMenuListener{
        public void createMenu(Menu menu);
    }
}
