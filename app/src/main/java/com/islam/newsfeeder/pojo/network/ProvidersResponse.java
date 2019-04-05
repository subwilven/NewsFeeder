package com.islam.newsfeeder.pojo.network;

import android.support.annotation.Nullable;

import com.islam.newsfeeder.pojo.Provider;

import java.util.List;

public class ProvidersResponse extends ApiResponse<List<Provider>> {

    private List<Provider> sources;

    @Nullable
    @Override
    public List<Provider> getData() {
        return sources;
    }

    @Override
    public void setData(@Nullable List<Provider> data) {
        sources = data;

    }

}