package com.mobcent.lowest.android.ui.widget.sign;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.utils.MCAnimationUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class SignInDialog extends AlertDialog {
    public String TAG = "RegisterDialog";
    private TranslateAnimation animation = null;
    private float fromXDelta;
    private Handler handler = new Handler();
    private ImageView iconBgImg;
    private float iconHeigth;
    private ImageView iconImg;
    private float iconWidth;
    private boolean isClicked = false;
    private MCResource mcResource;
    private boolean phoneShakeAble = true;
    private RelativeLayout registerBox;
    private RelativeLayout registerIconBox;
    private int screenHeight;
    private int screenWidth;
    private float toYDelta;
    private Vibrator vibrator = null;
    private View.OnClickListener viewOnclickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v != SignInDialog.this.registerBox && v == SignInDialog.this.iconImg) {
                SignInDialog.this.dismiss();
            }
        }
    };

    public void setPhoneShakeAble(boolean phoneShakeAble) {
        this.phoneShakeAble = phoneShakeAble;
    }

    public SignInDialog(Context context) {
        super(context);
        init(context);
    }

    public SignInDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        this.mcResource = MCResource.getInstance(context);
        this.iconWidth = (float) MCPhoneUtil.dip2px(context, 145.0f);
        this.iconHeigth = (float) MCPhoneUtil.dip2px(context, 220.0f);
        this.screenWidth = MCPhoneUtil.getDisplayWidth(context);
        this.screenHeight = MCPhoneUtil.getDisplayHeight(context);
        this.fromXDelta = (((float) this.screenWidth) - this.iconWidth) / 2.0f;
        this.toYDelta = (((float) this.screenHeight) - this.iconHeigth) / 2.0f;
        this.vibrator = (Vibrator) getContext().getSystemService("vibrator");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lps = getWindow().getAttributes();
        lps.width = MCPhoneUtil.getDisplayWidth(getContext());
        lps.height = MCPhoneUtil.getDisplayHeight(getContext());
        getWindow().setAttributes(lps);
        setContentView(this.mcResource.getLayoutId("mc_forum_register_dialog"));
        this.registerBox = (RelativeLayout) findViewById(this.mcResource.getViewId("mc_forum_register_layout"));
        this.registerIconBox = (RelativeLayout) findViewById(this.mcResource.getViewId("mc_forum_register_icon_layout"));
        this.iconImg = (ImageView) findViewById(this.mcResource.getViewId("mc_forum_icon_img"));
        this.iconBgImg = (ImageView) findViewById(this.mcResource.getViewId("mc_forum_icon_bg_img"));
        this.iconImg.setOnClickListener(this.viewOnclickListener);
    }

    public void dismiss() {
        super.dismiss();
        if (this.vibrator != null && isHasShakePermission()) {
            this.vibrator.cancel();
        }
    }

    public void registerImgClick() {
        shakePhone();
        MCAnimationUtils.shakeView(this.iconImg, new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SignInDialog.this.iconImg.clearAnimation();
                MCAnimationUtils.scaleAndAlpha(SignInDialog.this.registerIconBox, 500, new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        SignInDialog.this.registerIconBox.clearAnimation();
                        SignInDialog.this.dismiss();
                    }
                });
            }
        });
    }

    public void showDialog() {
        show();
        this.registerIconBox.setVisibility(8);
        this.animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, this.toYDelta + this.iconHeigth);
        this.animation.setDuration(600);
        this.animation.setFillAfter(true);
        this.animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SignInDialog.this.registerIconBox.clearAnimation();
                SignInDialog.this.iconBgImg.setVisibility(8);
                SignInDialog.this.setIconBoxLps(SignInDialog.this.fromXDelta, SignInDialog.this.toYDelta);
                SignInDialog.this.hideDialog();
            }
        });
        this.handler.postDelayed(new Runnable() {
            public void run() {
                SignInDialog.this.registerIconBox.setVisibility(0);
                SignInDialog.this.registerIconBox.startAnimation(SignInDialog.this.animation);
            }
        }, 100);
    }

    private void hideDialog() {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (SignInDialog.this.isShowing() && !SignInDialog.this.isClicked) {
                    SignInDialog.this.dismiss();
                }
            }
        }, 1000);
    }

    private void shakePhone() {
        if (this.vibrator != null && this.phoneShakeAble && isHasShakePermission()) {
            this.vibrator.vibrate(new long[]{200, 200, 200, 200}, 1);
        }
    }

    private void setIconBoxLps(float left, float top) {
        RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) this.registerIconBox.getLayoutParams();
        lps.leftMargin = (int) left;
        lps.topMargin = (int) top;
        this.registerIconBox.setLayoutParams(lps);
    }

    private boolean isHasShakePermission() {
        if (getContext().getPackageManager().checkPermission("android.permission.VIBRATE", getContext().getPackageName()) == 0) {
            return true;
        }
        return false;
    }

    protected void showMsg(String msg) {
        Toast.makeText(getContext(), msg, 1000).show();
    }
}
