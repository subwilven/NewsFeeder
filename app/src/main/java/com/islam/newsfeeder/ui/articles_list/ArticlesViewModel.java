package com.islam.newsfeeder.ui.articles_list;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.NetworkState;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticlesViewModel extends ViewModel {

    private final ArticleRepository mRepository;
    //here is the articles
    private LiveData<PagedList<Article>> mArticles;
    // useed to indicate the the data should be updated ( in case there are changes in providers or the user force reload)
    private SingleLiveEvent<Boolean> shouldReload = new SingleLiveEvent<>();

    private LiveData<NetworkState> mNetworkState;

    public ArticlesViewModel(ArticleRepository mArticleRepository) {
        mRepository = mArticleRepository;
        loadArticles();
    }

    private void loadArticles() {
        shouldReload.setValue(true);
        if (mArticles != null) {
            return;
        }
        mArticles = mRepository.getArticles();
        mArticles = Transformations.switchMap(shouldReload, new Function<Boolean, LiveData<PagedList<Article>>>() {
            @Override
            public LiveData<PagedList<Article>> apply(Boolean input) {
                if (input)
                    return mRepository.getArticles();
                return mArticles;
            }
        });
        mNetworkState = mRepository.getNetworkState();
    }

    public LiveData<PagedList<Article>> getArticles() {
        return mArticles;
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public void reload() {
        shouldReload.setValue(true);
    }
}
