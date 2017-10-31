package com.mobcent.discuz.module.person.activity.helper;

import java.util.ArrayList;
import java.util.List;

public class InTimeHelper {
    private static InTimeHelper inTimeHelper;
    private List<InTimeDelegate> delegates = new ArrayList();

    public interface InTimeDelegate {
        void refresh();
    }

    private InTimeHelper() {
    }

    public static synchronized InTimeHelper getInstance() {
        synchronized (InTimeHelper.class) {
            if (inTimeHelper == null) {
                inTimeHelper = new InTimeHelper();
            }
        }
        return inTimeHelper;
    }

    public void add(InTimeDelegate delegate) {
        this.delegates.add(delegate);
    }

    public void remove(InTimeDelegate delegate) {
        this.delegates.remove(delegate);
    }

    public void notifyRefresh() {
        for (InTimeDelegate delegate : this.delegates) {
            delegate.refresh();
        }
    }
}
