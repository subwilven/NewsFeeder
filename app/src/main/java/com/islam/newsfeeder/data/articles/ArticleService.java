package com.islam.newsfeeder.data.articles;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.network.ArticleResponse;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.NetworkUtils;
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleService {

    private static ArticleService INSTANCE;

    private ArticleService() {
    }

    public static ArticleService getInstance() {
        if (INSTANCE == null)
            synchronized (ArticleService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleService();
                }
            }

        return INSTANCE;
    }

    public void fetchArticles(int pageNumber, CallBacks.NetworkCallBack callBack) {

        ArticleApi articleApi = NetworkUtils.getArticleApi();
        List<Provider> providers = PreferenceUtils.getProvidersFromShared(MyApplication.getInstance().getApplicationContext());
        String sources = ActivityUtils.convertProvidersToString(providers);

        Call<ArticleResponse> connection = articleApi.getArticles(
                "top-headlines",
                Constants.PAGE_SIZE_NETWORK,
                pageNumber,
                sources,
                BuildConfig.NEWS_API_KEY);

        connection.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                callBack.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
    }

}
