package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.ApiResponse;
import com.islam.newsfeeder.POJO.network.ArticleResponse;
import com.islam.newsfeeder.POJO.network.ProvidersResponse;
import com.islam.newsfeeder.data.server_connection_helper.NetworkBoundResource;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.NetworkUtils;
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {

    private static ArticleRepository INSTANCE = null;
    private final ArticleDao mArticleDao;

    private ArticleRepository(ArticleDao articleDao) {
        this.mArticleDao = articleDao;
    }

    public static ArticleRepository getInstance(ArticleDao articleDao) {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleRepository(articleDao);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<Resource<List<Article>>> getArticles() {
        return new NetworkBoundResource<List<Article>>(MyApplication.getInstance()) {
            @Override
            protected void saveToDatabase(@NonNull List<Article> articles) {
                saveArticlesToDataBase(articles);
            }

            @NonNull
            @Override
            protected LiveData<List<Article>> loadFromDb() {
                return mArticleDao.getAllArticles();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Article>>> serveRequest() {

                final MutableLiveData<ApiResponse<List<Article>>> data = new MutableLiveData<>();
                fetchArticles(new CallBacks.NetworkCallBack<ArticleResponse>() {
                    @Override
                    public void onSuccess(ArticleResponse item) {
                        data.setValue(item);
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });
                return data;
            }
        }.getAsLiveData();
    }

    public void saveArticlesToDataBase(List<Article> articles) {
        Log.i(ArticleRepository.class.getName(), "UPDATE THE DATA BASE");
        mArticleDao.clearAllData();
        mArticleDao.insert(articles);
    }

    public void fetchArticles(CallBacks.NetworkCallBack callBack) {

        ArticleApi articleApi = NetworkUtils.getArticleApi();
        List<Provider> providers = PreferenceUtils.getProvidersFromShared(MyApplication.getInstance().getApplicationContext());
        String sources = convertProvidersToString(providers);

        Call<ArticleResponse> connection = articleApi.getArticles(
                "top-headlines",
                sources,
                BuildConfig.NEWS_API_KEY);

        connection.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
    }

    private String convertProvidersToString(List<Provider> providers) {
        List<String> resources = new ArrayList<>();
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            if (provider.isChecked()) {
                resources.add(provider.getSourceId());
            }
        }
        return TextUtils.join(",", resources);
    }

    public void fetchAllProviders(CallBacks.NetworkCallBack<List<Provider>> callBack) {

        if (!NetworkUtils.haveNetworkConnection(MyApplication.getInstance().getApplicationContext())) {
            callBack.onFailed(Constants.ERROR_NO_CONNECTION);
        }

        ArticleApi articleApi = NetworkUtils.getArticleApi();
        Call<ProvidersResponse> connection = articleApi.getProviders(
                "sources",
                BuildConfig.NEWS_API_KEY);

        connection.enqueue(new Callback<ProvidersResponse>() {
            @Override
            public void onResponse(Call<ProvidersResponse> call, Response<ProvidersResponse> response) {
                callBack.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<ProvidersResponse> call, Throwable t) {

            }

        });
    }
}



