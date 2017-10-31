package com.mobcent.lowest.module.ad.cache;

import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.model.AdStateModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdDatasCache implements AdConstant {
    public String TAG = "AdDatasCache";
    private Map<Integer, HashMap<Long, AdModel>> adDataMap = new HashMap();
    private long[] gids = null;
    private boolean requestSucc;
    private Integer[] types = new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)};

    public boolean isRequestSucc() {
        return this.requestSucc;
    }

    public void setRequestSucc(boolean requestSucc) {
        this.requestSucc = requestSucc;
    }

    public Map<Integer, HashMap<Long, AdModel>> getAdDataMap() {
        return this.adDataMap;
    }

    public void setAdDataMap(Map<Integer, HashMap<Long, AdModel>> adDataMap) {
        this.adDataMap = adDataMap;
    }

    public String toString() {
        return "AdDatasCache [adDataMap=" + this.adDataMap + "]";
    }

    public AdContainerModel createAdContainerModel(AdStateModel adStateModel) {
        AdContainerModel adContainerModel;
        switch (adStateModel.getDtType()) {
            case 1:
                adContainerModel = randomMultiViewData(false);
                if (adContainerModel == null) {
                    return randomMultiViewData(true);
                }
                return adContainerModel;
            case 2:
                adContainerModel = randomShortTextViewData(false);
                if (adContainerModel == null) {
                    return randomShortTextViewData(true);
                }
                return adContainerModel;
            case 3:
                adContainerModel = randomBigImgViewData(false);
                if (adContainerModel == null) {
                    return randomBigImgViewData(true);
                }
                return adContainerModel;
            case 4:
                adContainerModel = randomLinkTextViewData(false);
                if (adContainerModel == null) {
                    return randomLinkTextViewData(true);
                }
                return adContainerModel;
            case 5:
                adContainerModel = randomPicAndTextViewData(false);
                if (adContainerModel == null) {
                    return randomPicAndTextViewData(true);
                }
                return adContainerModel;
            case 6:
                adContainerModel = randomSearchPicAndTextViewData(false);
                if (adContainerModel == null) {
                    return randomSearchPicAndTextViewData(true);
                }
                return adContainerModel;
            case 7:
                return randomLinkOrPicText();
            default:
                return null;
        }
    }

    public List<AdContainerModel> createAdContainerModels(List<AdStateModel> adStateModels) {
        List<AdContainerModel> adContainerModels = new ArrayList();
        int count = adStateModels.size();
        this.gids = new long[count];
        for (int i = 0; i < count; i++) {
            AdContainerModel adContainerModel = createAdContainerModel((AdStateModel) adStateModels.get(i));
            if (adContainerModel != null) {
                AdModel adModel = (AdModel) adContainerModel.getAdSet().iterator().next();
                if (!isGidExist(adModel.getGid())) {
                    this.gids[i] = adModel.getGid();
                    adContainerModel.setPosition(((AdStateModel) adStateModels.get(i)).getAdPosition());
                    adContainerModel.setDtType(((AdStateModel) adStateModels.get(i)).getDtType());
                    adContainerModels.add(adContainerModel);
                }
            }
        }
        return adContainerModels.isEmpty() ? null : adContainerModels;
    }

    private AdContainerModel randomLinkOrPicText() {
        int dtType = 4;
        if (new Random().nextInt(2) == 1) {
            dtType = 5;
        }
        AdContainerModel adContainerModel;
        if (dtType == 4) {
            adContainerModel = randomLinkTextViewData(false);
            if (adContainerModel == null) {
                adContainerModel = randomLinkTextViewData(true);
            }
            if (adContainerModel != null) {
                return adContainerModel;
            }
            adContainerModel = randomPicAndTextViewData(false);
            if (adContainerModel == null) {
                return randomPicAndTextViewData(true);
            }
            return adContainerModel;
        }
        adContainerModel = randomPicAndTextViewData(false);
        if (adContainerModel == null) {
            adContainerModel = randomPicAndTextViewData(true);
        }
        if (adContainerModel != null) {
            return adContainerModel;
        }
        adContainerModel = randomLinkTextViewData(false);
        if (adContainerModel == null) {
            return randomLinkTextViewData(true);
        }
        return adContainerModel;
    }

    private AdContainerModel randomMultiViewData(boolean isForce) {
        confuseArray(this.types);
        AdContainerModel multiAdModel = null;
        for (int i = 0; i < this.types.length; i++) {
            multiAdModel = getAdDataByType(this.types[i].intValue(), isForce);
            if (multiAdModel != null) {
                multiAdModel.setType(this.types[i].intValue());
                break;
            }
        }
        return multiAdModel;
    }

    private AdContainerModel randomShortTextViewData(boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(2));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(2)));
        }
        if (adMap == null || adMap.size() < 2) {
            return null;
        }
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        AdContainerModel multiAdModel = new AdContainerModel();
        HashSet<AdModel> adSet = new HashSet();
        adSet.add((AdModel) adMap.get(keys[0]));
        adSet.add((AdModel) adMap.get(keys[1]));
        multiAdModel.setType(7);
        multiAdModel.setAdSet(adSet);
        return multiAdModel;
    }

    private AdContainerModel randomBigImgViewData(boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(9));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(9)));
        }
        if (adMap == null || adMap.isEmpty()) {
            return null;
        }
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        AdContainerModel multiAdModel = new AdContainerModel();
        HashSet<AdModel> adSet = new HashSet();
        adSet.add((AdModel) adMap.get(keys[0]));
        multiAdModel.setType(9);
        multiAdModel.setAdSet(adSet);
        return multiAdModel;
    }

    private AdContainerModel randomLinkTextViewData(boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(11));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(11)));
        }
        if (adMap == null || adMap.size() < 1) {
            return null;
        }
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        AdContainerModel multiAdModel = new AdContainerModel();
        HashSet<AdModel> adSet = new HashSet();
        adSet.add((AdModel) adMap.get(keys[0]));
        multiAdModel.setType(11);
        multiAdModel.setAdSet(adSet);
        return multiAdModel;
    }

    private AdContainerModel randomPicAndTextViewData(boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(12));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(12)));
        }
        if (adMap == null || adMap.size() < 1) {
            return null;
        }
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        AdContainerModel multiAdModel = new AdContainerModel();
        HashSet<AdModel> adSet = new HashSet();
        adSet.add((AdModel) adMap.get(keys[0]));
        multiAdModel.setType(12);
        multiAdModel.setAdSet(adSet);
        return multiAdModel;
    }

    private AdContainerModel randomSearchPicAndTextViewData(boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(13));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(13)));
        }
        if (adMap == null || adMap.size() < 1) {
            return null;
        }
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        AdContainerModel multiAdModel = new AdContainerModel();
        HashSet<AdModel> adSet = new HashSet();
        adSet.add((AdModel) adMap.get(keys[0]));
        multiAdModel.setType(13);
        multiAdModel.setAdSet(adSet);
        return multiAdModel;
    }

    private AdContainerModel getAdDataByType(int type, boolean isForce) {
        HashMap<Long, AdModel> adMap;
        if (isForce) {
            adMap = (HashMap) this.adDataMap.get(Integer.valueOf(type));
        } else {
            adMap = getUnConsumedMap((HashMap) this.adDataMap.get(Integer.valueOf(type)));
        }
        if (adMap == null || !hasEnoughData(adMap.size(), type)) {
            return null;
        }
        AdContainerModel multiAdModel = new AdContainerModel();
        multiAdModel.setAdSet(getMultiData(type));
        multiAdModel.setType(type);
        return multiAdModel;
    }

    private boolean hasEnoughData(int count, int type) {
        if (type == 1 || type == 3) {
            return count >= 1;
        } else {
            if (type == 4) {
                boolean enough;
                if (count >= 2) {
                    enough = true;
                } else {
                    enough = false;
                }
                return enough;
            }
            return count >= 3;
        }
    }

    private HashSet<AdModel> getMultiData(int type) {
        int count;
        HashSet<AdModel> adSet = new HashSet();
        if (type == 1 || type == 3) {
            count = 1;
        } else if (type == 4) {
            count = 2;
        } else {
            count = 3;
        }
        HashMap<Long, AdModel> adMap = (HashMap) this.adDataMap.get(Integer.valueOf(type));
        Object[] keys = adMap.keySet().toArray();
        confuseArray(keys);
        for (int i = 0; i < count; i++) {
            adSet.add((AdModel) adMap.get(keys[i]));
        }
        return adSet;
    }

    private HashMap<Long, AdModel> getUnConsumedMap(HashMap<Long, AdModel> adMap) {
        HashMap<Long, AdModel> unConsumedMap = new HashMap();
        if (!(adMap == null || adMap.isEmpty())) {
            Object[] keys = adMap.keySet().toArray();
            confuseArray(keys);
            for (Object key : keys) {
                AdModel adModel = (AdModel) adMap.get(key);
                if (!(adModel.isConsumed() || isHaveSameGidAd(unConsumedMap, adModel))) {
                    unConsumedMap.put((Long) key, adModel);
                }
            }
        }
        return unConsumedMap;
    }

    private boolean isHaveSameGidAd(HashMap<Long, AdModel> adMap, AdModel adModel) {
        for (Long key : adMap.keySet()) {
            if (((AdModel) adMap.get(key)).getGid() == adModel.getGid()) {
                return true;
            }
        }
        return isGidExist(adModel.getGid());
    }

    private boolean isGidExist(long gid) {
        if (this.gids != null) {
            for (long j : this.gids) {
                if (gid == j) {
                    return true;
                }
            }
        }
        return false;
    }

    private void confuseArray(Object[] args) {
        int length = args.length;
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int index = r.nextInt(length);
            Object temp = args[i];
            args[i] = args[index];
            args[index] = temp;
        }
    }
}
