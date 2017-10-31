package com.mobcent.discuz.base.adapter;

import java.util.List;

public class WheelListAdapter implements WheelAdapter {
    private List<String> list;

    public WheelListAdapter(List<String> list) {
        this.list = list;
    }

    public int getItemsCount() {
        return this.list.size();
    }

    public String getItem(int index) {
        return (String) this.list.get(index);
    }

    public int getMaximumLength() {
        return this.list.size();
    }
}
