package com.mobcent.discuz.base.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentOptHelper {
    public FragmentManager fm;
    private FragmentTransaction ft;

    public FragmentOptHelper(FragmentManager fm) {
        this.fm = fm;
    }

    public void addFragment(int boxId, Fragment fragment) {
        if (fragment != null) {
            getFt().add(boxId, fragment);
            closeFt();
        }
    }

    public void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getFt().hide(fragment);
            closeFt();
        }
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            getFt().show(fragment);
            closeFt();
        }
    }

    public void replaceFragment(int boxId, Fragment fragment) {
        if (fragment != null) {
            getFt().replace(boxId, fragment);
            closeFt();
        }
    }

    private FragmentTransaction getFt() {
        if (this.ft == null) {
            this.ft = this.fm.beginTransaction();
        }
        return this.ft;
    }

    private void closeFt() {
        if (this.ft != null) {
            this.ft.addToBackStack(null);
            this.ft.commit();
            this.ft = null;
        }
    }
}
