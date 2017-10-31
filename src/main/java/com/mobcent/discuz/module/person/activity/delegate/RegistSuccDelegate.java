package com.mobcent.discuz.module.person.activity.delegate;

public class RegistSuccDelegate {
    private static final RegistSuccDelegate loginDelegate = new RegistSuccDelegate();
    private OnRegistSuccListener onRegistSuccListener;

    public interface OnRegistSuccListener {
        void registSucc();
    }

    public static RegistSuccDelegate getInstance() {
        return loginDelegate;
    }

    public void setOnRegistSuccListener(OnRegistSuccListener onRegistSuccListener) {
        this.onRegistSuccListener = onRegistSuccListener;
    }

    public OnRegistSuccListener getOnRegistSuccListener() {
        return this.onRegistSuccListener;
    }
}
