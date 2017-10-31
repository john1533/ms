package com.mobcent.discuz.module.publish.fragment.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.widget.MCPollEditBar;
import com.mobcent.discuz.base.widget.MCPollEditBar.PollEditBarDelegate;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.List;

public class PublishPollTopicActivity extends PublishTopicActivity implements FinalConstant {
    private final int INIT_POLL_NUM = 2;
    private LinearLayout addBox;
    private Button addBtn;
    private String contentStr;
    private EditText deatimeEdt;
    private String overtime;
    private PublishPollAsyncTask pollAsyncTask;
    private LinearLayout pollBox;
    private String pollCount;
    private EditText pollCountEdt;
    private List<MCPollEditBar> pollList;
    private List<String> pollLists;
    private int pollType = 0;
    private int pollVisible = 0;

    class PublishPollAsyncTask extends AsyncTask<Object, Void, BaseResultModel<Object>> {
        PublishPollAsyncTask() {
        }

        protected void onPreExecute() {
            ((InputMethodManager) PublishPollTopicActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PublishPollTopicActivity.this.titleEdText.getWindowToken(), 0);
            ((InputMethodManager) PublishPollTopicActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PublishPollTopicActivity.this.contentEdText.getWindowToken(), 0);
            PublishPollTopicActivity.this.publishIng = true;
//            PublishPollTopicActivity.this.sendBtn.setEnabled(false);
        }

        protected BaseResultModel<Object> doInBackground(Object... params) {
            PostsService postsService = new PostsServiceImpl(PublishPollTopicActivity.this);
            if (params[0] == null || params[1] == null || params[2] == null) {
                return null;
            }
            long boardId = Long.parseLong((String) params[0]);
            if (PublishPollTopicActivity.this.requireLocation == 0) {
                PublishPollTopicActivity.this.locationStr = "";
            }
            return postsService.publishPollTopic(boardId, (String) params[1], (String) params[2], PublishPollTopicActivity.this.pollType + "", PublishPollTopicActivity.this.pollVisible, (String) params[3], PublishPollTopicActivity.this.overtime, PublishPollTopicActivity.this.longitude, PublishPollTopicActivity.this.latitude, PublishPollTopicActivity.this.locationStr, 1, PublishPollTopicActivity.this.getAid(), (PermissionModel) params[4]);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            DZProgressDialogUtils.hideProgressDialog();
            if (result != null) {
                if (MCStringUtil.isEmpty(result.getErrorInfo()) || result.getRs() != 0) {
                    MCToastUtils.toastByResName(PublishPollTopicActivity.this, "mc_forum_publish_succ", 0);
                } else {
                    MCToastUtils.toast(PublishPollTopicActivity.this.getApplicationContext(), result.getErrorInfo(), 1);
                }
                PublishPollTopicActivity.this.sharedPreferencesDB.setRefresh(true);
//                PublishPollTopicActivity.this.sendBtn.setEnabled(true);
                PublishPollTopicActivity.this.publishIng = false;
            }
        }
    }

    protected void initDatas() {
        this.draftId = 2;
        super.initDatas();
        this.pollLists = new ArrayList();
        this.pollList = new ArrayList();
        this.isPublishTopic = false;
    }

    protected String getLayoutName() {
        return "publish_poll_topic_activity";
    }

    protected void initViews() {
        super.initViews();
        this.addBtn = (Button) findViewByName("mc_froum_add_btn");
        this.addBox = (LinearLayout) findViewByName("mc_froum_add_box");
        this.pollCountEdt = (EditText) findViewByName("mc_forum_poll_count_edt");
        this.deatimeEdt = (EditText) findViewByName("mc_forum_poll_deatime_edt");
        this.pollBox = (LinearLayout) findViewByName("mc_forum_poll_item");
        for (int i = 0; i < 2; i++) {
            MCPollEditBar voteItem = createPollBar(0, "");
            voteItem.hideDeleteBtn();
            this.pollBox.addView(voteItem.getView());
            this.pollList.add(voteItem);
        }
        updatePollItemNum();
    }

    protected void initActions() {
        super.initActions();
//        this.sendBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
//                if (!PublishPollTopicActivity.this.publishIng) {
//                    PublishPollTopicActivity.this.publishIng = true;
//                    PublishPollTopicActivity.this.publicTopic();
//                }
//            }
//        });
        this.addBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishPollTopicActivity.this.addPollItemEvent();
            }
        });
        this.addBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishPollTopicActivity.this.addPollItemEvent();
            }
        });
    }

    private void addPollItemEvent() {
        if (this.pollList.size() <= 20) {
            MCPollEditBar voteItem = createPollBar(0, "");
            this.pollBox.addView(voteItem.getView());
            this.pollList.add(voteItem);
            if (this.pollList.size() >= 20) {
                this.addBox.setVisibility(8);
            }
            updateInitPollItemView();
            updatePollItemNum();
        }
    }

    private MCPollEditBar createPollBar(int pollNum, String s) {
        return new MCPollEditBar(this, pollNum, s, new PollEditBarDelegate() {
            public void deletePollBar(View v, MCPollEditBar bar) {
                if (PublishPollTopicActivity.this.pollList.size() > 2) {
                    PublishPollTopicActivity.this.pollBox.removeView(v);
                    PublishPollTopicActivity.this.pollList.remove(bar);
                    PublishPollTopicActivity.this.updatePollItemNum();
                    PublishPollTopicActivity.this.updateInitPollItemView();
                    PublishPollTopicActivity.this.addBox.setVisibility(0);
                }
            }

            public void onEditClick(View v) {
            }
        });
    }

    private List<String> getPollcontent() {
        List<String> pollContet = new ArrayList();
        int i = 0;
        while (i < this.pollList.size()) {
            MCPollEditBar pollBar = (MCPollEditBar) this.pollList.get(i);
            if (MCStringUtil.isEmpty(pollBar.getEditContent())) {
                Toast.makeText(this, MCStringBundleUtil.resolveString(MCResource.getInstance(this).getStringId("mc_forum_poll_item_not_null"), (i + 1) + "", (Context) this), 0).show();
                this.publishIng = false;
                return null;
            } else if (pollBar.getEditContent().length() > 20) {
                Toast.makeText(this, getResources().getString(this.resource.getStringId("mc_forum_poll_item_len")), 0).show();
                this.publishIng = false;
                return null;
            } else {
                pollContet.add(pollBar.getEditContent());
                i++;
            }
        }
        return pollContet;
    }

    private void updatePollItemNum() {
        for (int i = 1; i <= this.pollList.size(); i++) {
            ((MCPollEditBar) this.pollList.get(i - 1)).getNum().setText(i + "");
        }
    }

    private void updateInitPollItemView() {
        if (!this.pollList.isEmpty() && this.pollList.size() == 2) {
            ((MCPollEditBar) this.pollList.get(0)).hideDeleteBtn();
            ((MCPollEditBar) this.pollList.get(1)).hideDeleteBtn();
        } else if (!this.pollList.isEmpty() && this.pollList.size() > 2) {
            ((MCPollEditBar) this.pollList.get(0)).showDeleteBtn();
            ((MCPollEditBar) this.pollList.get(1)).showDeleteBtn();
        }
    }

    protected boolean publicTopic() {
        getMentionedFriend();
        this.title = this.titleEdText.getText().toString();
        this.content = this.contentEdText.getText().toString();
        this.pollCount = this.pollCountEdt.getText().toString();
        this.overtime = this.deatimeEdt.getText().toString();
        if (this.boardId <= 0) {
            this.publishIng = false;
            Toast.makeText(this, getResources().getString(this.resource.getStringId("mc_forum_publish_select_board")), 0).show();
            return false;
        } else if (this.title == null || this.title.trim().equals("")) {
            this.publishIng = false;
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_title_length_error");
            return false;
        } else if (MCStringUtil.isEmpty(this.content) || this.content.length() <= 7000) {
            if (!MCStringUtil.isEmpty(this.pollCount)) {
                if (MCStringUtil.isNumeric(this.pollCount)) {
                    this.pollType = new Integer(this.pollCount).intValue();
                } else {
                    this.publishIng = false;
                    MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_select_poll_count", 1);
                    return false;
                }
            }
            if (this.pollType > this.pollList.size()) {
                this.publishIng = false;
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_error_select_poll_count", 1);
                return false;
            } else if (MCStringUtil.isEmpty(this.overtime) || MCStringUtil.isNumeric(this.overtime)) {
                this.pollLists = getPollcontent();
                if (MCListUtils.isEmpty(this.pollLists)) {
                    this.publishIng = false;
                    return false;
                }
                hideSoftKeyboard();
                finish();
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_publish", 0);
                if (this.pollLists != null) {
                    if (this.picMap.size() > 0) {
                        uploadPic();
                    } else if (this.hasAudio) {
                        uploadAudio();
                    } else {
                        uploadAudioSucc();
                    }
                }
                return true;
            } else {
                this.publishIng = false;
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_poll_overtime", 1);
                return false;
            }
        } else {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_max_length_error", 1);
            this.publishIng = false;
            return false;
        }
    }

    protected void uploadAudioSucc() {
        if (this.content != null) {
            this.contentStr = this.postsService.createPublishTopicJson(this.content.trim(), "ß", "á", this.audioPath, this.uploadList);
        }
        String pollContent = this.postsService.createPublishPollTopicJson(this.pollLists);
        if (this.pollAsyncTask != null) {
            this.pollAsyncTask.cancel(true);
        }
        this.pollAsyncTask = new PublishPollAsyncTask();
        this.pollAsyncTask.execute(new Object[]{this.boardId + "", this.title, this.contentStr, pollContent, this.isCheckedPermissionModel});
    }
}
