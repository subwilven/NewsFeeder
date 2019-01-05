package com.islam.newsfeeder.ui.article_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

public class ArticlesDetailsViewModel extends ViewModel {

    private final MutableLiveData<Article> articleData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> shouldOpenCustomTab = new SingleLiveEvent<>();

    public void init(Article article) {
        articleData.setValue(article);
    }

    public LiveData<Article> getArticleData() {
        return articleData;
    }

    public LiveData<Boolean> getShouldOpenCustomTab() {
        return shouldOpenCustomTab;
    }

    public void setShouldOpenCustomTab(boolean b) {
        shouldOpenCustomTab.setValue(true);
    }
}
