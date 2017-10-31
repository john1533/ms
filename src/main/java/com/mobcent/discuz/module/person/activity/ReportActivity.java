package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.ReportFragment;

public class ReportActivity extends PopComponentActivity {
    private int reportType;
    private Long userId;

    protected void initDatas() {
        super.initDatas();
        this.reportType = getIntent().getIntExtra(IntentConstant.REPORT_TYPE, 0);
        this.userId = Long.valueOf(getIntent().getLongExtra(IntentConstant.REPOR_OBJECT_ID, 0));
    }

    protected Fragment initContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstant.REPORT_TYPE, this.reportType);
        bundle.putLong(IntentConstant.REPOR_OBJECT_ID, this.userId.longValue());
        Fragment fragment = new ReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
