package com.mobcent.lowest.android.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;

public class PhoneReceiver extends BroadcastReceiver {

    class MyPhoneStateListener extends PhoneStateListener {
        private Context context;

        public MyPhoneStateListener(Context context) {
            this.context = context.getApplicationContext();
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case 0:
                    MCAudioUtils.getInstance(this.context).phonePlayAudio();
                    return;
                case 1:
                    MCAudioUtils.getInstance(this.context).phonePauseAudio();
                    return;
                case 2:
                    MCAudioUtils.getInstance(this.context).phonePauseAudio();
                    return;
                default:
                    return;
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                MCAudioUtils.getInstance(context).phonePauseAudio();
            } else {
                ((TelephonyManager) context.getSystemService("phone")).listen(new MyPhoneStateListener(context), 32);
            }
        } catch (Exception e) {
        }
    }
}
