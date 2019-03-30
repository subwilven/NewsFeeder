package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.NetworkState;
import com.islam.newsfeeder.util.CallBacks;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BoundaryCallback extends PagedList.BoundaryCallback<Article> {

    private ArticleService mArticleService;
    private ArticleDao mArticleDao;
    private MutableLiveData<NetworkState> mNetworkState;

    // keep the last requested page. When the request is successful, increment the page number.
    private int lastRequestedPage = 1;

    // to avoid triggering multiple requests in the same time
    private boolean isRequestInProgress = false;

    public BoundaryCallback(ArticleService articleService, ArticleDao articleDao,MutableLiveData<NetworkState> mNetworkState) {
        mArticleDao = articleDao;
        mArticleService = articleService;
        this.mNetworkState =mNetworkState;
        mNetworkState.setValue(NetworkState.SUCCESS());
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    @Override
    public void onZeroItemsLoaded() {
        lastRequestedPage = 1;
        requestAndSaveData();
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within it
     * <p>
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Article itemAtEnd) {
        requestAndSaveData();
    }

    private void requestAndSaveData() {
        if (isRequestInProgress) return;
        mNetworkState.setValue(NetworkState.LOADING());
        isRequestInProgress = true;
            fetchArticles();


    }


    private void fetchArticles() {
        mArticleService.fetchArticles(lastRequestedPage, new CallBacks.NetworkCallBack<List<Article>>() {
            @Override
            public void onSuccess(List<Article> items) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mArticleDao.insert(items);
                        mNetworkState.postValue(NetworkState.SUCCESS());
                        lastRequestedPage++;
                        isRequestInProgress = false;
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                isRequestInProgress = false;
                mNetworkState.setValue(NetworkState.FAILED());
            }
        });
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }
}
