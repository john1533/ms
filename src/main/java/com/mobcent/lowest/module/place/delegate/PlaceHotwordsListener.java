package com.mobcent.lowest.module.place.delegate;

import com.mobcent.lowest.module.place.model.PlaceHotNavModel;
import java.util.List;

public interface PlaceHotwordsListener {
    void onResult(List<PlaceHotNavModel> list);
}
