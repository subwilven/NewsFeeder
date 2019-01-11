package com.islam.newsfeeder.ui.read_later;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.ReadLaterArticle;
import com.islam.newsfeeder.data.pocket.PocketRepository;

import java.util.List;

public class ReadLaterViewModel extends ViewModel {


    private final PocketRepository mRepository;
    private LiveData<String> onRequestTokenReceived;
    private LiveData<Resource<List<ReadLaterArticle>>> articlesList;

    public ReadLaterViewModel(PocketRepository mArticleRepository) {
        mRepository = mArticleRepository;

    }

    public void loginAtPocket() {
        onRequestTokenReceived = mRepository.login();
    }

    public LiveData<String> getOnRequestTokenReceived() {
        return onRequestTokenReceived;
    }

    public LiveData<Resource<List<ReadLaterArticle>>> getArticlesList() {
        return articlesList;
    }

    public void init() {
        if (articlesList != null)
            return;
        articlesList = mRepository.fetchReadLaterArticles();
    }
}
