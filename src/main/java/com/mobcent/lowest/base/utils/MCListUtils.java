package com.mobcent.lowest.base.utils;

import android.util.SparseArray;
import java.util.List;

public class MCListUtils {
    public static boolean isEmpty(List<?> data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(SparseArray<?> data) {
        if (data == null || data.size() == 0) {
            return true;
        }
        return false;
    }
}
