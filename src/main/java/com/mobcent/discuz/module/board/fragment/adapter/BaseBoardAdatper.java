package com.mobcent.discuz.module.board.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.board.fragment.adapter.holder.BoardAdapterHolder;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.List;

public abstract class BaseBoardAdatper extends BaseListAdatper<BoardParent, BoardAdapterHolder> implements BaseIntentConstant {
    public String TAG = "BaseBoardAdatper";
    private ConfigComponentModel componentModel;
    private boolean isChildBoard = false;

    protected abstract String getDoubleLayoutName();

    protected abstract String getSingleLayoutName();

    protected abstract void initDoubleView(View view, BoardChild boardChild, BoardChild boardChild2, boolean z);

    protected abstract void initSingleView(View view, BoardChild boardChild, boolean z);

    public ConfigComponentModel getComponentModel() {
        return this.componentModel;
    }

    public void setComponentModel(ConfigComponentModel componentModel) {
        this.componentModel = componentModel;
    }

    public boolean isChildBoard() {
        return this.isChildBoard;
    }

    public void setChildBoard(boolean isChildBoard) {
        this.isChildBoard = isChildBoard;
    }

    public BaseBoardAdatper(Context context, List<BoardParent> datas) {
        super(context, datas);
    }

    protected void initViews(View converView, BoardAdapterHolder holder) {
        holder.setContainerBox((LinearLayout) findViewByName(converView, "container_layout"));
        holder.setTitleText((TextView) findViewByName(converView, "title_text"));
    }

    protected void initViewDatas(final BoardAdapterHolder holder, BoardParent data, int position) {
        if (isChildBoard()) {
            holder.getTitleText().setVisibility(8);
        } else {
            holder.getTitleText().setText(data.getBoardCategoryName());
        }
        holder.getContainerBox().removeAllViews();
        int column = data.getBoardCategoryType();
        if (!MCListUtils.isEmpty(data.getChildList())) {
            if (column == 1) {
                init1Column(holder.getContainerBox(), data.getChildList());
            } else {
                init2Column(holder.getContainerBox(), data.getChildList());
            }
        }
        holder.getContainerBox().getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                holder.getContainerBox().getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    private void init1Column(LinearLayout containerBox, List<BoardChild> data) {
        int i = 0;
        int len = data.size();
        while (i < len) {
            View view = this.inflater.inflate(this.resource.getLayoutId(getSingleLayoutName()), null);
            initSingleView(view, (BoardChild) data.get(i), i == len + -1);
            containerBox.addView(view);
            i++;
        }
    }

    private void init2Column(LinearLayout containerBox, List<BoardChild> data) {
        if (MCListUtils.isEmpty((List) data)) {
            containerBox.removeAllViews();
            return;
        }
        int len;
        int size = data.size();
        if (size % 2 == 0) {
            len = size / 2;
        } else {
            len = (size / 2) + 1;
        }
        int currentPosition = 0;
        int i = 0;
        while (i < len) {
            View view = this.inflater.inflate(this.resource.getLayoutId(getDoubleLayoutName()), null);
            BoardChild child1 = (BoardChild) data.get(currentPosition);
            currentPosition++;
            BoardChild child2 = null;
            if (currentPosition < size) {
                child2 = (BoardChild) data.get(currentPosition);
                currentPosition++;
            }
            initDoubleView(view, child1, child2, i == len + -1);
            containerBox.addView(view);
            i++;
        }
    }

    protected boolean isCard() {
        return false;
    }
}
