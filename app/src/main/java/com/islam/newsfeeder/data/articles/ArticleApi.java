package com.islam.newsfeeder.data.articles;

import com.islam.newsfeeder.POJO.ApiResponse;
import com.islam.newsfeeder.POJO.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {

    @GET("top-headlines?country=us&category=business")
    Call<ApiResponse<List<Article>>> getArticles(@Query("apiKey") String key);

    Call<ApiResponse> get(@Path(value = "path", encoded = true) String path2,
                          @Query("apiKey") String key);
}
