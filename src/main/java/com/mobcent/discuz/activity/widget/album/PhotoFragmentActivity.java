package com.mobcent.discuz.activity.widget.album;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;

public class PhotoFragmentActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        return new PhotoFragment();
    }
}
