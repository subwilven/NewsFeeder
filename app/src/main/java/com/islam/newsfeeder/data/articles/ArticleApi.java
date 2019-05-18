package com.islam.newsfeeder.data.articles;

import com.islam.newsfeeder.pojo.network.ArticleResponse;
import com.islam.newsfeeder.pojo.network.ProvidersResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {

    @GET("{path}")
    Call<ArticleResponse> getArticles(
            @Path(value = "path") String path,
            @Query(value = "pageSize") int pageSize,
            @Query(value = "page") int pageNumber,
            @Query(value = "sources", encoded = true) String sources,
            @Query("apiKey") String key);

    @GET("{path}")
    Single<ProvidersResponse> getProviders(
            @Path(value = "path") String path,
            @Query("apiKey") String key);
}
