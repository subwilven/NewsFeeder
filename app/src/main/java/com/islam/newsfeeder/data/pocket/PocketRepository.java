package com.islam.newsfeeder.data.pocket;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.pojo.Article;
import com.islam.newsfeeder.pojo.network.Resource;
import com.islam.newsfeeder.pojo.network.PocketResponse;
import com.islam.newsfeeder.pojo.ReadLaterArticle;
import com.islam.newsfeeder.dagger.network.pocket.DaggerNetworkPocketComponent;
import com.islam.newsfeeder.dagger.network.pocket.NetworkPocketComponent;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.NetworkUtils;
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.islam.newsfeeder.util.Constants.ERROR_NO_CONNECTION;
import static com.islam.newsfeeder.util.Constants.KEY_ACCESS_TOKEN;
import static com.islam.newsfeeder.util.Constants.KEY_REQUEST_TOKEN;
import static com.islam.newsfeeder.util.Constants.redirectUri;

public class PocketRepository {

    MutableLiveData<Boolean> accessTokenListner;

    public PocketRepository() {}


    public LiveData<String> login() {
        MutableLiveData<String> result = new MutableLiveData<>();

        NetworkPocketComponent component = DaggerNetworkPocketComponent.create();
        PocketApi pocketApi = component.getPocketApi();

        Call<PocketResponse.RequestTokenResponse> connection = pocketApi.requestToken(
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

        String requestToken = PreferenceUtils.getPocketData(MyApplication.getInstance().getApplicationContext(), KEY_REQUEST_TOKEN);

        NetworkPocketComponent component = DaggerNetworkPocketComponent.create();
        PocketApi pocketApi = component.getPocketApi();

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

        String accessToken = PreferenceUtils.getPocketData(context, KEY_ACCESS_TOKEN);

        NetworkPocketComponent component = DaggerNetworkPocketComponent.create();
        PocketApi pocketApi = component.getPocketApi();

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

            String accessToken = PreferenceUtils.getPocketData(MyApplication.getInstance().getApplicationContext(), KEY_ACCESS_TOKEN);

            NetworkPocketComponent component = DaggerNetworkPocketComponent.create();
            PocketApi pocketApi = component.getPocketApi();

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
