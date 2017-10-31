package com.mobcent.lowest.module.place.model;

import java.io.Serializable;

public class PlaceQueryModel implements Serializable {
    private static final long serialVersionUID = 4386642150730089450L;
    private String drawableName;
    private String query;
    private String queryType;

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryType() {
        return this.queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDrawableName() {
        return this.drawableName;
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }
}
