package com.mobcent.discuz.module.draft.fragment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.internal.view.SupportMenu;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.db.TopicDraftDBUtil;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.article.fragment.activity.ArticleDetailActivity;
import com.mobcent.discuz.module.publish.fragment.activity.PublishTopicActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCDateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DraftFragment extends BaseFragment implements Observer {
    private DraftAdapter adapter;
    private List<TopicDraftModel> draftModels = new ArrayList();
    private PullToRefreshListView list;

    final class Holder {
        public TextView dateTv;
        public TextView tv;

        Holder() {
        }
    }

    private class DraftAdapter extends BaseListAdatper<TopicDraftModel, Holder> {
        public DraftAdapter(Context context, List<TopicDraftModel> datas) {
            super(context, datas);
        }

        protected void initViews(View convertView, Holder holder) {
            holder.tv = (TextView) findViewByName(convertView, "content");
            holder.dateTv = (TextView) findViewByName(convertView, "date_text");
        }

        protected void initViewDatas(Holder holder, TopicDraftModel data, final int position) {
            String baseStr = this.resource.getString("mc_forum_draft_flag");
            String desc = TextUtils.isEmpty(data.getTitle()) ? TextUtils.isEmpty(data.getContent()) ? data.getBoardName() : data.getContent() : data.getTitle();
            if (TextUtils.isEmpty(desc)) {
                desc = "";
            }
            SpannableString ss = new SpannableString(baseStr + "  " + desc);
            ss.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), 0, baseStr.length(), 33);
            holder.tv.setText(ss);
            holder.tv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DraftFragment.this.onItemClick((TopicDraftModel) DraftAdapter.this.getItem(position));
                }
            });
            holder.tv.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    DraftFragment.this.onItemLongClick((TopicDraftModel) DraftAdapter.this.getItem(position));
                    return false;
                }
            });
            holder.dateTv.setText(MCDateUtil.getFormatTimeByWord(this.resource, data.getSaveTime(), "yyyy-MM-dd HH:mm"));
        }

        protected String getLayoutName() {
            return "draft_fragment_item";
        }

        protected Holder instanceHolder() {
            return new Holder();
        }
    }

    protected String getRootLayoutName() {
        return "draft_fragment";
    }

    protected void initViews(View rootView) {
        this.list = (PullToRefreshListView) findViewByName(rootView, "list_layout");
        this.adapter = new DraftAdapter(this.activity, this.draftModels);
        this.list.setAdapter(this.adapter);
    }

    protected void initActions(View rootView) {
        this.list.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                DraftFragment.this.notifyList();
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.list.onRefresh(false);
        TopicDraftDBUtil.getInstance(getAppApplication()).register(this);
    }

    public void onPause() {
        super.onPause();
        TopicDraftDBUtil.getInstance(getAppApplication()).unRegister(this);
    }

    protected void componentDealTopbar() {
        TopSettingModel topModel = createTopSettingModel();
        topModel.title = this.resource.getString("mc_forum_draft_box");
        List<TopBtnModel> topBtns = new ArrayList();
        TopBtnModel btn = new TopBtnModel();
        btn.action = 1;
        btn.title = this.resource.getString("mc_forum_draft_clear");
        topBtns.add(btn);
        topModel.rightModels = topBtns;
        dealTopBar(topModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if ((v.getTag() instanceof TopBtnModel) && ((TopBtnModel) v.getTag()).action == 1) {
                    new Builder(DraftFragment.this.activity).setMessage(DraftFragment.this.resource.getStringId("mc_forum_draft_clear_msg")).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            TopicDraftModel draftModel = new TopicDraftModel();
                            draftModel.setType(1);
                            TopicDraftDBUtil.getInstance(DraftFragment.this.activity).deleteDraftModel(draftModel);
                            DraftFragment.this.notifyList();
                        }
                    }).setNegativeButton(17039360, null).create().show();
                }
            }
        });
    }

    private void notifyList() {
        this.list.onRefreshComplete();
        this.list.onBottomRefreshComplete(6);
        this.draftModels.clear();
        TopicDraftModel draftModel = new TopicDraftModel();
        draftModel.setType(1);
        this.draftModels.addAll(TopicDraftDBUtil.getInstance(this.activity).queryDraftModel(draftModel));
        this.adapter.notifyDataSetChanged();
    }

    public void onItemClick(TopicDraftModel draftModel) {
        if (draftModel != null) {
            Class<?> cls = null;
            if (draftModel.getType() == 1) {
                cls = PublishTopicActivity.class;
            } else if (draftModel.getType() == 3) {
                cls = ArticleDetailActivity.class;
            } else if (draftModel.getType() == 2) {
                cls = TopicDetailActivity.class;
            }
            if (cls != null) {
                Intent intent = new Intent(this.activity, cls);
                intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, draftModel);
                ActivityDispatchHelper.startActivity(this.activity, intent);
            }
        }
    }

    public void onItemLongClick(final TopicDraftModel draftModel) {
        new Builder(this.activity).setMessage(this.resource.getStringId("mc_forum_draft_delete_alert")).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TopicDraftDBUtil.getInstance(DraftFragment.this.activity).deleteDraftModel(draftModel);
                DraftFragment.this.notifyList();
            }
        }).setNegativeButton(17039360, null).create().show();
    }

    public void update(Observable observable, Object data) {
        if (this.activity != null) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    DraftFragment.this.notifyList();
                }
            });
        }
    }
}
