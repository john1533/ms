package com.mobcent.discuz.activity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;

public class MCHeaderSearchView extends RelativeLayout {
    private Button button;
    private OnSearchClickListener clickListener;
    private Context context;
    private EditText editText;
    private MCResource forumResource;
    private String keywordStr;

    public interface OnSearchClickListener {
        void OnClickListener(String str);
    }

    public MCHeaderSearchView(Context context, AttributeSet attrs) {
        this(context);
    }

    public MCHeaderSearchView(Context context) {
        this(context, 0, "mc_forum_bg1");
    }

    public MCHeaderSearchView(Context context, int bottom, String background) {
        super(context);
        initHeadSearhView(context, bottom, bottom, bottom, bottom, background);
        this.context = context;
    }

    public MCHeaderSearchView(Context context, int topBottom, int leftRight, String background) {
        super(context);
        initHeadSearhView(context, topBottom, topBottom, leftRight, leftRight, background);
        this.context = context;
    }

    public MCHeaderSearchView(Context context, int top, int bottom, int left, int right, String background) {
        super(context);
        initHeadSearhView(context, top, bottom, left, right, background);
        this.context = context;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initHeadSearhView(final Context context, int top, int bottom, int left, int right, String background) {
        this.forumResource = MCResource.getInstance(context);
        this.editText = new EditText(context);
        LayoutParams lp1 = new LayoutParams(-1, MCPhoneUtil.getRawSize(context, 1, 32.0f));
        lp1.topMargin = MCPhoneUtil.getRawSize(context, 1, (float) top);
        lp1.bottomMargin = MCPhoneUtil.getRawSize(context, 1, (float) bottom);
        this.editText.setLayoutParams(lp1);
        this.editText.setTextSize(13.0f);
        this.editText.setSingleLine(true);
        this.editText.setHintTextColor(getResources().getColorStateList(this.forumResource.getColorId("mc_forum_text7_normal_color")));
        this.editText.setHint(this.forumResource.getString("mc_forum_search_keywords"));
        this.editText.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_card_search_bg"));
        this.editText.setTextColor(getResources().getColorStateList(this.forumResource.getColorId("mc_forum_text3_content_normal_color")));
        this.button = new Button(context);
        LayoutParams lp2 = new LayoutParams(MCPhoneUtil.getRawSize(context, 1, 44.0f), MCPhoneUtil.getRawSize(context, 1, 32.0f));
        lp2.topMargin = MCPhoneUtil.getRawSize(context, 1, (float) top);
        lp2.bottomMargin = MCPhoneUtil.getRawSize(context, 1, (float) bottom);
        lp2.addRule(11);
        this.button.setLayoutParams(lp2);
        setLayoutParams(new AbsListView.LayoutParams(-1, MCPhoneUtil.getRawSize(context, 1, (float) ((top + 32) + bottom))));
        setPadding(MCPhoneUtil.getRawSize(context, 1, (float) left), 0, MCPhoneUtil.getRawSize(context, 1, (float) right), 0);
        setBackgroundResource(this.forumResource.getDrawableId(background));
        this.button.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_card_search"));
        addView(this.editText);
        addView(this.button);
        this.editText.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MCHeaderSearchView.this.editText.requestFocus();
                ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(0, 1);
                return false;
            }
        });
        this.editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    MCHeaderSearchView.this.editText.setFocusable(false);
                    MCHeaderSearchView.this.editText.setFocusableInTouchMode(true);
                    if (context instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) context).hideSoftKeyboard();
                    }
                    MCHeaderSearchView.this.searchClick();
                }
                MCHeaderSearchView.this.editText.setFocusable(true);
                return false;
            }
        });
        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCHeaderSearchView.this.searchClick();
            }
        });
    }

    private void searchClick() {
        this.keywordStr = this.editText.getText().toString();
        if (MCStringUtil.isEmpty(this.keywordStr)) {
            Toast.makeText(this.context, this.forumResource.getString("mc_forum_no_keywords"), 0).show();
        } else if (this.clickListener != null) {
            this.clickListener.OnClickListener(this.keywordStr);
        }
    }

    public OnSearchClickListener getClickListener() {
        return this.clickListener;
    }

    public void setClickListener(OnSearchClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
