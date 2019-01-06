package com.islam.newsfeeder.data.articles;

import com.islam.newsfeeder.POJO.network.ArticleResponse;
import com.islam.newsfeeder.POJO.network.ProvidersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {

    @GET("{path}")
    Call<ArticleResponse> getArticles(
            @Path(value = "path") String path,
            @Query(value = "sources", encoded = true) String sources,
            @Query("apiKey") String key);

    @GET("{path}")
    Call<ProvidersResponse> getProviders(
            @Path(value = "path") String path,
            @Query("apiKey") String key);
}
