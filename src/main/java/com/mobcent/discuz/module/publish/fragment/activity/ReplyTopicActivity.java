package com.mobcent.discuz.module.publish.fragment.activity;

import android.os.AsyncTask;
import android.view.inputmethod.InputMethodManager;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;

public class ReplyTopicActivity extends PublishTopicActivity {
    private static ReplyRetrunDelegate replyRetrunDelegate;
    private boolean isQuote;
    private ReplyAsyncTask replyAsyncTask;
    private SharedPreferencesDB sharedPreferencesDB;
    private long toReplyId;

    class ReplyAsyncTask extends AsyncTask<Object, Void, BaseResultModel<Object>> {
        ReplyAsyncTask() {
        }

        protected void onPreExecute() {
            ((InputMethodManager) ReplyTopicActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(ReplyTopicActivity.this.contentEdText.getWindowToken(), 0);
        }

        protected BaseResultModel<Object> doInBackground(Object... params) {
            String rContent = (String)params[0];

            return ReplyTopicActivity.this.postsService.publishTopic(ReplyTopicActivity.this.postsService.createReplyJson(ReplyTopicActivity.this.boardId, ReplyTopicActivity.this.topicId, rContent, ReplyTopicActivity.this.toReplyId, ReplyTopicActivity.this.isQuote, 0,0,"", 1, ReplyTopicActivity.this.getAid(), ReplyTopicActivity.this.isCheckedPermissionModel), "reply");
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            ReplyTopicActivity.this.publishIng = false;
            if (result == null) {
                return;
            }
            if (result.getRs() == 1) {
                ReplyTopicActivity.this.sharedPreferencesDB.setRefresh(true);
                ReplyModel model = new ReplyModel();
                model.setReplyName(ReplyTopicActivity.this.sharedPreferencesDB.getNickName());
                model.setIcon(ReplyTopicActivity.this.sharedPreferencesDB.getIcon());
                model.setReplyUserId(ReplyTopicActivity.this.sharedPreferencesDB.getUserId());
                model.setReplyType(PostsConstant.TOPIC_TYPE_NORMAL);
                model.setPostsDate(System.currentTimeMillis());
                model.setPosition(-1);
                model.setLocation("");
                model.setReplyContent(ReplyTopicActivity.this.postsService.createContentList(ReplyTopicActivity.this.contentEdText.getText().toString(), "ß", "á", ReplyTopicActivity.this.mentionedFriends, ReplyTopicActivity.this.audioPath, null));
                if (ReplyTopicActivity.getReplyRetrunDelegate() != null) {
                    ReplyTopicActivity.replyRetrunDelegate.replyReturn();
                }
                if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                    MCToastUtils.toastByResName(ReplyTopicActivity.this.getApplicationContext(), "mc_forum_reply_succ", 1);
                } else {
                    MCToastUtils.toast(ReplyTopicActivity.this.getApplicationContext(), result.getErrorInfo(), 0);
                }
                ReplyTopicActivity.this.picMap.clear();
                ReplyTopicActivity.this.deleteDraft(ReplyTopicActivity.this.topicDraftModel);
                ReplyTopicActivity.this.finish();
            } else if (!result.getErrorInfo().equals("")) {
                MCToastUtils.toast(ReplyTopicActivity.this.getApplicationContext(), result.getErrorInfo(), 0);
            }
        }
    }

    public interface ReplyRetrunDelegate {
        void replyReturn();
    }

    protected void initDatas() {
        this.draftId = 3;
        super.initDatas();
        this.topicId = getIntent().getLongExtra("topicId", 0);
        this.isQuote = getIntent().getBooleanExtra(IntentConstant.INTENT_IS_QUITE, false);
        this.toReplyId = getIntent().getLongExtra("toReplyId", 0);
        this.userInfoModels = (ArrayList) getIntent().getSerializableExtra(FinalConstant.POSTS_USER_LIST);
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(getApplicationContext());
        this.photoManageHelper.setMaxSelectNum(this.maxUploadPhotoNum);
    }

    protected void initViews() {
        super.initViews();
        this.titleEdText.setVisibility(8);
//        this.topBarTitleText.setText(this.resource.getString("mc_forum_warn_photo_reply_topic"));
        mToolbar.setCenteredTitle(resource.getString("mc_forum_warn_photo_reply_topic"));
        findViewByName("mc_forum_title_img").setVisibility(8);
        findViewByName("mc_forum_top_line_img").setVisibility(8);
        findViewByName("mc_forum_content_top_box").setVisibility(8);
        mToolbar.setTitle(this.resource.getString("mc_forum_warn_photo_reply_topic"));
    }

    @Override
    protected String getLayoutName() {
        return super.getLayoutName();
    }

    protected boolean publicTopic() {
        this.content = this.contentEdText.getText().toString();
        if (MCStringUtil.isEmpty(this.content) && !this.hasAudio) {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_length_error", 0);
            this.publishIng = false;
            return false;
        } else if (this.content.length() > 7000) {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_max_length_error", 0);
            this.publishIng = false;
            return false;
        } else {
            this.publishIng = true;
            this.aid = getAid();
            finish();
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_reply", 0);
            if (this.picMap.size() > 0) {
                uploadPic();
            } else if (this.hasAudio) {
                uploadAudio();
            } else {
                uploadAudioSucc();
            }
            return true;
        }
    }

    protected void uploadAudioSucc() {
        String contentStr = this.postsService.createPublishTopicJson(this.content, "ß", "á", this.audioPath, this.uploadList);
        if (this.replyAsyncTask != null) {
            this.replyAsyncTask.cancel(true);
        }
        this.replyAsyncTask = new ReplyAsyncTask();
        this.replyAsyncTask.execute(new Object[]{contentStr});
    }

    public static ReplyRetrunDelegate getReplyRetrunDelegate() {
        return replyRetrunDelegate;
    }

    public static void setReplyRetrunDelegate(ReplyRetrunDelegate replyRetrunDelegate) {
        replyRetrunDelegate = replyRetrunDelegate;
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
