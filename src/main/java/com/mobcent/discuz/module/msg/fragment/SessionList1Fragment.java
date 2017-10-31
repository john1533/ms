package com.mobcent.discuz.module.msg.fragment;

import android.view.View;
import com.mobcent.discuz.module.msg.fragment.adapter.SessionList1FragmentAdapter;
import com.mobcent.discuz.module.msg.helper.MsgNotificationHelper;

public class SessionList1Fragment extends BaseSessionListFragment {
    protected String getRootLayoutName() {
        return "msg_list1_fragment";
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        if (this.adapter == null) {
            this.adapter = new SessionList1FragmentAdapter(this.activity, this.userMsgList, this.componentModel);
        }
        this.msgListView.setAdapter(this.adapter);
    }

    public void onResume() {
        super.onResume();
        MsgNotificationHelper.getInstance(this.activity.getApplicationContext()).setAtSessionList(true);
    }

    public void onPause() {
        super.onPause();
        MsgNotificationHelper.getInstance(this.activity.getApplicationContext()).setAtSessionList(false);
    }
}
