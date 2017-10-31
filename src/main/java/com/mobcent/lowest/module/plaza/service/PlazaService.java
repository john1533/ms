package com.mobcent.lowest.module.plaza.service;

import com.mobcent.lowest.base.model.LowestResultModel;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.List;

public interface PlazaService {
    LowestResultModel<List<PlazaAppModel>> getPlazaAppModelListByLocal();

    LowestResultModel<List<PlazaAppModel>> getPlazaAppModelListByNet(String str, long j);

    String getPlazaLinkUrl(String str, long j, long j2);
}
