package com.islam.newsfeeder.POJO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Provider implements Serializable {

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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Objects.equals(sourceId, provider.sourceId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
