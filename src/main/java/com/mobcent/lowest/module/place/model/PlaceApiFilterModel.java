package com.mobcent.lowest.module.place.model;

import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.constant.BasePlaceApiConstant;

public class PlaceApiFilterModel extends BasePlaceModel implements BasePlaceApiConstant {
    private static final long serialVersionUID = -4173557736115039482L;
    private int discount = -1;
    private int groupon = -1;
    private String industryType;
    private String priceSection;
    private String sortName;
    private int sortRule = -1;

    public void clearFilter() {
        this.industryType = null;
        this.sortName = null;
        this.sortRule = -1;
        this.priceSection = null;
        this.groupon = -1;
        this.discount = -1;
    }

    public String getIndustryType() {
        return this.industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getSortName() {
        return this.sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getSortRule() {
        return this.sortRule;
    }

    public void setSortRule(int sortRule) {
        this.sortRule = sortRule;
    }

    public String getPriceSection() {
        return this.priceSection;
    }

    public void setPriceSection(String priceSection) {
        this.priceSection = priceSection;
    }

    public int getGroupon() {
        return this.groupon;
    }

    public void setGroupon(int groupon) {
        this.groupon = groupon;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getFilterString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getParamsRequestString(BasePlaceApiConstant.REQ_INDUSTRY_TYPE, this.industryType));
        sb.append(getParamsRequestString(BasePlaceApiConstant.REQ_SORT_NAME, this.sortName));
        if (this.sortRule != -1) {
            sb.append(getParamsRequestInt(BasePlaceApiConstant.REQ_SORT_RULE, this.sortRule));
        }
        sb.append(getParamsRequestString(BasePlaceApiConstant.REQ_PRICE_SECTION, this.priceSection));
        sb.append(getParamsRequestInt(BasePlaceApiConstant.REQ_GROUPON, this.groupon));
        sb.append(getParamsRequestInt(BasePlaceApiConstant.REQ_DISCOUNT, this.discount));
        sb.append(getParamsRequestInt(this.sortName, 1));
        return sb.toString();
    }

    private String getParamsRequestString(String key, String value) {
        String str = "";
        if (MCStringUtil.isEmpty(value)) {
            return str;
        }
        return new StringBuilder(String.valueOf(key)).append(":").append(value).append("|").toString();
    }

    private String getParamsRequestInt(String key, int value) {
        String str = "";
        if (value != -1) {
            return new StringBuilder(String.valueOf(key)).append(":").append(value).append("|").toString();
        }
        return str;
    }
}
