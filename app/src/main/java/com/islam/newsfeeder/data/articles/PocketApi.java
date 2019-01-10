package com.islam.newsfeeder.data.articles;

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
    Call<PocketResponse.TokenResponse> requestToken(@Field("consumer_key") String code,
                                                    @Field("redirect_uri") String grantType);


}
