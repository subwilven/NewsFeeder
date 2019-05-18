package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.pojo.Article;
import com.islam.newsfeeder.pojo.network.NetworkState;
import com.islam.newsfeeder.pojo.Provider;
import com.islam.newsfeeder.pojo.network.ProvidersResponse;
import com.islam.newsfeeder.dagger.network.article.DaggerNetworkArticleComponent;
import com.islam.newsfeeder.dagger.network.article.NetworkArticleComponent;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.NetworkUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {

    private final ArticleDao mArticleDao;
    private final ArticleService mAarticleService;

    private MutableLiveData<NetworkState> mNetworkState = new MutableLiveData<>();

    public ArticleRepository(ArticleService articleService, ArticleDao articleDao) {
        this.mArticleDao = articleDao;
        mAarticleService = articleService;
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

    public Single<ProvidersResponse> fetchAllProviders() {

//        if (!NetworkUtils.haveNetworkConnection(MyApplication.getInstance().getApplicationContext())) {
//            callBack.onFailed(Constants.ERROR_NO_CONNECTION);
//        }

        NetworkArticleComponent component = MyApplication.getInstance().getNetworkArticleComponent();
        ArticleApi articleApi = component.getArticleApi();

        return articleApi.getProviders("sources", BuildConfig.NEWS_API_KEY);
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

}



