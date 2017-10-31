package com.mobcent.discuz.base.widget;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCResource;

public class MCPollEditBar {
    private EditText contentEdit;
    private Context context;
    private ImageView deleImg;
    private LayoutInflater layoutInflater;
    private int num;
    private TextView numText;
    private PollEditBarDelegate pollEditBarDelegate;
    private MCResource resource;
    private View view;

    public interface PollEditBarDelegate {
        void deletePollBar(View view, MCPollEditBar mCPollEditBar);

        void onEditClick(View view);
    }

    public MCPollEditBar(Activity activity, int num, PollEditBarDelegate delegate) {
        this.layoutInflater = activity.getLayoutInflater();
        this.resource = MCResource.getInstance(activity);
        this.pollEditBarDelegate = delegate;
        this.num = num;
        this.context = activity;
        initVoteBar();
        initVoteBarActions();
        updateVoteBar();
    }

    public MCPollEditBar(Activity activity, int num, String s, PollEditBarDelegate delete) {
        this(activity, num, delete);
        this.contentEdit.setText(s);
    }

    private void updateVoteBar() {
        this.numText.setText(this.num + "");
    }

    private void initVoteBar() {
        this.view = this.layoutInflater.inflate(this.resource.getLayoutId("widget_poll_edit_item"), null);
        this.numText = (TextView) this.view.findViewById(this.resource.getViewId("mc_forum_publish_vote_num_text"));
        this.contentEdit = (EditText) this.view.findViewById(this.resource.getViewId("mc_forum_publish_vote_conent_edit"));
        this.deleImg = (ImageView) this.view.findViewById(this.resource.getViewId("mc_forum_publish_vote_delete_img"));
    }

    private void initVoteBarActions() {
        this.deleImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MCPollEditBar.this.getEditContent().length() > 0) {
                    Builder exitAlertDialog = new Builder(MCPollEditBar.this.context).setTitle(MCPollEditBar.this.resource.getStringId("mc_forum_dialog_tip")).setMessage(MCPollEditBar.this.resource.getStringId("mc_forum_warn_poll_delete"));
                    exitAlertDialog.setNegativeButton(MCPollEditBar.this.resource.getStringId("mc_forum_dialog_cancel"), null);
                    exitAlertDialog.setPositiveButton(MCPollEditBar.this.resource.getStringId("mc_forum_dialog_confirm"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MCPollEditBar.this.pollEditBarDelegate.deletePollBar(MCPollEditBar.this.view, MCPollEditBar.this);
                        }
                    });
                    exitAlertDialog.show();
                    return;
                }
                MCPollEditBar.this.pollEditBarDelegate.deletePollBar(MCPollEditBar.this.view, MCPollEditBar.this);
            }
        });
        this.contentEdit.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MCPollEditBar.this.pollEditBarDelegate.onEditClick(v);
                return false;
            }
        });
        this.contentEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MCPollEditBar.this.contentEdit.getText().toString().equals("")) {
                    MCPollEditBar.this.contentEdit.setBackgroundResource(MCPollEditBar.this.resource.getDrawableId("mc_forum_input_box"));
                } else {
                    MCPollEditBar.this.contentEdit.setBackgroundColor(0);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    public View getView() {
        return this.view;
    }

    public String getEditContent() {
        return this.contentEdit.getText().toString();
    }

    public TextView getNum() {
        return this.numText;
    }

    public void hideDeleteBtn() {
        this.deleImg.setVisibility(4);
    }

    public void showDeleteBtn() {
        this.deleImg.setVisibility(0);
    }
}
