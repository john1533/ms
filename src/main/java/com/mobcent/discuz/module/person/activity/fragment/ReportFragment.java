package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.ReportService;
import com.mobcent.discuz.android.service.impl.ReportServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCStringUtil;

public class ReportFragment extends BaseFragment {
    private TextView adultContentText;
    private boolean isShowTopicTypeBox = false;
    private Long oid;
    private TextView otherReasonText;
    private TextView politySensContentText;
    private String reason;
    private ReportAsyncTask reportAsyncTask;
    private ScrollView reportDetailBox;
    private EditText reportDetailDescriptionEdit;
    private EditText reportReasonEdit;
    private ImageButton reportReasonImgBtn;
    private String reportReasonStr;
    private ReportService reportService;
    private Button reportSubmitBtn;
    private String reportTitleText;
    private String sureReportStr = "";
    private RelativeLayout transparentBox;
    private int type;
    private TextView vulgarSpeechText;

    private class ReportAsyncTask extends AsyncTask<Object, Void, BaseResultModel> {
        String reportType;

        private ReportAsyncTask() {
        }

        protected BaseResultModel doInBackground(Object... params) {
            ReportFragment.this.type = ((Integer) params[0]).intValue();
            ReportFragment.this.oid = (Long) params[1];
            ReportFragment.this.reason = (String) params[2];
            if (ReportFragment.this.type == 1) {
                this.reportType = FinalConstant.REPORT_TOPIC;
            } else if (ReportFragment.this.type == 2) {
                this.reportType = "post";
            } else {
                this.reportType = "user";
            }
            return ReportFragment.this.reportService.report(ReportFragment.this.oid.longValue(), ReportFragment.this.reason, this.reportType);
        }

        protected void onPostExecute(BaseResultModel result) {
            if (result != null) {
                DZToastAlertUtils.toast(ReportFragment.this.activity, result);
            }
            ReportFragment.this.activity.finish();
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.type = getBundle().getInt(IntentConstant.REPORT_TYPE);
        this.oid = Long.valueOf(getBundle().getLong(IntentConstant.REPOR_OBJECT_ID));
        this.reportService = new ReportServiceImpl(this.activity);
    }

    protected String getRootLayoutName() {
        return "report_fragment";
    }

    protected void initViews(View rootView) {
        this.reportDetailBox = (ScrollView) findViewByName(rootView, "mc_forum_report_detail_box");
        this.adultContentText = (TextView) findViewByName(rootView, "mc_forum_report_adult_content");
        this.politySensContentText = (TextView) findViewByName(rootView, "mc_forum_polity_sensi_content");
        this.vulgarSpeechText = (TextView) findViewByName(rootView, "mc_forum_vulgar_speech");
        this.otherReasonText = (TextView) findViewByName(rootView, "mc_forum_other_reason");
        this.reportReasonEdit = (EditText) findViewByName(rootView, "mc_forum_report_reason_edit");
        this.reportDetailDescriptionEdit = (EditText) findViewByName(rootView, "mc_forum_report_detail_description_edit");
        this.reportReasonImgBtn = (ImageButton) findViewByName(rootView, "mc_forum_report_reason_imgBtn");
        this.transparentBox = (RelativeLayout) findViewByName(rootView, "mc_forum_transparent_box");
        this.reportSubmitBtn = (Button) findViewByName(rootView, "mc_forum_report_submit_btn");
        if (this.type != 3) {
            this.reportTitleText = getString(this.resource.getStringId("mc_forum_report_post_str"));
            this.sureReportStr = getString(this.resource.getStringId("mc_forum_sure_report_posts_str"));
            return;
        }
        this.reportTitleText = getString(this.resource.getStringId("mc_forum_report_user_str"));
        this.sureReportStr = getString(this.resource.getStringId("mc_forum_sure_report_user_str"));
    }

    protected void initActions(View rootView) {
        this.reportReasonImgBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (ReportFragment.this.isShowTopicTypeBox) {
                    ReportFragment.this.showdropDownBox(false);
                } else {
                    ReportFragment.this.showdropDownBox(true);
                }
            }
        });
        this.adultContentText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReportFragment.this.reportReasonEdit.setText(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_report_adult_content")));
                ReportFragment.this.showdropDownBox(false);
            }
        });
        this.politySensContentText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReportFragment.this.reportReasonEdit.setText(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_polity_sensi_content")));
                ReportFragment.this.showdropDownBox(false);
            }
        });
        this.vulgarSpeechText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReportFragment.this.reportReasonEdit.setText(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_vulgar_speech")));
                ReportFragment.this.showdropDownBox(false);
            }
        });
        this.otherReasonText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReportFragment.this.reportReasonEdit.setText(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_other_reason")));
                ReportFragment.this.showdropDownBox(false);
            }
        });
        this.transparentBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (ReportFragment.this.transparentBox.getVisibility() == 0) {
                    ReportFragment.this.showdropDownBox(false);
                }
                return false;
            }
        });
        this.reportSubmitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReportFragment.this.reportReasonStr = ReportFragment.this.reportReasonEdit.getText().toString().trim();
                ReportFragment.this.reason = "[" + ReportFragment.this.reportReasonStr + "]" + ReportFragment.this.reportDetailDescriptionEdit.getText().toString().trim();
                if (MCStringUtil.isEmpty(ReportFragment.this.reportReasonStr)) {
                    Toast.makeText(ReportFragment.this.activity, ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_select_report_reason_str")), 1).show();
                } else {
                    new Builder(ReportFragment.this.activity).setTitle(ReportFragment.this.sureReportStr).setNegativeButton(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_dialog_cancel")), null).setPositiveButton(ReportFragment.this.getString(ReportFragment.this.resource.getStringId("mc_forum_dialog_confirm")), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (ReportFragment.this.reportAsyncTask != null) {
                                ReportFragment.this.reportAsyncTask.cancel(true);
                            }
                            ReportFragment.this.reportAsyncTask = new ReportAsyncTask();
                            ReportFragment.this.reportAsyncTask.execute(new Object[]{Integer.valueOf(ReportFragment.this.type), ReportFragment.this.oid, ReportFragment.this.reason});
                        }
                    }).show();
                }
            }
        });
    }

    private void showdropDownBox(boolean isShowTopicTypeBox) {
        if (this.isShowTopicTypeBox != isShowTopicTypeBox) {
            this.isShowTopicTypeBox = isShowTopicTypeBox;
            if (isShowTopicTypeBox) {
                this.reportDetailBox.setVisibility(0);
                this.transparentBox.setVisibility(0);
                this.reportReasonImgBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_publish2_h"));
                return;
            }
            this.reportDetailBox.setVisibility(8);
            this.transparentBox.setVisibility(8);
            this.reportReasonImgBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_publish2_n"));
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.reportTitleText;
        dealTopBar(topSettingModel);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.reportAsyncTask != null) {
            this.reportAsyncTask.cancel(true);
        }
    }

    public boolean isChildInteruptBackPress() {
        if (this.reportDetailBox.getVisibility() != 0) {
            return super.isChildInteruptBackPress();
        }
        showdropDownBox(false);
        return true;
    }
}
