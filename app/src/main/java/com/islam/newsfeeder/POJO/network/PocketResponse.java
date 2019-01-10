package com.islam.newsfeeder.POJO.network;

import com.google.gson.annotations.SerializedName;

public interface PocketResponse {

    class RequestTokenResponse {
        public String code;
    }

    class AccessTokenResponse {
        @SerializedName("access_token")
        public String accessToken;
        public String username;
    }

    class AddArticleResponse {
        public int status;
    }
}
