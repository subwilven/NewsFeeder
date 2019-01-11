package com.islam.newsfeeder.POJO.network;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

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

    class SavedArticlesResponse {
        public Map<String, ReadLaterArticle> list;
    }


}
