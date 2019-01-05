package com.islam.newsfeeder.POJO;

import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("id")
    private String sourceId;
    @SerializedName("name")
    private String name;

    private boolean isChecked;

    public Provider(String sourceId, String name, boolean isChecked) {
        this.sourceId = sourceId;
        this.name = name;
        this.isChecked = isChecked;
    }

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
