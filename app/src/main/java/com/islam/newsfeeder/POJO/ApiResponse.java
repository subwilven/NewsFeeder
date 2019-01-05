package com.islam.newsfeeder.POJO;

import android.support.annotation.Nullable;

public final class ApiResponse<V> {

    @Nullable
    private V articles;

    private String status;

    private String totalResults;

    @Nullable
    public V getData() {
        return articles;
    }

    public void setData(@Nullable V data) {
        this.articles = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
