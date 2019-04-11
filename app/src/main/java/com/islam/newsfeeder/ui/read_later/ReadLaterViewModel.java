package com.islam.newsfeeder.ui.read_later;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.pojo.network.Resource;
import com.islam.newsfeeder.pojo.ReadLaterArticle;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import java.util.List;

public class ReadLaterViewModel extends ViewModel {


    private final PocketRepository mRepository;

    //to determine to show sing in button or not if the user has not signed in bfore
    private final MutableLiveData<Boolean> shouldShowPocketSignInLayout = new MutableLiveData<>();
    //fired  request token api has been called and the code has been fetched so we can open the getpocket authentication
    private LiveData<String> onRequestTokenReceived;

    private MutableLiveData<Boolean> onAccessTokenReceived = new MutableLiveData<>();

    private LiveData<Resource<List<ReadLaterArticle>>> articlesList;

    private SingleLiveEvent<Boolean> shouldShowLoadingDialog = new SingleLiveEvent<>();

    public ReadLaterViewModel(PocketRepository mArticleRepository) {
        mRepository = mArticleRepository;
    }


    public void loginAtPocket() {
        //show loading dialog
        shouldShowLoadingDialog.setValue(true);
        onRequestTokenReceived = mRepository.logintoPocketService();

        //when data is fetched dismiss loading dialog
        onRequestTokenReceived = Transformations.map(onRequestTokenReceived, new Function<String, String>() {
            @Override
            public String apply(String input) {
                shouldShowLoadingDialog.setValue(false);
                return input;
            }
        });
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

    public LiveData<Boolean> getShouldShowLoadingDialog() {
        return shouldShowLoadingDialog;
    }


    public LiveData<Boolean> getOnAccessTokenReceived() {
        return onAccessTokenReceived;
    }
}
