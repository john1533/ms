package com.mobcent.discuz.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;

public class ErrorFragment extends BaseFragment {
    public static final int CONFIG_ERROR = 1;
    public static String TYPE = "type";
    private String error = "";
    private TextView errorText;
    private int type = 0;

    private class MyUriSpan extends URLSpan {
        private String url;

        public MyUriSpan(String url) {
            super(url);
            this.url = url;
        }

        public void onClick(View widget) {
            Intent i = new Intent(ErrorFragment.this.activity, WebViewFragmentActivity.class);
            i.putExtra("webViewUrl", this.url);
            ErrorFragment.this.startActivity(i);
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.error = bundle.getString("error");
            this.type = bundle.getInt(TYPE);
        }
    }

    protected String getRootLayoutName() {
        return "base_error_fragment";
    }

    protected void initViews(View rootView) {
        try {
            this.errorText = (TextView) findViewByName(rootView, "error_text");
            String guide = this.resource.getString("mc_forum_guide");
            if (this.type == 1) {
                SpannableString ss = new SpannableString(this.error + "\n\n" + guide);
                float size = this.activity.getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_19"));
                ss.setSpan(new MyUriSpan("http://bbs.appbyme.com/forum-57-1.html"), this.error.length(), ss.length(), 33);
                ss.setSpan(new AbsoluteSizeSpan((int) size), this.error.length(), ss.length(), 33);
                this.errorText.setText(ss);
            } else {
                this.errorText.setText(this.error);
            }
            this.errorText.setMovementMethod(new LinkMovementMethod());
        } catch (Exception e) {
        }
    }

    protected void initActions(View rootView) {
    }
}
