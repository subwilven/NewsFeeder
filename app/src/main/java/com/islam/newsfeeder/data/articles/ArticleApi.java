package com.islam.newsfeeder.data.articles;

import com.islam.newsfeeder.POJO.ApiResponse;
import com.islam.newsfeeder.POJO.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {

    @GET("{path}")
    Call<ApiResponse<List<Article>>> getArticles(
            @Path(value = "path") String path,
            @Query(value = "sources", encoded = true) String sources,
            @Query("apiKey") String key);

}
