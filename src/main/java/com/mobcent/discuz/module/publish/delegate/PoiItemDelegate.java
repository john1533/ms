package com.mobcent.discuz.module.publish.delegate;

public class PoiItemDelegate {
    private static final PoiItemDelegate delegate = new PoiItemDelegate();
    private ClickPoiItemLisenter clickPoiItemLisenter;

    public interface ClickPoiItemLisenter {
        void clickItem(String str);
    }

    private PoiItemDelegate() {
    }

    public static PoiItemDelegate getInstance() {
        return delegate;
    }

    public void setClickPoiItemLisenter(ClickPoiItemLisenter clickPoiItemLisenter) {
        this.clickPoiItemLisenter = clickPoiItemLisenter;
    }

    public ClickPoiItemLisenter getClickPoiItemLisenter() {
        return this.clickPoiItemLisenter;
    }
}
