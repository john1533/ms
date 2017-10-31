package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.UploadPhotosfragment;
import java.io.Serializable;
import java.util.List;

public class UploadPhotosActivity extends PopComponentActivity {
    private Fragment fragment;
    private List<PhotoModel> list;

    protected void initDatas() {
        super.initDatas();
        this.list = (List) getIntent().getSerializableExtra(BaseIntentConstant.BUNDLE_PHOTOMODEL);
    }

    protected Fragment initContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseIntentConstant.BUNDLE_PHOTOMODEL, (Serializable) this.list);
        this.fragment = new UploadPhotosfragment();
        this.fragment.setArguments(bundle);
        return this.fragment;
    }
}
