package com.mobcent.discuz.module.publish.fragment.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ChoicesModle;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.android.service.impl.helper.PostsServiceImplHelper;
import com.mobcent.discuz.base.widget.MultyChoseBuilder;
import com.mobcent.discuz.base.widget.SelectChoseItemBuilder;
import com.mobcent.discuz.base.widget.SingleChoseBuilder;
import com.mobcent.discuz.base.widget.WheelDialog;
import com.mobcent.discuz.base.widget.WheelDialog.WheelDelegate;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.publish.adapter.PublishTopicTypeListAdapter;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassifyTopicActivity extends PublishTopicActivity implements FinalConstant {
    private String[] checkBoxitems = null;
    private String classifiedJson;
    private ClassifiedModel classifiedModel;
    private GetClassifiedModelInfoTask classifiedModelInfoTask;
    private List<ClassifiedModel> classifiedModelList;
    private Map<String, List<ClassifiedModel>> classifiedModelMap;
    private ClassifiedModel dateClassifiedModel;
    private EditText dateText;
    private WheelDialog dialog;
    private LayoutInflater inflater;

    @Override
    protected String getLayoutName() {
        return super.getLayoutName();
    }

    private boolean isDate = false;
    private String[] radioitems = null;
    private LinearLayout rulesLinearLayout;
    private ScrollView scrollView;
    private String[] selectBoxitems = null;
    int type = 1;

    class EditTextClickListener implements OnClickListener {
        public EditTextClickListener(ClassifiedModel dateModel, EditText dateEditText) {
            ClassifyTopicActivity.this.dateClassifiedModel = dateModel;
            ClassifyTopicActivity.this.dateText = dateEditText;
        }

        public void onClick(View v) {
            ClassifyTopicActivity.this.dialog = new WheelDialog(ClassifyTopicActivity.this, ClassifyTopicActivity.this.dateText, new DateDelegate());
            ClassifyTopicActivity.this.dialog.setAddYear(false);
            ClassifyTopicActivity.this.dialog.setYearNum(60);
            ClassifyTopicActivity.this.dialog.show();
        }
    }

    class GetClassifiedModelInfoTask extends AsyncTask<Integer, Void, BaseResultModel<List<ClassifiedModel>>> {
        GetClassifiedModelInfoTask() {
        }

        protected BaseResultModel<List<ClassifiedModel>> doInBackground(Integer... params) {
            return ClassifyTopicActivity.this.postsService.getClassifiedModleList(ClassifyTopicActivity.this.classificationTopId, ClassifyTopicActivity.this.boardId);
        }

        protected void onPostExecute(BaseResultModel<List<ClassifiedModel>> result) {
            super.onPostExecute(result);
            if (result == null) {
                MCToastUtils.toastByResName(ClassifyTopicActivity.this.getApplicationContext(), "mc_forum_no_info");
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                ClassifyTopicActivity.this.classifiedModelList = (List) result.getData();
                if (ClassifyTopicActivity.this.classifiedModelMap != null) {
                    ClassifyTopicActivity.this.classifiedModelMap.put(ClassifyTopicActivity.this.classificationTopId + "" + ClassifyTopicActivity.this.boardId, ClassifyTopicActivity.this.classifiedModelList);
                }
                if (!MCListUtils.isEmpty(ClassifyTopicActivity.this.classifiedModelList)) {
                    ClassifyTopicActivity.this.createView();
                }
            } else {
                MCToastUtils.toastByResName(ClassifyTopicActivity.this.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    class MoreButtonOnclickListenner implements OnClickListener {
        private ClassifiedModel classifiedModel;

        public MoreButtonOnclickListenner(ClassifiedModel classifiedModel) {
            this.classifiedModel = classifiedModel;
        }

        public void onClick(View v) {
            int choicesModleListSize;
            int i;
            switch (this.classifiedModel.getClassifiedType()) {
                case 2:
                    List<ChoicesModle> radioChoicesModleList = this.classifiedModel.getClassifiedRules().getChoicesModleList();
                    String[] radioChoices = new String[radioChoicesModleList.size()];
                    if (!(radioChoicesModleList == null || radioChoicesModleList.isEmpty())) {
                        choicesModleListSize = radioChoicesModleList.size();
                        for (i = 0; i < choicesModleListSize; i++) {
                            radioChoices[i] = ((ChoicesModle) radioChoicesModleList.get(i)).getChoicesName();
                        }
                    }
                    new Builder(ClassifyTopicActivity.this).setTitle(this.classifiedModel.getClassifiedTitle()).setIcon(17301659).setSingleChoiceItems(radioChoices, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton(ClassifyTopicActivity.this.resource.getString("mc_forum_dialog_cancel"), null).show();
                    return;
                case 3:
                    List<ChoicesModle> checkBoxChoicesModleList = this.classifiedModel.getClassifiedRules().getChoicesModleList();
                    String[] checkBoxChoices = new String[checkBoxChoicesModleList.size()];
                    if (!(checkBoxChoicesModleList == null || checkBoxChoicesModleList.isEmpty())) {
                        choicesModleListSize = checkBoxChoicesModleList.size();
                        for (i = 0; i < choicesModleListSize; i++) {
                            checkBoxChoices[i] = ((ChoicesModle) checkBoxChoicesModleList.get(i)).getChoicesName();
                        }
                    }
                    new Builder(ClassifyTopicActivity.this).setTitle(this.classifiedModel.getClassifiedTitle()).setMultiChoiceItems(checkBoxChoices, null, null).setPositiveButton(ClassifyTopicActivity.this.resource.getString("mc_forum_dialog_confirm"), null).setNegativeButton(ClassifyTopicActivity.this.resource.getString("mc_forum_dialog_cancel"), null).show();
                    return;
                case 4:
                    List<ChoicesModle> selectChoicesModleList = this.classifiedModel.getClassifiedRules().getChoicesModleList();
                    String[] selectChoices = new String[selectChoicesModleList.size()];
                    if (!(selectChoicesModleList == null || selectChoicesModleList.isEmpty())) {
                        choicesModleListSize = selectChoicesModleList.size();
                        for (i = 0; i < choicesModleListSize; i++) {
                            selectChoices[i] = ((ChoicesModle) selectChoicesModleList.get(i)).getChoicesName();
                        }
                    }
                    new Builder(ClassifyTopicActivity.this).setTitle(this.classifiedModel.getClassifiedTitle()).setItems(selectChoices, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                    return;
                default:
                    return;
            }
        }
    }

    class PhotoClickListener implements OnClickListener {
        private int btnTag;
        private ClassifiedModel model;
        private ImageView onselfIcon;

        public PhotoClickListener(int btnTag, ClassifiedModel model, ImageView onselfIcon) {
            this.btnTag = btnTag;
            this.onselfIcon = onselfIcon;
            this.model = model;
            ClassifyTopicActivity.this.sortId = ClassifyTopicActivity.this.classificationTopId;
        }

        public void onClick(View v) {
            switch (this.btnTag) {
                case 1:
                    ClassifyTopicActivity.this.photoManageHelper.openPhotoSelector(ClassifyTopicActivity.this, 1, ClassifyTopicActivity.this.type, null);
                    ClassifyTopicActivity.this.publicIcon = this.onselfIcon;
                    ClassifyTopicActivity.this.publicModel = this.model;
                    return;
                case 2:
                    ClassifyTopicActivity.this.photoManageHelper.openPhotoGraph(ClassifyTopicActivity.this, 1, ClassifyTopicActivity.this.type, null);
                    ClassifyTopicActivity.this.publicIcon = this.onselfIcon;
                    ClassifyTopicActivity.this.publicModel = this.model;
                    return;
                default:
                    return;
            }
        }
    }

    private class DateDelegate implements WheelDelegate {
        private DateDelegate() {
        }

        public void positiveClickListener(View mainView) {
            ClassifyTopicActivity.this.dateText.setText((CharSequence) mainView.getTag());
            ClassifyTopicActivity.this.dateClassifiedModel.setClassifiedValue(ClassifyTopicActivity.this.dateText.getText() + "");
        }

        public void negativeClickListener(View mainView) {
            ClassifyTopicActivity.this.dateText.setText((CharSequence) mainView.getTag());
            ClassifyTopicActivity.this.dateClassifiedModel.setClassifiedValue(ClassifyTopicActivity.this.dateText.getText() + "");
        }
    }

    protected void initDatas() {
        super.initDatas();
        this.classificationTopId = getIntent().getIntExtra(IntentConstant.CLASSIFICATIONTOP_ID, 0);
        this.classifiedModelList = new ArrayList();
        this.classifiedModel = new ClassifiedModel();
        this.classifiedModeLoadDatalList = new ArrayList();
        this.publicModel = new ClassifiedModel();
        if (this.classificationTopId > 0) {
            this.classifiedModelInfoTask = new GetClassifiedModelInfoTask();
            this.classifiedModelInfoTask.execute(new Integer[0]);
            this.isPublishTopic = false;
        }
        this.postsService = new PostsServiceImpl(this);
        this.inflater = LayoutInflater.from(getApplicationContext());
    }

    protected void initViews() {
        super.initViews();
        this.rulesLinearLayout = (LinearLayout) findViewById(this.resource.getViewId("mc_forum_classified_view_layout"));
        this.scrollView = (ScrollView) findViewById(this.resource.getViewId("mc_forum_scrollview"));
        mToolbar.setCenteredTitle(resource.getString("mc_forum_publish"));
        if (this.classificationTopId > 0) {
            findViewByName("mc_forum_title_img").setVisibility(0);
        }
        hideSoftKeyboard();

    }

    protected void initActions() {
        super.initActions();
    }

    protected void onResume() {
        super.onResume();
    }

    private void createView() {
        this.rulesLinearLayout.removeAllViews();
        int classifiedModelListSize = this.classifiedModelList.size();
        int textViewWidth = dip2px(80);
        int textViewHeight = dip2px(50);
        int pading = dip2px(5);
        int leftMagin = dip2px(10);
        int iconLeftMagin = dip2px(5);
        int imageMoreButtonWidth = dip2px(50);
        int imageIconWidth = dip2px(50);
        int headInfoHeight = dip2px(60);
        int imageButtonWidth = dip2px(60);
        int imageButtonHeight = dip2px(25);
        int selfIntroductionHeight = dip2px(60);
        int i = 0;
        while (i < classifiedModelListSize) {
            LinearLayout.LayoutParams layoutParams;
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, textViewHeight);
            ((LinearLayout)linearLayout).setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutParams2);


            TextView textView = new TextView(getApplicationContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle() + ":");
            textView.setTextColor(this.resource.getColor("mc_forum_text4_normal_color"));
            textView.setTextSize(16.0f);
            textView.setPadding(leftMagin, 0, 0, 0);
            textView.setLayoutParams(new LinearLayout.LayoutParams(textViewWidth, -1));

            linearLayout.addView(textView);

            this.rulesLinearLayout.addView(linearLayout);
            int j;
            final AlertDialog alertDialog;
            final ClassifiedModel classifiedModel;
            final ClassifiedModel classifiedModel2;
            final List<ChoicesModle> list;
            switch (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedType()) {
                case 1:
                    ClassifiedModel classifiedModelText = new ClassifiedModel();
                    EditText editText = (EditText) this.inflater.inflate(this.resource.getLayoutId("edittext_view"), null);
                    ClassifiedModel dateModel = new ClassifiedModel();
                    String defaultvalue = null;
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue().equals(""))) {
                        defaultvalue = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue();
                    }
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || defaultvalue == null)) {
                        if (!defaultvalue.equals("")) {
                            editText.setText(defaultvalue);
                            classifiedModelText.setClassifiedValue(defaultvalue);
                            if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() != null) {
                                this.isDate = false;
                                editEvent(classifiedModelText, editText);
                            } else if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getIsDate() != 1) {
                                this.isDate = true;
                                dateModel.setClassifiedValue(classifiedModelText.getClassifiedValue());
                                editText.setFocusableInTouchMode(false);
                                editText.setInputType(0);
                                editText.setOnClickListener(new EditTextClickListener(dateModel, editText));
                            } else {
                                this.isDate = false;
                                editEvent(classifiedModelText, editText);
                            }
                            layoutParams = new LinearLayout.LayoutParams(-1, -1);
                            editText.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                            layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                            editText.setGravity(16);
                            editText.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
                            editText.setTextColor(this.resource.getColor("mc_forum_text3_content_normal_color"));
                            editText.setTextSize(16.0f);
                            editText.setSingleLine();
                            editText.setId(99999);
                            editText.setLayoutParams(layoutParams);
                            linearLayout.addView(editText);
                            if (this.isDate) {
                                this.classifiedModeLoadDatalList.add(dateModel);
                                break;
                            } else {
                                this.classifiedModeLoadDatalList.add(classifiedModelText);
                                break;
                            }
                        }
                    }
                    classifiedModelText.setClassifiedValue("");
                    if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() != null) {
                        this.isDate = false;
                        editEvent(classifiedModelText, editText);
                    } else if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getIsDate() != 1) {
                        this.isDate = false;
                        editEvent(classifiedModelText, editText);
                    } else {
                        this.isDate = true;
                        dateModel.setClassifiedValue(classifiedModelText.getClassifiedValue());
                        editText.setFocusableInTouchMode(false);
                        editText.setInputType(0);
                        editText.setOnClickListener(new EditTextClickListener(dateModel, editText));
                    }
                    layoutParams = new LinearLayout.LayoutParams(-1, -1);
                    editText.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                    layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                    editText.setGravity(16);
                    editText.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
                    editText.setTextColor(this.resource.getColor("mc_forum_text3_content_normal_color"));
                    editText.setTextSize(16.0f);
                    editText.setSingleLine();
                    editText.setId(99999);
                    editText.setLayoutParams(layoutParams);
                    linearLayout.addView(editText);
                    if (this.isDate) {
                        this.classifiedModeLoadDatalList.add(dateModel);
                    } else {
                        this.classifiedModeLoadDatalList.add(classifiedModelText);
                    }
                case 2:
                    List<ChoicesModle> radiostrList;
                    AlertDialog scb;
                    ClassifiedModel classifiedModelRadio = new ClassifiedModel();
                    TextView radioHinttextVeiw = new TextView(getApplicationContext(), null, this.resource.getStyleId(""));
                    List<ChoicesModle> radiostrListLoadDate = new ArrayList();
                    layoutParams = new LinearLayout.LayoutParams(-2, -1);
                    radioHinttextVeiw.setTextSize(16.0f);
                    radioHinttextVeiw.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
                    radioHinttextVeiw.setTextColor(this.resource.getColor("mc_forum_text4_normal_color"));
                    String defaultvalueRadio = null;
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue().equals(""))) {
                        defaultvalueRadio = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue();
                    }
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || defaultvalueRadio == null)) {
                        if (!defaultvalueRadio.equals("")) {
                            radioHinttextVeiw.setText(defaultvalueRadio);
                            classifiedModelRadio.setClassifiedValue(defaultvalueRadio);
                            layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                            layoutParams.setMargins(leftMagin, 0, 0, 0);
                            radioHinttextVeiw.setGravity(3);
                            radioHinttextVeiw.setGravity(16);
                            radioHinttextVeiw.setEllipsize(TruncateAt.END);
                            radioHinttextVeiw.setSingleLine(true);
                            radioHinttextVeiw.setLayoutParams(layoutParams);

//                            linearLayout = new ImageButton(getApplicationContext());
//                            layoutParams = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
//                            linearLayout.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
//                            linearLayout.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
//                            linearLayout.setLayoutParams(layoutParams);
                            ImageButton imageButton = new ImageButton(getApplicationContext());
                            layoutParams = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                            imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                            imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                            imageButton.setLayoutParams(layoutParams);

                            linearLayout.addView(radioHinttextVeiw);
                            linearLayout.addView(imageButton);

                            if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList() == null)) {
                                radiostrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                            }
                            radiostrList = radiostrListLoadDate;
                            if (radiostrList != null || radiostrList.isEmpty()) {
                                this.radioitems = new String[0];
                            } else if (radiostrList.size() > 0) {
                                this.radioitems = new String[radiostrList.size()];
                                for (j = 0; j < radiostrList.size(); j++) {
                                    this.radioitems[j] = ((ChoicesModle) radiostrList.get(j)).getChoicesName();
                                }
                            }
                            scb = new SingleChoseBuilder(this, this.radioitems, radioHinttextVeiw, null, this.classifiedModel).create();
                            alertDialog = scb;
                            classifiedModel = classifiedModelRadio;
                            radioHinttextVeiw.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    if (ClassifyTopicActivity.this.radioitems.length < 1) {
                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
                                        return;
                                    }
                                    alertDialog.show();
                                    classifiedModel.setClassifiedValue(null);
                                }
                            });
//                            alertDialog = scb;
//                            classifiedModel = classifiedModelRadio;
//                            linearLayout.setOnClickListener(new OnClickListener() {
//                                public void onClick(View v) {
//                                    if (ClassifyTopicActivity.this.radioitems.length < 1) {
//                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
//                                        return;
//                                    }
//                                    alertDialog.show();
//                                    classifiedModel.setClassifiedValue(null);
//                                }
//                            });
                            classifiedModel2 = classifiedModelRadio;
                            list = radiostrList;
                            radioHinttextVeiw.addTextChangedListener(new TextWatcher() {
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void afterTextChanged(Editable s) {
                                    if (!MCStringUtil.isEmpty(ClassifyTopicActivity.this.classifiedModel.getClassifiedValue())) {
                                        classifiedModel2.setClassifiedValue(((ChoicesModle) list.get(Integer.parseInt(ClassifyTopicActivity.this.classifiedModel.getClassifiedValue()))).getChoicesValue() + "");
                                    }
                                }
                            });
                            this.classifiedModeLoadDatalList.add(classifiedModelRadio);
                            break;
                        }
                    }
                    classifiedModelRadio.setClassifiedValue("");
                    layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                    layoutParams.setMargins(leftMagin, 0, 0, 0);
                    radioHinttextVeiw.setGravity(3);
                    radioHinttextVeiw.setGravity(16);
                    radioHinttextVeiw.setEllipsize(TruncateAt.END);
                    radioHinttextVeiw.setSingleLine(true);
                    radioHinttextVeiw.setLayoutParams(layoutParams);


                    ImageButton imageButton = new ImageButton(getApplicationContext());
                    layoutParams = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                    imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                    imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                    imageButton.setLayoutParams(layoutParams);

                    linearLayout.addView(radioHinttextVeiw);
                    linearLayout.addView(imageButton);
                    radiostrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                    radiostrList = radiostrListLoadDate;
                    if (radiostrList != null) {
                        break;
                    }
                    this.radioitems = new String[0];
                    scb = new SingleChoseBuilder(this, this.radioitems, radioHinttextVeiw, null, this.classifiedModel).create();
                    alertDialog = scb;
                    classifiedModel = classifiedModelRadio;
//                    radioHinttextVeiw.setOnClickListener(/* anonymous class already generated */);
//                    alertDialog = scb;
//                    classifiedModel = classifiedModelRadio;
//                    linearLayout.setOnClickListener(/* anonymous class already generated */);
                    classifiedModel2 = classifiedModelRadio;
                    list = radiostrList;
//                    radioHinttextVeiw.addTextChangedListener(/* anonymous class already generated */);
                    this.classifiedModeLoadDatalList.add(classifiedModelRadio);
                    break;
                case 3:
                    List<ChoicesModle> checkBoxstrList;
                    AlertDialog multyBuilder;
                    final List<ChoicesModle> list2;
                    ClassifiedModel classifiedModelCheckBox = new ClassifiedModel();
                    TextView checkBoxHinttextVeiw = new TextView(getApplicationContext());
                    List<ChoicesModle> checkBoxstrListLoadDate = new ArrayList();
                    layoutParams = new LinearLayout.LayoutParams(-2, -1);
                    checkBoxHinttextVeiw.setTextSize(16.0f);
                    checkBoxHinttextVeiw.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
                    checkBoxHinttextVeiw.setTextColor(this.resource.getColor("mc_forum_text4_normal_color"));
                    String defaultvalueCheckBox = null;
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().toString()) || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() == null || MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue()))) {
                        defaultvalueCheckBox = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue();
                    }
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || defaultvalueCheckBox == null)) {
                        if (!defaultvalueCheckBox.equals("")) {
                            checkBoxHinttextVeiw.setText(defaultvalueCheckBox);
                            classifiedModelCheckBox.setClassifiedValue(defaultvalueCheckBox);
                            layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                            layoutParams.setMargins(leftMagin, 0, 0, 0);
                            checkBoxHinttextVeiw.setGravity(3);
                            checkBoxHinttextVeiw.setGravity(16);
                            checkBoxHinttextVeiw.setEllipsize(TruncateAt.END);
                            checkBoxHinttextVeiw.setSingleLine(true);
                            checkBoxHinttextVeiw.setLayoutParams(layoutParams);

                            imageButton = new ImageButton(getApplicationContext());
                            layoutParams2 = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                            imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                            imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                            imageButton.setLayoutParams(layoutParams2);
                            imageButton.setOnClickListener(new MoreButtonOnclickListenner((ClassifiedModel) this.classifiedModelList.get(i)));

                            linearLayout.addView(checkBoxHinttextVeiw);
                            linearLayout.addView(imageButton);
                            if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList() == null)) {
                                checkBoxstrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                            }
                            checkBoxstrList = checkBoxstrListLoadDate;
                            if (checkBoxstrList != null || checkBoxstrList.isEmpty()) {
                                this.checkBoxitems = new String[0];
                            } else if (checkBoxstrList.size() > 0) {
                                this.checkBoxitems = new String[checkBoxstrList.size()];
                                for (j = 0; j < checkBoxstrList.size(); j++) {
                                    this.checkBoxitems[j] = ((ChoicesModle) checkBoxstrList.get(j)).getChoicesName();
                                }
                            }
                            multyBuilder = new MultyChoseBuilder(this, this.checkBoxitems, new boolean[this.checkBoxitems.length], checkBoxHinttextVeiw, this.classifiedModel).create();
                            alertDialog = multyBuilder;
                            checkBoxHinttextVeiw.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    if (ClassifyTopicActivity.this.checkBoxitems.length < 1) {
                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
                                    } else {
                                        alertDialog.show();
                                    }
                                }
                            });
//                            alertDialog = multyBuilder;
//                            linearLayout.setOnClickListener(new OnClickListener() {
//                                public void onClick(View v) {
//                                    if (ClassifyTopicActivity.this.checkBoxitems.length < 1) {
//                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
//                                    } else {
//                                        alertDialog.show();
//                                    }
//                                }
//                            });
                            list2 = checkBoxstrList;
                            classifiedModel = classifiedModelCheckBox;
                            checkBoxHinttextVeiw.addTextChangedListener(new TextWatcher() {
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void afterTextChanged(Editable s) {
                                    if (MCStringUtil.isEmpty(ClassifyTopicActivity.this.classifiedModel.getClassifiedValue())) {
                                        classifiedModel.setClassifiedValue("");
                                        return;
                                    }
                                    String[] checkedItems = ClassifyTopicActivity.this.classifiedModel.getClassifiedValue().split(AdApiConstant.RES_SPLIT_COMMA);
                                    StringBuilder sb = new StringBuilder();
                                    String text = null;
                                    for (String parseInt : checkedItems) {
                                        sb.append(((ChoicesModle) list2.get(Integer.parseInt(parseInt))).getChoicesValue() + AdApiConstant.RES_SPLIT_COMMA);
                                    }
                                    if (sb.length() > 1) {
                                        text = sb.subSequence(0, sb.length() - 1).toString();
                                    }
                                    classifiedModel.setClassifiedValue(text);
                                }
                            });
                            this.classifiedModeLoadDatalList.add(classifiedModelCheckBox);
                            break;
                        }
                    }
                    classifiedModelCheckBox.setClassifiedValue("");
                    layoutParams.weight = CustomConstant.RATIO_ONE_HEIGHT;
                    layoutParams.setMargins(leftMagin, 0, 0, 0);
                    checkBoxHinttextVeiw.setGravity(3);
                    checkBoxHinttextVeiw.setGravity(16);
                    checkBoxHinttextVeiw.setEllipsize(TruncateAt.END);
                    checkBoxHinttextVeiw.setSingleLine(true);
                    checkBoxHinttextVeiw.setLayoutParams(layoutParams);
                    imageButton = new ImageButton(getApplicationContext());
                    layoutParams2 = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                    imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                    imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                    imageButton.setLayoutParams(layoutParams2);
                    imageButton.setOnClickListener(new MoreButtonOnclickListenner((ClassifiedModel) this.classifiedModelList.get(i)));

                    linearLayout.addView(checkBoxHinttextVeiw);
                    linearLayout.addView(imageButton);

                    checkBoxstrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                    checkBoxstrList = checkBoxstrListLoadDate;
                    if (checkBoxstrList != null) {
                        break;
                    }
                    this.checkBoxitems = new String[0];
                    multyBuilder = new MultyChoseBuilder(this, this.checkBoxitems, new boolean[this.checkBoxitems.length], checkBoxHinttextVeiw, this.classifiedModel).create();
                    alertDialog = multyBuilder;
//                    checkBoxHinttextVeiw.setOnClickListener(/* anonymous class already generated */);
//                    alertDialog = multyBuilder;
//                    linearLayout.setOnClickListener(/* anonymous class already generated */);
//                    list2 = checkBoxstrList;
//                    classifiedModel = classifiedModelCheckBox;
//                    checkBoxHinttextVeiw.addTextChangedListener(/* anonymous class already generated */);
                    this.classifiedModeLoadDatalList.add(classifiedModelCheckBox);
                    break;
                case 4:
                    List<ChoicesModle> selectBoxstrList;
                    AlertDialog selectBoxscb;
                    ClassifiedModel classifiedModelSelect = new ClassifiedModel();
                    textView = new TextView(getApplicationContext());
                    List<ChoicesModle> selectBoxstrListLoadDate = new ArrayList();
                    layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
                    layoutParams2.weight = CustomConstant.RATIO_ONE_HEIGHT;
                    layoutParams2.setMargins(leftMagin, 0, 0, 0);
                    textView.setTextSize(16.0f);
                    textView.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
                    textView.setTextColor(this.resource.getColor("mc_forum_text4_normal_color"));
                    String defaultvalueSelectBox = null;
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue().equals(""))) {
                        defaultvalueSelectBox = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue();
                    }
                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || defaultvalueSelectBox == null)) {
                        if (!defaultvalueSelectBox.equals("")) {
                            textView.setText(defaultvalueSelectBox);
                            classifiedModelSelect.setClassifiedValue(defaultvalueSelectBox);
                            textView.setGravity(3);
                            textView.setGravity(16);
                            textView.setEllipsize(TruncateAt.END);
                            textView.setSingleLine(true);
                            textView.setLayoutParams(layoutParams2);

                            imageButton = new ImageButton(getApplicationContext());
                            layoutParams2 = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                            imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                            imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                            imageButton.setLayoutParams(layoutParams2);
                            linearLayout.addView(textView);
                            linearLayout.addView(imageButton);

                            if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList() == null)) {
                                selectBoxstrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                            }
                            selectBoxstrList = selectBoxstrListLoadDate;
                            if (selectBoxstrList != null || selectBoxstrList.isEmpty()) {
                                this.selectBoxitems = new String[0];
                            } else if (selectBoxstrList.size() > 0) {
                                this.selectBoxitems = new String[selectBoxstrList.size()];
                                for (j = 0; j < selectBoxstrList.size(); j++) {
                                    this.selectBoxitems[j] = ((ChoicesModle) selectBoxstrList.get(j)).getChoicesName();
                                }
                            }

                            selectBoxscb = new SelectChoseItemBuilder(this, this.selectBoxitems, textView, this.classifiedModel).create();

                            alertDialog = selectBoxscb;
                            textView.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    if (ClassifyTopicActivity.this.selectBoxitems.length < 1) {
                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
                                    } else {
                                        alertDialog.show();
                                    }
                                }
                            });
                            imageButton.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    if (ClassifyTopicActivity.this.selectBoxitems.length < 1) {
                                        MCToastUtils.toast(ClassifyTopicActivity.this.getApplicationContext(), ClassifyTopicActivity.this.resource.getString("mc_forum_there_hava_no_choice"), 0);
                                    } else {
                                        alertDialog.show();
                                    }
                                }
                            });
                            classifiedModel2 = classifiedModelSelect;
                            list = selectBoxstrList;
                            textView.addTextChangedListener(new TextWatcher() {
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void afterTextChanged(Editable s) {
                                    if (!MCStringUtil.isEmpty(ClassifyTopicActivity.this.classifiedModel.getClassifiedValue())) {
                                        classifiedModel2.setClassifiedValue(((ChoicesModle) list.get(Integer.parseInt(ClassifyTopicActivity.this.classifiedModel.getClassifiedValue()))).getChoicesValue() + "");
                                    }
                                }
                            });
                            this.classifiedModeLoadDatalList.add(classifiedModelSelect);
                            break;
                        }
                    }

                    classifiedModelSelect.setClassifiedValue("");
                    textView.setGravity(3);
                    textView.setGravity(16);
                    textView.setEllipsize(TruncateAt.END);
                    textView.setSingleLine(true);
                    textView.setLayoutParams(layoutParams2);
                    imageButton = new ImageButton(getApplicationContext());
                    layoutParams2 = new LinearLayout.LayoutParams(imageMoreButtonWidth, -1);
                    imageButton.setImageResource(this.resource.getDrawableId("mc_forum_release_arrow"));
                    imageButton.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
                    imageButton.setLayoutParams(layoutParams2);
                    linearLayout.addView(textView);
                    linearLayout.addView(imageButton);
                    selectBoxstrListLoadDate = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getChoicesModleList();
                    selectBoxstrList = selectBoxstrListLoadDate;
                    if (selectBoxstrList != null) {
                        break;
                    }
                    this.selectBoxitems = new String[0];
                    selectBoxscb = new SelectChoseItemBuilder(this, this.selectBoxitems, textView, this.classifiedModel).create();
//                    alertDialog = selectBoxscb;
//                    linearLayout.setOnClickListener(/* anonymous class already generated */);
//                    alertDialog = selectBoxscb;
//                    linearLayout.setOnClickListener(/* anonymous class already generated */);
//                    classifiedModel2 = classifiedModelSelect;
//                    list = selectBoxstrList;
//                    textView.addTextChangedListener(/* anonymous class already generated */);

                    this.classifiedModeLoadDatalList.add(classifiedModelSelect);
                    break;
                case 5:


//                    linearLayout.removeView(linearLayout);
//                    this.rulesLinearLayout.removeView(linearLayout);
//                    linearLayout = new LinearLayout(getApplicationContext());
//                    layoutParams2 = new LinearLayout.LayoutParams(-1, selfIntroductionHeight);
//                    linearLayout.setOrientation(0);
//                    linearLayout.setLayoutParams(layoutParams2);
//                    linearLayout = new TextView(getApplicationContext());
//                    linearLayout.setGravity(16);
//                    linearLayout.setPadding(leftMagin, 0, 0, 0);
//                    linearLayout.setTextColor(this.resource.getColor("mc_forum_text3_content_normal_color"));
//                    linearLayout.setText(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle() + ":");
//                    linearLayout.setTextSize(16.0f);
//                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(textViewWidth, -2));
//                    linearLayout.addView(linearLayout);
//                    ClassifiedModel classifiedModelTextArea = new ClassifiedModel();
//                    View editTextArea = (EditText) this.inflater.inflate(this.resource.getLayoutId("edittext_view"), null);
//                    layoutParams2 = new LinearLayout.LayoutParams(-1, -1);
//                    layoutParams2.weight = CustomConstant.RATIO_ONE_HEIGHT;
//                    editTextArea.setTextSize(16.0f);
//                    editTextArea.setBackgroundColor(this.resource.getDrawableId("mc_forum_email_bg_color"));
//                    editTextArea.setHintTextColor(this.resource.getColor("mc_forum_text4_other_normal_color"));
//                    editTextArea.setTextColor(this.resource.getColor("mc_forum_text3_content_normal_color"));
//                    String defaultvalueTextArea = null;
//                    if (!(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() == null || ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue().equals(""))) {
//                        defaultvalueTextArea = ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue();
//                    }
//                    if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() != null) {
//                        if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getMaxlength())) {
//                            editTextArea.setMaxWidth(MCPhoneUtil.getRawSize(getApplicationContext(), 1, (float) Integer.parseInt(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getMaxlength())));
//                        }
//                        if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue() != null) {
//                            classifiedModelTextArea.setClassifiedValue(defaultvalueTextArea);
//                            editTextArea.setHint(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getDefaultvalue());
//                        }
//                    }
//                    layoutParams2.setMargins(0, 0, 0, 0);
//                    editTextArea.setLayoutParams(layoutParams2);
//                    editTextArea.setOnTouchListener(new OnTouchListener() {
//                        public boolean onTouch(View v, MotionEvent event) {
//                            ClassifyTopicActivity.this.scrollView.setFocusable(false);
//                            return false;
//                        }
//                    });
//                    classifiedModel2 = classifiedModelTextArea;
//                    final EditText editText2 = editTextArea;
//                    editTextArea.addTextChangedListener(new TextWatcher() {
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            ClassifyTopicActivity.this.scrollView.setFocusable(false);
//                        }
//
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        }
//
//                        public void afterTextChanged(Editable s) {
//                            classifiedModel2.setClassifiedValue(editText2.getText().toString());
//                        }
//                    });
//                    this.classifiedModeLoadDatalList.add(classifiedModelTextArea);
//                    linearLayout.addView(editTextArea);
//                    this.rulesLinearLayout.addView(linearLayout);
                    break;
                case 6:
//                    linearLayout.removeView(linearLayout);
//                    this.rulesLinearLayout.removeView(linearLayout);
//                    linearLayout = new LinearLayout(getApplicationContext());
//                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
//                    linearLayout = new TextView(getApplicationContext());
//                    linearLayout.setGravity(16);
//                    linearLayout.setTextColor(this.resource.getColor("mc_forum_text4_normal_color"));
//                    linearLayout.setText(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle() + ":");
//                    linearLayout.setTextSize(16.0f);
//                    layoutParams2 = new LinearLayout.LayoutParams(textViewWidth, -2);
//                    layoutParams2.topMargin = dip2px(18);
//                    layoutParams2.leftMargin = leftMagin;
//                    linearLayout.setLayoutParams(layoutParams2);
//                    linearLayout.addView(linearLayout);
//                    linearLayout = new LinearLayout(getApplicationContext());
//                    layoutParams = new LinearLayout.LayoutParams(-1, headInfoHeight);
//                    layoutParams.setMargins(iconLeftMagin, 0, 0, 0);
//                    linearLayout = new RelativeLayout(this);
//                    linearLayout = new RelativeLayout(this);
//                    linearLayout = new RelativeLayout(this);
//                    linearLayout.setGravity(17);
//                    linearLayout.setGravity(17);
//                    linearLayout.setGravity(17);
//                    linearLayout = new ImageView(getApplicationContext());
//                    ClassifiedModel classifiedModelImage = new ClassifiedModel();
//                    layoutParams = new LinearLayout.LayoutParams(imageIconWidth, imageIconWidth);
//                    layoutParams.setMargins(iconLeftMagin, pading, pading, pading);
//                    linearLayout.setBackgroundResource(this.resource.getDrawableId("mc_forum_card_img"));
//                    linearLayout.addView(linearLayout, layoutParams);
//                    linearLayout = new Button(getApplicationContext());
//                    layoutParams = new LinearLayout.LayoutParams(imageButtonWidth, dip2px(30));
//                    linearLayout.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon1"));
//                    linearLayout.setText(this.resource.getStringId("mc_forum_take_photo"));
//                    linearLayout.setTextSize(0, getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_14")));
//                    linearLayout.setIncludeFontPadding(false);
//                    linearLayout.setGravity(17);
//                    linearLayout.setTextColor(this.resource.getColor("mc_forum_text1_normal_color"));
//                    linearLayout.setOnClickListener(new PhotoClickListener(2, classifiedModelImage, linearLayout));
//                    linearLayout.addView(linearLayout, layoutParams);
//                    Button button = new Button(getApplicationContext());
//                    layoutParams2 = new LinearLayout.LayoutParams(imageButtonWidth, dip2px(30));
//                    button.setText(this.resource.getStringId("mc_forum_gallery_pic"));
//                    button.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon1"));
//                    button.setIncludeFontPadding(false);
//                    button.setGravity(17);
//                    button.setTextSize(0, getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_14")));
//                    button.setTextColor(this.resource.getColor("mc_forum_text1_normal_color"));
//                    button.setLayoutParams(layoutParams2);
//                    button.setOnClickListener(new PhotoClickListener(1, classifiedModelImage, linearLayout));
//                    linearLayout.addView(button, layoutParams2);
//                    linearLayout.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1, CustomConstant.RATIO_ONE_HEIGHT));
//                    linearLayout.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1, CustomConstant.RATIO_ONE_HEIGHT));
//                    linearLayout.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1, CustomConstant.RATIO_ONE_HEIGHT));
//                    linearLayout.addView(linearLayout, layoutParams);
//                    this.rulesLinearLayout.addView(linearLayout);
//                    this.classifiedModeLoadDatalList.add(classifiedModelImage);
                    break;
                default:
                    break;
            }
            layoutParams = new LinearLayout.LayoutParams(-1, -2);
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(this.resource.getDrawableId("mc_forum_wire"));
            imageView.setLayoutParams(layoutParams);
            this.rulesLinearLayout.addView(imageView);
            i++;
        }
    }

    private void editEvent(final ClassifiedModel classifiedModelText, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (editText.getText() != null && !editText.getText().equals("")) {
                    classifiedModelText.setClassifiedValue(editText.getText().toString());
                }
            }
        });
        editText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                editText.requestFocus();
                ClassifyTopicActivity.this.showSoftKeyboard();
            }
        });
    }

    protected boolean publicTopic() {
        this.content = this.contentEdText.getText().toString();
        this.title = this.titleEdText.getText().toString();
        if (this.boardId <= 0) {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_select_board", 0);
            this.publishIng = false;
            return false;
        } else if (checkTitleAndContent()) {
            if (this.classificationTopId > 0 && this.classifiedModelList != null && !this.classifiedModelList.isEmpty() && this.classifiedModelList.size() == this.classifiedModeLoadDatalList.size()) {
                int i = 0;
                while (i < this.classifiedModelList.size()) {
                    ((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).setClassifiedName(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedName().toString());
                    ((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).setClassifiedType(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedType());
                    if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() != null) {
                        ((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).setClassifiedRules(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules());
                    }
                    if (MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue())) {
                        if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModelList.get(i)).getRequired()) && Integer.parseInt(((ClassifiedModel) this.classifiedModelList.get(i)).getRequired().trim()) == 1) {
                            this.publishIng = false;
                            MCToastUtils.toast(getApplicationContext(), ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle() + this.resource.getString("mc_forum_classifiedmodle_not_null"), 0);
                            return false;
                        }
                    } else if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedName()) && ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedName().contains("email") && !MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue()) && !MCStringUtil.isEmail(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().toString())) {
                        MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_user_email_format_error_warn", 0);
                        this.publishIng = false;
                        return false;
                    } else if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules() == null) {
                        continue;
                    } else if (((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedRules().getTypeNum() != 1 || MCStringUtil.isNumeric(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue())) {
                        String[] args;
                        if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxnum())) {
                            if (Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().trim()).floatValue() > Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxnum().trim()).floatValue()) {
                                float maxNum = Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxnum().trim()).floatValue();
                                if (MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum())) {
                                    args = new String[]{((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle(), maxNum + ""};
                                    this.publishIng = false;
                                    MCToastUtils.toast(getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_classifiedmodle_salary_controll_down"), args, (Context) this));
                                    return false;
                                }
                                float minNum = Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim()).floatValue();
                                args = new String[]{((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle(), minNum + "", maxNum + ""};
                                this.publishIng = false;
                                MCToastUtils.toast(getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_classifiedmodle_salary_controll"), args, getApplicationContext()), 0);
                                return false;
                            } else if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum())) {
                                if (Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().trim()).floatValue() < Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim()).floatValue()) {

                                    float maxNum = Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxnum().trim()).floatValue();
                                    args = new String[]{((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle(), Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim()).floatValue() + "", maxNum + ""};
                                    this.publishIng = false;
                                    MCToastUtils.toast(getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_classifiedmodle_salary_controll"), args, (Context) this));
                                    return false;
                                }
                            }
                        } else if (!MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim())) {
                            if (Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().trim()).floatValue() < Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim()).floatValue()) {
                                args = new String[]{((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle(), Float.valueOf(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMinnum().trim()).floatValue() + ""};
                                this.publishIng = false;
                                MCToastUtils.toast(getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_classifiedmodle_salary_controll_up"), args, (Context) this));
                                return false;
                            }
                        }
                        if (MCStringUtil.isEmpty(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxlength())) {
                            continue;
                        } else {
                            int maxLength = Integer.parseInt(((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedRules().getMaxlength());
                            if (((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().length() < 0 || ((ClassifiedModel) this.classifiedModeLoadDatalList.get(i)).getClassifiedValue().length() > maxLength) {
                                args = new String[]{((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle()};
                                this.publishIng = false;
                                MCToastUtils.toast(getApplicationContext(), MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_classifiedmodle_controll"), args, (Context) this));
                                return false;
                            }
                        }
                    } else {
                        this.publishIng = false;
                        MCToastUtils.toast(getApplicationContext(), ((ClassifiedModel) this.classifiedModelList.get(i)).getClassifiedTitle() + this.resource.getString("mc_forum_classifiedmodle_must_be_num"), 0);
                        return false;
                    }
                    i++;
                }
            }
            if (this.classificationTopId <= 0) {
                finish();
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_publish", 0);
            }
            if (!SharedPreferencesDB.getInstance(getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND) && this.isOnlytTopicType == 1 && this.classificationTypeId < 1) {
                this.publishIng = false;
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_warn_classified_typename_not_be_null", 1);
                return false;
            } else if (this.picMap == null || this.picMap.size() <= 0) {
                if (!this.hasAudio) {
                    uploadAudioSucc();
                } else if (this.settingModel == null || this.settingModel.getPostAudioLimit() == -1) {
                    this.publishIng = false;
                    MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_record_permission", 1);
                } else {
                    uploadAudio();
                }
                return true;
            } else if (this.allowPostImage == 1) {
                uploadPic();
                return true;
            } else {
                this.publishIng = false;
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_image_permission", 1);
                return false;
            }
        } else {
            this.publishIng = false;
            return false;
        }
    }

    protected void uploadAudioSucc() {
        this.classifiedJson = this.postsService.createPublishClassifiedModelJson(this.classifiedModeLoadDatalList);
        String contentStr = this.postsService.createContentJson(this.content, "ß", "á", this.audioPath, this.uploadList);
        if (this.requireLocation == 0) {
            this.locationStr = "";
        }
        String publishJson = PostsServiceImplHelper.getPublishSortJsonStr(this, this.boardId, this.title, this.classifiedJson, contentStr, getAid(), this.longitude, this.latitude, this.locationStr, 1, this.classificationTypeId, this.classificationTopId, this.isCheckedPermissionModel);
        this.publishAsyncTask = new PublishAsyncTask(convertDraftModel());
        this.publishAsyncTask.execute(new Object[]{publishJson});
    }

    public void updateView(BoardChild boardModel) {
        this.typeAdapter = new PublishTopicTypeListAdapter(this, this.typeList);
        this.boardId = boardModel.getBoardId();
        this.boardName = boardModel.getBoardName();
        if (this.boradText != null) {
            this.boradText.setText(boardModel.getBoardName());
        }
        getSettingModel();
        this.rulesLinearLayout.removeAllViews();
        if (this.classificationTopId > 0) {
            this.classifiedModelInfoTask = new GetClassifiedModelInfoTask();
            this.classifiedModelInfoTask.execute(new Integer[0]);
            findViewByName("mc_forum_title_img").setVisibility(0);
            this.isPublishTopic = false;
        }
    }

    public boolean checkTitleAndContent() {
        this.title = this.titleEdText.getText().toString();
        if (this.configComponentModel == null || this.configComponentModel.isShowTopicTitle()) {
            this.title = this.titleEdText.getText().toString();
            if (MCStringUtil.isEmpty(this.title)) {
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_title_length_error");
                return false;
            } else if (MCStringUtil.isEmpty(this.content) && this.classificationTopId <= 0 && this.picMap.size() <= 0) {
                MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_publish_min_length_error");
                return false;
            }
        }
        if (this.content.length() > 20) {
            this.title = this.content.substring(0, 20);
        } else {
            this.title = this.content;
        }
        if (MCStringUtil.isEmpty(this.title)) {
            this.title = this.resource.getString("mc_forum_no_title") + this.boardName;
        }
        return true;
    }

    private int dip2px(int dip) {
        return MCPhoneUtil.getRawSize(this, 1, (float) dip);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.classifiedModelInfoTask != null) {
            this.classifiedModelInfoTask.cancel(true);
        }
        this.photoManageHelper.onDestroy();
    }
}
