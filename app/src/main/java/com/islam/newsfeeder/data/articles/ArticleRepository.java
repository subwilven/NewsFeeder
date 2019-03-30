package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.NetworkState;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.network.ProvidersResponse;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.NetworkUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {

    private static ArticleRepository INSTANCE = null;
    private final ArticleDao mArticleDao;
    private final ArticleService mAarticleService;

    private MutableLiveData<NetworkState> mNetworkState = new MutableLiveData<>();

    private ArticleRepository(ArticleService articleService, ArticleDao articleDao) {
        this.mArticleDao = articleDao;
        mAarticleService = articleService;
    }

    public static ArticleRepository getInstance(ArticleService articleService, ArticleDao articleDao) {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleRepository(articleService, articleDao);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<PagedList<Article>> getArticles() {
        BoundaryCallback boundaryCallback = new BoundaryCallback(mAarticleService, mArticleDao, mNetworkState);

        return new LivePagedListBuilder(mArticleDao.getAllArticles(), Constants.PAGE_SIZE_DATABASE)
                .setBoundaryCallback(boundaryCallback)
                .build();
    }

    public void clearAllData() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mArticleDao.clearAllData();
            }
        });

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

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

}



