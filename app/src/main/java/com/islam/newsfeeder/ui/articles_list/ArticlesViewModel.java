package com.islam.newsfeeder.ui.articles_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.islam.newsfeeder.pojo.Article;
import com.islam.newsfeeder.pojo.network.NetworkState;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

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
        mRepository.clearAllData();
        mArticles = mRepository.getArticles();
        mNetworkState = mRepository.getNetworkState();
    }

    public LiveData<PagedList<Article>> getArticles() {
        return mArticles;
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public void reload() {
        mRepository.clearAllData();
    }
}
