package com.islam.newsfeeder.data.pocket;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.PocketResponse;
import com.islam.newsfeeder.POJO.network.ReadLaterArticle;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.NetworkUtils;
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.islam.newsfeeder.util.Constants.ERROR_NO_CONNECTION;
import static com.islam.newsfeeder.util.Constants.KEY_ACCESS_TOKEN;
import static com.islam.newsfeeder.util.Constants.KEY_REQUEST_TOKEN;
import static com.islam.newsfeeder.util.Constants.redirectUri;

public class PocketRepository {

    private static PocketRepository INSTANCE = null;
    MutableLiveData<Boolean> accessTokenListner;

    private PocketRepository() {
    }

    public static PocketRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PocketRepository();
                }
            }
        }
        return INSTANCE;
    }


    public LiveData<String> login() {
        MutableLiveData<String> result = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PocketApi articleApi = retrofit.create(PocketApi.class);
        Call<PocketResponse.RequestTokenResponse> connection = articleApi.requestToken(
                BuildConfig.KEY_POCKET_CONSUMER, redirectUri);

        connection.enqueue(new Callback<PocketResponse.RequestTokenResponse>() {
            @Override
            public void onResponse(Call<PocketResponse.RequestTokenResponse> call, Response<PocketResponse.RequestTokenResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body().code);
                    PreferenceUtils.savePocketData(MyApplication.getInstance().getApplicationContext(),
                            KEY_REQUEST_TOKEN, response.body().code);
                }
            }

            @Override
            public void onFailure(Call<PocketResponse.RequestTokenResponse> call, Throwable t) {

            }

        });
        return result;
    }

    public void getAccessToken() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String requestToken = PreferenceUtils.getPocketData(MyApplication.getInstance().getApplicationContext(), KEY_REQUEST_TOKEN);
        PocketApi pocketApi = retrofit.create(PocketApi.class);
        Call<PocketResponse.AccessTokenResponse> token
                = pocketApi.getAccessToken(BuildConfig.KEY_POCKET_CONSUMER, requestToken);

        token.enqueue(new Callback<PocketResponse.AccessTokenResponse>() {
            @Override
            public void onResponse(Call<PocketResponse.AccessTokenResponse> call, Response<PocketResponse.AccessTokenResponse> response) {
                if (response.isSuccessful()) {
                    Context context = MyApplication.getInstance().getApplicationContext();
                    PreferenceUtils.savePocketData(context, KEY_ACCESS_TOKEN, response.body().accessToken);
                    // to tell the fragment to start listen on articles live data
                    if (accessTokenListner != null)
                        accessTokenListner.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PocketResponse.AccessTokenResponse> call, Throwable t) {

            }
        });
    }

    public void addArticleToReadLater(Article article, CallBacks.NetworkCallBack callBack) {
        Context context = MyApplication.getInstance().getApplicationContext();
        //check internet connection
        if (!NetworkUtils.haveNetworkConnection(context)) {
            callBack.onFailed(ERROR_NO_CONNECTION);
            return;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String accessToken = PreferenceUtils.getPocketData(context, KEY_ACCESS_TOKEN);
        PocketApi pocketApi = retrofit.create(PocketApi.class);

        Call<PocketResponse.AddArticleResponse> token = pocketApi.
                addArticleToReadLater(BuildConfig.KEY_POCKET_CONSUMER,
                        accessToken,
                        article.getTitle(),
                        article.getArticleUrl());

        token.enqueue(new Callback<PocketResponse.AddArticleResponse>() {
            @Override
            public void onResponse(Call<PocketResponse.AddArticleResponse> call, Response<PocketResponse.AddArticleResponse> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<PocketResponse.AddArticleResponse> call, Throwable t) {

            }
        });
    }


    public LiveData<Resource<List<ReadLaterArticle>>> fetchReadLaterArticles() {
        MutableLiveData<Resource<List<ReadLaterArticle>>> list = new MutableLiveData<>();

        if (!NetworkUtils.haveNetworkConnection(MyApplication.getInstance().getApplicationContext())) {
            list.setValue(Resource.success(null, false));

        } else {
            list.setValue(Resource.loading(null));
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String accessToken = PreferenceUtils.getPocketData(MyApplication.getInstance().getApplicationContext(), KEY_ACCESS_TOKEN);
            PocketApi pocketApi = retrofit.create(PocketApi.class);

            Call<PocketResponse.SavedArticlesResponse> token = pocketApi.getReadLaterArticles(BuildConfig.KEY_POCKET_CONSUMER,
                    accessToken);

            token.enqueue(new Callback<PocketResponse.SavedArticlesResponse>() {
                @Override
                public void onResponse(Call<PocketResponse.SavedArticlesResponse> call, Response<PocketResponse.SavedArticlesResponse> response) {
                    list.setValue(Resource.success(new ArrayList(response.body().list.values()), true));
                }

                @Override
                public void onFailure(Call<PocketResponse.SavedArticlesResponse> call, Throwable t) {

                }
            });
        }
        return list;
    }

    public void setAccessTokenListner(MutableLiveData<Boolean> accessTokenListner) {
        this.accessTokenListner = accessTokenListner;
    }
}
