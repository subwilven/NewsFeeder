package com.islam.newsfeeder.data.pocket;

import com.islam.newsfeeder.POJO.network.PocketResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PocketApi {

    @Headers("X-Accept: application/json")
    @FormUrlEncoded
    @POST("oauth/request")
    Call<PocketResponse.RequestTokenResponse> requestToken(@Field("consumer_key") String code,
                                                           @Field("redirect_uri") String grantType);


    @Headers("X-Accept: application/json")
    @FormUrlEncoded
    @POST("oauth/authorize")
    Call<PocketResponse.AccessTokenResponse> getAccessToken(@Field("consumer_key") String consumerKey,
                                                            @Field("code") String code);

    @Headers("X-Accept: application/json")
    @FormUrlEncoded
    @POST("add")
    Call<PocketResponse.AddArticleResponse> addArticleToReadLater(@Field("consumer_key") String consumerKey,
                                                                  @Field("access_token") String accessToken,
                                                                  @Field("title") String title,
                                                                  @Field("url") String url);
}
