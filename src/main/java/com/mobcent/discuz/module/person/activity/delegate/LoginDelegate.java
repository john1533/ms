package com.mobcent.discuz.module.person.activity.delegate;

public class LoginDelegate {
    private static final LoginDelegate loginDelegate = new LoginDelegate();
    private OnLoginSkipListener onLoginSkipListener;

    public interface OnLoginSkipListener {
        void loginSkip();
    }

    public static LoginDelegate getInstance() {
        return loginDelegate;
    }

    public void setOnLoginSkipListener(OnLoginSkipListener onLoginSkipListener) {
        this.onLoginSkipListener = onLoginSkipListener;
    }

    public OnLoginSkipListener getOnLoginSkipListener() {
        return this.onLoginSkipListener;
    }
}
