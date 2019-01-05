package com.islam.newsfeeder.POJO;

import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("id")
    private String sourceId;
    @SerializedName("name")
    private String name;

    public Provider() {
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
