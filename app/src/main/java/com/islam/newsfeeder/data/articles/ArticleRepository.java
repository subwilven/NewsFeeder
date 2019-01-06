package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                mArticleDao.clearAllData();
                mArticleDao.insert(articles);
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

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);

                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.basicUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                ArticleApi articleApi = retrofit.create(ArticleApi.class);
                List<Provider> providers = PreferenceUtils.getProvidersFromShared(MyApplication.getInstance().getApplicationContext());
                String sources = convertProvidersToString(providers);

                Call<ArticleResponse> connection = articleApi.getArticles(
                        "top-headlines",
                        sources,
                        BuildConfig.NEWS_API_KEY);

                connection.enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable t) {

                    }
                });
                return data;
            }
        }.getAsLiveData();
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

    public void getProviders(CallBacks.NetworkCallBack<List<Provider>> callBack) {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.basicUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ArticleApi articleApi = retrofit.create(ArticleApi.class);
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



