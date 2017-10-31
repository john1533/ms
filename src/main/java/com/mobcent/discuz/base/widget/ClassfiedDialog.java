package com.mobcent.discuz.base.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ClassifyTopModel;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.List;

public class ClassfiedDialog extends Dialog {
    private BoardChild boardModel;
    private ClassfiedCallBack callBack;
    private Button cancleBtn;
    private List<ClassifyTopModel> classficationTopList;
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private MCResource resource;

    public interface ClassfiedCallBack {
        void classfiedTopCallBack(ClassifyTopModel classifyTopModel);
    }

    public ClassfiedCallBack getCallBack() {
        return this.callBack;
    }

    public void setCallBack(ClassfiedCallBack callBack) {
        this.callBack = callBack;
    }

    public ClassfiedDialog(Context context) {
        super(context);
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public ClassfiedDialog(Context context, int theme, List<ClassifyTopModel> classficationTopList, BoardChild boardModel) {
        super(context, theme);
        this.resource = MCResource.getInstance(context);
        this.classficationTopList = classficationTopList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.boardModel = boardModel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.resource.getLayoutId("publish_classify_dialog"));
        this.layout = (LinearLayout) findViewById(this.resource.getViewId("mc_forun_publish_classify_box"));
        this.cancleBtn = (Button) findViewById(this.resource.getViewId("mc_forum_cancle_btn"));
        for (int i = 0; i < this.classficationTopList.size(); i++) {
            final ClassifyTopModel topModel = (ClassifyTopModel) this.classficationTopList.get(i);
            if ("sort".equals(topModel.getType())) {
                View itemView = this.inflater.inflate(this.resource.getLayoutId("classfication_top_list_item"), null);
                ((TextView) itemView.findViewById(this.resource.getViewId("mc_forum_publish_text"))).setText(topModel.getTitle());
                this.layout.addView(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ClassfiedDialog.this.dismiss();
                        ClassfiedDialog.this.callBack.classfiedTopCallBack(topModel);
                    }
                });
            }
        }
        this.cancleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = ((Activity) ClassfiedDialog.this.context).getIntent();
                intent.putExtra(IntentConstant.INTENT_SELECT_BOARD, ClassfiedDialog.this.boardModel);
                ((Activity) ClassfiedDialog.this.context).setResult(0, intent);
                ((Activity) ClassfiedDialog.this.context).finish();
                ClassfiedDialog.this.dismiss();
            }
        });
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }
}
