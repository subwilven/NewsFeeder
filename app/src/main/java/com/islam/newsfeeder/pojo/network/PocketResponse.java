package com.islam.newsfeeder.pojo.network;

import com.google.gson.annotations.SerializedName;
import com.islam.newsfeeder.pojo.ReadLaterArticle;

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
