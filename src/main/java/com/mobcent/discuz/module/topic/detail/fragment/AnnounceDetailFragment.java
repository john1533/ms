package com.mobcent.discuz.module.topic.detail.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnounceDetailFragment extends BaseFragment {
    private int SHARE = 1;
    private ImageView annoAuthorIconImg;
    private TextView annoAuthorNameText;
    private AnnoModel annoModel;
    private TextView annoStartDateText;
    private TextView annoTitleText;
    private ObtainAnnounceDetailTask announceDetailTask;
    private long announceId;
    private TextView contentText;
    private PostsService postsService;

    private class ObtainAnnounceDetailTask extends AsyncTask<Long, Void, BaseResultModel<AnnoModel>> {
        private ObtainAnnounceDetailTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected BaseResultModel<AnnoModel> doInBackground(Long... params) {
            return AnnounceDetailFragment.this.postsService.getAnnoDetail(params[0].longValue());
        }

        protected void onPostExecute(BaseResultModel<AnnoModel> result) {
            if (result == null) {
                Toast.makeText(AnnounceDetailFragment.this.activity, AnnounceDetailFragment.this.getString(AnnounceDetailFragment.this.resource.getStringId("mc_forum_no_data")), 0).show();
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                AnnounceDetailFragment.this.updateView((AnnoModel) result.getData());
            } else {
                MCToastUtils.toastByResName(AnnounceDetailFragment.this.activity, result.getErrorInfo(), 1);
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.postsService = new PostsServiceImpl(this.activity.getApplicationContext());
        this.announceId = getBundle().getLong(IntentConstant.INTENT_ANNO_ID, 0);
        onRefresh();
    }

    protected String getRootLayoutName() {
        return "announce_detail_fragment";
    }

    protected void initViews(View rootView) {
        this.annoAuthorIconImg = (ImageView) findViewByName(rootView, "mc_forum_announce_author_img");
        this.annoAuthorNameText = (TextView) findViewByName(rootView, "mc_forum_announce_author_text");
        this.annoTitleText = (TextView) findViewByName(rootView, "mc_forum_announce_title_text");
        this.annoStartDateText = (TextView) findViewByName(rootView, "mc_forum_announce_start_date_text");
        this.contentText = (TextView) findViewByName(rootView, "mc_forum_anno_content_text");
    }

    protected void initActions(View rootView) {
    }

    private void share() {
        String shareContent = "";
        if (!(this.annoModel == null || MCStringUtil.isEmpty(this.annoModel.getContent()))) {
            shareContent = this.annoModel.getContent().length() <= 70 ? this.annoModel.getContent() : this.annoModel.getContent().substring(0, 70);
        }
        MCShareModel shareModel = new MCShareModel();
        if (this.annoModel != null) {
            shareModel.setTitle(this.annoModel.getTitle());
        }
        shareModel.setType(3);
        shareModel.setContent(shareContent);
        shareModel.setDownloadUrl(this.resource.getString("mc_share_download_url"));
        HashMap<String, String> params = new HashMap();
        params.put("topicId", String.valueOf(this.announceId));
        params.put(MCShareConstant.PARAM_SHARE_FROM, MCShareConstant.FROM_PHP);
        params.put("baseUrl", this.resource.getString("mc_discuz_base_request_url"));
        params.put("style", String.valueOf(1));
        params.put(MCShareConstant.PARAM_CONTENT_TYPE, String.valueOf(6));
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        shareModel.setParams(params);
        MCForumLaunchShareHelper.share(this.activity, shareModel);
    }

    private void updateView(final AnnoModel annoModel) {
        this.annoAuthorNameText.setText(annoModel.getAuthor());
        this.annoTitleText.setText(annoModel.getTitle());
        if (annoModel.getType().equals(FinalConstant.URL)) {
            SpannableString sp = new SpannableString(annoModel.getContent());
            if (annoModel.getContent() != null && MCStringUtil.isUrl(annoModel.getContent())) {
                sp.setSpan(new URLSpan(annoModel.getContent().toString()), 0, annoModel.getContent().length(), 33);
            }
            this.contentText.setText(sp);
            this.contentText.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent it = new Intent(AnnounceDetailFragment.this.activity.getApplicationContext(), WebViewFragmentActivity.class);
                    it.putExtra("webViewUrl", annoModel.getContent());
                    AnnounceDetailFragment.this.startActivity(it);
                }
            });
        } else {
            this.contentText.setText(annoModel.getContent());
            DZFaceUtil.setStrToFace(this.contentText, annoModel.getContent(), this.activity.getApplicationContext());
        }
        this.annoAuthorIconImg.setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.activity));
        ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(annoModel.getIcon(), FinalConstant.RESOLUTION_SMALL), this.annoAuthorIconImg);
        this.annoStartDateText.setText(annoModel.getAnnoStartTime() + "");
    }

    public void onRefresh() {
        if (this.announceDetailTask != null) {
            this.announceDetailTask.cancel(true);
        }
        this.announceDetailTask = new ObtainAnnounceDetailTask();
        this.announceDetailTask.execute(new Long[]{Long.valueOf(this.announceId)});
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.announceDetailTask != null) {
            this.announceDetailTask.cancel(true);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = this.resource.getString("mc_forum_announce_detail_text");
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.title = this.resource.getString("mc_forum_announce_share");
        topBtnModel.action = this.SHARE;
        rightModels.add(topBtnModel);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == AnnounceDetailFragment.this.SHARE) {
                    AnnounceDetailFragment.this.share();
                }
            }
        });
    }
}
