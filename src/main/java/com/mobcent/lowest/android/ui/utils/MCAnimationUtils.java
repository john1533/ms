package com.mobcent.lowest.android.ui.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;

public class MCAnimationUtils {
    public static void shakeView(View v, AnimationListener animationListener) {
        int pivotX = v.getWidth() / 2;
        int pivotY = v.getHeight() / 2;
        AnimationSet animationSet = new AnimationSet(v.getContext(), null);
        Animation animation = createRotateAnimation(0, -15, pivotX, pivotY, 200 / 2, 0);
        animationSet.addAnimation(animation);
        long startTimeMillis = 0 + animation.getDuration();
        int offsetDegree = 0;
        for (int i = 0; i < 3; i++) {
            offsetDegree = (i % 2 == 0 ? 1 : -1) * 30;
            animation = createRotateAnimation(0, offsetDegree, pivotX, pivotY, 200, startTimeMillis);
            animationSet.addAnimation(animation);
            startTimeMillis += animation.getDuration();
        }
        animation = createRotateAnimation(0, (-offsetDegree) / 2, pivotX, pivotY, 200, startTimeMillis);
        animationSet.addAnimation(animation);
        startTimeMillis += animation.getDuration();
        if (animationListener == null) {
            final View view = v;
            AnimationListener anonymousClass1 = new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            };
        }
        animationSet.setAnimationListener(animationListener);
        animationSet.setFillAfter(true);
        v.startAnimation(animationSet);
    }

    public static void scaleAndAlpha(final View view, long duration, AnimationListener animationListener) {
        AnimationSet animationSet = new AnimationSet(view.getContext(), null);
        if (duration == 0) {
            duration = 500;
        }
        Animation animation = new ScaleAnimation(CustomConstant.RATIO_ONE_HEIGHT, 2.0f, CustomConstant.RATIO_ONE_HEIGHT, 2.0f, 1, 0.5f, 1, 0.5f);
        animation.setDuration(duration);
        animationSet.addAnimation(animation);
        animation = new AlphaAnimation(CustomConstant.RATIO_ONE_HEIGHT, 0.0f);
        animation.setDuration(duration);
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        if (animationListener == null) {
            animationListener = new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }
            };
        }
        animationSet.setAnimationListener(animationListener);
        view.startAnimation(animationSet);
    }

    private static Animation createRotateAnimation(int fromDegrees, int toDegrees, int pivotX, int pivotY, long duration, long startTimeMillis) {
        Animation animation = new RotateAnimation((float) fromDegrees, (float) toDegrees, (float) pivotX, (float) pivotY);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        animation.setStartOffset(startTimeMillis);
        return animation;
    }

    public static void rotateView(View view) {
        RotateAnimation animation = new RotateAnimation(0.0f, 3.6E8f, 1, 0.5f, 1, 0.5f);
        animation.setDuration(1400000000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }
}
