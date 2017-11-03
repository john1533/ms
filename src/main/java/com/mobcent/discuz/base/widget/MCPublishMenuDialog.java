package com.mobcent.discuz.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.DraftHelper;
import com.mobcent.discuz.base.helper.DraftHelper.DraftDelegate;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.publish.fragment.activity.ClassifyTopicActivity;
import com.mobcent.discuz.module.sign.SignInAsyncTask;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.List;

public class MCPublishMenuDialog extends AlertDialog implements FinalConstant, ConfigConstant {
    private LinearLayout bottomBox;
    private Button cancleBtn;
    private List<ConfigComponentModel> componentList;
    private Context context;
    private int count;
    private LayoutInflater inflater;
    private int marginLeft;
    private LinearLayout middleBox;
    private PermissionModel permissionModel;
    private MCResource resource;
    private SettingModel settingModel;
    private LinearLayout topBox;

    public MCPublishMenuDialog(Context context) {
        super(context);
        this.context = context;
        initDate();
    }

    public MCPublishMenuDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initDate();
    }

    private void initDate() {
        this.resource = MCResource.getInstance(this.context);
        this.componentList = new ArrayList();
        this.inflater = LayoutInflater.from(this.context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.resource.getLayoutId("publish_dialog"));
        this.topBox = (LinearLayout) findViewById(this.resource.getViewId("mc_forum_top_box"));
        this.middleBox = (LinearLayout) findViewById(this.resource.getViewId("mc_forum_middle_box"));
        this.bottomBox = (LinearLayout) findViewById(this.resource.getViewId("mc_forum_bottom_box"));
        this.cancleBtn = (Button) findViewById(this.resource.getViewId("mc_forum_cancle_btn"));
        if (this.marginLeft != 0) {
            LayoutParams lps = (LayoutParams) this.cancleBtn.getLayoutParams();
            lps.addRule(9);
            lps.leftMargin = this.marginLeft;
            this.cancleBtn.setLayoutParams(lps);
        }
        initView();
        initActions();
    }

    private void initActions() {

        this.cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCPublishMenuDialog.this.dismiss();
            }
        });
    }

    private void initView() {
        if (MCListUtils.isEmpty(this.componentList)) {
            this.componentList = createComponentList();
        }
        this.count = this.componentList.size();
        int j = this.count % 3;
        for (int i = 0; i < this.count; i++) {
            if (j == 0) {
                compute(i);
            } else if (j == 1 && this.count == 4) {
                if (i < 2) {
                    this.middleBox.addView(createPublishItem((ConfigComponentModel) this.componentList.get(i)));
                } else {
                    this.bottomBox.addView(createPublishItem((ConfigComponentModel) this.componentList.get(i)));
                }
            } else if (j == 1) {
                compute(i);
            } else {
                compute(i);
            }
            if (this.count == 7) {
                LayoutParams layoutParams = (LayoutParams) this.bottomBox.getLayoutParams();
                layoutParams.addRule(9, -1);
                this.bottomBox.setLayoutParams(layoutParams);
            }
        }
    }

    private void compute(int i) {
        if (i < 3) {
            this.topBox.addView(createPublishItem((ConfigComponentModel) this.componentList.get(i)));
        } else if (i < 6) {
            this.middleBox.addView(createPublishItem((ConfigComponentModel) this.componentList.get(i)));
        } else if (i < 9) {
            this.bottomBox.addView(createPublishItem((ConfigComponentModel) this.componentList.get(i)));
        }
    }

    private List<ConfigComponentModel> createComponentList() {
        List<ConfigComponentModel> componentModels = new ArrayList();
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_publish_text"), "mc_forum_ico27", ConfigConstant.COMPONENT_FASTPOST));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_pic_topic_list"), "mc_forum_ico28", ConfigConstant.COMPONENT_FASTIMAGE));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_take_photo"), "mc_forum_ico29", ConfigConstant.COMPONENT_FASTCAMERA));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_posts_voice"), "mc_forum_ico45", ConfigConstant.COMPONENT_FASTAUDIO));
        return componentModels;
    }

    private ConfigComponentModel createConfigComponentModel(String title, String icon, String type) {
        ConfigComponentModel componentModel = new ConfigComponentModel();
        componentModel.setTitle(title);
        componentModel.setIcon(icon);
        componentModel.setType(type);
        return componentModel;
    }

    private View createPublishItem(final ConfigComponentModel componentModel) {
        View view = this.inflater.inflate(this.resource.getLayoutId("publish_item"), null);
        ImageView button = (ImageView) view.findViewById(this.resource.getViewId("mc_forum_publish_btn"));
        TextView textView = (TextView) view.findViewById(this.resource.getViewId("mc_forum_publish_text"));
        DZImageLoadUtils.loadImage(button, componentModel.getIcon());
        if (componentModel.getTitle().length() > 5) {
            textView.setText(componentModel.getTitle().substring(0, 5));
        } else {
            textView.setText(componentModel.getTitle());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginHelper.doInterceptor(MCPublishMenuDialog.this.context, null, null) && !DraftHelper.isNeedAlertDialog(MCPublishMenuDialog.this.context, 1, new DraftDelegate() {
                    public void onDraftAlertBack(TopicDraftModel model) {
                        MCPublishMenuDialog.this.onClick(componentModel, model);
                    }
                })) {
                    MCPublishMenuDialog.this.onClick(componentModel, null);
                }
                MCPublishMenuDialog.this.dismiss();
            }
        });


        return view;
    }

    public void onClick(ConfigComponentModel componentModel, TopicDraftModel topicDraftModel) {
        Intent intent;
        intent = new Intent(this.context, ClassifyTopicActivity.class);
        intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
        intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, topicDraftModel);
        this.context.startActivity(intent);
//        if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTPOST)) {
//            intent = new Intent(this.context, ClassifyTopicActivity.class);
//            intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
//            intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, topicDraftModel);
//            this.context.startActivity(intent);
//        } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTIMAGE)) {
//            intent = new Intent(this.context, ClassifyTopicActivity.class);
//            intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 1);
//            intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
//            intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, topicDraftModel);
//            this.context.startActivity(intent);
//        } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTCAMERA)) {
//            intent = new Intent(this.context, ClassifyTopicActivity.class);
//            intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 2);
//            intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
//            intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, topicDraftModel);
//            this.context.startActivity(intent);
//        } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_SIGN)) {
//            if (this.settingModel == null || this.settingModel.getPlugCheck() != 1) {
//                MCToastUtils.toast(this.context, this.resource.getString("mc_forum_error_sign_none"), 1);
//            } else if (LoginHelper.doInterceptor(this.context, null, null)) {
//                new SignInAsyncTask(this.context, null).execute(new Void[0]);
//            }
//        } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTAUDIO)) {
//            intent = new Intent(this.context, ClassifyTopicActivity.class);
//            intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 3);
//            intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
//            intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, topicDraftModel);
//            this.context.startActivity(intent);
//        }
    }

    public void show() {
        super.show();
    }

    public void show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        setMarginLeft(location[0]);
        Window win = getWindow();
        int displayHeight = MCPhoneUtil.getDisplayHeight(getContext());
        WindowManager.LayoutParams params = win.getAttributes();
        params.height = displayHeight;
        params.width = MCPhoneUtil.getDisplayWidth(getContext());
        win.addFlags(1024);
        win.setAttributes(params);
        show();
    }

    public void dismiss() {
        super.dismiss();
    }

    protected void onStop() {
        super.onStop();
    }

    public PermissionModel getPermissionModel() {
        return this.permissionModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public void setConfigComponenList(List<ConfigComponentModel> list) {
        this.componentList = list;
    }

    public SettingModel getSettingModel() {
        return this.settingModel;
    }

    public void setSettingModel(SettingModel settingModel) {
        this.settingModel = settingModel;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }
}
