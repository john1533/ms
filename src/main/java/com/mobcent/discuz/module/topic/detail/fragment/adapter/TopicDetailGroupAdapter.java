package com.mobcent.discuz.module.topic.detail.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCPollSelectBar;
import com.mobcent.discuz.activity.view.MCPollSelectBar.SelectPollDelegate;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.ActivityModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.OtherPanelModel;
import com.mobcent.discuz.android.model.PollItemModel;
import com.mobcent.discuz.android.model.PollModel;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.person.activity.UserLoginActivity;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsGroupHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsLastHolder;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TopicDetailGroupAdapter extends TopicDetailCommonAdapter {
    protected List<PollItemModel> pollItemList = new ArrayList();
    protected PollModel pollModel;
    protected UserPollTask pollTask;
    protected int pollType;
    protected List<MCPollSelectBar> pollViewList = new ArrayList();
    protected List<MCPollSelectBar> selectedPollViewList = new ArrayList();

    public class UserPollTask extends AsyncTask<Void, Void, BaseResultModel<List<PollItemModel>>> {
        private Context context;
        private BasePostsLastHolder holder;
        private String itemId;
        private long topicId;

        public UserPollTask(Context context, long topicId, String itemId, BasePostsLastHolder holder) {
            this.context = context;
            this.topicId = topicId;
            this.itemId = itemId;
            this.holder = holder;
        }

        protected void onPreExecute() {
            this.holder.getPollSubmitBtn().setEnabled(false);
            DZProgressDialogUtils.showProgressDialog(this.context, "mc_forum_please_wait", this);
        }

        protected BaseResultModel<List<PollItemModel>> doInBackground(Void... params) {
            return TopicDetailGroupAdapter.this.postsService.getUserPolls(TopicDetailGroupAdapter.this.boardId, this.topicId, this.itemId);
        }

        protected void onPostExecute(BaseResultModel<List<PollItemModel>> result) {
            DZProgressDialogUtils.hideProgressDialog();
            this.holder.getPollSubmitBtn().setEnabled(true);
            DZToastAlertUtils.toast(this.context, result);
            if (result.getRs() != 0) {
                this.holder.getPollSubmitBtn().setVisibility(8);
                TopicDetailGroupAdapter.this.pollItemList = (List) result.getData();
                if (TopicDetailGroupAdapter.this.pollModel != null) {
                    TopicDetailGroupAdapter.this.pollModel.setPollItemList(TopicDetailGroupAdapter.this.pollItemList);
                }
                TopicDetailGroupAdapter.this.updatePollResultView(TopicDetailGroupAdapter.this.pollItemList);
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(this.context.getApplicationContext(), "mc_forum_error_poll_fail");
            } else {
                MCToastUtils.toast(this.context.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    public TopicDetailGroupAdapter(Context context, List<Object> detailList) {
        super(context, detailList);
    }

    protected void updatePostsPollView(final PostsModel postsModel, final BasePostsLastHolder holder) {
        if (postsModel.getPollInfo() == null) {
            holder.getPollLayout().setVisibility(8);
            return;
        }
        holder.getPollLayout().setVisibility(0);
        this.pollModel = postsModel.getPollInfo();
        if (this.pollModel != null) {
            List<PollItemModel> pollItemList = this.pollModel.getPollItemList();
            if (pollItemList != null && !pollItemList.isEmpty()) {
                this.pollType = this.pollModel.getType();
                if (this.pollType == 0) {
                    this.pollType = pollItemList.size();
                }
                updatePollSelectView(pollItemList, holder, this.pollModel.getPollId(), this.pollType);
                if (this.pollModel.getPollStatus() == 1) {
                    holder.getPollSubmitBtn().setVisibility(8);
                    if (this.pollModel.getIsVisible() == 0) {
                        holder.getPollLayout().setVisibility(0);
                        holder.getPollTitleText().setText(this.resource.getString("mc_forum_poll_result_invisiable"));
                        holder.getPollSelectLayout().setVisibility(8);
                    } else {
                        updatePollResultView(pollItemList);
                    }
                } else {
                    holder.getPollSubmitBtn().setVisibility(0);
                }
                holder.getPollSubmitBtn().setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        if (!LoginHelper.doInterceptor(TopicDetailGroupAdapter.this.context, null, null)) {
                            return;
                        }
                        if (TopicDetailGroupAdapter.this.pollModel.getPollStatus() == 2) {
                            TopicDetailGroupAdapter.this.publishPoll(postsModel, holder);
                        } else if (TopicDetailGroupAdapter.this.pollModel.getPollStatus() == 3) {
                            MCToastUtils.toastByResName(TopicDetailGroupAdapter.this.context.getApplicationContext(), "mc_forum_poll_no_perm");
                        } else if (TopicDetailGroupAdapter.this.pollModel.getPollStatus() == 4) {
                            MCToastUtils.toastByResName(TopicDetailGroupAdapter.this.context.getApplicationContext(), "mc_forum_poll_close");
                        }
                    }
                });
            }
        }
    }

    private void updatePollSelectView(List<PollItemModel> pollItemList, BasePostsLastHolder holder, List<Long> pollId, int poolType) {
        holder.getPollLayout().setVisibility(0);
        LinearLayout pollSelectLayout = holder.getPollSelectLayout();
        holder.getPollTitleText().setText(MCStringBundleUtil.resolveString(this.resource.getStringId("mc_froum_radio_poll"), poolType + "", this.context.getApplicationContext()));
        pollSelectLayout.setVisibility(0);
        pollSelectLayout.removeAllViews();
        this.selectedPollViewList.clear();
        this.pollViewList.clear();
        int j = pollItemList.size();
        for (int i = 0; i < j; i++) {
            PollItemModel pollItem = (PollItemModel) pollItemList.get(i);
            MCPollSelectBar pollSelectBar = new MCPollSelectBar(this.inflater, this.resource, pollItem, new SelectPollDelegate() {
                public void onItemSelect(View view, MCPollSelectBar selectBar, boolean select) {
                    if (select) {
                        if (!TopicDetailGroupAdapter.this.selectedPollViewList.contains(selectBar)) {
                            TopicDetailGroupAdapter.this.selectedPollViewList.add(selectBar);
                        }
                    } else if (TopicDetailGroupAdapter.this.selectedPollViewList.contains(selectBar)) {
                        TopicDetailGroupAdapter.this.selectedPollViewList.remove(selectBar);
                    }
                }
            }, this.context);
            this.pollViewList.add(pollSelectBar);
            if (!(pollId == null || pollId.isEmpty())) {
                int k = 0;
                while (k < pollId.size()) {
                    if (((Long) pollId.get(k)).longValue() == pollItem.getPollItemId()) {
                        pollSelectBar.setSelect(false);
                        if (!this.selectedPollViewList.contains(pollSelectBar)) {
                            this.selectedPollViewList.add(pollSelectBar);
                        }
                    } else {
                        k++;
                    }
                }
            }
            pollSelectLayout.addView(pollSelectBar.getView());
        }
    }

    private void updatePollResultView(List<PollItemModel> pollItemList) {
        if (pollItemList != null && pollItemList.size() > 0) {
            int j = pollItemList.size();
            for (int i = 0; i < j; i++) {
                ((MCPollSelectBar) this.pollViewList.get(i)).updateResultView((PollItemModel) pollItemList.get(i));
            }
        }
    }

    private void publishPoll(PostsModel postsModel, BasePostsLastHolder holder) {
        int size = this.selectedPollViewList.size();
        if (size == 0) {
            MCToastUtils.toast(this.context.getApplicationContext(), this.resource.getString("mc_forum_poll_select"));
        } else if (size > this.pollType) {
            MCToastUtils.toast(this.context.getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_poll_single"), this.pollType + "", this.context));
        } else {
            String itemId = "";
            for (MCPollSelectBar poll : this.selectedPollViewList) {
                if ("".equals(itemId)) {
                    itemId = poll.getPollItem().getPollItemId() + "";
                } else {
                    itemId = itemId + AdApiConstant.RES_SPLIT_COMMA + poll.getPollItem().getPollItemId();
                }
            }
            this.pollTask = new UserPollTask(this.context, postsModel.getTopicId(), itemId, holder);
            this.pollTask.execute(new Void[0]);
        }
    }

    protected void updatePostsActivityView(final ActivityModel activityModel, BasePostsGroupHolder holder, final long topicId) {
        holder.getActivityTopicBox().setVisibility(0);
        holder.getActivityImage().setTag(Boolean.valueOf(true));
        updateImageView(MCAsyncTaskLoaderImage.formatUrl(activityModel.getImage(), FinalConstant.RESOLUTION_SMALL), holder.getActivityImage(), true);
        holder.getActivitySummaryText().setText(activityModel.getSummary());
        MCLogUtil.e(this.TAG, "activityModel.getTitle()=" + activityModel.getTitle());
        holder.getActivitySubmitBtn().setText(activityModel.getTitle());
        if (MCStringUtil.isEmpty(activityModel.getType()) || !activityModel.getType().equals(FinalConstant.CANCEL) || MCStringUtil.isEmpty(activityModel.getDescription())) {
            holder.getActivityDescText().setVisibility(8);
        } else {
            holder.getActivityDescText().setVisibility(0);
            holder.getActivityDescText().setText(activityModel.getDescription());
        }
        holder.getActivityApplyBox().removeAllViews();
        if (activityModel.getApplyList() != null) {
            holder.getActivityApplyBox().addView(createActivityTextView(activityModel.getApplyList().getTitle()));
            holder.getActivityApplyBox().addView(createActivityTextView(activityModel.getApplyList().getSummary()));
        }
        if (activityModel.getApplyListVerified() != null) {
            holder.getActivityApplyBox().addView(createActivityTextView(activityModel.getApplyListVerified().getTitle()));
            holder.getActivityApplyBox().addView(createActivityTextView(activityModel.getApplyListVerified().getSummary()));
        }
        if (!MCStringUtil.isEmpty(activityModel.getType()) && !activityModel.getType().equals(FinalConstant.NONE)) {
            holder.getActivitySubmitBtn().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (activityModel.getType().equals(FinalConstant.TIPS)) {
                        MCToastUtils.toast(TopicDetailGroupAdapter.this.context.getApplicationContext(), activityModel.getDescription());
                    } else if (activityModel.getType().equals("login")) {
                        TopicDetailGroupAdapter.this.context.startActivity(new Intent(TopicDetailGroupAdapter.this.context, UserLoginActivity.class));
                    } else {
                        String urlString = TopicDetailGroupAdapter.this.resource.getString("mc_discuz_base_request_url") + TopicDetailGroupAdapter.this.resource.getString("mc_forum_topic_activity") + "&accessToken=" + TopicDetailGroupAdapter.this.sDb.getAccessToken() + "&accessSecret=" + TopicDetailGroupAdapter.this.sDb.getAccessSecret() + "&sdkVersion=" + BaseApiConstant.SDK_VERSION_VALUE + "&tid=" + topicId + "&act=" + activityModel.getType() + "&apphash=" + MCStringUtil.stringToMD5((System.currentTimeMillis() + "").substring(0, 5) + BaseApiConstant.AUTHKEY).substring(8, 16);
                        Intent intent = new Intent(TopicDetailGroupAdapter.this.context, WebViewFragmentActivity.class);
                        intent.putExtra("webViewUrl", urlString);
                        TopicDetailGroupAdapter.this.context.startActivity(intent);
                    }
                }
            });
        }
    }

    private TextView createActivityTextView(String content) {
        TextView textView = new TextView(this.context);
        textView.setText(content);
        textView.setTextSize(16.0f);
        textView.setTextColor(this.resource.getColorId("mc_forum_text_apparent_color"));
        return textView;
    }

    protected void initRateView(final BasePostsLastHolder holder) {
        boolean isExistRate = false;
        OtherPanelModel extraPanelModel = null;
        if (this.postsModel == null) {
            this.postsModel = new PostsModel();
        }
        Map<String, OtherPanelModel> extraPanelModels = this.postsModel.getExtraPanel();
        if (extraPanelModels != null) {
            for (String key : extraPanelModels.keySet()) {
                if (FinalConstant.RATE.equals(key)) {
                    extraPanelModel = (OtherPanelModel) extraPanelModels.get(key);
                    if (extraPanelModel != null) {
                        isExistRate = true;
                    }
                }
            }
        }
        Map<String, List<String>> rateMap = this.postsModel.getRateList();
        if (!(rateMap == null || rateMap.isEmpty())) {
            isExistRate = true;
        }
        if (isExistRate) {
            if (extraPanelModel == null) {
                holder.getRateBtn().setVisibility(8);
            } else {
                holder.getRateBtn().setVisibility(0);
                final OtherPanelModel model = extraPanelModel;
                holder.getRateBtn().setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(TopicDetailGroupAdapter.this.context, WebViewFragmentActivity.class);
                        intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TITLE, model.getTitle());
                        intent.putExtra("webViewUrl", model.getAction());
                        intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TYPE, FinalConstant.RATE);
                        TopicDetailGroupAdapter.this.context.startActivity(intent);
                    }
                });
            }
            if (rateMap == null || rateMap.isEmpty()) {
                holder.getRateMoreBox().setVisibility(8);
                holder.getRateDetailBox().setVisibility(8);
                holder.getRateNotMoreBox().setVisibility(0);
                holder.getRateNotLine().setVisibility(0);
            } else {
                holder.getRateNotLine().setVisibility(8);
                holder.getRateNotMoreBox().setVisibility(8);
                holder.getRateDetailBox().setVisibility(0);
                holder.getRateMoreBox().setVisibility(0);
                LinearLayout rateDetailBox = holder.getRateDetailBox();
                rateDetailBox.removeAllViews();
                for (String key2 : rateMap.keySet()) {
                    LinearLayout layout = createRateLayout();
                    rateDetailBox.addView(layout);
                    List<String> list = (List) rateMap.get(key2);
                    if ("head".equals(key2)) {
                        createRateView(layout, list, "mc_forum_text4_normal_color", "mc_forum_text4_normal_color");
                    } else if ("total".equals(key2)) {
                        createRateView(layout, list, "mc_forum_text6_normal_color", "mc_forum_text6_normal_color");
                    } else if ("body0".equals(key2)) {
                        createRateView(layout, list, "mc_forum_text4_desc_normal_color", "mc_forum_rate_text1_color");
                    } else if ("body1".equals(key2)) {
                        createRateView(layout, list, "mc_forum_text4_desc_normal_color", "mc_forum_rate_text1_color");
                    } else if ("body2".equals(key2)) {
                        createRateView(layout, list, "mc_forum_text4_desc_normal_color", "mc_forum_rate_text1_color");
                    }
                }
                if (rateMap.size() == 3) {
                    createRateLineView(rateDetailBox, 2);
                } else if (rateMap.size() == 4) {
                    createRateLineView(rateDetailBox, 2);
                    createRateLineView(rateDetailBox, 4);
                } else if (rateMap.size() == 5) {
                    createRateLineView(rateDetailBox, 2);
                    createRateLineView(rateDetailBox, 4);
                    createRateLineView(rateDetailBox, 6);
                }
            }
            holder.getRateMoreText().setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent(TopicDetailGroupAdapter.this.context, WebViewFragmentActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TITLE, holder.getRateMoreText().getText());
                    intent.putExtra("webViewUrl", TopicDetailGroupAdapter.this.postsModel.getRateShowAllUrl());
                    TopicDetailGroupAdapter.this.context.startActivity(intent);
                }
            });
            return;
        }
        holder.getRateBox().setVisibility(8);
    }

    private LinearLayout createRateLayout() {
        LinearLayout layout = new LinearLayout(this.context);
        LayoutParams params = new LayoutParams(-1, -2);
        params.topMargin = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 5.0f);
        layout.setLayoutParams(params);
        layout.setOrientation(0);
        return layout;
    }

    private void createRateView(LinearLayout layout, List<String> list, String colorId1, String colorId2) {
        int itemWidth = getRateItemWidth(list.size());
        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(this.context);
            LayoutParams params = new LayoutParams(itemWidth, -2);
            if (i == 0) {
                params.leftMargin = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 8.0f);
            } else if (i == list.size() - 1) {
                params.rightMargin = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 8.0f);
            }
            textView.setLayoutParams(params);
            if (i == 0) {
                textView.setTextColor(this.context.getApplicationContext().getResources().getColorStateList(this.resource.getColorId(colorId1)));
            } else {
                textView.setTextColor(this.context.getApplicationContext().getResources().getColorStateList(this.resource.getColorId(colorId2)));
            }
            textView.setSingleLine(true);
            textView.setText((CharSequence) list.get(i));
            textView.setEllipsize(TruncateAt.END);
            textView.setTextSize(14.0f);
            layout.addView(textView);
        }
    }

    private int getRateItemWidth(int size) {
        return (MCPhoneUtil.getDisplayWidth(this.context.getApplicationContext()) - MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 16.0f)) / size;
    }

    private void createRateLineView(LinearLayout layout, int index) {
        View view = new View(this.context);
        LayoutParams params = new LayoutParams(-1, 1);
        params.topMargin = MCPhoneUtil.getRawSize(this.context.getApplicationContext(), 1, 5.0f);
        view.setLayoutParams(params);
        view.setBackgroundResource(this.resource.getDrawableId("mc_forum_rate"));
        layout.addView(view, index);
    }
}
