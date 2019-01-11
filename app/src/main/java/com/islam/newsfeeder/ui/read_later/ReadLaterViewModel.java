package com.islam.newsfeeder.ui.read_later;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.ReadLaterArticle;
import com.islam.newsfeeder.data.pocket.PocketRepository;

import java.util.List;

public class ReadLaterViewModel extends ViewModel {


    private final PocketRepository mRepository;
    private LiveData<String> onRequestTokenReceived;
    private final MutableLiveData<Boolean> shouldShowPocketSignInLayout = new MutableLiveData<>();
    private MutableLiveData<Boolean> onAccessTokenReceived = new MutableLiveData<>();
    private LiveData<Resource<List<ReadLaterArticle>>> articlesList;

    public ReadLaterViewModel(PocketRepository mArticleRepository) {
        mRepository = mArticleRepository;
    }


    public void loginAtPocket() {
        onRequestTokenReceived = mRepository.login();
    }

    public void init(String accessToken) {
        //if the user has not signed in yet
        if (accessToken == null) {
            shouldShowPocketSignInLayout.setValue(true);
            mRepository.setAccessTokenListner(onAccessTokenReceived);
            return;
        }
        //if the access token is already exit start observe the articles live data
        onAccessTokenReceived.setValue(true);


    }

    public void loadArticles() {
        if (articlesList != null)
            return;
        articlesList = mRepository.fetchReadLaterArticles();
    }

    //------------------------
    //setters and getters

    public LiveData<String> getOnRequestTokenReceived() {
        return onRequestTokenReceived;
    }

    public LiveData<Resource<List<ReadLaterArticle>>> getArticlesList() {
        return articlesList;
    }

    public LiveData<Boolean> getShouldShowPocketSignInLayout() {
        return shouldShowPocketSignInLayout;
    }

    public void setShouldShowPocketSignInLayout(Boolean b) {
        shouldShowPocketSignInLayout.setValue(b);
    }

    public LiveData<Boolean> getOnAccessTokenReceived() {
        return onAccessTokenReceived;
    }
}
