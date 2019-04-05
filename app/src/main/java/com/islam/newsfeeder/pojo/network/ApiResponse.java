package com.islam.newsfeeder.pojo.network;

import android.support.annotation.Nullable;

public abstract class ApiResponse<V> {

    private String status;

    private String totalResults;

    @Nullable
    public abstract V getData();

    public abstract void setData(@Nullable V data);

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
