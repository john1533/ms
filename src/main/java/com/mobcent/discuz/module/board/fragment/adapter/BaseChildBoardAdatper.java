package com.mobcent.discuz.module.board.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.base.adapter.BaseSimpleAdapter;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.List;

public abstract class BaseChildBoardAdatper extends BaseSimpleAdapter<BoardChild> implements BaseIntentConstant {
    public String TAG = "BaseBoardAdatper";
    protected int column = 1;
    protected String style = StyleConstant.STYLE_DEFAULT;

    protected abstract String getItemLayoutName();

    protected abstract void init1Column(Object obj, BoardChild boardChild);

    protected abstract void init2Column(Object obj, BoardChild boardChild, BoardChild boardChild2);

    protected abstract void initViews(View view, Object obj);

    protected abstract Object instanceHolder();

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BaseChildBoardAdatper(Context context, List<BoardChild> datas, String style) {
        super(context, datas);
        this.style = style;
    }

    public int getCount() {
        if (MCListUtils.isEmpty(this.datas)) {
            return 0;
        }
        if (this.column == 1) {
            return this.datas.size();
        }
        int size = this.datas.size();
        return size % 2 == 0 ? size / 2 : (size / 2) + 1;
    }

    public BoardChild getItem(int position) {
        return (BoardChild) this.datas.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Object holder;
        BoardChild data = getItem(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getItemLayoutName()), null);
            holder = instanceHolder();
            initViews(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = convertView.getTag();
        }
        initViewDatas(holder, data, position);
        return convertView;
    }

    protected void initViewDatas(Object holder, BoardChild data, int position) {
        if (this.column == 1) {
            init1Column(holder, data);
            return;
        }
        int currentPosition = position * 2;
        BoardChild leftChild = (BoardChild) this.datas.get(currentPosition);
        BoardChild rightChild = null;
        currentPosition++;
        if (currentPosition < this.datas.size()) {
            rightChild = (BoardChild) this.datas.get(currentPosition);
        }
        init2Column(holder, leftChild, rightChild);
    }
}
