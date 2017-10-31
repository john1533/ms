package com.mobcent.lowest.module.ad.cache;

import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdStateModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdPositionsCache implements AdConstant {
    private int cycle;
    private int[] dt1;
    private int[] dt2;
    private int[] dt3;
    private int[] dt4;
    private int[] dt5;
    private int[] dt6;
    private int[] dt7;
    private boolean isConectFailed = false;

    public boolean isConectFailed() {
        return this.isConectFailed;
    }

    public void setConectFailed(boolean isConectFailed) {
        this.isConectFailed = isConectFailed;
    }

    public int[] getDt1() {
        return this.dt1;
    }

    public void setDt1(int[] dt1) {
        this.dt1 = dt1;
    }

    public int[] getDt2() {
        return this.dt2;
    }

    public void setDt2(int[] dt2) {
        this.dt2 = dt2;
    }

    public int[] getDt3() {
        return this.dt3;
    }

    public void setDt3(int[] dt3) {
        this.dt3 = dt3;
    }

    public int[] getDt4() {
        return this.dt4;
    }

    public void setDt4(int[] dt4) {
        this.dt4 = dt4;
    }

    public int[] getDt5() {
        return this.dt5;
    }

    public void setDt5(int[] dt5) {
        this.dt5 = dt5;
    }

    public int[] getDt6() {
        return this.dt6;
    }

    public void setDt6(int[] dt6) {
        this.dt6 = dt6;
    }

    public int[] getDt7() {
        return this.dt7;
    }

    public void setDt7(int[] dt7) {
        this.dt7 = dt7;
    }

    public int getCycle() {
        return this.cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String toString() {
        return "AdPositionsCache [dt1=" + Arrays.toString(this.dt1) + ", dt2=" + Arrays.toString(this.dt2) + ", dt3=" + Arrays.toString(this.dt3) + ", cycle=" + this.cycle + "]";
    }

    public AdStateModel getAdState(int position) {
        AdStateModel adStateModel = haveAd(this.dt1, 1, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt2, 2, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt3, 3, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt4, 4, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt5, 5, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt6, 6, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        adStateModel = haveAd(this.dt7, 7, position);
        if (adStateModel != null) {
            return adStateModel;
        }
        return null;
    }

    public List<AdStateModel> getAdStates(int[] positions) {
        if (positions == null) {
            return null;
        }
        List<AdStateModel> adStateModels = new ArrayList();
        for (int adState : positions) {
            AdStateModel adStateModel = getAdState(adState);
            if (adStateModel != null) {
                adStateModels.add(adStateModel);
            }
        }
        return adStateModels;
    }

    private boolean isEmpty(int[] dts) {
        if (dts == null || dts.length == 0) {
            return true;
        }
        return false;
    }

    private AdStateModel haveAd(int[] dt, int dtType, int position) {
        if (isEmpty(dt)) {
            return null;
        }
        for (int i : dt) {
            if (i == position) {
                AdStateModel adStateModel = new AdStateModel();
                adStateModel.setDtType(dtType);
                adStateModel.setHaveAd(true);
                adStateModel.setAdPosition(position);
                return adStateModel;
            }
        }
        return null;
    }

    public int[] isHaveAd(int[] positions) {
        int i;
        List<Integer> positionList = new ArrayList();
        for (i = 0; i < positions.length; i++) {
            if (isPositionsHaveAd(positions[i])) {
                positionList.add(Integer.valueOf(positions[i]));
            }
        }
        int[] pos = new int[positionList.size()];
        for (i = 0; i < positionList.size(); i++) {
            pos[i] = ((Integer) positionList.get(i)).intValue();
        }
        return pos;
    }

    private boolean isPositionsHaveAd(int position) {
        return isInDt(this.dt1, position) || isInDt(this.dt2, position) || isInDt(this.dt3, position) || isInDt(this.dt4, position) || isInDt(this.dt5, position) || isInDt(this.dt6, position) || isInDt(this.dt7, position);
    }

    private boolean isInDt(int[] dt, int position) {
        if (isEmpty(dt)) {
            return false;
        }
        for (int i : dt) {
            if (position == i) {
                return true;
            }
        }
        return false;
    }
}
