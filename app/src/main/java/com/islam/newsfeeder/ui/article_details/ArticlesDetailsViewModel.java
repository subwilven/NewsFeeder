package com.islam.newsfeeder.ui.article_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Article;

public class ArticlesDetailsViewModel extends ViewModel {

    private final MutableLiveData<Article> articleData = new MutableLiveData<>();

    public void init(Article article) {
        articleData.setValue(article);
    }

    public LiveData<Article> getArticleData() {
        return articleData;
    }
}
