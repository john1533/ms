package com.mobcent.discuz.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ClassifyTopModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.base.widget.ClassfiedDialog.ClassfiedCallBack;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class MCBoardSortView extends LinearLayout {
    private long boardId;
    private BoardChild boardModel;
    private ClassifyTopModel classficationTopModel;
    private List<ClassifyTopModel> classficationTopModelList;
    private Context context;
    private PermissionModel permissionModel;
    private MCResource resource;
    private PermissionModel settingModel;

    private MCBoardSortView(Context context) {
        super(context);
    }

    private MCBoardSortView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCBoardSortView(Context context, BoardChild boardModel, PermissionModel settingModel, long boardId) {
        super(context);
        this.context = context;
        this.boardModel = boardModel;
        this.settingModel = settingModel;
        this.resource = MCResource.getInstance(context);
        setLayoutParams(new LayoutParams(-1, -1));
        setOrientation(0);
        setPadding(0, MCPhoneUtil.getRawSize(context, 1, 16.0f), 0, 0);
        this.classficationTopModelList = new ArrayList();
        this.boardId = boardId;
        show();
    }

    private void getSettingModel(long boardId) {
        if (this.settingModel != null && this.settingModel.getPostInfo() != null) {
            this.permissionModel = (PermissionModel) this.settingModel.getPostInfo().get(Long.valueOf(boardId));
            if (this.permissionModel != null && !MCListUtils.isEmpty(this.permissionModel.getTopicPermissionModel().getNewTopicPanel())) {
                for (int i = 0; i < this.permissionModel.getTopicPermissionModel().getNewTopicPanel().size(); i++) {
                    ClassifyTopModel model = (ClassifyTopModel) this.permissionModel.getTopicPermissionModel().getNewTopicPanel().get(i);
                    if (model.getType().equals("sort")) {
                        this.classficationTopModelList.add(model);
                    }
                }
            }
        }
    }

    private void show() {
        final ImageView imageView = new ImageView(this.context);
        imageView.setLayoutParams(new LayoutParams(MCPhoneUtil.getRawSize(this.context, 1, 22.0f), MCPhoneUtil.getRawSize(this.context, 1, 22.0f)));
        if (this.boardModel.getBoardId() == this.boardId) {
            imageView.setBackgroundDrawable(this.resource.getDrawable("mc_forum_personal_set_icon3_h"));
        } else {
            imageView.setBackgroundDrawable(this.resource.getDrawable("mc_forum_personal_set_icon3_n"));
        }
        addView(imageView);
        TextView textView = new TextView(this.context);
        textView.setPadding(Math.round(TypedValue.applyDimension(1, 10.0f, getResources().getDisplayMetrics())), 0, 0, 0);
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(this.boardModel.getBoardName());
        textView.setSingleLine();
        textView.setEllipsize(TruncateAt.END);
        textView.setTextSize(14.0f);
        textView.setTextColor(this.resource.getColor("mc_forum_text4_desc_normal_color"));
        addView(textView);
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCBoardSortView.this.getSettingModel(MCBoardSortView.this.boardModel.getBoardId());
                imageView.setBackgroundDrawable(MCBoardSortView.this.resource.getDrawable("mc_forum_personal_set_icon3_h"));
                if (MCBoardSortView.this.classficationTopModelList == null || MCBoardSortView.this.classficationTopModelList.size() <= 0) {
                    Intent intent = ((Activity) MCBoardSortView.this.context).getIntent();
                    intent.putExtra(IntentConstant.INTENT_SELECT_BOARD, MCBoardSortView.this.boardModel);
                    ((Activity) MCBoardSortView.this.context).setResult(0, intent);
                    ((Activity) MCBoardSortView.this.context).finish();
                    return;
                }
                ClassfiedDialog classfiedDialog = new ClassfiedDialog(MCBoardSortView.this.context, MCBoardSortView.this.resource.getStyleId("mc_forum_home_publish_classity_dialog"), MCBoardSortView.this.classficationTopModelList, MCBoardSortView.this.boardModel);
                classfiedDialog.show();
                classfiedDialog.setCallBack(new ClassfiedCallBack() {
                    public void classfiedTopCallBack(ClassifyTopModel topModel) {
                        MCBoardSortView.this.classficationTopModel = topModel;
                        Intent intent = ((Activity) MCBoardSortView.this.context).getIntent();
                        intent.putExtra(IntentConstant.CLASSIFICATIONTOP_ID, MCBoardSortView.this.classficationTopModel.getActionId());
                        intent.putExtra(IntentConstant.INTENT_SELECT_BOARD, MCBoardSortView.this.boardModel);
                        ((Activity) MCBoardSortView.this.context).setResult(0, intent);
                        ((Activity) MCBoardSortView.this.context).finish();
                    }
                });
            }
        });
    }
}
